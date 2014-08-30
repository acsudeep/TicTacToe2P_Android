package com.acharya.tictactoe;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.graphics.Typeface;

public class AboutActivity extends SimpleBaseGameActivity {
	final private int CAMERA_WIDTH = 480;
	final private int CAMERA_HEIGHT = 720;
	
	private Font mFont;
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 24);
		this.mFont.load();

	}

	@Override
	protected Scene onCreateScene() {
		final Scene scene = new Scene();
		
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		Text aboutText = new Text(140, 290, this.mFont, "TicTacToe 2p\nby Sudeep Acharya\nTwitter @acsudeep", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
		
		scene.attachChild(aboutText);
		return scene;
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}	

}
