package com.courseConnect.admin.servicio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courseConnect.admin.dao.ContenidoDao;
import com.courseConnect.admin.dao.CursoDao;
import com.courseConnect.admin.entidad.Contenido;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.servicio.ContenidoServicio;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContenidoServicioImpl implements ContenidoServicio {

	@Autowired
	private ContenidoDao contenidoDao;

	@Autowired
	private CursoDao cursoDao;

	@Override
	public Contenido guardarContenido(String nombre1Pdf, byte[] archivo1Pdf, String nombre2Pdf, byte[] archivo2Pdf,
			String nombre1Doc, byte[] archivo1Doc, String nombre2Doc, byte[] archivo2Doc, String tutorialVideo,
			byte[] archivoVideo, String imagenNombre, byte[] imagenGuia, Long cursoId) {
		Curso curso = cursoDao.findById(cursoId)
				.orElseThrow(() -> new EntityNotFoundException("Curso con Id" + cursoId + "No Encontrado"));

		return contenidoDao.save(new Contenido(nombre1Pdf, archivo1Pdf, nombre2Pdf, archivo2Pdf, nombre1Doc,
				archivo2Doc, nombre2Doc, archivo2Doc, tutorialVideo, archivoVideo, imagenNombre, imagenGuia, curso));
	}

}
