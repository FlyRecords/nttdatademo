package com.ntt.demo.dao;

import com.ntt.demo.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioDao {
     public List<UsuarioDTO> obtenerUsuarios() throws Exception;

    public UsuarioDTO ingresarUsuario(UsuarioDTO usuario) throws Exception;
}
