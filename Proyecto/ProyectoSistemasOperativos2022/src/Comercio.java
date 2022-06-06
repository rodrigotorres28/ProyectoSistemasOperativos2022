import java.util.*;
import java.util.concurrent.Semaphore;

public class Comercio implements Runnable{

    private String nombre;
	private Boolean ocupado;
    private List<Pedido> pedidos;
	private List<Repartidor> repartidoresEsperando;
    private Queue<Pedido> pedidosListos = new LinkedList<>();
	private Semaphore semComienzo;
    private Semaphore semFinal;
    private Semaphore semFinalTodos;
    private Pedido siguiente;
    private Pedido elaborando;
    private int elaboracionActual = 0;

    // Getters
    public String getNombre() {
		return nombre;
	}
    public Boolean getOcupado() {
		return ocupado;
	}
    public List<Pedido> getPedidos() {
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

            if(siguiente != null){
                siguiente.setAntiguedad(siguiente.getAntiguedad()+1);
            }

            for (Pedido pedido : pedidos) {
                pedido.setAntiguedad(pedido.getAntiguedad()+1);
                if(siguiente == null){
                    siguiente = pedido;
                    pedidos.remove(pedido);
                }
                else if ((3*siguiente.getAntiguedad())/(siguiente.getTiempoElaboracion() + 1) < (3*pedido.getAntiguedad())/(pedido.getTiempoElaboracion() + 1)){
                    pedidos.add(siguiente);
                    siguiente = pedido;
                    pedidos.remove(pedido);
                }
            }

            if (elaboracionActual != 0){
                elaboracionActual--;
            }

            if (elaborando != null && elaboracionActual == 0){
                System.out.println("Se Termino de elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                pedidosListos.add(elaborando);
                elaborando = siguiente;
                siguiente = null;
                if(elaborando != null){
                    System.out.println("Se empezó a elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                    elaboracionActual = elaborando.getTiempoElaboracion();
                }
            }

            if (elaborando == null && siguiente != null){
                elaborando = siguiente;
                siguiente = null;
                System.out.println("Se empezó a elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                elaboracionActual = elaborando.getTiempoElaboracion();
            }

            while(pedidosListos.size() > 0){
                break; //asignar reprtidores (en vez del break)
            }

            //System.out.println("comercio " + nombre + " elaborando");
            
            semFinal.release();

            try {
                semFinalTodos.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}