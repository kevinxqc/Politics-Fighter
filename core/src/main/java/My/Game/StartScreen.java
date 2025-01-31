package My.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen implements Screen {

    private final FighterGame game;
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private Skin skin;
    private TextButton chooseFighterButton;
    private OrthographicCamera camera;
    private Viewport viewport;

    public StartScreen(FighterGame game) {
        this.game = game;
        batch = game.getBatch();

        camera = game.getCamera();
        viewport = game.getViewport();
    }

    @Override
    public void show() {
        background = new Texture("FrontCover.png");

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);


        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Skapa knappen
        chooseFighterButton = new TextButton("Choose Fighters", skin);
        chooseFighterButton.setSize(200, 50);
        chooseFighterButton.setPosition(
                (viewport.getWorldWidth() - chooseFighterButton.getWidth()) / 2,
                (viewport.getWorldHeight() - 40 - chooseFighterButton.getHeight()) / 2
        );

        // Lägg till en ClickListener på knappen
        chooseFighterButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new StartMenuScreen(game));
            }
        });


        stage.addActor(chooseFighterButton);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Börja batch-rendering
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());  // Rita bakgrunden
        batch.end();


        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();  // Rita knappen på skärmen
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
        background.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
