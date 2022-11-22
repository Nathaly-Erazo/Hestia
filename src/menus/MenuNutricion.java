package menus;

import entidades.Dieta;
import entidades.Paciente;
import jdbc.JdbcDieta;
import jdbc.JdbcPaciente;

import java.sql.Connection;
import java.util.Scanner;


public class MenuNutricion {
    public static void menuNutricion(Connection conn) {
        //Clase correspondiente al menú del departamento nutrición
        //Tiene la misma estructura que la clase MenuMedico
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int seccion;
        try {
            MenuPrincipal.espacios();
            System.out.println("───────────── DEPARTAMENTO NUTRICIÓN ──────────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique a qué sección quiere acceder:       │\s
                    │    1.-Consultar Pacientes                    │\s
                    │    2.-Consultar Dietas                       │\s
                    │    3.-Toma                                   │\s
                    │    4.-Registro                               │\s
                    │    5.-Salir de Hestia                        │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            seccion = sc.nextInt();
            switch (seccion) {
                case 1 -> Paciente.mostrarPacientes(JdbcPaciente.consultarPaciente(conn));
                case 2 -> Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn));
                case 3 -> MenuToma.menuToma(conn);
                case 4 -> MenuDpt.menuDpt(conn);
                case 5 -> {
                    System.out.println("════════════ HASTA PRONTO ════════════");
                    System.exit(0);
                }
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuNutricion(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No");
                int continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuNutricion(conn);
                    case 2 -> {
                        System.out.println("════════════ HASTA PRONTO ════════════");
                        System.exit(0);
                    }
                    default -> {
                        System.err.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (NumberFormatException e) {
            System.err.println("Formato de número no válido");
            menuNutricion(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuNutricion(conn);
        }
    }
}
