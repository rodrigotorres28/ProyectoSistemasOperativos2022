public class Pedido {

  private String comercio;
  private String tipoComercio;
  private int tiempoElaboracion;
  private int distanciaCliente;
  private int id;
  private String productos;
  private int horaIngresado;
  private int antiguedad;

  public void setAntiguedad(int antiguedad) {
    this.antiguedad = antiguedad;
  }

  public int getAntiguedad() {
    return antiguedad;
  }

  public int getHoraIngresado() {
    return horaIngresado;
  }

  public String getComercio() {
    return comercio;
  }

  public int getTiempoElaboracion() {
    return tiempoElaboracion;
  }

  public int getDistanciaCliente() {
    return distanciaCliente;
  }

  public int getId() {
    return id;
  }

  public String getProductos() {
    return productos;
  }

  public String getTipoComercio() {
    return tipoComercio;
  }

  public Pedido(String Comercio, String TipoComercio , int TiempoElaboracion, int DistanciaCliente, int Id, String Productos,
      int HoraIngresado) {

    this.comercio = Comercio;
    this.tipoComercio = TipoComercio;
    this.tiempoElaboracion = TiempoElaboracion;
    this.distanciaCliente = DistanciaCliente;
    this.id = Id;
    this.productos = Productos;
    this.horaIngresado = HoraIngresado;
  }

}