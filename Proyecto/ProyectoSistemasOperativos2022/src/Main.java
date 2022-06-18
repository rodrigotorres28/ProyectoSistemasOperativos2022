public class Main {
    public static void main(String[] args) throws Exception {

        ManejadorArchivosGenerico.borrarYEscribirLinea("Salidas/BitacoraPedidos.csv", "Tick,Evento,Tipo de Comercio,ID Pedido");
        ManejadorArchivosGenerico.borrarYEscribirLinea("Salidas/BitacoraRepartidores.csv", "Tick,Evento,ID Repartidor");
        ManejadorArchivosGenerico.borrarYEscribirLinea("Salidas/SaturacionDeColas.csv", "Tick,Restaurante dist. Larga,Restaurante dist. Media,Restaurante dist. Corta,Almacen,Farmacia,ID Siguiente Restaurante, ID Siguiente Para Atender");
        Logger logger = new Logger();

        ManejadorComercios manejadorComercios = new ManejadorComercios();
        Thread hilomanejadorComercios = new Thread((Runnable)manejadorComercios);

        ManejadorRepartidores manejadorRepartidores = new ManejadorRepartidores();
        Thread hilomanejadorRepartidores = new Thread((Runnable)manejadorRepartidores);

        ManejadorPedidos manejadorPedidos = new ManejadorPedidos();
        Thread hilomanejadorPedidos = new Thread((Runnable)manejadorPedidos);

        Runnable reloj = new Reloj(manejadorComercios, manejadorRepartidores, manejadorPedidos, logger);
        Thread hiloReloj = new Thread(reloj);
        
        Boolean comerciosCorrecto = manejadorComercios.cargarComercios(manejadorRepartidores, logger);
        Boolean pedidosCorrecto = manejadorPedidos.cargarPedidos();
        Boolean repartidoresCorrecto = manejadorRepartidores.cargarRepartidores(logger);

        if(!(comerciosCorrecto && pedidosCorrecto && repartidoresCorrecto)){
            System.exit(1);;
        }

        hilomanejadorPedidos.start();
        hilomanejadorComercios.start();
        hilomanejadorRepartidores.start();
        hiloReloj.run();

        System.out.println("Generando salida...");
        String[] salida = logger.crearStringsParaSalida();
        ManejadorArchivosGenerico.borrarYEscribirArchivo("Salidas/ResumenConIndicadores.csv", salida);
        System.out.println("FIN DE LA SIMULACIÓN");
        System.exit(0);
    }
}