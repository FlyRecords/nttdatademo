package com.ntt.demo.dto;



import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioDTO implements Serializable {
    private Integer id;
    private String uuidusuario;
    private String name;
    private String email;
    private String password;
    private String token;
    private Boolean isActive;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private List<PhoneDTO> phones;

    public UsuarioDTO(Integer id, String name, String email, String password, String token, Boolean isActive, LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin, List<PhoneDTO> phones) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.isActive = isActive;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.phones = phones;
    }

    public UsuarioDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public String getUuidusuario() {
        return uuidusuario;
    }

    public void setUuidusuario(String uuidusuario) {
        this.uuidusuario = uuidusuario;
    }
}
