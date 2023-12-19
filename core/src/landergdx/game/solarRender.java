package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    final ShapeRenderer rend;
    float delta;
    HashMap<Vector2,Color> dots;
    float scale = .25f;
    public solarRender(ShapeRenderer rend)
    {
        this.rend = rend;
        delta = 0f;
        this.dots = new HashMap<>();
    }
    public void drawPlanets(solarObject[] system)
    {
        for(solarObject ob:system)
        {
            rend.setColor(ob.color);
            rend.circle(ob.pos.x, ob.pos.y, ob.radius); //draw the planets
        }
    }
    public void hitBoxRender(solarObject[] system, Lander land) {
        if (Gdx.input.isKeyPressed(SPACE))
        {
            rend.setColor(Color.RED);
            for(solarObject ob:system) {
                rend.circle(ob.hitBox.x, ob.hitBox.y, ob.hitBox.radius);
            }
            rend.circle(land.hitBox.x,land.hitBox.y,land.hitBox.radius);
        }
    }
    public void orbitRender(solarObject ob1, solarObject[] system) {
        for (solarObject ob2 : system)
        {
            if (ob1.Gravity(ob2).len() > 5e-4) {
                rend.setColor(ob2.color);
                rend.circle(ob2.pos.x, ob2.pos.y, ob1.getDistance(ob1, ob2));
            }
        }
    }
    public void miniRend(solarObject ob)
    {
        float miniX = ob.pos.x * scale;
        float miniY = ob.pos.y * scale;
        float miniRad = ob.radius * scale;
        rend.setColor(ob.color);
        rend.circle(miniX,miniY,miniRad);
    }
    public void landerMiniRend(Lander land)
    {
        rend.setColor(Color.WHITE);
        float miniX = land.pos.x * scale;
        float miniY = land.pos.y * scale;
        rend.circle(miniX,miniY,20);
    }
    public void vectLine(solarObject[] system, solarObject ob2) {
        for (solarObject ob1 : system) {
            //Gravity
            rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(ob1.Gravity(ob2));
            rend.rectLine(ob1.pos.x, ob1.pos.y, ob1.pos.x + test.x * 2000, ob1.pos.y + test.y * 2000, 2f);
            //Velocity
            rend.setColor(0, 0, 1, 1f);
            rend.line(ob1.pos.x, ob1.pos.y, ob1.pos.x + ob1.vel.x * 10, ob1.pos.y + ob1.vel.y * 10);
        }
    }
    public void miniVectLine(solarObject ob1, solarObject ob2)
    {
        //Gravity
        rend.setColor(1,0,0,1f);
        Vector2 test = new Vector2(ob1.Gravity(ob2));
        rend.rectLine(ob1.pos.x*scale,ob1.pos.y*scale,(ob1.pos.x+test.x*2000)*scale,(ob1.pos.y+test.y*2000)*scale,2f);
        //Velocity
        rend.setColor(0,0,1,1f);
        rend.line(ob1.pos.x*scale,ob1.pos.y*scale,(ob1.pos.x+ob1.vel.x*10)*scale,(ob1.pos.y+ob1.vel.y*10)*scale);
    }
    public void landerVectLine(Lander land, solarObject ob2)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.V))
        {
            //Gravity
            rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(land.Gravity(ob2));
            rend.rectLine(land.pos.x + 16, land.pos.y + 16, (land.pos.x + 16) + test.x * 2000, (land.pos.y + 16) + test.y * 2000, 2f);
            //Velocity
            rend.setColor(0, 0, 1, 1f);
            rend.line(land.pos.x + 16, land.pos.y + 16, (land.pos.x + 16) + land.vel.x * 10, (land.pos.y + 16) + land.vel.y * 10);
        }
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
                if(dots.size()%10==0)
                    Gdx.app.log("Render", "Dots.Size: "+dots.size());
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
    public void drawLandUi(Lander land, solarObject[] system, Viewport view)
    {
        int gravBoxX = view.getScreenWidth()/2;
        int gravBoxY = view.getScreenHeight()-(view.getScreenHeight()-50);

        Vector2 netGrav = new Vector2(land.getNetGrav(system));
        netGrav.x *= 10000;
        netGrav.y *= 10000;
        netGrav.clamp(0,50);
        rend.begin(ShapeRenderer.ShapeType.Filled);
        rend.setColor(Color.BLACK);
        rend.rect(gravBoxX,gravBoxY,100,100);
        rend.rect(gravBoxX-101,gravBoxY,100,100);
        rend.end();
        rend.begin(ShapeRenderer.ShapeType.Line);
        rend.setColor(Color.WHITE);
        rend.rect(gravBoxX,gravBoxY,100,100);
        rend.rect(gravBoxX-101,gravBoxY,100,100);
        rend.setColor(1,0,0,1);
        rend.rectLine(gravBoxX+50, gravBoxY+50, (gravBoxX+50) + netGrav.x, (gravBoxY+50) + netGrav.y, 2f);
        rend.setColor(0,0,1,1f);
        Vector2 landvel = new Vector2(land.vel);
        landvel.x *= 5;
        landvel.y *= 5;
        landvel.clamp(0,50);
        rend.rectLine(gravBoxX-51, gravBoxY+50, (gravBoxX-51) + landvel.x, (gravBoxY+50) + landvel.y, 2f);

    }
}
