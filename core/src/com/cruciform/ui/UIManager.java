package com.cruciform.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.cruciform.images.ImageManager;
import com.cruciform.images.NinePatches;
import com.cruciform.utils.Conf.Setting;

public class UIManager {
	public static final TextButtonStyle buttonStyle = new TextButtonStyle();
	public static final LabelStyle labelStyle = new LabelStyle();
	public static final BitmapFont font = new BitmapFont();
	public static final NinePatchDrawable patchUp = 
				new NinePatchDrawable(ImageManager.getPatch(NinePatches.BUTTON_1));
	public static final NinePatchDrawable sliderPatch = 
				new NinePatchDrawable(ImageManager.getPatch(NinePatches.CRUCIFORM_WEAPON_BEAM_HORIZONTAL));
	
	static  {
		buttonStyle.font = font;
		buttonStyle.up = patchUp;
		buttonStyle.down = patchUp; //TODO
	    labelStyle.font = font;
	}

	public static void addSettingSlider(Table table, Setting<Float> setting) {
	    final Label nameLabel = new Label(setting.name, labelStyle);
	    final Label valueLabel = new Label(setting.get().toString(), labelStyle);
	    valueLabel.setAlignment(Align.center);
	    final SliderStyle sliderStyle = new SliderStyle();
	    sliderStyle.background = UIManager.sliderPatch;
	    sliderStyle.knob = UIManager.patchUp;
	    final Slider slider = new Slider(setting.min, setting.max, 0.05f, false, sliderStyle);
	    slider.setValue(setting.get());
	    slider.setSize(500, 8);
	    slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setting.set(slider.getValue());
				valueLabel.setText(String.format("%.2f", setting.get()));
			}
	    });
	    table.add(nameLabel);
	    table.add(slider).minWidth(1000);
	    table.add(valueLabel).minWidth(50);
	    table.row();
	}

	public static interface DropDownSetter {
		public void set(int index);
	}
	
	public static void addDropDown(final Table table, final Array<String> choices,
			final DropDownSetter setter, 
			final String title) {
	    final Label titleLabel = new Label(title, labelStyle);
	    final SelectBoxStyle style = new SelectBoxStyle();
	    style.font = font;
	    style.scrollStyle = new ScrollPaneStyle();
	    style.listStyle = new ListStyle();
	    style.listStyle.selection = sliderPatch;
	    style.listStyle.font = font;
	    final SelectBox<String> dropDown = new SelectBox<String>(style);
	    dropDown.setItems(choices);
	    dropDown.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		final int index = dropDown.getSelectedIndex();
	    		if (index != -1) {
	    			setter.set(index);
	    		}
	    	}
	    });
	    //table.add(titleLabel);
	    table.add(dropDown);
	}
}
