package landergdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public abstract class solarObject {
    public Vector2 pos;
    public Vector2 vel;
    public final float mass;
    public Circle hitBox;
    public final float G = 10F;
    public float radius;
    public Color color;
    public solarObject(float mass)
    {
        this.mass = mass;
        vel = new Vector2(0f,0f);
        pos = new Vector2(0f,0f);
    }
    public float getDistance(solarObject ob2)
    {
        return new Vector2(ob2.pos.x - this.pos.x,ob2.pos.y - this.pos.y).len();
    }
    public Vector2 gravity(solarObject ob2, float delta)
    {
        Vector2 distVect = new Vector2(ob2.pos.x - this.pos.x,ob2.pos.y - this.pos.y);
        float distMag = distVect.len();
        if(distMag==0)
            return new Vector2(0,0);
        float gravForce = ((G * this.mass * ob2.mass)/ (distMag*distMag));
        float angle = (float) Math.atan2(distVect.y, distVect.x);
        return new Vector2((float) (Math.cos(angle)*gravForce*delta), (float) (Math.sin(angle)*gravForce*delta));
    }
    public void orbit(solarObject ob2, float delta)
    {
        this.vel.add(gravity(ob2,delta));
        this.pos.x += this.vel.x * delta;
        this.pos.y += this.vel.y * delta;
    }
    public void setStartVel(solarObject ob2)
    {
        double circleOrbit= 1.57;
        Vector2 distVect = new Vector2(this.pos.x-ob2.pos.x,this.pos.y-ob2.pos.y);
        float velocity = (float) Math.sqrt((G*ob2.mass*this.mass)/distVect.len());
        float angle = (float) Math.atan2(ob2.pos.y - this.pos.y, ob2.pos.x - this.pos.x);
        float velx = (float) (velocity*(Math.cos(angle+circleOrbit)));
        float vely = (float) (velocity*(Math.sin(angle+circleOrbit)));
        this.vel.add(velx,vely);
    }
    public boolean orbitCheck(solarObject[] system)
    {
        for(solarObject ob2: system)
        {
            float dist = this.getDistance(ob2);
            if(dist < (this.radius + ob2.radius)&&(this.hashCode()!=ob2.hashCode()))
            {
                return false;
            }
        }
        return true;
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
            if(this.mass<ob2.mass)
            {
                Gdx.app.log("Collision", "Before: "+" Pos: ("+this.pos.x+","+this.pos.y+")"+" Vel: ("+this.vel.x+","+this.vel.y+")");
                float dist = this.radius + ob2.radius - this.getDistance(ob2);
                this.pos.x -= (ob2.pos.x - this.pos.x) * dist / (2 * this.radius);
                this.pos.y -= (ob2.pos.y - this.pos.y) * dist / (2 * this.radius);
                if(Math.abs(this.vel.x)>Math.abs(this.vel.y))
                    this.vel.x *= -1;
                else
                    this.vel.y *=-1;
                Gdx.app.log("Collision", "After: "+" Pos: ("+this.pos.x+","+this.pos.y+")"+" Vel: ("+this.vel.x+","+this.vel.y+")");
            }
            else
            {
                Gdx.app.log("Collision", "Before: "+" Pos: ("+ob2.pos.x+","+ob2.pos.y+")"+" Vel: ("+ob2.vel.x+","+ob2.vel.y+")");
                float dist = ob2.radius + this.radius - ob2.getDistance(this);
                ob2.pos.x -= (this.pos.x - ob2.pos.x) * dist / (2 * ob2.radius);
                ob2.pos.y -= (this.pos.y - ob2.pos.y) * dist / (2 * ob2.radius);
                if(Math.abs(ob2.vel.x)>Math.abs(ob2.vel.y))
                    ob2.vel.x *= -1;
                else
                    ob2.vel.y *=-1;
                Gdx.app.log("Collision", "After: "+" Pos: ("+ob2.pos.x+","+ob2.pos.y+")"+" Vel: ("+ob2.vel.x+","+ob2.vel.y+")");
            }
        }
    }
    public Vector2 getNetGrav(solarObject[] system,float delta)
    {
        Vector2 netGrav = new Vector2();
        for(solarObject ob2: system)
        {
            netGrav.add(this.gravity(ob2,delta));
        }
        return netGrav;
    }

}
