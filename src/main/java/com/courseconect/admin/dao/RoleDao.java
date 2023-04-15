package com.courseconect.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courseconect.admin.entidad.Role;

public interface RoleDao extends JpaRepository<Role, Long>{

	Role buscarPorNombre(String name);
}
