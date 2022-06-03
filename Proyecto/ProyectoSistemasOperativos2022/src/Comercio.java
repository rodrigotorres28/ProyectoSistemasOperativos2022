import java.util.*;
import java.util.concurrent.Semaphore;

public class Comercio implements Runnable{

    private String nombre;
	private Boolean ocupado;
    private Queue<Pedido> pedidos;
	private List<Repartidor> repartidoresEsperando;
	private Semaphore sem;

    // Getters
    public String getNombre() {
		return nombre;
	}
    public Boolean getOcupado() {
		return ocupado;
	}
    public Queue<Pedido> getPedidos() {
		return pedidos;
	}
    public List<Repartidor> getRepartidoresEsperando() {
		return repartidoresEsperando;
	}
    public Semaphore getSem() {
		return sem;
	}
	
	public Comercio(String Nombre, Semaphore Sem){
        this.nombre = Nombre;
        this.sem = Sem;
        this.ocupado = false;
        this.pedidos = new LinkedList<Pedido>();
        this.repartidoresEsperando = new ArrayList<Repartidor>();
    }

    @Override
    public void run() {
        
        
    }
}