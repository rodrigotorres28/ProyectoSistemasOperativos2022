import java.util.concurrent.Semaphore;

public class Reloj implements Runnable{
    
    private long contadorGlobal = 0;
    private ManejadorComercios manejadorComercios;
    private ManejadorRepartidores manejadorRepartidores;
    private ManejadorPedidos manejadorPedidos;
    private Semaphore semTickComercios = new Semaphore(0);
    private Semaphore semFinTickComercios = new Semaphore(0);
    private Semaphore semTickPedidos = new Semaphore(0);
    private Semaphore semFinTickPedidos = new Semaphore(0);
    private Semaphore semTickRepartidores = new Semaphore(0);
    private Semaphore semFinTickRepartidores = new Semaphore(0);
    private Logger logger;
    

    public long getContador(){
        return this.contadorGlobal;
    }
    public Reloj(ManejadorComercios ManejadorComercios, ManejadorRepartidores ManejadorRepartidores, ManejadorPedidos ManejadorPedidos, Logger Logger) {
        this.manejadorComercios = ManejadorComercios;
        this.manejadorRepartidores = ManejadorRepartidores;
        this.manejadorPedidos = ManejadorPedidos;
        this.logger = Logger;
    }

    @Override
    public void run() {
        manejadorPedidos.setManejadorComercios(manejadorComercios);
        manejadorPedidos.setManejadorRepartidores(manejadorRepartidores);
        manejadorPedidos.setSemTickPedidos(semTickPedidos);
        manejadorPedidos.setSemFinTickPedidos(semFinTickPedidos);
        manejadorPedidos.setLogger(logger);
        manejadorPedidos.setIniciando(false);

        manejadorComercios.setManejadorRepartidores(manejadorRepartidores);
        manejadorComercios.setSemTickComercios(semTickComercios);
        manejadorComercios.setSemFinTickComercios(semFinTickComercios);
        manejadorComercios.setIniciando(false);

        manejadorRepartidores.setManejadorComercios(manejadorComercios);
        manejadorRepartidores.setSemTickRepartidores(semTickRepartidores);
        manejadorRepartidores.setSemFinTickRepartidores(semFinTickRepartidores);
        manejadorRepartidores.setLogger(logger);
        manejadorRepartidores.setIniciando(false);
        

        long inicio = System.currentTimeMillis();
        int finSimulacion = 100;
        while(finSimulacion > 0){
            //Se actualizan los contadores
            manejadorComercios.setTickActual(contadorGlobal);
            manejadorRepartidores.setTickActual(contadorGlobal);


            manejadorPedidos.setContadorGlobal(contadorGlobal);
            logger.setContadorGlobal(contadorGlobal);
            semTickPedidos.release();
            semTickComercios.release();
            semTickRepartidores.release();
            
            try {
                semFinTickComercios.acquire(); 
                semFinTickPedidos.acquire();
                semFinTickRepartidores.acquire();
            } catch (InterruptedException e1) {}

            if(manejadorPedidos.getTerminar()){
                //Se espera 100 ticks para finalizar por el extra??o caso posible de que tras recibirse todos los pedidos; todos los repartidores esten a
                //la espera pero queden pedidos aun siendo elaborados.
                if(manejadorRepartidores.getRepartidoresListos().size() == manejadorRepartidores.getTotalDeRepartidores())
                {
                    finSimulacion--; 
                }
                else{
                    finSimulacion = 100;
                }
            }

            System.out.println("\nTermin?? tick #" + contadorGlobal + " | Transcurridos:  "+ (System.currentTimeMillis() - inicio)+ "ms\n----------------------------------------------------------------------------");

            //try {Thread.sleep(500);} catch (InterruptedException e) {} // <-- Utilizar este sleep si se quiere cambiar el tiempo en ms de los ticks
            
            contadorGlobal++;
        }
    }
    
}
