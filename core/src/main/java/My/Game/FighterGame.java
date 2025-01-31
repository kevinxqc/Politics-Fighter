
package My.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Importera BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import My.Game.StartScreen;  // Lägg till denna rad för att importera StartScreen
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class FighterGame extends Game {
    private BitmapFont font;  // Font för att visa text i StartMenuScreen
    private Texture[] fighterTextures; // En array av fighterbilder
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;


    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 600;

    @Override
    public void create() {
        font = new BitmapFont();
        fighterTextures = new Texture[3];
        fighterTextures[0] = new Texture("fighter1.png");
        fighterTextures[1] = new Texture("fighter2.png");
        fighterTextures[2] = new Texture("fighter3.png");
        batch = new SpriteBatch(); // Skapa en SpriteBatch


        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);


        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        // Byt till StartScreen istället för StartMenuScreen
        setScreen(new StartScreen(this)); // Sätt StartScreen som första skärm
    }

    public BitmapFont getFont() {
        return font;
    }

    public Texture[] getFighterTextures() {
        return fighterTextures;
    }

    @Override
    public void render() {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();


        batch.setProjectionMatrix(camera.combined);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        for (Texture texture : fighterTextures) {
            texture.dispose();
        }
        batch.dispose();
    }
}

