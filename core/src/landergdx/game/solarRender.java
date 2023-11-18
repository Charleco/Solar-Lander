package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    ShapeRenderer rend;
    public solarRender(ShapeRenderer rend)
    {
        this.rend = rend;
    }
    public void hitBoxRender(Circle hitBox) {
        if (Gdx.input.isKeyPressed(SPACE))
        {
            rend.setColor(Color.RED);
            rend.circle(hitBox.x,hitBox.y,hitBox.radius);
        }
    }
    public void orbitRender(Lander land, solarObject ob)
    {
        double velocity = (land.vel.y+land.vel.x);
        rend.setColor(ob.color);
        if(velocity!=0)
        {
            double r = (ob.G * ob.mass) / Math.pow(velocity, 2);
            rend.circle(ob.pos.x,ob.pos.y, (float) (ob.getDistance(land,ob)));
        }
    }
    public void miniRend(solarObject ob, FitViewport miniMap, ExtendViewport map)
    {
        float scale = miniMap.getScreenX()/map.getWorldWidth();
        float miniX = ob.pos.x * scale;
        float miniY = ob.pos.y * scale;
        float miniRad = ob.radius * scale;
        rend.setColor(ob.color);
        rend.circle(miniX,miniY,miniRad);
    }
    public void landerMiniRend(Lander land, FitViewport miniMap,ExtendViewport map)
    {
        float scale = miniMap.getScreenX()/map.getWorldWidth();
        rend.setColor(Color.WHITE);
        float miniX = land.pos.x * scale;
        float miniY = land.pos.y * scale;
        rend.circle(miniX,miniY,20);
    }
}
