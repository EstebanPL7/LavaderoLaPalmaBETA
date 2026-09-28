package bean;

import java.io.Serializable;

public abstract class Vehiculo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    protected String placa;
    protected String marca;
    protected int modelo;

    public Vehiculo() {}

    public Vehiculo(String placa, String marca, int modelo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getModelo() { return modelo; }
    public void setModelo(int modelo) { this.modelo = modelo; }
}
