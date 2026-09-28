package bean;

import java.time.LocalDate;
import java.io.Serializable;

public class ServicioBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Vehiculo vehiculo;
    private String nombreCliente;  
    private String celularCliente; 
    private String tipoLavado;
    private double valor;
    private boolean pagado;
    private LocalDate fecha;

    public ServicioBean() {}

    // Constructor
    public ServicioBean(Vehiculo vehiculo, String nombreCliente, String celularCliente, String tipoLavado, double valor, boolean pagado, LocalDate fecha) {
        this.vehiculo = vehiculo;
        this.nombreCliente = nombreCliente;
        this.celularCliente = celularCliente;
        this.tipoLavado = tipoLavado;
        this.valor = valor;
        this.pagado = pagado;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCelularCliente() { return celularCliente; }
    public void setCelularCliente(String celularCliente) { this.celularCliente = celularCliente; }
    public String getTipoLavado() { return tipoLavado; }
    public void setTipoLavado(String tipoLavado) { this.tipoLavado = tipoLavado; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}