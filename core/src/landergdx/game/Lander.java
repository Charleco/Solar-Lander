package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Lander extends SolarObject {
    public Lander(float mass, float x, float y, Color color, float radius)
    {
        super(mass,x,y,color,radius);
        setHitBox(new Circle(getPos().x+16, getPos().y+16,16));
    }
    public void render(float delta)
    {
        this.fly(delta);
        this.hitboxUpdate();
    }

    public void fly(float delta) //moves the lander with constant acceleration 1
    {
        if(Gdx.input.isKeyPressed(Keys.A)) {
            getVel().x-= 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            getVel().x+= 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.W)) {
            getVel().y += 1*delta;
        }
        if(Gdx.input.isKeyPressed(Keys.S)) {
            getVel().y -= 1*delta;
        }
        getPos().add(getVel());
    }
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust) //returns the correct animation based on the current input
    {
        if(Gdx.input.isKeyPressed(Keys.W))
            return thrust;
        return idle;
    }
    public void hitboxUpdate() //updates the hitbox to the current position of the lander's COM(center of mass)
    {
        getHitBox().x = getPos().x+16;
        getHitBox().y = getPos().y+16;
    }
    public float distance(SolarObject ob2) //returns the distance between this and ob2 accounting for the position of the lander technically being the bottom left corner
    {
        return new Vector2(ob2.getPos().x - (this.getPos().x+16), ob2.getPos().y - (this.getPos().y+16)).len();
    }
}
