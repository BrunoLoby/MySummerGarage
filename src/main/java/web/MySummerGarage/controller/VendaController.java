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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.MySummerGarage.dto.VendaDTOInput;
import web.MySummerGarage.model.AnuncioCarro;
import web.MySummerGarage.model.Venda;
import web.MySummerGarage.service.AnuncioCarroService;
import web.MySummerGarage.service.VendaService;
import web.MySummerGarage.util.PaginaInfo;

@Controller
@RequestMapping("/venda")
public class VendaController {

    private final VendaService vendaService;

    private final AnuncioCarroService anuncioCarroService;

    VendaController(VendaService vendaService, AnuncioCarroService anuncioCarroService) {
        this.vendaService = vendaService;
        this.anuncioCarroService = anuncioCarroService;
    }

    private boolean isHtmx(HttpServletRequest request) {
        return "true".equals(request.getHeader("HX-Request"));
    }

    // Tela de confirmação de compra
    @GetMapping("/comprar/{codigoAnuncio}")
    public String abrirComprar(@PathVariable Long codigoAnuncio, Model model,
            HttpServletRequest request) {
        AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(codigoAnuncio)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        VendaDTOInput dto = new VendaDTOInput();
        dto.setCodigoAnuncio(codigoAnuncio);
        dto.setValorFinal(anuncio.getValor());
        dto.setParcelas(1);

        model.addAttribute("anuncio", anuncio);
        model.addAttribute("vendaDTOInput", dto);

        if (isHtmx(request))
            return "venda/comprar :: conteudo";
        return "venda/comprar";
    }

    // Confirma a compra
    @PostMapping("/comprar")
    public String confirmarCompra(@Valid @ModelAttribute("vendaDTOInput") VendaDTOInput dto,
            BindingResult result, Model model,
            Authentication auth,
            HttpServletRequest request,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            AnuncioCarro anuncio = anuncioCarroService.buscarPorCodigo(dto.getCodigoAnuncio())
                    .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
            model.addAttribute("anuncio", anuncio);
            if (isHtmx(request))
                return "venda/comprar :: conteudo";
            return "venda/comprar";
        }

        try {
            Venda venda = vendaService.registrarVenda(dto, auth);
            redirect.addFlashAttribute("mensagemSucesso", "Compra realizada com sucesso!");
            return "redirect:/venda/visualizar/" + venda.getCodigo();
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/anuncio/pesquisar";
        }
    }

    // Visualizar detalhes de uma venda
    @GetMapping("/visualizar/{codigo}")
    public String visualizar(@PathVariable Long codigo, Model model,
            HttpServletRequest request) {
        Venda venda = vendaService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        model.addAttribute("venda", venda);
        model.addAttribute("pagamentos", vendaService.listarPagamentos(venda));

        if (isHtmx(request))
            return "venda/visualizar :: conteudo";
        return "venda/visualizar";
    }

    // Histórico de compras do usuário logado
    @GetMapping("/minhas")
    public String minhasCompras(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "dataVenda") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Authentication auth, Model model,
            HttpServletRequest request) {

        Sort.Direction direcao = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direcao, sort));
        Page<Venda> resultado = vendaService.listarPorComprador(auth.getName(), pageable);

        String urlBase = "/venda/minhas?sort=" + sort + "&dir=" + dir;
        model.addAttribute("pagina", new PaginaInfo(resultado, urlBase));
        model.addAttribute("vendas", resultado.getContent());

        if (isHtmx(request))
            return "venda/minhas :: conteudo";
        return "venda/minhas";
    }

    // Todas as vendas (ADMIN)
    @GetMapping("/pesquisar")
    public String pesquisar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "dataVenda") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model, HttpServletRequest request) {

        Sort.Direction direcao = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direcao, sort));
        Page<Venda> resultado = vendaService.listarTodas(pageable);

        String urlBase = "/venda/pesquisar?sort=" + sort + "&dir=" + dir;
        model.addAttribute("pagina", new PaginaInfo(resultado, urlBase));
        model.addAttribute("vendas", resultado.getContent());

        if (isHtmx(request))
            return "venda/pesquisar :: conteudo";
        return "venda/pesquisar";
    }
}