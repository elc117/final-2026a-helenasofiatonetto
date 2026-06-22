package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class PersonagemAnimado {

    public enum Direcao {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private float x;
    private float y;

    private float velocidade = 250f;

    private float stateTime;

    private Direcao direcaoAtual;

    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;

    private TextureAtlas atlas;

    public PersonagemAnimado(float xInicial, float yInicial) {

    atlas = new TextureAtlas("personagens/turista.atlas");

    walkUp = new Animation<>(
        0.15f,
        atlas.findRegions("up"),
        Animation.PlayMode.LOOP
    );

    walkDown = new Animation<>(
        0.15f,
        atlas.findRegions("down"),
        Animation.PlayMode.LOOP
    );

    walkLeft = new Animation<>(
        0.15f,
        atlas.findRegions("left"),
        Animation.PlayMode.LOOP
    );

    walkRight = new Animation<>(
        0.15f,
        atlas.findRegions("right"),
        Animation.PlayMode.LOOP
    );

    direcaoAtual = Direcao.DOWN;

    this.x = xInicial;
    this.y = yInicial;
    
    }

    public void update(float delta) {

    boolean moving = false;

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        x -= velocidade * delta;
        direcaoAtual = Direcao.LEFT;
        moving = true;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        x += velocidade * delta;
        direcaoAtual = Direcao.RIGHT;
        moving = true;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        y += velocidade * delta;
        direcaoAtual = Direcao.UP;
        moving = true;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        y -= velocidade * delta;
        direcaoAtual = Direcao.DOWN;
        moving = true;
    }

    if (moving) {
        stateTime += delta;
    }
    }

    public void draw(SpriteBatch batch) {

    TextureRegion frame;

    switch (direcaoAtual) {

        case UP:
            frame = walkUp.getKeyFrame(stateTime, true);
            break;

        case LEFT:
            frame = walkLeft.getKeyFrame(stateTime, true);
            break;

        case RIGHT:
            frame = walkRight.getKeyFrame(stateTime, true);
            break;

        default:
            frame = walkDown.getKeyFrame(stateTime, true);
            break;
    }

    batch.draw(frame, x, y, 80, 80);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}