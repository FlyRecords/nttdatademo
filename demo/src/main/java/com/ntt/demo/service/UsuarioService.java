package com.ntt.demo.service;

import com.ntt.demo.dao.UsuarioDao;
import com.ntt.demo.dto.JsonResponseDTO;
import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.utils.MensajeEnum;
import com.ntt.demo.utils.StatusServiceEnum;
import com.ntt.demo.utils.TokenStatusEnum;
import com.ntt.demo.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioService {
    @Autowired
    UsuarioDao usuarioDao;

    @GetMapping("/obtenertodos")
    public ResponseEntity<JsonResponseDTO> obtenerUsuarios() throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        try{
            usuarioDTOList = usuarioDao.obtenerUsuarios();
            if(usuarioDTOList !=null && !usuarioDTOList.isEmpty()){
                responseUsuario.setObj(usuarioDTOList);
                responseUsuario.setEstado(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus());
            }else{
                responseUsuario.setMensaje(MensajeEnum.SN_USUARIOS_REGISTRADOS.getMensaje());
            }
            return ResponseEntity.ok(responseUsuario); // 200 OK
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/ingresar")
    public ResponseEntity<JsonResponseDTO> ingresarUsuario(@RequestBody(required = false) UsuarioDTO dto) throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<String> causas = new ArrayList<>();
        try{
            causas = this.valid(dto);
            if(causas.isEmpty()){
                usuarioDTO = usuarioDao.ingresarUsuario(dto);
                responseUsuario.setObj(usuarioDTO);
                responseUsuario.setEstado(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus());
                responseUsuario.setStatusToken(TokenStatusEnum.TOKEN_CREADO.getMensajeToken());
                return ResponseEntity.status(HttpStatus.CREATED).body(responseUsuario); // 201 Created
            }else{
                responseUsuario.setEstado(StatusServiceEnum.DATOS_INCORRECTOS.getMensajeStatus());
                responseUsuario.setMensaje(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje());
                responseUsuario.setListaValidaciones(causas);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseUsuario); //400 ya que no cumple las validaciones
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponseDTO> loginUsuario(@RequestBody UsuarioDTO dto) throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try{
            usuarioDTO = usuarioDao.loginUsuario(dto);
            if(usuarioDTO !=null && usuarioDTO.getEmail() != null){
                responseUsuario.setObj(usuarioDTO);
                responseUsuario.setStatusToken(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken());
                responseUsuario.setEstado(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus());
                return ResponseEntity.ok(responseUsuario); // 200 OK
            }else{
                responseUsuario.setEstado(StatusServiceEnum.DATOS_INCORRECTOS.getMensajeStatus());
                responseUsuario.setMensaje(MensajeEnum.CREDENCIALES_INCORRECTAS.getMensaje());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseUsuario); // 401 Unauthorized
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @PutMapping("/modificarbyidanduuid")
    public ResponseEntity<JsonResponseDTO> modificarUsuarioByIdAndUuid(@RequestBody UsuarioDTO dto) throws Exception{
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<String> causas = new ArrayList<>();
        try{
            UsuarioDTO usuarioEncontrado = usuarioDao.obtenerUsuariosByIdAndUuid(dto.getId(),dto.getUuidusuario());
            UsuarioDTO usuarioTokenValido = usuarioDao.obtenerUsuariosByIdAndUuidAndToken(dto.getId(), dto.getUuidusuario(), dto.getToken());
            if(usuarioEncontrado == null || usuarioEncontrado.getId() == null){
                //usuario no encontrado
                responseUsuario.setMensaje(MensajeEnum.US_INEXISTENTE.getMensaje());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseUsuario); //404
            }else if(usuarioTokenValido == null || usuarioTokenValido.getId() == null){
                //usuario token invalido
                responseUsuario.setMensaje(TokenStatusEnum.TOKEN_INVALIDO.getMensajeToken());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseUsuario); // 403
            }
            causas = this.valid(dto);
            if(causas.isEmpty()){
                usuarioDTO = usuarioDao.modificarUsuario(dto);
                responseUsuario.setObj(usuarioDTO);
                responseUsuario.setStatusToken(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken());
                responseUsuario.setEstado(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus());
                return ResponseEntity.status(HttpStatus.OK).body(responseUsuario); // 200 EXITO
            }else{
                responseUsuario.setMensaje(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje());
                responseUsuario.setListaValidaciones(causas);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseUsuario); //400 ya que no cumple las validaciones
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }
    @PutMapping("/modificarbymailandpass")
    public ResponseEntity<JsonResponseDTO> modificarUsuarioByMailAndPass(@RequestBody UsuarioDTO dto) throws Exception {
        JsonResponseDTO responseUsuario = new JsonResponseDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<String> causas = new ArrayList<>();
        try {
            UsuarioDTO usuarioEncontrado = usuarioDao.obtenerUsuariosByMailAndPass(dto.getEmail(), dto.getPassword());
            UsuarioDTO usuarioTokenValido = usuarioDao.obtenerUsuariosByEmailAndPassAndToken(dto.getEmail(), dto.getPassword(), dto.getToken());
            if (usuarioEncontrado == null || usuarioEncontrado.getId() == null) {
                //usuario no encontrado
                responseUsuario.setMensaje(MensajeEnum.US_INEXISTENTE.getMensaje());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseUsuario); //404
            } else if (usuarioTokenValido == null || usuarioTokenValido.getId() == null) {
                //usuario token invalido
                responseUsuario.setMensaje(TokenStatusEnum.TOKEN_INVALIDO.getMensajeToken());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseUsuario); // 403
            }
            causas = this.valid(dto);
            if (causas.isEmpty()) {
                usuarioDTO = usuarioDao.modificarUsuario(dto);
                responseUsuario.setObj(usuarioDTO);
                responseUsuario.setStatusToken(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken());
                responseUsuario.setEstado(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus());
                return ResponseEntity.status(HttpStatus.OK).body(responseUsuario); // 200 EXITO
            } else {
                responseUsuario.setEstado(StatusServiceEnum.DATOS_INCORRECTOS.getMensajeStatus());
                responseUsuario.setMensaje(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje());
                responseUsuario.setListaValidaciones(causas);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseUsuario); //400 ya que no cumple las validaciones
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        if(Utilidades.isEmptyPhones(usuarioDTO.getPhones())){
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
        if(Utilidades.isEmptyUsuario(usuarioDTO)){
            causas.add("El usuario viene nulo. ");
        }else{
            List<UsuarioDTO>  usuarioVOList = usuarioDao.obtenerUsuarios();

            //Validaciones de campos
            if(usuarioDTO.getName() == null) causas.add("El nombre de usuario viene nulo. ");
            if(usuarioDTO.getEmail() == null){
                causas.add("El email de usuario viene nulo. ");
            }else if (!Utilidades.isValidEmail(usuarioDTO.getEmail())){
                causas.add("El formato de correo ingresado no es valido. ");
            }else if(usuarioVOList !=null  && usuarioDTO.getId() == null &&
                    !usuarioVOList.isEmpty() && usuarioVOList.stream().
                    filter(u-> u !=null && u.getEmail() != null).
                    anyMatch(u->u.getEmail().equals(usuarioDTO.getEmail()))){
                causas.add("El correo ya existe para otro usuario, ingrese uno distinto. ");
            }
            if(usuarioDTO.getPassword() == null){
                causas.add("El password de usuario viene nulo. ");
            }else if (!Utilidades.isValidPass(usuarioDTO.getPassword())){
                causas.add("El formato de la contraseña ingresado no es valido. Ingrese una mayuscula, letras minusculas y dos numeros como minimo. ");
            }
        }
    }


}
