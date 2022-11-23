package entidades;

import java.util.ArrayList;
import java.util.Scanner;

public class Paciente {
    //En esta clase se guardarán los datos de la tabla paciente
    // Los atributos de esta calse son: Nombre, apellidos, habitación, NHC y observaciones
    // Crear las variables que hacen referencia a los atributos de paciente.
    private String nombre;
    private String apellidos;
    private String observaciones;
    private int habitacion;
    private int nhc;

    // Crear los métodos getter y setter para obtener los datos.
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getHabitacion() {
        return this.habitacion;
    }

    public void setHabitacion(int habitacion) {
        Scanner sc = new Scanner(System.in);
        boolean rango;
        //Se comprueba que se introduce un número de habitación válido (101-130,201-230,301-330)
        do {
            if (habitacion < 131 && habitacion > 100 || habitacion < 231 && habitacion > 200 || habitacion < 331 && habitacion > 300) {
                this.habitacion = habitacion;
                rango = true;
            } else {
                System.err.println("La habitación no existe. Introduzca otra: ");
                habitacion = sc.nextInt();
                rango = false;
            }
        } while (!rango);
    }

    public int getNhc() {
        return this.nhc;
    }

    public void setNhc(int nhc) {
        Scanner sc = new Scanner(System.in);
        String textoNhc;
        do {
            textoNhc = String.valueOf(nhc); //El nhc se pasa a texto para controlar que sea un núermo de 5 dígitos
            if (textoNhc.length() == 5){
                this.nhc = nhc;
            } else {
                System.err.println("NHC no válido. Vuelva a introducirlo (5 Dígitos): ");
                nhc = sc.nextInt();
            }
        } while (textoNhc.length() != 5);
    }

    //Método para mostar los datos de la tabla pacientes
    public static void mostrarPacientes(ArrayList<Paciente> pacientes) {
        for (Paciente paciente : pacientes) {
            System.out.println("NHC: " + paciente.getNhc() + " |Nombre: " + paciente.getNombre() + " |Apellidos: " + paciente.getApellidos() +
                    " |Observaciones: " + paciente.getObservaciones() + " |Habitación: " + paciente.getHabitacion());
            System.out.println("-----------------------------------");
        }
        System.out.println("───────────────────────────────────");
    }

    // En el siguiente método se recogerán los datos
    public void recogerDatosPaciente() {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        System.out.println("─────── DATOS DEL PACIENTE ───────");
        System.out.println("NHC (5 Dígitos): ");
        this.setNhc(scInt.nextInt());
        System.out.println("Nombre: ");
        this.setNombre(scString.nextLine());
        System.out.println("Apellidos: ");
        this.setApellidos(scString.nextLine());
        System.out.println("Observaciones: ");
        this.setObservaciones(scString.nextLine());
        System.out.println("Habitación: ");
        this.setHabitacion(scInt.nextInt());
        System.out.println("──────────────────────────────────");
    }
}
