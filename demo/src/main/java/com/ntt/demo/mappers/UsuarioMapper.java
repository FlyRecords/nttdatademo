package com.ntt.demo.mappers;

import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.entity.Phone;
import com.ntt.demo.entity.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setName(usuario.getName() != null ? usuario.getName() : null);
        usuarioDTO.setEmail(usuario.getEmail() != null ? usuario.getEmail() : null);
        usuarioDTO.setPassword(usuario.getPassword() != null ? usuario.getPassword() : null);
        usuarioDTO.setToken(usuario.getToken() != null ? usuario.getToken() : null);
        usuarioDTO.setActive(usuario.getActive() != null ? usuario.getActive() : null);
        usuarioDTO.setCreated(usuario.getCreated() != null ? usuario.getCreated() : LocalDateTime.now());
        usuarioDTO.setModified(usuario.getModified() != null ? usuario.getModified() : null);
        usuarioDTO.setLastLogin(usuario.getLastLogin() != null ? usuario.getLastLogin() : null);
        //usuarioDTO.setPhones(usuario.getPhones() != null ? PhoneTransformer.phoneList(usuario.getPhones()) : null);

        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        if(usuario.getPhones() !=null){
            for (Phone p : usuario.getPhones()){
                phoneDTOList.add(PhoneMapper.toDTO(p));
            }
            usuarioDTO.setPhones(phoneDTOList);
        }
        return usuarioDTO;
    }

    public static Usuario toModel(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setName(usuarioDTO.getName() != null ? usuarioDTO.getName() : null);
        usuario.setEmail(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : null);
        usuario.setPassword(usuarioDTO.getPassword() != null ? usuarioDTO.getPassword() : null);
        usuario.setToken(usuarioDTO.getToken() != null ? usuarioDTO.getToken() : null);
        usuario.setActive(usuario.getActive() != null ? usuario.getActive() : null);
        usuario.setCreated(usuarioDTO.getCreated() != null ? usuarioDTO.getCreated() : LocalDateTime.now());
        usuario.setModified(usuarioDTO.getModified() != null ? usuarioDTO.getModified() : null);
        usuario.setLastLogin(usuarioDTO.getLastLogin() != null ? usuarioDTO.getLastLogin() : null);
        usuario.setPhones(usuarioDTO.getPhones() != null ? PhoneMapper.phoneList(usuarioDTO.getPhones()) : null);
        return usuario;
    }
}
