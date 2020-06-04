package com.screens;
import com.TiledMap.GameMap;
import com.TiledMap.TiledGameMap;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gameObjects.ActorClass;
import com.gameObjects.TextInputField;
import com.gameObjects.WorldGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

//import com.gameObjects.goalClass;

public class GameScreen implements Screen {
    private GameMap gameMap;
    //private TiledGameMap gameMap;
    private TiledMap gm;
    private String tiledGameMap;
    private TextInputField textInputField;
    private TextInputField textInputField1;
    private WorldGenerator worldGenerator;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private ActorClass playerActor;
    private List<ActorClass> goalActorList = new ArrayList<>();
    private static int w = 32;
    private static int bigw = 64;
    private int playerX;
    private int playerY;
    private int level;
    private int amountGoals;
    private boolean lvlBegun;
    private int commandCounter;
    private boolean measure;
    private Dialog dialog;
    Skin uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    BitmapFont font;



    public GameScreen(int playerX, int playerY, int amountGoals, int level, String tiledGameMap) {
        this.playerX = playerX * bigw;
        this.playerY = playerY * bigw;
        this.tiledGameMap = tiledGameMap;
        this.level = level;
        this.amountGoals = amountGoals;
    }


    public ArrayList<String[]> validateInput(String input) {
        ArrayList<String[]> cmds = new ArrayList<>();
        String[] cmdArray = input.split("\\n");
        String[] cmd0, cmd1;

        int repeatEndIndex = 0;
        for (int index = 0; index < cmdArray.length; index++) {
            cmd0 = cmdArray[index].split(" ");
            if (validateCommand(cmd0)) {
                if (cmd0[0].equals("gentag")) {
                    //System.out.println("REPEAT");
                    boolean cmdIsValid = true;
                    for (int subIndex = index + 1; subIndex < cmdArray.length; subIndex++) {
                        cmd1 = cmdArray[subIndex].split(" ");
                        cmdIsValid &= validateCommand(cmd1);
                        if (cmdIsValid) {
                            if (cmd1[0].equals("stop")) {
                                //System.out.println("BREAK");
                                repeatEndIndex = subIndex;
                                break;
                            }
                        } else {
                            cmds.clear();
                            return cmds;
                        }
                    }
                    int amountOfRepeats = Integer.parseInt(cmd0[1]);
                    for (int i = 0; i < amountOfRepeats; i++) {
                        for (int subIndex = index + 1; subIndex < repeatEndIndex; subIndex++) {
                            cmds.add(cmdArray[subIndex].split(" "));
                        }
                    }
                    index += repeatEndIndex;
                } else cmds.add(cmd0);
            } else {
                cmds.clear();
                return cmds;
            }
        }
        return cmds;
    }


    public boolean validateCommand(String[] cmd) {
        commandCounter++;
        if (cmd == null || cmd.length == 0) return false;
        switch (cmd[0]) {
            case "-gå":
                return cmd.length == 2 && cmd[1].matches("^[0-9]*$");
            case "gå":
                return cmd.length == 2 && cmd[1].matches("^[0-9]*$");
            case "drej":
                if (cmd.length != 2) return false;
                return cmd[1].equals("venstre") || cmd[1].equals("højre");
            case "gentag":
                return cmd.length == 2 && cmd[1].matches("^[0-9]*$");
            case "stop":
                return true;
        }
        return false;

    }


