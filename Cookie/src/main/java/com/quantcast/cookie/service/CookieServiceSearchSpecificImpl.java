package com.quantcast.cookie.service;

import com.quantcast.cookie.cookie.csv.CSVReader;
import com.quantcast.cookie.model.CookieLineItem;
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
public class CookieServiceSearchSpecificImpl implements CookieService {
    private static final Logger logger = LogManager.getLogger(CookieServiceSearchSpecificImpl.class);

    /**
     *  @param fileName
     * @param date
     * @return
     */
    public List<String> findMostActiveCookie(String fileName, ZonedDateTime date) {
        var csvReader = new CSVReader();
        var allCookieLineItems = csvReader.readInputFile(fileName);

        if(allCookieLineItems.isEmpty()) {
            throw new IllegalArgumentException("Empty Cookie Log File!");
        }

        List<String> cookieIds = new ArrayList<>();
        List<CookieLineItem> cookieLineItems = getSpecificDate(allCookieLineItems, date);

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

    private List<CookieLineItem> getSpecificDate(List<CookieLineItem> allCookieLineItems, ZonedDateTime date) {
        var start = 0;
        var end = allCookieLineItems.size() - 1;

        int subListStart = 0, subListEnd = 0, middle = -1;

        while(start < end) {
            middle = start + (end - start)/2;

            if(allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                    .isBefore(date.truncatedTo(ChronoUnit.DAYS).toInstant()) ||
                    allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                            .equals(date.truncatedTo(ChronoUnit.DAYS).toInstant())) {

                if((middle == 0) || (allCookieLineItems.get(middle - 1).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                        .isAfter(date.truncatedTo(ChronoUnit.DAYS).toInstant()) &&
                        allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                                .equals(date.truncatedTo(ChronoUnit.DAYS).toInstant()))) {
                    break;
                }

                end = middle - 1;

            } else if (allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                    .isAfter(date.truncatedTo(ChronoUnit.DAYS).toInstant())) {

                start = middle + 1;
            }
        }

        subListStart = start == end ? start : middle; start = 0; end = allCookieLineItems.size() - 1;


        while(start < end) {
            middle = start + (end - start)/2;

            if(allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                    .isAfter(date.truncatedTo(ChronoUnit.DAYS).toInstant()) ||
                    allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                            .equals(date.truncatedTo(ChronoUnit.DAYS).toInstant())) {

                if((middle == allCookieLineItems.size() - 1) || (allCookieLineItems.get(middle + 1).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                        .isBefore(date.truncatedTo(ChronoUnit.DAYS).toInstant()) &&
                        allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                                .equals(date.truncatedTo(ChronoUnit.DAYS).toInstant()))) {
                    break;
                }

                start = middle + 1;

            } else if (allCookieLineItems.get(middle).getDate().truncatedTo(ChronoUnit.DAYS).toInstant()
                    .isBefore(date.truncatedTo(ChronoUnit.DAYS).toInstant())) {

                end = middle - 1;
            }
        }

        subListEnd = start == end ? start : middle;

        return allCookieLineItems.subList(subListStart, subListEnd == allCookieLineItems.size() - 1 ?
                allCookieLineItems.size() : subListEnd + 1);
    }
}