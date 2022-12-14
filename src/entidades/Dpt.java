package entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void setFecha(String fecha) throws ParseException {
        //El SimpleDateFormart es una clase de java que sirve para formatear (fecha->texto) y parsear (texto->fecha)
        if (fecha.contains("/")){
            SimpleDateFormat sdfFuente = new SimpleDateFormat("dd/MM/yyyy"); //Formato en el que llega la fecha
            SimpleDateFormat sdfDestino = new SimpleDateFormat("yyyy-MM-dd"); //Formato en el que se quiere guardar
            this.fecha = sdfDestino.format(sdfFuente.parse(fecha)); //Se guarda la fecha con el formato que se desea
        } else {
            this.fecha = fecha;
        }
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
        System.out.println("───────────────────────────────────");
    }

    //Método para recoger los datos del registro  que se quiere guardar
    public void recogerDatosDpt() throws ParseException {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        System.out.println("─────── REGISTRO DE DIETAS DEL PACIENTE ───────");
        System.out.println("Paciente (NHC): ");
        this.setNhcPaciente(scInt.nextInt());
        System.out.println("Código de Dieta (1.-B 2.-BL 3.-SMBL 4.-SMLQ 5.-LQ 6.-TX 7.-ABS): ");
        this.setCodigoDieta(scInt.nextInt());
        System.out.println("Código de Toma (1.-Desayuno 2.-Comida 3.-Merienda 4.-Cena): ");
        this.setCodigoToma(scInt.nextInt());
        System.out.println("Fecha (dd/mm/yyyy): ");
        this.setFecha(scString.nextLine());
        System.out.println("───────────────────────────────────────────────");
    }
}
