package com.gameObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.screens.GameScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class WorldGenerator {
    Stage stage;

    public void switchScreen(final Game game, final Screen newScreen){
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(0.5f));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(newScreen);
            }
        }));
    }

    public WorldGenerator(int newLevel){

        if(newLevel == 1){
            //LVL1 = LÆR AT GÅ
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(4,2,1,1,"maps/level1.tmx")); }
        else if(newLevel == 2){
            //LVL2 = 2 goals - GÅ LÆNGERE
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(4,2,2,2,"maps/level2.tmx")); }
        else if(newLevel == 3){
            //LVL3 = LÆR AT DREJE
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(2,2,2,3,"maps/level3.tmx"));
        }else if(newLevel == 4){
            //LVL4 = MERE TRÆNING
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(7,2,2,4,"maps/level4.tmx"));
        }else if(newLevel == 5){
            //LÆR 4LOOP
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1,4,3,5,"maps/level5.tmx"));
        }else if(newLevel == 6){
            //LÆR AT BRUGE 4loop til at løse mønstre
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1,1,3,6,"maps/level6.tmx"));
        }else if(newLevel == 7){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1,2,4,7,"maps/level7.tmx"));

        }else if(newLevel == 8){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(0,0,8,8,"maps/level8.tmx"));
        }else if(newLevel == 9){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1,5,3,9,"maps/level9.tmx"));
        }else if(newLevel == 10){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1,1,10,10,"maps/level10.tmx"));
        }
    }





}
