package com.ntt.demo.dao;

import com.ntt.demo.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioDao {
     public List<UsuarioDTO> obtenerUsuarios() throws Exception;

    public UsuarioDTO ingresarUsuario(UsuarioDTO usuario) throws Exception;

    public UsuarioDTO loginUsuario(UsuarioDTO usuario) throws  Exception;

    public UsuarioDTO modificarUsuario(UsuarioDTO usuario) throws Exception;

    public UsuarioDTO obtenerUsuariosByIdAndUuid(Integer id, String Uuid) throws Exception;

    public UsuarioDTO obtenerUsuariosByMailAndPass(String mail, String pass) throws Exception;

    public UsuarioDTO obtenerUsuariosByIdAndUuidAndToken(Integer id, String uuid, String token) throws Exception;

    public UsuarioDTO obtenerUsuariosByEmailAndPassAndToken(String email, String pass, String token) throws Exception;
}
