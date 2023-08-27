package com.ntt.demo.dao;

import com.ntt.demo.dto.UsuarioDTO;
import com.ntt.demo.entity.Phone;
import com.ntt.demo.entity.Usuario;
import com.ntt.demo.mappers.UsuarioMapper;
import com.ntt.demo.utils.Utilidades;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
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
                String token = UUID.randomUUID().toString();
                String uuidUsuario = UUID.randomUUID().toString();
                model.setUuidusuario(uuidUsuario);
                model.setToken(token);
                model.setActive(true);
                model.setCreated(Utilidades.obtenerFechaHoraActual());
                model.setModified(Utilidades.obtenerFechaHoraActual());
                model.setLastLogin(Utilidades.obtenerFechaHoraActual());
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

    @Override
    public UsuarioDTO loginUsuario(UsuarioDTO usuario) throws  Exception{
        Session session = sessionFactory.openSession();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try{
            session.beginTransaction();
            Query query = session.createNamedQuery("Usuario.findByEmailAndPassword").setParameter("email", usuario.getEmail()).setParameter("password",usuario.getPassword());
            Usuario usuarioQuery = !query.getResultList().isEmpty() ? (Usuario) query.getResultList().get(0) : null;
            if(usuarioQuery != null){
                String token = UUID.randomUUID().toString(); // se actualizará el token en cada login. IMPORTANTE!
                usuarioQuery.setToken(token);
                usuarioQuery.setLastLogin(Utilidades.obtenerFechaHoraActual());
                session.update(usuarioQuery);
                usuarioDTO = UsuarioMapper.toDTO(usuarioQuery);
            }
            session.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }
        return usuarioDTO;
    }


    @Override
    public UsuarioDTO modificarUsuario(UsuarioDTO usuario) throws Exception{
        Session session = sessionFactory.openSession();
        try{
            if(usuario !=null){

                session.beginTransaction();
                Usuario model = new Usuario();
                model = UsuarioMapper.toModel(usuario);

                String token = UUID.randomUUID().toString(); // se actualizará el token en cada registro. IMPORTANTE!
                model.setToken(token);
                model.setActive(true);
                model.setModified(Utilidades.obtenerFechaHoraActual());
                session.update(model);
                for (Phone p : model.getPhones()){
                    p.setUsuarios(model);
                    session.update(p);
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

    @Override
    public UsuarioDTO obtenerUsuariosByIdAndUuid(Integer id, String Uuid) throws Exception{
        Session session = sessionFactory.openSession();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try {
            Query query =session.createNamedQuery("Usuario.findByIdAndUuidusuario").setParameter("id", id).setParameter("uuidusuario",Uuid);
            Usuario usuario = !query.getResultList().isEmpty() ? (Usuario) query.getResultList().get(0) : null;
            usuarioDTO = UsuarioMapper.toDTO(usuario);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            session.close();
        }
        return usuarioDTO;
    }
    @Override
    public UsuarioDTO obtenerUsuariosByMailAndPass(String mail, String pass) throws Exception{
        Session session = sessionFactory.openSession();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try {
            Query query =session.createNamedQuery("Usuario.findByEmailAndPassword").setParameter("email", mail).setParameter("password",pass);
            Usuario usuario = !query.getResultList().isEmpty() ? (Usuario) query.getResultList().get(0) : null;
            usuarioDTO = UsuarioMapper.toDTO(usuario);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            session.close();
        }
        return usuarioDTO;
    }
    @Override
    public UsuarioDTO obtenerUsuariosByIdAndUuidAndToken(Integer id, String uuid, String token) throws Exception{
        Session session = sessionFactory.openSession();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try {
            Query query =session.createNamedQuery("Usuario.findByIdAndUuidusuarioAndToken").setParameter("id", id).setParameter("uuidusuario",uuid).setParameter("token",token);
            Usuario usuario = !query.getResultList().isEmpty() ? (Usuario) query.getResultList().get(0) : null;
            usuarioDTO = UsuarioMapper.toDTO(usuario);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            session.close();
        }
        return usuarioDTO;
    }

    @Override
    public UsuarioDTO obtenerUsuariosByEmailAndPassAndToken(String email, String pass, String token) throws Exception{
        Session session = sessionFactory.openSession();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        try {
            Query query =session.createNamedQuery("Usuario.findByEmailAndPasswordAndToken").setParameter("email", email).setParameter("password",pass).setParameter("token",token);
            Usuario usuario = !query.getResultList().isEmpty() ? (Usuario) query.getResultList().get(0) : null;
            usuarioDTO = UsuarioMapper.toDTO(usuario);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            session.close();
        }
        return usuarioDTO;
    }



}
