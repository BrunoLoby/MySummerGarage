package web.MySummerGarage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.MySummerGarage.model.AnuncioCarro;
import web.MySummerGarage.model.StatusAnuncio;

public interface AnuncioCarroRepository extends JpaRepository<AnuncioCarro, Long> {

    @Query("SELECT a FROM AnuncioCarro a WHERE " +
           "LOWER(a.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')) AND " +
           "LOWER(a.marca) LIKE LOWER(CONCAT('%', :marca, '%')) AND " +
           "(:status IS NULL OR a.status = :status)")
    Page<AnuncioCarro> pesquisar(
            @Param("titulo") String titulo,
            @Param("marca") String marca,
            @Param("status") StatusAnuncio status,
            Pageable pageable);

    @Query("SELECT a FROM AnuncioCarro a WHERE " +
           "a.vendedor.nomeUsuario = :nomeUsuario AND " +
           "LOWER(a.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')) AND " +
           "LOWER(a.marca) LIKE LOWER(CONCAT('%', :marca, '%')) AND " +
           "(:status IS NULL OR a.status = :status)")
    Page<AnuncioCarro> pesquisarPorVendedor(
            @Param("nomeUsuario") String nomeUsuario,
            @Param("titulo") String titulo,
            @Param("marca") String marca,
            @Param("status") StatusAnuncio status,
            Pageable pageable);
}