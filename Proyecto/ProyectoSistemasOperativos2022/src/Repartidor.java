import java.util.*;

public class Repartidor implements Runnable{

    private Boolean libre;
    private int id;
    private int distanciaPedido;

    // Getters
    public Boolean getLibre() {
		return libre;
	}
	public void setLibre(Boolean libre) {
		this.libre = libre;
	}
    public int getId() {
		return id;
	}
    public int getDistanciaPedido() {
		return distanciaPedido;
	}
	public void setDistanciaPedido(int distanciaPedido) {
		this.distanciaPedido = distanciaPedido;
	}

	public Repartidor(int Id, int DistanciaPedido){

        this.id = Id;
        this.distanciaPedido = DistanciaPedido;
    }
	@Override
	public void run() {
		
        
		
	}

}