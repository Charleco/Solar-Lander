package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
public class Lander {
    public float accel;
    public Vector2 pos;
    public Vector2 vel;
    //public double angle;
    public double mass;
    int x;
    int y;
    public Rectangle landHitBox;

    public Lander()
    {
        //angle = 0.0;
        accel = 1f;
        pos = new Vector2(0f,0f);
        pos.x = 0;
        pos.y = 0;
        vel = new Vector2(0f,0f);
        mass = 1;
        landHitBox = new Rectangle(pos.x,pos.y,32,32);

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

        x = Gdx.graphics.getWidth();
        y = Gdx.graphics.getHeight();
        if(pos.x>(x-32))
        {
            pos.x = x - 32;
            vel.x = -vel.x;
        }
        if(pos.x<0)
        {
            pos.x = 0;
            vel.x = -vel.x;
        }
        if(pos.y>(y-32))
        {
            pos.y = y - 32;
            vel.y = -vel.y;
        }
        if(pos.y<0)
        {
            pos.y = 0;
            vel.y = -vel.y;
        }
        pos.add(vel);
    }
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust)
    {
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            return thrust;
        }
        return idle;
    }
    public void gravFly(double grav, Planet plan)
    {
        if(pos.x>plan.x)
        {
            vel.x-=grav;
        }
        if(pos.x<plan.x)
        {
            vel.x+=grav;
        }
        if(pos.y>plan.y)
        {
            vel.y-=grav;
        }
        if(pos.y<plan.y)
        {
            vel.y+=grav;
        }
        pos.add(vel);
    }
    public boolean crashTest(Circle planBox, Rectangle landBox)
    {

        if(Intersector.overlaps(planBox, landBox))
        {
            vel.y= -(vel.y);
            vel.x= -(vel.x);

            return true;
        }
        return false;
    }
    public void holdOrbit(Planet plan)
    {
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
        {

        }
    }
    public void hitboxUpdate(Rectangle hitbox)
    {
        hitbox.x = pos.x;
        hitbox.y = pos.y;
    }
}
