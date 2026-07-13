package ui;

import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;

public class FontChanger {
    public static void apply(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }
}
