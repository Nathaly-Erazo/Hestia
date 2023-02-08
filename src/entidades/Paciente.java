package entidades;

import javax.swing.*;

public class Paciente {
    //En esta clase se guardarán los datos de la tabla paciente
    // Los atributos de esta calse son: Nombre, apellidos, habitación, NHC, fecha de nacimiento y observaciones
    // Crear los atributos del paciente.
    private String nombre;
    private String apellidos;
    private String observaciones;
    private int habitacion;
    private int nhc;
    private String fecha_nacimiento;

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
        //Se comprueba que se introduce un número de habitación válido (101-130,201-230,301-330)
            if (habitacion < 131 && habitacion > 100 || habitacion < 231 && habitacion > 200 || habitacion < 331 && habitacion > 300) {
                this.habitacion = habitacion;
            } else {
                JOptionPane.showMessageDialog(null,"La habitación no existe");
            }
    }

    public int getNhc() {
        return this.nhc;
    }

    public void setNhc(int nhc) {
        this.nhc = nhc;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

}
