package alejandro.foro_hub.Domain.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios_google")
public class GoogleUser extends UsuarioBase implements Serializable {

    private String sub;
    private String foto;
    private Boolean activo;
}
