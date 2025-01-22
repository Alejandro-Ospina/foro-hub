package alejandro.foro_hub.Domain.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "respuesta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (of = "id")
public class Respuesta {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @Column (
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @Column (columnDefinition = "TEXT")
    private String solucion;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
            name = "topico_id",
            nullable = false
    )
    private Topico topico;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
            name = "usuario_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private UsuarioBase autor;
}
