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
            
            semComienzo.release(comercios.size());
            try {semFinal.acquire(comercios.size());} catch (InterruptedException e) {}
            semFinalTodos.release(comercios.size());

            semFinTickComercios.release();
        }
    }

    void cargarComercios(ManejadorRepartidores manejador, Logger logger){

        Comercio com1 = new Comercio("La vaca picada", manejador , semComienzo, semFinal, semFinalTodos, logger);
        Comercio com2 = new Comercio("McDonalds", manejador, semComienzo, semFinal, semFinalTodos, logger);
        Comercio com3 = new Comercio("Burger King", manejador, semComienzo, semFinal, semFinalTodos, logger);
        ComercioSinElaboracion com4 = new ComercioSinElaboracion("Farmashop", manejador, semComienzo, semFinal, semFinalTodos, logger);
        ComercioSinElaboracion com5 = new ComercioSinElaboracion("TaTa", manejador, semComienzo, semFinal, semFinalTodos, logger);
        comercios.add(com1);
        comercios.add(com2);
        comercios.add(com3);
        comercios.add(com4);
        comercios.add(com5);

        for (Comercio comercio : comercios) {
            Thread hilo = new Thread(comercio);
            hilo.start();
        }

    }

    boolean nuevoPedido(Pedido pedido, Logger logger){
        for (Comercio comercio : comercios) {
            if (comercio.getNombre() == pedido.getComercio()){
                //Escribir a csv
                logger.registrarPedido(pedido);
                System.out.println("Entro el pedido #" + String.valueOf(pedido.getId()) + " para el comercio: " + pedido.getComercio());
                comercio.agregarPedido(pedido);
                return true;
            }
        }
        return false;
    }
}