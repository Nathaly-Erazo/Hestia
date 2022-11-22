package menus;


import entidades.Dieta;
import entidades.Dpt;
import entidades.Toma;
import jdbc.JdbcDieta;
import jdbc.JdbcDpt;
import jdbc.JdbcToma;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuMedico {
    public static void menuMedico(Connection conn) {
        //Clase correspondiente al menú del departamento médico
        Scanner sc = new Scanner(System.in);
        boolean seguir = true; //Controlar que quiera seguir en este menú
        int seccion;
        int continuar;
        try {
            MenuPrincipal.espacios(); //Llamada al método para crear espacios
            System.out.println("───────────── DEPARTAMENTO MÉDICO ─────────────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique a qué sección quiere acceder:       │\s
                    │    1.-Paciente                               │\s
                    │    2.-Consultar Tomas                        │\s
                    │    3.-Consultar Dietas                       │\s
                    │    4.-Consultar Registros                    │\s
                    │    5.-Salir de Hestia                        │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            seccion = sc.nextInt(); //El usuario introduce el número de lo que quiere acceder
            switch (seccion) {
                case 1 -> MenuPaciente.menuPaciente(conn);
                case 2 -> Toma.mostrarTomas(JdbcToma.consultarToma(conn));
                case 3 -> Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn));
                case 4 -> Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                case 5 -> {
                    System.out.println("════════════ HASTA PRONTO ════════════");
                    System.exit(0);
                }
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuMedico(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No"); //Deja al usuario volver al menú o no
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuMedico(conn);
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
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuMedico(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuMedico(conn);
        }
    }
}
