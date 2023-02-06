package ventanasMedico;

import entidades.Dieta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MostrarDietas extends JFrame{
    private JPanel contentPane;
    private JTable tablaTomas;

    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE","DESAYUNO","COMIDA","MERIENDA","CENA"};
    String[][] datos = {};
    String query = "SELECT * FROM dieta";

    public MostrarDietas(Connection conn){
        myModel = new DefaultTableModel(datos, titulos);
        tablaTomas.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Consulta Dietas");
        setSize(800, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTable(String query, Connection conn) {      //preguntar - ACtualizar la tabla
        try {
            emptyTable(); // llamamos al metodo empty tabla para limpiar la tabla
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                Dieta dieta = new Dieta();

                dieta.setCodigo(rs.getInt(1));
                dieta.setNombre(rs.getString(2));
                dieta.setDesayuno(rs.getString(3));
                dieta.setComida(rs.getString(4));
                dieta.setMerienda(rs.getString(5));
                dieta.setCena(rs.getString(6));
                Object[] row = {dieta.getCodigo(), dieta.getNombre(), dieta.getDesayuno(),
                dieta.getComida(),dieta.getMerienda(),dieta.getCena()};
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
