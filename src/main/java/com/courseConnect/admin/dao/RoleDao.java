package com.courseConnect.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courseConnect.admin.entidad.Role;

public interface RoleDao extends JpaRepository<Role, Long> {

	Role buscarRolePorNombre(String nombre);
}
