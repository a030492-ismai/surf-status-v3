package pt.a030492.surfstatusv3;


import java.util.ArrayList;
import java.util.List;


public class Praia {

    public static List<Praia> listaPraias = new ArrayList<>();
    public static List<Praia> listaPraiasListar = new ArrayList<>();

    protected int praiaId;
    protected String nomePraia;
    protected String condicaoActual;
    protected String urlPraia;
    protected boolean listar;

    public Praia(int id) {
        praiaId = id;
    }

    public void setId(int id) {
        praiaId = id;
    }
    public int getId() {
        return praiaId;
    }
    public void setNomePraia(String s) {
        nomePraia = s;
    }
    public String getNomePraia() {
        return nomePraia;
    }
    public void setCondicaoActual(String s) {
        condicaoActual = s;
    }
    public String getCondicaoActual(){
        return condicaoActual;
    }
    public void setUrlPraia(String s){
        urlPraia = s;
    }
    public String getUrlPraia(){
        return urlPraia;
    }
    public void setListar(boolean b){listar = b; }
    public boolean getListar(){return listar; }

    public static void addPraias(ArrayList<Praia> praias){
        listaPraias = null;
        listaPraias = praias;
    }
    public void delPraia(int index){
        listaPraias.remove(index);
    }
    public Praia getPraia(int index){
        return listaPraias.get(index);
    }

    public static void addPraiasListar(ArrayList<Praia> praias){
        listaPraiasListar = null;
        listaPraiasListar = praias;
    }

    @Override
    public String toString(){
        if(this.getCondicaoActual() == null){
            return this.getNomePraia();
        }else{
            return this.getNomePraia() + "\n" + this.getCondicaoActual();
        }
    }
}

