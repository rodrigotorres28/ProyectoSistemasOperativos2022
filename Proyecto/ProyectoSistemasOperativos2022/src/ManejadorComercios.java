import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ManejadorComercios implements Runnable{

    private Boolean iniciando = true;
    private List<Comercio> comercios = new ArrayList<Comercio>();
    private Semaphore semComienzo = new Semaphore(0);
    private Semaphore semFinal = new Semaphore(0);
    private Semaphore semFinalTodos = new Semaphore(0);
    private Semaphore semTickComercios;
    private Semaphore semFinTickComercios;
    private ManejadorRepartidores manejadorRepartidores;
    private long tickActual;

    public void setTickActual(long tickActual) {
		this.tickActual = tickActual;
	}
	public void setIniciando(Boolean iniciando) {
        this.iniciando = iniciando;
    }
    public void setManejadorRepartidores(ManejadorRepartidores manejadorRepartidores) {
		this.manejadorRepartidores = manejadorRepartidores;
	}
	public void setSemTickComercios(Semaphore semTickComercios) {
        this.semTickComercios = semTickComercios;
    }
    public void setSemFinTickComercios(Semaphore semFinTickComercios) {
        this.semFinTickComercios = semFinTickComercios;
    }

    public List<Comercio> getComercios() {
		return comercios;
	}

    @Override
    public void run() {
        while(iniciando){
            try {Thread.sleep(1);} catch (InterruptedException e) {}
        }

        while(true){
            try {semTickComercios.acquire();} catch (InterruptedException e1) {}
            
            //Se actualizan los contadores de los comercios
            for (Comercio comercio: comercios){
                comercio.setTickActual(tickActual);
            }

            semComienzo.release(comercios.size());
            try {semFinal.acquire(comercios.size());} catch (InterruptedException e) {}
            semFinalTodos.release(comercios.size());

            semFinTickComercios.release();
        }
    }

    void cargarComercios(ManejadorRepartidores manejador, Logger logger){
        //Se cargan los comercios
        String[] entradaComercios = ManejadorArchivosGenerico.leerArchivo("Entradas/ComerciosRepartidores.csv");
        for (String entrada: entradaComercios){
            if (entrada != entradaComercios[0]) {
                comercios.add(new Comercio(entrada, manejador, semComienzo, semFinal, semFinalTodos, logger)); 
            }
        }

        for (Comercio comercio : comercios) {
            Thread hilo = new Thread(comercio);
            hilo.start();
        }

    }

    boolean nuevoPedido(Pedido pedido, Logger logger){
        for (Comercio comercio : comercios) {
            if (comercio.getNombre().compareTo(pedido.getComercio()) == 0){
                ManejadorArchivosGenerico.escribirLinea("Salidas/BitacoraPedidos.csv", String.valueOf(tickActual) + ",Entró el pedido al sistema," + String.valueOf(pedido.getId()));
                logger.registrarPedido(pedido);
                System.out.println("Entro el pedido #" + String.valueOf(pedido.getId()) + " para el comercio: " + pedido.getComercio());
                comercio.agregarPedido(pedido);
                return true;
            }
        }
        return false;
    }
}