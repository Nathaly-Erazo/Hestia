package entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

}
