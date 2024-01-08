package landergdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public abstract class SolarObject {
    private final Vector2 pos;
    private final Vector2 vel;
    private final float mass;
    private Circle hitBox;
    static final float G = 10F;
    private final float radius;
    private final Color color;
    public SolarObject(float mass, float x, float y, Color color, float radius)
    {
        this.mass = mass;
        this.vel = new Vector2(0f,0f);
        this.pos = new Vector2(x,y);
        this.color = color;
        this.radius = radius;
        this.hitBox = new Circle(this.pos.x,this.pos.y,this.radius);
    }
    public float distance(SolarObject ob2) //returns the distance between this and ob2
    {
        return new Vector2(ob2.pos.x - this.pos.x,ob2.pos.y - this.pos.y).len();
    }
    public Vector2 gravity(SolarObject ob2, float delta) //returns the gravitational force between this and ob2
    {
            Vector2 distVect = new Vector2(ob2.pos.x - this.pos.x, ob2.pos.y - this.pos.y);
            float distMag = distVect.len();
            float gravForce = ((G * this.mass * ob2.mass) / (distMag * distMag));
            float angle = (float) Math.atan2(distVect.y, distVect.x);
            return new Vector2((float) (Math.cos(angle) * gravForce * delta), (float) (Math.sin(angle) * gravForce * delta));
    }
    public Vector2 netGrav(SolarObject[] system, float delta) //returns the net gravitational force between this and all other objects in system
    {
        Vector2 netGrav = new Vector2();
        for(SolarObject ob2: system)
        {
            if(this.hashCode()!=ob2.hashCode())
            {
                netGrav.add(this.gravity(ob2,delta));
            }
        }
        return netGrav;
    }
    public void orbit(SolarObject ob2, float delta) //orbiting that accounts only for the gravity between ob2 / the sun and this
    {
        this.vel.add(gravity(ob2,delta));
        this.pos.x += this.vel.x * delta;
        this.pos.y += this.vel.y * delta;
    }
    public void orbit2(SolarObject[] system, float delta) //orbiting that accounts for NetGravity
    {
        this.vel.add(netGrav(system,delta));
        this.pos.x += this.vel.x * delta;
        this.pos.y += this.vel.y * delta;
    }
    public void setStartVel(SolarObject ob2) //sets the starting velocity based on the gravitational force between it and ob2
    {
        double circleOrbit= 1.57;
        Vector2 distVect = new Vector2(this.pos.x-ob2.pos.x,this.pos.y-ob2.pos.y);
        float velocity = (float) Math.sqrt((G*ob2.mass*this.mass)/distVect.len());
        float angle = (float) Math.atan2(ob2.pos.y - this.pos.y, ob2.pos.x - this.pos.x);
        float velx = (float) (velocity*(Math.cos(angle+circleOrbit)));
        float vely = (float) (velocity*(Math.sin(angle+circleOrbit)));
        this.vel.add(velx,vely);
    }
    public boolean orbitCheck(SolarObject[] system) //checks if the planet overlaps any other planets
    {
        for(SolarObject ob2: system)
        {
            float dist = this.distance(ob2);
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
    public void crashTest(SolarObject[] system) //checks if the planet overlaps any other planets and if so, handle the collision based on mass
    {
        for(SolarObject ob2: system) {
            if (Intersector.overlaps(this.hitBox, ob2.hitBox)&&this.hashCode()!=ob2.hashCode()) {
                if (this.mass < ob2.mass) {
                    Gdx.app.log("Collision", "Before: " + " Pos: (" + this.pos.x + "," + this.pos.y + ")" + " Vel: (" + this.vel.x + "," + this.vel.y + ")");
                    float dist = this.radius + ob2.radius - this.distance(ob2);
                    this.pos.x -= (ob2.pos.x - this.pos.x) * dist / (2 * this.radius);
                    this.pos.y -= (ob2.pos.y - this.pos.y) * dist / (2 * this.radius);
                    if (Math.abs(this.vel.x) > Math.abs(this.vel.y))
                        this.vel.x *= -1;
                    else
                        this.vel.y *= -1;
                    Gdx.app.log("Collision", "After: " + " Pos: (" + this.pos.x + "," + this.pos.y + ")" + " Vel: (" + this.vel.x + "," + this.vel.y + ")");
                } else {
                    Gdx.app.log("Collision", "Before: " + " Pos: (" + ob2.pos.x + "," + ob2.pos.y + ")" + " Vel: (" + ob2.vel.x + "," + ob2.vel.y + ")");
                    float dist = ob2.radius + this.radius - ob2.distance(this);
                    ob2.pos.x -= (this.pos.x - ob2.pos.x) * dist / (2 * ob2.radius);
                    ob2.pos.y -= (this.pos.y - ob2.pos.y) * dist / (2 * ob2.radius);
                    if (Math.abs(ob2.vel.x) > Math.abs(ob2.vel.y))
                        ob2.vel.x *= -1;
                    else
                        ob2.vel.y *= -1;
                    Gdx.app.log("Collision", "After: " + " Pos: (" + ob2.pos.x + "," + ob2.pos.y + ")" + " Vel: (" + ob2.vel.x + "," + ob2.vel.y + ")");
                }
            }
        }
    }

    public Vector2 getPos()
    {
        return this.pos;
    }
    public Vector2 getVel()
    {
        return this.vel;
    }
    public float getMass()
    {
        return this.mass;
    }
    public Circle getHitBox()
    {
        return this.hitBox;
    }
    public void setHitBox(Circle newBox) {
        this.hitBox=newBox;
    }
    public float getRadius()
    {
        return this.radius;
    }
    public Color getColor()
    {
        return this.color;
    }

}
