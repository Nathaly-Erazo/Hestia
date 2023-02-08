package entidades;

public class Toma {
    //En esta clase se guardan los datos de la tabla toma
    //Esta clase tiene los atributos codigo y nombre
    //Se crean los atributos
    private int codigo;
    private String nombre;

    //Se crean los get y set de cada atributo
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
