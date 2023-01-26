package udp;

import java.io.Serializable;

public class Unidad implements Serializable {

    private float vida;
    private String nombre;
    private int posX, posY;
    private String ejercito;


    public Unidad(float vida, String nombre, int posX, int posY, String ejercito) {
        this.vida = vida;
        this.nombre = nombre;
        this.posX = posX;
        this.posY = posY;
        this.ejercito = ejercito;
    }

    public float getVida() {
        return vida;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "Unidad{" +
                "vida=" + vida +
                ", nombre='" + nombre + '\'' +
                ", posX=" + posX +
                ", posY=" + posY +
                ", ejercito='" + ejercito + '\'' +
                '}';
    }
}
