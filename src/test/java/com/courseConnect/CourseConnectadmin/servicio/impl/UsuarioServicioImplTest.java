package com.courseConnect.CourseConnectadmin.servicio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.dao.UsuarioDao;
import com.courseConnect.admin.entidad.Role;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.impl.UsuarioServicioImpl;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioImplTest {

	@InjectMocks
	private UsuarioServicioImpl servicio;

	@Mock
	private UsuarioDao usuarioDao;

	@Mock
	private RoleDao roleDao;

	private Usuario usuario;

	private Role role;

	private String email = "usuario1@gmail.com";
	private String password = "54654A";

	@BeforeEach
	public void init() {
		usuario = new Usuario();
		usuario.setUsuarioId(1L);
		usuario.setEmail("usuario2@gmail.com");

		role = new Role();
		role.setRoleId(2L);
		role.setNombre("Estudiante");
	}

	@Test
	public void cargarUsuarioPorEmail() {
		when(usuarioDao.findByEmail(anyString())).thenReturn(usuario);

		Usuario respuesta = servicio.cargarUsuarioPorEmail(email);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getUsuarioId());
		assertEquals("usuario2@gmail.com", respuesta.getEmail());

		verify(usuarioDao, times(1)).findByEmail(anyString());
	}

	@Test
	public void crearUsuarios() {
		when(usuarioDao.save(any())).thenReturn(usuario);

		Usuario respuesta = servicio.crearUsuarios(email, password);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getUsuarioId());
		assertEquals("usuario2@gmail.com", respuesta.getEmail());

		verify(usuarioDao, times(1)).save(any());
	}

	@Test
	public void asignarRoleToUsuario() {
		when(usuarioDao.findByEmail(anyString())).thenReturn(usuario);
		when(roleDao.findByNombre(anyString())).thenReturn(role);

		servicio.asignarRoleToUsuario(email, password);

		verify(usuarioDao, times(1)).findByEmail(anyString());
		verify(roleDao, times(1)).findByNombre(anyString());
	}
}
