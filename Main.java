package Proyecto;
public class Main {
    public static void main(String[] args) throws Exception {

        MandoTelevision mando = new MandoTelevision();

        // HILO PARA VOZ
        Thread hiloVoz = new Thread(() -> {
            new MandoVoz(mando).iniciar();
        });

        // HILO PARA GESTOS
        Thread hiloGestos = new Thread(() -> {
            new DeteccionMovimientoAR(mando).iniciar();
        });

        hiloVoz.start();
        hiloGestos.start();
    }
}
