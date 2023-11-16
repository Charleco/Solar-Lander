package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Lander  extends solarObject{
    public float accel;
    //public double angle;
    //public double mass;
    int Sx;
    int Sy;


    public Lander(double mass,int x, int y)
    {
        super(mass,x,y);
        //angle = 0.0;
        accel = 1f;
        pos.x = x;
        pos.y = y;
        vel.x = 0;
        vel.y = 0;
        mass = 1;
        hitBox = new Circle(pos.x+16,pos.y+16,16);

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
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust)
    {
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            return thrust;
        }
        return idle;
    }


    public void holdOrbit(Planet plan)
    {
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
        {

        }
    }
    public void hitboxUpdate()
    {
        hitBox.x = pos.x+16;
        hitBox.y = pos.y+16;
    }
}
