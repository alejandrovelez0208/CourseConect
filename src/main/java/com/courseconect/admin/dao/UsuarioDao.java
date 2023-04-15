package com.courseconect.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courseconect.admin.entidad.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long>{

	Usuario buscarPorEmail(String email);
}
