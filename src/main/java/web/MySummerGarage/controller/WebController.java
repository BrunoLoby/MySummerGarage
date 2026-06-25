package web.MySummerGarage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.MySummerGarage.filter.AnuncioCarroFilter;
import web.MySummerGarage.model.StatusAnuncio;
import web.MySummerGarage.service.AnuncioCarroService;

@Controller
public class WebController {

    @Autowired
    private AnuncioCarroService anuncioCarroService;

    @GetMapping("/")
    public String index(Model model) {
        // Vitrine pública: mostra os carros ativos (anunciados), mais recentes primeiro.
        AnuncioCarroFilter filtro = new AnuncioCarroFilter();
        filtro.setStatus(StatusAnuncio.ATIVO);
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "codigo"));
        model.addAttribute("anuncios", anuncioCarroService.pesquisar(filtro, pageable).getContent());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }
}
