package de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Verleihkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ServiceObserver;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Datum;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Kundennummer;

/**
 * @author SE2-Team
 */
public class VerleihServiceImplTest
{
    private Datum _datum;
    private Kunde _kunde;
    private VerleihService _service;
    private List<Medium> _medienListe;
    private Kunde _vormerkkunde;
    private Kunde _vormerkkunde1;
    private Kunde _vormerkkunde2;
    private Kunde _vormerkkunde3;

    public VerleihServiceImplTest()
    {
        _datum = new Datum(3, 4, 2009);
        KundenstammService kundenstamm = new KundenstammServiceImpl(
                new ArrayList<Kunde>());
        _kunde = new Kunde(new Kundennummer(123456), "ich", "du");

        _vormerkkunde = new Kunde(new Kundennummer(666999), "paul", "panter");
        _vormerkkunde1 = new Kunde(new Kundennummer(666999), "paul",
                "mccartney");
        _vormerkkunde2 = new Kunde(new Kundennummer(666999), "paul",
                "langsdorf");
        _vormerkkunde3 = new Kunde(new Kundennummer(666999), "paul",
                "burchert");

        kundenstamm.fuegeKundenEin(_kunde);
        kundenstamm.fuegeKundenEin(_vormerkkunde);
        kundenstamm.fuegeKundenEin(_vormerkkunde1);
        kundenstamm.fuegeKundenEin(_vormerkkunde2);
        kundenstamm.fuegeKundenEin(_vormerkkunde3);
        MedienbestandService medienbestand = new MedienbestandServiceImpl(
                new ArrayList<Medium>());
        Medium medium = new CD("CD1", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD2", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD3", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD4", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        _medienListe = medienbestand.getMedien();
        _service = new VerleihServiceImpl(kundenstamm, medienbestand,
                new ArrayList<Verleihkarte>());
    }

    @Test
    public void testeNachInitialisierungIstNichtsVerliehen() throws Exception
    {
        assertTrue(_service.getVerleihkarten()
            .isEmpty());
        assertFalse(_service.istVerliehen(_medienListe.get(0)));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertTrue(_service.sindAlleNichtVerliehen(_medienListe));
    }

    @Test
    public void testeVerleihUndRueckgabeVonMedien() throws Exception
    {
        // Lege eine Liste mit nur verliehenen und eine Liste mit ausschließlich
        // nicht verliehenen Medien an
        List<Medium> verlieheneMedien = _medienListe.subList(0, 2);
        List<Medium> nichtVerlieheneMedien = _medienListe.subList(2, 4);
        _service.verleiheAn(_kunde, verlieheneMedien, _datum);

        // Prüfe, ob alle sondierenden Operationen für das Vertragsmodell
        // funktionieren
        assertTrue(_service.istVerliehen(verlieheneMedien.get(0)));
        assertTrue(_service.istVerliehen(verlieheneMedien.get(1)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(1)));
        assertTrue(_service.sindAlleVerliehen(verlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleNichtVerliehen(verlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertFalse(_service.sindAlleNichtVerliehen(_medienListe));
        assertTrue(_service.istVerliehenAn(_kunde, verlieheneMedien.get(0)));
        assertTrue(_service.istVerliehenAn(_kunde, verlieheneMedien.get(1)));
        assertFalse(
                _service.istVerliehenAn(_kunde, nichtVerlieheneMedien.get(0)));
        assertFalse(
                _service.istVerliehenAn(_kunde, nichtVerlieheneMedien.get(1)));
        assertTrue(_service.sindAlleVerliehenAn(_kunde, verlieheneMedien));
        assertFalse(
                _service.sindAlleVerliehenAn(_kunde, nichtVerlieheneMedien));

        // Prüfe alle sonstigen sondierenden Methoden
        assertEquals(2, _service.getVerleihkarten()
            .size());

        _service.nimmZurueck(verlieheneMedien, _datum);
        // Prüfe, ob alle sondierenden Operationen für das Vertragsmodell
        // funktionieren
        assertFalse(_service.istVerliehen(verlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(verlieheneMedien.get(1)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(1)));
        assertFalse(_service.sindAlleVerliehen(verlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(nichtVerlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(verlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertTrue(_service.sindAlleNichtVerliehen(_medienListe));
        assertTrue(_service.getVerleihkarten()
            .isEmpty());
    }

    @Test
    public void testVerleihEreignisBeobachter() throws ProtokollierException
    {
        final boolean[] ereignisse = new boolean[1];
        ereignisse[0] = false;
        ServiceObserver beobachter = new ServiceObserver()
        {
            @Override
            public void reagiereAufAenderung()
            {
                ereignisse[0] = true;
            }
        };
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(0)), _datum);
        assertFalse(ereignisse[0]);

        _service.registriereBeobachter(beobachter);
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(1)), _datum);
        assertTrue(ereignisse[0]);

        _service.entferneBeobachter(beobachter);
        ereignisse[0] = false;
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(2)), _datum);
        assertFalse(ereignisse[0]);
    }

    @Test
    public void testVormerken() throws Exception
    {
        Medium m1 = _medienListe.get(0);
        List<Medium> m1l = Arrays.asList(new Medium[] {m1});

        //Zuerst lässt sich jemand Vormerken
        _service.merkeVor(_kunde, m1);
        assertTrue(
                "nachbedingung von merkeVor: istSchonVorgemerkt(kunde, medium)",
                _service.istSchonVorgemerkt(_kunde, m1));

        // es ist nicht möglich, zweimal den gleichen Kunden vorzumerken (a: unterschiedlich)
        assertFalse(_service.istVormerkenMoeglich(_kunde, m1));

        // dann lassen sich noch 2 Kunden Vormerken
        _service.merkeVor(_vormerkkunde, m1);
        _service.merkeVor(_vormerkkunde1, m1);

        // nur der erste darf noch das medium ausleihen (c)
        assertTrue(_service.istVerleihenMoeglich(_kunde, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde1, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde2, m1l));

        // es ist nicht möglich, dann noch eine Vormerkung zu machen (a)
        assertFalse(_service.istVormerkenMoeglich(_vormerkkunde2, m1));

        // wird ein Vorgemerktes Medium Ausgeliehen, wird der Kunde,
        // der das tut, aus der Vormerkung entfernt: (d)

        // er leiht m1 aus:
        _service.verleiheAn(_kunde, m1l, new Datum(24, 12, 2024)); // throws Exception

        // dann rückt _vormerkkunde nach, aber es ist nicht Möglich, zu leihen, weil _kunde das Medium hat
        assertFalse(_service.istVerleihenMoeglich(_kunde, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde, m1l));

        // der _kunde ist nicht mehr enthalten 
        assertFalse(_service.getVorgemerkteKunden(m1)
            .contains(_kunde));

        // es ist unabhängig von Verleihstatus möglich, Vorzumerken (b)
        assertTrue(_service.istVormerkenMoeglich(_vormerkkunde2, m1));
        // außer man ist der Ausleiher (e)
        assertFalse(_service.istVormerkenMoeglich(_kunde, m1));

        // der _kunde gibt m1 zurück
        _service.nimmZurueck(m1l, new Datum(31, 12, 2024));

        // jetzt darf nur _vormerkkunde das Medium ausleihen
        assertFalse(_service.istVerleihenMoeglich(_kunde, m1l));
        assertTrue(_service.istVerleihenMoeglich(_vormerkkunde, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde1, m1l));
        assertFalse(_service.istVerleihenMoeglich(_vormerkkunde2, m1l));

    }

}
