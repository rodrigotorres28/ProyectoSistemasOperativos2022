import java.util.*;
import java.util.concurrent.Semaphore;

public class Comercio implements Runnable{

    private String nombre;
	private Boolean ocupado;
    private Queue<Pedido> pedidos;
	private List<Repartidor> repartidoresEsperando;
	private Semaphore semComienzo;
    private Semaphore semFinal;
    private Semaphore semFinalTodos;

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
	
	public Comercio(String Nombre, Semaphore SemComienzo, Semaphore SemFinal, Semaphore SemFinalTodos){
        this.nombre = Nombre;
        this.semComienzo = SemComienzo;
        this.semFinal = SemFinal;
        this.semFinalTodos = SemFinalTodos;
        this.ocupado = false;
        this.pedidos = new LinkedList<Pedido>();
        this.repartidoresEsperando = new ArrayList<Repartidor>();
    }

    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);
    }

    @Override
    public void run() {
        while(true){
            try {
                semComienzo.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("comercio " + nombre + " elaborando");
            
            semFinal.release();

            try {
                semFinalTodos.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}