package messager.util;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static String encodeImage(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    public static Image decodeImage(String encodedImage) {
        byte[] bytes = Base64.getDecoder().decode(encodedImage);
        return new Image(new ByteArrayInputStream(bytes));
    }

}
