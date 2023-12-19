package landergdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
public class Planet extends solarObject {

    public Planet(float mass, float x, float y, float rad,Color color)
    {
        super(mass);
        radius = rad;
        pos.x = x;
        pos.y = y;
        hitBox = new Circle(pos.x,pos.y, radius);
        this.color = color;
    }
}
