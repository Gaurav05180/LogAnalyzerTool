package com.nic.loganalysis.controller;

import com.nic.loganalysis.dto.dashboardDTO.CityCountryDTO;
import com.nic.loganalysis.dto.dashboardDTO.HourlyHitsDTO;
import com.nic.loganalysis.dto.dashboardDTO.LabelCountDTO;
import com.nic.loganalysis.dto.dashboardDTO.OSVersionStatsDTO;
import com.nic.loganalysis.service.dashboardService.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/country-stats")
    public List<LabelCountDTO> getCountryStats() {
        return dashboardService.getCountryStats();
    }

    @GetMapping("/city-stats")
    public List<CityCountryDTO> getCityStats() {
        return dashboardService.getCityStats();
    }

    @GetMapping("/os-stats")
    public List<LabelCountDTO> getOSStats() { return dashboardService.getOSStats(); }

    @GetMapping("/os-version-stats")
    public List<OSVersionStatsDTO> getOSVersionStats() {
        return dashboardService.getOSVersionStats();
    }

    @GetMapping("/hourly-hits")
    public List<HourlyHitsDTO> getHourlyHits() {
        return dashboardService.getHourlyHits();
    }
}