package entidades;

import java.util.ArrayList;
import java.util.Scanner;

public class Toma {
    //codigo y nombre
    private int codigo;
    private String nombre;


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

    @Override
    public String toString() {
        return "TOMA\n" +
                "Código: " + codigo + "\n" +
                "Nombre: " + nombre;
    }
    public static void mostrarTomas (ArrayList<Toma> tomas){
        for (Toma toma: tomas){
            System.out.println("Código: " + toma.getCodigo() + " Nombre: " + toma.getNombre());
            System.out.println("-----------------------------------");
        }
    }

    public void recogerDatosToma() {
        Scanner scString = new Scanner(System.in);

        System.out.println("DATOS DE LA TOMA");
        System.out.println("Nombre: ");
        this.setNombre(scString.nextLine());
    }
}
