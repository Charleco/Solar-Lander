package landergdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnim {
    private final Texture sheet;
    private final int rowCount;
    private final int colCount;
    private final float fps;
    private Animation<TextureRegion> anim;
    public SpriteAnim(Texture sheet, int rowCount, int colCount, float fps)
    {
        this.sheet =sheet;
        this.rowCount=rowCount;
        this.colCount=colCount;
        this.fps=fps;
        this.anim = this.setAnim();
    }
    public Animation<TextureRegion> setAnim()
    {
        Animation<TextureRegion> output;
        TextureRegion[][] spritesheet = TextureRegion.split(sheet, sheet.getWidth() / colCount, sheet.getHeight() / rowCount);
        TextureRegion[] splitsheet = new TextureRegion[colCount * rowCount];
        int index = 0;
        for(int i = 0; i<rowCount;i++)
        {
            for(int j=0; j<colCount;j++)
            {
                splitsheet[index++] = spritesheet[i][j];
            }
        }
        output = new Animation<>(fps, splitsheet);
        return output;
    }
    public Animation<TextureRegion> getAnim()
    {
        return anim;
    }
}
