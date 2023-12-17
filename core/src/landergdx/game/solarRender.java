package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    final ShapeRenderer rend;
    float delta;
    HashMap<Vector2,Color> dots;
    public solarRender(ShapeRenderer rend)
    {
        this.rend = rend;
        delta = 0f;
        this.dots = new HashMap<>();
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
    public void gravLanderVectLine(Lander land, solarObject ob2)
    {
        rend.setColor(1,0,0,1f);
        Vector2 test = new Vector2(land.Gravity(ob2));
        rend.rectLine(land.pos.x+16,land.pos.y+16,(land.pos.x+16)+test.x*15000,(land.pos.y+16)+test.y*15000,2f);
    }
    public void velLanderVectLine(Lander land)
    {
        rend.setColor(0,0,1,1f);
        rend.line(land.pos.x+16,land.pos.y+16,(land.pos.x+16)+land.vel.x*10,(land.pos.y+16)+land.vel.y*10);
    }
    public void trailDot(solarObject[] system)
    {
        float curTime = Gdx.graphics.getDeltaTime();
        delta += curTime;
        if((delta)>2f)
        {
            for(solarObject ob: system)
            {
                dots.put(new Vector2(ob.pos),ob.color);
            }
            delta = 0f;
        }
        rend.setColor(Color.WHITE);
        for(Vector2 pos: dots.keySet())
        {
            rend.setColor(dots.get(pos));
            rend.circle(pos.x,pos.y,10);
        }
    }
    public void miniDots()
    {
        for(Vector2 pos: dots.keySet())
        {
            rend.setColor(dots.get(pos));
            rend.circle(pos.x*.25f,pos.y*.25f,10);
        }
    }
}