    public static SequenceAction parseCommands(String[] input, ActorClass playerObject) {
        String[] cmdSplit = input;
        SequenceAction cmdsToExecute = Actions.sequence();
        switch (cmdSplit[0]) {
            case "-gå":
                float currentRotation1 = playerObject.getRotation();
                int cycles1 = (int) (currentRotation1 / 360);
                currentRotation1 = currentRotation1 - Math.signum(currentRotation1) * 360 * cycles1;
                currentRotation1 = (currentRotation1 + 360) % 360;
                currentRotation1 = Math.round(currentRotation1);
                int value1 = Integer.parseInt(cmdSplit[1]);
                if (currentRotation1 == 0)
                    cmdsToExecute.addAction(Actions.moveBy(-value1 * bigw, 0, (float) value1 / 2.5f));
                if (currentRotation1 == 90)
                    cmdsToExecute.addAction(Actions.moveBy(0, -value1 * bigw, (float) value1 / 2.5f));
                if (currentRotation1 == 180)
                    cmdsToExecute.addAction(Actions.moveBy(value1 * bigw, 0, (float) value1 / 2.5f));
                if (currentRotation1 == 270)
                    cmdsToExecute.addAction(Actions.moveBy(0, value1 * bigw, (float) value1 / 2.5f));
                break;
            case "gå":
                float currentRotation = playerObject.getRotation();
                int cycles = (int) (currentRotation / 360);
                currentRotation = currentRotation - Math.signum(currentRotation) * 360 * cycles;
                currentRotation = (currentRotation + 360) % 360;
                currentRotation = Math.round(currentRotation);
                int value = Integer.parseInt(cmdSplit[1]);
                System.out.println(value);
                //int value = Integer.parseInt(cmdSplit[1]);
                if (currentRotation == 0)
                    cmdsToExecute.addAction(Actions.moveBy(value * bigw, 0, (float) value / 2.5f));
                if (currentRotation == 90)
                    cmdsToExecute.addAction(Actions.moveBy(0, value * bigw, (float) value / 2.5f));
                if (currentRotation == 180)
                    cmdsToExecute.addAction(Actions.moveBy(-value * bigw, 0, (float) value / 2.5f));
                if (currentRotation == 270)
                    cmdsToExecute.addAction(Actions.moveBy(0, -value * bigw, (float) value / 2.5f));
                break;
            case "drej":
                switch (cmdSplit[1]) {
                    case "venstre":
                        cmdsToExecute.addAction(Actions.rotateBy(90, 1));
                        break;
                    case "højre":
                        cmdsToExecute.addAction(Actions.rotateBy(-90, 1));
                        break;
                }
                break;
            //cmdsToExecute.addAction(Actions.delay(1));
        }

        return cmdsToExecute;
    }


    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        FitViewport viewp = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewp, spriteBatch);
        Gdx.graphics.getDisplayMode();
        Gdx.input.setInputProcessor(stage);
        playerActor = new ActorClass("images/player.png", bigw, bigw);
        playerActor.sprite.setOrigin(playerActor.getWidth() / 2, playerActor.getHeight() / 2);
        playerActor.setX(playerX);
        playerActor.setY(playerY);
        gameMap = new TiledGameMap(tiledGameMap);
        textInputField = new TextInputField(640, 0, 240, 60, 240, 640);
        textInputField1 = new TextInputField(640,60,240,60,240,120);
        stage.addActor(gameMap);
        stage.addActor(playerActor);
        stage.addActor(textInputField.textArea);
        stage.addActor(textInputField.textButton);
        //stage.addActor(textInputField1.textArea);
        stage.addActor(textInputField1.textButton);
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(1f));

        textInputField.textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                playerActor.setCommands(validateInput(textInputField.textArea.getText()));
                System.out.println(validateInput(textInputField.textArea.getText()));
                textInputField.textArea.setText("");
                //System.out.println(lvlBegun);
            }
        });
        textInputField1.textButton.setText("Mål!");
        textInputField1.textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                measure = true;
            }
        });

        for (int i = 0; i < amountGoals; i++) {
            goalActorList.add(new ActorClass("images/diamond.png", bigw, bigw));
            stage.addActor(goalActorList.get(i));
        }

        dialog = new Dialog("Level "+level, uiSkin , "Dialog"){
            public void result(Object obj){
                if(obj.equals(1L)){
                    dialog.clear();
                    dialog.hide();

                }
            }
        };


        if(level == 1){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Kan du fange diamanten? \nHINT: Du skal 'gå' 4 skridt!");
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(4*bigw,6*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(4*bigw,6*bigw);
        } else if(level == 2){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Godt klaret! \nDu skal nu bevæge dig længere for at fange begge diamanter! \nBrug måleknappen til at beregne afstand!");
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(4*bigw,8*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(4*bigw,8*bigw);
            goalActorList.get(1).setBounds(4*bigw,6*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(4*bigw,6*bigw);
        }else if(level == 3){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du kan dreje ved at skrive: 'drej' \nDu kan vælge 'højre' eller 'venstre'!");
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(2*bigw,6*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(2*bigw,6*bigw);
            goalActorList.get(1).setBounds(6*bigw,6*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(6*bigw,6*bigw);
        }else if(level == 4){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du klarede det godt! \nDu skal nu dreje til venstre i stedet!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(7*bigw,7*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(7*bigw,7*bigw);
            goalActorList.get(1).setBounds(2*bigw,7*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(2*bigw,7*bigw);
        } else if(level == 5){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Det kører for dig! \nPas på! Der er sten i vejen! \nUndgå stenene, og hent diamanterne!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            goalActorList.get(0).setBounds(1*bigw,1*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(1*bigw,1*bigw);
            goalActorList.get(1).setBounds(4*bigw,2*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(4*bigw,2*bigw);
            goalActorList.get(2).setBounds(6*bigw,4*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(6*bigw,4*bigw);
        }else if(level == 6){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Nu bliver det svært! \nDu skal rundt om lavaen!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            goalActorList.get(0).setBounds(3*bigw,1*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(3*bigw,1*bigw);
            goalActorList.get(1).setBounds(3*bigw,8*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(3*bigw,8*bigw);
            goalActorList.get(2).setBounds(7*bigw,3*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(7*bigw,3*bigw);
        }else if(level == 7){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du er en mester! \nHvis du bruger 'gentag', så kan du skrive meget kode på få linjer! \nDu skal skrive 'gentag' og 2 (hvis du vil gentage 2 gange) \nHer efter skal du skrive det kode du vil køre! \nAfslut ved at skrive stop!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(1*bigw,8*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(1*bigw,8*bigw);
            goalActorList.get(1).setBounds(7*bigw,8*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(7*bigw,8*bigw);
            goalActorList.get(2).setBounds(7*bigw,2*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(7*bigw,2*bigw);
            goalActorList.get(3).setBounds(2*bigw,2*bigw, bigw,bigw);
            goalActorList.get(3).setPosition(2*bigw,2*bigw);
        }else if(level == 8){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du er for sej! \nNu skal du på den store diamant jagt! \nBrug 'gentag' igen!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            goalActorList.get(0).setBounds(bigw,bigw, bigw,bigw);
            goalActorList.get(0).setPosition(bigw,bigw);
            goalActorList.get(1).setBounds(2*bigw,2*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(2*bigw,2*bigw);
            goalActorList.get(2).setBounds(3*bigw,3*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(3*bigw,3*bigw);
            goalActorList.get(3).setBounds(4*bigw,4*bigw, bigw,bigw);
            goalActorList.get(3).setPosition(4*bigw,4*bigw);
            goalActorList.get(4).setBounds(5*bigw,5*bigw, bigw,bigw);
            goalActorList.get(4).setPosition(5*bigw,5*bigw);
            goalActorList.get(5).setBounds(6*bigw,6*bigw, bigw,bigw);
            goalActorList.get(5).setPosition(6*bigw,6*bigw);
            goalActorList.get(6).setBounds(7*bigw,7*bigw, bigw,bigw);
            goalActorList.get(6).setPosition(7*bigw,7*bigw);
            goalActorList.get(7).setBounds(8*bigw,8*bigw, bigw,bigw);
            goalActorList.get(7).setPosition(8*bigw,8*bigw);
        }else if(level == 9){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du er blevet er blevet en ægte programmør! \nSe om du kan klare den her!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            playerActor.rotateBy(90);
            goalActorList.get(0).setBounds(1*bigw,9*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(1*bigw,9*bigw);
            goalActorList.get(1).setBounds(4*bigw,9*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(4*bigw,9*bigw);
            goalActorList.get(2).setBounds(7*bigw,9*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(7*bigw,9*bigw);
        }else if(level == 10){
            dialog.getStyle().titleFont.getData().setScale(0.5f,0.5f);
            dialog.text("Du er på sidste niveau! \nHvis du klare dette niveau - så er du en kode-jedi!");
            dialog.add().row();
            dialog.button("OK!", 1L);
            dialog.show(stage);
            goalActorList.get(0).setBounds(bigw,3*bigw, bigw,bigw);
            goalActorList.get(0).setPosition(bigw,3*bigw);
            goalActorList.get(1).setBounds(bigw,8*bigw, bigw,bigw);
            goalActorList.get(1).setPosition(bigw,8*bigw);
            goalActorList.get(2).setBounds(3*bigw,6*bigw, bigw,bigw);
            goalActorList.get(2).setPosition(3*bigw,6*bigw);
            goalActorList.get(3).setBounds(5*bigw,7*bigw, bigw,bigw);
            goalActorList.get(3).setPosition(5*bigw,7*bigw);
            goalActorList.get(4).setBounds(3*bigw,1*bigw, bigw,bigw);
            goalActorList.get(4).setPosition(3*bigw,1*bigw);
            goalActorList.get(5).setBounds(5*bigw,3*bigw, bigw,bigw);
            goalActorList.get(5).setPosition(5*bigw,3*bigw);
            goalActorList.get(6).setBounds(7*bigw,1*bigw, bigw,bigw);
            goalActorList.get(6).setPosition(7*bigw,1*bigw);
            goalActorList.get(7).setBounds(7*bigw,5*bigw, bigw,bigw);
            goalActorList.get(7).setPosition(7*bigw,5*bigw);
            goalActorList.get(8).setBounds(5*bigw,7*bigw, bigw,bigw);
            goalActorList.get(8).setPosition(5*bigw,7*bigw);
            goalActorList.get(9).setBounds(8*bigw,8*bigw, bigw,bigw);
            goalActorList.get(9).setPosition(8*bigw,8*bigw);
        }


        /*for (ActorClass actorClass : goalActorList) {
            Random r = new Random();
            int tileSize = 32;
            int x = (r.nextInt(5) * tileSize);
            int y = (r.nextInt(5) * tileSize);
            actorClass.setBounds(x, y, actorClass.getWidth(), actorClass.getHeight());
            actorClass.sprite.setPosition(x, y);
        }*/

        /*for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(" , " +  gameMap.getTileTypeByCoordinate(1,j,i));
            }
            System.out.println(" ");
        }*/

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameMap.render(camera);
        gameMap.update(delta);
        camera.update();
        stage.act(delta);
        stage.draw();

        //MEASURE BUTTON
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && measure){
            int clickCounter = 0;
            clickCounter++;
            int firstMouseY = Gdx.input.getY() / 64;
            int firstMouseX = Gdx.input.getX() / 64;
            firstMouseY = Gdx.graphics.getHeight()/64-1 - firstMouseY;
            int playerX = (int)playerActor.getX()/64;
            int playerY = (int)playerActor.getY()/64;
            dialog = new Dialog("Measure", uiSkin , "Dialog"){
                public void result(Object obj){
                }
            };
            dialog.text("Du er her: " + playerX + "," + playerY + " // ");
            dialog.text("Her har du trykket: " + firstMouseX + "," + firstMouseY);
            dialog.button("Ok!", true);
            dialog.show(stage);
            measure = false;
        }

        playerActor.rectangle.set(playerActor.getX(), playerActor.getY(), playerActor.sprite.getWidth() / 2, playerActor.getHeight() / 2);

        if (playerActor.getActions().size > 0) {
            textInputField.textButton.setText("Beregner kode!");
            lvlBegun = true;
        } else {
            textInputField.textButton.setText("Kør koden!");
        }

        if (lvlBegun == true && playerActor.getActions().size <= 0 && amountGoals >= 1){
             worldGenerator = new WorldGenerator(level);
            //System.out.println("RESTART GAME HERE!");
        }


        //COLLISION

       // if(playerActor.getX()/w<20*w && playerActor.getX()/w>w && playerActor.getY()/w<20*w && playerActor.getY()/w>w) {

       if(!gameMap.getTileTypeByCoordinate(1,(int)playerActor.getX()/bigw,(int)playerActor.getY()/bigw).isCollidable()){
            playerActor.cmdList.clear();
            playerActor.cmdListRepeat.clear();
            //cmdsToExecute.reset();
            playerActor.getActions().clear();
            playerActor.setX(9*bigw);
            playerActor.setY(9*bigw);

            System.out.println("COLLIDE");
            dialog = new Dialog("Warning", uiSkin , "Dialog"){
                public void result(Object obj){

                    //System.out.println("result " + obj);
                    if (obj.equals(1L)){
                        goalActorList.clear();
                        worldGenerator = new WorldGenerator(level);
                    }else if (obj.equals(2L)){
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                    }
                }
            };

                dialog.text("Hov! Du ramte forkert - Vil du prøve igen?");
                dialog.button("Ja!", 1L);
                dialog.button("Nej!", 2L);
                dialog.show(stage);

            }


        //SET GOALS
        for (int i = 0; i < amountGoals; i++) {
            goalActorList.get(i).sprite.setOrigin(goalActorList.get(i).getWidth() / 2, goalActorList.get(i).getHeight() / 2);
            //System.out.println(amountGoals);

        }

        //WHEN PLAYER GET GOAL
        for (Iterator<ActorClass> iterator = goalActorList.iterator(); iterator.hasNext(); ) {
            ActorClass actorGoal = iterator.next();
            float goalX = actorGoal.getX();
            float goalY = actorGoal.getY();
            float goalWidth = actorGoal.sprite.getWidth();
            float goalHeight = actorGoal.sprite.getHeight();
            actorGoal.rectangle.set(goalX, goalY, goalWidth, goalHeight);

            if (actorGoal.rectangle.overlaps(playerActor.rectangle)) {
                actorGoal.remove();
                iterator.remove();
                amountGoals = amountGoals - 1;

                dialog = new Dialog("Challenge #"+level+" completed!", uiSkin , "Dialog"){
                    public void result(Object obj){
                        //System.out.println("result " + obj);
                        if (obj.equals(1L)){
                            int newLvl = level + 1;
                            goalActorList.clear();
                            worldGenerator = new WorldGenerator(newLvl);
                        }else if (obj.equals(2L)){
                            goalActorList.clear();
                            worldGenerator = new WorldGenerator(level);
                        } else if (obj.equals(3L)){
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                        }
                    }
                };

                if (amountGoals == 0) {
                    dialog.text("Du klarede det helt perfekt! Vil du fortsætte?");
                    dialog.button("Ja!", 1L);
                    dialog.button("Genstart bane!", 2L);
                    dialog.button("Nej tak!", 3L);
                    dialog.show(stage);
                }



            }
        }

        switch (commandCounter){
            case 2:
                int amountofPoints = 3;
                break;
            case 3:
                amountofPoints = 2;
                break;
            case 4:
                amountofPoints = 1;
                break;
        }

        //System.out.println(amountofPoints);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();


    }
}