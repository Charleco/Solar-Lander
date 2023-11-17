package landergdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Planet extends solarObject {

    public Planet(double mass, float x, float y, float rad,Color color)
    {
        super(mass,x,y);
        this.radius = rad;
        hitBox = new Circle(pos.x,pos.y, radius);
        this.color = color;
        pos.x = x;
        pos.y = y;
        vel.x = 0;
        vel.y = 0;
    }
}
