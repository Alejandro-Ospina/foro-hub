package alejandro.foro_hub.Domain.Models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode (of = "id")
@Inheritance (strategy = InheritanceType.JOINED)
@Table (name = "usuarios_base")
public class UsuarioBase {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String nombre;
    protected String email;
}
