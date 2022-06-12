import java.util.concurrent.Semaphore;

public class Repartidor implements Runnable{

	private boolean enEspera = true;
    private Boolean enviando;
	private int id;
    private int distanciaRestante;
	private Pedido pedido;
	private ManejadorRepartidores manejadorRepartidores;
	private Semaphore semComienzo;
    private Semaphore semFinal;
    private Semaphore semFinalTodos;

	public Repartidor(int id, ManejadorRepartidores manejadorRepartidores, Semaphore SemComienzo, Semaphore SemFinal, Semaphore SemFinalTodos) {
		this.id = id;
		this.manejadorRepartidores = manejadorRepartidores;
		this.semComienzo = SemComienzo;
		this.semFinal = SemFinal;
		this.semFinalTodos = SemFinalTodos;
	}

	public void setManejadorRepartidores(ManejadorRepartidores manejadorRepartidores) {
		this.manejadorRepartidores = manejadorRepartidores;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public void setEnEspera(boolean enEspera) {
		this.enEspera = enEspera;
	}

	public void setDistanciaRestante(int distanciaRestante) {
		this.distanciaRestante = distanciaRestante;
	}

	public void setEnviando(Boolean enviando) {
		this.enviando = enviando;
	}

	// Getters
    public int getId() {
		return id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	@Override
	public void run() {
		while(true){

			try {semComienzo.acquire();} catch (InterruptedException e) {}
			
			if(distanciaRestante != 0){
				distanciaRestante--;
			}
			
			if (!enEspera){
				if(enviando && distanciaRestante == 0){
					System.out.println("Finalizó el envio del pedido #" + pedido.getId() + ". Por el repartidor #" + id);
					distanciaRestante = pedido.getDistanciaCliente();
					enviando = false;
				}
				if(!enviando && distanciaRestante == 0){
					System.out.println("El repartidor #" + id + " vuelve a estar listo");
					enEspera = true;
					manejadorRepartidores.repartidorListo(this);
				}

			}

			semFinal.release();

            try {semFinalTodos.acquire();} catch (InterruptedException e) {}
		}
	}

}