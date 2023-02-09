package ventanasNutricion;

import entidades.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MostrarPacientes extends JFrame{
    //Atributos que corresponden a los elementos del formulario
    private JPanel contentPane;
    private JTable tablaPacientes;

    //Datos para construir la tabla en la que se muestran los datos
    DefaultTableModel myModel;
    String[] titulos = {"NHC", "NOMBRE", "APELLLIDOS", "FECHA NACIMIENTO", "OBSERVACIONES", "HABITACIÓN",};
    String[][] datos = {};
    String query = "SELECT * FROM paciente";

    public MostrarPacientes(Connection conn){
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        tablaPacientes.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Consulta Pacientes");
        setSize(800, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTable(String query, Connection conn) {
        //Método para mostrar y actualizar la tabla
        try {
            emptyTable(); // Llamamos al metodo empty tabla para limpiar la tabla
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setNhc(rs.getInt(1));
                paciente.setNombre(rs.getString(2));
                paciente.setApellidos(rs.getString(3));
                paciente.setFecha_nacimiento(rs.getString(4));
                paciente.setObservaciones(rs.getString(5));
                paciente.setHabitacion(rs.getInt(6));
                Object[] row = {paciente.getNhc(), paciente.getNombre(), paciente.getApellidos(),
                        paciente.getFecha_nacimiento(), paciente.getObservaciones(), paciente.getHabitacion()};
                myModel.addRow(row);
            }
            preparedStmt.close();
        } catch (SQLException ex) {
            System.out.println();
        }
    }
    private void emptyTable() {
        // Método limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaPacientes.getRowCount();
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }
}
