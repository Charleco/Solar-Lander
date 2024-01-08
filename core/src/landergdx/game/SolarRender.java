package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class SolarRender {
    private final ShapeRenderer rend;
    private float dotTime;
    private final HashMap<Vector2,Color> dots;
    private final float scale = .25f;
    public SolarRender()
    {
        this.rend = new ShapeRenderer();
        dotTime = 0f;
        this.dots = new HashMap<>();
    }
    public ShapeRenderer getRend()
    {
        return this.rend;
    }
    public void render(SolarSystem system, float delta, UserInterface ui)
    {
        //planets and their prior positions
        rend.setProjectionMatrix(ui.getExtendView().getCamera().combined);
        rend.begin(ShapeRenderer.ShapeType.Filled);
        this.drawPlanets(system.getSystem());
        this.trailDot(system.getSystem());
        rend.end();
        //hitboxes, orbits, force vectors
        rend.begin(ShapeRenderer.ShapeType.Line);
        this.hitBoxRender(system.getSystem(),system.getLand());
        this.orbitRender(system.getLand(),system.getSystem(),delta);
        this.landerVectLine(system.getLand(),system.getSystem()[0],delta);
        this.vectLine(system.getSystem(),system.getSystem()[0],delta);
        rend.end();
        //ui vector boxes
        rend.setProjectionMatrix(ui.getScreenView().getCamera().combined);
        rend.begin(ShapeRenderer.ShapeType.Filled);
        this.drawLandBoxVectors(system.getLand(), system.getSystem(),ui.getScreenView(),delta);
        rend.end();

        //minimap
        ui.getMiniView().apply();
        rend.setProjectionMatrix(ui.getMiniView().getCamera().combined);
        //minimap background
        rend.begin(ShapeRenderer.ShapeType.Filled);
        rend.setColor(Color.BLACK);
        rend.rect((ui.getMiniView().getCamera().position.x-ui.getMiniView().getCamera().position.x)+5,(ui.getMiniView().getCamera().position.y-ui.getMiniView().getCamera().position.y)+2,ui.getMiniView().getWorldWidth()-10,ui.getMiniView().getWorldHeight()-10);
        rend.end();

        //minimap border
        rend.begin(ShapeRenderer.ShapeType.Line);
        rend.setColor(Color.WHITE);
        rend.rect((ui.getMiniView().getCamera().position.x-ui.getMiniView().getCamera().position.x)+5,(ui.getMiniView().getCamera().position.y-ui.getMiniView().getCamera().position.y)+2,ui.getMiniView().getWorldWidth()-10,ui.getMiniView().getWorldHeight()-10);
        rend.end();

        //minimap icons
        rend.begin(ShapeRenderer.ShapeType.Filled);
        this.miniRend(system.getSystem());
        this.landerMiniRend(system.getLand());
        this.miniDots();
        rend.end();
        rend.begin(ShapeRenderer.ShapeType.Line);
        this.miniVectLine(system.getSystem(),system.getSystem()[0],delta);
        rend.end();
    }
    public void dispose()
    {
        this.rend.dispose();
    }
    public void drawPlanets(SolarObject[] system)
    {
        for(SolarObject ob:system)
        {
            this.rend.setColor(ob.getColor());
            this.rend.circle(ob.getPos().x, ob.getPos().y, ob.getRadius()); //draw the planets
        }
    }
    public void hitBoxRender(SolarObject[] system, Lander land) //draws the hitboxes of the planets if space is held
    {
        if (Gdx.input.isKeyPressed(SPACE))
        {
            this.rend.setColor(Color.RED);
            for(SolarObject ob:system) {
                this.rend.circle(ob.getHitBox().x, ob.getHitBox().y, ob.getHitBox().radius);
            }
            this.rend.circle(land.getHitBox().x, land.getHitBox().y, land.getHitBox().radius);
        }
    }
    public void orbitRender(SolarObject ob1, SolarObject[] system, float delta) //if gravitational force is greater than 5e-4, draw the orbit circle
    {
        for (SolarObject ob2 : system)
        {
            if (ob1.gravity(ob2,delta).len() > 5e-4) {
                this.rend.setColor(ob2.getColor());
                this.rend.circle(ob2.getPos().x, ob2.getPos().y, ob1.distance(ob2));
            }
        }
    }
    public void miniRend(SolarObject[] system) //draws the scaled down planets on the minimap
    {
        for(SolarObject ob: system) {
            float miniX = ob.getPos().x * scale;
            float miniY = ob.getPos().y * scale;
            float miniRad = ob.getRadius() * scale;
            this.rend.setColor(ob.getColor());
            this.rend.circle(miniX, miniY, miniRad);
        }
    }
    public void landerMiniRend(Lander land) //draws the lander / white dot on the minimap
    {
        this.rend.setColor(Color.WHITE);
        float miniX = land.getPos().x * scale;
        float miniY = land.getPos().y * scale;
        this.rend.circle(miniX,miniY,20);
    }
    public void vectLine(SolarObject[] system, SolarObject ob2, float delta)
    {
        for (SolarObject ob1 : system) {
            //Gravity
            this.rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(ob1.gravity(ob2,delta));
            this.rend.rectLine(ob1.getPos().x, ob1.getPos().y, ob1.getPos().x + test.x * 2000, ob1.getPos().y + test.y * 2000, 2f);
            //Velocity
            this.rend.setColor(0, 0, 1, 1f);
            this.rend.line(ob1.getPos().x, ob1.getPos().y, ob1.getPos().x + ob1.getVel().x * 10, ob1.getPos().y + ob1.getVel().y * 10);
        }
    }
    public void miniVectLine(SolarObject[] system, SolarObject ob2, float delta)
    {
        for(SolarObject ob1: system) {
            //Gravity
            this.rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(ob1.gravity(ob2, delta));
            this.rend.rectLine(ob1.getPos().x * scale, ob1.getPos().y * scale, (ob1.getPos().x + test.x * 2000) * scale, (ob1.getPos().y + test.y * 2000) * scale, 2f);
            //Velocity
            this.rend.setColor(0, 0, 1, 1f);
            this.rend.line(ob1.getPos().x * scale, ob1.getPos().y * scale, (ob1.getPos().x + ob1.getVel().x * 10) * scale, (ob1.getPos().y + ob1.getVel().y * 10) * scale);
        }
    }
    public void landerVectLine(Lander land, SolarObject ob2, float delta) //somewhat redundant with drawLandBoxVectors, but draws the lander's velocity and netgrav vectors if V is held
    {
        if(Gdx.input.isKeyPressed(Input.Keys.V))
        {
            //Gravity
            this.rend.setColor(1, 0, 0, 1f);
            Vector2 test = new Vector2(land.gravity(ob2,delta));
            this.rend.rectLine(land.getPos().x + 16, land.getPos().y + 16, (land.getPos().x + 16) + test.x * 2000, (land.getPos().y + 16) + test.y * 2000, 2f);
            //Velocity
            this.rend.setColor(0, 0, 1, 1f);
            this.rend.line(land.getPos().x + 16, land.getPos().y + 16, (land.getPos().x + 16) + land.getVel().x * 10, (land.getPos().y + 16) + land.getVel().y * 10);
        }
    }
    public void trailDot(SolarObject[] system) // every 2 seconds, add a Vector2/dot to the hashmap with each planet's position and color, then draw them all
    {
        float curTime = Gdx.graphics.getDeltaTime();
        dotTime += curTime;
        if((dotTime)>2f)
        {
            for(SolarObject ob: system)
            {
                dots.put(new Vector2(ob.getPos()), ob.getColor());
                if(dots.size()%10==0)
                    Gdx.app.log("Render", "Dots.Size: "+dots.size());
            }
            dotTime = 0f;
        }
        for(Vector2 pos: dots.keySet())
        {
            this.rend.setColor(dots.get(pos));
            this.rend.circle(pos.x,pos.y,10);
        }
    }
    public void miniDots() //draws the scaled down traildots on the minimap
    {
        for(Vector2 pos: dots.keySet())
        {
            this.rend.setColor(dots.get(pos));
            this.rend.circle(pos.x*.25f,pos.y*.25f,10);
        }
    }
    public void drawLandBoxVectors(Lander land, SolarObject[] system, Viewport view, float delta) //draws the lander's velocity and netgrav vectors in the center boxes
    {
        int gravBoxX = view.getScreenWidth()/2;
        int gravBoxY = view.getScreenHeight()-(view.getScreenHeight()-50);
        Vector2 netGrav = new Vector2(land.netGrav(system,delta));
        netGrav.x *= 10000;
        netGrav.y *= 10000;
        netGrav.clamp(0,50);
        this.rend.setColor(Color.BLACK);
        this.rend.rect(gravBoxX,gravBoxY,100,100);
        this.rend.rect(gravBoxX-101,gravBoxY,100,100);
        this.rend.end();
        this.rend.begin(ShapeRenderer.ShapeType.Line);
        this.rend.setColor(Color.WHITE);
        this.rend.rect(gravBoxX,gravBoxY,100,100);
        this.rend.rect(gravBoxX-101,gravBoxY,100,100);
        this.rend.setColor(1,0,0,1);
        this.rend.rectLine(gravBoxX+50, gravBoxY+50, (gravBoxX+50) + netGrav.x, (gravBoxY+50) + netGrav.y, 2f);
        this.rend.setColor(0,0,1,1f);
        Vector2 landvel = new Vector2(land.getVel());
        landvel.x *= 5;
        landvel.y *= 5;
        landvel.clamp(0,50);
        this.rend.rectLine(gravBoxX-51, gravBoxY+50, (gravBoxX-51) + landvel.x, (gravBoxY+50) + landvel.y, 2f);

    }
}
