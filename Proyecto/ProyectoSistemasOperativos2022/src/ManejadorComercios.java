import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ManejadorComercios {

    private List<Comercio> comercios = new ArrayList<Comercio>();

    public List<Comercio> getComercios() {
		return comercios;
	}

	void checkearComercios(long contadorGlobal){

        // 

    }

    void cargarComercios(){

        Semaphore sem1 = new Semaphore(0);
        Comercio com1 = new Comercio("La vaca picada",sem1);

        Semaphore sem2 = new Semaphore(0);
        Comercio com2 = new Comercio("McDonalds",sem2);

        Semaphore sem3 = new Semaphore(0);
        Comercio com3 = new Comercio("Burger King",sem3);
        
        comercios.add(com1);
        comercios.add(com2);
        comercios.add(com3);
    }

    void nuevoPedido(Pedido pedido){
        
    }
}