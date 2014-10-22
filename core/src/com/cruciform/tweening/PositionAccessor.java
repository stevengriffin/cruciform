package com.cruciform.tweening;

import aurelienribon.tweenengine.TweenAccessor;

import com.cruciform.components.Position;

public class PositionAccessor implements TweenAccessor<Position> {

    public static final int POSITION_XY = 1;
    public static final int POSITION_X = 2;
    public static final int POSITION_Y = 3;

    @Override
    public int getValues(Position target, int tweenType, float[] returnValues) {
    	switch(tweenType) {
    	case(POSITION_XY):
    		returnValues[0] = target.bounds.getX();
    		returnValues[1] = target.bounds.getY();
    		return 2;
    	case(POSITION_X):
    		returnValues[0] = target.bounds.getX();
    		return 1;
    	case(POSITION_Y):
    		returnValues[0] = target.bounds.getY();
    		return 1;
    	default:
    		assert false; return -1;
    	}
    }
    
    @Override
    public void setValues(Position target, int tweenType, float[] newValues) {
    	switch(tweenType) {
    	case(POSITION_XY):
    		target.bounds.setPosition(newValues[0], newValues[1]);
    		return;
    	case(POSITION_X):
    		target.bounds.setPosition(newValues[0], target.bounds.getY());
    		return;
    	case(POSITION_Y):
    		target.bounds.setPosition(target.bounds.getX(), newValues[0]);
    		return;
    	default:
    		assert false;
    		return;
    	}
    }

}
