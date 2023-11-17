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
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
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
	private OrthographicCamera miniCam;
	private FitViewport miniView;

	//objects
	Lander land = new Lander(1,400,400);
	solarObject[] solarSystem;
	ArrayList<Label> solarLabels;
	ShapeRenderer rend;
	solarRender solarRender;
	@Override
	public void create () {
		Random rand = new Random();
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
		solarSystem = new Planet[2];
		for(int i =0;i<solarSystem.length;i++)
		{
			float plX = rand.nextFloat(1000);
			float plY = rand.nextFloat(1000);
			float plRad = rand.nextFloat() *(120-60)+60;
			double plMass = rand.nextDouble(8e12-7e11)+7e11;

			float red = rand.nextFloat();
			float green = rand.nextFloat();
			float blue = rand.nextFloat();
			Color plColor = new Color(red,green,blue,1f);
			solarSystem[i] = new Planet(plMass,plX,plY,plRad,plColor);
		}
		//Camera and Viewports
		orthoCam = new OrthographicCamera();
		extendView = new ExtendViewport(900,900, orthoCam); //Batch/World viewport
		orthoCam.position.set(land.pos.x,land.pos.y,0);
		orthoCam.update();
		screenView = new ScreenViewport(); //UI viewport
		miniCam = new OrthographicCamera();
		miniView = new FitViewport(5000,5000,miniCam);
		miniCam.position.set(extendView.getWorldWidth() - miniView.getWorldWidth()/2f,miniView.getWorldHeight()/2f,0);
		miniCam.update();
		miniView.setScreenBounds(Gdx.graphics.getWidth()-120,20,100,100);
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
		table.add(velocityLabel).growX().space(10).padLeft(5).uniform();
		solarLabels = new ArrayList<>();
		table.row();
		table.add(debugLabel).growX().space(10).padLeft(5).uniform();
		for(solarObject ob: solarSystem)
		{
			table.row();
			Label obLabel = new Label("test",labelStyle);
			obLabel.setWrap(true);
			solarLabels.add(obLabel);
			table.add(obLabel).growX().space(10).padLeft(5).uniform();
		}

		debugLabel.setWrap(true);
		velocityLabel.setWrap(true);
		ui = new userInterface();

	}
	public void resize(int width, int height)
	{
		extendView.update(width, height,true);
		screenView.update(width, height, true);
		miniView.setScreenBounds(Gdx.graphics.getWidth()-500,20,500,500);
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
			rend.setColor(ob.color);
			rend.circle(ob.pos.x, ob.pos.y, ob.radius);
		}
		rend.end();
		rend.begin(ShapeRenderer.ShapeType.Line);
		for(solarObject ob: solarSystem)
		{
			solarRender.hitBoxRender(ob.hitBox);	//planet hitboxes
			//solarRender.orbitRender(land,ob); //orbit path
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
			land.crashTest(ob.hitBox);
		}
		batch.draw(currentFrame, land.pos.x, land.pos.y);
		batch.end();
		//UI Stage
		screenView.apply();
		stage.act(Gdx.graphics.getDeltaTime());
		ui.velLabelUpdate(velocityLabel,land);
		ui.debugUpdate(debugLabel);
		for(int i =0; i<solarSystem.length;i++)
		{
			ui.obLabelUpdate(solarLabels.get(i),solarSystem[i],i);
		}
		stage.draw();
		miniCam.position.set(solarSystem[2].pos.x*(miniView.getScreenX()/extendView.getWorldWidth()),solarSystem[2].pos.y*(miniView.getScreenX()/extendView.getWorldWidth()),0);
		miniCam.update();
		miniView.apply();
		rend.setProjectionMatrix(miniView.getCamera().combined);
		rend.begin(ShapeRenderer.ShapeType.Filled);
		for(solarObject ob: solarSystem)
		{
			solarRender.miniRend(ob, miniView,extendView);
		}
		solarRender.landerMiniRend(land,miniView,extendView);
		rend.end();

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
