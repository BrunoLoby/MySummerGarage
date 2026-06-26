package web.MySummerGarage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.MySummerGarage.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT v FROM Venda v WHERE " +
           "(:nomeComprador IS NULL OR LOWER(v.comprador.nome) LIKE LOWER(CONCAT('%', :nomeComprador, '%')))")
    Page<Venda> pesquisar(
            @Param("nomeComprador") String nomeComprador,
            Pageable pageable);

    Page<Venda> findByCompradorNomeUsuario(String nomeUsuario, Pageable pageable);
}