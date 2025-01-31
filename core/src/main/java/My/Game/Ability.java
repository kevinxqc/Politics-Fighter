package My.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Ability {
    private static SpriteBatch batch;
    private float x, y;
    private float width, height;
    private float speed;
    private boolean isFlipped;
    private boolean isActive;
    private TextureRegion texture;
    private Animation<TextureRegion> animation;
    private float animationTime;


    public Ability(float x, float y, boolean isFlipped, TextureRegion texture, float width, float height, float speed) {
        this.x = x;
        this.y = y;
        this.isFlipped = isFlipped;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.isActive = true;
        TextureRegion[] frames = texture.split(texture.getRegionWidth() / 2, texture.getRegionHeight())[0];
        animation = new Animation<>(0.1f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animationTime = 0f;

    }

    public static void setBatch(SpriteBatch batch1) {
        batch = batch1;
    }

    public void goFaster(){
        speed += 5;
    }

    public void update(float delta) {
        // uppdatera positionen, kollar om den ska åka höger eller vänster
        x += isFlipped ? -speed : speed;

        animationTime += delta;
        // ta bort den om den är utanför skärmen
        if (x < -width || x > 800 + width) {
            isActive = false;
        }
    }

    public void render() {
        if (isActive) {

            TextureRegion currentFrame = animation.getKeyFrame(animationTime);
            if(isFlipped && !currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
            else if(!isFlipped && currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
            batch.draw(currentFrame, x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

}
