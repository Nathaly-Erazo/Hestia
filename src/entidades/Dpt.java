package entidades;

import java.text.ParseException;

public class Dpt {
    // En esta clase se guardarán los datos de la tabla llamada dieta_paciente_toma.
    //Los atributos son: código, fecha, codigo de la dieta, codigo de la toma y nch del paciente.

    //Al añadir estos datos en el formulario mediante Combobox, solo se van a usar el codigo y la fecha
    //Crear los atributos
    private int codigo;
    private String fecha;

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

}
