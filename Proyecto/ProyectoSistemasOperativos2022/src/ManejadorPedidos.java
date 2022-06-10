import java.util.*;
import java.util.concurrent.Semaphore;


public class ManejadorPedidos implements Runnable {
    
    private Boolean terminar = false;
    private Stack<Pedido> pedidos = new Stack<>();
    private long contadorGlobal;
    private ManejadorComercios manejadorComercios;
    private Semaphore semTickPedidos;
    private Semaphore semFinTickPedidos;

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

	public List<Pedido> getPedidos() {
        return pedidos;
    }

    void cargarPedidos(){

        Pedido ped1 = new Pedido("McDonalds", "restaurante", 10, 12, 1, "1 Mc Combo Cuarto de Libra grande", 5);
        Pedido ped2 = new Pedido("McDonalds", "restaurante", 5, 10, 2, "Mc Nuggets", 8);
        Pedido ped3 = new Pedido("La vaca picada", "restaurante", 7, 3, 3, "1 Braserito para 2", 15);

        pedidos.add(ped3);
        pedidos.add(ped2);
        pedidos.add(ped1);
    }

    @Override
    public void run() {
        while (true){
            try {semTickPedidos.acquire();} catch (InterruptedException e){}
            Pedido actual = null;

            if(!pedidos.empty()){
                actual = pedidos.pop();
            
                while(actual.getHoraIngresado() <= contadorGlobal){
                    if(manejadorComercios.nuevoPedido(actual)){
                        System.out.println("Entro el pedido #" + String.valueOf(actual.getId()) + " para el comercio: " + actual.getComercio());
                        //nuevo pedido repartidores
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
