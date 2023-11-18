package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SolarLander extends ApplicationAdapter {
	//sprites
	SpriteBatch batch;
	Texture landerImg;
	Texture thrusterSheet;
	//animations
	sprAnim thrusters;
	sprAnim idling;
	float stateTime;
	//UI
	private Stage stage;
	private Label velocityLabel;
	private Label debugLabel;
	private FreeTypeFontGenerator generator;
	private userInterface ui;
	private ExtendViewport extendView;
	private ScreenViewport screenView;
	private OrthographicCamera extendCam;
	private FitViewport miniView;
	private Lander land;
	//objects
	solarObject[] solarSystem;
	ArrayList<Label> solarLabels;
	ShapeRenderer rend;
	solarRender solarRender;
	@Override
	public void create () {
		stateTime = 0f;
		this.setUpObjects(); //Lander/Sprites
		this.generateSystem(); //Planets
		this.createUi();
	}
	public void resize(int width, int height)
	{
		extendView.update(width, height,true);
		screenView.update(width, height, true);
		miniView.setScreenBounds((Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/4)),((Gdx.graphics.getHeight()/96)), Gdx.graphics.getWidth()/4,Gdx.graphics.getWidth()/4);
		land.Sx = width;
		land.Sy = height;
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
		this.extendRender();
		this.uiRender();
		this.miniRender();
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
	//////////////////////////////
	/////// CREATE FUNCTIONS /////
	//////////////////////////////
	public void createUi()
	{
		extendCam = new OrthographicCamera();
		extendView = new ExtendViewport(900,900, extendCam); //Batch/World
		extendCam.position.set(land.pos.x,land.pos.y,0);
		extendCam.update();
		screenView = new ScreenViewport(); //UI viewport
		OrthographicCamera miniCam = new OrthographicCamera();
		miniView = new FitViewport(5000,5000, miniCam); //Minimap
		miniCam.position.set(miniView.getWorldWidth()/2,miniView.getWorldHeight()/2,0);
		miniCam.update();

		stage = new Stage(screenView, batch);
		Gdx.input.setInputProcessor(stage);
		ui = new userInterface();

		Table root = new Table();
		root.setFillParent(true);
		root.pad(10);
		stage.addActor(root);

		Table table = new Table();
		table.setFillParent(false);
		table.defaults().space(10);
		root.add(table).growX().expandY().top();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 15;
		BitmapFont labelFont = generator.generateFont(parameter);
		Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);

		velocityLabel = new Label("",labelStyle);
		debugLabel = new Label("",labelStyle);
		table.add(velocityLabel).growX().space(10).padLeft(5).uniform();
		solarLabels = new ArrayList<>();
		table.row();
		table.add(debugLabel).growX().space(10).padLeft(5).uniform();

		for(int i=0;i<solarSystem.length;i++) {
			table.row();
			Label obLabel = new Label("test",labelStyle);
			solarLabels.add(obLabel);
			table.add(obLabel).growX().space(10).padLeft(5).uniform();
		}
	}
	public void generateSystem()
	{
		rend = new ShapeRenderer();
		solarRender = new solarRender(rend);
		Random rand = new Random();
		solarSystem = new Planet[3];
		for(int i =0;i<solarSystem.length-1;i++)
		{
			float plX = rand.nextFloat(10000);
			float plY = rand.nextFloat(10000);
			float plRad = rand.nextFloat() *(400-300)+300;
			double plMass = rand.nextDouble(8e12-7e11)+7e11;

			float red = rand.nextFloat();
			float green = rand.nextFloat();
			float blue = rand.nextFloat();
			Color plColor = new Color(red,green,blue,1f);

			solarSystem[i] = new Planet(plMass,plX,plY,plRad,plColor);
		}
		solarSystem[2] = new Planet(10e12,10000,10000,800,Color.YELLOW);
	}
	public void setUpObjects()
	{
		//textures
		batch = new SpriteBatch();
		landerImg = new Texture("Lunar Lander.png");
		thrusterSheet = new Texture("Lunar Lander thruster.png");
		//animations
		thrusters = new sprAnim(thrusterSheet,1,3,.1f);
		idling = new sprAnim(landerImg,1,1,.1f);
		land = new Lander(1,400,400);
	}
	///////////////////////////////////////////////////
	////////////// Render Functions////////////////////
	///////////////////////////////////////////////////
	public void extendRender()
	{
		land.fly();
		for(solarObject ob:solarSystem)
			land.crashTest(ob.hitBox);
		for(int i =0;i<solarSystem.length-1;i++) {
			solarSystem[2].gravVel((solarSystem[2].Gravity(solarSystem[i])),solarSystem[i]);
			solarSystem[i].gravVel((solarSystem[i].Gravity(solarSystem[2])),solarSystem[2]);
		}
		land.hitboxUpdate();
		for(solarObject ob:solarSystem) {
			ob.gravVel((ob.Gravity(land)), land); // Gravity Between Planets and Lander
			ob.orbit();
			ob.hitboxUpdate();
		}
		extendCam.position.set(land.pos.x, land.pos.y, 0);
		extendCam.update();
		extendView.apply();
		// shape rendering(planets)
		rend.setProjectionMatrix(extendView.getCamera().combined);
		rend.setAutoShapeType(false);
		rend.begin(ShapeRenderer.ShapeType.Filled);
		for(solarObject ob : solarSystem)
		{
			rend.setColor(ob.color);
			rend.circle(ob.pos.x, ob.pos.y, ob.radius); //draw the planets
		}
		rend.end();
		rend.begin(ShapeRenderer.ShapeType.Line);
		for(solarObject ob: solarSystem) {
			solarRender.hitBoxRender(ob.hitBox);	//planet hitboxes
			solarRender.orbitRender(land,ob); //orbit path
		}
		solarRender.hitBoxRender(land.hitBox);
		rend.end();
		//batch rendering(lander)
		batch.setProjectionMatrix(extendView.getCamera().combined);
		batch.begin();
		TextureRegion currentFrame = (land.thrusters(idling.getAnim(), thrusters.getAnim())).getKeyFrame(stateTime, true);
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.end();
	}
	public void uiRender()
	{
		screenView.apply();
		for(int i =0; i<solarSystem.length;i++)
			ui.obLabelUpdate(solarLabels.get(i),solarSystem[i],i); //Planet Stats
		ui.debugUpdate(debugLabel); //Memory,Window,Etc
		ui.velLabelUpdate(velocityLabel,land); // Lander Stats
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	public void miniRender()
	{
		miniView.apply();
		rend.setProjectionMatrix(miniView.getCamera().combined);

		//minimap backround
		rend.begin(ShapeRenderer.ShapeType.Filled);
		rend.setColor(Color.BLUE);
		rend.rect((miniView.getCamera().position.x-miniView.getCamera().position.x)+5,(miniView.getCamera().position.y-miniView.getCamera().position.y)+2,miniView.getWorldWidth()-10,miniView.getWorldHeight()-10);
		rend.end();

		//minimap border
		rend.begin(ShapeRenderer.ShapeType.Line);
		rend.setColor(Color.WHITE);
		rend.rect((miniView.getCamera().position.x-miniView.getCamera().position.x)+5,(miniView.getCamera().position.y-miniView.getCamera().position.y)+2,miniView.getWorldWidth()-10,miniView.getWorldHeight()-10);
		rend.end();

		//minimap icons
		rend.begin(ShapeRenderer.ShapeType.Filled);
		for(solarObject ob: solarSystem)
			solarRender.miniRend(ob);
		solarRender.landerMiniRend(land,miniView,extendView);
		rend.end();
	}

}
