package de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten;

import java.util.LinkedList;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;

/**
 * 
 *@author L-26, Björn Wege, Jan Heymann, Omran Hassanzada
 *@version 2025.06.10
 */
public class Vormerkung
{

    private Medium _medium;
    private LinkedList<Kunde> _listVormerkungen;

    /**
     * Konstruktor der Vormerkungen.
     * @param kunde Einzelnder Kunde der Vorgemkert wird.
     * @param medium das Medium zur Vormerkung
     * @ensure medium != null
     */
    public Vormerkung(Kunde kunde, Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";
        assert kunde != null : "Vorbedingung verletzt: kunden != null";

        LinkedList<Kunde> lst = new LinkedList<Kunde>();
        lst.add(kunde);

        this._listVormerkungen = lst;
        this._medium = medium;
    }

    /**
     * Erstellt eine Vormerkung auf Medium mit Kunden.
     * @param medium das Medium zur Vormerkung
     * @param kunden Eine vordefinierte Liste von Vorgemerkten Kunden 
     * @require kunden != null
     * @require medium != null
     * @require !kunden.isEmpty()
     */
    public Vormerkung(LinkedList<Kunde> kunden, Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";
        assert kunden != null : "Vorbedingung verletzt: kunden != null";
        assert !kunden.isEmpty() : "Vorbedingung verletzt: !kunden.isEmpty()";
        this._listVormerkungen = kunden;
        this._medium = medium;
    }

    /**
     * Fügt einen Kunden der Vormerkungsliste hinzu, wenn weniger als 3 Einträge bereits vorhanden sind.
     * @param kunde Der Kunde.
     */
    public void kundeHinzufuegen(Kunde kunde)
    {
        assert !listeIstVoll() : "Vorbedingung verletzt: !listeIstVoll()";
        _listVormerkungen.add(kunde);
    }

    /**
     * Gibt den ersten vorgemerkten Kunden zurück, oder null wenn kein Kunde vorgemerkt ist.
     * @return erster vorgemerkter Kunde.
     */
    public Kunde getErsterKunde()
    {
        // isEmpty als Vorbedingung?
        return _listVormerkungen.peekFirst();
    }

    /**
     * Entfernt den ersten vorgemerkten Kunden.
     */
    public void erstenKundenEntfernen()
    {
        //peekFirst() != null als Vorbedingung?
        _listVormerkungen.removeFirst();
    }

    /**
     * Gibt einen Boolean-Wert zurück, ob ein Kunde bereits vorgemerkt ist.
     * @param kunde Prüft ob der angegebene Kunde bereits vorgemerkt ist.
     * @return 
     */
    public boolean containsKunde(Kunde kunde)
    {
        //
        return _listVormerkungen.contains(kunde);
    }

    /**
     * Gibt die Gesamte Kundenliste zurück.
     * @return Die interne Liste.
     */
    public LinkedList<Kunde> getListeVonKunden()
    {
        return (LinkedList<Kunde>) _listVormerkungen.clone();
    }

    /**
     * 
     */
    public Medium getMedium()
    {
        return _medium;
    }

    /**
     * 
     * @return 
     */
    public boolean listeIstVoll()
    {
        return _listVormerkungen.size() >= 3;
    }

    /**
     * 
     * @param i
     * @return
     */
    private Kunde getKunde(int i)
    {
        return _listVormerkungen.get(i);
    }
}
