package com.quantcast.cookie;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldThrowIllegalArgumentEx()
    {
        assertThrows( IllegalArgumentException.class, () -> {
            App.main(new String[] { "-f cookie_log_extended.csv", "-d", "2018-12-09" });
        } );
    }
}
