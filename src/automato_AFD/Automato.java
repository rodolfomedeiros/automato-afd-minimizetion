package automato_AFD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Automato {
    
    private String estadoInicial;                               //Estado inicial do automato.
    private HashSet<String> estadoFinal = new HashSet<>();      //Todos os estados finais em strings.
    private HashMap<String,Estado> estadoHash;                  //Todos os estados(key = nome do estado// value = classe estado)
    private HashSet<String> alfabeto = new HashSet<>();         //O alfabeto(vetor de strings)
    
    public Automato(){}                             

    public String getEstadoInicial() {                          //Retorna o estado inicial
        return estadoInicial;
    }
    
    public ArrayList<String> getEstados(){
        ArrayList<String> estados = new ArrayList<>();
        String letra = getEstadoInicial();
        for(int i = 0; i < getEstadoHash().size(); i++){
            estados.add(""+letra.charAt(0)+i);
        }
        
        return estados;
    }
    
    public boolean isEstadoInicial(String nameEstado){          //Verifica se(nameEstado) é estado inicial.
        return this.estadoInicial.equalsIgnoreCase(nameEstado);
    }

    public boolean setEstadoInicial(String estadoInicial) {      //set estado se ele estiver nos estados recebidos.
        if(isEstadoHash(estadoInicial)){
            this.estadoInicial = estadoInicial;
            return true;
        }else{
           this.estadoInicial = "";
        }
        return false;
    }

    public HashSet<String> getEstadoFinal() {                   //Retorna uma hashSet de todos os estados finais.
        return estadoFinal;
    }
    
    public boolean isEstadoFinal(String nameEstado){            //Verifica se é um estado final. 
        return this.estadoFinal.contains(nameEstado);
    }   

    public void setEstadoFinal(HashSet<String> estadoFinal) {   
        this.estadoFinal = estadoFinal;
    }
    
    public boolean setEstadoFinal(String estadoFinal){          //Verifica se é um estado, se true, entao seta em estado final.
        if(isEstadoHash(estadoFinal)){
            this.estadoFinal.add(estadoFinal);
            return true;
        }
        return false;
    }

    public HashMap<String,Estado> getEstadoHash() {
        return estadoHash;
    }
    
    public Estado getEstadoHash(String nameEstado) {                    //Retorna um estado
        return estadoHash.get(nameEstado);
    }
    
    public boolean isEstadoHash(String nameEstado){
        return estadoHash.containsKey(nameEstado);
    }

    public void setEstadoHash(HashMap<String,Estado> estado) {
        this.estadoHash = estado;
    }
    
    public void setEstadoHash(Estado estado) {
       this.estadoHash.put(estado.getName(),estado);
    }
    
    public HashSet<String> getAlfabeto(){
        return this.alfabeto;
    }

    public String[] getAlfabetoString(){
        String s[] = new String[alfabeto.size()];
        return alfabeto.toArray(s);
    }
    
    public boolean isAlfabeto(String name){
        return this.alfabeto.contains(name);
    }
    
    public void setAlfabeto(String name){
        this.alfabeto.add(name);
    }
    
    public void setAlfabeto(HashSet<String> alfabeto){
        this.alfabeto = alfabeto;
    }

    public boolean setTransicao(String[] transicao) {                   
        for (String transicao1 : transicao) {                           //Pecorrer todas as transições
            String[] transT = transicao1.split("[,=]");                 //Transforma uma transição(q1,0=q2) em 3 elementos de um vetor
            Estado estadoSrc = getEstadoHash(transT[0]);                //transT[0] refere ao estado atual.
            //não aceita trasição vazia e nem estado que não existe.
            if(transT[1].equalsIgnoreCase("vazio") || estadoSrc == null){
                return false;
            }else{
                //pega as transições do estado atual.
                Transicao trans1 = estadoSrc.getTrans1();
                Transicao trans2 = estadoSrc.getTrans2();
                
                //caso seja null, então seta na trans1
                if(trans1 == null){
                    estadoSrc.setTrans1(new Transicao(transT[1], transT[2]));
                }else{
                    //verifica ambiguidade
                    if(!trans1.getSimbolo().equalsIgnoreCase(transT[1])){   //transT[1] refere o simbolo da transição(Ex: 0 ou 1).              
                        //trans2 == null, ja seta! //Verifica os simbolos e destino, se forem diferentes, seta o "trans2".
                        if(trans2 == null || (!(trans2.getSimbolo().equalsIgnoreCase(transT[1])) && !(trans2.getDestino().equalsIgnoreCase(transT[2])))){
                            estadoSrc.setTrans2(new Transicao(transT[1], transT[2])); //transT[2] refere ao estado destino.
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }
            }
            setEstadoHash(estadoSrc);
        }
        return true;
    }
    
    public boolean verificarCadeia(String cadeia){
              
        String estadoAtual = getEstadoInicial();
        
        HashMap<String,Estado> estadoH = getEstadoHash();
        int i;
        
        for(i = 0; i < cadeia.length(); i++){
            Estado est = estadoH.get(estadoAtual);
            
            if(est == null){
                break;
            }
            
            Transicao trans1 = est.getTrans1();
            Transicao trans2 = est.getTrans2();
            
            if(trans1 == null){
                break;
            }
            //se for igual, então pega o destino e coloca em estado atual e retorno o loop
            if(trans1.getSimbolo().equals(cadeia.charAt(i)+"")){
                estadoAtual = trans1.getDestino();
            }else{
                if(trans2 == null){
                    break;
                }
                //se for igual, então pega o destino e coloca em estado atual e retorno o loop
                if(trans2.getSimbolo().equals(cadeia.charAt(i)+"")){
                    estadoAtual = trans2.getDestino();
                }
            }
        }
        //verifica o tamanho da cadeia, cadeia for maior do que os estados podem pecorrer, então retorna false
        if(i < cadeia.length()){
            return false;
        }
        //retorna true se obteve o estado de aceitação(se parou no estado final).
        return isEstadoFinal(estadoAtual);
    }
    
    public Automato minimizar(){
      
    //--------------------------------------------------------------------------
    
        //Fazendo o 1. 2.
        //Marcados X
        ArrayList<String> marcadoX = new ArrayList<>();
        //Marcado -
        ArrayList<String> marcadoV = new ArrayList<>();
        //Marcado +
        ArrayList<String> marcadoCMais = new ArrayList<>();
        
        //Atribuindo na tabela...
        int tamEstado = estadoHash.size();
        String letra = getEstadoInicial();
        for(int i = 0; i < tamEstado-1; i++){
            for(int j = i+1; j < tamEstado; j++){
                String strI = ""+letra.charAt(0)+i;
                String strJ = ""+letra.charAt(0)+j;
                if((isEstadoFinal(strI) && !isEstadoFinal(strJ)) || (!isEstadoFinal(strI) && isEstadoFinal(strJ))){
                    marcadoX.add(strI+"/"+strJ);
                }else{
                    marcadoV.add(strI+"/"+strJ);
                }
            }
        }

    //--------------------------------------------------------------------------    
        //Fazendo 3.
        ArrayList<String> marcadoVazio = new ArrayList<>();
        marcadoVazio = (ArrayList<String>) marcadoV.clone();
        HashMap<String,ArrayList<String>> listaAnalise = new HashMap<>();
        for(int i = 0; i < marcadoV.size(); i++){
            String str = marcadoV.get(i);
            String strEstado[] = str.split("/");
            //String strAlfabeto[] = getAlfabetoString();
            String strEstadoResult1[] = new String[2];
            String strEstadoResult2[] = new String[2];
            
            //(q)
            strEstadoResult1[0] = getEstadoHash(strEstado[0]).getTrans1().getDestino();
            strEstadoResult1[1] = getEstadoHash(strEstado[0]).getTrans2().getDestino();
            //(q')
            strEstadoResult2[0] = getEstadoHash(strEstado[1]).getTrans1().getDestino();
            strEstadoResult2[1] = getEstadoHash(strEstado[1]).getTrans2().getDestino();
            
            //marcações/lista analise
            for(int w = 0; w < 2; w++){
                //p=p' não entra
                if(!(strEstadoResult1[w].equals(strEstadoResult2[w]))){
                    //p!=p' e faz verificação se ta marcado ou não.
                    String op1 = strEstadoResult1[w]+"/"+strEstadoResult2[w];
                    boolean result1 = marcadoVazio.contains(op1);
                    String op2 = strEstadoResult2[w]+"/"+strEstadoResult1[w];
                    boolean result2 = marcadoVazio.contains(op2);
                    if(result1 || result2){
                        ArrayList<String> vetorString;
                        if(result1){
                            vetorString = (listaAnalise.get(op1)==null) ? new ArrayList<>(): listaAnalise.get(op1);
                            vetorString.add(str);
                            listaAnalise.put(op1, vetorString);
                        }else{
                            vetorString = (listaAnalise.get(op2)==null) ? new ArrayList<>(): listaAnalise.get(op2);
                            vetorString.add(str);
                            listaAnalise.put(op2, vetorString);
                        }
                    }else{
                        marcadoVazio.remove(str);
                        marcadoCMais.add(str);
                        ArrayList<String> vetorString;
                        vetorString = (listaAnalise.get(str)==null) ? new ArrayList<>(): listaAnalise.get(str);
                        for (String vetorString1 : vetorString) {
                            result1 = marcadoVazio.remove(vetorString1);
                            if(result1){
                                marcadoCMais.add(vetorString1);
                            }
                        }
                    }
                }
            }           
        }
        
        //--------------------------------------------------------------------------
        //4. e 5.
        
        Automato aut = new Automato();
        String estadoinicial = null;
        ArrayList<String> estadosNovos = new ArrayList<>();
        
        //temporarios
        ArrayList<String> estados = getEstados();
        HashSet<String> estadosEquivalente = new HashSet<>();
        
        //setando estados novos
        for(String i : marcadoVazio){
            String s[] = i.split("/");
            estadosEquivalente.add(s[0]);
            estadosEquivalente.add(s[1]);
        }
        for(String estado : estados){
            if(!estadosEquivalente.contains(estado)){
                estadosNovos.add(estado);
            }
        }
        //Retirando estados de morte...
        for(int i = 0; i < estadosNovos.size(); i++){
            Estado e = getEstadoHash(estadosNovos.get(i));
            Boolean r = e.getTrans1().getDestino().equals(e.getName());
            r = r && e.getTrans2().getDestino().equals(e.getName());
            if(r){
                estadosNovos.remove(i);
            }
        }
        //Estados
        for(String i : marcadoVazio){
            estadosNovos.add(i);
        }
        aut.setEstadoHash(new HashMap<>());
        for(String estado : estadosNovos) {
            aut.setEstadoHash(new Estado(estado));
        }
        
        //Setando estado inicial
        for(String i : marcadoVazio){
            String s[] = i.split("/");
            if(isEstadoInicial(s[0]) || isEstadoInicial(s[1])){
                estadoinicial = i;
            }
        }
        if(estadoinicial==null){
            aut.setEstadoInicial(getEstadoInicial());
        }else{
            aut.setEstadoInicial(estadoinicial);
        }
        
        //setando Alfabeto
        aut.setAlfabeto(getAlfabeto());
        
        //Setando estados finais
        for(String estado : estadosNovos){
            if(estado.length() > 2){
                String s[] = estado.split("/");
                if(isEstadoFinal(s[0]) || isEstadoFinal(s[1])){
                    aut.setEstadoFinal(estado);
                }
            }else{
                if(isEstadoFinal(estado)){
                    aut.setEstadoFinal(estado);
                }
            }
        }
        
        //setando transições
        ArrayList<String> transicao = new ArrayList<>();
        for(String estado : estadosNovos){
            Estado e = getEstadoHash(estado);
            //Entra para estados que já existiam...
            if(e != null){
                Transicao trans[] = new Transicao[2];
                trans[0] = e.getTrans1();
                trans[1] = e.getTrans2();
                
                //tratando transições
                for (Transicao tran : trans) {
                    if (aut.isEstadoHash(tran.getDestino())) {
                        transicao.add(e.getName()+"," + tran.getSimbolo() + "=" + tran.getDestino());
                    } else {
                        for (String estadoNovo : marcadoVazio) {
                            String s[] = estadoNovo.split("/");
                            if (s[0].equals(tran.getDestino()) || s[1].equals(tran.getDestino())) {
                                transicao.add(e.getName()+"," + tran.getSimbolo() + "=" + estadoNovo);
                            }
                        }
                    }
                }
            //Estados novos, resolvendo as transições
            }else{
                String s[] = estado.split("/");
                for (String estadoNovo : marcadoVazio) {
                    String ss[] = estadoNovo.split("/");
                    Estado estado1 = getEstadoHash(s[0]);
                    if(estado1 != null){
                        Transicao trans1 = estado1.getTrans1();
                        if(trans1.getDestino().equals(ss[0]) || trans1.getDestino().equals(ss[1])){
                            transicao.add(estado+"," + trans1.getSimbolo() + "=" + estadoNovo);
                        }
                        Transicao trans2 = estado1.getTrans2();
                        if(trans2.getDestino().equals(ss[0]) || trans2.getDestino().equals(ss[1])){
                            transicao.add(estado+"," + trans2.getSimbolo() + "=" + estadoNovo);
                        }
                    }
                }
            }
        }
        
        //Atribuindo transições
        String[] TString = new String[transicao.size()];
        TString = transicao.toArray(TString);
        aut.setTransicao(TString);
        
        //Verificando
        System.out.println("Estados:");
        for(String s : estadosNovos){
            System.out.print(s+",");
        }
        System.out.println();
        System.out.println();
        System.out.println("Estado Inicial: " + aut.getEstadoInicial());
        System.out.println();
        System.out.println("Alfabeto:");
        String a[] = getAlfabetoString();
        for(String al : a){
            System.out.print(al+",");
        }
        System.out.println();
        System.out.println();
        System.out.println("Estado Finais: ");
        HashSet<String> finais = aut.getEstadoFinal();
        for(String f : finais){
            System.out.print(f+",");
        }
        System.out.println();
        System.out.println();
        System.out.println("Trasições:");
        for(String t : transicao){
            System.out.println(t);
        }
        System.out.println();
        
        return aut;
    }
}
