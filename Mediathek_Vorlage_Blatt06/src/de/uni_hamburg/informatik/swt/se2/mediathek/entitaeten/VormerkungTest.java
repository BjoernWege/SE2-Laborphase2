package de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Kundennummer;

/**
 * Unit-Tests für die Klasse Vormerkung.
 */
public class VormerkungTest
{

    private Kunde k1, k2, k3, k4;
    private Medium medium;
    private Vormerkung v1, v2;

    /**
     * Testdaten initialisieren: 1 Medium, 4 Kunden, 1 Vormerkungen
     */
    @Before
    public void setUp()
    {
        k1 = new Kunde(new Kundennummer(111111), "Hans", "Hansen");
        k2 = new Kunde(new Kundennummer(222222), "Marie", "Meyer");
        k3 = new Kunde(new Kundennummer(333333), "Peter", "Petersen");
        k4 = new Kunde(new Kundennummer(444444), "Anna", "Schmidt");

        medium = new CD("ID1", "Bezeichnung", "Titel", 2025);

        v1 = new Vormerkung(k1, medium);
    }

    /**
     * Testet den Anfangszustand einer neuen Vormerkung.
     */
    @Test
    public void testInitialzustandEinKunde()
    {
        //assert(actual,expedcted)
        assertEquals(v1.getListeVonKunden()
            .size(), 1);
        assertTrue(v1.containsKunde(k1));
        assertFalse(v1.listeIstVoll());
        assertEquals(v1.getErsterKunde(), k1);
    }

    public void testInitialzustandListeKunden()
    {
        LinkedList<Kunde> lst = new LinkedList<Kunde>();
        lst.add(k1);
        lst.add(k2);

        Vormerkung v2 = new Vormerkung(lst, medium);

        assertEquals(v2.getListeVonKunden()
            .size(), 2);
        assertTrue(v2.containsKunde(k1));
        assertTrue(v2.containsKunde(k2));
        assertFalse(v2.listeIstVoll());
        assertEquals(v2.getErsterKunde(), k1);
    }

    /**
     * Testet das erfolgreiche Hinzufügen eines Kunden zur Vormerkung.
     */
    @Test
    public void testEinKundeHinzufuegen()
    {
        v1.kundeHinzufuegen(k2);
        assertEquals(v1.getErsterKunde(), k1);
        assertEquals(v1.getListeVonKunden()
            .size(), 2);
        assertEquals(v1.getListeVonKunden()
            .get(1), k2);
    }

    /**
     * Tested die containsKunde(Kunde) Methode
     */
    @Test
    public void testVormerkungContains()
    {
        v1.kundeHinzufuegen(k2);

        assertTrue(v1.containsKunde(k1));
        assertTrue(v1.containsKunde(k2));
        assertFalse(v1.containsKunde(k3));
        assertFalse(v1.containsKunde(k4));
    }

    /**
     * Testet, dass das Hinzufügen eines null-Kunden eine AssertionError wirft.
     */
    @Test(expected = AssertionError.class)
    public void testKundeHinzufuegenNull()
    {
        try
        {
            v1.kundeHinzufuegen(null);
            fail();
        }
        catch (Exception e)
        {
        }
        assertEquals(v1.getErsterKunde(), k1);
    }

    /**
     * Testet, dass nicht mehr als 3 Kunden vorgemerkt werden können.
     */
    @Test
    public void testMaximalDreiEintraege()
    {
        v1.kundeHinzufuegen(k2);
        v1.kundeHinzufuegen(k3);

        assertTrue(v1.listeIstVoll());
        assertEquals(v1.getListeVonKunden()
            .size(), 3);

        try
        {
            v1.kundeHinzufuegen(k4);
            fail("Trotz voller Liste wurde kein AssertionError geworfen");
        }
        catch (AssertionError e)
        {
        }
    }

    /**
     * Testet das Entfernen des ersten Kunden in der Liste.
     */
    @Test
    public void testErstenKundenEntfernen()
    {
        v1.kundeHinzufuegen(k1);
        v1.kundeHinzufuegen(k2);

        assertTrue(v1.containsKunde(k1));
        assertTrue(v1.containsKunde(k2));
        assertEquals(v1.getErsterKunde(), k1);

        v1.erstenKundenEntfernen();
        assertTrue(v1.containsKunde(k2));
        assertFalse(v1.containsKunde(k1));
        assertEquals(v1.getErsterKunde(), k2);
    }

    /**
     * Testet, dass getListeVonKunden eine Kopie zurückliefert.
     */
    @Test
    public void testGetListeVonKundenGibtKopieZurueck()
    {
        v1.kundeHinzufuegen(k1);
        List<Kunde> kopie = v1.getListeVonKunden();
        kopie.clear();
        assertTrue(v1.containsKunde(k1));
        assertEquals(v1.getListeVonKunden()
            .size(), 1);
    }

    /**
     * Entfernen mehr Vormerkungen als vorhanden.
     */
    @Test
    public void testErstenKundenEntfernenAufLeer()
    {
        v1.kundeHinzufuegen(k1);

        v1.erstenKundenEntfernen();
        assertEquals(v1.getListeVonKunden()
            .size(), 0);
        assertNull(v1.getErsterKunde());
        try
        {
            v1.erstenKundenEntfernen();
            fail("Kein Kunde vorgemerkt, es sollte ein AssertionError geben.");
        }
        catch (Exception e)
        {
        }
    }

}
