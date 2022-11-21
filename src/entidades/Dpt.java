package entidades;

import java.util.ArrayList;
import java.util.Scanner;

public class Dpt {
    // En esta clase se guardarán los datos de la tabla llamada dieta_paciente_toma.
    //Los atributos son: código, fecha, codigo de la dieta, codigo de la toma y nch del paciente.
    //Crear los atributos
    private int codigo;
    private String fecha;
    private int codigoDieta;
    private int codigoToma;
    private int nhcPaciente;

    // Crear los get y set de cada atributo
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCodigoDieta() {
        return codigoDieta;
    }

    public void setCodigoDieta(int codigoDieta) {
        this.codigoDieta = codigoDieta;
    }

    public int getCodigoToma() {
        return codigoToma;
    }

    public void setCodigoToma(int codigoToma) {
        this.codigoToma = codigoToma;
    }

    public int getNhcPaciente() {
        return nhcPaciente;
    }

    public void setNhcPaciente(int nhcPaciente) {
        this.nhcPaciente = nhcPaciente;
    }

    //Método para mostar los datos de la tabla dieta_paciente_toma
    public static void mostrarRegistros(ArrayList<Dpt> registros) {
        for (Dpt registro : registros) {
            System.out.println("Código: " + registro.getCodigo()  + " |NHC Paciente: " + registro.getNhcPaciente() +
                    " |Dieta: " + registro.getCodigoDieta() + " |Toma: " + registro.getCodigoToma() + " |Fecha: " + registro.getFecha());
            System.out.println("-----------------------------------");
        }
    }

    //Método para recoger los datos del registro  que se quiere guardar
    public void recogerDatosDpt() {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);

        System.out.println("REGISTRO DE DIETAS DEL PACIENTE");
        System.out.println("Paciente: ");
        this.setNhcPaciente(scInt.nextInt());
        System.out.println("Dieta: ");
        this.setCodigoDieta(scInt.nextInt());
        System.out.println("Toma: ");
        this.setCodigoToma(scInt.nextInt());
        System.out.println("Fecha (yyyy-mm-dd): ");
        this.setFecha(scString.nextLine());
    }
}
