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
    public solarObject(float mass,float x, float y)
    {
        this.mass = mass;
        vel = new Vector2(0f,0f);
        pos = new Vector2(0f,0f);
        color = Color.WHITE;
        radius = 0f;
    }
    public float getDistance(solarObject ob1,solarObject ob2)
    {
        return (float) Math.sqrt(Math.abs(Math.pow(ob1.pos.x - ob2.pos.x,2) + Math.pow(ob1.pos.y - ob2.pos.y,2)));
    }
    public Vector2 Gravity(solarObject ob2)
    {
        Vector2 distVect = new Vector2(ob2.pos.x - this.pos.x,ob2.pos.y - this.pos.y);
        float distMag = distVect.len();
        float gravForce = ((G * this.mass * ob2.mass)/ (distMag*distMag));
        float angle = (float) Math.atan2(distVect.y, distVect.x);
        Vector2 grav = new Vector2((float) (Math.cos(angle)*gravForce*Gdx.graphics.getDeltaTime()), (float) (Math.sin(angle)*gravForce*Gdx.graphics.getDeltaTime()));
        return grav;
    }
    public void orbit(solarObject ob2)
    {
        this.vel.add(Gravity(ob2));
        this.pos.x += this.vel.x * Gdx.graphics.getDeltaTime();
        this.pos.y += this.vel.y * Gdx.graphics.getDeltaTime();
    }
    public void setStartVel(solarObject ob2)
    {
        Vector2 distVect = new Vector2(this.pos.x-ob2.pos.x,this.pos.y-ob2.pos.y);
        float velocity = (float) Math.sqrt((G*ob2.mass*this.mass)/distVect.len());
        float angle = (float) Math.atan2(ob2.pos.y - this.pos.y, ob2.pos.x - this.pos.x);
        float velx = (float) (velocity*(Math.cos(angle+1.57)));
        float vely = (float) (velocity*(Math.sin(angle+1.57)));
        this.vel.add(velx,vely);
    }
    public boolean orbitCheck(solarObject pl2)
    {
        float dist = Vector2.dst(this.pos.x,this.pos.y,pl2.pos.x,pl2.pos.y);
        System.out.println(dist);
        return dist > 1;
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
                float dist = this.radius + ob2.radius - this.getDistance(this,ob2);
                this.pos.x -= (ob2.pos.x - this.pos.x) * dist / (2 * this.radius);
                this.pos.y -= (ob2.pos.y - this.pos.y) * dist / (2 * this.radius);
                if(Math.abs(this.vel.x)>Math.abs(this.vel.y))
                    this.vel.x *= -1;
                else
                    this.vel.y *=-1;
            }
            else
            {
                float dist = ob2.radius + this.radius - ob2.getDistance(ob2,this);
                ob2.pos.x -= (this.pos.x - ob2.pos.x) * dist / (2 * ob2.radius);
                ob2.pos.y -= (this.pos.y - ob2.pos.y) * dist / (2 * ob2.radius);
                if(Math.abs(ob2.vel.x)>Math.abs(ob2.vel.y))
                    ob2.vel.x *= -1;
                else
                    ob2.vel.y *=-1;
            }
        }
    }
    public Vector2 getNetGrav(solarObject[] system)
    {
        Vector2 netGrav = new Vector2();
        for(solarObject ob2: system)
        {
            netGrav.add(this.Gravity(ob2));
        }
        return netGrav;
    }

}
