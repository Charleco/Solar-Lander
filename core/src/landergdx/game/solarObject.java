package landergdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class solarObject {
    public Vector2 pos;
    public Vector2 vel;
    public final double mass;
    public Circle hitBox;
    public final double G = 6.674e-11;
    public float radius;
    public Color color;
    public solarObject(double mass,float x, float y)
    {
        pos = new Vector2(0f,0f);
        vel = new Vector2(0f,0f);
        this.mass = mass;
        this.color = new Color();
    }
    public double getDistance(solarObject ob1,solarObject ob2)
    {
        return Math.sqrt(Math.abs(Math.pow(ob1.pos.x - ob2.pos.x,2) + Math.pow(ob1.pos.y - ob2.pos.y,2)));
    }
    public double Gravity(solarObject ob1)
    {
        double distance = getDistance(ob1,this);
        double grav = (G*ob1.mass*this.mass)/Math.pow(distance,2);
        return grav;
    }
    public void gravVel(double grav, solarObject ob1)
    {
        if(ob1.pos.x>pos.x)
        {
            ob1.vel.x-=grav;
        }
        if(ob1.pos.x<pos.x)
        {
            ob1.vel.x+=grav;
        }
        if(ob1.pos.y>pos.y)
        {
            ob1.vel.y-=grav;
        }
        if(ob1.pos.y<pos.y)
        {
            ob1.vel.y+=grav;
        }
        ob1.pos.add(ob1.vel);
    }
    public void orbit()
    {
        pos.add(vel);
    }
    public void hitboxUpdate()
    {
        hitBox.x = pos.x;
        hitBox.y = pos.y;
    }
    public boolean crashTest(Circle ob1Box)
    {

        if(Intersector.overlaps(ob1Box, this.hitBox))
        {
            vel.y= -(vel.y);
            vel.x= -(vel.x);

            return true;
        }
        return false;
    }
}
