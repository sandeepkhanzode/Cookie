package com.quantcast.cookie;

import com.quantcast.cookie.cookie.csv.CSVReader;
import com.quantcast.cookie.model.CookieLineItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;

public class CookieCSVReaderTest {

    private static final Logger logger = LogManager.getLogger(CookieCSVReaderTest.class);

    @Test
    public void testEmptyCSVReader() {
        List<CookieLineItem> cookieLineItems = new CSVReader().readInputFile("emptycookie_log.csv");

        Assert.assertEquals(0, cookieLineItems.size());
    }

    @Test
    public void testCSVReader() {
        List<CookieLineItem> cookieLineItems = new CSVReader().readInputFile("cookie_log_extra.csv");

        Assert.assertEquals(27, cookieLineItems.size());
        cookieLineItems.forEach(cookieLineItem -> {
            logger.atDebug().log(cookieLineItem.getId());
        });
    }

    @Test
    public void testCSVReaderWithIncorrectData() {
        assertThrows( IllegalArgumentException.class, () -> {
            new CSVReader().readInputFile("noncookie_log.csv");
        } );
    }}
