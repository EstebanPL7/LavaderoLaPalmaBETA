package vista;

import javax.swing.*;
import java.awt.event.*;

public class VLogin extends JFrame {
    
    
	private static final long serialVersionUID = 1L;
	private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public VLogin() {
        setTitle("Acceso - Lavadero La Palma");
       
        setSize(320, 300); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false); 

        JPanel panel = new JPanel();
        panel.setLayout(null); 
       
        ImageIcon iconoOriginal = new ImageIcon("imagenes/lapalma.png");
             
        java.awt.Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
        ImageIcon iconoPerfecto = new ImageIcon(imagenEscalada);
        
        JLabel lblLogo = new JLabel(iconoPerfecto);
        lblLogo.setBounds(120, 10, 80, 80); 
        panel.add(lblLogo);
        

        
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(40, 110, 80, 25); 
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(130, 110, 130, 25); 
        panel.add(txtUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(40, 150, 100, 25); 
        panel.add(lblPass);

        txtPassword = new JPasswordField(); 
        txtPassword.setBounds(130, 150, 130, 25); 
        panel.add(txtPassword);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(100, 200, 110, 30); 
        panel.add(btnIngresar);

        
        AccionIngresar escucharClic = new AccionIngresar();
        btnIngresar.addActionListener(escucharClic);

        add(panel);
    }

    
    private class AccionIngresar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            
            if (usuario.equals("admin") && password.equals("12345")) {
                JOptionPane.showMessageDialog(null, "¡Bienvenido al sistema!");
                
                dispose(); 
                
                
                VPrincipal ventanaPrincipal = new VPrincipal();
                ventanaPrincipal.setVisible(true); 
                
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        VLogin ventanaAcceso = new VLogin();
        ventanaAcceso.setVisible(true); 
    }
}