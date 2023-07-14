package messager.util;

import javafx.scene.image.Image;
import messager.Main;

public class Resources {

    private static Image DEFAULT_USER_IMAGE;

    public static Image getDefaultUserImage() {
        if (DEFAULT_USER_IMAGE == null) {
            DEFAULT_USER_IMAGE = new Image(Main.class.getResourceAsStream("images/default_user_small.png"));
        }
        return DEFAULT_USER_IMAGE;
    }

}
