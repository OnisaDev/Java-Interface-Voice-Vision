package Proyecto;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;

public class MandoVoz {
    
    private MandoTelevision mando;

    public MandoVoz(MandoTelevision mando) {
        this.mando = mando;
    }

    public void iniciar() {

        try {
            // Cargar el modelo de voz (carpeta descargada)
            Model model = new Model(
    "D:\\CESUR DAM\\Segundo\\Desarrollo de interfaces\\CP2\\control_interfaces\\desa_inter\\src\\main\\resources\\model-small-es");


            Recognizer reconocedor = new Recognizer(model, 16000);

            // Configurar micrófono
            AudioFormat formato = new AudioFormat(16000, 16, 1, true, false);
            TargetDataLine microfono = AudioSystem.getTargetDataLine(formato);

            microfono.open(formato);
            microfono.start();

            System.out.println("Diga 'subir volumen' o 'bajar volumen'.");

            byte[] buffer = new byte[4096];

            while (true) {

                int numBytes = microfono.read(buffer, 0, buffer.length);

                if (reconocedor.acceptWaveForm(buffer, numBytes)) {

                    String texto = reconocedor.getResult();
                    System.out.println("Detectado: " + texto);

                    if (texto.contains("subir volumen")) {
                        mando.subirVolumen();
                        System.out.println("Volumen: " + mando.obtenerVolumen());
                    }

                    if (texto.contains("bajar volumen")) {
                        mando.bajarVolumen();
                        System.out.println("Volumen: " + mando.obtenerVolumen());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el reconocimiento de voz: " + e.getMessage());
        }
    }
}

