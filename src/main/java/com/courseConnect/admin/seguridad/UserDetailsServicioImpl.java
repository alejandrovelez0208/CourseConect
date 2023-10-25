package com.courseConnect.admin.seguridad;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.UsuarioServicio;

@Service
public class UserDetailsServicioImpl implements UserDetailsService {

	private UsuarioServicio usuarioServicio;

	public UserDetailsServicioImpl(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioServicio.cargarUsuarioPorEmail(email);
		if (usuario == null)
			throw new UsernameNotFoundException("Usuario no encontrado");
		Collection<GrantedAuthority> autorizaciones = new ArrayList<>();
		usuario.getRoles().forEach(role -> {
			SimpleGrantedAuthority autoridad = new SimpleGrantedAuthority(role.getNombre());
			autorizaciones.add(autoridad);
		});
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				usuario.getEmail(), usuario.getContraseña(), autorizaciones);
		return userDetails;
	}
}
