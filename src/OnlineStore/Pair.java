package OnlineStore;


import java.io.Serializable;

/**
 * Classe que representa um par generico. Eh usada para
 * associar um determinado produto à quantidade dele em estoque
 * @param <L> um produto
 * @param <R> um integer
 */
public class Pair<L,R> implements Serializable {
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}
