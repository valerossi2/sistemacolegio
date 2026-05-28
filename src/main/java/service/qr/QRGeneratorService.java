package service.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import model.Usuario;
import repository.UsuarioRepositry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Path path = Paths.get(rutaArchivo);
        Files.createDirectories(path.getParent());
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }

    /**
     * Genera y guarda un QR en PNG para cada maestro en la base de datos.
     * @param outputDir directorio donde se guardarán las imágenes
     * @return lista de rutas de archivos generados
     */
    public static List<String> generarQrMaestros(String outputDir) {
        List<String> archivos = new ArrayList<>();
        UsuarioRepositry repo = new UsuarioRepositry();
        List<Usuario> maestros = repo.findAllMaestros();

        for (Usuario maestro : maestros) {
            try {
                String contenido = String.format(
                        "NOMBRE: %s\nEMAIL: %s\nID: %d\nROL: %s\nCOLEGIO: SIS Colegio\nFECHA: %s",
                        maestro.getNombreCompleto(),
                        maestro.getEmail(),
                        maestro.getId(),
                        maestro.getRol().getNombre(),
                        LocalDate.now()
                );

                String nombreArchivo = sanitizeFileName(maestro.getNombreCompleto())
                        + "_" + maestro.getId() + ".png";
                String ruta = outputDir + "/" + nombreArchivo;

                guardarQr(contenido, ruta, 300, 300);
                archivos.add(ruta);
            } catch (Exception e) {
                System.err.println("Error generando QR para " + maestro.getEmail() + ": " + e.getMessage());
            }
        }
        return archivos;
    }

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_").replace(" ", "_");
    }
}

