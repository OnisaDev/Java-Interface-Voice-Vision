package Proyecto;

public class MandoTelevision {

    private int volumen;

    public MandoTelevision() {
        this.volumen = 0;
    }

    public void subirVolumen() {
        this.volumen++;
        
    }

    public void bajarVolumen() {
        if (this.volumen > 0) {
            this.volumen--;
        } else {
            System.out.println("El volumen ya está en 0 y no se puede bajar más.");
        }
    }

    public int obtenerVolumen() {
        return this.volumen;
    }
}

