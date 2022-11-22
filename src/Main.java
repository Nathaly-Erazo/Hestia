import jdbc.JdbcConfig;

import menus.MenuPrincipal;

import java.sql.Connection;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {

        String databaseUrl = "jdbc:mysql://localhost:3306/hestia"; //Url de la base de datos
        Properties info = new Properties(); //Donde se  va a guardar el usuario y contraseña si existiese
        info.put("user", "root");
        info.put("password", "");

        //Se llama a la clase JdbcConfig para hacer la conexión
        Connection dbConnection = JdbcConfig.getConnection(databaseUrl, info);

        //Si la conexión se ha realizado, entrará en en el sistema
        if (dbConnection != null){
            System.out.println("════════════ BIENVENIDO/A A HESTIA ════════════");
            //Le lleva al usuario al menú principal y al sistema
            MenuPrincipal.menuPrincipal(dbConnection);
        }
    }
}