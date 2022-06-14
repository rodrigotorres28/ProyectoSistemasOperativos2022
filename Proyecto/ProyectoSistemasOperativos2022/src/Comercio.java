import java.util.*;
import java.util.concurrent.Semaphore;

public class Comercio implements Runnable{

    protected String nombre;
	protected Boolean ocupado;
    protected List<Pedido> pedidos;
	protected Queue<Repartidor> repartidoresEsperando = new LinkedList<Repartidor>();
    protected Queue<Pedido> pedidosListos = new LinkedList<>();
	protected Semaphore semComienzo;
    protected Semaphore semFinal;
    protected Semaphore semFinalTodos;
    protected Semaphore pedidosMutex = new Semaphore(1);
    protected Pedido siguiente;
    protected Pedido elaborando;
    protected int elaboracionActual = 0;
    protected ManejadorRepartidores manejadorRepartidores;
    protected Logger logger;

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
    public Queue<Repartidor> getRepartidoresEsperando() {
		return repartidoresEsperando;
	}
	
	public Comercio(String Nombre, ManejadorRepartidores ManejadorRepartidores, Semaphore SemComienzo, Semaphore SemFinal, Semaphore SemFinalTodos, Logger Logger){
        this.nombre = Nombre;
        this.manejadorRepartidores = ManejadorRepartidores;
        this.semComienzo = SemComienzo;
        this.semFinal = SemFinal;
        this.semFinalTodos = SemFinalTodos;
        this.logger = Logger;
        this.ocupado = false;
        this.pedidos = new LinkedList<Pedido>();
        this.repartidoresEsperando = new LinkedList<Repartidor>();
    }

    public void agregarPedido(Pedido pedido){
        try {pedidosMutex.acquire();} catch (InterruptedException e) {}
        pedidos.add(pedido);
        pedidosMutex.release();
    }

    public void agregarRepartidor(Repartidor repartidor){
        repartidoresEsperando.add(repartidor);
    }

    @Override
    public void run() {
        while(true){
            try {semComienzo.acquire();} catch (InterruptedException e) {}

            if(siguiente != null){
                siguiente.setAntiguedad(siguiente.getAntiguedad()+1);
            }
            try {pedidosMutex.acquire();} catch (InterruptedException e) {}
            for (Pedido pedido : pedidos) {
                pedido.setAntiguedad(pedido.getAntiguedad()+1);
                if(siguiente == null){
                    siguiente = pedido;
                    pedidos.remove(pedido);
                }
                else if (prioridadHRRN(siguiente.getAntiguedad(),siguiente.getTiempoElaboracion()) < prioridadHRRN(pedido.getAntiguedad(),pedido.getTiempoElaboracion())){
                    pedidos.add(siguiente);
                    siguiente = pedido;
                    pedidos.remove(pedido);
                }
            }
            pedidosMutex.release();

            if (elaboracionActual != 0){
                elaboracionActual--;
            }

            if (elaborando != null && elaboracionActual == 0){
                //Escribir a csv
                logger.actualizarPedido(elaborando, "finElab");
                System.out.println("Se Termino de elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                pedidosListos.add(elaborando);
                elaborando = siguiente;
                siguiente = null;
                if(elaborando != null){
                    //Escribir a csv
                    logger.actualizarPedido(elaborando, "iniElab");
                    System.out.println("Se empezó a elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                    elaboracionActual = elaborando.getTiempoElaboracion();
                }
            }

            if (elaborando == null && siguiente != null){
                elaborando = siguiente;
                siguiente = null;
                //Escribir a csv
                logger.actualizarPedido(elaborando, "iniElab");
                System.out.println("Se empezó a elaborar el pedido #" + elaborando.getId() + " en el comercio: " + this.getNombre());
                elaboracionActual = elaborando.getTiempoElaboracion();
            }

            while(pedidosListos.size() > 0){
                if(repartidoresEsperando.size()>0){
                    Repartidor repartidor = repartidoresEsperando.remove();
                    Pedido pedido = pedidosListos.remove();
                    repartidor.setPedido(pedido);
                    repartidor.setEnEspera(false);
                    repartidor.setDistanciaRestante(pedido.getDistanciaCliente());
                    repartidor.setEnviando(true);
                    manejadorRepartidores.repartidorEnviando(repartidor);
                    //Escribir a csv
                    logger.actualizarPedido(pedido, "iniEnv");
                    System.out.println("Comenzó el envio del pedido #" + pedido.getId() + ". Por el repartidor #" + repartidor.getId());
                }
                else{
                    break;
                }
            }
            
            semFinal.release();

            try {semFinalTodos.acquire();} catch (InterruptedException e) {}
        }
    }

    private Float prioridadHRRN(int antiguedad, int elaboracion){
        Float fElab = (float) elaboracion;
        float prioridadElaboracion = 100.0f - (((fElab-10.0f)*100.0f)/35.0f);
        return Math.max((antiguedad*5), prioridadElaboracion);
    }
}