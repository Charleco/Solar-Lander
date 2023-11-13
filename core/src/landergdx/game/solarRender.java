package landergdx.game;
import com.badlogic.gdx.Gdx;
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
    public void boxRender(Rectangle landHitbox, Circle moonHitbox, OrthographicCamera camera)
    {
        if(Gdx.input.isKeyPressed(SPACE))
        {
            //lander hitbox
            rend.setProjectionMatrix(camera.combined);
            rend.begin(ShapeRenderer.ShapeType.Line);
            rend.setColor(1, 0, 0, 1); // Red color for land hitbox
            rend.rect(landHitbox.x, landHitbox.y, landHitbox.width, landHitbox.height);
            //planet hitboxes
            rend.setColor(0, 1, 0, 1); // Green color for moon hitbox
            rend.circle(moonHitbox.x, moonHitbox.y, moonHitbox.radius);
            rend.end();
        }
    }

    public void orbitRender(Lander land, OrthographicCamera camera, Planet plan)
    {

        double velocity = (land.vel.y+land.gravVel.y+land.vel.x+land.gravVel.x);
        rend.setProjectionMatrix(camera.combined);
        rend.begin(ShapeRenderer.ShapeType.Line);
        rend.setColor(0,0,1,1);
        if(velocity!=0)
        {
            double r = (plan.G * plan.mass) / Math.pow(velocity, 2);
            rend.circle(plan.x,plan.y,(float)plan.getDistance(land));
        }
        rend.end();
    }


}
