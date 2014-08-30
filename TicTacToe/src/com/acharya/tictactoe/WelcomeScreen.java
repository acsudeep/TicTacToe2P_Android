package com.acharya.tictactoe;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import android.content.Intent;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */


public class WelcomeScreen extends SimpleBaseGameActivity implements OnClickListener {
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;

	private ITexture mTexture;
	private ITextureRegion mFaceTextureRegion;
	
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFace1TextureRegion;
	private ITextureRegion mTextureRegion2;
	
	public final int playButtonY = 548;
	public final int aboutButtonY = 623;

@Override
public EngineOptions onCreateEngineOptions() {
	final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

	return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
}

@Override
protected void onCreateResources() {
	try {
		this.mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			@Override
			public InputStream open() throws IOException {
				return getAssets().open("gfx/logo.png");
			}
		});

		this.mTexture.load();
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture);
	} catch (IOException e) {
		Debug.e(e);
	}
	
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

	this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512);
	this.mFace1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "play.png");
	this.mTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "about.png");
	
	try {
		this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		this.mBitmapTextureAtlas.load();
	} catch (TextureAtlasBuilderException e) {
		Debug.e(e);
	}

}

@Override
protected Scene onCreateScene() {
	this.mEngine.registerUpdateHandler(new FPSLogger());
	
	final Scene scene = new Scene();
	scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

	/* Calculate the coordinates for the face, so its centered on the camera. */
	final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
	final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 5;

	/* Create the face and add it to the scene. */
	final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
	scene.attachChild(face);
	
	
	/* Create the button and add it to the scene. */
	final Sprite playButton = new ButtonSprite(centerX+70, playButtonY, this.mFace1TextureRegion, this.getVertexBufferObjectManager(), this);
	final Sprite aboutButton = new ButtonSprite(centerX+70, aboutButtonY, this.mTextureRegion2, this.getVertexBufferObjectManager(), this);
	
	scene.registerTouchArea(playButton);
	scene.registerTouchArea(aboutButton);
	
	scene.attachChild(playButton);
	scene.attachChild(aboutButton);
	
	return scene;
}

@Override
public void onClick(final ButtonSprite pButtonSprite, float pTouchAreaLocalX,
		float pTouchAreaLocalY) {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
			if(pButtonSprite.getY() == playButtonY) {
				WelcomeScreen.this.startActivity(new Intent(WelcomeScreen.this, TicTacToeActivity.class));
				WelcomeScreen.this.finish();
			} else {
				WelcomeScreen.this.startActivity(new Intent(WelcomeScreen.this, AboutActivity.class));
			}
			
		}
	});
	
}


@Override
public void  onBackPressed() {
	
}

}
