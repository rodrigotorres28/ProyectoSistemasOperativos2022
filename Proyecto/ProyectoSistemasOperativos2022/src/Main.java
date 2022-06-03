public class Main {

    public static void main(String[] args) throws Exception {

        ManejadorArchivosGenerico manejador = new ManejadorArchivosGenerico();
        ManejadorComercios manejadorComercios = new ManejadorComercios();
        ManejadorPedidos manejadorPedidos = new ManejadorPedidos();

        //Creo el hilo reloj
        Runnable reloj = new Reloj(manejadorComercios, manejadorPedidos);
        Thread hiloReloj = new Thread(reloj);

        manejadorComercios.cargarComercios();
        manejadorPedidos.cargarPedidos();
        
        hiloReloj.run();

    }
}