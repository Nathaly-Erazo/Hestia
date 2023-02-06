package ventanasMedico;

import entidades.Toma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MostrarTomas extends JFrame{
    private JPanel contentPane;
    private JTable tablaTomas;

    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE"};
    String[][] datos = {};
    String query = "SELECT * FROM toma";

    public MostrarTomas(Connection conn){
        myModel = new DefaultTableModel(datos, titulos);
        tablaTomas.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Consulta Tomas");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void updateTable(String query, Connection conn) {      //preguntar - ACtualizar la tabla
        try {
            emptyTable(); // llamamos al metodo empty tabla para limpiar la tabla
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
            System.out.println("");
        }
    }
    private void emptyTable() {                  // limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaTomas.getRowCount(); //validamos el numero de filas de la tabla
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }
}
