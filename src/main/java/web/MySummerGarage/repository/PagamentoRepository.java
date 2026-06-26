package web.MySummerGarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.MySummerGarage.model.Pagamento;
import web.MySummerGarage.model.Venda;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByVenda(Venda venda);
}