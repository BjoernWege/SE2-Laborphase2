package de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte;

import org.junit.Test;

import static org.junit.Assert.*;

public class PLZTest
{

    @Test
    public void constructorTest()
    {
        PLZ plz1 = new PLZ("22761");
        PLZ plz2 = new PLZ("D-22760");
        assertEquals("22761", plz1.toString());
        assertEquals("D-22760", plz2.toString());
        assertNotSame(plz1, plz2);
    }

    @Test
    public void equalsUndHashCodeTest()
    {
        PLZ plz1 = new PLZ("22761");
        PLZ plz2 = new PLZ("D-22761");

        assertEquals(plz1, new PLZ("22761"));
        assertEquals(plz1, plz2);
        assertEquals(plz1.hashCode(), plz2.hashCode());

        PLZ plz3 = new PLZ("22760");
        assertNotEquals(plz1, plz3);
        assertNotEquals(plz1.hashCode(), plz3.hashCode());
    }
}
