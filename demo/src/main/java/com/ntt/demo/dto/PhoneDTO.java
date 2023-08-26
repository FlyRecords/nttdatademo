package com.ntt.demo.dto;

public class PhoneDTO {
    private Integer id;
    private String number;
    private String citycode;
    private String countrycode;

    public PhoneDTO(String s, Object o, String s1) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public PhoneDTO(Integer id, String number, String cytycode, String countrycode) {
        this.id = id;
        this.number = number;
        this.citycode = cytycode;
        this.countrycode = countrycode;
    }

    public PhoneDTO() {
    }
}
