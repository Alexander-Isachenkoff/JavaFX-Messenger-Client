package messager.utility;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class LinesCountTest {

    public int[] qsort(int... array) {
        if (array.length <= 1) {
            return array;
        } else {
            int pivot = array[0];
            int[] left = Arrays.stream(array).skip(1).filter(i -> i <= pivot).toArray();
            int[] right = Arrays.stream(array).skip(1).filter(i -> i > pivot).toArray();
            int[] newArray = new int[array.length];
            int[] qsortLeft = qsort(left);
            int[] qsortRight = qsort(right);
            System.arraycopy(qsortLeft, 0, newArray, 0, qsortLeft.length);
            newArray[left.length] = pivot;
            System.arraycopy(qsortRight, 0, newArray, left.length + 1, qsortRight.length);
            return newArray;
        }
    }

    @Test
    void countLines() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("C:/Users/user/Desktop/Исаченков/atom-file-icons-master/atom-file-icons-master/icons/default_file.svg"));
            ImageIO.write(image, "png", new File("C:/Users/user/Desktop/Исаченков/atom-file-icons-master/atom-file-icons-master/icons/default_file.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
