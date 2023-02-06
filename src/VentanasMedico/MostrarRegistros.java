package VentanasMedico;

import entidades.Dieta;
import entidades.Dpt;
import entidades.Paciente;
import entidades.Toma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class MostrarRegistros extends JFrame {
    private JPanel contentPane;
    private JTable table1;
    private JComboBox nombres;
    private JButton ordenarButton;

    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE", "APELLLIDOS", "DIETA", "TOMA", "FECHA"};
    String[][] datos = {};
    String query = """
            SELECT `dieta_paciente_toma`.`codigo`, `paciente`.`nombre`, `paciente`.`apellidos`, `dieta`.`nombre`, `toma`.`nombre`, `dieta_paciente_toma`.`fecha`
            FROM `dieta_paciente_toma`\s
            \tLEFT JOIN `paciente` ON `dieta_paciente_toma`.`nhc_paciente` = `paciente`.`nhc`\s
            \tLEFT JOIN `dieta` ON `dieta_paciente_toma`.`codigo_dieta` = `dieta`.`codigo`\s
            \tLEFT JOIN `toma` ON `dieta_paciente_toma`.`codigo_toma` = `toma`.`codigo`
            """;

    String queryOrdenar = """
            SELECT `dieta_paciente_toma`.`codigo`, `paciente`.`nombre`, `paciente`.`apellidos`, `dieta`.`nombre`, `toma`.`nombre`, `dieta_paciente_toma`.`fecha`
                        FROM `dieta_paciente_toma`
                        LEFT JOIN `paciente` ON `dieta_paciente_toma`.`nhc_paciente` = `paciente`.`nhc`
                        LEFT JOIN `dieta` ON `dieta_paciente_toma`.`codigo_dieta` = `dieta`.`codigo`
                        LEFT JOIN `toma` ON `dieta_paciente_toma`.`codigo_toma` = `toma`.`codigo`
                        WHERE paciente.nombre=?
                        """;

    public MostrarRegistros(Connection conn) throws SQLException {
        myModel = new DefaultTableModel(datos, titulos);
        table1.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Consulta Registros");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        listaNombre(conn, nombres);

//        ordenarButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    ordenarNombre(queryOrdenar, conn);
//                } catch (SQLException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
    }

    public void ordenarNombre(String query, Connection conn) throws SQLException {
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, (String) nombres.getSelectedItem());
        preparedStmt.execute();
        updateTable(this.query, conn);
    }

    public void listaNombre(Connection conn, JComboBox nombres) throws SQLException {
        String sql = "SELECT nombre FROM paciente";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        nombres.addItem("Seleccione");

        while (rs.next()) {
            nombres.addItem(rs.getString("nombre"));
        }
    }

    private void updateTable(String query, Connection conn) {      //preguntar - ACtualizar la tabla
        try {
            emptyTable(); // llamamos al metodo empty tabla para limpiar la tabla
            PreparedStatement preparedStmt = conn.prepareStatement(query);
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
            System.out.println("");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void emptyTable() {                  // limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = table1.getRowCount(); //validamos el numero de filas de la tabla
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }
}
