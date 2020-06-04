package com.com.jesper.jespil.Accessors;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ActorAccessor implements TweenAccessor<Actor> {

    public static final int Y = 0, X = 1, RGB = 2, ALPHA = 3;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch(tweenType){

            case X:
                returnValues[0] = target.getX();
                return 1;
            case Y:
                returnValues[0] = target.getY();
                return 1;
            case RGB:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                return 3;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
                default:
                    assert false;
                    return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType){
            case Y:
                target.setY(newValues[0]);
                break;
            case X:
                target.setX(newValues[0]);
                break;
            case RGB:
                target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
                break;
            case ALPHA:
                target.setColor(target.getColor().r,target.getColor().g, target.getColor().b, newValues[0]);
                break;
                default:
                    assert false;
        }

    }
}
