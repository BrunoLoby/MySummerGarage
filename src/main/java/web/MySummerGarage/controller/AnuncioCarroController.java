package web.MySummerGarage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.MySummerGarage.dto.AnuncioCarroDTOInput;
import web.MySummerGarage.filter.AnuncioCarroFilter;
import web.MySummerGarage.model.AnuncioCarro;
import web.MySummerGarage.model.StatusAnuncio;
import web.MySummerGarage.service.AnuncioCarroService;
import web.MySummerGarage.util.PaginaInfo;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

@Controller
@RequestMapping("/anuncio")
public class AnuncioCarroController {

    @Autowired
    private AnuncioCarroService anuncioCarroService;

    private boolean isHtmx(HttpServletRequest request) {
        return "true".equals(request.getHeader("HX-Request"));
    }


    @InitBinder
    public void configurarConversores(WebDataBinder binder) {
        // BigDecimal: remove separador de milhar (.) e troca a vírgula decimal por ponto
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                String limpo = text.trim().replace(".", "").replace(",", ".");
                setValue(new BigDecimal(limpo));
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : valor.toString();
            }
        });

        // Integer: mantém apenas os dígitos (remove o separador de milhar)
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null) {
                    setValue(null);
                    return;
                }
                String somenteDigitos = text.replaceAll("\\D", "");
                setValue(somenteDigitos.isEmpty() ? null : Integer.valueOf(somenteDigitos));
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : valor.toString();
            }
        });
    }

    // CADASTRAR
    @GetMapping("/cadastrar")
    public String abrirCadastrar(Model model, HttpServletRequest request) {
        model.addAttribute("anuncioDTOInput", new AnuncioCarroDTOInput());
        model.addAttribute("todosStatus", StatusAnuncio.values());
        if (isHtmx(request)) return "anuncio/cadastrar :: conteudo";
        return "anuncio/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("anuncioDTOInput") AnuncioCarroDTOInput dto,
                            BindingResult result, Model model,
                            Authentication auth,
                            HttpServletRequest request,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosStatus", StatusAnuncio.values());
            if (isHtmx(request)) return "anuncio/cadastrar :: conteudo";
            return "anuncio/cadastrar";
        }
        anuncioCarroService.salvar(dto, auth);
        redirect.addFlashAttribute("mensagemSucesso", "Anúncio cadastrado com sucesso!");
        return "redirect:/anuncio/pesquisar";
    }

    // PESQUISAR
    @GetMapping("/pesquisar")
    public String pesquisar(
            @ModelAttribute AnuncioCarroFilter filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "titulo") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model, HttpServletRequest request) {

        // O componente de paginação envia o sort no formato "campo,direcao" (ex: titulo,desc).
        // Separamos o campo da direção para montar o Sort corretamente.
        String[] partesSort = sort.split(",");
        String campo = partesSort[0];
        Sort.Direction direcao = (partesSort.length > 1)
                ? Sort.Direction.fromString(partesSort[1])
                : (dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direcao, campo));
        Page<AnuncioCarro> resultado = anuncioCarroService.pesquisar(filtro, pageable);

        String urlBase = "/anuncio/pesquisar?titulo=" + nvl(filtro.getTitulo()) +
                         "&marca=" + nvl(filtro.getMarca()) +
                         "&status=" + nvl(filtro.getStatus()) +
                         "&sort=" + campo + "&dir=" + direcao.name().toLowerCase();

        model.addAttribute("pagina", new PaginaInfo(resultado, urlBase));
        model.addAttribute("anuncios", resultado.getContent());
        model.addAttribute("filtro", filtro);
        model.addAttribute("todosStatus", StatusAnuncio.values());
        if (isHtmx(request)) return "anuncio/pesquisar :: conteudo";
        return "anuncio/pesquisar";
    }

    // ALTERAR
    @GetMapping("/alterar/{codigo}")
    public String abrirAlterar(@PathVariable Long codigo, Model model, HttpServletRequest request) {
        AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        model.addAttribute("anuncioDTOInput", anuncioCarroService.toDTO(anuncio));
        model.addAttribute("todosStatus", StatusAnuncio.values());
        if (isHtmx(request)) return "anuncio/alterar :: conteudo";
        return "anuncio/alterar";
    }

    @PostMapping("/alterar/{codigo}")
    public String alterar(@PathVariable Long codigo,
                          @Valid @ModelAttribute("anuncioDTOInput") AnuncioCarroDTOInput dto,
                          BindingResult result, Model model,
                          HttpServletRequest request,
                          RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosStatus", StatusAnuncio.values());
            if (isHtmx(request)) return "anuncio/alterar :: conteudo";
            return "anuncio/alterar";
        }
        anuncioCarroService.atualizar(codigo, dto);
        redirect.addFlashAttribute("mensagemSucesso", "Anúncio atualizado com sucesso!");
        return "redirect:/anuncio/pesquisar";
    }

    // VISUALIZAR
    @GetMapping("/visualizar/{codigo}")
    public String visualizar(@PathVariable Long codigo, Model model, HttpServletRequest request) {
        AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        model.addAttribute("anuncio", anuncio);
        if (isHtmx(request)) return "anuncio/visualizar :: conteudo";
        return "anuncio/visualizar";
    }

    // EXCLUIR
    @PostMapping("/excluir/{codigo}")
    public String excluir(@PathVariable Long codigo, RedirectAttributes redirect) {
        anuncioCarroService.excluir(codigo);
        redirect.addFlashAttribute("mensagemSucesso", "Anúncio excluído com sucesso!");
        return "redirect:/anuncio/pesquisar";
    }

    private String nvl(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}