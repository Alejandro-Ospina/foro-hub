package alejandro.foro_hub.Domain.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table (name = "perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
public class Perfil {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany (mappedBy = "perfiles")
    private Set<Usuario> usuarios;
}
