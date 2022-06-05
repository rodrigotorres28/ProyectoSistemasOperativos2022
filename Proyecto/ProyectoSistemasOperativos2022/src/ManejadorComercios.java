import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ManejadorComercios {

    private List<Comercio> comercios = new ArrayList<Comercio>();
    private Semaphore semComienzo = new Semaphore(0);
    private Semaphore semFinal = new Semaphore(0);
    private Semaphore semFinalTodos = new Semaphore(0);

    public List<Comercio> getComercios() {
		return comercios;
	}

	void checkearComercios(long contadorGlobal){
        System.out.println("se liberan los semaforos comienzo");
        semComienzo.release(comercios.size());
        
        try {
            semFinal.acquire(comercios.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("se liberan los semaforos final");
        semFinalTodos.release(comercios.size());

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