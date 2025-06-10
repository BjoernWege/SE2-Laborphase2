package de.uni_hamburg.informatik.swt.se2.mediathek.ui.vormerken;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ServiceObserver;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.SubWerkzeugObserver;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.subwerkzeuge.kundenauflister.KundenauflisterWerkzeug;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.subwerkzeuge.kundendetailanzeiger.KundenDetailAnzeigerWerkzeug;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.subwerkzeuge.mediendetailanzeiger.MedienDetailAnzeigerWerkzeug;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.subwerkzeuge.vormerkmedienauflister.VormerkMedienauflisterWerkzeug;

/**
 * Ein VormerkWerkzeug stellt die Funktionalität des Vormerkens für die
 * Benutzungsoberfläche bereit. Die UI wird durch die VormerkUI gestaltet.
 * 
 * @author SE2-Team
 * @version SoSe 2021
 */
public class VormerkWerkzeug
{

    /**
     * Die UI-Komponente der Ausleihe.
     */
    private final VormerkUI _vormerkUI;

    /**
     * Der Service zum Ausleihen von Medien.
     */
    private final VerleihService _verleihService;

    /**
     * Das Sub-Werkzeug zum Darstellen und Selektieren der Kunden.
     */
    private KundenauflisterWerkzeug _kundenAuflisterWerkzeug;

    /**
     * Das Sub-Werkzeug zum Darstellen und Selektieren der Medien.
     */
    private VormerkMedienauflisterWerkzeug _medienAuflisterWerkzeug;

    /**
     * Das Sub-Werkzeug zum Anzeigen der Details der selektieten Medien.
     */
    private MedienDetailAnzeigerWerkzeug _medienDetailAnzeigerWerkzeug;

    /**
     * Das Sub-Werkzeug zum Anzeigen der Details des selektieten Kunden.
     */
    private KundenDetailAnzeigerWerkzeug _kundenDetailAnzeigerWerkzeug;

    /**
     * Initialisiert ein neues VormerkWerkzeug. Es wird die Benutzungsoberfläche
     * mit den Ausleihaktionen erzeugt, Beobachter an den Services registriert
     * und die anzuzeigenden Kunden und Medien gesetzt.
     * 
     * @param medienbestand Der Medienbestand.
     * @param kundenstamm Der Kundenstamm.
     * @param verleihService Der Verleih-Service.
     * 
     * @require medienbestand != null
     * @require kundenstamm != null
     * @require verleihService != null
     */
    public VormerkWerkzeug(MedienbestandService medienbestand,
            KundenstammService kundenstamm, VerleihService verleihService)
    {
        assert medienbestand != null : "Vorbedingung verletzt: medienbestand != null";
        assert kundenstamm != null : "Vorbedingung verletzt: kundenstamm != null";
        assert verleihService != null : "Vorbedingung verletzt: verleihService != null";

        _verleihService = verleihService;

        // Subwerkzeuge erstellen
        _kundenAuflisterWerkzeug = new KundenauflisterWerkzeug(kundenstamm);
        _medienAuflisterWerkzeug = new VormerkMedienauflisterWerkzeug(
                medienbestand, verleihService);
        _medienDetailAnzeigerWerkzeug = new MedienDetailAnzeigerWerkzeug();
        _kundenDetailAnzeigerWerkzeug = new KundenDetailAnzeigerWerkzeug();

        // UI wird erzeugt.
        _vormerkUI = new VormerkUI(_kundenAuflisterWerkzeug.getUIPanel(),
                _medienAuflisterWerkzeug.getUIPanel(),
                _kundenDetailAnzeigerWerkzeug.getUIPanel(),
                _medienDetailAnzeigerWerkzeug.getUIPanel());

        // Beobachter erzeugen und an den Services registrieren
        registriereServiceBeobachter();

        // Beobachter erzeugen und an den Subwerkzeugen registrieren
        registriereSubWerkzeugBeobachter();

        // Die Vormerkaktionen werden erzeugt und an der UI registriert.
        registriereUIAktionen();
    }

    /**
     * Registriert die Aktionen, die bei benachrichtigungen der Services
     * ausgeführt werden.
     */
    private void registriereServiceBeobachter()
    {
        registriereVormerkButtonAktualisierenAktion();
    }

    /**
     * Registriert die Aktionen, die bei bestimmten Änderungen in Subwerkzeugen
     * ausgeführt werden.
     */
    private void registriereSubWerkzeugBeobachter()
    {
        registriereKundenAnzeigenAktion();
        registriereMedienAnzeigenAktion();
    }

    /**
     * Registriert die Aktionen, die bei bestimmten UI-Events ausgeführt werden.
     */
    private void registriereUIAktionen()
    {
        registriereVormerkAktion();
    }

    /**
     * Registriert die Aktion zur Aktualisierung des Vormerkbuttons, wenn eine
     * Benachrichtigung vom Verleihservice auftaucht.
     */
    private void registriereVormerkButtonAktualisierenAktion()
    {
        _verleihService.registriereBeobachter(new ServiceObserver()
        {

            @Override
            public void reagiereAufAenderung()
            {
                aktualisiereVormerkButton();
            }
        });
    }

