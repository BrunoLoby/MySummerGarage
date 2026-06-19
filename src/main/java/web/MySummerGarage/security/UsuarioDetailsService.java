package web.MySummerGarage.security;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.MySummerGarage.model.Usuario;
import web.MySummerGarage.repository.UsuarioRepository;

/**
 * Carrega o usuário a partir da tabela "usuario" para autenticação do Spring Security.
 * O login é feito pelo campo nome_usuario; os perfis viram authorities no formato ROLE_<NOME>.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + nomeUsuario));

        List<GrantedAuthority> authorities = usuario.getPerfis().stream()
                .map(perfil -> (GrantedAuthority) new SimpleGrantedAuthority(
                        "ROLE_" + perfil.getNome().toUpperCase()))
                .toList();

        return User.withUsername(usuario.getNomeUsuario())
                .password(usuario.getSenha())
                .authorities(authorities)
                .disabled(!usuario.isAtivo())
                .build();
    }
}
