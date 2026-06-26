package web.MySummerGarage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.MySummerGarage.dto.AnuncioCarroDTOInput;
import web.MySummerGarage.filter.AnuncioCarroFilter;
import web.MySummerGarage.model.AnuncioCarro;
import web.MySummerGarage.model.StatusAnuncio;
import web.MySummerGarage.model.Usuario;
import web.MySummerGarage.repository.AnuncioCarroRepository;
import web.MySummerGarage.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AnuncioCarroService {

    private final AnuncioCarroRepository anuncioCarroRepository;

    private final UsuarioRepository usuarioRepository;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    AnuncioCarroService(AnuncioCarroRepository anuncioCarroRepository, UsuarioRepository usuarioRepository) {
        this.anuncioCarroRepository = anuncioCarroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<AnuncioCarro> pesquisar(AnuncioCarroFilter filtro, Pageable pageable) {
        String titulo = (filtro.getTitulo() != null && !filtro.getTitulo().isBlank()) ? filtro.getTitulo() : "";
        String marca = (filtro.getMarca() != null && !filtro.getMarca().isBlank()) ? filtro.getMarca() : "";
        return anuncioCarroRepository.pesquisar(titulo, marca, filtro.getStatus(), pageable);
    }

    /**
     * Pesquisa para a tela de gerenciamento. O ADMIN enxerga todos os anúncios;
     * qualquer outro usuário enxerga apenas os seus próprios.
     */
    public Page<AnuncioCarro> pesquisarParaGerenciamento(AnuncioCarroFilter filtro, Pageable pageable, Authentication auth) {
        if (isAdmin(auth)) {
            return pesquisar(filtro, pageable);
        }
        String titulo = (filtro.getTitulo() != null && !filtro.getTitulo().isBlank()) ? filtro.getTitulo() : "";
        String marca = (filtro.getMarca() != null && !filtro.getMarca().isBlank()) ? filtro.getMarca() : "";
        return anuncioCarroRepository.pesquisarPorVendedor(auth.getName(), titulo, marca, filtro.getStatus(), pageable);
    }

    public Optional<AnuncioCarro> buscarPorCodigo(Long codigo) {
        return anuncioCarroRepository.findById(codigo);
    }

    /**
     * Busca um anúncio garantindo que o usuário autenticado tem permissão para gerenciá-lo
     * (é o dono ou é ADMIN). Caso contrário, lança AccessDeniedException.
     */
    public AnuncioCarro buscarParaGerenciar(Long codigo, Authentication auth) {
        AnuncioCarro anuncio = anuncioCarroRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado: " + codigo));
        verificarPermissao(anuncio, auth);
        return anuncio;
    }

    @Transactional
    public AnuncioCarro salvar(AnuncioCarroDTOInput dto, Authentication auth) {
        AnuncioCarro anuncio = new AnuncioCarro();
        preencherAnuncio(anuncio, dto);

        Usuario vendedor = usuarioRepository.findByNomeUsuario(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        anuncio.setVendedor(vendedor);

        return anuncioCarroRepository.save(anuncio);
    }

    @Transactional
    public AnuncioCarro atualizar(Long codigo, AnuncioCarroDTOInput dto, Authentication auth) {
        AnuncioCarro anuncio = anuncioCarroRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado: " + codigo));
        verificarPermissao(anuncio, auth);
        preencherAnuncio(anuncio, dto);
        return anuncioCarroRepository.save(anuncio);
    }

    @Transactional
    public void excluir(Long codigo, Authentication auth) {
        AnuncioCarro anuncio = anuncioCarroRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado: " + codigo));
        verificarPermissao(anuncio, auth);
        anuncioCarroRepository.delete(anuncio);
    }

    /** Indica se o usuário autenticado possui o perfil ADMIN. */
    public boolean isAdmin(Authentication auth) {
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    /**
     * Garante que o usuário autenticado pode gerenciar o anúncio: precisa ser ADMIN
     * ou o próprio vendedor dono do anúncio.
     */
    private void verificarPermissao(AnuncioCarro anuncio, Authentication auth) {
        if (isAdmin(auth)) {
            return;
        }
        boolean ehDono = auth != null
                && anuncio.getVendedor() != null
                && auth.getName().equals(anuncio.getVendedor().getNomeUsuario());
        if (!ehDono) {
            throw new AccessDeniedException("Você não tem permissão para gerenciar este anúncio.");
        }
    }

    public AnuncioCarroDTOInput toDTO(AnuncioCarro anuncio) {
        AnuncioCarroDTOInput dto = new AnuncioCarroDTOInput();
        dto.setCodigo(anuncio.getCodigo());
        dto.setTitulo(anuncio.getTitulo());
        dto.setMarca(anuncio.getMarca());
        dto.setModelo(anuncio.getModelo());
        dto.setAno(anuncio.getAno());
        dto.setValor(anuncio.getValor());
        dto.setCor(anuncio.getCor());
        dto.setQuilometragem(anuncio.getQuilometragem());
        dto.setDescricao(anuncio.getDescricao());
        if (anuncio.getDataPublicacao() != null) {
            dto.setDataPublicacao(anuncio.getDataPublicacao().format(FORMATO_DATA));
        }
        return dto;
    }

    private void preencherAnuncio(AnuncioCarro anuncio, AnuncioCarroDTOInput dto) {
        anuncio.setTitulo(dto.getTitulo());
        anuncio.setMarca(dto.getMarca());
        anuncio.setModelo(dto.getModelo());
        anuncio.setAno(dto.getAno());
        anuncio.setValor(dto.getValor());
        anuncio.setCor(dto.getCor());
        anuncio.setQuilometragem(dto.getQuilometragem());
        anuncio.setDescricao(dto.getDescricao());
        if (dto.getDataPublicacao() != null && !dto.getDataPublicacao().isBlank()) {
            anuncio.setDataPublicacao(LocalDate.parse(dto.getDataPublicacao(), FORMATO_DATA));
        }
    }

    public StatusAnuncio[] listarStatus() {
        return StatusAnuncio.values();
    }
}