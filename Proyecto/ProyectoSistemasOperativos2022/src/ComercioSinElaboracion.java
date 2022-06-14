import java.util.concurrent.Semaphore;

public class ComercioSinElaboracion extends Comercio {



    public ComercioSinElaboracion(String Nombre, ManejadorRepartidores ManejadorRepartidores, Semaphore SemComienzo,
            Semaphore SemFinal, Semaphore SemFinalTodos, Logger Logger) {
        super(Nombre, ManejadorRepartidores, SemComienzo, SemFinal, SemFinalTodos, Logger);
    }

    @Override
    public void run() {
        while(true){
            try {semComienzo.acquire();} catch (InterruptedException e) {}

            try {pedidosMutex.acquire();} catch (InterruptedException e) {}
            for (Pedido pedido : pedidos) {
                pedidosListos.add (pedido);
                pedidos.remove(pedido);
            }
            pedidosMutex.release();
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
                    logger.actualizarPedido(pedido, "iniElab");
                    logger.actualizarPedido(pedido, "finElab");
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


}
