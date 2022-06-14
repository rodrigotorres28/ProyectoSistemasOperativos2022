public class LogPedido {
    private int id;
    private String tipoComercio;
    private String ingresaTick;
    private String inicioElaboracionTick;
    private String finElaboracionTick;
    private String asignacionRepartidor;
    private String inicioEnvioTick;
    private String finEnvioTick;

    public LogPedido(int Id, String TipoComercio, String IngresaTick){
        this.id = Id;
        this.tipoComercio = TipoComercio;
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

    public void setAsignacionRepartidor(String asignacionRepartidor) {
        this.asignacionRepartidor = asignacionRepartidor;
    }
    
    public int getId() {
        return id;
    }
    public String getTipoComercio() {
        return tipoComercio;
    }
    public String getIngresaTick() {
        return ingresaTick;
    }
    public String getInicioElaboracionTick() {
        return inicioElaboracionTick;
    }
    public String getFinElaboracionTick() {
        return finElaboracionTick;
    }
    public String getInicioEnvioTick() {
        return inicioEnvioTick;
    }
    public String getFinEnvioTick() {
        return finEnvioTick;
    }
    public String getAsignacionRepartidor() {
        return asignacionRepartidor;
    }
}
