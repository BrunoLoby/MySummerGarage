package web.MySummerGarage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import web.MySummerGarage.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);
}
