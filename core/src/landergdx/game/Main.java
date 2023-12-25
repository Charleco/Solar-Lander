package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private float stateTime = 0f;
	private UserInterface ui;
    private SolarSystem system;
	private Lander land;
	private SolarRender solarRender;
	@Override
	public void create () {
        land = new Lander(15, 6000, 6000, Color.WHITE, 16);
		system = new SolarSystem(land,5);
		solarRender = new SolarRender();
		ui = new UserInterface(system);
		Gdx.input.setInputProcessor(ui.getStage());
	}
	public void resize(int width, int height) {
		ui.uiResize(width,height);
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		stateTime += Gdx.graphics.getDeltaTime();
        float delta = Gdx.graphics.getDeltaTime();
		system.update(delta);
		ui.update(system, stateTime);
		solarRender.render(system, delta,ui);
	}
	@Override
	public void dispose () {
		ui.dispose();
		solarRender.getRend().dispose();
	}
}
