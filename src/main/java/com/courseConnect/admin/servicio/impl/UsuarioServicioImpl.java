package com.courseConnect.admin.servicio.impl;

import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.dao.UsuarioDao;
import com.courseConnect.admin.entidad.Role;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.UsuarioServicio;

public class UsuarioServicioImpl implements UsuarioServicio {

	private UsuarioDao usuarioDao;

	private RoleDao roleDao;

	public UsuarioServicioImpl(UsuarioDao usuarioDao, RoleDao roleDao) {
		this.usuarioDao = usuarioDao;
		this.roleDao = roleDao;
	}

	@Override
	public Usuario cargarUsuarioPorEmail(String email) {
		return usuarioDao.buscarUsuarioPorEmail(email);
	}

	@Override
	public Usuario crearUsuarios(String email, String password) {
		return usuarioDao.save(new Usuario(email, password));
	}

	@Override
	public void asignarRoleToUsuario(String email, String nombreRole) {
		Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
		Role role = roleDao.buscarRolePorNombre(nombreRole);
		usuario.asignarRoleAUsuario(role);
	}
}
