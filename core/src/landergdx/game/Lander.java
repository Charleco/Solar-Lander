package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Lander {
    public float accel;
    public Vector2 pos;
    public Vector2 vel;
    public Vector2 gravvel;
    //public double angle;
    public double mass;
    // need to fix this to account for anyone changing window size
    int width;
    int height;
    public Lander()
    {
        //angle = 0.0;
        accel = 1f;
        pos = new Vector2(0f,0f);
        pos.x = 0;
        pos.y = 0;
        vel = new Vector2(0f,0f);
        mass = 1;
        gravvel = new Vector2(0,0);
    }
    public void fly()
    {
        accel =1f;
        if(Gdx.input.isKeyPressed(Keys.A))
        {
            vel.x-= 1*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.D))
        {
            vel.x+= 1*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            vel.y += 1*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.S))
        {
            vel.y -= 1*Gdx.graphics.getDeltaTime();
        }
        /*
        if(Gdx.input.isKeyPressed(Keys.Q))
        {
            angle -= 1*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.E))
        {
            angle += 1*Gdx.graphics.getDeltaTime();
        }
        */

        pos.add(vel);
    }
    public void boundscheck()
    {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        if(pos.x>(width-32))
            pos.x=width-32;
        if(pos.x<0)
            pos.x=0;
        if(pos.y>(height-32))
            pos.y=height-32;
        if(pos.y<0)
            pos.y=0;
    }
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust)
    {
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            return thrust;
        }
        return idle;
    }
    public void Gravfly(double grav,Planet plan)
    {
        if(pos.x>plan.x)
        {
            gravvel.x-=grav;
        }
        if(pos.x<plan.x)
        {
            gravvel.x+=grav;
        }
        if(pos.y>plan.y)
        {
            gravvel.y-=grav;
        }
        if(pos.y<plan.y)
        {
            gravvel.y+=grav;
        }
        pos.add(gravvel);
    }
}
