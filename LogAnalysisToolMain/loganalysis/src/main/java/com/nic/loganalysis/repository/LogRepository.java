package com.nic.loganalysis.repository;

import com.nic.loganalysis.dto.dashboardDTO.CityCountryDTO;
import com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO;
import com.nic.loganalysis.dto.dashboardDTO.OSVersionStatsDTO;
import com.nic.loganalysis.model.LogEntry;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<LogEntry, Long> {

    // Country-wise stats
    @Query("SELECT new com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO(l.country, COUNT(l)) " +
            "FROM LogEntry l GROUP BY l.country")
    List<LabelCountDTO> getCountryStats();

    // City-wise stats grouped by country
    @Query("SELECT new com.nic.loganalysis.dto.dashboardDTO.CityCountryDTO(l.city, l.country, COUNT(l)) " +
            "FROM LogEntry l GROUP BY l.city, l.country")
    List<CityCountryDTO> getCityStats();

    @Query("SELECT new com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO(l.os, COUNT(l)) " +
            "FROM LogEntry l GROUP BY l.os")
    List<LabelCountDTO> getOSStats();

    // OS and OS version distribution
    @Query("SELECT new com.nic.loganalysis.dto.dashboardDTO.OSVersionStatsDTO(l.os, l.osVersion, COUNT(l)) " +
            "FROM LogEntry l GROUP BY l.os, l.osVersion")
    List<OSVersionStatsDTO> getOSVersionStats();

    // Hourly access trend

//    @Query("SELECT new com.nic.loganalysis.dto.dashboardDTO.HourlyHitsDTO(FUNCTION('HOUR', l.timestamp), COUNT(l)) " +
//            "FROM LogEntry l GROUP BY FUNCTION('HOUR', l.timestamp) ORDER BY FUNCTION('HOUR', l.timestamp)")
//    List<HourlyHitsDTO> getHourlyHits();

    @Query(value = "SELECT HOUR(timestamp) AS hour, COUNT(id) AS hits " +
            "FROM log_entry " +
            "GROUP BY HOUR(timestamp) " +
            "ORDER BY hour", nativeQuery = true)
    List<Object[]> getHourlyHits();
}