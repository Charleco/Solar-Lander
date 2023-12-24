package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    final ShapeRenderer rend;
    float dotTime;
    HashMap<Vector2,Color> dots;
    float scale = .25f;
    public solarRender(ShapeRenderer rend)
    {
        this.rend = rend;
        dotTime = 0f;
        this.dots = new HashMap<>();
    }
    public void lineRend(){

    }
    public void drawPlanets(solarObject[] system)
    {
        for(solarObject ob:system)
        {
            rend.setColor(ob.getColor());
            rend.circle(ob.getPos().x, ob.getPos().y, ob.getRadius()); //draw the planets
        }
    }
    public void hitBoxRender(solarObject[] system, Lander land) {
        if (Gdx.input.isKeyPressed(SPACE))
        {
            rend.setColor(Color.RED);
            for(solarObject ob:system) {
                rend.circle(ob.getHitBox().x, ob.getHitBox().y, ob.getHitBox().radius);
            }
            rend.circle(land.getHitBox().x, land.getHitBox().y, land.getHitBox().radius);
        }
    }
    public void orbitRender(solarObject ob1, solarObject[] system, float delta) {
        for (solarObject ob2 : system)
        {
            if (ob1.gravity(ob2,delta).len() > 5e-4) {
                rend.setColor(ob2.getColor());
                rend.circle(ob2.getPos().x, ob2.getPos().y, ob1.getDistance(ob2));
            }
        }
    }
    public void miniRend(solarObject[] system)
    {
        for(solarObject ob: system) {
            float miniX = ob.getPos().x * scale;
            float miniY = ob.getPos().y * scale;
            float miniRad = ob.getRadius() * scale;
            rend.setColor(ob.getColor());
            rend.circle(miniX, miniY, miniRad);
        }
    }
    public void landerMiniRend(Lander land)
    {
        rend.setColor(Color.WHITE);
        float miniX = land.getPos().x * scale;
        float miniY = land.getPos().y * scale;
        rend.circle(miniX,miniY,20);
    }
    public void vectLine(solarObject[] system, solarObject ob2,float delta) {
        for (solarObject ob1 : system) {
            //Gravity
            rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(ob1.gravity(ob2,delta));
            rend.rectLine(ob1.getPos().x, ob1.getPos().y, ob1.getPos().x + test.x * 2000, ob1.getPos().y + test.y * 2000, 2f);
            //Velocity
            rend.setColor(0, 0, 1, 1f);
            rend.line(ob1.getPos().x, ob1.getPos().y, ob1.getPos().x + ob1.getVel().x * 10, ob1.getPos().y + ob1.getVel().y * 10);
        }
    }
    public void miniVectLine(solarObject[] system, solarObject ob2,float delta)
    {
        for(solarObject ob1: system) {
            //Gravity
            rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(ob1.gravity(ob2, delta));
            rend.rectLine(ob1.getPos().x * scale, ob1.getPos().y * scale, (ob1.getPos().x + test.x * 2000) * scale, (ob1.getPos().y + test.y * 2000) * scale, 2f);
            //Velocity
            rend.setColor(0, 0, 1, 1f);
            rend.line(ob1.getPos().x * scale, ob1.getPos().y * scale, (ob1.getPos().x + ob1.getVel().x * 10) * scale, (ob1.getPos().y + ob1.getVel().y * 10) * scale);
        }
    }
    public void landerVectLine(Lander land, solarObject ob2,float delta)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.V))
        {
            //Gravity
            rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(land.gravity(ob2,delta));
            rend.rectLine(land.getPos().x + 16, land.getPos().y + 16, (land.getPos().x + 16) + test.x * 2000, (land.getPos().y + 16) + test.y * 2000, 2f);
            //Velocity
            rend.setColor(0, 0, 1, 1f);
            rend.line(land.getPos().x + 16, land.getPos().y + 16, (land.getPos().x + 16) + land.getVel().x * 10, (land.getPos().y + 16) + land.getVel().y * 10);
        }
    }
    public void trailDot(solarObject[] system)
    {
        float curTime = Gdx.graphics.getDeltaTime();
        dotTime += curTime;
        if((dotTime)>2f)
        {
            for(solarObject ob: system)
            {
                dots.put(new Vector2(ob.getPos()), ob.getColor());
                if(dots.size()%10==0)
                    Gdx.app.log("Render", "Dots.Size: "+dots.size());
            }
            dotTime = 0f;
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
    public void drawLandUi(Lander land, solarObject[] system, Viewport view,float delta)
    {
        int gravBoxX = view.getScreenWidth()/2;
        int gravBoxY = view.getScreenHeight()-(view.getScreenHeight()-50);
        Vector2 netGrav = new Vector2(land.getNetGrav(system,delta));
        netGrav.x *= 10000;
        netGrav.y *= 10000;
        netGrav.clamp(0,50);
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
        Vector2 landvel = new Vector2(land.getVel());
        landvel.x *= 5;
        landvel.y *= 5;
        landvel.clamp(0,50);
        rend.rectLine(gravBoxX-51, gravBoxY+50, (gravBoxX-51) + landvel.x, (gravBoxY+50) + landvel.y, 2f);

    }
}
