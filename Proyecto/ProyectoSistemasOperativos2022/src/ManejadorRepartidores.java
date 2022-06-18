import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;
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
    private Semaphore colasRepartidoresMutex = new Semaphore(1);
    private Semaphore semComienzo = new Semaphore(0);
    private Semaphore semFinal = new Semaphore(0);
    private Semaphore semFinalTodos = new Semaphore(0);
    private Semaphore semTickRepartidores;
    private Semaphore semFinTickRepartidores;
    private Queue<Repartidor> repartidoresListos = new LinkedList<Repartidor>();
    private List<Repartidor> repartidoresEnviando = new ArrayList<Repartidor>();
    private int totalDeRepartidores;
    private ManejadorComercios manejadorComercios;
    private Logger logger;
    private long tickActual;

    public void setTickActual(long tickActual) {
        this.tickActual = tickActual;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

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

    public Queue<Repartidor> getRepartidoresListos() {
        return repartidoresListos;
    }
    public int getTotalDeRepartidores() {
        return totalDeRepartidores;
    }

    @Override
    public void run() {
        while(iniciando){
            try {Thread.sleep(1);} catch (InterruptedException e) {}
        }
        while(true){     
            try {semTickRepartidores.acquire();} catch (InterruptedException e) {}

            //Se actualizan los contadores
            try {colasRepartidoresMutex.acquire();} catch (InterruptedException e1) {}
            for (Repartidor repartidor: repartidoresListos){
                repartidor.setTickActual(tickActual);
            }
            for (int i = 0; i < repartidoresEnviando.size(); i++){
                repartidoresEnviando.get(i).setTickActual(tickActual);
            }
            colasRepartidoresMutex.release();

            if(siguienteDistanciaCorta != null){
                siguienteDistanciaCorta.setAntiguedad(siguienteDistanciaCorta.getAntiguedad()+1);
                if(siguienteDistanciaCorta.getAntiguedad() >= 25){
                    colaAlmacen.add(siguienteDistanciaCorta);
                    siguienteDistanciaCorta = null;
                }
            }
            if(siguienteDistanciaMedia != null){
                siguienteDistanciaMedia.setAntiguedad(siguienteDistanciaMedia.getAntiguedad()+1);
                if(siguienteDistanciaMedia.getAntiguedad() >= 20){
                    colaRestauranteDistanciaCorta.add(siguienteDistanciaMedia);
                    siguienteDistanciaMedia = null;
                }
            }
            if(siguienteDistanciaLarga != null){
                siguienteDistanciaLarga.setAntiguedad(siguienteDistanciaLarga.getAntiguedad()+1);
                if(siguienteDistanciaLarga.getAntiguedad() >= 20){
                    colaRestauranteDistanciaMedia.add(siguienteDistanciaLarga);
                    siguienteDistanciaLarga = null;
                }
            }
            if(siguienteRestaurante != null){
                siguienteRestaurante.setAntiguedad(siguienteRestaurante.getAntiguedad()+1);
            }

            try {colasRestaurantesMutex.acquire();} catch (InterruptedException e) {}

            for (int i=0; i < colaRestauranteDistanciaCorta.size(); i++) {
                colaRestauranteDistanciaCorta.get(i).setAntiguedad(colaRestauranteDistanciaCorta.get(i).getAntiguedad() + 1);

                if(colaRestauranteDistanciaCorta.get(i).getAntiguedad() >= 25){
                    colaRestauranteDistanciaCorta.get(i).setAntiguedad(0);
                    colaAlmacen.add(colaRestauranteDistanciaCorta.get(i));
                    colaRestauranteDistanciaCorta.remove(colaRestauranteDistanciaCorta.get(i));
                }
                
                else if (siguienteDistanciaCorta == null){
                    siguienteDistanciaCorta = colaRestauranteDistanciaCorta.get(i);
                    colaRestauranteDistanciaCorta.remove(colaRestauranteDistanciaCorta.get(i));
                }
                else if (prioridadHRRN(siguienteDistanciaCorta.getAntiguedad(), siguienteDistanciaCorta.getTiempoElaboracion()) < prioridadHRRN(colaRestauranteDistanciaCorta.get(i).getAntiguedad(), (colaRestauranteDistanciaCorta.get(i).getTiempoElaboracion()))){
                    colaRestauranteDistanciaCorta.add(siguienteDistanciaCorta);
                    siguienteDistanciaCorta = colaRestauranteDistanciaCorta.get(i);
                    colaRestauranteDistanciaCorta.remove(colaRestauranteDistanciaCorta.get(i));
                }
            }
            
            for (int i=0; i < colaRestauranteDistanciaMedia.size(); i++) {
                colaRestauranteDistanciaMedia.get(i).setAntiguedad(colaRestauranteDistanciaMedia.get(i).getAntiguedad() + 1);

                if(colaRestauranteDistanciaMedia.get(i).getAntiguedad() >= 20){
                    colaRestauranteDistanciaMedia.get(i).setAntiguedad(0);
                    colaRestauranteDistanciaCorta.add(colaRestauranteDistanciaMedia.get(i));
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
                else if (siguienteDistanciaMedia == null){
                    siguienteDistanciaMedia = colaRestauranteDistanciaMedia.get(i);
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
                else if (prioridadHRRN(siguienteDistanciaMedia.getAntiguedad(), siguienteDistanciaMedia.getTiempoElaboracion()) < prioridadHRRN(colaRestauranteDistanciaMedia.get(i).getAntiguedad(), (colaRestauranteDistanciaMedia.get(i).getTiempoElaboracion()))){
                    colaRestauranteDistanciaMedia.add(siguienteDistanciaMedia);
                    siguienteDistanciaMedia = colaRestauranteDistanciaMedia.get(i);
                    colaRestauranteDistanciaMedia.remove(colaRestauranteDistanciaMedia.get(i));
                }
            }

            for (int i=0; i < colaRestauranteDistanciaLarga.size(); i++){
                colaRestauranteDistanciaLarga.get(i).setAntiguedad(colaRestauranteDistanciaLarga.get(i).getAntiguedad() + 1);
                
                if(colaRestauranteDistanciaLarga.get(i).getAntiguedad() >= 20){
                    colaRestauranteDistanciaLarga.get(i).setAntiguedad(0);
                    colaRestauranteDistanciaMedia.add(colaRestauranteDistanciaLarga.get(i));
                    colaRestauranteDistanciaLarga.remove(colaRestauranteDistanciaLarga.get(i));
                }
                else if (siguienteDistanciaLarga == null){
                    siguienteDistanciaLarga = colaRestauranteDistanciaLarga.get(i);
                    colaRestauranteDistanciaLarga.remove(colaRestauranteDistanciaLarga.get(i));
                }
                else if (prioridadHRRN(siguienteDistanciaLarga.getAntiguedad(), siguienteDistanciaLarga.getTiempoElaboracion()) < prioridadHRRN(colaRestauranteDistanciaLarga.get(i).getAntiguedad(), (colaRestauranteDistanciaLarga.get(i).getTiempoElaboracion()))){
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
                if(siguienteRestaurante.getAntiguedad() >= 25){
                    siguienteRestaurante.setAntiguedad(0);
                    colaAlmacen.add(siguienteRestaurante);
                    siguienteRestaurante = null;
                }
            }
            if(!colaAlmacen.isEmpty()){
                Pedido primeroAlmacen = colaAlmacen.peek();
                while (primeroAlmacen.getAntiguedad() >=30){
                    colaFarmacia.add(colaAlmacen.remove());
                    if(colaAlmacen.isEmpty()){
                        break;
                    }
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

            StringJoiner str = new StringJoiner(",");
            str.add(String.valueOf(tickActual));
            str.add(String.valueOf(colaRestauranteDistanciaLarga.size()));
            str.add(String.valueOf(colaRestauranteDistanciaMedia.size()));
            str.add(String.valueOf(colaRestauranteDistanciaCorta.size()));
            str.add(String.valueOf(colaAlmacen.size()));
            str.add(String.valueOf(colaFarmacia.size()));
            if (siguienteRestaurante != null){
                str.add(String.valueOf(siguienteRestaurante.getId()));
            }
            else{
                str.add("Ninguno");
            }
            if (siguienteParaAtender != null){
                str.add(String.valueOf(siguienteParaAtender.getId()));
            }
            else{
                str.add("Ninguno");
            }
            ManejadorArchivosGenerico.escribirLinea("Salidas/SaturacionDeColas.csv", str.toString());

            if(!repartidoresListos.isEmpty() && siguienteParaAtender != null){
                Repartidor repartidor = repartidoresListos.remove();
                for (Comercio comercio : manejadorComercios.getComercios()) {
                    if(siguienteParaAtender.getComercio().compareTo(comercio.nombre) == 0){
                        ManejadorArchivosGenerico.escribirLinea("Salidas/BitacoraPedidos.csv", String.valueOf(tickActual) + ",Se asignó el rapartidor,"+ String.valueOf(siguienteParaAtender.getTipoComercio()) + "," + String.valueOf(siguienteParaAtender.getId()));
                        ManejadorArchivosGenerico.escribirLinea("Salidas/BitacoraRepartidores.csv", String.valueOf(tickActual) + ",Se asignó el rapartidor," + String.valueOf(repartidor.getId()));
                        logger.actualizarPedido(siguienteParaAtender, "asignRep");
                        System.out.println("Se asignó el rapartidor #" + repartidor.getId() + " al comercio: " + comercio.getNombre());
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
                if(pedido.getDistanciaCliente()<=9){
                    colaRestauranteDistanciaCorta.add(pedido);
                    colasRestaurantesMutex.release();
                    break;
                }
                if(pedido.getDistanciaCliente()<=14){
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

    private Float prioridadHRRN(int antiguedad, int elaboracion){
        Float fElab = (float) elaboracion;
        float prioridadElaboracion = 100.0f - (((fElab-10.0f)*100.0f)/35.0f);
        return Math.max((antiguedad*5), prioridadElaboracion);
    }

    public void repartidorListo(Repartidor repartidor){
        try {colasRepartidoresMutex.acquire();} catch (InterruptedException e) {}
        repartidoresEnviando.remove(repartidor);
        repartidoresListos.add(repartidor);
        colasRepartidoresMutex.release();
    }

    public void repartidorEnviando(Repartidor repartidor){
        try {colasRepartidoresMutex.acquire();} catch (InterruptedException e) {}
        repartidoresEnviando.add(repartidor);
        colasRepartidoresMutex.release();
    }

	public Boolean cargarRepartidores(Logger logger) {

        String[] entrada = ManejadorArchivosGenerico.leerArchivo("Entradas/ComerciosRepartidores.csv");
        try {
            String repartidores = entrada[0];
            int cantidadRepartidores = Integer.parseInt(repartidores.split(",")[1]);
            for (int i = 1; i <= cantidadRepartidores; i++){
                repartidoresListos.add(new Repartidor(i, this, semComienzo, semFinal, semFinalTodos, logger));
            }
            
        } catch (Exception e) {
            System.out.println("Formato de Pedidos.csv incorrecto");
            return false;
        }

        totalDeRepartidores = repartidoresListos.size();
        for (Repartidor rep : repartidoresListos) {
            Thread hilo = new Thread(rep);
            hilo.start();
        }
        return true;
	}
}
