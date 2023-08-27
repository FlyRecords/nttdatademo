package com.ntt.demo.service;

import com.ntt.demo.dao.UsuarioDao;
import com.ntt.demo.dto.JsonResponseDTO;
import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.utils.MensajeEnum;
import com.ntt.demo.utils.StatusServiceEnum;
import com.ntt.demo.utils.TokenStatusEnum;
import com.ntt.demo.utils.Utilidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class UsuarioServiceTest {

    @Mock
    private UsuarioDao usuarioDao;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //TEST CASO DE EXITO
    @Test
    void obtenerUsuariosExitosamente() throws Exception {
        //Seteo de variables para probar el servicio
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId(1);
        usuarioDTO.setPassword("Hola123123");
        usuarioDTO.setName("Alonso Cerda");
        usuarioDTO.setEmail("Alonso@gmail.com");
        phoneDTO.setId(1);
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("123123");
        phoneDTOList.add(phoneDTO);
        usuarioDTO.setPhones(phoneDTOList);
        usuarioDTOList.add(usuarioDTO);

        when(usuarioDao.obtenerUsuarios()).thenReturn(usuarioDTOList);

        ResponseEntity<JsonResponseDTO> response = usuarioService.obtenerUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EXITO", response.getBody().getEstado());
        assertEquals(usuarioDTOList, response.getBody().getObj());
    }
    @Test
    void obtenerUsuariosNingunRegistro() throws Exception{
        //Seteo de variables
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();

        when(usuarioDao.obtenerUsuarios()).thenReturn(usuarioDTOList);

        ResponseEntity<JsonResponseDTO> response = usuarioService.obtenerUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());//viene con un cuerpo, ya que, tiene mensajes seteados.
        assertNull(response.getBody().getObj());
        assertEquals(MensajeEnum.SN_USUARIOS_REGISTRADOS.getMensaje(), response.getBody().getMensaje());


    }

    @Test
    void ingresarUsuarioExitosamente() throws Exception{

        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOIngreso = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();

        setVariablesUsuExito(usuarioDTOIngreso, usuarioDTORetorno, phoneDTOList, phoneDTO);


        when(usuarioDao.ingresarUsuario(usuarioDTOIngreso)).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.ingresarUsuario(usuarioDTOIngreso);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(TokenStatusEnum.TOKEN_CREADO.getMensajeToken(), response.getBody().getStatusToken());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }


    @Test
    void ingresarUsuarioValidaciones() throws Exception{

        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOIngreso = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();

        //Ingreso como el JSON
        usuarioDTOIngreso.setEmail("Alonso@");
        usuarioDTOIngreso.setPassword("hola123123");
        phoneDTO.setId(1);
        phoneDTOList.add(phoneDTO);
        usuarioDTOIngreso.setPhones(phoneDTOList);
        //Rellenando el retorno como si hubiera pasado por el DAO
        usuarioDTORetorno.setName(usuarioDTOIngreso.getName());
        usuarioDTORetorno.setEmail(usuarioDTOIngreso.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOIngreso.getPassword());
        usuarioDTORetorno.setPhones(usuarioDTOIngreso.getPhones());
        usuarioDTORetorno.setId(1);
        usuarioDTORetorno.setUuidusuario(UUID.randomUUID().toString());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setActive(true);
        usuarioDTORetorno.setCreated(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setLastLogin(Utilidades.obtenerFechaHoraActual());

        List<String> causas = new ArrayList<>();
        causas.add("El nombre de usuario viene nulo. ");
        causas.add("El formato de correo ingresado no es valido. ");
        causas.add("El formato de la contraseña ingresado no es valido. Ingrese una mayuscula, letras minusculas y dos numeros como minimo. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Number. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío city code. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Country Code. ");
        when(usuarioDao.ingresarUsuario(usuarioDTOIngreso)).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.ingresarUsuario(usuarioDTOIngreso);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.DATOS_INCORRECTOS.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje(), response.getBody().getMensaje());
        assertEquals(causas, response.getBody().getListaValidaciones());

    }
    /*@Test
    void ingresarUsuarioCatch() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOIngreso = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();

        setVariablesUsuExito(usuarioDTOIngreso, usuarioDTORetorno, phoneDTOList, phoneDTO);
        //generarmos una excepecion en el Dao
        when(usuarioDao.ingresarUsuario(usuarioDTOIngreso)).thenThrow(new Exception());

        ResponseEntity<JsonResponseDTO> response = usuarioService.ingresarUsuario(usuarioDTOIngreso);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());


    }*/
    @Test
    void loginExitoso () throws Exception{
        UsuarioDTO usuarioDTOLogin = new UsuarioDTO();
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOIngreso = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        setVariablesUsuExito(usuarioDTOIngreso, usuarioDTORetorno, phoneDTOList, phoneDTO);


        usuarioDTOLogin.setEmail("Alonso@gmail.com");
        usuarioDTOLogin.setPassword("Hola123123");

        when(usuarioDao.loginUsuario(usuarioDTOLogin)).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.loginUsuario(usuarioDTOLogin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken(), response.getBody().getStatusToken());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }
    @Test
    void loginIncorrecto () throws Exception{
        UsuarioDTO usuarioDTOLogin = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();

        usuarioDTOLogin.setEmail("Alonso@gmail.com");
        usuarioDTOLogin.setPassword("Hola123123");

        when(usuarioDao.loginUsuario(usuarioDTOLogin)).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.loginUsuario(usuarioDTOLogin);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.DATOS_INCORRECTOS.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(MensajeEnum.CREDENCIALES_INCORRECTAS.getMensaje(), response.getBody().getMensaje());
        assertNull(response.getBody().getObj());

    }
    @Test
    void modificarUsuarioExitosamenteId() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("alonso2@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        usuarioDTORetorno.setId(usuarioDTOModificar.getId());
        usuarioDTORetorno.setUuidusuario(usuarioDTOModificar.getUuidusuario());
        usuarioDTORetorno.setName(usuarioDTOModificar.getName());
        usuarioDTORetorno.setEmail(usuarioDTOModificar.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOModificar.getPassword());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setCreated(usuarioDTOModificar.getCreated());
        usuarioDTORetorno.setLastLogin(usuarioDTOModificar.getLastLogin());
        usuarioDTORetorno.setPhones(usuarioDTOModificar.getPhones());

        when(usuarioDao.modificarUsuario(usuarioDTOModificar)).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByIdAndUuid(usuarioDTOModificar.getId(),usuarioDTOModificar.getUuidusuario())).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByIdAndUuidAndToken(usuarioDTOModificar.getId(), usuarioDTOModificar.getUuidusuario(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByIdAndUuid(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken(), response.getBody().getStatusToken());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());



    }
    @Test
    void modificarUsuarioInexistenteId() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = null;
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(100);
        usuarioDTOModificar.setUuidusuario("uiidpersonalizadoincorrecto");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("alonso2@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        when(usuarioDao.obtenerUsuariosByIdAndUuid(usuarioDTOModificar.getId(),usuarioDTOModificar.getUuidusuario())).thenReturn(usuarioDTORetorno);


        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByIdAndUuid(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(MensajeEnum.US_INEXISTENTE.getMensaje(), response.getBody().getMensaje());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }

    @Test
    void modificarUsuarioTokenInvalidoId() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = null;
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("alonso2@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("tokeinvalido-personalizado-123123");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);
        when(usuarioDao.obtenerUsuariosByIdAndUuid(usuarioDTOModificar.getId(), usuarioDTOModificar.getUuidusuario())).thenReturn(usuarioDTOModificar);
        when(usuarioDao.obtenerUsuariosByIdAndUuidAndToken(usuarioDTOModificar.getId(),usuarioDTOModificar.getUuidusuario(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);


        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByIdAndUuid(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(TokenStatusEnum.TOKEN_INVALIDO.getMensajeToken(), response.getBody().getMensaje());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }

    @Test
    void modificarUsuarioValidacionesId() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setEmail("alonso2@");
        usuarioDTOModificar.setPassword("hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        usuarioDTORetorno.setId(usuarioDTOModificar.getId());
        usuarioDTORetorno.setUuidusuario(usuarioDTOModificar.getUuidusuario());
        usuarioDTORetorno.setName(usuarioDTOModificar.getName());
        usuarioDTORetorno.setEmail(usuarioDTOModificar.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOModificar.getPassword());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setCreated(usuarioDTOModificar.getCreated());
        usuarioDTORetorno.setLastLogin(usuarioDTOModificar.getLastLogin());
        usuarioDTORetorno.setPhones(usuarioDTOModificar.getPhones());

        List<String> causas = new ArrayList<>();
        causas.add("El nombre de usuario viene nulo. ");
        causas.add("El formato de correo ingresado no es valido. ");
        causas.add("El formato de la contraseña ingresado no es valido. Ingrese una mayuscula, letras minusculas y dos numeros como minimo. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Number. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío city code. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Country Code. ");

        when(usuarioDao.obtenerUsuariosByIdAndUuid(usuarioDTOModificar.getId(),usuarioDTOModificar.getUuidusuario())).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByIdAndUuidAndToken(usuarioDTOModificar.getId(), usuarioDTOModificar.getUuidusuario(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByIdAndUuid(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getListaValidaciones());
        assertEquals(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje(), response.getBody().getMensaje());

        assertEquals(causas, response.getBody().getListaValidaciones());
    }

    @Test
    void modificarUsuarioExitosamenteEmail() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("alonso2@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        usuarioDTORetorno.setId(usuarioDTOModificar.getId());
        usuarioDTORetorno.setUuidusuario(usuarioDTOModificar.getUuidusuario());
        usuarioDTORetorno.setName(usuarioDTOModificar.getName());
        usuarioDTORetorno.setEmail(usuarioDTOModificar.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOModificar.getPassword());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setCreated(usuarioDTOModificar.getCreated());
        usuarioDTORetorno.setLastLogin(usuarioDTOModificar.getLastLogin());
        usuarioDTORetorno.setPhones(usuarioDTOModificar.getPhones());

        when(usuarioDao.modificarUsuario(usuarioDTOModificar)).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByMailAndPass(usuarioDTOModificar.getEmail(),usuarioDTOModificar.getPassword())).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByEmailAndPassAndToken(usuarioDTOModificar.getEmail(), usuarioDTOModificar.getPassword(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByMailAndPass(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(StatusServiceEnum.EXITO_SERVICE.getMensajeStatus(), response.getBody().getEstado());
        assertEquals(TokenStatusEnum.TOKEN_ACTUALIZADO.getMensajeToken(), response.getBody().getStatusToken());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());



    }
    @Test
    void modificarUsuarioInexistenteEmail() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = null;
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("mail-inexistente@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        when(usuarioDao.obtenerUsuariosByIdAndUuid(usuarioDTOModificar.getId(),usuarioDTOModificar.getUuidusuario())).thenReturn(usuarioDTORetorno);


        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByMailAndPass(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(MensajeEnum.US_INEXISTENTE.getMensaje(), response.getBody().getMensaje());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }

    @Test
    void modificarUsuarioTokenInvalidoEmail() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = null;
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setName("Alonso Cerda");
        usuarioDTOModificar.setEmail("alonso2@gmail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("tokeinvalido-personalizado-123123");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTO.setCountrycode("123123");
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("12312");
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);
        when(usuarioDao.obtenerUsuariosByMailAndPass(usuarioDTOModificar.getEmail(), usuarioDTOModificar.getPassword())).thenReturn(usuarioDTOModificar);
        when(usuarioDao.obtenerUsuariosByEmailAndPassAndToken(usuarioDTOModificar.getEmail(),usuarioDTOModificar.getPassword(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);


        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByMailAndPass(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getListaValidaciones());
        assertEquals(TokenStatusEnum.TOKEN_INVALIDO.getMensajeToken(), response.getBody().getMensaje());
        assertEquals(usuarioDTORetorno, response.getBody().getObj());

    }

    @Test
    void modificarUsuarioValidacionesEmail() throws Exception{
        //Seteo de variables para probar el servicio
        UsuarioDTO usuarioDTOModificar = new UsuarioDTO();
        UsuarioDTO usuarioDTORetorno = new UsuarioDTO();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        usuarioDTOModificar.setId(1);
        usuarioDTOModificar.setUuidusuario("7dd22a12-0c14-47ea-9432-ecf4221566b4");
        usuarioDTOModificar.setEmail("alonso2@mail.com");
        usuarioDTOModificar.setPassword("Hola1233");
        usuarioDTOModificar.setToken("cab6aba8-9588-44e1-a88e-97e6f4a1a2ec");
        usuarioDTOModificar.setCreated(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        usuarioDTOModificar.setLastLogin(LocalDateTime.parse("2023-08-26T21:49:52.3859277"));
        phoneDTO.setId(1);
        phoneDTOList.add(phoneDTO);
        usuarioDTOModificar.setPhones(phoneDTOList);

        usuarioDTORetorno.setId(usuarioDTOModificar.getId());
        usuarioDTORetorno.setUuidusuario(usuarioDTOModificar.getUuidusuario());
        usuarioDTORetorno.setName(usuarioDTOModificar.getName());
        usuarioDTORetorno.setEmail(usuarioDTOModificar.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOModificar.getPassword());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setCreated(usuarioDTOModificar.getCreated());
        usuarioDTORetorno.setLastLogin(usuarioDTOModificar.getLastLogin());
        usuarioDTORetorno.setPhones(usuarioDTOModificar.getPhones());

        List<String> causas = new ArrayList<>();
        causas.add("El nombre de usuario viene nulo. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Number. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío city code. ");
        causas.add("En algún registro de tus listas de teléfono, viene vacío Country Code. ");

        when(usuarioDao.obtenerUsuariosByMailAndPass(usuarioDTOModificar.getEmail(),usuarioDTOModificar.getPassword())).thenReturn(usuarioDTORetorno);
        when(usuarioDao.obtenerUsuariosByEmailAndPassAndToken(usuarioDTOModificar.getEmail(), usuarioDTOModificar.getPassword(), usuarioDTOModificar.getToken())).thenReturn(usuarioDTORetorno);

        ResponseEntity<JsonResponseDTO> response = usuarioService.modificarUsuarioByMailAndPass(usuarioDTOModificar);

        //Ejecutando servicio
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getListaValidaciones());
        assertEquals(MensajeEnum.ENTREGA_VALIDACIONES.getMensaje(), response.getBody().getMensaje());

        assertEquals(causas, response.getBody().getListaValidaciones());
    }


    private static void setVariablesUsuExito(UsuarioDTO usuarioDTOIngreso, UsuarioDTO usuarioDTORetorno, List<PhoneDTO> phoneDTOList, PhoneDTO phoneDTO) {
        //Ingreso como el JSON
        usuarioDTOIngreso.setPassword("Hola123123");
        usuarioDTOIngreso.setName("Alonso Cerda");
        usuarioDTOIngreso.setEmail("Alonso@gmail.com");
        phoneDTO.setId(1);
        phoneDTO.setNumber("99887766");
        phoneDTO.setCitycode("123123");
        phoneDTO.setCountrycode("928212");
        phoneDTOList.add(phoneDTO);
        usuarioDTOIngreso.setPhones(phoneDTOList);
        //Rellenando el retorno como si hubiera pasado por el DAO
        usuarioDTORetorno.setName(usuarioDTOIngreso.getName());
        usuarioDTORetorno.setEmail(usuarioDTOIngreso.getEmail());
        usuarioDTORetorno.setPassword(usuarioDTOIngreso.getPassword());
        usuarioDTORetorno.setPhones(usuarioDTOIngreso.getPhones());
        usuarioDTORetorno.setId(1);
        usuarioDTORetorno.setUuidusuario(UUID.randomUUID().toString());
        usuarioDTORetorno.setToken(UUID.randomUUID().toString());
        usuarioDTORetorno.setActive(true);
        usuarioDTORetorno.setCreated(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setModified(Utilidades.obtenerFechaHoraActual());
        usuarioDTORetorno.setLastLogin(Utilidades.obtenerFechaHoraActual());
    }

}