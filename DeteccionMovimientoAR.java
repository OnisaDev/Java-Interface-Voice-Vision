package Proyecto;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.swing.*;

public class DeteccionMovimientoAR {

    private MandoTelevision mando;

    public DeteccionMovimientoAR(MandoTelevision mando) {
        this.mando = mando;
    }

    public void iniciar() {

        OpenCVFrameConverter.ToMat convertidor = new OpenCVFrameConverter.ToMat();

        VideoCapture camara = new VideoCapture(0);
        if (!camara.isOpened()) {
            System.out.println("No se puede abrir la cámara");
            return;
        }

        Mat actual = new Mat();
        Mat previo = new Mat();
        Mat diferencia = new Mat();

        CanvasFrame ventana = new CanvasFrame("Control por Gestos");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        camara.read(previo);
        opencv_imgproc.cvtColor(previo, previo, opencv_imgproc.COLOR_BGR2GRAY);

        while (ventana.isVisible() && camara.read(actual)) {

            opencv_imgproc.cvtColor(actual, actual, opencv_imgproc.COLOR_BGR2GRAY);

            opencv_core.absdiff(previo, actual, diferencia);
            opencv_imgproc.GaussianBlur(diferencia, diferencia, new Size(5, 5), 0);
            opencv_imgproc.threshold(diferencia, diferencia, 25, 255, opencv_imgproc.THRESH_BINARY);

            MatVector contornos = new MatVector();
            opencv_imgproc.findContours(diferencia.clone(), contornos, new Mat(),
                    opencv_imgproc.RETR_EXTERNAL, opencv_imgproc.CHAIN_APPROX_SIMPLE);

            boolean izquierda = false;
            boolean derecha = false;

            for (int i = 0; i < contornos.size(); i++) {

                Rect area = opencv_imgproc.boundingRect(contornos.get(i));

                if (area.height() < 100) continue;
                if (area.y() > actual.rows() / 2) continue;

                if (area.x() < actual.cols() / 2) izquierda = true;
                else derecha = true;
            }

            // ACCIONES
            if (izquierda) mando.subirVolumen();
            if (derecha) mando.bajarVolumen();

            // Mostrar volumen en pantalla
            opencv_imgproc.putText(actual, "Volumen: " + mando.obtenerVolumen(),
                    new Point(10, 30), opencv_imgproc.FONT_HERSHEY_SIMPLEX,
                    1.0, new Scalar(255, 255, 255, 0));

            ventana.showImage(convertidor.convert(actual));

            previo = actual.clone();
        }

        camara.release();
        ventana.dispose();
    }
}
