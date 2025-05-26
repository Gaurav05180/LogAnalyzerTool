package com.nic.loganalysis.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.nic.loganalysis.model.LogEntry;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Service
public class GeoLocationService {
    private final DatabaseReader dbReader;

    public GeoLocationService() throws IOException {
        InputStream databaseStream = getClass().getClassLoader().getResourceAsStream("geo/GeoLite2-City.mmdb");
        if (databaseStream == null) {
            throw new FileNotFoundException("GeoLite2-City.mmdb file not found in resources/geo folder");
        }
        this.dbReader = new DatabaseReader.Builder(databaseStream).build();
    }


    public void enrichLogEntry(LogEntry entry){
        String ipAddress = entry.getIpAddress();

        entry.setCity(getCity(ipAddress));
        entry.setCountry(getCountry(ipAddress));
    }

    public String getCountry(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            String country = response.getCountry().getName();
            if (country != null && !country.trim().isEmpty()) {
                return country.trim();
            }

        } catch (Exception e) {
            // Optionally log the error if needed
        }

        return "Unknown";
    }

    public String getCity(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            String city = response.getCity().getName();
            if (city != null && !city.trim().isEmpty()) {
                return city.trim();
            }
        } catch (Exception e) {
            // You can log this if needed for debugging
        }
        return "Unknown";
    }
}