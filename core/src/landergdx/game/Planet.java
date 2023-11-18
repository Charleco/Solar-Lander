package landergdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
public class Planet extends solarObject {

    public Planet(double mass, float x, float y, float rad,Color color)
    {
        super(mass,x,y);
        this.radius = rad;
        this.color = color;
        this.pos.x = x;
        this.pos.y = y;
        this.vel.x = 0f;
        this.vel.y = 0f;
        this.hitBox = new Circle(pos.x,pos.y, radius);
    }
}
