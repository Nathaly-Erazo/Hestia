import ventanasCocina.Cocina;
import ventanasMedico.Medico;
import ventanasNutricion.Nutricion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Registro extends JFrame {
    //Lista de atributos que corresponden los companentes del formulario
    private JPanel contentPane;
    private JComboBox<String> departamentos;
    private JPasswordField passwordField1;
    private JButton entrarButton;

    public Registro(Connection conn) throws SQLException {

        setContentPane(contentPane);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Inicio de sesión");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listaDepartamentos(conn, departamentos);

        entrarButton.addActionListener(e -> {
            try {
                //Primero se compueba que la contraseña exista
                if (existePassword(conn) != 0) {
                    //En caso de que exista se manda al formulario correspondiente al deparatamento que ha introducido

                    switch (coincidePassword(conn)) {
                        case "medico1" -> {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido/a al Menú Médico!");
                            new Medico(conn);
                        }
                        case "nutricion1" -> {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido/a al Menú Nutrición!");
                            new Nutricion(conn);
                        }
                        case "cocina1" -> {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido/a al Menú Cocina!");
                            new Cocina(conn);
                        }
                    }
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
            } catch (NullPointerException ex){
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void listaDepartamentos(Connection conn, JComboBox<String> departamentos) throws SQLException {
        //Método para que en el combobox aparezacan el nombre los departamentos que están guardados en la base de datos
        String sql = "SELECT nombre FROM departamento";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        departamentos.addItem("Seleccione");

        while (rs.next()) {
            departamentos.addItem(rs.getString("nombre"));
        }
    }

    private String coincidePassword(Connection conn) throws SQLException {
        //Método para comprobar que la contraseña introducida corresponde con el departamento seleccionado
        String resultado = null;
        String query = "SELECT password FROM departamento WHERE nombre =?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, (String) departamentos.getSelectedItem());
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
           if(String.valueOf(passwordField1.getPassword()).equals(rs.getString("password")) )
            resultado = rs.getString("password");
        }
        return resultado;
    }

    private int existePassword(Connection conn) throws SQLException {
        //Método para comprobar que la contraseña existe
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
