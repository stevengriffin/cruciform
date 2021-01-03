package com.cruciform.tweening;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.math.Vector2;

public class VectorAccessor implements TweenAccessor<Vector2> {

    public static final int VECTOR_XY = 1;
    public static final int VECTOR_X = 2;
    public static final int VECTOR_Y = 3;

    @Override
    public int getValues(Vector2 target, int tweenType, float[] returnValues) {
    	switch(tweenType) {
    	case(VECTOR_XY):
    		returnValues[0] = target.x;
    		returnValues[1] = target.y;
    		return 2;
    	case(VECTOR_X):
    		returnValues[0] = target.x;
    		return 1;
    	case(VECTOR_Y):
    		returnValues[0] = target.y;
    		return 1;
    	default:
    		assert false; return -1;
    	}
    }
    
    @Override
    public void setValues(Vector2 target, int tweenType, float[] newValues) {
    	switch(tweenType) {
    	case(VECTOR_XY):
    		target.set(newValues[0], newValues[1]);
    		return;
    	case(VECTOR_X):
    		target.x = newValues[0];
    		return;
    	case(VECTOR_Y):
    		target.y = newValues[0];
    		return;
    	default:
    		assert false;
    		return;
    	}
    }

}
