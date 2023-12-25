package landergdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class SolarSystem {
    private Lander land;
    private SolarObject[] system;
    public SolarSystem(Lander land, int size)
    {
        this.land = land;
        system = new SolarObject[size];
        this.generateSystem();
    }
    public Lander getLand()
    {
        return this.land;
    }
    public SolarObject[] getSystem(){
        return this.system;
    }
    public void update(float delta)
    {
        land.update(delta);
        land.crashTest(this.system);
        land.orbit2(this.system,delta);
        for(SolarObject ob: this.system)
        {
            ob.orbit2(this.system,delta);
            ob.hitboxUpdate();
            ob.crashTest(this.system);
        }
    }

    public void generateSystem()
    {

        for(int i = 0;i<this.system.length;i++) {
            this.system[i]= this.planetGen();
        }
        system[0] = new Planet(15000f,10000,10000,800,Color.YELLOW);
        int reponeeded=0;
        for(int i =1;i<this.system.length;i++)
        {
            this.system[i] = this.planetGen();
            while (!this.system[i].orbitCheck(this.system))
            {
                this.system[i] = this.planetGen();
                reponeeded++;
            }
        }
        Gdx.app.log("Gen", "Repositions Needed: "+reponeeded);
        for(int i =1;i<this.system.length;i++) {
            this.system[i].setStartVel(this.system[0]);
        }
    }
    public SolarObject planetGen()
    {
        Random rand = new Random();
        float plX = 10000f;
        float plY = rand.nextFloat(10000);
        float plRad = rand.nextFloat() *(200f-100f)+100f;
        float plMass = (float) (((4/3)*3.14)*plRad);
        float red = rand.nextFloat();
        float green = rand.nextFloat();
        float blue = rand.nextFloat();
        Color plColor = new Color(red,green,blue,1f);
        return new Planet(plMass,plX,plY,plRad,plColor);
    }

}
