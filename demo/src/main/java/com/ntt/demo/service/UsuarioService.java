package com.ntt.demo.service;

import com.ntt.demo.dao.UsuarioDao;
import com.ntt.demo.dto.JsonResponseDTO;
import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public JsonResponseDTO ingresarUsuario(@RequestBody(required = false) UsuarioDTO dto) throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<String> causas = new ArrayList<>();
        try{
            causas = this.valid(dto);
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



    public List<String> valid(UsuarioDTO usuarioDTO) throws Exception{
        List<String> causas = new ArrayList<>();
        try{
            validUsuarios(usuarioDTO, causas);
            validPhones(usuarioDTO, causas);


        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return causas;
    }

    private static void validPhones(UsuarioDTO usuarioDTO, List<String> causas) {
        if(Validation.isEmptyPhones(usuarioDTO.getPhones())){
            causas.add("La lista de teléfonos viene vacía o nula. ");
        }else{
            for (PhoneDTO dto : usuarioDTO.getPhones()){
                if(dto.getNumber() == null) causas.add("En algun registro de tus listas de teléfono, viene vacío Number. ");
                if(dto.getCitycode() == null) causas.add("En algun registro de tus listas de teléfono, viene vacío city code. ");
                if(dto.getCountrycode() == null) causas.add("En algun registro de tus listas de teléfono, viene vacío Country Code. ");
            }
        }
    }

    private void validUsuarios(UsuarioDTO usuarioDTO, List<String> causas) throws Exception {
        if(Validation.isEmptyUsuario(usuarioDTO)){
            causas.add("El usuario viene nulo. ");
        }else{
            List<UsuarioDTO>  usuarioVOList = usuarioDao.obtenerUsuarios();
            if(usuarioVOList !=null  &&
                    !usuarioVOList.isEmpty()
                    && usuarioVOList.stream().
                    filter(u-> u !=null && u.getId() != null).
                    anyMatch(u->u.getEmail().equals(usuarioDTO.getEmail()))) causas.add("El correo ya existe para otro usuario, ingrese uno distinto. ");
            //Validaciones de campos
            if(usuarioDTO.getName() == null) causas.add("El nombre de usuario viene nulo. ");
            if(usuarioDTO.getEmail() == null){
                causas.add("El email de usuario viene nulo. ");
            }else if (!Validation.isValidEmail(usuarioDTO.getEmail())){
                causas.add("El formato de correo ingresado no es valido. ");
            }
            if(usuarioDTO.getPassword() == null){
                causas.add("El password de usuario viene nulo. ");
            }else if (!Validation.isValidPass(usuarioDTO.getPassword())){
                causas.add("El formato de la contraseña ingresado no es valido. Ingrese una mayuscula, letras minusculas y dos numeros como minimo. ");
            }
        }
    }


}
