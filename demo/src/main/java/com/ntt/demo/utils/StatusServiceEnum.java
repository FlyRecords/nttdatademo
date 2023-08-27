package com.ntt.demo.utils;

public enum StatusServiceEnum {
    EXITO_SERVICE("EXITO"),
    DATOS_INCORRECTOS("VALIDACIONES GATILLADAS"),
    CATCH_SERVICE("CAIMOS EN EL CATCH");

    private String mensajeStatus;

    public String getMensajeStatus() {
        return mensajeStatus;
    }

    public void setMensajeStatus(String mensajeStatus) {
        this.mensajeStatus = mensajeStatus;
    }

    StatusServiceEnum(String mensajeStatus) {
        this.mensajeStatus = mensajeStatus;
    }
}
