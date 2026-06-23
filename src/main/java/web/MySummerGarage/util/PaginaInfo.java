package web.MySummerGarage.util;

import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

public class PaginaInfo {

    private final Page<?> page;
    private final String urlBase;

    public PaginaInfo(Page<?> page, String urlBase) {
        this.page = page;
        this.urlBase = urlBase;
    }

    public int getAtual() { return page.getNumber(); }
    public int getNumeroPaginas() { return page.getTotalPages(); }
    public boolean isPrimeira() { return page.isFirst(); }
    public boolean isUltima() { return page.isLast(); }
    public boolean isVazia() { return page.isEmpty(); }

    public int getInicio() {
        int bloco = (getAtual() / 5) * 5;
        return bloco + 1;
    }

    public int getFim() {
        return Math.min(getInicio() + 4, getNumeroPaginas());
    }

    public String urlParaPagina(int pagina) {
        return UriComponentsBuilder.fromUriString(urlBase)
                .replaceQueryParam("page", pagina)
                .build().toUriString();
    }

    public String urlInvertendoDirecaoOrdem(String propriedade) {
        String sort = page.getSort().toString();
        String direcao = sort.contains(propriedade + ": DESC") ? "asc" : "desc";
        return UriComponentsBuilder.fromUriString(urlBase)
                .replaceQueryParam("sort", propriedade + "," + direcao)
                .replaceQueryParam("page", 0)
                .build().toUriString();
    }

    public boolean isDescendente(String propriedade) {
        return page.getSort().toString().contains(propriedade + ": DESC");
    }

    public boolean isAscendente(String propriedade) {
        return page.getSort().toString().contains(propriedade + ": ASC");
    }
}