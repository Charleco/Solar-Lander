package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Lander {
    public int xpos;
    public int ypos;
    //going to ignore angle for now
    //public double angle;

    // need to fix this to account for anyone changing window size
    int width;
    int height;
    public Lander()
    {
        xpos = 0;
        ypos =0;
        //angle = 0.0;
    }
    public void fly()
    {
        if(Gdx.input.isKeyPressed(Keys.A))
        {
            xpos-= 200*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.D))
        {
            xpos+= 200*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            ypos += 200*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Keys.S))
        {
            ypos -= 200*Gdx.graphics.getDeltaTime();
        }
    }
    public void boundscheck()
    {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        if(xpos>(width-32))
            xpos=width-32;
        if(xpos<0)
            xpos=0;
        if(ypos>(height-32))
            ypos=height-32;
        if(ypos<0)
            ypos=0;
    }
    public Animation<TextureRegion> thrusters(Animation<TextureRegion> idle, Animation<TextureRegion> thrust)
    {
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            return thrust;
        }
        return idle;
    }

}
