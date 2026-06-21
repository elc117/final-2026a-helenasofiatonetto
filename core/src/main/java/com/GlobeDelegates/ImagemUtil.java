package com.GlobeDelegates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImagemUtil {

    public static void desenharFundo(SpriteBatch batch, Texture img, float w, float h) {
        float imgW = img.getWidth();
        float imgH = img.getHeight();

        float escalaW = w / imgW;
        float escalaH = h / imgH;
        float escala = Math.max(escalaW, escalaH);

        float novaW = imgW * escala;
        float novaH = imgH * escala;

        float x = (w - novaW) / 2f;
        float y = (h - novaH) / 2f;

        batch.draw(img, x, y, novaW, novaH);
    }
}
