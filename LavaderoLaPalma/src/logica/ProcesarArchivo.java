package logica;

import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import bean.*;

public class ProcesarArchivo implements ICrud {

    private DecimalFormat obtenerFormatoMoneda() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.'); 
        return new DecimalFormat("$ #,##0", simbolos); 
    }

    private void writeFixedString(RandomAccessFile raf, String texto, int tam) throws IOException {
        StringBuffer buffer = new StringBuffer(texto != null ? texto : "");
        buffer.setLength(tam);
        raf.writeChars(buffer.toString());
    }

    private String readFixedString(RandomAccessFile raf, int tam) throws IOException {
        char[] caracteres = new char[tam];
        for (int i = 0; i < tam; i++) {
            caracteres[i] = raf.readChar();
        }
        return new String(caracteres).replace("\0", "").trim();
    }

    @Override
    public void insertar(ServicioBean serv) throws IOException {
        new File("datos").mkdirs();
        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "rw")) {
            raf.seek(raf.length());
            Vehiculo v = serv.getVehiculo();
            writeFixedString(raf, v.getPlaca(), Configuracion.TAM_PLACA);
            writeFixedString(raf, v.getMarca(), Configuracion.TAM_MARCA);
            raf.writeInt(v.getModelo());

            if (v instanceof Carro) {
                raf.writeChar('C');
                raf.writeInt(((Carro) v).getNumPuertas());
            } else {
                raf.writeChar('M');
                raf.writeInt(((Moto) v).getCilindraje());
            }

            
            writeFixedString(raf, serv.getNombreCliente(), Configuracion.TAM_NOMBRE);
            writeFixedString(raf, serv.getCelularCliente(), Configuracion.TAM_CELULAR);
            
            writeFixedString(raf, serv.getTipoLavado(), Configuracion.TAM_TIPO_LAVADO);
            raf.writeDouble(serv.getValor());
            raf.writeBoolean(serv.isPagado());
            writeFixedString(raf, serv.getFecha().toString(), Configuracion.TAM_FECHA);
        }
    }

    @Override
    public String listar() {
        DecimalFormat formatoMoneda = obtenerFormatoMoneda();
        StringBuilder sb = new StringBuilder();
        
        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "r")) {
            sb.append("=").append("=".repeat(168)).append("\n");
            
            sb.append(String.format("%-10s | %-20s | %-12s | %-15s | %-6s | %-15s | %-30s | %-12s | %-6s | %-10s\n",
                    "PLACA", "CLIENTE", "CELULAR", "MARCA", "MOD", "DETALLE", "TIPO LAVADO", "VALOR", "PAGO", "FECHA"));
            sb.append("-").append("-".repeat(168)).append("\n");

            while (raf.getFilePointer() < raf.length()) {
                String p = readFixedString(raf, Configuracion.TAM_PLACA);
                String m = readFixedString(raf, Configuracion.TAM_MARCA);
                int mod = raf.readInt();
                char tipoV = raf.readChar();
                int extra = raf.readInt();
                String nom = readFixedString(raf, Configuracion.TAM_NOMBRE);   
                String cel = readFixedString(raf, Configuracion.TAM_CELULAR);  
                String tl = readFixedString(raf, Configuracion.TAM_TIPO_LAVADO);
                double v = raf.readDouble();
                boolean pag = raf.readBoolean();
                String f = readFixedString(raf, Configuracion.TAM_FECHA);

                String det = (tipoV == 'C') ? "Carro (" + extra + "p)" : "Moto (" + extra + "cc)";
                sb.append(String.format("%-10s | %-20s | %-12s | %-15s | %-6d | %-15s | %-30s | %-12s | %-6s | %-10s\n",
                        p, nom, cel, m, mod, det, tl, formatoMoneda.format(v), (pag ? "SÍ" : "NO"), f));
            }
            sb.append("=").append("=".repeat(168)).append("\n");
        } catch (IOException e) {
            return ">> No hay registros guardados en el sistema.";
        }
        return sb.toString();
    }

    @Override
    public void actualizarPago(String placaBuscada) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "rw")) {
            boolean encontrado = false;
            while (raf.getFilePointer() < raf.length()) {
                long posInicio = raf.getFilePointer();
                String p = readFixedString(raf, Configuracion.TAM_PLACA);
                
                if (p.equalsIgnoreCase(placaBuscada)) {
                    raf.seek(posInicio + Configuracion.OFFSET_PAGO); 
                    raf.writeBoolean(true); 
                    encontrado = true;
                    break;
                } else {
                    raf.seek(posInicio + Configuracion.TAM_REGISTRO);
                }
            }
            if (!encontrado) throw new IOException("Placa no encontrada.");
        }
    }

    @Override
    public String buscar(String placaBuscada) throws IOException {
        DecimalFormat formatoMoneda = obtenerFormatoMoneda();
        StringBuilder sb = new StringBuilder();
        boolean encontrado = false;
        
        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String p = readFixedString(raf, Configuracion.TAM_PLACA);
                String m = readFixedString(raf, Configuracion.TAM_MARCA);
                int mod = raf.readInt();
                char tipoV = raf.readChar();
                int extra = raf.readInt();
                String nom = readFixedString(raf, Configuracion.TAM_NOMBRE);
                String cel = readFixedString(raf, Configuracion.TAM_CELULAR);
                String tl = readFixedString(raf, Configuracion.TAM_TIPO_LAVADO);
                double v = raf.readDouble();
                boolean pag = raf.readBoolean();
                String f = readFixedString(raf, Configuracion.TAM_FECHA);

                if (p.equalsIgnoreCase(placaBuscada)) {
                    sb.append(">>> REGISTRO ENCONTRADO <<<\n\n");
                    sb.append(String.format("%-10s | %-20s | %-12s | %-15s | %-6s | %-15s | %-30s | %-12s | %-6s | %-10s\n",
                            "PLACA", "CLIENTE", "CELULAR", "MARCA", "MOD", "DETALLE", "TIPO LAVADO", "VALOR", "PAGO", "FECHA"));
                    sb.append("-".repeat(175)).append("\n");
                    String det = (tipoV == 'C') ? "Carro (" + extra + "p)" : "Moto (" + extra + "cc)";
                    sb.append(String.format("%-10s | %-20s | %-12s | %-15s | %-6d | %-15s | %-30s | %-12s | %-6s | %-10s\n",
                            p, nom, cel, m, mod, det, tl, formatoMoneda.format(v), (pag ? "SÍ" : "NO"), f));
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                return ">> La placa [" + placaBuscada + "] no se encuentra en el sistema.";
            }
        } catch (IOException e) {
            return ">> Error al leer el archivo de registros.";
        }
        return sb.toString();
    }

    @Override
    public void borrar(String placaBuscada) throws IOException {
        File original = new File(Configuracion.RUTA_DAT);
        File temporal = new File("datos/temporal.dat");
        boolean encontrado = false;
        try (RandomAccessFile rafOri = new RandomAccessFile(original, "r");
             RandomAccessFile rafTmp = new RandomAccessFile(temporal, "rw")) {
            while (rafOri.getFilePointer() < rafOri.length()) {
                String p = readFixedString(rafOri, Configuracion.TAM_PLACA);
                String m = readFixedString(rafOri, Configuracion.TAM_MARCA);
                int mod = rafOri.readInt();
                char tipoV = rafOri.readChar();
                int extra = rafOri.readInt();
                String nom = readFixedString(rafOri, Configuracion.TAM_NOMBRE);
                String cel = readFixedString(rafOri, Configuracion.TAM_CELULAR);
                String tl = readFixedString(rafOri, Configuracion.TAM_TIPO_LAVADO);
                double v = rafOri.readDouble();
                boolean pag = rafOri.readBoolean();
                String f = readFixedString(rafOri, Configuracion.TAM_FECHA);

                if (!p.equalsIgnoreCase(placaBuscada)) {
                    writeFixedString(rafTmp, p, Configuracion.TAM_PLACA);
                    writeFixedString(rafTmp, m, Configuracion.TAM_MARCA);
                    rafTmp.writeInt(mod);
                    rafTmp.writeChar(tipoV);
                    rafTmp.writeInt(extra);
                    writeFixedString(rafTmp, nom, Configuracion.TAM_NOMBRE);
                    writeFixedString(rafTmp, cel, Configuracion.TAM_CELULAR);
                    writeFixedString(rafTmp, tl, Configuracion.TAM_TIPO_LAVADO);
                    rafTmp.writeDouble(v);
                    rafTmp.writeBoolean(pag);
                    writeFixedString(rafTmp, f, Configuracion.TAM_FECHA);
                } else encontrado = true;
            }
        }
        if (encontrado) { original.delete(); temporal.renameTo(original); }
        else { temporal.delete(); throw new IOException("Placa no encontrada."); }
    }

    @Override
    public void exportarSerializado() {
        ArrayList<ServicioBean> listaSerializar = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String p = readFixedString(raf, Configuracion.TAM_PLACA);
                String m = readFixedString(raf, Configuracion.TAM_MARCA);
                int mod = raf.readInt();
                char tipoV = raf.readChar();
                int extra = raf.readInt();
                String nom = readFixedString(raf, Configuracion.TAM_NOMBRE);
                String cel = readFixedString(raf, Configuracion.TAM_CELULAR);
                String tl = readFixedString(raf, Configuracion.TAM_TIPO_LAVADO);
                double v = raf.readDouble();
                boolean pag = raf.readBoolean();
                String f = readFixedString(raf, Configuracion.TAM_FECHA);

                Vehiculo vehiculo;
                if (tipoV == 'C') vehiculo = new Carro(p, m, mod, extra);
                else vehiculo = new Moto(p, m, mod, extra);
                
                java.time.LocalDate fecha = java.time.LocalDate.parse(f);
                listaSerializar.add(new ServicioBean(vehiculo, nom, cel, tl, v, pag, fecha));
            }
        } catch (Exception e) {
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos/servicios_exportados.ser"))) {
            oos.writeObject(listaSerializar);
        } catch (IOException e) {
            System.out.println("Error al serializar: " + e.getMessage());
        }
    }

    @Override
    public void generarReporteIngresos() throws IOException {
        DecimalFormat formatoMoneda = obtenerFormatoMoneda();
        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<Double> totalesDiarios = new ArrayList<>();
        ArrayList<Integer> cantPagados = new ArrayList<>();

        double totalHistorico = 0;

        try (RandomAccessFile raf = new RandomAccessFile(Configuracion.RUTA_DAT, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                readFixedString(raf, Configuracion.TAM_PLACA);
                readFixedString(raf, Configuracion.TAM_MARCA);
                raf.readInt(); raf.readChar(); raf.readInt();
                readFixedString(raf, Configuracion.TAM_NOMBRE);
                readFixedString(raf, Configuracion.TAM_CELULAR);
                readFixedString(raf, Configuracion.TAM_TIPO_LAVADO);
                double v = raf.readDouble();
                boolean pag = raf.readBoolean();
                String f = readFixedString(raf, Configuracion.TAM_FECHA);

                if (pag) {
                    totalHistorico += v;
                    int index = fechas.indexOf(f);
                    if (index == -1) {
                        fechas.add(f);
                        totalesDiarios.add(v);
                        cantPagados.add(1);
                    } else {
                        totalesDiarios.set(index, totalesDiarios.get(index) + v);
                        cantPagados.set(index, cantPagados.get(index) + 1);
                    }
                }
            }
        } catch (IOException e) {
            return;
        }

        new File("datos").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter("datos/reporte_ingresos.txt"))) {
            pw.println("==========================================================");
            pw.println("               LAVADERO LA PALMA - REPORTES               ");
            pw.println("==========================================================");
            pw.println("Fecha de Emisión: " + java.time.LocalDate.now());
            pw.println("----------------------------------------------------------");
            pw.println("               INGRESOS DESGLOSADOS POR DÍA               ");
            pw.println("----------------------------------------------------------");
            
            for (int i = 0; i < fechas.size(); i++) {
                pw.printf("Fecha: %-12s | Servicios: %-3d | Total: %s%n", 
                          fechas.get(i), cantPagados.get(i), formatoMoneda.format(totalesDiarios.get(i)));
            }
            
            pw.println("----------------------------------------------------------");
            pw.println("TOTAL RECAUDADO HISTÓRICO:                " + formatoMoneda.format(totalHistorico));
            pw.println("==========================================================");
        }
    }
}