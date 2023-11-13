package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class HitboxRender {
    ShapeRenderer rend;
    public HitboxRender(ShapeRenderer rend)
    {
        this.rend = rend;
    }
    public void BoxRender(Rectangle landHitbox, Circle moonHitbox, OrthographicCamera camera)
    {
        if(Gdx.input.isKeyPressed(SPACE))
        {
            //hitbox render
            rend.setProjectionMatrix(camera.combined);
            rend.begin(ShapeRenderer.ShapeType.Line);
            rend.setColor(1, 0, 0, 1); // Red color for land hitbox
            rend.rect(landHitbox.x, landHitbox.y, landHitbox.width, landHitbox.height);

            rend.setColor(0, 1, 0, 1); // Green color for moon hitbox
            rend.circle(moonHitbox.x, moonHitbox.y, moonHitbox.radius);
            rend.end();
        }
    }
}
