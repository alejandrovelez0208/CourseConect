package com.courseConnect.admin.servicio;

import com.courseConnect.admin.entidad.Contenido;

public interface ContenidoServicio {

	Contenido guardarContenido(String nombre1Pdf, byte[] archivo1Pdf, String nombre2Pdf, byte[] archivo2Pdf,
			String nombre1Doc, byte[] archivo1Doc, String nombre2Doc, byte[] archivo2Doc, String tutorialVideo,
			byte[] archivoVideo, String imagenNombre, byte[] imagenGuia, Long cursoId);

	Contenido crearOrActualizarContenido(Contenido contenido);

	Contenido cargarContenidoById(Long CursoId);
}
