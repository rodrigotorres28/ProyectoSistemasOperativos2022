public class Reloj implements Runnable{
    
    private long contadorGlobal = 0;
    private ManejadorComercios manejadorComercios;
    private ManejadorPedidos manejadorPedidos;

    public long getContador(){
        return this.contadorGlobal;
    }
    public Reloj(ManejadorComercios ManejadorComercios, ManejadorPedidos ManejadorPedidos) {
        this.manejadorComercios = ManejadorComercios;
        this.manejadorPedidos = ManejadorPedidos;
    }

    @Override
    public void run() {
        long inicio = System.currentTimeMillis();
        while(true){
            
            manejadorPedidos.checkearPedidos(contadorGlobal, manejadorComercios);
            manejadorComercios.checkearComercios(contadorGlobal);
            //manejador de repartidores
            
            System.out.println("\nTerminó tick #" + contadorGlobal + " | Transcurridos:  "+ (System.currentTimeMillis() - inicio)+ "ms\n----------------------------------------------------------------------------");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            contadorGlobal++;
        }
    }
    
}
