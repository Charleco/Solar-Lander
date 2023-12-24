package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Lander  extends solarObject{
    public Lander(float mass,float x,float y,Color color, float radius)
    {
        super(mass,x,y,color,radius);
        setHitBox(new Circle(getPos().x+16, getPos().y+16,16));
    }
    public void fly(float delta)
    {
        if(Gdx.input.isKeyPressed(Keys.A))
        {
            getVel().x-= 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.D))
        {
            getVel().x+= 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            getVel().y += 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.S))
        {
            getVel().y -= 1*delta;
        }
        getPos().add(getVel());
    }
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust)
    {
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            return thrust;
        }
        return idle;
    }
    public void hitboxUpdate()
    {
        getHitBox().x = getPos().x+16;
        getHitBox().y = getPos().y+16;
    }
    public float getDistance(solarObject ob2)
    {
        return (float) Math.sqrt(Math.abs(Math.pow((this.getPos().x+16) - ob2.getPos().x,2) + Math.pow((this.getPos().y+16) - ob2.getPos().y,2)));
    }

}
