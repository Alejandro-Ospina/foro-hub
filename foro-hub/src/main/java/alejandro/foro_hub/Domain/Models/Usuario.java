package alejandro.foro_hub.Domain.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends UsuarioBase implements UserDetails {

    @Column (name = "password")
    private String pass;

    private Boolean activo;

    @ManyToMany (cascade = CascadeType.PERSIST)
    @JoinTable (
            name = "usuario_perfil",
            joinColumns = @JoinColumn (name = "usuario_id"),
            inverseJoinColumns = @JoinColumn (name = "perfil_id")
    )
    private Set<Perfil> perfiles;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.nombre;
    }

    @Override
    public String getPassword() {
        return this.pass;
    }
}