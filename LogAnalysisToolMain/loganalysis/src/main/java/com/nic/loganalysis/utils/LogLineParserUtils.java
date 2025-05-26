package com.nic.loganalysis.utils;

import com.nic.loganalysis.model.LogEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogLineParserUtils {

    public static LogEntry parseValidLogLine(String line){
        String[] parts = line.split("~", 4); // limit = 4 to avoid splitting user agent extra ~

        String timestampStr = parts[0].trim();
        String email = parts[1].trim();
        String ip = parts[2].trim();
        String userAgent = parts[3].trim();

        LogEntry entry = new LogEntry();

        entry.setEmail(email);
        entry.setIpAddress(ip);
        entry.setUserAgent(userAgent);

        // entry.setTimestamp()
        try{
            // Parse ISO-8601 format timestamp
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date timestamp = formatter.parse(timestampStr);
            entry.setTimestamp(timestamp);
        }
        catch (ParseException e){
            entry.setTimestamp(null);
        }

        return entry;
    }
}