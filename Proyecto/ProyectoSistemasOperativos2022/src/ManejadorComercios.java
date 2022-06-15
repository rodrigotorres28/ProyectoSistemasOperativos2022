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
        //Se cargan los comercios
        String[] entradaComercios = ManejadorArchivosGenerico.leerArchivo("src/EntradaComercios.csv");
        for (String entrada: entradaComercios){
            String[] temp = entrada.split(";");
            if (temp[1].compareTo("restaurante") == 0) {
                comercios.add(new Comercio(temp[0], manejador, semComienzo, semFinal, semFinalTodos, logger)); 
            }
            else if(temp[1].compareTo("farmacia") == 0 || temp[1].compareTo("almacen") == 0){
                comercios.add(new ComercioSinElaboracion(temp[0], manejador, semComienzo, semFinal, semFinalTodos, logger));
            }
            else{
                System.out.println("Tipo de Comercio incorrecto en el archivo de entrada de comercios: " + temp[0]);
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