package com.quantcast.cookie.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@Builder
@ToString
public class CookieLineItem {

    private String id;
    private ZonedDateTime date;
}
