import ventanasCocina.Cocina;
import ventanasMedico.Medico;
import ventanasNutricion.Nutricion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Registro extends JFrame{
    //Lista de atributos que corresponden los companentes del formulario
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
                    //Primero se compueba que el usuario y la contraseña existan
                    if (existeUsuario(conn) != 0) {
                        if (existePassword(conn) != 0) {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido/a!");
                            setVisible(false);
                            //En caso de que exista se manda al formulario correspondiente al deparatamento que ha introducido
                            switch (Objects.requireNonNull(departamentos.getSelectedItem()).toString()){
                                case "Médico" -> new Medico(conn);
                                case "Nutricion" -> new Nutricion();
                                case "Cocina" -> new Cocina(conn);
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
        //Método para que en el combobox aparezacan el nombre los departamentos que están guardados en la base de datos
        String sql= "SELECT nombre FROM departamento";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        departamentos.addItem("Seleccione");

        while (rs.next()){
            departamentos.addItem(rs.getString("nombre"));
        }
    }
    private int existeUsuario(Connection conn) throws SQLException {
        //Método para comprobar que el usuario existe
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
