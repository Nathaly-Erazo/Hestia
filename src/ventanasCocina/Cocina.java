package ventanasCocina;

import entidades.Dieta;
import ventanasMedico.MostrarTomas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cocina extends JFrame {
    //Atributos que corresponden a los elementos del formulario
    private JPanel contentPane;
    private JButton addDietaButton;
    private JButton consultarDietaButton;
    private JButton borrarDietaButton;
    private JButton modificarDIetaButton;
    private JButton consultaTomas;
    private JTextField codigoText;
    private JTextField nombreText;
    private JTextField desayunoText;
    private JTable tablaDietas;
    private JTextField comidaText;
    private JTextField meriendaText;
    private JTextField cenaText;

    //Datos para construir la tabla en la que se muestran los datos
    DefaultTableModel myModel;
    String[] titulos = {"CÓDIGO", "NOMBRE", "DESYUNO", "COMIDA", "MERIENDA", "CENA",};
    String[][] datos = {};
    String query = "SELECT * FROM dieta";

    public Cocina(Connection conn){
        //Se crea la tabla y se rellana
        myModel = new DefaultTableModel(datos, titulos);
        tablaDietas.setModel(myModel);
        updateTable(query, conn);

        setContentPane(contentPane);
        setTitle("Departamento cocina");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Acciones de los botones de añadir, borrar, modificar y consultar que llevan a los métodos correspondietnes
        addDietaButton.addActionListener(e -> {
            try {
                insertarDieta(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Datos mal introducidos");
            }
        });
        borrarDietaButton.addActionListener(e -> {
            try {
                borrarDieta(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        modificarDIetaButton.addActionListener(e -> {
            try {
                modificarDieta(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Datos mal introducidos");
            }
        });
        consultarDietaButton.addActionListener(e -> {
            try {
                consultarDieta(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Acción del botón que abre otra ventana en la que se muestran los datos
        consultaTomas.addActionListener(e -> new MostrarTomas(conn));

        //Con los siguientes KeyListener se controla lo que introduce el usuario
        codigoText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //Solo deja que se introduzcan números y de dos dígitos
                int key = e.getKeyChar();
                boolean numeros = key >= 48 && key <= 57;
                if (!(numeros))
                    e.consume();
                if(codigoText.getText().trim().length()>=2)
                    e.consume();
            }
        });

        nombreText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //No deja que introduzcan números en el nombre
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
                Dieta dieta = new Dieta();
                dieta.setCodigo(rs.getInt(1));
                dieta.setNombre(rs.getString(2));
                dieta.setDesayuno(rs.getString(3));
                dieta.setComida(rs.getString(4));
                dieta.setMerienda(rs.getString(5));
                dieta.setCena(rs.getString(6));
                Object[] row = {dieta.getCodigo(), dieta.getNombre(), dieta.getDesayuno(),
                        dieta.getComida(), dieta.getMerienda(), dieta.getCena()};
                myModel.addRow(row);
            }
            preparedStmt.close();
        } catch (SQLException ex) {
            System.out.println();
        }
    }
    private void emptyTable() {
        // Método que limpia la tabla para que vuelva a cargar la nueva informacion de la BD
        int filas = tablaDietas.getRowCount();
        for (int i = 0; i < filas; i++)
            myModel.removeRow(0);
    }
    private void insertarDieta (Connection conn) throws SQLException {
        //Método para insertar una dieta en la BD, se deja el código en null porque es autoincremental
        String query = "INSERT INTO dieta (codigo, nombre, desayuno, comida, merienda, cena) VALUES (NULL,?,?,?,?,?) ";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,nombreText.getText());
        preparedStatement.setString(2, desayunoText.getText());
        preparedStatement.setString(3,comidaText.getText());
        preparedStatement.setString(4,meriendaText.getText());
        preparedStatement.setString(5,cenaText.getText());
        preparedStatement.execute();
        JOptionPane.showMessageDialog(null,"Dieta introducida");
        updateTable(this.query, conn);
    }
    private void borrarDieta (Connection conn) throws SQLException {
        //Método para borrar la dieta de la BD
        String query = "DELETE FROM dieta WHERE codigo = ?";
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
            desayunoText.setText("");
            comidaText.setText("");
            meriendaText.setText("");
            cenaText.setText("");
        }
        updateTable(this.query, conn);
    }
    private void modificarDieta(Connection conn) throws SQLException {
        //Método para modificar la dieta de la BD
        String query = "UPDATE dieta SET nombre=?, desayuno=?, comida=?, merienda=?, cena=? WHERE codigo =?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,nombreText.getText());
        preparedStatement.setString(2,desayunoText.getText());
        preparedStatement.setString(3,comidaText.getText());
        preparedStatement.setString(4,meriendaText.getText());
        preparedStatement.setString(5,cenaText.getText());
        preparedStatement.setString(6,codigoText.getText());

        preparedStatement.execute();
        JOptionPane.showMessageDialog(null,"Dieta editada");
        updateTable(this.query, conn);
    }
    private void consultarDieta(Connection conn) throws SQLException {
        //Método para consultar los datos de la dieta con el código que se ha escrito en el campo correspondiente
        String query = "SELECT * FROM dieta WHERE codigo = ?";
        PreparedStatement preparedStatement  = conn.prepareStatement(query);
        preparedStatement.setInt(1,Integer.parseInt(codigoText.getText()));
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            codigoText.setText(rs.getString("codigo"));
            nombreText.setText(rs.getString("nombre"));
            desayunoText.setText(rs.getString("desayuno"));
            comidaText.setText(rs.getString("comida"));
            meriendaText.setText(rs.getString("merienda"));
            cenaText.setText(rs.getString("cena"));
        }
    }
}
