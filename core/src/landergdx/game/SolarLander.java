package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
public class SolarLander extends ApplicationAdapter {
	//sprites
	SpriteBatch batch;
	Texture landerImg;
	Texture thrusterSheet;
	//animations
	Animation<TextureRegion> thrusters;
	Animation<TextureRegion> idle;
	float stateTime;;

	//other
	private OrthographicCamera camera;
	Lander land = new Lander();
	Planet moon;
	Rectangle landHitbox;
	Circle moonHitbox;
	ShapeRenderer rend;
	solarRender solarRender;
	@Override
	public void create () {

		//textures
		batch = new SpriteBatch();
		landerImg = new Texture("Lunar Lander.png");
		thrusterSheet = new Texture("Lunar Lander thruster.png");
		//animations
		sprAnim thruster = new sprAnim();
		sprAnim idling = new sprAnim();
		thrusters = thruster.getAnim(1,3,thrusterSheet,.1f);
		idle = idling.getAnim(1,1,landerImg,.1f);
		//other
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		stateTime = 0f;
		moon = new Planet(7.3477e11,200,200, 60);
		landHitbox = new Rectangle(land.pos.x,land.pos.y,32,32);
		moonHitbox = new Circle(moon.x,moon.y,moon.radius);
		rend = new ShapeRenderer();
		solarRender = new solarRender(rend);
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		//draw the moon
		rend.begin(ShapeRenderer.ShapeType.Filled);
		rend.setColor(0,0,1,1);
		rend.circle(moon.x,moon.y,moon.radius);
		rend.end();
		//render object hitboxes
		solarRender.boxRender(landHitbox,moonHitbox,camera);
		//render orbit path
		solarRender.orbitRender(land,camera,moon);

		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idle, thrusters)).getKeyFrame(stateTime, true);
		land.fly();
		land.gravFly(moon.Gravity(moon,land), moon);
		land.boundscheck();
		landHitbox.x = land.pos.x;
		landHitbox.y = land.pos.y;
		land.crashTest(moonHitbox,landHitbox);
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.end();

	}
	@Override
	public void dispose () {
		batch.dispose();
		landerImg.dispose();
		thrusterSheet.dispose();
		rend.dispose();
	}
}
