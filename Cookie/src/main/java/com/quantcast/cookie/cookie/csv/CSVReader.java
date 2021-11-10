package com.quantcast.cookie.cookie.csv;

import com.quantcast.cookie.model.CookieLineItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.quantcast.cookie.service.CookieConstants.COMMA;

public class CSVReader {

    private static final Logger logger = LogManager.getLogger(CSVReader.class);

    /**
     *
     * @param inputFilePath
     * @return List<CookieLineItem>
     */
    public List<CookieLineItem> readInputFile(String inputFilePath) {
        List<CookieLineItem> inputList = null;

        try(BufferedReader br = getFileFromResourceAsStream(inputFilePath)) {
            inputList = br.lines().skip(1).map(mapToCookieLog).collect(Collectors.toList());

        } catch (IOException e) {
            logger.atError().log("Exception in reading the cookie log CSV", e);
        }

        return inputList ;
    }

    private BufferedReader getFileFromResourceAsStream(final String fileName) {
        var inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("Cookie log file not found: " + fileName);
        }

        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private final Function<String, CookieLineItem> mapToCookieLog = (line) -> {
        String[] p = line.split(COMMA);

        if(p.length > 2) {
            throw new IllegalArgumentException("Incorrect CSV file for Cookie log");
        }

        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime parsedDate;

        try {
            parsedDate = ZonedDateTime.parse(p[1].trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Incorrect Date format for Cookie log");
        }
        var cookieLineItem = CookieLineItem.builder().id(p[0]).date(parsedDate).build();

        logger.atDebug().log(cookieLineItem.toString());
        return cookieLineItem;
    };
}