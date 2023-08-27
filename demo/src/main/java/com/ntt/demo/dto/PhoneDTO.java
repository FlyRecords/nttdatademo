package com.ntt.demo.dto;

public class PhoneDTO {
    private Integer id;
    private String number;
    private String citycode;
    private String contrycode;

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

    public String getContrycode() {
        return contrycode;
    }

    public void setContrycode(String contrycode) {
        this.contrycode = contrycode;
    }

    public PhoneDTO(Integer id, String number, String cytycode, String contrycode) {
        this.id = id;
        this.number = number;
        this.citycode = cytycode;
        this.contrycode = contrycode;
    }

    public PhoneDTO() {
    }
}
