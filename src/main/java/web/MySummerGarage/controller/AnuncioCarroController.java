package web.MySummerGarage.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    /**
     * Os campos numéricos chegam mascarados no formato pt-BR (ex.: "89.000,00", "18.000")
     * por causa das máscaras de JS. Aqui ensinamos o Spring a interpretar esse formato
     * antes de bindar em BigDecimal/Integer.
     */
    @InitBinder
    public void configurarBinder(WebDataBinder binder) {
        // Moeda: "89.000,00" -> 89000.00 (remove pontos de milhar, vírgula vira ponto decimal)
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                String normalizado = text.trim().replace(".", "").replace(",", ".");
                setValue(new BigDecimal(normalizado));
            }
        });

        // Inteiro: "18.000" -> 18000 (remove tudo que não for dígito ou sinal)
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                String normalizado = text.trim().replaceAll("[^0-9-]", "");
                setValue(normalizado.isEmpty() ? null : Integer.valueOf(normalizado));
            }
        });
    }

    // CADASTRAR
    @GetMapping("/cadastrar")
    public String abrirCadastrar(Model model,
                                 @RequestHeader(value = "HX-Request", required = false) boolean hx) {
        model.addAttribute("anuncioDTOInput", new AnuncioCarroDTOInput());
        model.addAttribute("todosStatus", StatusAnuncio.values());
        return view("anuncio/cadastrar", hx);
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("anuncioDTOInput") AnuncioCarroDTOInput dto,
                            BindingResult result, Model model,
                            Authentication auth,
                            @RequestHeader(value = "HX-Request", required = false) boolean hx,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosStatus", StatusAnuncio.values());
            return view("anuncio/cadastrar", hx);
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
            @RequestHeader(value = "HX-Request", required = false) boolean hx,
            Model model) {

        Sort.Direction direcao = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direcao, sort));
        Page<AnuncioCarro> resultado = anuncioCarroService.pesquisar(filtro, pageable);

        String urlBase = "/anuncio/pesquisar?titulo=" + nvl(filtro.getTitulo()) +
                         "&marca=" + nvl(filtro.getMarca()) +
                         "&status=" + nvl(filtro.getStatus()) +
                         "&sort=" + sort + "&dir=" + dir;

        model.addAttribute("pagina", new PaginaInfo(resultado, urlBase));
        model.addAttribute("anuncios", resultado.getContent());
        model.addAttribute("filtro", filtro);
        model.addAttribute("todosStatus", StatusAnuncio.values());
        return view("anuncio/pesquisar", hx);
    }

    // ALTERAR
    @GetMapping("/alterar/{codigo}")
    public String abrirAlterar(@PathVariable Long codigo, Model model,
                               @RequestHeader(value = "HX-Request", required = false) boolean hx) {
        AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        model.addAttribute("anuncioDTOInput", anuncioCarroService.toDTO(anuncio));
        model.addAttribute("todosStatus", StatusAnuncio.values());
        return view("anuncio/alterar", hx);
    }

    @PostMapping("/alterar/{codigo}")
    public String alterar(@PathVariable Long codigo,
                          @Valid @ModelAttribute("anuncioDTOInput") AnuncioCarroDTOInput dto,
                          BindingResult result, Model model,
                          @RequestHeader(value = "HX-Request", required = false) boolean hx,
                          RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("todosStatus", StatusAnuncio.values());
            return view("anuncio/alterar", hx);
        }
        anuncioCarroService.atualizar(codigo, dto);
        redirect.addFlashAttribute("mensagemSucesso", "Anúncio atualizado com sucesso!");
        return "redirect:/anuncio/pesquisar";
    }

    // VISUALIZAR
    @GetMapping("/visualizar/{codigo}")
    public String visualizar(@PathVariable Long codigo, Model model,
                             @RequestHeader(value = "HX-Request", required = false) boolean hx) {
        AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        model.addAttribute("anuncio", anuncio);
        return view("anuncio/visualizar", hx);
    }

    // EXCLUIR
    @PostMapping("/excluir/{codigo}")
    public String excluir(@PathVariable Long codigo, RedirectAttributes redirect) {
        try {
            anuncioCarroService.excluir(codigo);
            redirect.addFlashAttribute("mensagemSucesso", "Anúncio excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("mensagemErro",
                    "Não é possível excluir este anúncio porque ele possui uma venda associada.");
        }
        return "redirect:/anuncio/pesquisar";
    }

    private String nvl(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * Em requisições HTMX retorna apenas o fragmento "conteudo" (sem o layout),
     * para que o swap em #main troque só o conteúdo. Em navegação normal
     * retorna a página completa decorada pelo layout.
     */
    private String view(String template, boolean hxRequest) {
        return hxRequest ? template + " :: conteudo" : template;
    }
}