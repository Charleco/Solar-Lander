package landergdx.game;

import com.badlogic.gdx.Gdx;
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
        this.vel = new Vector2(0f,0f);
        this.mass = mass;
        this.color = new Color();
    }
    public float getDistance(solarObject ob1,solarObject ob2)
    {
        return (float) Math.sqrt(Math.abs(Math.pow(ob1.pos.x - ob2.pos.x,2) + Math.pow(ob1.pos.y - ob2.pos.y,2)));
    }
    public Vector2 Gravity(solarObject ob2)
    {
        Vector2 gravForce = new Vector2(0f,0f);
        double distance = getDistance(ob2,this);
        double grav = (G*ob2.mass*this.mass)/Math.pow(distance,2);
        float xDir = this.pos.x-ob2.pos.x;
        float yDir = this.pos.y-ob2.pos.y;
        float mag = (float) Math.sqrt(Math.pow(xDir,2)+Math.pow(yDir,2));
        gravForce.x= (float) (((xDir/mag)*grav)/ob2.mass);
        gravForce.y = (float) (((yDir/mag)*grav)/ob2.mass);

        return gravForce;
    }
    public void gravVel(Vector2 gravForce, solarObject ob1)
    {
        ob1.vel.add(gravForce);
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
    public void crashTest(solarObject ob2)
    {
        if(Intersector.overlaps(this.hitBox,ob2.hitBox))
        {
            double angle = Math.atan2((this.pos.y-ob2.pos.y),(this.pos.x-ob2.pos.x));

            if(this.mass<ob2.mass)
            {
                Gdx.app.log("Collision","From "+this.pos.x + " "+this.pos.y );
                this.pos.x += (float) (Math.cos(angle)*(this.getDistance(this,ob2)-(ob2.radius)));
                this.pos.y += (float) (Math.sin(angle)*(this.getDistance(this,ob2)-(ob2.radius)));
                this.vel.x = -this.vel.x;
                this.vel.y = -this.vel.y;
                Gdx.app.log("Collision","To "+this.pos.x + " "+this.pos.y );
            }
            else
            {
                Gdx.app.log("Collision","From "+ob2.pos.x + " "+ob2.pos.y );
                ob2.pos.x += (float) (Math.cos(angle) * (this.getDistance(this, ob2) - (this.radius)));
                ob2.pos.y += (float) (Math.sin(angle) * (this.getDistance(this, ob2) - (this.radius)));
                ob2.vel.x = -ob2.vel.x;
                ob2.vel.y = -ob2.vel.y;
                Gdx.app.log("Collision","To "+ob2.pos.x + " "+ob2.pos.y );
            }

        }
    }

}
