package entidades;

public class Dieta {
    //En esta clase se guardan los datos de la tabla dieta
    //Los atributos que va a tener esta clase son: código, nombre, desayuno, comida, merienda y cena
    //Crear los atributos
    private int codigo;
    private String nombre;
    private String desayuno;
    private String comida;
    private String merienda;
    private String cena;


    // Crear los get y set de cada atributo
    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDesayuno() {
        return this.desayuno;
    }

    public void setDesayuno(String desayuno) {
        this.desayuno = desayuno;
    }

    public String getComida() {
        return this.comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getMerienda() {
        return this.merienda;
    }

    public void setMerienda(String merienda) {
        this.merienda = merienda;
    }

    public String getCena() {
        return this.cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }


}
