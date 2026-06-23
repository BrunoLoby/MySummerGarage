package web.MySummerGarage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.MySummerGarage.dto.UsuarioDTOInput;
import web.MySummerGarage.model.Usuario;
import web.MySummerGarage.service.UsuarioService;
import web.MySummerGarage.util.PaginaInfo;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private boolean isHtmx(HttpServletRequest request) {
        return "true".equals(request.getHeader("HX-Request"));
    }

    @GetMapping("/cadastrar")
    public String abrirCadastrar(Model model, HttpServletRequest request) {
        model.addAttribute("usuarioDTOInput", new UsuarioDTOInput());
        model.addAttribute("todosPapeis", usuarioService.listarPerfis());
        if (isHtmx(request)) return "usuario/cadastrar :: conteudo";
        return "usuario/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("usuarioDTOInput") UsuarioDTOInput dto,
                            BindingResult result, Model model,
                            HttpServletRequest request,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosPapeis", usuarioService.listarPerfis());
            if (isHtmx(request)) return "usuario/cadastrar :: conteudo";
            return "usuario/cadastrar";
        }
        usuarioService.salvar(dto);
        redirect.addFlashAttribute("mensagemSucesso", "Usuário cadastrado com sucesso!");
        return "redirect:/usuario/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model, HttpServletRequest request) {

        Sort.Direction direcao = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direcao, sort));
        Page<Usuario> resultado = usuarioService.listarTodos(pageable);
        String urlBase = "/usuario/pesquisar?sort=" + sort + "&dir=" + dir;
        model.addAttribute("pagina", new PaginaInfo(resultado, urlBase));
        model.addAttribute("usuarios", resultado.getContent());
        if (isHtmx(request)) return "usuario/pesquisar :: conteudo";
        return "usuario/pesquisar";
    }

    @GetMapping("/alterar/{codigo}")
    public String abrirAlterar(@PathVariable Long codigo, Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuarioDTOInput", usuarioService.toDTO(usuario));
        model.addAttribute("todosPapeis", usuarioService.listarPerfis());
        if (isHtmx(request)) return "usuario/alterar :: conteudo";
        return "usuario/alterar";
    }

    @PostMapping("/alterar/{codigo}")
    public String alterar(@PathVariable Long codigo,
                          @Valid @ModelAttribute("usuarioDTOInput") UsuarioDTOInput dto,
                          BindingResult result, Model model,
                          HttpServletRequest request,
                          RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosPapeis", usuarioService.listarPerfis());
            if (isHtmx(request)) return "usuario/alterar :: conteudo";
            return "usuario/alterar";
        }
        usuarioService.atualizar(codigo, dto);
        redirect.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso!");
        return "redirect:/usuario/pesquisar";
    }

    @PostMapping("/excluir/{codigo}")
    public String excluir(@PathVariable Long codigo, RedirectAttributes redirect) {
        usuarioService.excluir(codigo);
        redirect.addFlashAttribute("mensagemSucesso", "Usuário excluído com sucesso!");
        return "redirect:/usuario/pesquisar";
    }
}