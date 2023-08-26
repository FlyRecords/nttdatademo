package com.ntt.demo.dto;

public class PhoneDTO {
    private Integer id;
    private String number;
    private String cytycode;
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

    public String getCytycode() {
        return cytycode;
    }

    public void setCytycode(String cytycode) {
        this.cytycode = cytycode;
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
        this.cytycode = cytycode;
        this.countrycode = countrycode;
    }

    public PhoneDTO() {
    }
}
