package com.SAFE_Rescue.API_Registros.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa el estado de una entidad o registro dentro del sistema.
 * <p>
 * Esta clase se utiliza para estandarizar los posibles estados (por ejemplo, Activo, Inactivo,
 * Pendiente, Eliminado) que pueden tener otras entidades como usuarios, incidentes o categorías.
 * Se mapea a la tabla "estado" en la base de datos.
 * </p>
 *
 */
@Entity
@Table(name = "estado")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Estado {

    /**
     * Identificador único del estado. Es la clave primaria (Primary Key).
     * <p>
     * La estrategia {@code GenerationType.IDENTITY} permite que la base de datos
     * genere automáticamente el valor (auto-incremento).
     * </p>
     */
    @Id
    @Column(name = "id_estado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del estado", example = "1")
    private int idEstado;

    /**
     * Nombre descriptivo del estado. Debe ser único para evitar ambigüedad.
     * <p>
     * Restricciones: Máximo 50 caracteres y no puede ser nulo ({@code nullable = false}).
     * Ejemplos: "Activo", "Resuelto", "En Proceso".
     * </p>
     */
    @Column(name = "nombre", unique = true, length = 50, nullable = false)
    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    /**
     * Descripción detallada del estado. Este campo es opcional.
     * <p>
     * Restricción: Máximo 100 caracteres.
     * </p>
     */
    @Column(name = "descripcion", length = 100, nullable = true)
    @Schema(description = "Descripción del estado", example = "El usuario está activo en el sistema.")
    private String descripcion;
}