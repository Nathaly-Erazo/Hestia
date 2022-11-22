package entidades;

import java.util.ArrayList;
import java.util.Scanner;

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

    //Método para mostrar las tomas
    public static void mostrarTomas(ArrayList<Toma> tomas) {
        for (Toma toma : tomas) {
            System.out.println("Código: " + toma.getCodigo() + " Nombre: " + toma.getNombre());
            System.out.println("-----------------------------------");
        }
        System.out.println("───────────────────────────────────");
    }

    //Método para recoger los datos de la toma.
    public void recogerDatosToma() {
        Scanner scString = new Scanner(System.in);
        System.out.println("─────── DATOS DE LA TOMA ───────");
        System.out.println("Nombre: ");
        this.setNombre(scString.nextLine());
        System.out.println("────────────────────────────────");
    }
}
