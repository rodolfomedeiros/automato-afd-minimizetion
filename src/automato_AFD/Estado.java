package automato_AFD;

public class Estado {
    private String name;
    private Transicao trans1 = null;
    private Transicao trans2 = null;
    
    public Estado(){}
    
    public Estado(String name){
        setName(name);
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }

    public Transicao getTrans1() {
        return trans1;
    }

    public void setTrans1(Transicao trans1) {
        this.trans1 = trans1;
    }

    public Transicao getTrans2() {
        return trans2;
    }

    public void setTrans2(Transicao trans2) {
        this.trans2 = trans2;
    }
}
