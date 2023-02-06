package ventanasMedico;

import entidades.Paciente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class Medico extends JFrame {
    private JPanel contentPane;
    private JButton addPacienteButton;
    private JButton consultarPacienteButton;
    private JButton borrarPacienteButton;
    private JButton modificarPacienteButton;
    private JTextField nhcText;
    private JTextField habitacionText;
    private JTextField observacionesText;
    private JTextField nombreText;
    private JTextField apellidosText;
    private JTextField fechaNacimientoText;
    private JTable tablaPacientes;
    private JButton contulaRegistros;
    private JButton consultaTomas;
    private JButton consultaDietas;

    DefaultTableModel myModel;
    String[] titulos = {"NHC", "NOMBRE", "APELLLIDOS", "FECHA NACIMIENTO", "OBSERVACIONES", "HABITACIÓN",};
    String[][] datos = {};
    String query = "SELECT * FROM paciente";

    public Medico(Connection conn) throws SQLException {
        myModel = new DefaultTableModel(datos, titulos);
        tablaPacientes.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Departamento médico");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        addPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    insertarPaciente(conn);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        borrarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    borrarPaciente(conn);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        modificarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modificarPaciente(conn);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Datos mal introducidos");
                }
            }
        });
        consultarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarPaciente(conn);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contulaRegistros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MostrarRegistros(conn);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        consultaTomas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MostrarTomas(conn);
            }
        });
        consultaDietas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MostrarDietas(conn);
            }
        });
    }

    private void updateTable(String query, Connection conn) {      //preguntar - ACtualizar la tabla
        try {
            emptyTable(); // llamamos al metodo empty tabla para limpiar la tabla
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
            System.out.println("");
        }
    }
    private void emptyTable() {                  // limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaPacientes.getRowCount(); //validamos el numero de filas de la tabla
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }

    public void insertarPaciente(Connection conn) throws SQLException {
        String query = "INSERT INTO paciente (nhc, nombre, apellidos,, fecha_nacimiento, observaciones, habitacion) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, nhcText.getText());
        preparedStmt.setString(2, nombreText.getText());
        preparedStmt.setString(3, apellidosText.getText());
        preparedStmt.setString(4, fechaNacimientoText.getText());
        preparedStmt.setString(5, observacionesText.getText());
        preparedStmt.setString(6, habitacionText.getText());
        preparedStmt.execute();
        JOptionPane.showMessageDialog(null, "Paciente introducido");
        updateTable(this.query, conn);
    }

    public void borrarPaciente(Connection conn) throws SQLException {
        String query = "DELETE FROM paciente WHERE nhc = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, nhcText.getText());
        int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere borrar este paciente?");
        if (opcion == JOptionPane.YES_OPTION) {
            preparedStmt.execute();
            JOptionPane.showMessageDialog(null, "Borrado correctamente");
            nhcText.setText("");
            nombreText.setText("");
            apellidosText.setText("");
            fechaNacimientoText.setText("");
            observacionesText.setText("");
            habitacionText.setText("");
        }
        updateTable(this.query, conn);
    }

    public void modificarPaciente(Connection conn) throws SQLException {

        String query = "UPDATE paciente SET nombre=?, apellidos=?, fecha_nacimiento=?, observaciones=?, habitacion=? WHERE nhc=?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, nombreText.getText());
        preparedStmt.setString(2, apellidosText.getText());
        preparedStmt.setString(3, fechaNacimientoText.getText());
        preparedStmt.setString(4, observacionesText.getText());
        preparedStmt.setString(5, habitacionText.getText());
        preparedStmt.setString(6, nhcText.getText());
        preparedStmt.execute();
        JOptionPane.showMessageDialog(null, "Paciente editado");
        updateTable(this.query, conn);
    }

    public void consultarPaciente(Connection conn) throws SQLException {
        String query = "SELECT * FROM paciente WHERE nhc = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(nhcText.getText()));
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            nhcText.setText(rs.getString("nhc"));
            nombreText.setText(rs.getString("nombre"));
            apellidosText.setText(rs.getString("apellidos"));
            fechaNacimientoText.setText(rs.getString("fecha_nacimiento"));
            observacionesText.setText(rs.getString("observaciones"));
            habitacionText.setText(rs.getString("habitacion"));
        }
    }

}

