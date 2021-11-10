package com.quantcast.cookie.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DailyCookiesEntity {

    private List<Cookie> mostActiveCookies;
    private Integer mostActiveCookieCount;

    private Map<String, Integer> cookieCounts;

    public void incrementCount(final String cookieId) {
        var count = cookieCounts.computeIfAbsent(cookieId, u -> 0);
        cookieCounts.put(cookieId, ++count);

        if(count >= mostActiveCookieCount) {
            if(count > mostActiveCookieCount) {
                mostActiveCookieCount = count;
                mostActiveCookies = new ArrayList<>();
            }
            mostActiveCookies.add(Cookie.builder().cookieId(cookieId).build());
        }
    }
}
