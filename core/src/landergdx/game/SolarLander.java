package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	private OrthographicCamera orthoCam;
	//objects
	Lander land = new Lander(1,400,400);
	solarObject[] solarSystem;
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
		Random rand = new Random();

		for(int i = 0 ; i < solarSystem.length;i++)
		{
			int coords = rand.nextInt(800);
			solarSystem[i] = new Planet(8.3477e11,coords,coords,60);
			System.out.println(coords);
		}
		//Camera and Viewports
		orthoCam = new OrthographicCamera();
		extendView = new ExtendViewport(900,900, orthoCam); //Batch viewport
		orthoCam.position.set(land.pos.x,land.pos.y,0);
		orthoCam.update();
		screenView = new ScreenViewport(); //UI viewport

		//UI
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
		ui = new userInterface();

	}
	public void resize(int width, int height)
	{
		extendView.update(width, height,true);
		screenView.update(width, height, true);
		land.Sx = width;
		land.Sy = height;
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		orthoCam.position.set(land.pos.x, land.pos.y, 0);
		orthoCam.update();
		extendView.apply();
		//draw the planets
		rend.setProjectionMatrix(extendView.getCamera().combined);
		rend.setAutoShapeType(false);
		rend.begin(ShapeRenderer.ShapeType.Filled);
		for(solarObject ob : solarSystem)
		{
			rend.setColor(Color.BLUE);
			rend.circle(ob.pos.x, ob.pos.y, ob.radius);
			System.out.println("X: "+ob.pos.x+" Y: "+ob.pos.y);
		}
		rend.end();
		rend.begin(ShapeRenderer.ShapeType.Line);
		for(solarObject ob: solarSystem)
		{
			solarRender.hitBoxRender(ob.hitBox);	//planet hitboxes
			solarRender.orbitRender(land,ob); //orbit path
		}
		solarRender.hitBoxRender(land.hitBox);
		rend.end();
		//textures and actions
		batch.setProjectionMatrix(extendView.getCamera().combined);
		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idle, thrusters)).getKeyFrame(stateTime, true);
		land.fly();
		for(int i =0;i<solarSystem.length;i++)
		{
			solarSystem[i].gravVel((solarSystem[i].Gravity(land)),land);
		}
		for(solarObject ob: solarSystem)
		{
			ob.orbit();
			ob.hitboxUpdate();
		}
		land.hitboxUpdate();
		for(solarObject ob:solarSystem)
		{
			land.crashTest(ob.hitBox,land.hitBox);
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
