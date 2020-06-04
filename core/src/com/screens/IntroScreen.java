package com.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.com.jesper.jespil.Accessors.SpriteAccessor;
import com.gameObjects.ImageFileClass;

public class IntroScreen implements Screen {
    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;
    ImageFileClass ImageFileClass = new ImageFileClass();
    public Sound sound = Gdx.audio.newSound(Gdx.files.internal("introsong.mp3"));


    public IntroScreen() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        batch.begin();
        ImageFileClass.loadTextures();
        ImageFileClass.backgroundSprite.draw(batch);
        splash.draw(batch);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        sound.play(0.5f);
        batch = new SpriteBatch();
        tweenManager = new TweenManager();

        //Starter tween - med spriteaccessor klassen
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        //Her loader vi intro logo
        Texture splashTexture = new Texture("2d-final.png");
        splash = new Sprite(splashTexture);
        splash.setSize(680,180);
        splash.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-200);



        //Bruges til fade ind og ud på intro logo
        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager); //loader tweenmanager
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1,0.5f).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        }).start(tweenManager); //fade in/out (repeatYoyo fader ud)




    }




    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

       // dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
        ImageFileClass.backgroundSprite.getTexture().dispose();
    }
}
