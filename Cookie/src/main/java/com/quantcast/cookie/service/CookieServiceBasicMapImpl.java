package com.quantcast.cookie.service;

import com.quantcast.cookie.cookie.csv.CSVReader;
import com.quantcast.cookie.model.DailyCookiesEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CookieServiceBasicMapImpl implements CookieService {
    private static final Logger logger = LogManager.getLogger(CookieServiceBasicMapImpl.class);

    /**
     *  @param fileName
     * @param date
     * @return
     */
    public List<String> findMostActiveCookie(String fileName, ZonedDateTime date) {
        var csvReader = new CSVReader();
        var cookieLineItems = csvReader.readInputFile(fileName);

        if(cookieLineItems.isEmpty()) {
            throw new IllegalArgumentException("Empty Cookie Log File!");
        }

        List<String> cookieIds = new ArrayList<>();

        Map<ZonedDateTime, DailyCookiesEntity> timedMap = new HashMap<>();
        cookieLineItems.forEach(cookieLineItem -> {
            DailyCookiesEntity dailyCookiesEntity = timedMap
                .computeIfAbsent(cookieLineItem.getDate().truncatedTo(ChronoUnit.DAYS), zonedDateTime ->
                        DailyCookiesEntity.builder()
                            .cookieCounts(new HashMap<>()).mostActiveCookieCount(0).build());

            dailyCookiesEntity.incrementCount(cookieLineItem.getId());
        });


        timedMap.forEach((zonedDateTime, dailyCookiesEntity) -> {
            dailyCookiesEntity.getCookieCounts().forEach((s, integer) -> {
                logger.atDebug().log("Entry: " + zonedDateTime + " : " + s + " : " + integer);
            });
        });

        timedMap.forEach((zonedDateTime, dailyCookiesEntity) -> {
            if(zonedDateTime.truncatedTo(ChronoUnit.DAYS).toInstant()
                    .equals(date.truncatedTo(ChronoUnit.DAYS).toInstant())) {

                dailyCookiesEntity.getMostActiveCookies().forEach(cookie -> {
                    cookieIds.add(cookie.getCookieId());
                });
            }
        });

        return cookieIds;
    }
}