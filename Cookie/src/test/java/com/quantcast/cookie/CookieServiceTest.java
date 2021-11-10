package com.quantcast.cookie;

import com.quantcast.cookie.service.CookieService;
import com.quantcast.cookie.service.CookieServiceType;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.quantcast.cookie.service.CookieConstants.DATE_PATTERN;
import static com.quantcast.cookie.service.CookieConstants.UTC;

public class CookieServiceTest {

    @Test
    public void testCookieServiceForDate1() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate localDate = LocalDate.parse("2018-12-09", formatter);

        List<String> mostActiveCookies = CookieService.getInstance(CookieServiceType.SEARCH_SPECIFIC).findMostActiveCookie("cookie_log_extra.csv",
                localDate.atStartOfDay(ZoneId.of(UTC)));

        Assert.assertEquals(1, mostActiveCookies.size());
        Assert.assertEquals("AtY0laUfhglK3lC7", mostActiveCookies.get(0));
    }

    @Test
    public void testCookieServiceForDate2() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate localDate = LocalDate.parse("2018-12-08", formatter);

        List<String> mostActiveCookies = CookieService.getInstance(CookieServiceType.SEARCH_SPECIFIC).findMostActiveCookie("cookie_log_extra.csv",
                localDate.atStartOfDay(ZoneId.of(UTC)));

        Assert.assertEquals(1, mostActiveCookies.size());
        Assert.assertEquals("4sMM2LxV07bPJzwf", mostActiveCookies.get(0));
    }
}
