package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
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
    public void orbitRender(solarObject ob1, solarObject ob2)
    {
        rend.setColor(ob2.color);
        rend.circle(ob2.pos.x,ob2.pos.y,ob1.getDistance(ob1,ob2));
    }
    public void miniRend(solarObject ob)
    {
        float scale =.25f;
        float miniX = ob.pos.x * scale;
        float miniY = ob.pos.y * scale;
        float miniRad = ob.radius * scale;
        rend.setColor(ob.color);
        rend.circle(miniX,miniY,miniRad);
    }
    public void landerMiniRend(Lander land)
    {
        float scale = .25f;
        rend.setColor(Color.WHITE);
        float miniX = land.pos.x * scale;
        float miniY = land.pos.y * scale;
        rend.circle(miniX,miniY,20);
    }
}
