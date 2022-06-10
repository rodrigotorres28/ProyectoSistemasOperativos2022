import java.util.concurrent.Semaphore;

public class Reloj implements Runnable{
    
    private long contadorGlobal = 0;
    private ManejadorComercios manejadorComercios;
    private ManejadorPedidos manejadorPedidos;
    private Semaphore semTickComercios = new Semaphore(0);
    private Semaphore semFinTickComercios = new Semaphore(0);
    private Semaphore semTickPedidos = new Semaphore(0);
    private Semaphore semFinTickPedidos = new Semaphore(0);

    public long getContador(){
        return this.contadorGlobal;
    }
    public Reloj(ManejadorComercios ManejadorComercios, ManejadorPedidos ManejadorPedidos) {
        this.manejadorComercios = ManejadorComercios;
        this.manejadorPedidos = ManejadorPedidos;
    }

    @Override
    public void run() {
        manejadorPedidos.setManejadorComercios(manejadorComercios);
        manejadorPedidos.setSemTickPedidos(semTickPedidos);
        manejadorPedidos.setSemFinTickPedidos(semFinTickPedidos);

        manejadorComercios.setSemTickComercios(semTickComercios);
        manejadorComercios.setSemFinTickComercios(semFinTickComercios);

        long inicio = System.currentTimeMillis();
        while(contadorGlobal <= 50){
            manejadorPedidos.setContadorGlobal(contadorGlobal);
            semTickPedidos.release();
            semTickComercios.release();
            //manejador de repartidores
            
            try {
                semFinTickComercios.acquire(); 
                semFinTickPedidos.acquire();
            } catch (InterruptedException e1) {}
            System.out.println("\nTerminó tick #" + contadorGlobal + " | Transcurridos:  "+ (System.currentTimeMillis() - inicio)+ "ms\n----------------------------------------------------------------------------");

            //try {Thread.sleep(500);} catch (InterruptedException e) {}
            
            contadorGlobal++;
        }
    }
    
}
