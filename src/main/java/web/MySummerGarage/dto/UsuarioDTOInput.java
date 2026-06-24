package web.MySummerGarage.dto;

//IMPORTS
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class UsuarioDTOInput {

    private Long codigo;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(max = 50, message = "O nome de usuário deve ter no máximo 50 caracteres")
    private String nomeUsuario;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    private boolean ativo = true;

    @NotEmpty(message = "Selecione ao menos um papel")
    private List<Long> papeis;

    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<Long> getPapeis() { return papeis; }
    public void setPapeis(List<Long> papeis) { this.papeis = papeis; }
}