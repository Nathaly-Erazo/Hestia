package entidades;

import java.util.ArrayList;
import java.util.Scanner;

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

    //Método para mostar los datos de la tabla Dieta
    public static void mostrarDietas(ArrayList<Dieta> dietas) {
        //For each, funciona como un for normal, pero asignando cada uno de los elementos de la lista a una
        //variable u objeto, dependiendo del tipo del que sea el array
        for (Dieta dieta : dietas) {
            System.out.println("Código: " + dieta.getCodigo() + " Nombre: " + dieta.getNombre() + "\n" +
                    " |Desayuno: " + dieta.getDesayuno() + "\n" +
                    " |Comida: " + dieta.getComida() + "\n" +
                    " |Merienda: " + dieta.getMerienda() + "\n" +
                    " |Cena: " + dieta.getCena());
            System.out.println("-----------------------------------");
        }
        System.out.println("───────────────────────────────────");
    }

    //Método para recoger los datos de la dieta que se quiere guardar
    public void recogerDatosDieta() {
        Scanner scString = new Scanner(System.in);
        System.out.println("─────── DATOS DE LA DIETA  ───────");
        System.out.println("Nombre: ");
        this.setNombre(scString.nextLine());
        System.out.println("Desayuno: ");
        this.setDesayuno(scString.nextLine());
        System.out.println("Comida: ");
        this.setComida(scString.nextLine());
        System.out.println("Merienda: ");
        this.setMerienda(scString.nextLine());
        System.out.println("Cena: ");
        this.setCena(scString.nextLine());
        System.out.println("──────────────────────────────────");
    }

}
