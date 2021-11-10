package com.quantcast.cookie;

import com.quantcast.cookie.service.CookieService;
import com.quantcast.cookie.service.CookieServiceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.quantcast.cookie.service.CookieConstants.*;
import static com.quantcast.cookie.service.CookieConstants.UTC;

/**
 *
 */
public class CookieClient {

    private static final Logger logger = LogManager.getLogger(CookieClient.class);

    /**
     *
     * @param args
     */
    public void invokeGetMostActiveCookies(String[] args) {
        String fileName = null;
        ZonedDateTime date = null;

        for(int i=0; i<args.length-1; i++) {
            switch (args[i]) {
                case FILENAME:
                    fileName = args[i+1].trim(); break;
                case DATE:
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
                    LocalDate localDate = LocalDate.parse(args[i+1].trim(), formatter);
                    date = localDate.atStartOfDay(ZoneId.of(UTC));
                    break;
            }
        }

        List<String> cookies = CookieService.getInstance(CookieServiceType.SEARCH_SPECIFIC).findMostActiveCookie(fileName, date);
        cookies.forEach(cookieId -> logger.atInfo().log(cookieId));
    }
}
