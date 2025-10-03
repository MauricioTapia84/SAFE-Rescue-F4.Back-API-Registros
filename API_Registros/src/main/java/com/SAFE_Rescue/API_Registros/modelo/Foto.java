package com.SAFE_Rescue.API_Registros.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la foto de perfil o una imagen asociada a un registro en el sistema.
 * <p>
 * Se utiliza para almacenar la URL externa de la imagen y la fecha de subida,
 * y generalmente se asocia con la entidad de un Usuario o de otro registro que requiera una imagen.
 * Se mapea a la tabla "foto" en la base de datos.
 * </p>
 *
 */
@Entity
@Table(name = "foto")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Foto {

    /**
     * Identificador único de la foto. Es la clave primaria (Primary Key).
     * <p>
     * La estrategia {@code GenerationType.IDENTITY} permite que la base de datos
     * genere automáticamente el valor (auto-incremento).
     * </p>
     */
    @Id
    @Column(name = "id_foto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la foto", example = "1")
    private int idFoto;

    /**
     * URL absoluta que apunta a la ubicación de la foto.
     * <p>
     * Este valor es **obligatorio** (no nulo) y tiene una longitud máxima de 255 caracteres,
     * adecuada para una URL de almacenamiento en la nube o CDN.
     * </p>
     */
    @Column(name = "url", length = 255, nullable = false)
    @Schema(description = "URL de la foto del usuario", example = "http://api-fotos.com/fotos/user123.jpg")
    private String url;

    /**
     * Fecha y hora exacta en la que se subió o se registró la foto.
     * <p>
     * Se utiliza {@code LocalDateTime} para capturar tanto la fecha como la hora,
     * y es un campo obligatorio (no nulo).
     * </p>
     */
    @Column(name = "fecha_subida", nullable = false)
    @Schema(description = "Fecha y hora en que se subió la Foto", example = "2025-09-09T10:30:00")
    private LocalDateTime fechaSubida;

    /**
     * Descripción detallada de la foto. Este campo es opcional.
     * <p>
     * Restricción: Máximo 100 caracteres.
     * </p>
     */
    @Column(name = "descripcion", length = 100, nullable = true)
    @Schema(description = "Descripción de la foto", example = "Fotografía de incidente.")
    private String descripcion;

}