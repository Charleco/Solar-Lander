package landergdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
public class SolarLander extends ApplicationAdapter {
	SpriteBatch batch;
	Texture landerimg;
	private OrthographicCamera camera;

	Lander land = new Lander();

	@Override
	public void create () {
		batch = new SpriteBatch();
		landerimg = new Texture("Lunar Lander.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		land.fly();
		land.boundscheck();
		batch.draw(landerimg, land.xpos, land.ypos);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		landerimg.dispose();
	}
}
