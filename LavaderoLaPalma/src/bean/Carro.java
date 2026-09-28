package bean;

public class Carro extends Vehiculo {
	private static final long serialVersionUID = 1L;
	private int numPuertas;

    public Carro() {}

    public Carro(String placa, String marca, int modelo, int numPuertas) {
        super(placa, marca, modelo);
        this.numPuertas = numPuertas;
    }

    public int getNumPuertas() { return numPuertas; }
    public void setNumPuertas(int numPuertas) { this.numPuertas = numPuertas; }
}