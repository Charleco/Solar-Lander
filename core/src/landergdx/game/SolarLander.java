package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SolarLander extends ApplicationAdapter {
	//sprites
	SpriteBatch batch;
	Texture landerImg;
	Texture thrusterSheet;
	//animations
	Animation<TextureRegion> thrusters;
	Animation<TextureRegion> idle;
	float stateTime;
	//UI
	private Stage stage;
	private Table table;
	private Table root;
	private Label velocityLabel;
	private Label debugLabel;
	private BitmapFont labelFont;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	private userInterface ui;
	private ExtendViewport extendView;
	private ScreenViewport screenView;
	//objects
	Lander land = new Lander();
	Planet[] solarSystem;
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
		stateTime = 0f;
		rend = new ShapeRenderer();
		solarRender = new solarRender(rend);
		//planet creation
		solarSystem = new Planet[1];
		for(int i = 0 ; i < solarSystem.length;i++)
		{
			solarSystem[i] = new Planet(8.3477e11,200,200,60);
			new Circle(solarSystem[i].x,solarSystem[i].y,solarSystem[i].radius);
			solarSystem[i].setHitbox();
		}

		//UI
		extendView = new ExtendViewport(900,900); //Batch viewport
		extendView.getCamera().position.set(800, 400, 0);
		screenView = new ScreenViewport(); //UI viewport

		stage = new Stage(screenView, batch);
		Gdx.input.setInputProcessor(stage);

		root = new Table();
		root.setFillParent(true);
		root.pad(10);
		stage.addActor(root);

		table = new Table();
		table.setFillParent(false);
		table.defaults().space(10);
		root.add(table).growX().expandY().top();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 15;
		labelFont = generator.generateFont(parameter);
		Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);
		velocityLabel = new Label("test",labelStyle);
		debugLabel = new Label("test",labelStyle);
		table.add(velocityLabel).growX().space(10).padLeft(5);
		table.row();
		table.add(debugLabel).growX().space(10).padLeft(5);
		debugLabel.setWrap(true);
		velocityLabel.setWrap(true);

		//table.setDebug(true);
		//debugLabel.setDebug(true);
		//velocityLabel.setDebug(true);
		ui = new userInterface();

	}
	public void resize(int width, int height)
	{
		extendView.update(width, height,true);
		screenView.update(width, height, true);

		land.x = width;
		land.y = height;
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		extendView.apply();
		//draw the planets
		rend.setProjectionMatrix(extendView.getCamera().combined);
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
		solarRender.landerBoxRender(land.landHitBox); //lander hitbox
		rend.end();

		//ui debug render
		rend.setAutoShapeType(true);
		rend.begin();
		table.drawDebug(rend);
		rend.end();
		//textures and actions
		batch.setProjectionMatrix(extendView.getCamera().combined);
		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idle, thrusters)).getKeyFrame(stateTime, true);
		land.fly();
		for(Planet plan:solarSystem)
		{
			land.gravFly(plan.Gravity(land), plan);

		}
		land.boundscheck();
		land.hitboxUpdate(land.landHitBox);
		for(Planet plan:solarSystem)
		{
			land.crashTest(plan.hitBox,land.landHitBox);
		}
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.end();
		//UI Stage
		screenView.apply();
		stage.act(Gdx.graphics.getDeltaTime());
		ui.velLabelUpdate(velocityLabel,land);
		ui.debugUpdate(debugLabel);
		stage.draw();
	}
	@Override
	public void dispose () {
		batch.dispose();
		landerImg.dispose();
		thrusterSheet.dispose();
		rend.dispose();
		stage.dispose();
		generator.dispose();
	}
}
