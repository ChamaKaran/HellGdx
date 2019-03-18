package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Texture img1;
	private BitmapFont myBitmapFont;
	private Music musicBackbround;
	private int intX;
	private com.badlogic.gdx.math.Rectangle dogRectangle,chickenRectangle;
	private OrthographicCamera orthographicCamery;
	private Vector3 objVertor3;
	private Array<com.badlogic.gdx.math.Rectangle> objCoinsDrop;
	private long lastDropTime;
	private Iterator<com.badlogic.gdx.math.Rectangle> objIterator;
	private Sound soundSuccess , soundfalse;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//Set Img
		img = new Texture("dog.png");
		img1 = new Texture("chik.png");

		orthographicCamery = new OrthographicCamera();
		orthographicCamery.setToOrtho(false,800,480);
		batch = new SpriteBatch();

		dogRectangle = new com.badlogic.gdx.math.Rectangle();
		dogRectangle.x = 368;
		dogRectangle.y = 20;
		dogRectangle.width = 64;
		dogRectangle.height = 64;

		myBitmapFont = new BitmapFont();
		myBitmapFont.setColor(Color.BLACK);

		objCoinsDrop = new Array<com.badlogic.gdx.math.Rectangle>();
		gameCoinsDrop();

		//set Sound
		musicBackbround = Gdx.audio.newMusic(Gdx.files.internal("rain.wav"));
		soundSuccess = Gdx.audio.newSound(Gdx.files.internal("kick.wav"));
		soundfalse = Gdx.audio.newSound(Gdx.files.internal("pan.wav"));

		musicBackbround.setLooping(true);
		musicBackbround.play();

	}
	private void gameCoinsDrop() {

		chickenRectangle = new com.badlogic.gdx.math.Rectangle();
		chickenRectangle.x = MathUtils.random(0,736);
		chickenRectangle.y = 480;
		chickenRectangle.width = 64;
		chickenRectangle.height = 64;
		objCoinsDrop.add(chickenRectangle);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		orthographicCamery.update();

		batch.setProjectionMatrix(orthographicCamery.combined);

		batch.begin();
		batch.draw(img, dogRectangle.x, dogRectangle.y);

		for(com.badlogic.gdx.math.Rectangle forRectangle: objCoinsDrop){
			batch.draw(img1,forRectangle.x,forRectangle.y);
		}
		batch.end();

		if (Gdx.input.isTouched()){
			objVertor3 = new Vector3();
			objVertor3.set(Gdx.input.getX(),Gdx.input.getY(),0);
			orthographicCamery.unproject(objVertor3);
			dogRectangle.x = objVertor3.x - 32;

		}
		if (dogRectangle.x < 0){
			dogRectangle.x = 0;
		}
		if (dogRectangle.x > 736) {
			dogRectangle.x = 736;
		}
		if(TimeUtils.nanoTime() - lastDropTime > 1E9){
			gameCoinsDrop();
		}
		objIterator = objCoinsDrop.iterator();
		while (objIterator.hasNext()){
			Rectangle objMyCoins = objIterator.next();
			objMyCoins.y -= 200 * Gdx.graphics.getDeltaTime();
			if(objMyCoins.y + 64 < 0 ){
				objIterator.remove();
				soundfalse.play();
			}
			if(objMyCoins.overlaps(dogRectangle)){
				soundSuccess.play();
				objIterator.remove();
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}