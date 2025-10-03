package com.SAFE_Rescue.API_Registros;

import com.SAFE_Rescue.API_Registros.modelo.Categoria;
import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Foto;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import com.SAFE_Rescue.API_Registros.repository.CategoriaRepository;
import com.SAFE_Rescue.API_Registros.repository.EstadoRepository;
import com.SAFE_Rescue.API_Registros.repository.FotoRepository;
import com.SAFE_Rescue.API_Registros.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Clase que carga datos iniciales para las entidades de configuración del sistema.
 * Solo se ejecuta en el perfil 'dev'.
 */
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    // INYECCIONES DE DEPENDENCIA
    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cargando datos de registros iniciales...");

        // 1. Cargar las categorías
        crearCategorias();

        // 2. Cargar los estados
        crearEstados();

        // 3. Obtener entidades persistidas para Historial usando List<T>
        List<Estado> estadosActivos = estadoRepository.findByNombre("Activo");
        List<Categoria> categoriasSistema = categoriaRepository.findByNombre("Sistema");

        // Verificación de existencia y obtención del primer elemento de la lista
        if (!estadosActivos.isEmpty() && !categoriasSistema.isEmpty()) {
            Estado estadoActivo = estadosActivos.get(0);
            Categoria categoriaSistema = categoriasSistema.get(0);

            // 4. Cargar las fotos de ejemplo
            crearFotosDeEjemplo();

            // 5. Cargar el historial, usando los objetos persistidos
            crearHistorial(estadoActivo, categoriaSistema);
        } else {
            System.err.println("ERROR: No se pudieron encontrar las entidades 'Activo' o 'Sistema' después de la creación. Historial no cargado.");
        }

        System.out.println("Carga de datos de registros finalizada.");
    }

    /**
     * Crea y guarda las categorías predefinidas.
     */
    private void crearCategorias() {
        List<String> nombresCategorias = Arrays.asList("Sistema", "Incidente","Usuario","Mensaje","Ubicación","Reporte","Curso");
        nombresCategorias.forEach(nombre -> {
            // Verifica si la lista devuelta por findByNombre está vacía
            if (categoriaRepository.findByNombre(nombre).isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre);
                categoriaRepository.save(categoria);
            }
        });
    }

    /**
     * Crea y guarda los estados predefinidos para los usuarios.
     */
    private void crearEstados() {
        List<String> nombresEstados = Arrays.asList("Activo", "Baneado", "Inactivo","En Proceso","Localizado","Cerrado","Enviado","Recibido","Visto");
        nombresEstados.forEach(nombre -> {
            // Verifica si la lista devuelta por findByNombre está vacía
            if (estadoRepository.findByNombre(nombre).isEmpty()) {
                Estado estado = new Estado();
                estado.setNombre(nombre);
                estadoRepository.save(estado);
            }
        });
    }

    /**
     * Crea y guarda fotos de ejemplo con URLs ficticias.
     * NOTA: Carga directa sin verificar duplicados, ya que findByUrl no existe.
     */
    private void crearFotosDeEjemplo() {
        List<String> urlsDeEjemplo = Arrays.asList(
                "http://api.ejemplo.com/fotos/1.jpg",
                "http://api.ejemplo.com/fotos/2.jpg",
                "http://api.ejemplo.com/fotos/3.jpg"
        );
        urlsDeEjemplo.forEach(url -> {
            Foto foto = new Foto();
            foto.setUrl(url);
            foto.setDescripcion("Foto de ejemplo para DataLoader");
            foto.setFechaSubida(LocalDateTime.now());
            fotoRepository.save(foto);
        });
    }

    /**
     * Crea y guarda registros de historial de ejemplo.
     * @param estadoActivo El objeto Estado con el que se asociarán los registros.
     * @param categoriaSistema El objeto Categoria con el que se asociarán los registros.
     */
    private void crearHistorial(Estado estadoActivo, Categoria categoriaSistema) {
        if (historialRepository.count() == 0) {

            // Historial 1: Cambio de estado (No asociado a una asignación de usuario)
            Historial h1 = new Historial();
            h1.setEstado(estadoActivo);
            h1.setCategoria(categoriaSistema);
            h1.setFechaHistorial(LocalDateTime.now().minusDays(10));
            h1.setDetalle("Usuario 'admin' cambió el estado de un perfil a 'Activo'.");
            h1.setIdAsignacionUsuario(25);
            historialRepository.save(h1);

            // Historial 2: Creación de Reporte (Puede implicar una asignación inicial)
            Historial h2 = new Historial();
            h2.setEstado(estadoActivo);
            h2.setCategoria(categoriaSistema);
            h2.setFechaHistorial(LocalDateTime.now().minusDays(5));
            h2.setDetalle("Se creó un nuevo reporte de bombero.");
            h1.setIdAsignacionUsuario(5);
            h2.setIdUsuarioReporte(1);
            historialRepository.save(h2);

            // Historial 3: Envío de mensaje (No asociado a una asignación de usuario)
            Historial h3 = new Historial();
            h3.setEstado(estadoActivo);
            h3.setCategoria(categoriaSistema);
            h3.setFechaHistorial(LocalDateTime.now());
            h3.setDetalle("Se envió un mensaje de alerta por un incidente.");
            h3.setIdEnvioMensaje(10);
            historialRepository.save(h3);
        }
    }
}