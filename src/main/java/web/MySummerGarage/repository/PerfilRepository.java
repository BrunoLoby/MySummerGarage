package web.MySummerGarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.MySummerGarage.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}