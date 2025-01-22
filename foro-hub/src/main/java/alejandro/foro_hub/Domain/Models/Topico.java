package alejandro.foro_hub.Domain.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Topico {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;

    @Column (
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;
    private Boolean status;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
            name = "usuario_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private UsuarioBase autor;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
            name = "curso_id",
            nullable = false
    )
    private Curso curso;

    @OneToMany (
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "topico",
            cascade = CascadeType.ALL
    )
    private List<Respuesta> respuestas;
}