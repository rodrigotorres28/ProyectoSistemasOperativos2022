import java.util.List;

public class Logger {
    private List<LogPedido> listaPedidosSimulados;

    public void registrarPedido(Pedido pedido, long contador){
        LogPedido logPedido = new LogPedido(pedido.getId(), String.valueOf(contador));
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

}
