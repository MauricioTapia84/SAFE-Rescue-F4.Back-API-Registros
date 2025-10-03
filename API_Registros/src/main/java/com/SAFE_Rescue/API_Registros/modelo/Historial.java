package com.SAFE_Rescue.API_Registros.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa el **registro de auditoría** de eventos, cambios de estado
 * y acciones importantes dentro del sistema (historial de logs).
 * <p>
 * Su objetivo principal es mantener un registro inmutable de todas las operaciones
 * relevantes para fines de trazabilidad, depuración o cumplimiento.
 * Se mapea a la tabla "historial" en la base de datos.
 * </p>
 *
 */
@Entity
@Table(name = "historial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Detalles de los registros de historial del sistema")
public class Historial {

    /**
     * Identificador único del registro de historial. Es la clave primaria (Primary Key).
     */
    @Id
    @Column(name = "id_historial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del registro de historial", example = "1")
    private int idHistorial;

    // -------------------------------------------------------------------------
    // RELACIONES M-A-1
    // -------------------------------------------------------------------------

    /**
     * Relación Muchos-a-Uno con la entidad {@code Estado}.
     * Representa el estado que se registró en el momento del evento del historial
     * (e.g., "Incidente Abierto", "Usuario Bloqueado").
     */
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    @Schema(description = "Estado asociado a este registro de historial")
    private Estado estado;

    /**
     * Relación Muchos-a-Uno con la entidad {@code Categoria}.
     * Representa la categoría del evento registrado (e.g., "Evento de Sistema",
     * "Cambio de Estado", "Registro de Incidente").
     *
     * <p>
     * de {@code id_estado} a **{@code id_categoria}** para que apunte
     * a la tabla correcta.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @Schema(description = "Categoría asociada a este registro de historial")
    private Categoria categoria;

    // -------------------------------------------------------------------------
    // CAMPOS DE VALOR
    // -------------------------------------------------------------------------

    /**
     * Fecha y hora exacta en que se creó el registro de historial.
     * <p>
     * Utiliza {@code LocalDateTime} y es un campo obligatorio.
     * </p>
     */
    @Column(name = "fecha_historial", nullable = false)
    @Schema(description = "Fecha y hora en que se registró el historial", example = "2025-09-09T10:30:00")
    private LocalDateTime fechaHistorial;

    /**
     * Descripción detallada y obligatoria del evento registrado.
     * <p>
     * Longitud máxima: 250 caracteres.
     * </p>
     */
    @Column(name = "detalle", length = 250, nullable = false)
    @Schema(description = "Descripción detallada del evento del historial", example = "El usuario 'juan_perez' cambió su estado a 'Activo'")
    private String detalle;

    // -------------------------------------------------------------------------
    // CLAVES FORÁNEAS OPCIONALES DE REFERENCIA
    // -------------------------------------------------------------------------

    /**
     * Identificador opcional del registro de Asignación de Incidente relacionado.
     * Es {@code nullable = true} porque no todo evento de historial se relaciona
     * con una asignación.
     */
    @Column(name = "id_asignacion_incidente", nullable = true)
    @Schema(description = "Identificador opcional de la asignación del incidente", example = "101")
    private Integer idAsignacionIncidente;

    /**
     * Identificador opcional del registro de Asignación de Usuario relacionado.
     * Es {@code nullable = true} porque no todo evento de historial se relaciona
     * con una asignación.
     */
    @Column(name = "id_asignacion_usuario", nullable = true)
    @Schema(description = "Identificador opcional de la asignación del usuario", example = "101")
    private Integer idAsignacionUsuario;

    /**
     * Identificador opcional del registro de Envío de Mensaje relacionado.
     */
    @Column(name = "id_envio_mensaje", nullable = true)
    @Schema(description = "Identificador opcional del envío de mensaje", example = "205")
    private Integer idEnvioMensaje;

    /**
     * Identificador opcional de la entidad Dirección relacionada.
     */
    @Column(name = "id_direccion", nullable = true)
    @Schema(description = "Identificador opcional de la dirección", example = "45")
    private Integer idDireccion;

    /**
     * Identificador opcional del usuario que generó un Reporte.
     */
    @Column(name = "id_usuario_reporte", nullable = true)
    @Schema(description = "Identificador opcional del usuario que realizó el reporte", example = "789")
    private Integer idUsuarioReporte;

    /**
     * Identificador opcional del registro de Asignación de Curso relacionado.
     */
    @Column(name = "id_asignacion_curso", nullable = true)
    @Schema(description = "Identificador opcional de la asignación del curso", example = "50")
    private Integer idAsignacionCurso;
}