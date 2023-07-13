package messager.util;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTest {

    @Test
    void test() throws IOException {
        BufferedImage img = ImageIO.read(new File("test_resources/test.png"));

        String encodedImage = ImageUtils.encodeImage(img, "png");
        Image image = ImageUtils.decodeImage(encodedImage);
    }

}
