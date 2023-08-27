package com.ntt.demo.utils;

public enum TokenStatusEnum {

    TOKEN_ACTUALIZADO ("TOKEN ACTUALIZADO!"),
    TOKEN_CREADO("TOKEN CREADO!"),
    TOKEN_INVALIDO("TOKEN INVALIDO!");

    private String mensajeToken;

    TokenStatusEnum(String mensajeToken) {
        this.mensajeToken = mensajeToken;
    }

    public String getMensajeToken() {
        return mensajeToken;
    }

    public void setMensajeToken(String mensajeToken) {
        this.mensajeToken = mensajeToken;
    }
}
