package com.nic.loganalysis.dto.dashboardDTO;

public class OSVersionStatsDTO {
    private String os;
    private String osVersion;
    private Long count;

    public OSVersionStatsDTO(String os, String osVersion, Long count) {
        this.os = os;
        this.osVersion = osVersion;
        this.count = count;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
