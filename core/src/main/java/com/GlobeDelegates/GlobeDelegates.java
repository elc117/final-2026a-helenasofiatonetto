package com.GlobeDelegates;
import com.badlogic.gdx.Game;

public class  GlobeDelegates extends Game{
    @Override
    public void create(){
        setScreen(new TelaInicio(this));
    }
}