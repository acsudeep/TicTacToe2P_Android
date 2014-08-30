package com.acharya.tictactoe;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;

import com.acharya.tictactoe.TicTacToeModel.Piece;

//import android.util.Log;
//import android.widget.Toast; // For clicked it uses toast

public class TicTacToeActivity extends SimpleBaseGameActivity implements
		OnClickListener {

	final private int CAMERA_WIDTH = 480;
	final private int CAMERA_HEIGHT = 720;
	
	final private int GRID_WIDTH = 3;
	final private int GRID_HEIGHT = 3;
	
	final private int STROKE_WIDTH = 4;
	
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mBlankTextureRegion;
	private ITextureRegion mXTextureRegion;
	private ITextureRegion mOTextureRegion;
		
	private ButtonSprite[][] gridSprite = new ButtonSprite[GRID_WIDTH][GRID_HEIGHT];
	
	private TicTacToeModel board = new TicTacToeModel();
	private Piece currentPlayer = board.getCurrentPlayer();
	
	public int count = 0;
	
	public int xS = 0;
	public int oS = 0;
	public int dS = 0;
	
	private Font mFont;
	private Font m2Font;
	Text xWin;
	Text oWin;
	Text drawWin;
	Text whichTurn;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		this.m2Font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 72);
		this.m2Font.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 512);
		this.mBlankTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, "blankIcon.png");
		this.mXTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset
				(this.mBitmapTextureAtlas, this, "xIcon.png");
		this.mOTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset
				(this.mBitmapTextureAtlas, this, "oIcon.png");
		
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(0, 0, 0));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

	}

	@Override
	protected Scene onCreateScene() {
		
		final Scene scene = new Scene();
		final VertexBufferObjectManager VBOManager = this.getVertexBufferObjectManager();
		
		float lineX[] = new float[GRID_WIDTH];
		float lineY[] = new float[GRID_HEIGHT];
		
		float touchX[] = new float[GRID_WIDTH];
		float touchY[] = new float[GRID_HEIGHT];
		
		float midTouchX = (CAMERA_WIDTH) / GRID_WIDTH / 2;
		float midTouchY = (CAMERA_HEIGHT - 200) / GRID_HEIGHT / 2;
		
		float halfTouchX = mBlankTextureRegion.getWidth() / 2;
		float halfTouchY = mBlankTextureRegion.getHeight() / 2;
		
		float paddingX = midTouchX - halfTouchX;
		float paddingY = midTouchY - halfTouchY;
	
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		for(int i=0; i<GRID_WIDTH; i++) {
			lineX[i] = (CAMERA_WIDTH) / GRID_WIDTH * i;
			touchX[i] = lineX[i] + paddingX;
		}
		
		for(int i=0; i<GRID_HEIGHT; i++) {
			lineY[i] = (CAMERA_HEIGHT-200) / GRID_HEIGHT * i;
			touchY[i] = lineY[i] + paddingY;
		}
		
		// Draw Grid Lines
		for(int i=1; i < GRID_WIDTH; i++) {
			final Line line = new Line(lineX[i], 0, lineX[i], CAMERA_HEIGHT-200, STROKE_WIDTH, VBOManager);
			line.setColor(0.15f, 0.15f, 0.15f);
			scene.attachChild(line);
		}
		
		for(int i=1; i < GRID_HEIGHT; i++) {
			final Line line = new Line(0, lineY[i], CAMERA_WIDTH, lineY[i], 
					STROKE_WIDTH, VBOManager);
			line.setColor(0.15f, 0.15f, 0.15f);
			scene.attachChild(line);
		} 
		
		// Lay out the ButtonSprites
		for(int i = 0; i < GRID_WIDTH; i++) { 
			for(int j=0; j<GRID_HEIGHT; j++) {
				final ButtonSprite button = new ButtonSprite(touchX[i], 
						touchY[j], this.mBlankTextureRegion, this.mXTextureRegion, this.mOTextureRegion, VBOManager, this);
				scene.registerTouchArea(button);
				scene.attachChild(button);
				gridSprite[i][j] = button;
			}
		}
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		xWin = new Text(10, 570, this.mFont, "X - 0", vertexBufferObjectManager);
		oWin = new Text(10, 620, this.mFont, "O - 0", vertexBufferObjectManager);
		drawWin = new Text(10, 670, this.mFont, "Draw - 0", vertexBufferObjectManager);
		whichTurn = new Text(290, 600, this.m2Font, "X", vertexBufferObjectManager);
				
		scene.attachChild(xWin);
		scene.attachChild(oWin);
		scene.attachChild(drawWin);
		scene.attachChild(whichTurn);
		
		return scene;
	}	
	
	@Override
	public void  onBackPressed() {
		
	}
	
	@Override
	public void onClick(final ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				Toast.makeText(TicTacToeActivity.this, "Clicked", Toast.LENGTH_LONG).show();
//				The above line shows the clicked when we click the area so now we disable it
				// Determine which button was pressed
				float x = pButtonSprite.getX();
				float y = pButtonSprite.getY();
				
				int gridX = (int) Math.floor(x / (CAMERA_WIDTH) * GRID_WIDTH);
				int gridY = (int) Math.floor(y / (CAMERA_HEIGHT-200) * GRID_HEIGHT);
				
				if(gridSprite[gridX][gridY] == pButtonSprite &&
					currentPlayer == board.getCurrentPlayer()) {
					// we're okay to update the model
					
					// Update the Data Model
					board.setValue(gridX, gridY, currentPlayer);
					
					// Disable the button
					pButtonSprite.setEnabled(false);
					
					if (currentPlayer == Piece.X) {
						// change the sprite to X
						pButtonSprite.setCurrentTileIndex(1);
					} else {
						// change the sprite to Y
						pButtonSprite.setCurrentTileIndex(2);
					}
					

					// Check If there's a Winner
					count += 1;
					Piece winner = board.checkWinner();
					if(winner != Piece._) {
//						Toast.makeText(TicTacToeActivity.this, "WinnerNotEqualUS", Toast.LENGTH_LONG).show();
						// Pop Up a Dialog if There is a Winner
						
						if(winner == Piece.O) {
							oS++;
							oWin.setText("O - " + oS);
						} else if(winner == Piece.X) {
							xS++;
							xWin.setText("X - " + xS);
						}
						AlertDialog.Builder builder = new AlertDialog.Builder(TicTacToeActivity.this);
						builder.setMessage(winner + " wins!")
						       .setCancelable(false)
						       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   reset();
										board.reset();
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
					} else if(winner == Piece._ && count == 9) {
						AlertDialog.Builder builder = new AlertDialog.Builder(TicTacToeActivity.this);
						builder.setMessage("This is draw!")
						       .setCancelable(false)
						       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   reset();
										board.reset();
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
						
						dS ++;
						drawWin.setText("Draw - " + dS);
					}
										
					currentPlayer = board.getCurrentPlayer();
					
					if(currentPlayer == Piece.X) {
						whichTurn.setText("X");
					}
					else if (currentPlayer == Piece.O) {
						whichTurn.setText("O");
					}
					
				}
				
			}
		});

	}

	public void reset() {
		for(int i = 0; i < GRID_WIDTH; i++) { 
			for(int j=0; j<GRID_HEIGHT; j++) {
				gridSprite[i][j].setEnabled(true);
				gridSprite[i][j].setCurrentTileIndex(0);
			}
		}
		count = 0;
	}
	

	
}
