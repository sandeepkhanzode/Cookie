package com.quantcast.cookie;

import com.quantcast.cookie.service.CookieService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.quantcast.cookie.service.CookieConstants.*;

/**
 * Cookie Log - Active Analyzer
 *
 */
public class App {
    public static void main(String[] args ) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Insufficient arguments! Use -f <cookie_log_extended.csv> -d <yyyy-MM-dd> to start ...");
        }

        new CookieClient().invokeGetMostActiveCookies(args);
    }
}