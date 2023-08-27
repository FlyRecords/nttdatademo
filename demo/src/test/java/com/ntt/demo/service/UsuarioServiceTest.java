package com.ntt.demo.service;

import com.ntt.demo.dao.UsuarioDao;
import com.ntt.demo.dto.JsonResponseDTO;
import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

class UsuarioServiceTest {

    @Mock
    private UsuarioDao usuarioDao;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerUsuarios() throws Exception {
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        // Agregar datos a usuarioDTO
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
}