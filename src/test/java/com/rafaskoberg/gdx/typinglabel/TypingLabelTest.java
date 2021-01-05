package com.rafaskoberg.gdx.typinglabel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TypingLabelTest extends ApplicationAdapter {
	Skin skin;
	SpriteBatch batch;
	TypingLabel label;
	Viewport viewport = new ScreenViewport();

	@Override
	public void create() {
		adjustTypingConfigs();

		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin.getAtlas().getTextures().iterator().next().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		float scale = 1;
		skin.getFont("default-font").getData().setScale(scale);

		label = createTypingLabel();
	}

	public void adjustTypingConfigs() {
		// Only allow two chars per frame
		TypingConfig.CHAR_LIMIT_PER_FRAME = 2;

		// Change color used by CLEARCOLOR token
		TypingConfig.DEFAULT_CLEAR_COLOR = Color.WHITE;

		// Force bitmap fonts to use color markup
		TypingConfig.FORCE_COLOR_MARKUP_BY_DEFAULT = true;

		// Create some global variables to handle style
		TypingConfig.GLOBAL_VARS.put("FIRE_WIND", "{FASTER}{GRADIENT=ORANGE;DB6600;-0.5;5}{SLOWER}{WIND=2;4;0.5;0.5}");
	}

	public TypingLabel createTypingLabel() {
		// Create text with tokens
		final StringBuilder text = new StringBuilder();
		text.append("{WAIT=1}{SLOWER}{GRADIENT=FF70F1;FFC300;-0.5;5}{EASE=-8;2;1}Welcome,{WAIT} {VAR=title}!{ENDEASE}");
		text.append("{FAST}\n\n");
		text.append("{RESET}{HANG=0.7}This is a simple test{ENDHANG} to show you");
		text.append("{GRADIENT=27C1F5;2776E7;-0.5;5} {JUMP}how to make dialogues {SLOW}fun again! {ENDJUMP}{WAIT}{ENDGRADIENT}\n");
		text.append("{NORMAL}{CLEARCOLOR}{SICK} With this library{ENDSICK} you can control the flow of the text with");
		text.append(" {BLINK=FF6BF3;FF0582;3}tokens{ENDBLINK},{WAIT=0.7}");
		text.append("{SPEED=2.50}{COLOR=#84DD60} making the text go {SHAKE=1;1;3}really fast{ENDSHAKE}{WAIT=0.5} ");
		text.append("{SPEED=0.25}{COLOR=#A6E22D}{WAVE=0.66}or extremely slow.{ENDWAVE}");
		text.append("{RESET} You can also wait for a {EASE=-15;2;1}second{ENDEASE}{WAIT=1} {EASE=15;8;1}{COLOR=#E6DB74}or two{CLEARCOLOR}{ENDEASE}{WAIT=2},");
		text.append("{RAINBOW=1;1;0.7} just to catch an event in code{EVENT=example}!{WAIT} {ENDHANG}{ENDRAINBOW}");
		text.append("{NORMAL}\n\n");
		text.append("{VAR=FIRE_WIND}Imagine the possibilities! =D {RESET}\n");

		// Create label
		final TypingLabel label = new TypingLabel(text, skin.getFont("default-font"), Color.WHITE);
		label.setDefaultToken("{EASE}{FADE=0;1;0.33}");

		// Make the label wrap to new lines, respecting the table's layout.
		label.setWrap(true);
		label.setWidth(650);

		// Set variable replacements for the {VAR} token
		label.setVariable("title", "curious human");

		// Finally parse tokens in the label text.
		label.parseTokens();

		return label;
	}

	public void update(float delta) {
		label.update(delta);
	}

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		label.draw(batch, 30, 160, Color.WHITE);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		skin.dispose();
	}

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "TypingLabel Test";
		config.width = 720;
		config.height = 405;
		config.depth = 16;
		config.fullscreen = false;
		config.resizable = false;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;

		new LwjglApplication(new TypingLabelTest(), config);
	}

}
