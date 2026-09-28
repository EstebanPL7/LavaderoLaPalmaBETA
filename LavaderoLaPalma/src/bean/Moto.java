package bean;

public class Moto extends Vehiculo {
	private static final long serialVersionUID = 1L;
	private int cilindraje;

    public Moto() {}

    public Moto(String placa, String marca, int modelo, int cilindraje) {
        super(placa, marca, modelo);
        this.cilindraje = cilindraje;
    }

    public int getCilindraje() { return cilindraje; }
    public void setCilindraje(int cilindraje) { this.cilindraje = cilindraje; }
}