package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.ScreenUtils;
import java.lang.Math;
public class SolarLander extends ApplicationAdapter {
	//sprites
	SpriteBatch batch;
	Texture landerimg;
	Texture thrustersheet;
	Texture moonimg;
	//animations
	Animation<TextureRegion> thrusters;
	Animation<TextureRegion> idle;
	float stateTime;

	//other
	private OrthographicCamera camera;
	Lander land = new Lander();
	Planet moon;


	@Override
	public void create () {
		//textures
		batch = new SpriteBatch();
		landerimg = new Texture("Lunar Lander.png");
		thrustersheet = new Texture("Lunar Lander thruster.png");
		moonimg = new Texture("moon.png");
		//animations
		sprAnim thruster = new sprAnim();
		sprAnim idling = new sprAnim();
		thrusters = thruster.getAnim(1,3,thrustersheet,.1f);
		idle = idling.getAnim(1,1,landerimg,.1f);
		//other
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		stateTime = 0f;
		moon = new Planet(7.3477e10,50,50, 13);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idle, thrusters)).getKeyFrame(stateTime, true);
		land.fly();
		land.Gravfly(moon.Gravity(moon,land), moon);
		System.out.println("x: "+land.pos.x+" y: "+land.pos.y+" grav: "+moon.Gravity(moon,land));
		land.boundscheck();
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.draw(moonimg, moon.x,moon.y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		landerimg.dispose();
		thrustersheet.dispose();
	}
}
