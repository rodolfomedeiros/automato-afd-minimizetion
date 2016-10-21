package automato_AFD;

import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import com.mxgraph.swing.mxGraphComponent;

import com.mxgraph.util.mxConstants;

import javax.swing.JFrame;
import java.util.HashMap;
import java.util.Hashtable;

public class GerarAutomato extends JFrame{
    private Automato automato;

    public GerarAutomato(Automato automato){
        super("AFD - Autômato Finito Deterministico");
        setAutomato(automato);
        criarAutomato();
        init();
    }
    
    private void setAutomato(Automato aut){
        this.automato = aut;
    }
    
    private void criarAutomato(){
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        
        try{
            int i;
            HashMap<String, Object> graphEstado = new HashMap<>();
            Object[] estadoStr = automato.getEstadoHash().keySet().toArray();
            HashMap<String, Estado> estadoHash = automato.getEstadoHash();
            int tam = automato.getEstadoHash().size();
            
            int w2 = 80;
            int w = 10;
            
            //criando os estados
            for(i = 0; i < tam; i++){
                if(i%3 == 0){
                    graphEstado.put(estadoStr[i].toString(), graph.insertVertex(parent, null, estadoStr[i].toString(), w, 150, 50, 50,"ROUNDED"));
                    w += 140;
                }else{
                    graphEstado.put(estadoStr[i].toString(), graph.insertVertex(parent, null, estadoStr[i].toString(), w2, 50, 50, 50,"ROUNDED"));
                    if(++i < tam){
                        graphEstado.put(estadoStr[i].toString(),graph.insertVertex(parent, null, estadoStr[i].toString(), w2, 250, 50, 50,"ROUNDED"));
                    }
                    w2 += 140;
                }
            }
            
            for (Object estadoStr1 : estadoStr) {
                if (automato.isEstadoFinal(estadoStr1.toString())) {
                    Object v1 = graphEstado.get(estadoStr1);
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", new Object[]{v1});
                }
            }
            
            //ligando os estados
            for(i = 0; i < tam; i++){
                Estado est = estadoHash.get(estadoStr[i].toString());
                Transicao trans1 = est.getTrans1();
                if(trans1 != null){
                    graph.insertEdge(parent, null, trans1.getSimbolo(), graphEstado.get(est.getName()), graphEstado.get(trans1.getDestino()),"edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");   
                }
                Transicao trans2 = est.getTrans2();
                if(trans2 != null){
                    graph.insertEdge(parent, null, trans2.getSimbolo(), graphEstado.get(est.getName()), graphEstado.get(trans2.getDestino()),"edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
                }
            }  
        }
        finally{
            graph.setStylesheet(getStyleAutomato(graph.getStylesheet()));
            graph.setCellsResizable(false);
            graph.setAllowDanglingEdges(false);
            graph.setCellsDisconnectable(false);
            graph.setEdgeLabelsMovable(false);
            graph.setCellsEditable(false);
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }
    
    private void init(){
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private mxStylesheet getStyleAutomato(mxStylesheet s){
        mxStylesheet stylesheet = s;
        Hashtable<String, Object> style = new Hashtable<String,Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("ROUNDED", style);
        return stylesheet;
    }
}
