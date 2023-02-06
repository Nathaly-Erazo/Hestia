import ventanasMedico.Medico;
import jdbc.JdbcConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {

        String databaseUrl = "jdbc:mysql://localhost:3306/hestia"; //Url de la base de datos
        Properties info = new Properties(); //Donde se  va a guardar el usuario y contraseña si existiese
        info.put("user", "root");
        info.put("password", "");

        //Se llama a la clase JdbcConfig para hacer la conexión
        Connection dbConnection = JdbcConfig.getConnection(databaseUrl, info);

        //Si la conexión se ha realizado, entrará en en el sistema
        if (dbConnection != null){
            //Le lleva al usuario a la ventana de registro
            new Medico(dbConnection);
        }
    }
}