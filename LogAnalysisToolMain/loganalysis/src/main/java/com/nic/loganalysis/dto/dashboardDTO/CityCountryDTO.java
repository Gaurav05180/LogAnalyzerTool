package com.nic.loganalysis.dto.dashboardDTO;

public class CityCountryDTO {
    private String city;
    private String country;
    private Long count;

    public CityCountryDTO(String city, String country, Long count) {
        this.city = city;
        this.country = country;
        this.count = count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
