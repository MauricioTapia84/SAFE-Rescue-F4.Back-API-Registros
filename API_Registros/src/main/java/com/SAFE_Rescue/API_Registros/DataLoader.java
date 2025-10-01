package com.SAFE_Rescue.API_Registros;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Foto;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
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

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private HistorialRepository historialRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cargando datos de registros iniciales...");

        // Cargar los estados
        crearEstados();

        // CORRECCIÓN CRÍTICA: Obtener el estado 'Activo' persistido
        List<Estado> estadoActivoList = estadoRepository.findByNombre("Activo");

        // Verificar que el estado se haya creado
        if (estadoActivoList!=null) {
            Estado estadoActivo = estadoActivoList.get(0);

            // 2. Cargar las fotos de ejemplo
            crearFotosDeEjemplo();

            // 3. Cargar el historial, usando el objeto Estado persistido
            crearHistorial(estadoActivo);
        } else {
            System.err.println("ERROR: No se pudo encontrar el estado 'Activo' después de la creación. Historial no cargado.");
        }

        System.out.println("Carga de datos de registros finalizada.");
    }

    /**
     * Crea y guarda los estados predefinidos para los usuarios.
     */
    private void crearEstados() {
        List<String> nombresEstados = Arrays.asList("Activo", "Baneado", "Inactivo");
        nombresEstados.forEach(nombre -> {
            if (estadoRepository.findByNombre(nombre).isEmpty()) {
                Estado estado = new Estado();
                estado.setNombre(nombre);
                estadoRepository.save(estado);
            }
        });
    }

    /**
     * Crea y guarda fotos de ejemplo con URLs ficticias.
     */
    private void crearFotosDeEjemplo() {
        List<String> urlsDeEjemplo = Arrays.asList(
                "http://api.ejemplo.com/fotos/1.jpg",
                "http://api.ejemplo.com/fotos/2.jpg",
                "http://api.ejemplo.com/fotos/3.jpg"
        );
        urlsDeEjemplo.forEach(url -> {
            // Se asume que FotoRepository tiene un método findByUrl
            if (fotoRepository.findByUrl(url).isEmpty()) {
                Foto foto = new Foto();
                foto.setUrl(url);
                fotoRepository.save(foto);
            }
        });
    }

    /**
     * Crea y guarda registros de historial de ejemplo.
     * @param estadoActivo El objeto Estado con el que se asociarán los registros.
     */
    private void crearHistorial(Estado estadoActivo) {
        if (historialRepository.count() == 0) {
            Historial h1 = new Historial();
            h1.setEstado(estadoActivo);
            h1.setFechaHistorial(LocalDateTime.now().minusDays(10));
            h1.setDetalle("Usuario 'admin' cambió el estado de un perfil a 'Activo'.");
            historialRepository.save(h1);

            Historial h2 = new Historial();
            h2.setEstado(estadoActivo);
            h2.setFechaHistorial(LocalDateTime.now().minusDays(5));
            h2.setDetalle("Se creó un nuevo usuario bombero con ID 5.");
            h2.setIdUsuarioReporte(5);
            historialRepository.save(h2);

            Historial h3 = new Historial();
            h3.setEstado(estadoActivo);
            h3.setFechaHistorial(LocalDateTime.now());
            h3.setDetalle("Se envió un mensaje de alerta por un incidente.");
            h3.setIdEnvioMensaje(10);
            h3.setIdIncidente(20);
            historialRepository.save(h3);
        }
    }
}