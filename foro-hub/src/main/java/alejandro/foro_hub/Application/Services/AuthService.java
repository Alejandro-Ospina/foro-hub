package alejandro.foro_hub.Application.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
