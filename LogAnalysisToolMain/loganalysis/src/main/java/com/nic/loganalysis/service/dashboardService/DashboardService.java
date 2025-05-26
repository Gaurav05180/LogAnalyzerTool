package com.nic.loganalysis.service.dashboardService;

import com.nic.loganalysis.dto.dashboardDTO.CityCountryDTO;
import com.nic.loganalysis.dto.dashboardDTO.HourlyHitsDTO;
import com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO;
import com.nic.loganalysis.dto.dashboardDTO.OSVersionStatsDTO;

import java.util.List;

public interface DashboardService {

    List<LabelCountDTO> getCountryStats();
    List<CityCountryDTO> getCityStats();
    List<LabelCountDTO> getOSStats();
    List<OSVersionStatsDTO> getOSVersionStats();
    List<HourlyHitsDTO> getHourlyHits();
}
