package com.GlobeDelegates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImagemUtil {

    private static Texture pergaminho;

    public static void desenharFundo(SpriteBatch batch, Texture img, float w, float h) {
        float imgW = img.getWidth();
        float imgH = img.getHeight();
        float escala = Math.max(w / imgW, h / imgH);
        float novaW = imgW * escala;
        float novaH = imgH * escala;
        float x = (w - novaW) / 2f;
        float y = (h - novaH) / 2f;
        batch.draw(img, x, y, novaW, novaH);
    }

    public static void desenharPergaminho(SpriteBatch batch, float x, float y, float w, float h) {
        if (pergaminho == null) {
            pergaminho = new Texture("pergaminho.png");
        }
        batch.draw(pergaminho, x, y, w, h);
    }
}
