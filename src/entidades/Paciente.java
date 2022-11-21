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
        this.habitacion = habitacion;
    }

    public int getNhc() {
        return this.nhc;
    }

    public void setNhc(int nhc) {
        this.nhc = nhc;
    }

    //Método para mostar los datos de la tabla pacientes
    public static void mostrarPacientes (ArrayList<Paciente> pacientes){
        for (Paciente paciente: pacientes){
            System.out.println("Nombre: " + paciente.getNombre() + " |Apellidos: " + paciente.getApellidos() +
                    " |NHC: " + paciente.getNhc() + " |Observaciones: " + paciente.getObservaciones() + " |Habitación: " + paciente.getHabitacion());
            System.out.println("-----------------------------------");
        }
    }

    // En el siguiente método se recogerán los datos
    public void recogerDatosPaciente() {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);

        System.out.println("DATOS DEL PACIENTE");
        System.out.println("NHC: ");
        this.setNhc(scInt.nextInt());
        System.out.println("Nombre: ");
        this.setNombre(scString.nextLine());
        System.out.println("Apellidos: ");
        this.setApellidos(scString.nextLine());
        System.out.println("Observaciones: ");
        this.setObservaciones(scString.nextLine());
        System.out.println("Habitación: ");
        this.setHabitacion(scInt.nextInt());
    }
}
