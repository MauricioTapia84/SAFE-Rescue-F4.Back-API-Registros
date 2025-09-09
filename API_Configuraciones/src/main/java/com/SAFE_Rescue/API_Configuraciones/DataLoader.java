package com.SAFE_Rescue.API_Configuraciones;

import com.SAFE_Rescue.API_Configuraciones.modelo.Estado;
import com.SAFE_Rescue.API_Configuraciones.modelo.Foto;
import com.SAFE_Rescue.API_Configuraciones.repository.EstadoRepository;
import com.SAFE_Rescue.API_Configuraciones.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cargando datos de configuración iniciales...");

        // Cargar los estados
        crearEstados();

        // Cargar las fotos de ejemplo
        crearFotosDeEjemplo();

        System.out.println("Carga de datos de configuración finalizada.");
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
}