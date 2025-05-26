package com.nic.loganalysis.dto.dashboardDTO;

public class HourlyHitsDTO {
    private int hour;
    private Long count;

    public HourlyHitsDTO(int hour, Long count) {
        this.hour = hour;
        this.count = count;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
