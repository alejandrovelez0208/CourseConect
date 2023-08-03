package com.courseConnect.admin.servicio.impl;

import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.entidad.Role;
import com.courseConnect.admin.servicio.RoleServicio;

public class RoleServicioImpl implements RoleServicio {

	private RoleDao roleDao;

	public RoleServicioImpl(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public Role cargarRolePorNombre(String roleNombre) {
		return roleDao.buscarRolePorNombre(roleNombre);
	}

	@Override
	public Role crearRole(String roleNombre) {
		return roleDao.save(new Role(roleNombre));
	}

}
