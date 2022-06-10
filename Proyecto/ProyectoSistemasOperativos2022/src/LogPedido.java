public class LogPedido {
    private int id;
    private String ingresaTick;
    private String inicioElaboracionTick;
    private String finElaboracionTick;
    private String inicioEnvioTick;
    private String finEnvioTick;

    public LogPedido(int Id, String IngresaTick){
        this.id = Id;
        this.ingresaTick = IngresaTick;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIngresaTick(String ingresaTick) {
        this.ingresaTick = ingresaTick;
    }
    
    public void setInicioElaboracionTick(String inicioElaboracionTick) {
        this.inicioElaboracionTick = inicioElaboracionTick;
    }

    public void setFinElaboracionTick(String finElaboracionTick) {
        this.finElaboracionTick = finElaboracionTick;
    }

    public void setInicioEnvioTick(String inicioEnvioTick) {
        this.inicioEnvioTick = inicioEnvioTick;
    }

    public void setFinEnvioTick(String finEnvioTick) {
        this.finEnvioTick = finEnvioTick;
    }

    public int getId() {
        return id;
    }

}
