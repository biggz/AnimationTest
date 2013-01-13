package com.bigerstaff.animationtest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class AnimationTest implements ApplicationListener {
	OrthographicCamera camera;
	AssetManager manager;
	Texture rightTexture, leftTexture;
	TextureRegion right1, right2, left1, left2;
	SpriteBatch batch;
	Rectangle player;
	Animation rightAnimation, leftAnimation;
	float stateTime;
	boolean facingRight;
	
	
	@Override
	public void create() {		
		//Create Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		//Create Asset Manager
		AssetManager manager = new AssetManager();
		//Load Assets
		manager.load("data/mario_right.png", Texture.class);
		manager.load("data/mario_left.png", Texture.class);
		//Create Animation Frames, there must be a better way than this.
		while (manager.update() == false) {
			Gdx.app.log("Loading", "Loading from asset manager");
		}
		Gdx.app.log("Loading", "Done!");
		Texture rightTexture = manager.get("data/mario_right.png", Texture.class);
		Texture leftTexture = manager.get("data/mario_left.png", Texture.class);
	
		right1 = new TextureRegion(rightTexture, 0,0, 15,27);
		right2 = new TextureRegion(rightTexture, 16,0, 15,27);
		left1 = new TextureRegion(leftTexture, 0,0, 15,27);
		left2 = new TextureRegion(leftTexture, 16,0, 15,27);
		//Create Animation
		rightAnimation = new Animation(0.2f, right1, right2);
		leftAnimation = new Animation(0.2f, left1, left2);
		//Start player facing right
		facingRight = true;
		//Temp State timer
		stateTime = 0f;
		
		//Create Sprite Batch
		batch = new SpriteBatch();
		//Create player and set initial position
		player = new Rectangle();		
		player.x = 1280 / 2 -  16 / 2;
		player.y = 20;
		player.width = 16;
		player.height = 28;
	}

	@Override
	public void dispose() {
		manager.dispose();
		batch.dispose();

	}

	@Override
	public void render() {		
		stateTime += Gdx.graphics.getDeltaTime(); 
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); 
		camera.update();      
			      
		batch.setProjectionMatrix(camera.combined);
		batch.begin();      
		//Is left or right pressed, and show corresponding animation
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			batch.draw(leftAnimation.getKeyFrame(stateTime, true), player.x, player.y);
			player.x -= 200 * Gdx.graphics.getDeltaTime();
			facingRight = false;
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			batch.draw(rightAnimation.getKeyFrame(stateTime, true), player.x, player.y);
			player.x += 200 * Gdx.graphics.getDeltaTime();
			facingRight = true;
		}
		else { //No key pressed
			if (facingRight){
				batch.draw(right1,player.x, player.y);
			}
			else {
				batch.draw(left1,player.x, player.y);
			}
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
