import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Semaphore;

public class Logger {
    private List<LogPedido> listaPedidosSimulados = new ArrayList<>();
    private Long contadorGlobal;
    Semaphore semPedidosSimuladosMutex = new Semaphore(1);

    public void setContadorGlobal(Long contadorGlobal) {
        this.contadorGlobal = contadorGlobal;
    }

    public void registrarPedido(Pedido pedido){
        LogPedido logPedido = new LogPedido(pedido.getId(), pedido.getTipoComercio(), String.valueOf(contadorGlobal));
        listaPedidosSimulados.add(logPedido);
    }

    public void actualizarPedido(Pedido pedido, String etapa){
        try {semPedidosSimuladosMutex.acquire();} catch (InterruptedException e) {}
        for (int i=0; i < listaPedidosSimulados.size(); i++) {
            if (listaPedidosSimulados.get(i).getId() == pedido.getId()){
                switch(etapa){
                    case "iniElab":
                        listaPedidosSimulados.get(i).setInicioElaboracionTick(String.valueOf(contadorGlobal));
                        break;
                    case "finElab":
                        listaPedidosSimulados.get(i).setFinElaboracionTick(String.valueOf(contadorGlobal));
                        break;
                    case "asignRep":
                        listaPedidosSimulados.get(i).setAsignacionRepartidor(String.valueOf(contadorGlobal));
                    case "iniEnv":
                        listaPedidosSimulados.get(i).setInicioEnvioTick(String.valueOf(contadorGlobal));
                        break;
                    case "finEnv":
                        listaPedidosSimulados.get(i).setFinEnvioTick(String.valueOf(contadorGlobal));
                        break;
                    default:
                        System.out.println("Error del Switch Case del Logger");
                        break;
                }
            }
        }
        semPedidosSimuladosMutex.release();
    }

    public String[] crearStringsParaSalida(){
        String[] stringSalida = new String[listaPedidosSimulados.size() + 1];
        String linea = "ID;Tipo de Comercio;Ingreso;Inicio Elaboración;Fin Elaboración;Asignación de repartidor;Inicio Envio;Fin Envio;Demora total del Pedido;Demora Sin elaboración ni envio;Tiempo para asignar repartidor;Demora de entrega al repartidor";
        stringSalida[0] = linea;
        int i = 1;
        for (LogPedido logPedido : listaPedidosSimulados) {
            String demoraTotal = String.valueOf(Integer.parseInt(logPedido.getFinEnvioTick())-Integer.parseInt(logPedido.getIngresaTick()));
            int elaboracion = Integer.parseInt(logPedido.getFinElaboracionTick()) - Integer.parseInt(logPedido.getInicioElaboracionTick());
            int envio = Integer.parseInt(logPedido.getFinEnvioTick()) - Integer.parseInt(logPedido.getInicioEnvioTick());
            String demoraSinElaboracionNiEnvio = String.valueOf(Integer.parseInt(demoraTotal) - elaboracion - envio);
            int diferenciaAsignYFinElab = Integer.parseInt(logPedido.getFinElaboracionTick()) - Integer.parseInt(logPedido.getAsignacionRepartidor());
            String strDifAsignElab = String.valueOf(Math.abs(diferenciaAsignYFinElab));
            String tiempoParaAsignarRepartidor = String.valueOf(Integer.parseInt(logPedido.getAsignacionRepartidor()) - Integer.parseInt(logPedido.getIngresaTick()));
            StringJoiner str = new StringJoiner(";");
            str.add(String.valueOf(logPedido.getId()));
            str.add(logPedido.getTipoComercio());
            str.add(logPedido.getIngresaTick());
            str.add(logPedido.getInicioElaboracionTick());
            str.add(logPedido.getFinElaboracionTick());
            str.add(logPedido.getAsignacionRepartidor());
            str.add(logPedido.getInicioEnvioTick());
            str.add(logPedido.getFinEnvioTick());
            str.add(demoraTotal);
            str.add(demoraSinElaboracionNiEnvio);
            str.add(tiempoParaAsignarRepartidor);
            str.add(strDifAsignElab);
            linea = str.toString();
            stringSalida[i] = linea;
            i++;
        }
        return stringSalida;
    }

}
