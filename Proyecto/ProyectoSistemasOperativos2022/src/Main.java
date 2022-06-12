public class Main {
    public static void main(String[] args) throws Exception {

        //ManejadorArchivosGenerico manejador = new ManejadorArchivosGenerico();

        ManejadorComercios manejadorComercios = new ManejadorComercios();
        Thread hilomanejadorComercios = new Thread((Runnable)manejadorComercios);

        ManejadorRepartidores manejadorRepartidores = new ManejadorRepartidores();
        Thread hilomanejadorRepartidores = new Thread((Runnable)manejadorRepartidores);

        ManejadorPedidos manejadorPedidos = new ManejadorPedidos();
        Thread hilomanejadorPedidos = new Thread((Runnable)manejadorPedidos);

        Runnable reloj = new Reloj(manejadorComercios, manejadorRepartidores, manejadorPedidos);
        Thread hiloReloj = new Thread(reloj);

        manejadorComercios.cargarComercios(manejadorRepartidores);
        manejadorPedidos.cargarPedidos();
        manejadorRepartidores.cargarRepartidores();

        hilomanejadorPedidos.start();
        hilomanejadorComercios.start();
        hilomanejadorRepartidores.start();
        hiloReloj.run();
        System.out.println("Generando salida...");
        //salida logger
        System.out.println("FIN DE LA SIMULACIÓN");
        System.exit(0);
    }
}