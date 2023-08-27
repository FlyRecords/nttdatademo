package com.ntt.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@NamedQueries({
        @NamedQuery(name = "Phone.findAll", query = "select p from Phone p"),
        @NamedQuery(name = "Phone.findById", query = "select p from Phone p where p.id = :id")
})

public class Phone{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number")
    private String number;

    @Column(name = "citycode")
    private String cityCode;

    @Column(name = "contrycode")
    private String contryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario")
    private Usuario usuario;


    public Phone() {
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public Usuario getUsuarios() {
        return usuario;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuario = usuarios;
    }

    public Phone(Integer id, String number, String cityCode, String contryCode, Usuario usuarios) {
        this.id = id;
        this.number = number;
        this.cityCode = cityCode;
        this.contryCode = contryCode;
        this.usuario = usuarios;
    }
}
