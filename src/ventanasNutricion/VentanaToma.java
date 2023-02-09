package ventanasNutricion;

import entidades.Toma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaToma extends JFrame{
    //Lista de atributos que corresponden los companentes del formulario
    private JPanel contentPane;
    private JButton borrarTomaButton;
    private JButton modificarTomaButton;
    private JButton consultarTomaButton;
    private JButton addTomaButton;
    private JTextField codigoText;
    private JTextField nombreText;
    private JTable tablaTomas;

    //Datos para construir la tabla en la que se muestran los datos
    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE"};
    String[][] datos = {};
    String query = "SELECT * FROM toma";

    public VentanaToma(Connection conn){
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        tablaTomas.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Consulta Tomas");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);


        addTomaButton.addActionListener(e -> {
            try {
                insertarToma(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Datos mal introducidos");
            }
        });
        borrarTomaButton.addActionListener(e -> {
            try {
                borrarToma(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        modificarTomaButton.addActionListener(e -> {
            try {
                modificarToma(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Datos mal introducidos");
            }
        });
        consultarTomaButton.addActionListener(e -> {
            try {
                consultarToma(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void updateTable(String query, Connection conn) {
        //Método para mostrar y actualizar la tabla
        try {
            emptyTable(); // Llamamos al metodo empty tabla para limpiar la tabla
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                Toma toma = new Toma();
                toma.setCodigo(rs.getInt(1));
                toma.setNombre(rs.getString(2));

                Object[] row = {toma.getCodigo(), toma.getNombre()};
                myModel.addRow(row);
            }
            preparedStmt.close();
        } catch (SQLException ex) {
            System.out.println();
        }
    }
    private void emptyTable() {
        // Método que limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaTomas.getRowCount();
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }

    private void insertarToma(Connection conn) throws SQLException {
        //Método para insertar una toma en la BD, se deja el código en null porque es autoincremental
        String query = "INSERT INTO toma (codigo, nombre) VALUES (NULL,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,nombreText.getText());
        preparedStatement.execute();
        JOptionPane.showMessageDialog(null,"Toma introducida");
        updateTable(this.query, conn);
    }
    private void borrarToma(Connection conn) throws SQLException {
        //Método para borrar la toma de la BD
        String query = "DELETE FROM toma WHERE codigo = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,codigoText.getText());

        //Se pregunta primero si se quiere borrar
        int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere borrar esta dieta?");
        if (opcion == JOptionPane.YES_OPTION){
            //Si selecciona sí, se borra y se limpian los campos de texto
            preparedStatement.execute();
            JOptionPane.showMessageDialog(null, "Borrado correctamente");
            codigoText.setText("");
            nombreText.setText("");
        }
        updateTable(this.query, conn);
    }
    private void modificarToma(Connection conn) throws SQLException {
        //Método para modificar la toma de la BD
        String query = "UPDATE toma SET nombre=? WHERE codigo =?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,nombreText.getText());
        preparedStatement.setString(2,codigoText.getText());

        preparedStatement.execute();
        JOptionPane.showMessageDialog(null,"Toma editada");
        updateTable(this.query, conn);

    }

    private void consultarToma(Connection conn) throws SQLException {
        //Método para consultar los datos de la toma con el código que se ha escrito en el campo correspondiente
        String query = "SELECT * FROM toma WHERE codigo = ?";
        PreparedStatement preparedStatement  = conn.prepareStatement(query);
        preparedStatement.setInt(1,Integer.parseInt(codigoText.getText()));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            codigoText.setText(rs.getString("codigo"));
            nombreText.setText(rs.getString("nombre"));
        }
    }
}
