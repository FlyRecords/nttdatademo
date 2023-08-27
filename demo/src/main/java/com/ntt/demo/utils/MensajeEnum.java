package com.ntt.demo.utils;

public enum MensajeEnum {
    ENTREGA_VALIDACIONES("ENTREGA DE VALIDACIONES"),
    CREDENCIALES_INCORRECTAS("EMAIL Y/O CONTRASEÑA NO COINCIDEN"),
    SN_USUARIOS_REGISTRADOS("NINGÚN USUARIO REGISTRADO"),
    US_INEXISTENTE("USUARIO NO INEXISTENTE");

    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    MensajeEnum(String mensaje) {
        this.mensaje = mensaje;
    }
}
