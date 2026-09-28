package logica;

import java.io.IOException;
import bean.ServicioBean;

public interface ICrud {
    void insertar(ServicioBean servicio) throws IOException;
    String listar(); 
    void borrar(String placa) throws IOException;
    void actualizarPago(String placa) throws IOException;
    String buscar(String placa) throws IOException; 
    void exportarSerializado();
    void generarReporteIngresos() throws IOException; 
}