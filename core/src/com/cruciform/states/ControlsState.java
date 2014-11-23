package com.cruciform.states;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.badlogic.gdx.Gdx;
import com.cruciform.Cruciform;
import com.cruciform.input.StateInputProcessor;
import com.cruciform.utils.Conf;

@NonNullByDefault
public class ControlsState extends State {
	protected final StateInputProcessor processor;
	
	public ControlsState(Cruciform game) {
		super(game);
		processor = new StateInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		manager.batch.begin();
		//super.render(delta);
        
        drawer.drawCentered("Controls", Conf.screenCenterX, Conf.screenHeight*0.7f);
        drawer.drawCentered("Configurability coming soon!", Conf.screenCenterX, Conf.screenHeight*0.6f);
        drawer.drawCentered("Move: Mouse or WASD (but try the mouse please!)", Conf.screenCenterX, Conf.screenHeight*0.55f);
        drawer.drawCentered("Fire cruciform: Left click", Conf.screenCenterX, Conf.screenHeight*0.5f);
        drawer.drawCentered("Fire rockets: Right click", Conf.screenCenterX, Conf.screenHeight*0.45f);
        drawer.drawCentered("Fire rifle: Z", Conf.screenCenterX, Conf.screenHeight*0.4f);
        drawer.drawCentered("Fire sweep: X", Conf.screenCenterX, Conf.screenHeight*0.35f);
        drawer.drawCentered("Focus: Left Shift", Conf.screenCenterX, Conf.screenHeight*0.3f);
		// TODO Make buttons instead
		final String exitmanager = "Exit To Main Menu [ESCAPE]";
		drawer.drawCentered(exitmanager, Conf.screenCenterX, Conf.screenHeight*0.25f);
        manager.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(processor);
		super.show();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
