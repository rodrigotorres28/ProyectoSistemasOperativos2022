import java.util.*;
import java.util.concurrent.Semaphore;


public class ManejadorPedidos implements Runnable {
    
    private Boolean terminar = false;
    private Stack<Pedido> pedidos = new Stack<>();
    private long contadorGlobal;
    private ManejadorComercios manejadorComercios;
    private ManejadorRepartidores manejadorRepartidores;
    private Semaphore semTickPedidos;
    private Semaphore semFinTickPedidos;
    private boolean iniciando = true;
    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public void setIniciando(boolean iniciando) {
        this.iniciando = iniciando;
    }
    public void setSemTickPedidos(Semaphore semTickPedidos) {
        this.semTickPedidos = semTickPedidos;
    }
    public void setSemFinTickPedidos(Semaphore semFinTickPedidos) {
        this.semFinTickPedidos = semFinTickPedidos;
    }
    public void setContadorGlobal(long contadorGlobal) {
        this.contadorGlobal = contadorGlobal;
    }
    public void setManejadorComercios(ManejadorComercios manejadorComercios) {
		this.manejadorComercios = manejadorComercios;
	}
    public void setManejadorRepartidores(ManejadorRepartidores manejadorRepartidores) {
        this.manejadorRepartidores = manejadorRepartidores;
    }

	public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void cargarPedidos(){

        String[] arrayPedidos = ManejadorArchivosGenerico.leerArchivo("Entradas/Pedidos.csv");
        for (String i: arrayPedidos){
            String[] temp = i.split(",");
            pedidos.add(new Pedido(temp[0], temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), temp[5], Integer.parseInt(temp[6])));
        }
    }

    @Override
    public void run() {
        
        while(iniciando){
            try {Thread.sleep(1);} catch (InterruptedException e) {}
        }

        while (true){
            try {semTickPedidos.acquire();} catch (InterruptedException e){}
            Pedido actual = null;

            if(!pedidos.empty()){
                actual = pedidos.pop();
            
                while(actual.getHoraIngresado() <= contadorGlobal){
                    if(manejadorComercios.nuevoPedido(actual, logger)){
                        manejadorRepartidores.nuevoPedido(actual);
                    }
                    if(!pedidos.empty()){
                    actual = pedidos.pop();
                    }
                    else{
                        terminar = true;
                        break;
                    }
                }
                if (!terminar){
                    pedidos.add(actual);
                }
            }
            semFinTickPedidos.release();
        }
    }
}
