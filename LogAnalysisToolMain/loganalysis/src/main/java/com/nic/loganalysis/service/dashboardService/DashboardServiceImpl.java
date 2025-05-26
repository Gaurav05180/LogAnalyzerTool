package com.nic.loganalysis.service.dashboardService;

import com.nic.loganalysis.dto.dashboardDTO.CityCountryDTO;
import com.nic.loganalysis.dto.dashboardDTO.HourlyHitsDTO;
import com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO;
import com.nic.loganalysis.dto.dashboardDTO.OSVersionStatsDTO;
import com.nic.loganalysis.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public List<LabelCountDTO> getCountryStats() {
        return logRepository.getCountryStats();
    }

    @Override
    public List<CityCountryDTO> getCityStats() {
        return logRepository.getCityStats();
    }

    public List<LabelCountDTO> getOSStats() {
        return logRepository.getOSStats();  // group by os
    }

    public List<OSVersionStatsDTO> getOSVersionStats() {
        return logRepository.getOSVersionStats();  // group by os + version
    }

    public List<HourlyHitsDTO> getHourlyHits() {
        List<Object[]> rawData = logRepository.getHourlyHits();
        List<HourlyHitsDTO> result = new ArrayList<>();

        for (Object[] row : rawData) {
            int hour = ((Number) row[0]).intValue();     // MySQL returns numeric types as Number
            long hits = ((Number) row[1]).longValue();
            result.add(new HourlyHitsDTO(hour, hits));
        }

        return result;
    }
}