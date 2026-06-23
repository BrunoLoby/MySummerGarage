package web.MySummerGarage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private AnuncioCarroRepository anuncioCarroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Page<AnuncioCarro> pesquisar(AnuncioCarroFilter filtro, Pageable pageable) {
        String titulo = (filtro.getTitulo() != null && !filtro.getTitulo().isBlank()) ? filtro.getTitulo() : "";
        String marca = (filtro.getMarca() != null && !filtro.getMarca().isBlank()) ? filtro.getMarca() : "";
        return anuncioCarroRepository.pesquisar(titulo, marca, filtro.getStatus(), pageable);
    }

    public Optional<AnuncioCarro> buscarPorCodigo(Long codigo) {
        return anuncioCarroRepository.findById(codigo);
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
    public AnuncioCarro atualizar(Long codigo, AnuncioCarroDTOInput dto) {
        AnuncioCarro anuncio = anuncioCarroRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado: " + codigo));
        preencherAnuncio(anuncio, dto);
        return anuncioCarroRepository.save(anuncio);
    }

    @Transactional
    public void excluir(Long codigo) {
        anuncioCarroRepository.deleteById(codigo);
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