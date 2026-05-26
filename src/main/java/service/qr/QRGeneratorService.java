package service.qr;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import model.Usuario;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class QRGeneratorService {
    private QRGeneratorService(){}

    public static Image generarQRMestro(Usuario maestro)
            throws WriterException, IOException {

        // Contenido del QR
        String contenido = String.format(
                "NOMBRE: %s\n"      +
                        "EMAIL: %s\n"       +
                        "ID: %d\n"          +
                        "ROL: %s\n"         +
                        "COLEGIO: SIS Colegio\n" +
                        "FECHA: %s",
                maestro.getNombreCompleto(),
                maestro.getEmail(),
                maestro.getId(),
                maestro.getRol().getNombre(),
                LocalDate.now()
        );

        return generarQR(contenido, 300, 300);
    }

    public static Image generarQREstudiante(model.Estudiante estudiante)
            throws WriterException, IOException{
        String contenido = String.format(
                "NOMBRE: %s\n"      +
                        "CODIGO: %s\n"      +
                        "COLEGIO: SIS Colegio\n" +
                        "FECHA: %s",
                estudiante.getNombreCompleto(),
                estudiante.getCodigo(),
                LocalDate.now()
        );

        return generarQR(contenido, 300, 300);
    }

    public static Image generarQR(String contenido, int ancho, int alto)
            throws WriterException, IOException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(
                contenido, BarcodeFormat.QR_CODE, ancho, alto
        );

        BufferedImage buffered = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(buffered, "PNG", out);
        return new Image(new ByteArrayInputStream(out.toByteArray()));
    }

    public static void guardarQr(String contenido, String rutaArchivo, int ancho, int alto)
            throws WriterException, IOException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix= writer.encode(
                contenido, BarcodeFormat.QR_CODE, ancho, alto
        );
        java.nio.file.Path path = java.nio.file.Paths.get(rutaArchivo);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }
}

