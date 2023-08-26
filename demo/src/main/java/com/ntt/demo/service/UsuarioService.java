package com.ntt.demo.service;

import com.ntt.demo.dao.UsuarioDao;
import com.ntt.demo.dto.JsonResponseDTO;
import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/usuarios")
public class UsuarioService {
    @Autowired
    UsuarioDao usuarioDao;

    @GetMapping("/obtenertodos")
    public JsonResponseDTO obtenerUsuarios() throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        try{
            usuarioDTOList = usuarioDao.obtenerUsuarios();
            if(usuarioDTOList !=null && !usuarioDTOList.isEmpty()){
                responseUsuario.setObj(usuarioDTOList);
                responseUsuario.setEstado("EXITO");
            }else{
                responseUsuario.setMensaje("NINGUN USUARIO REGISTRADO");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return responseUsuario;
    }

    @PostMapping("/ingresar")
    public JsonResponseDTO ingresarUsuario(@RequestBody UsuarioDTO dto) throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<String> causas = new ArrayList<>();
        try{
            causas = this.validUsuario(dto);
            if(causas.isEmpty()){
                usuarioDTO = usuarioDao.ingresarUsuario(dto);
                responseUsuario.setObj(usuarioDTO);
                responseUsuario.setEstado("EXITO");
            }else{
                responseUsuario.setMensaje("Entrega de validaciones");
                responseUsuario.setListaValidaciones(causas);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return responseUsuario;
    }



    public List<String> validUsuario(UsuarioDTO usuarioDTO) throws Exception{
        List<String> causas = new ArrayList<>();
        try{
            List<UsuarioDTO>  usuarioVOList = usuarioDao.obtenerUsuarios();
            if(usuarioVOList.stream().filter(u-> u.getId() == null).anyMatch(u->u.getEmail().equals(usuarioDTO.getEmail()))) causas.add("El correo ya existe para otro usuario, ingrese uno distinto. ");
            if(usuarioVOList.stream().filter(u-> u.getId() != null).anyMatch(u-> !Objects.equals(u.getId(), usuarioDTO.getId()) && u.getEmail().equals(usuarioDTO.getEmail()))) causas.add("El correo ya existe para otro usuario, ingrese uno distinto. ");
            if(usuarioDTO == null) causas.add("El usuario viene nulo. ");
            if(!Validation.isValidEmail(usuarioDTO.getEmail())) causas.add("El formato de correo ingresado no es valido. ");
            if(!Validation.isValidPass(usuarioDTO.getPassword())) causas.add("El formato de la contraseña ingresado no es valido. Ingrese una mayuscula, letras minusculas y dos numeros como minimo. ");

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return causas;
    }


}
