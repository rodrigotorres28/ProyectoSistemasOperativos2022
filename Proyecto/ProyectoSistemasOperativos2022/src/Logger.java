import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Logger {
    private List<LogPedido> listaPedidosSimulados = new ArrayList<>();

    public void registrarPedido(Pedido pedido, long contador){
        LogPedido logPedido = new LogPedido(pedido.getId(), pedido.getTipoComercio(), String.valueOf(contador));
        listaPedidosSimulados.add(logPedido);
    }

    public void actualizarPedido(Pedido pedido, String etapa, long contador){
        for (LogPedido logPedido : listaPedidosSimulados) {
            if (logPedido.getId() == pedido.getId()){
                switch(etapa){
                    case "iniElab":
                        logPedido.setInicioElaboracionTick(String.valueOf(contador));
                        break;
                    case "finElab":
                        logPedido.setFinElaboracionTick(String.valueOf(contador));
                        break;
                    case "iniEnv":
                        logPedido.setInicioEnvioTick(String.valueOf(contador));
                        break;
                    case "finEnv":
                        logPedido.setFinEnvioTick(String.valueOf(contador));
                        break;
                    default:
                        System.out.println("Error del Switch Case del Logger");
                        break;
                }
            }
        }
    }

    public String[] crearStringsParaSalida(){
        String[] stringSalida = new String[listaPedidosSimulados.size() + 1];
        String linea = "ID,Tipo de Comercio,Ingreso,Inicio Elaboración,Fin Elaboración,Inicio Envio,Fin Envio,Demora total del Pedido,Demora Sin elaboración ni envio";
        stringSalida[0] = linea;
        int i = 1;
        for (LogPedido logPedido : listaPedidosSimulados) {
            String demoraTotal = String.valueOf(Integer.parseInt(logPedido.getFinEnvioTick())-Integer.parseInt(logPedido.getIngresaTick()));
            int elaboracion = Integer.parseInt(logPedido.getFinElaboracionTick()) - Integer.parseInt(logPedido.getInicioElaboracionTick());
            int envio = Integer.parseInt(logPedido.getFinEnvioTick()) - Integer.parseInt(logPedido.getInicioEnvioTick());
            String demoraSinElaboracionNiEnvio = String.valueOf(Integer.parseInt(demoraTotal) - elaboracion - envio);
            
            StringJoiner str = new StringJoiner(",");
            str.add(String.valueOf(logPedido.getId()));
            str.add(logPedido.getTipoComercio());
            str.add(logPedido.getIngresaTick());
            str.add(logPedido.getInicioElaboracionTick());
            str.add(logPedido.getFinElaboracionTick());
            str.add(logPedido.getInicioEnvioTick());
            str.add(logPedido.getFinEnvioTick());
            str.add(demoraTotal);
            str.add(demoraSinElaboracionNiEnvio);
            linea = str.toString();
            stringSalida[i] = linea;
            i++;
        }
        return stringSalida;
    }

}
