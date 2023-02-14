package ventanasNutricion;

import entidades.Dieta;
import entidades.Dpt;
import entidades.Paciente;
import entidades.Toma;
import ventanasMedico.MostrarDietas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class Nutricion extends JFrame {
    //Atributos que corresponden a los elementos del formulario
    private JPanel contentPane;
    private JButton menuTomasButton;
    private JButton borrarRegistroButton;
    private JButton modificarRegistroButton;
    private JButton consultarRegistroButton;
    private JButton dietasButton;
    private JButton pacientesButton;
    private JTextField codigoText;
    private JTextField pacienteText;
    private JTextField dietaText;
    private JTextField tomaText;
    private JTable tablaRegistros;
    private JComboBox<String> dietasCbx;
    private JComboBox<String> nhcCbx;
    private JComboBox<String> tomasCbx;
    private JButton addRegistroButton;
    private JTextField fechaRegistroText;
    private JComboBox<String> nombresCbx;
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

    public Nutricion(Connection conn) throws SQLException {
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        tablaRegistros.setModel(myModel);
        updateTable(query, conn);
        listaNch(conn, nhcCbx);
        listaDietas(conn,dietasCbx);
        listaTomas(conn,tomasCbx);
        listaNombres(conn,nombresCbx);

        setContentPane(contentPane);
        setTitle("Departamento Nutrición");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ordenarButton.addActionListener(e -> updateTable(query, conn));

        //Acciones de los botones de añadir, borrar, modificar y consultar que llevan a los métodos correspondietnes
        addRegistroButton.addActionListener(e -> {
            try {
                insertarRegistro(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Datos mal introducidos");
            }
        });
        borrarRegistroButton.addActionListener(e -> {
            try {
                borrarRegistro(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        modificarRegistroButton.addActionListener(e -> {
            try {
                modificarRegistro(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Datos mal introducidos");
            }
        });
        consultarRegistroButton.addActionListener(e -> {
            try {
                consultarRegistro(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Acción de los botones que abre otra ventan en la que se muestras los datos correspondientes
        dietasButton.addActionListener(e -> new MostrarDietas(conn));
        pacientesButton.addActionListener(e -> new MostrarPacientes(conn));
        menuTomasButton.addActionListener(e -> new VentanaToma(conn));

        //Con los siguientes KeyListener se controla lo que introduce el usuario
        fechaRegistroText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números, guiones y tenga una longitud máxima de 10 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                boolean guion = key == 45;
                if (!(numeros || guion))
                    e.consume();
                if(fechaRegistroText.getText().trim().length()>=10)
                    e.consume();
            }
        });
        codigoText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 2 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros))
                    e.consume();
                if(codigoText.getText().trim().length()>=2)
                    e.consume();
            }

        });

        pacienteText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 5 dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros))
                    e.consume();
                if(pacienteText.getText().trim().length()>=5)
                    e.consume();
            }
        });
        dietaText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 1 dígito
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros)) {
                    e.consume();
                }
                if(dietaText.getText().trim().length()>=1) {
                    e.consume();
                }
            }
        });
        tomaText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de 1 dígito
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros)) {
                    e.consume();
                }
                if(tomaText.getText().trim().length()>=1) {
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
            preparedStmt.setString(1, (String) nombresCbx.getSelectedItem());
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
        // Método limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaRegistros.getRowCount();
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }

    private void listaNch(Connection conn, JComboBox<String> nch) throws SQLException {
        //Método para que en el combobox aparezacan los nombres de los pacientes que están guardados en la base de datos
        String sql = "SELECT nhc FROM paciente";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        nch.addItem("Seleccione");

        while (rs.next()) {
            nch.addItem(rs.getString("nhc"));
        }
    }
    private void listaDietas(Connection conn, JComboBox<String> dietas) throws SQLException {
        //Método para que en el combobox aparezacan los nombres de las dietas que están guardadas en la base de datos
        String sql = "SELECT codigo FROM dieta";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        dietas.addItem("Seleccione");

        while (rs.next()) {
            dietas.addItem(rs.getString("codigo"));
        }
    }
    private void listaTomas(Connection conn, JComboBox<String> tomas) throws SQLException {
        //Método para que en el combobox aparezacan los nombres de las tomas que están guardadas en la base de datos
        String sql = "SELECT codigo FROM toma";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        tomas.addItem("Seleccione");

        while (rs.next()) {
            tomas.addItem(rs.getString("codigo"));
        }
    }
    private void listaNombres(Connection conn, JComboBox<String> nombres) throws SQLException {
        //Método para que en el combobox aparezacan los nombres de los pacientes que están guardados en la base de datos
        String sql = "SELECT nombre FROM paciente";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        nombres.addItem("Seleccione");

        while (rs.next()) {
            nombres.addItem(rs.getString("nombre"));
        }
    }

    private void insertarRegistro(Connection conn) throws SQLException {
        //Método para insertar un registro en la BD, el código es null porque es autoincremental
        String query = "INSERT INTO dieta_paciente_toma (codigo, nhc_paciente, codigo_dieta, codigo_toma, fecha) VALUES (NULL,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, (String) nhcCbx.getSelectedItem());
        preparedStatement.setString(2, (String) dietasCbx.getSelectedItem());
        preparedStatement.setString(3, (String) tomasCbx.getSelectedItem());
        preparedStatement.setString(4,fechaRegistroText.getText());
        preparedStatement.execute();
        JOptionPane.showMessageDialog(null, "Registro introducido");
        updateTable(this.query, conn);
        nhcCbx.setSelectedItem("Seleccione");
        dietasCbx.setSelectedItem("Seleccione");
        tomasCbx.setSelectedItem("Seleccione");
        fechaRegistroText.setText("");
    }
    private void borrarRegistro(Connection conn) throws SQLException {
        //Método para borrar un registro de la BD
        String query = "DELETE FROM dieta_paciente_toma WHERE codigo = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,codigoText.getText());

        //Se pregunta primero si se quiere borrar
        int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere borrar este registro?");
        if (opcion == JOptionPane.YES_OPTION) {
            //Si selecciona sí, se borra y se limpian los campos de texto
            preparedStatement.execute();
            JOptionPane.showMessageDialog(null, "Borrado correctamente");
            codigoText.setText("");
            pacienteText.setText("");
            dietaText.setText("");
            fechaRegistroText.setText("");
            tomaText.setText("");
        }
        updateTable(this.query, conn);
    }

    private void modificarRegistro(Connection conn) throws SQLException {
        //Método para modificar un registro de la BD
        String query = "UPDATE dieta_paciente_toma SET nhc_paciente=?, codigo_dieta=?, codigo_toma=?, fecha=? WHERE codigo=?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1,pacienteText.getText());
        preparedStmt.setString(2,dietaText.getText());
        preparedStmt.setString(3,tomaText.getText());
        preparedStmt.setString(4,fechaRegistroText.getText());
        preparedStmt.setString(5,codigoText.getText());

        preparedStmt.execute();
        JOptionPane.showMessageDialog(null, "Registro editado");
        updateTable(this.query, conn);
    }

    private void consultarRegistro(Connection conn) throws SQLException {
        //Método para consultar los datos del registro con el codigo que se ha escrito en el campo correspondiente
        String query = "SELECT * FROM dieta_paciente_toma WHERE codigo = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(codigoText.getText()));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            codigoText.setText(rs.getString("codigo"));
            pacienteText.setText(rs.getString("nhc_paciente"));
            dietaText.setText(rs.getString("codigo_dieta"));
            tomaText.setText(rs.getString("codigo_toma"));
            fechaRegistroText.setText(rs.getString("fecha"));

        }
    }

}
