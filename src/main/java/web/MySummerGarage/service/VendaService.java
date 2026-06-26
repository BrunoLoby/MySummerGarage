package web.MySummerGarage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.MySummerGarage.dto.VendaDTOInput;
import web.MySummerGarage.model.AnuncioCarro;
import web.MySummerGarage.model.Pagamento;
import web.MySummerGarage.model.StatusAnuncio;
import web.MySummerGarage.model.StatusPagamento;
import web.MySummerGarage.model.StatusVenda;
import web.MySummerGarage.model.Usuario;
import web.MySummerGarage.model.Venda;
import web.MySummerGarage.repository.AnuncioCarroRepository;
import web.MySummerGarage.repository.PagamentoRepository;
import web.MySummerGarage.repository.UsuarioRepository;
import web.MySummerGarage.repository.VendaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AnuncioCarroRepository anuncioCarroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<Venda> listarTodas(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    public Page<Venda> listarPorComprador(String nomeUsuario, Pageable pageable) {
        return vendaRepository.findByCompradorNomeUsuario(nomeUsuario, pageable);
    }

    public Optional<Venda> buscarPorCodigo(Long codigo) {
        return vendaRepository.findById(codigo);
    }

    public List<Pagamento> listarPagamentos(Venda venda) {
        return pagamentoRepository.findByVenda(venda);
    }

    @Transactional
    public Venda registrarVenda(VendaDTOInput dto, Authentication auth) {
        // Busca o anúncio
        AnuncioCarro anuncio = anuncioCarroRepository.findById(dto.getCodigoAnuncio())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        if (anuncio.getStatus() != StatusAnuncio.ATIVO) {
            throw new RuntimeException("Este anúncio não está disponível para venda");
        }

        // Busca o comprador logado
        Usuario comprador = usuarioRepository.findByNomeUsuario(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Impede que o vendedor compre o próprio anúncio
        if (anuncio.getVendedor() != null
                && anuncio.getVendedor().getCodigo().equals(comprador.getCodigo())) {
            throw new RuntimeException("Você não pode comprar o seu próprio anúncio");
        }

        // Cria a venda
        Venda venda = new Venda();
        venda.setAnuncio(anuncio);
        venda.setComprador(comprador);
        venda.setValorFinal(dto.getValorFinal());
        venda.setDataVenda(LocalDate.now());
        venda.setStatus(StatusVenda.FINALIZADA);
        vendaRepository.save(venda);

        // Cria o pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setVenda(venda);
        pagamento.setTipo(dto.getTipoPagamento());
        pagamento.setParcelas(dto.getParcelas());
        pagamento.setValor(dto.getValorFinal());
        pagamento.setStatus(StatusPagamento.CONCLUIDO);
        pagamentoRepository.save(pagamento);

        // Atualiza o status do anúncio para VENDIDO
        anuncio.setStatus(StatusAnuncio.VENDIDO);
        anuncioCarroRepository.save(anuncio);

        return venda;
    }
}