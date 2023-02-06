package ventanasMedico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registro extends JFrame{
    private JPanel contentPane;
    private JComboBox departamentos;
    private JPasswordField passwordField1;
    private JButton entrarButton;

    public Registro(Connection conn) throws SQLException {
        setContentPane(contentPane);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Registro");
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listaDepartamentos(conn, departamentos);

        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (existeUsuario(conn) != 0) {
                        if (existePassword(conn) != 0) {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido/a!");
                            setVisible(false);
                            if(departamentos.getSelectedItem()==departamentos.getSelectedItem()){
                                new Medico(conn);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Contraseña incorrecta");
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void listaDepartamentos( Connection conn,JComboBox departamentos) throws SQLException {
        String sql= "SELECT nombre FROM departamento";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        departamentos.addItem("Seleccione");

        while (rs.next()){
            departamentos.addItem(rs.getString("nombre"));
        }
    }
    private int existeUsuario(Connection conn) throws SQLException {
        int resultado = 0;
        PreparedStatement pstmt = conn.prepareStatement("SELECT EXISTS (SELECT * FROM departamento WHERE nombre=?) AS resultado");
        pstmt.setString(1, (String) departamentos.getSelectedItem());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }

    private int existePassword(Connection conn) throws SQLException {
        int resultado = 0;
        PreparedStatement pstmt = conn.prepareStatement("SELECT EXISTS (SELECT * FROM departamento WHERE password=?) AS resultado");
        pstmt.setString(1, String.valueOf(passwordField1.getPassword()));
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }
}
