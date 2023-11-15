package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class solarRender {
    ShapeRenderer rend;
    public solarRender(ShapeRenderer rend)
    {
        this.rend = rend;
    }
    public void landerBoxRender(Rectangle landHitbox)
    {
        if(Gdx.input.isKeyPressed(SPACE))
        {
            //lander hitbox
            rend.setColor(Color.SCARLET); // Red color for land hitbox
            rend.rect(landHitbox.x, landHitbox.y, landHitbox.width, landHitbox.height);
        }
    }
    public void planetBoxRender(Circle planHitbox, Planet plan)
    {
        if(Gdx.input.isKeyPressed(SPACE))
        {
            rend.setColor(plan.color);
            rend.circle(planHitbox.x, planHitbox.y, planHitbox.radius);
        }
    }
    public void orbitRender(Lander land, Planet plan)
    {

        double velocity = (land.vel.y+land.vel.x);
        rend.setColor(0,0,1,1);
        if(velocity!=0)
        {
            double r = (plan.G * plan.mass) / Math.pow(velocity, 2);
            rend.circle(plan.x,plan.y,(float)plan.getDistance(land));
        }
    }
}
