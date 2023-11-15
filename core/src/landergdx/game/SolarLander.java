package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	Planet[] solarSystem;
	Rectangle landHitBox;
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
		rend = new ShapeRenderer();
		solarRender = new solarRender(rend);
		landHitBox = new Rectangle(land.pos.x,land.pos.y,32,32);
		//planet creation
		solarSystem = new Planet[1];
		for(int i = 0 ; i < solarSystem.length;i++)
		{
			solarSystem[i] = new Planet(8.3477e11,200,200,60);
			new Circle(solarSystem[i].x,solarSystem[i].y,solarSystem[i].radius);
			solarSystem[i].setHitbox();
		}
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		stage.act(Gdx.graphics.getDeltaTime());
		land.labelUpdate(label);
		stage.draw();
		//draw the planets
		rend.setProjectionMatrix(camera.combined);
		rend.setAutoShapeType(false);
		rend.begin(ShapeRenderer.ShapeType.Filled);
		for(Planet plan : solarSystem)
		{
			rend.setColor(Color.BLUE);
			rend.circle(plan.x, plan.y, plan.radius);
		}
		rend.end();

		rend.begin(ShapeRenderer.ShapeType.Line);
		for(Planet plan: solarSystem)
		{
			solarRender.planetBoxRender(plan.hitBox,plan);	//planet hitboxes
			solarRender.orbitRender(land,plan); //orbit path
		}
		solarRender.landerBoxRender(landHitBox); //lander hitbox
		rend.end();


		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idle, thrusters)).getKeyFrame(stateTime, true);
		land.fly();
		for(Planet plan:solarSystem)
		{
			land.gravFly(plan.Gravity(land), plan);

		}
		land.boundscheck();
		land.hitboxUpdate(landHitBox);
		for(Planet plan:solarSystem)
		{
			land.crashTest(plan.hitBox,landHitBox);
		}
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.end();

	}
	@Override
	public void dispose () {
		batch.dispose();
		landerImg.dispose();
		thrusterSheet.dispose();
		rend.dispose();
		stage.dispose();
	}
}
