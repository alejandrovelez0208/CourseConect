package com.courseConnect.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courseConnect.admin.entidad.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);
}
