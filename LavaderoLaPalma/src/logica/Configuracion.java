package logica;

public class Configuracion {
    public static final String RUTA_DAT = "datos/servicios_lapalma.dat";
    
    
    public static final int TAM_PLACA = 6;
    public static final int TAM_MARCA = 15;
    public static final int TAM_NOMBRE = 20;   
    public static final int TAM_CELULAR = 10;  
    public static final int TAM_TIPO_LAVADO = 30; 
    public static final int TAM_FECHA = 10;
    
    
    
    public static final int OFFSET_PAGO = 
        (TAM_PLACA * 2) + 
        (TAM_MARCA * 2) + 
        4 + 
        2 + 
        4 + 
        (TAM_NOMBRE * 2) + 
        (TAM_CELULAR * 2) + 
        (TAM_TIPO_LAVADO * 2) + 
        8;  
    
    public static final int TAM_REGISTRO = 
        OFFSET_PAGO + 
        1 + 
        (TAM_FECHA * 2);
}