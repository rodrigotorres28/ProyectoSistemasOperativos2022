import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ManejadorRepartidores implements Runnable {

    private Boolean iniciando = true;
    private Queue<Pedido> colaFarmacia = new LinkedList<Pedido>();
    private Queue<Pedido> colaAlmacen = new LinkedList<Pedido>();
    private List<Pedido> colaRestauranteDistanciaCorta = new ArrayList<>();
    private Pedido siguienteDistanciaCorta;
    private List<Pedido> colaRestauranteDistanciaMedia = new ArrayList<>();
    private Pedido siguienteDistanciaMedia;
    private List<Pedido> colaRestauranteDistanciaLarga = new ArrayList<>();
    private Pedido siguienteDistanciaLarga;
    private Pedido siguienteRestaurante;
    private Pedido siguienteParaAtender;
    private Semaphore colasRestaurantesMutex = new Semaphore(1);
    private Semaphore semComienzo = new Semaphore(0);
    private Semaphore semFinal = new Semaphore(0);
    private Semaphore semFinalTodos = new Semaphore(0);
    private Semaphore semTickRepartidores;
    private Semaphore semFinTickRepartidores;
    private Queue<Repartidor> repartidoresListos = new LinkedList<Repartidor>();
    private List<Repartidor> repartidoresEnviando = new ArrayList<Repartidor>();
    private int totalDeRepartidores;
    private ManejadorComercios manejadorComercios;

    public void setIniciando(Boolean iniciando) {
        this.iniciando = iniciando;
    }
    
    public void setSemTickRepartidores(Semaphore semTickRepartidores) {
        this.semTickRepartidores = semTickRepartidores;
    }

    public void setSemFinTickRepartidores(Semaphore semFinTickRepartidores) {
        this.semFinTickRepartidores = semFinTickRepartidores;
    }


    public void setManejadorComercios(ManejadorComercios manejadorComercios) {
        this.manejadorComercios = manejadorComercios;
    }

    @Override
    public void run() {
        while(iniciando){
            try {Thread.sleep(1);} catch (InterruptedException e) {}
        }
        while(true){
            
            try {semTickRepartidores.acquire();} catch (InterruptedException e) {}

            if(siguienteDistanciaCorta != null){
                siguienteDistanciaCorta.setAntiguedad(siguienteDistanciaCorta.getAntiguedad()+1);
            }
            if(siguienteDistanciaMedia != null){
                siguienteDistanciaMedia.setAntiguedad(siguienteDistanciaMedia.getAntiguedad()+1);
            }
            if(siguienteDistanciaLarga != null){
                siguienteDistanciaLarga.setAntiguedad(siguienteDistanciaLarga.getAntiguedad()+1);
            }
            if(siguienteRestaurante != null){
                siguienteRestaurante.setAntiguedad(siguienteRestaurante.getAntiguedad()+1);
            }

            try {colasRestaurantesMutex.acquire();} catch (InterruptedException e) {}

            for (int i=0; i < colaRestauranteDistanciaCorta.size(); i++) {
                colaRestauranteDistanciaCorta.get(i).setAntiguedad(colaRestauranteDistanciaCorta.get(i).getAntiguedad() + 1);
                
                if (siguienteDistanciaCorta == null){
                    siguienteDistanciaCorta = colaRestauranteDistanciaCorta.get(i);
                    colaRestauranteDistanciaCorta.remove(colaRestauranteDistanciaCorta.get(i));
                }
                else if ((3*siguienteDistanciaCorta.getAntiguedad())/(siguienteDistanciaCorta.getTiempoElaboracion() + 1) < (3*colaRestauranteDistanciaCorta.get(i).getAntiguedad())/(colaRestauranteDistanciaCorta.get(i).getTiempoElaboracion() + 1)){
                    colaRestauranteDistanciaCorta.add(siguienteDistanciaCorta);
                    siguienteDistanciaCorta = colaRestauranteDistanciaCorta.get(i);
                    colaRestauranteDistanciaCorta.remove(colaRestauranteDistanciaCorta.get(i));
                }
            }
            
            for (int i=0; i < colaRestauranteDistanciaMedia.size(); i++) {
                colaRestauranteDistanciaMedia.get(i).setAntiguedad(colaRestauranteDistanciaMedia.get(i).getAntiguedad() + 1);

                if(colaRestauranteDistanciaMedia.get(i).getAntiguedad() >= 10){
                    colaRestauranteDistanciaMedia.get(i).setAntiguedad(0);
                    colaRestauranteDistanciaCorta.add(colaRestauranteDistanciaMedia.get(i));
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
                else if (siguienteDistanciaMedia == null){
                    siguienteDistanciaMedia = colaRestauranteDistanciaMedia.get(i);
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
                else if ((3*siguienteDistanciaMedia.getAntiguedad())/(siguienteDistanciaMedia.getTiempoElaboracion() + 1) < (3*colaRestauranteDistanciaMedia.get(i).getAntiguedad())/(colaRestauranteDistanciaMedia.get(i).getTiempoElaboracion() + 1)){
                    colaRestauranteDistanciaMedia.add(siguienteDistanciaMedia);
                    siguienteDistanciaMedia = colaRestauranteDistanciaMedia.get(i);
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
            }

            for (int i=0; i < colaRestauranteDistanciaLarga.size(); i++){
                colaRestauranteDistanciaLarga.get(i).setAntiguedad(colaRestauranteDistanciaLarga.get(i).getAntiguedad() + 1);
                
                if(colaRestauranteDistanciaLarga.get(i).getAntiguedad() >= 10){
                    colaRestauranteDistanciaLarga.get(i).setAntiguedad(0);
                    colaRestauranteDistanciaMedia.add(colaRestauranteDistanciaLarga.get(i));
                    colaRestauranteDistanciaLarga.remove(colaRestauranteDistanciaLarga.get(i));
                }
                else if (siguienteDistanciaLarga == null){
                    siguienteDistanciaLarga = colaRestauranteDistanciaLarga.get(i);
                    colaRestauranteDistanciaLarga.remove(colaRestauranteDistanciaLarga.get(i));
                }
                else if ((3*siguienteDistanciaLarga.getAntiguedad())/(siguienteDistanciaLarga.getTiempoElaboracion() + 1) < (3*colaRestauranteDistanciaLarga.get(i).getAntiguedad())/(colaRestauranteDistanciaLarga.get(i).getTiempoElaboracion() + 1)){
                    colaRestauranteDistanciaLarga.add(siguienteDistanciaLarga);
                    siguienteDistanciaLarga = colaRestauranteDistanciaLarga.get(i);
                    colaRestauranteDistanciaLarga.remove(colaRestauranteDistanciaLarga.get(i));
                }
            }

            colasRestaurantesMutex.release();
            
            if(siguienteRestaurante == null){
                if(siguienteDistanciaCorta != null){
                siguienteRestaurante = siguienteDistanciaCorta;
                siguienteRestaurante.setAntiguedad(0);
                siguienteDistanciaCorta = null;
                }
                else if(siguienteDistanciaMedia != null){
                    siguienteRestaurante = siguienteDistanciaMedia;
                    siguienteRestaurante.setAntiguedad(0);
                    siguienteDistanciaMedia = null;
                }
                else if(siguienteDistanciaLarga != null){
                    siguienteRestaurante = siguienteDistanciaLarga;
                    siguienteRestaurante.setAntiguedad(0);
                    siguienteDistanciaLarga = null;
                }
            }

            if(siguienteRestaurante != null){
                if(siguienteRestaurante.getAntiguedad() >= 20){
                    siguienteRestaurante.setAntiguedad(0);
                    colaAlmacen.add(siguienteRestaurante);
                    siguienteRestaurante = null;
                }
            }
            if(!colaAlmacen.isEmpty() && siguienteRestaurante != null){
                if(siguienteRestaurante.getAntiguedad() >= 30){
                    colaFarmacia.add(colaAlmacen.remove());
                }
            }

            if(siguienteParaAtender == null){
                if(!colaFarmacia.isEmpty())
                {
                    siguienteParaAtender = colaFarmacia.remove();
                }
                else if(!colaAlmacen.isEmpty()){
                    siguienteParaAtender = colaAlmacen.remove();
                }
                else if (siguienteRestaurante != null){
                    siguienteParaAtender = siguienteRestaurante;
                    siguienteRestaurante = null;
                }
            }

            if(!repartidoresListos.isEmpty() && siguienteParaAtender != null){
                Repartidor repartidor = repartidoresListos.remove();
                for (Comercio comercio : manejadorComercios.getComercios()) {
                    if(siguienteParaAtender.getComercio() == comercio.nombre){
                        comercio.agregarRepartidor(repartidor);
                        break;
                    }
                }
                siguienteParaAtender = null;
            }

            semComienzo.release(totalDeRepartidores);
            try {semFinal.acquire(totalDeRepartidores);} catch (InterruptedException e) {}
            semFinalTodos.release(totalDeRepartidores);

            semFinTickRepartidores.release();

        }
    }
    
    public void nuevoPedido(Pedido pedido){
        switch (pedido.getTipoComercio()){
            case "farmacia":
                colaFarmacia.add(pedido);
                break;
            case "almacen":
                colaAlmacen.add(pedido);
                break;
            case "restaurante":
                try {colasRestaurantesMutex.acquire();} catch (InterruptedException e) {}
                if(pedido.getDistanciaCliente()<=5){
                    colaRestauranteDistanciaCorta.add(pedido);
                    colasRestaurantesMutex.release();
                    break;
                }
                if(pedido.getDistanciaCliente()<=11){
                    colaRestauranteDistanciaMedia.add(pedido);
                    colasRestaurantesMutex.release();
                    break;
                }
                colaRestauranteDistanciaLarga.add(pedido);
                colasRestaurantesMutex.release();
                break;
            default:
                System.out.println("ERROR: Pedido con 'Tipo de comercio' mal definido. ID: " + pedido.getId());
                break;
        }
    }

    public void repartidorListo(Repartidor repartidor){
        repartidoresEnviando.remove(repartidor);
        repartidoresListos.add(repartidor);
    }

    public void repartidorEnviando(Repartidor repartidor){
        repartidoresEnviando.add(repartidor);
    }

	public void cargarRepartidores() {

        Repartidor r1 = new Repartidor(1, this, semComienzo, semFinal, semFinalTodos);
        Repartidor r2 = new Repartidor(2, this, semComienzo, semFinal, semFinalTodos);
        Repartidor r3 = new Repartidor(3, this, semComienzo, semFinal, semFinalTodos);

        repartidoresListos.add(r1);
        repartidoresListos.add(r2);
        repartidoresListos.add(r3);

        totalDeRepartidores = repartidoresListos.size();
        for (Repartidor rep : repartidoresListos) {
            Thread hilo = new Thread(rep);
            hilo.start();
        }
	}
}
