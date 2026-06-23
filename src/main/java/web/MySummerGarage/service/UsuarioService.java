package web.MySummerGarage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.MySummerGarage.dto.UsuarioDTOInput;
import web.MySummerGarage.model.Perfil;
import web.MySummerGarage.model.Usuario;
import web.MySummerGarage.repository.PerfilRepository;
import web.MySummerGarage.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);
    }

    public List<Perfil> listarPerfis() {
        return perfilRepository.findAll();
    }

    @Transactional
    public Usuario salvar(UsuarioDTOInput dto) {
        Usuario usuario = new Usuario();
        preencherUsuario(usuario, dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long codigo, UsuarioDTOInput dto) {
        Usuario usuario = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + codigo));
        preencherUsuario(usuario, dto);
        // Só atualiza senha se uma nova foi informada
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long codigo) {
        usuarioRepository.deleteById(codigo);
    }

    public UsuarioDTOInput toDTO(Usuario usuario) {
        UsuarioDTOInput dto = new UsuarioDTOInput();
        dto.setCodigo(usuario.getCodigo());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setAtivo(usuario.isAtivo());
        List<Long> papeis = usuario.getPerfis().stream()
                .map(Perfil::getCodigo)
                .toList();
        dto.setPapeis(papeis);
        return dto;
    }

    private void preencherUsuario(Usuario usuario, UsuarioDTOInput dto) {
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setAtivo(dto.isAtivo());

        List<Perfil> perfis = perfilRepository.findAllById(dto.getPapeis());
        usuario.setPerfis(perfis);
    }
}