package com.cruciform.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
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

	public static void addSettingSlider(Table table, Setting<Float> setting, boolean squared) {
		final String FORMAT_FLOAT = "%.2f";
		float initialValue;
		if (squared) {
	    	initialValue = (float) Math.sqrt(setting.get()); 
	    } else {
	    	initialValue = setting.get();
	    }
	    final Label nameLabel = new Label(setting.name, labelStyle);
	    final Label valueLabel = new Label(String.format(FORMAT_FLOAT, initialValue), labelStyle);
	    valueLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
	    final SliderStyle sliderStyle = new SliderStyle();
	    sliderStyle.background = UIManager.sliderPatch;
	    sliderStyle.knob = UIManager.patchUp;
	    assert setting.hasMinMax();
	    final Slider slider = new Slider(setting.getMin(), setting.getMax(), 0.05f, false, sliderStyle);
	    slider.setValue(initialValue); 
	    slider.setSize(500, 8);
	    slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (squared) {
					setting.set(slider.getValue()*slider.getValue());
				} else {
					setting.set(slider.getValue());
				}
				valueLabel.setText(String.format(FORMAT_FLOAT, slider.getValue()));
			}
	    });
	    table.add(nameLabel);
	    table.add(slider).minWidth(1000);
	    table.add(valueLabel).minWidth(50);
	}

	public static interface DropDownSetter {
		public void set(int index);
	}
	
	public static void addDropDown(final Table table, final Array<String> choices,
			final DropDownSetter setter, final int currentIndex,
			final String title, float leftPad) {
	    final Label titleLabel = new Label(title, labelStyle);
	    final SelectBoxStyle style = new SelectBoxStyle();
	    style.font = font;
	    style.scrollStyle = new ScrollPaneStyle();
	    style.listStyle = new ListStyle();
	    style.listStyle.selection = sliderPatch;
	    style.listStyle.font = font;
	    final SelectBox<String> dropDown = new SelectBox<String>(style);
	    dropDown.setItems(choices);
	    dropDown.setSelectedIndex(currentIndex);
	    dropDown.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
	    		final int index = dropDown.getSelectedIndex();
	    		if (index != -1 && index < choices.size) {
	    			setter.set(index);
	    		}
			}
	    });
	    table.add(titleLabel);
	    table.row().padLeft(leftPad);
	    table.add(dropDown);
	}
	
	public static void addCheckBox(final Table table, final Setting<Boolean> setting) {
		final CheckBoxStyle style = new CheckBoxStyle();
		style.checkboxOff = sliderPatch;
		style.checkboxOn = patchUp;
		style.font = font;
		final CheckBox checkBox = new CheckBox(setting.name, style);
		checkBox.setChecked(setting.get());
		checkBox.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		setting.set(checkBox.isChecked());
	    	}
			
		});
		table.add(checkBox);
	}
}
