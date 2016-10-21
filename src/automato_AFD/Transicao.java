package automato_AFD;

public class Transicao {
    private String simbolo;
    private String destino;
    
    public Transicao(){}
    
    public Transicao(String simbolo, String destino){
        setDestino(destino);
        setSimbolo(simbolo);
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
