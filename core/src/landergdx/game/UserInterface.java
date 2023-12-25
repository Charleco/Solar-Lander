package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


import java.util.ArrayList;

public class UserInterface {
    private ArrayList<Label> solarLabels;
    private Stage stage;
    private final FreeTypeFontGenerator generator;
    private final SpriteBatch batch;
    private final OrthographicCamera extendCam;
    private final ExtendViewport extendView;
    private final ScreenViewport screenView;
    private final FitViewport miniView;
    private final OrthographicCamera miniCam;
    private Label velocityLabel;
    private Label debugLabel;

    public Texture landerImg;
    public Texture thrusterSheet;

    public SpriteAnim thrusters;
    public SpriteAnim idling;

    public UserInterface(SolarSystem system)
    {
        solarLabels = new ArrayList<>();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
        screenView = new ScreenViewport();
        batch = new SpriteBatch();
        stage = new Stage(this.screenView, this.batch);
        extendCam = new OrthographicCamera();
        extendView = new ExtendViewport(900,900, extendCam);
        miniCam = new OrthographicCamera();
        miniView = new FitViewport(5000,5000, miniCam);
        this.create(system.getLand(),system);
    }
    public void create(Lander land, SolarSystem system)
    {
        extendCam.position.set(land.getPos().x, land.getPos().y,0);
        extendCam.update();

        miniCam.position.set(miniView.getWorldWidth()/2,miniView.getWorldHeight()/2,0);
        miniCam.update();

        landerImg = new Texture("Lunar Lander.png");
        thrusterSheet = new Texture("Lunar Lander thruster.png");
        thrusters = new SpriteAnim(thrusterSheet,1,3,.1f);
        idling = new SpriteAnim(landerImg,1,1,.1f);

        this.stageSetUp(system.getSystem());
    }
    public void stageSetUp(SolarObject[] solarSystem)
    {
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        stage.addActor(root);

        Table table = new Table();
        table.setFillParent(false);
        table.defaults().space(10);
        root.add(table).growX().expandY().top();

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        BitmapFont labelFont = generator.generateFont(parameter);
        Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);

        this.velocityLabel = new Label("",labelStyle);
        this.debugLabel = new Label("",labelStyle);
        table.add(this.velocityLabel).growX().space(10).padLeft(5).uniform();
        table.row();
        table.add(this.debugLabel).growX().space(10).padLeft(5).uniform();
        for(int i=0;i<solarSystem.length;i++) {
            table.row();
            Label obLabel = new Label("test",labelStyle);
            this.solarLabels.add(obLabel);
            table.add(obLabel).growX().space(10).padLeft(5).uniform();
        }
    }
    public void update(SolarSystem system, float stateTime)
    {
        extendCam.position.set(system.getLand().getPos().x, system.getLand().getPos().y, 0);
        camZoom(extendCam);
        extendCam.update();
        extendView.apply();

        batch.setProjectionMatrix(extendView.getCamera().combined);
        batch.begin();
        TextureRegion currentFrame = (system.getLand().thrusters(idling.getAnim(), thrusters.getAnim())).getKeyFrame(stateTime, true);
        batch.draw(currentFrame, system.getLand().getPos().x, system.getLand().getPos().y);
        batch.end();

        screenView.apply();
        obLabelUpdate(solarLabels,system.getSystem());
        debugUpdate(debugLabel,stateTime);
        velLabelUpdate(velocityLabel,system.getLand());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void uiResize(int width,int height)
    {
        extendView.update(width, height,true);
        screenView.update(width, height, true);
        miniView.setScreenBounds((Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/4)),((Gdx.graphics.getHeight()/96)), Gdx.graphics.getWidth()/4,Gdx.graphics.getWidth()/4);
    }
    public long getMemUsage()
    {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        return heapSize - heapFreeSize;
    }
    public void debugUpdate(Label label, float stateTime) {
        label.setText("Debug: "+"\n"+"Memory Usage: "+this.getMemUsage()/(1024)+"kB"+"\n"+"Window Size: "+ Gdx.graphics.getHeight()+"x"+Gdx.graphics.getWidth()+"\n" + "DeltaTime(ms): "+Gdx.graphics.getDeltaTime()*1000+"\n"+"Total Time(s): "+Math.round(stateTime));
    }
    public void velLabelUpdate(Label label, Lander land) {
        label.setText("Lander: "+"\n"+"Velx: "+ land.getVel().x+"\n"+"Vely: "+ land.getVel().y+"\n"+"X: "+ land.getPos().x+"\n"+"Y: "+ land.getPos().y);
    }
    public void obLabelUpdate(ArrayList<Label> labels, SolarObject[] system) {
        for(int i =0;i<system.length;i++)
        {
            SolarObject ob = system[i];
            labels.get(i).setText("Planet " + (i + 1) + ":" + "\n" + "Velx: " + ob.getVel().x + "\n" + "Vely: " + ob.getVel().y + "\n" + "X: " + ob.getPos().x + "\n" + "Y: " + ob.getPos().y + "\n" + "Mass: " + ob.getMass());
        }
    }
    public void camZoom(OrthographicCamera cam)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.Z))
            cam.zoom = 10f;
        else if(Gdx.input.isKeyPressed(Input.Keys.X))
            cam.zoom = 100f;
        else
            cam.zoom = 1f;
    }
    public void dispose()
    {
        thrusterSheet.dispose();
        batch.dispose();
        landerImg.dispose();
        stage.dispose();
        generator.dispose();
    }
    public Stage getStage() {
        return this.stage;
    }
    public ScreenViewport getScreenView() {
        return this.screenView;
    }
    public ExtendViewport getExtendView() {
        return this.extendView;
    }
    public FitViewport getMiniView()
    {
        return this.miniView;
    }
}
