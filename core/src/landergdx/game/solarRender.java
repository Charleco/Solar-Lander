package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    final ShapeRenderer rend;
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
    public void gravVectLine(solarObject ob1, solarObject ob2)
    {
        rend.setColor(1,0,0,1f);
        Vector2 test = new Vector2(ob1.Gravity(ob2));

        rend.rectLine(ob1.pos.x,ob1.pos.y,ob1.pos.x+test.x*15000,ob1.pos.y+test.y*15000,2f);
    }
    public void velVectLine(solarObject ob1)
    {
        rend.setColor(0,0,1,1f);
        rend.line(ob1.pos.x,ob1.pos.y,ob1.pos.x+ob1.vel.x*10,ob1.pos.y+ob1.vel.y*10);
    }
}
