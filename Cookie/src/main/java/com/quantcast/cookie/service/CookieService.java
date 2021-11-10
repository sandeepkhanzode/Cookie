package com.quantcast.cookie.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface CookieService {

    Map<CookieServiceType, CookieService> map = Map.of(
            CookieServiceType.BASIC_MAP, new CookieServiceBasicMapImpl(),
            CookieServiceType.SEARCH_SPECIFIC, new CookieServiceSearchSpecificImpl()
    );

    static CookieService getInstance(CookieServiceType cookieServiceType) {
        return map.get(cookieServiceType);
    }

    List<String> findMostActiveCookie(String fileName, ZonedDateTime date);
}