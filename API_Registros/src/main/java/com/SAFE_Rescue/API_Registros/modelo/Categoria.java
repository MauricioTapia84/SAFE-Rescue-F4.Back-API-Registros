package com.SAFE_Rescue.API_Registros.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una Categoría dentro del sistema.
 * <p>
 * Esta clase se mapea a la tabla "categoria" en la base de datos
 * y se utiliza para clasificar o agrupar diferentes tipos de registros o entidades.
 * Utiliza anotaciones de Lombok para reducir el boilerplate (código repetitivo)
 * y anotaciones de Swagger para documentación de la API.
 * </p>
 */
@Entity
@Table(name = "categoria")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Categoria {

    /**
     * Identificador único de la categoría. Es la clave primaria (Primary Key).
     * <p>
     * La estrategia de generación {@code GenerationType.IDENTITY} indica que la
     * base de datos gestionará la asignación automática de valores (auto-incremento).
     * </p>
     */
    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la categoría", example = "1")
    private int idCategoria;

    /**
     * Nombre de la categoría. Debe ser único y no puede ser nulo.
     * <p>
     * Longitud máxima permitida: 50 caracteres.
     * </p>
     */
    @Column(name = "nombre", unique = true, length = 50, nullable = false)
    @Schema(description = "Nombre de la categoría", example = "Incidente")
    private String nombre;

    /**
     * Descripción opcional de la categoría. Puede ser nula.
     * <p>
     * Longitud máxima permitida: 100 caracteres.
     * </p>
     */
    @Column(name = "descripcion", length = 100, nullable = true)
    @Schema(description = "Descripción de la Categoría", example = "La Categoría representan un incidente.")
    private String descripcion;
}