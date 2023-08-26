package com.ntt.demo.dao;

import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.entity.Phone;
import com.ntt.demo.entity.Usuario;
import com.ntt.demo.mappers.UsuarioMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UsuarioDaoImpl implements UsuarioDao{
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<UsuarioDTO> obtenerUsuarios() throws Exception{
        Session session = sessionFactory.openSession();
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        try {
            Query query =session.createNamedQuery("Usuario.findAll");
            List<Usuario> usuarioList = query.getResultList();

            if(usuarioList !=null && !usuarioList.isEmpty()){
                for(Usuario model : usuarioList){
                    UsuarioDTO dto = new UsuarioDTO();
                    dto = UsuarioMapper.toDTO(model);
                    usuarioDTOList.add(dto);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            session.close();
        }


        return usuarioDTOList;
    }


    @Override
    public UsuarioDTO ingresarUsuario(UsuarioDTO usuario) throws Exception{
        Session session = sessionFactory.openSession();
        try{
            if(usuario !=null){
                session.beginTransaction();
                Usuario model = new Usuario();
                model = UsuarioMapper.toModel(usuario);

                ZoneId zoneChile = ZoneId.of("America/Santiago");
                ZonedDateTime fechaChile = ZonedDateTime.now(zoneChile);
                String uuid = UUID.randomUUID().toString();
                model.setToken(uuid);
                model.setActive(true);
                model.setCreated(fechaChile.toLocalDateTime());
                model.setLastLogin(fechaChile.toLocalDateTime());
                session.save(model);
                for (Phone p : model.getPhones()){
                    p.setUsuarios(model);
                    session.save(p);
                }

                usuario = UsuarioMapper.toDTO(model);
                session.getTransaction().commit();
            }
        }catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }

        return usuario;

    }
}
