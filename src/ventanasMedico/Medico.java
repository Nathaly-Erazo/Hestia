package ventanasMedico;

import entidades.Paciente;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class Medico extends JFrame {
    //Atributos que corresponden a los elementos del formulario
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

    //Datos para construir la tabla en la que se muestran los datos
    DefaultTableModel myModel;
    String[] titulos = {"NHC", "NOMBRE", "APELLLIDOS", "FECHA NACIMIENTO", "OBSERVACIONES", "HABITACIÓN",};
    String[][] datos = {};
    String query = "SELECT * FROM paciente";

    public Medico(Connection conn) throws SQLException {
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        tablaPacientes.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Departamento Médico");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Acciones de los botones de añadir, borrar, modificar y consultar que llevan a los métodos correspondietnes
        addPacienteButton.addActionListener(e -> {
            try {
                insertarPaciente(conn);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Datos mal introducidos");
            }
        });
        borrarPacienteButton.addActionListener(e -> {
            try {
                borrarPaciente(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        modificarPacienteButton.addActionListener(e -> {
            try {
                modificarPaciente(conn);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Datos mal introducidos");
            }
        });
        consultarPacienteButton.addActionListener(e -> {
            try {
                consultarPaciente(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Acción de los botones que abre otra ventan en la que se muestras los datos correspondientes
        contulaRegistros.addActionListener(e -> {
            try {
                new MostrarRegistros(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        consultaTomas.addActionListener(e -> new MostrarTomas(conn));
        consultaDietas.addActionListener(e -> new MostrarDietas(conn));

        //Con los siguientes KeyListener se controla lo que introduce el usuario
        nhcText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 5 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros))
                    e.consume();
                if(nhcText.getText().trim().length()>=5)
                    e.consume();
            }
        });

        habitacionText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 3 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros)) {
                    e.consume();
                }
                if(habitacionText.getText().trim().length()>=3) {
                    e.consume();
                }
            }
        });
        fechaNacimientoText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números, guiones y tenga una longitud máxima de 10 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                boolean guion = key == 45;
                if (!(numeros || guion))
                    e.consume();
                if(fechaNacimientoText.getText().trim().length()>=10)
                    e.consume();

            }
        });
        nombreText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //No deja que introuzcan números
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if ((numeros)) {
                    e.consume();
                }
            }
        });

        apellidosText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //No deja que introuzcan números
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if ((numeros)) {
                    e.consume();
                }
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

    private void insertarPaciente(Connection conn) throws SQLException {
        //Método para insertar un paciente en la BD
        String query = "INSERT INTO paciente (nhc, nombre, apellidos, fecha_nacimiento, observaciones, habitacion) VALUES (?,?,?,?,?,?)";
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

    private void borrarPaciente(Connection conn) throws SQLException {
        //Método para borrar el paciente de la BD
        String query = "DELETE FROM paciente WHERE nhc = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, nhcText.getText());

        //Se pregunta primero si se quiere borrar
        int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere borrar este paciente?");
        if (opcion == JOptionPane.YES_OPTION) {
            //Si selecciona sí, se borra y se limpian los campos de texto
            preparedStmt.execute();
            JOptionPane.showMessageDialog(null, "Borrado correctamente");
            //Se vacían los campos
            nhcText.setText("");
            nombreText.setText("");
            apellidosText.setText("");
            fechaNacimientoText.setText("");
            observacionesText.setText("");
            habitacionText.setText("");
        }
        updateTable(this.query, conn);
    }

    private void modificarPaciente(Connection conn) throws SQLException {
        //Método para modificar el paciente de la BD
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

    private void consultarPaciente(Connection conn) throws SQLException {
        //Método para consultar los datos del paciente con el nhc que se ha escrito en el campo correspondiente
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

