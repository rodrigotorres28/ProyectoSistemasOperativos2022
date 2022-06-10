import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ManejadorComercios implements Runnable{

    private List<Comercio> comercios = new ArrayList<Comercio>();
    private Semaphore semComienzo = new Semaphore(0);
    private Semaphore semFinal = new Semaphore(0);
    private Semaphore semFinalTodos = new Semaphore(0);
    private Semaphore semTickComercios;
    private Semaphore semFinTickComercios;

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
        while(true){
            try {semTickComercios.acquire();} catch (InterruptedException e1) {}
            
            semComienzo.release(comercios.size());
            try {semFinal.acquire(comercios.size());} catch (InterruptedException e) {}
            semFinalTodos.release(comercios.size());

            semFinTickComercios.release();
        }
    }

    void cargarComercios(){

        Comercio com1 = new Comercio("La vaca picada", semComienzo, semFinal, semFinalTodos);
        Comercio com2 = new Comercio("McDonalds", semComienzo, semFinal, semFinalTodos);
        Comercio com3 = new Comercio("Burger King", semComienzo, semFinal, semFinalTodos);
        
        comercios.add(com1);
        comercios.add(com2);
        comercios.add(com3);

        for (Comercio comercio : comercios) {
            Thread hilo = new Thread(comercio);
            hilo.start();
        }

    }

    boolean nuevoPedido(Pedido pedido){
        for (Comercio comercio : comercios) {
            if (comercio.getNombre() == pedido.getComercio()){
                comercio.agregarPedido(pedido);
                return true;
            }
        }
        return false;
    }
}