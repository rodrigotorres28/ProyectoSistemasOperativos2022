import java.util.*;


public class ManejadorPedidos {
    
    private Stack<Pedido> pedidos = new Stack<>();

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    void checkearPedidos(long contadorGlobal, ManejadorComercios manejadorComercios){


        Pedido actual = null;

        if(!pedidos.empty()){
            actual = pedidos.pop();
        
            while(actual.getHoraIngresado() <= contadorGlobal){
                System.out.println("SI entro el pedido " + String.valueOf(actual.getId()) + " en el tick: " + String.valueOf(contadorGlobal));
                if(manejadorComercios.nuevoPedido(actual)){
                //nuevo pedido repartidores
                }
                if(!pedidos.empty()){
                actual = pedidos.pop();
                }
                else{
                    return;
                }
            }
            System.out.println("NO entro el pedido " + String.valueOf(actual.getId()) + " en el tick: " + String.valueOf(contadorGlobal));
            pedidos.add(actual);
        }
    }

    void cargarPedidos(){

        Pedido ped1 = new Pedido("McDonalds", 10, 12, 1, "1 Mc Combo Cuarto de Libra grande", 5);
        Pedido ped2 = new Pedido("McDonalds", 5, 10, 2, "Mc Nuggets", 8);
        Pedido ped3 = new Pedido("La vaca picada", 7, 3, 3, "1 Braserito para 2", 15);

        pedidos.add(ped3);
        pedidos.add(ped2);
        pedidos.add(ped1);
    }
}
