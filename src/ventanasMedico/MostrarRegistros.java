package ventanasMedico;

import entidades.Dieta;
import entidades.Dpt;
import entidades.Paciente;
import entidades.Toma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class MostrarRegistros extends JFrame {
    //Lista de atributos que corresponden los companentes del formulario
    private JPanel contentPane;
    private JTable table1;
    private JComboBox<String> nombres;
    private JButton ordenarButton;

    //Datos para construir la tabla en la que se muestran los datos
    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE", "APELLLIDOS", "DIETA", "TOMA", "FECHA"};
    String[][] datos = {};
    String query = """
            SELECT `dieta_paciente_toma`.`codigo`, `paciente`.`nombre`, `paciente`.`apellidos`, `dieta`.`nombre`, `toma`.`nombre`, `dieta_paciente_toma`.`fecha`
            FROM `dieta_paciente_toma`\s
            \tLEFT JOIN `paciente` ON `dieta_paciente_toma`.`nhc_paciente` = `paciente`.`nhc`\s
            \tLEFT JOIN `dieta` ON `dieta_paciente_toma`.`codigo_dieta` = `dieta`.`codigo`\s
            \tLEFT JOIN `toma` ON `dieta_paciente_toma`.`codigo_toma` = `toma`.`codigo`
            WHERE paciente.nombre=?
            """;

    public MostrarRegistros(Connection conn) throws SQLException {
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        table1.setModel(myModel);

        setContentPane(contentPane);
        setTitle("Consulta Registros");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        listaNombre(conn, nombres);

        /*Una vez se haya selecionado un nombre del comboBox,
        Se actualiza la tabla con los registros correspondientes a ese nombre*/
        ordenarButton.addActionListener(e -> updateTable(query, conn));
    }

    private void listaNombre(Connection conn, JComboBox<String> nombres) throws SQLException {
        //Método para que en el combobox aparezacan los nombres de los pacientes que están guardados en la base de datos
        String sql = "SELECT nombre FROM paciente";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        nombres.addItem("Seleccione");

        while (rs.next()) {
            nombres.addItem(rs.getString("nombre"));
        }
    }

    private void updateTable(String query, Connection conn) {
        //Método para mostrar y actualizar la tabla
        try {
            emptyTable(); // llamamos al metodo empty tabla para limpiar la tabla
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, (String) nombres.getSelectedItem());
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                Dpt registros = new Dpt();
                registros.setCodigo(rs.getInt(1));

                Paciente paciente = new Paciente();
                paciente.setNombre(rs.getString(2));
                paciente.setApellidos(rs.getString(3));

                Dieta dieta = new Dieta();
                dieta.setNombre(rs.getString(4));

                Toma toma = new Toma();
                toma.setNombre(rs.getString(5));

                registros.setFecha(rs.getString(6));

                Object[] row = {registros.getCodigo(), paciente.getNombre(), paciente.getApellidos(),
                        dieta.getNombre(), toma.getNombre(), registros.getFecha()};
                myModel.addRow(row);
            }
            preparedStmt.close();
        } catch (SQLException ex) {
            System.out.println();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void emptyTable() {
        // Método que limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = table1.getRowCount();
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }
}