    /**
     * Registriert die Aktion, die ausgeführt wird, wenn auf den Vormerk-Button
     * gedrückt wird.
     */
    private void registriereVormerkAktion()
    {
        _vormerkUI.getVormerkenButton()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    merkeAusgewaehlteMedienVor();
                }
            });
    }

    /**
     * Registiert die Aktion, die ausgeführt wird, wenn ein Kunde ausgewählt
     * wird.
     */
    private void registriereKundenAnzeigenAktion()
    {
        _kundenAuflisterWerkzeug.registriereBeobachter(new SubWerkzeugObserver()
        {
            @Override
            public void reagiereAufAenderung()
            {
                zeigeAusgewaehltenKunden();
                aktualisiereVormerkButton();
            }
        });
    }

    /**
     * Registiert die Aktion, die ausgeführt wird, wenn ein Medium ausgewählt
     * wird.
     */
    private void registriereMedienAnzeigenAktion()
    {
        _medienAuflisterWerkzeug.registriereBeobachter(new SubWerkzeugObserver()
        {

            @Override
            public void reagiereAufAenderung()
            {
                zeigeAusgewaehlteMedien();
                aktualisiereVormerkButton();
            }
        });
    }

    /**
     * Überprüft, ob ein Kunde selektiert ist und ob die ausgewählten Medien
     * für diesen Kunden vorgemerkt werden können.
     * 
     * Dabei wird geprüft:
     * - Ein Kunde ist ausgewählt.
     * - Mindestens ein Medium ist ausgewählt.
     * - Für alle ausgewählten Medien darf der Kunde laut Geschäftslogik vorgemerkt werden.
     * 
     * @return true, wenn vormerken möglich ist, sonst false.
     * 
     * @require _kundenAuflisterWerkzeug != null
     * @require _medienAuflisterWerkzeug != null
     * @ensure result == true <==> alle Bedingungen sind erfüllt
     */
    private boolean istVormerkenMoeglich()
    {
        List<Medium> medien = _medienAuflisterWerkzeug.getSelectedMedien();
        Kunde kunde = _kundenAuflisterWerkzeug.getSelectedKunde();

        // TODO für Aufgabenblatt 6 (nicht löschen): Prüfung muss noch eingebaut
        // werden. Ist dies korrekt implementiert, wird der Vormerk-Button gemäß
        // der Anforderungen a), b), c) und e) aktiviert.

        assert _kundenAuflisterWerkzeug != null : "Vorbedingung verletzt: _kundenAuflisterWerkzeug != null";
        assert _medienAuflisterWerkzeug != null : "Vorbedingung verletzt: _medienAuflisterWerkzeug != null";

        if (kunde == null || medien.isEmpty())
        {
            return false;
        }

        for (Medium medium : medien)
        {
            if (!_verleihService.istVormerkenMoeglich(kunde, medium))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Merkt die ausgewählten Medien für den aktuell selektierten Kunden vor.
     * Diese Methode wird über einen Listener angestoßen, wenn der Benutzer
     * auf den Vormerk-Button klickt.
     *
     * Es werden nur solche Medien vorgemerkt, für die der Kunde laut Logik
     * vorgemerkt werden darf (max. 3 Vormerker, nicht Entleiher, nicht doppelt).
     *
     * @require _kundenAuflisterWerkzeug.getSelectedKunde() != null
     * @require !_medienAuflisterWerkzeug.getSelectedMedien().isEmpty()
     */
    private void merkeAusgewaehlteMedienVor()
    {
        Kunde kunde = _kundenAuflisterWerkzeug.getSelectedKunde();
        List<Medium> medien = _medienAuflisterWerkzeug.getSelectedMedien();

        assert kunde != null : "Vorbedingung verletzt: Kunde darf nicht null sein.";
        assert !medien
            .isEmpty() : "Vorbedingung verletzt: Medienliste darf nicht leer sein.";

        for (Medium medium : medien)
        {
            if (_verleihService.istVormerkenMoeglich(kunde, medium))
            {
                _verleihService.merkeVor(kunde, medium);
            }
        }

        // Aktualisiere Detailanzeigen nach erfolgreicher Vormerkung
        _medienDetailAnzeigerWerkzeug.setMedien(medien);
        _kundenDetailAnzeigerWerkzeug.setKunde(kunde);
    }

    /**
     * Zeigt die Details der ausgewählten Medien.
     */
    private void zeigeAusgewaehlteMedien()
    {
        List<Medium> selectedMedien = _medienAuflisterWerkzeug
            .getSelectedMedien();
        _medienDetailAnzeigerWerkzeug.setMedien(selectedMedien);
    }

    /**
     * Zeigt die Details des ausgewählten Kunden (rechts im Fenster)
     */
    private void zeigeAusgewaehltenKunden()
    {
        Kunde kunde = _kundenAuflisterWerkzeug.getSelectedKunde();
        _kundenDetailAnzeigerWerkzeug.setKunde(kunde);
    }

    /**
     * Setzt den Ausleihbutton auf benutzbar (enabled) falls die gerade
     * selektierten Medien alle ausgeliehen werden können und ein Kunde
     * ausgewählt ist.
     * 
     * Wenn keine Medien selektiert sind oder wenn mindestes eines der
     * selektierten Medien bereits ausgeliehen ist oder wenn kein Kunde
     * ausgewählt ist, wird der Button ausgegraut.
     */
    private void aktualisiereVormerkButton()
    {
        boolean istVormerkenMoeglich = istVormerkenMoeglich();
        _vormerkUI.getVormerkenButton()
            .setEnabled(istVormerkenMoeglich);
    }

    /**
     * Gibt das Panel, dass die UI-Komponente darstellt zurück.
     * 
     * @ensure result != null
     */
    public JPanel getUIPanel()
    {
        return _vormerkUI.getUIPanel();
    }
}
