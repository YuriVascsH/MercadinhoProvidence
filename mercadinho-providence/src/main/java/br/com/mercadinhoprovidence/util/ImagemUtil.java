package br.com.mercadinhoprovidence.util;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public final class ImagemUtil {

    private ImagemUtil() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    /**
     * 
     * @param clazz
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon loadImage(Class<?> clazz,String path, int width, int height) {
        if (path == null || path.isBlank())
            return null;
        try {
            URL url = clazz.getResource(path);
            if(url != null) {
                ImageIcon iconOriginal = new ImageIcon(url);
                Image img = iconOriginal.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); 
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + path);
        }

        return null;
    }
}
