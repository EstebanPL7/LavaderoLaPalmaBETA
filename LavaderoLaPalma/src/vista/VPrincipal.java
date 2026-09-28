package vista;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import bean.*;
import logica.*;
import excepciones.PlacaInvalidaException;

public class VPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private ICrud logica = new ProcesarArchivo();

    public VPrincipal() {
        setTitle("Sistema de Gestión - Lavadero La Palma");
        setSize(350, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null); 

        JLabel lblTitulo = new JLabel("--- MENÚ DE SERVICIOS ---");
        lblTitulo.setBounds(90, 20, 200, 25);
        panel.add(lblTitulo);

        JButton btnInsertar = new JButton("1. Insertar Vehículo");
        btnInsertar.setBounds(70, 60, 200, 35);
        panel.add(btnInsertar);

        JButton btnListar = new JButton("2. Listar Vehículos");
        btnListar.setBounds(70, 110, 200, 35);
        panel.add(btnListar);

        JButton btnEliminar = new JButton("3. Eliminar Registro");
        btnEliminar.setBounds(70, 160, 200, 35);
        panel.add(btnEliminar);

        JButton btnPago = new JButton("4. Registrar Pago");
        btnPago.setBounds(70, 210, 200, 35);
        panel.add(btnPago);

        JButton btnBuscar = new JButton("5. Buscar por Placa");
        btnBuscar.setBounds(70, 260, 200, 35);
        panel.add(btnBuscar);

        JButton btnSerializar = new JButton("6. Exportar (Serializar)");
        btnSerializar.setBounds(70, 310, 200, 35);
        panel.add(btnSerializar);

        JButton btnReporte = new JButton("7. Generar Reporte (.txt)");
        btnReporte.setBounds(70, 360, 200, 35);
        panel.add(btnReporte);

        btnInsertar.addActionListener(new AccionInsertar());
        btnListar.addActionListener(new AccionOperaciones(2));
        btnEliminar.addActionListener(new AccionOperaciones(3));
        btnPago.addActionListener(new AccionOperaciones(4));
        btnBuscar.addActionListener(new AccionOperaciones(5));
        btnSerializar.addActionListener(new AccionOperaciones(6));
        btnReporte.addActionListener(new AccionOperaciones(7));

        add(panel);
    }

    private void validarPlaca(String placa) throws PlacaInvalidaException {
        if (placa == null || placa.length() != 6) {
            throw new PlacaInvalidaException("La placa debe tener exactamente 6 caracteres (Ej: ABC123).");
        }
        for (int i = 0; i < 3; i++) {
            if (!Character.isLetter(placa.charAt(i))) {
                throw new PlacaInvalidaException("Los primeros 3 caracteres deben ser letras.");
            }
        }
    }

    
 
    private void mostrarVentanaTexto(String texto, String tituloVentana) {
        JTextArea areaTexto = new JTextArea(15, 110);
        areaTexto.setText(texto);
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Courier New", Font.PLAIN, 12)); 
        
        JScrollPane scroll = new JScrollPane(areaTexto);
        JOptionPane.showMessageDialog(null, scroll, tituloVentana, JOptionPane.PLAIN_MESSAGE);
    }

    private class AccionInsertar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Carro", "Moto"});
            JTextField txtPlaca = new JTextField();
            JTextField txtMarca = new JTextField();
            JTextField txtModelo = new JTextField();
            JTextField txtExtra = new JTextField();
            JTextField txtNombre = new JTextField(); 
            JTextField txtCelular = new JTextField(); 
            JTextField txtLavado = new JTextField();
            JTextField txtValor = new JTextField();
            JCheckBox chkPagado = new JCheckBox("¿El servicio ya fue pagado?");

            Object[] formulario = {
                "Tipo de Vehículo:", comboTipo,
                "Placa (6 caracteres):", txtPlaca,
                "Nombre del Cliente:", txtNombre,  
                "Teléfono Celular:", txtCelular,    
                "Marca Vehículo:", txtMarca,
                "Modelo (Año):", txtModelo,
                "Puertas (Carro) / Cilindraje (Moto):", txtExtra,
                "Tipo de Lavado:", txtLavado,
                "Precio ($):", txtValor,
                chkPagado
            };

            int resultado = JOptionPane.showConfirmDialog(null, formulario, "Nuevo Registro", JOptionPane.OK_CANCEL_OPTION);
            
            if (resultado == JOptionPane.OK_OPTION) {
                try {
                    String p = txtPlaca.getText().toUpperCase();
                    validarPlaca(p); 

                    int tipoV = comboTipo.getSelectedIndex() + 1;
                    String nom = txtNombre.getText(); 
                    String cel = txtCelular.getText(); 
                    String m = txtMarca.getText();
                    int mod = Integer.parseInt(txtModelo.getText());
                    int extra = Integer.parseInt(txtExtra.getText());
                    String tl = txtLavado.getText();
                    double v = Double.parseDouble(txtValor.getText());
                    boolean pag = chkPagado.isSelected();

                    Vehiculo vehiculo;
                    if (tipoV == 1) vehiculo = new Carro(p, m, mod, extra);
                    else vehiculo = new Moto(p, m, mod, extra);

                    
                    logica.insertar(new ServicioBean(vehiculo, nom, cel, tl, v, pag, LocalDate.now()));
                    JOptionPane.showMessageDialog(null, "¡Vehículo guardado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (PlacaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Placa Inválida", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Verifique los campos numéricos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar en el archivo.", "Error I/O", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class AccionOperaciones implements ActionListener {
        private int opcion;
        
        public AccionOperaciones(int opcion) {
            this.opcion = opcion;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String placa;
                switch (opcion) {
                    case 2: 
                        String listado = logica.listar();
                        mostrarVentanaTexto(listado, "Listado de Vehículos en Sistema");
                        break;
                    case 3:
                        placa = JOptionPane.showInputDialog("Ingrese la placa a eliminar:");
                        if (placa != null && !placa.trim().isEmpty()) {
                            logica.borrar(placa.toUpperCase());
                            JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case 4:
                        placa = JOptionPane.showInputDialog("Ingrese la placa para registrar el pago:");
                        if (placa != null && !placa.trim().isEmpty()) {
                            logica.actualizarPago(placa.toUpperCase());
                            JOptionPane.showMessageDialog(null, "Pago asentado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case 5: 
                        placa = JOptionPane.showInputDialog("Ingrese la placa a buscar:");
                        if (placa != null && !placa.trim().isEmpty()) {
                            String resultadoBusqueda = logica.buscar(placa.toUpperCase());
                            mostrarVentanaTexto(resultadoBusqueda, "Resultado de Búsqueda");
                        }
                        break;
                    case 6:
                        logica.exportarSerializado();
                        JOptionPane.showMessageDialog(null, "¡Serialización exitosa!\nArchivo generado: datos/servicios_exportados.ser", "Serialización", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 7:
                        logica.generarReporteIngresos();
                        JOptionPane.showMessageDialog(null, "¡Reporte generado con éxito!\nRevisa el archivo: datos/reporte_ingresos.txt", "Reporte Secuencial", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error de Operación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}