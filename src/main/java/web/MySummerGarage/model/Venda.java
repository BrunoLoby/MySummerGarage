package web.MySummerGarage.model;

public class Venda {

    public int id;
    public double valorFinal;
    public boolean statusVenda;


    //GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public boolean isStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(boolean statusVenda) {
        this.statusVenda = statusVenda;
    }

    //FUNCOES
    public void FinalizarVenda(){
        if(statusVenda){
            System.out.println("Venda finalizada com sucesso! Valor final: " + valorFinal);
        } else {
            System.out.println("Não foi possível finalizar a venda. Verifique o status da venda.");
        }
    }

}
