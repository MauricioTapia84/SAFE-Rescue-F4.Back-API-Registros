package com.SAFE_Rescue.API_Registros.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa el historial de eventos, cambios de estado y acciones dentro del sistema.
 * Sirve para mantener un registro de auditoría de todas las operaciones relevantes.
 */
@Entity
@Table(name = "historial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Detalles de los registros de historial del sistema")
public class Historial {

    @Id
    @Column(name = "id_historial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del registro de historial", example = "1")
    private int idHistorial;

    /**
     * Relación Muchos-a-uno con la entidad Estado.
     * Representa el estado que se registró en este punto del historial.
     */
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    @Schema(description = "Estado asociado a este registro de historial")
    private Estado estado;

    @Column(name = "fecha_historial", nullable = false)
    @Schema(description = "Fecha y hora en que se registró el historial", example = "2025-09-09T10:30:00")
    private LocalDateTime fechaHistorial;

    @Column(name = "detalle", length = 255, nullable = false)
    @Schema(description = "Descripción detallada del evento del historial", example = "El usuario 'juan_perez' cambió su estado a 'Activo'")
    private String detalle;

    /**
     * Campo opcional para registrar el ID de una asignación de incidente.
     * Solo se utiliza si el evento está relacionado con la asignación de un incidente.
     */
    @Column(name = "id_asignacion_incidente", nullable = true)
    @Schema(description = "Identificador opcional de la asignación del incidente", example = "101")
    private Integer idAsignacionIncidente;

    /**
     * Campo opcional para registrar el ID de un envío de mensaje.
     * Solo se utiliza si el evento está relacionado con el envío de un mensaje.
     */
    @Column(name = "id_envio_mensaje", nullable = true)
    @Schema(description = "Identificador opcional del envío de mensaje", example = "205")
    private Integer idEnvioMensaje;

    /**
     * Campo opcional para registrar el ID de un incidente.
     * Solo se utiliza si el evento está relacionado con un incidente.
     */
    @Column(name = "id_incidente", nullable = true)
    @Schema(description = "Identificador opcional del incidente", example = "300")
    private Integer idIncidente;

    /**
     * Campo opcional para registrar el ID de una dirección.
     * Solo se utiliza si el evento está relacionado con una dirección.
     */
    @Column(name = "id_direccion", nullable = true)
    @Schema(description = "Identificador opcional de la dirección", example = "45")
    private Integer idDireccion;

    /**
     * Campo opcional para registrar el ID del usuario que realizó un reporte.
     * Solo se utiliza si el evento está relacionado con un reporte de usuario.
     */
    @Column(name = "id_usuario_reporte", nullable = true)
    @Schema(description = "Identificador opcional del usuario que realizó el reporte", example = "789")
    private Integer idUsuarioReporte;

    /**
     * Campo opcional para registrar el ID de una asignación de curso.
     * Solo se utiliza si el evento está relacionado con la asignación de un curso.
     */
    @Column(name = "id_asignacion_curso", nullable = true)
    @Schema(description = "Identificador opcional de la asignación del curso", example = "50")
    private Integer idAsignacionCurso;
}