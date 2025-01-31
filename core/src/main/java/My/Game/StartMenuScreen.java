package My.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartMenuScreen implements Screen {
    private final FighterGame game;
    private SpriteBatch batch;

    private int player1SelectedFighter = 0;
    private int player2SelectedFighter = 1;


    private boolean player1Ready = false;
    private boolean player2Ready = false;


    private final String[] fighterNames = {"America", "Gopnik"};  // Namn på fighterna

    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture fighterTexture;

    public StartMenuScreen(FighterGame game) {
        this.game = game;
        fighterTexture = new Texture("fighterSelection0.png");


        camera = game.getCamera();
        viewport = game.getViewport();
        batch = game.getBatch();
    }

    @Override
    public void show() {
        batch = game.getBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        //fighterval för player 1 och player 2
        game.getFont().draw(batch, "Player 1 Fighter: " + fighterNames[player1SelectedFighter], 80, 220);
        game.getFont().draw(batch, "Player 2 Fighter: " + fighterNames[player2SelectedFighter], 580, 220);

        batch.draw(fighterTexture,0 ,0,800,600);

        if (player1Ready) {
            game.getFont().draw(batch, "Ready!", 100, 50);
        }
        if (player2Ready) {
            game.getFont().draw(batch, "Ready!", 600, 50);
        }

        // När båda spelarna är klara, byt till GameScreen
        if (player1Ready && player2Ready) {
            game.setScreen(new GameScreen(game, player1SelectedFighter, player2SelectedFighter));
        }

        batch.end();
        handleInput();
    }

    private void handleInput() {
        // Player 1 Fighter Selection

        if ((Gdx.input.isKeyJustPressed(Input.Keys.A) || (Gdx.input.isKeyJustPressed(Input.Keys.D)))) {
            if(player1SelectedFighter == 1){
                player1SelectedFighter = 0;
                player2SelectedFighter = 1;
                fighterTexture = new Texture("fighterSelection0.png");
            }
            else{
                player1SelectedFighter = 1;
                player2SelectedFighter = 0;
                fighterTexture = new Texture("fighterSelection1.png");
            }
        }







        // När Player 1 är klar
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            player1Ready = true;
        }

        // När Player 2 är klar
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            player2Ready = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {

    }
}
