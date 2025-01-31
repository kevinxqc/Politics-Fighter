package My.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen implements Screen {
    private final FighterGame game;
    private SpriteBatch batch;
    private Texture background;
    private Fighter player1, player2;
    private PlayerController player1Controller, player2Controller;
    private int player1Hp, player2Hp;
    private ShapeRenderer shapeRenderer;
    private boolean gameStarted = false;
    private float countdownTime = 3;
    private boolean countdownStarted = false;
    private boolean gameOver = false;
    private float gameOverTimer = 5;

    public GameScreen(FighterGame game, int player1FighterIndex, int player2FighterIndex) {
        this.game = game;
        this.batch = game.getBatch();
        this.background = new Texture("background2.png");


        player1 = new Fighter(player1FighterIndex+2, 100, 100);
        player2 = new Fighter(player2FighterIndex+2, 600, 100);





        player1Controller = new PlayerController(player1, Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.R, Input.Keys.F);
        player2Controller = new PlayerController(player2, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.CONTROL_RIGHT, Input.Keys.PERIOD);

        Ability.setBatch(batch);

        player1Hp = player1.getHp();
        player2Hp = player2.getHp();


        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

        game.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        game.getCamera().position.set(game.getCamera().viewportWidth / 2, game.getCamera().viewportHeight / 2, 0);
        countdownStarted = true;
        player1.jump();
        player2.jump();

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getCamera().update();

        game.getBatch().setProjectionMatrix(game.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, game.getCamera().viewportWidth, game.getCamera().viewportHeight);

        // Visa nedräkning innan spelet startar
        if (countdownStarted) {
            if (countdownTime > 1) {
                game.getFont().draw(batch, "Starting in: " + (int) countdownTime, 350, 500);
            } else if (countdownTime <= 1 && countdownTime > 0) {
                game.getFont().draw(batch, "START", 400, 400);
            }

            countdownTime -= delta;  // Minska nedräkningen med tiden som har gått


            if (countdownTime <= 0 && !gameStarted) {
                gameStarted = true;
                countdownTime = 0;  // Stoppa nedräkningen vid 0
            }
        }


        if (gameStarted) {
            player1Controller.handleInput(delta);
            player2Controller.handleInput(delta);

            // Rita spelarna
            player1.render(game.getBatch());
            player2.render(game.getBatch());
        }



        // Visa HP
        game.getFont().draw(batch, "Player 1 HP: " + player1Hp, 75, 550);
        game.getFont().draw(batch, "Player 2 HP: " + player2Hp, 600, 550);
        batch.end();

        // Rita hälsobars
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawHealthBar(50, 520, player1Hp, player1.getHp());
        drawHealthBar(550, 520, player2Hp, player2.getHp());
        shapeRenderer.end();

        // Hantera skada och kontrollera vinnare
        handleDamage();
        batch.begin();
        player1.renderAbility();
        player2.renderAbility();
        batch.end();
        if(player1.checkProjectileCollision(player2)){
            player2Hp -= player1.getAbilityDamage();
            player2.gotHit();
        }
        if(player2.checkProjectileCollision(player1)){
            player1Hp -= player2.getAbilityDamage();
            player1.gotHit();
        }
        checkWinners();

    }

    public void handleDamage() {
        if(!gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !Gdx.input.isKeyPressed(Input.Keys.S)) { // Om man slår, och inte blockar
                //animation
                player1.hit();
                player1.update(Gdx.graphics.getDeltaTime());
                //ta skada om man är inom ens range och player2 inte blockar
                if (Math.abs(player1.x - player2.x) < player1.getRange() + player1.getPlayerWidth() && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    player2Hp -= player1.getDamage();
                    player2.gotHit();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player2.hit();
                player2.update(Gdx.graphics.getDeltaTime());
                if (Math.abs(player1.x - player2.x) < player2.getRange() + player1.getPlayerWidth() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
                    player1Hp -= player2.getDamage();
                    player1.gotHit();
                }
            }
        }

    }


    public void drawHealthBar(float x, float y, int currentHp, int maxHp) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, 200, 15);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, (currentHp / (float) maxHp) * 200, 15);
    }

    public void checkWinners() {
        if (player1Hp <= 0) {
            player1.kill();
            player2.makeWinner();

            gameOverTimer -= Gdx.graphics.getDeltaTime();
            if (gameOverTimer <= 0) {
                    game.setScreen(new GameOverScreen(game, "Player 2 Wins!"));
            }


        } else if (player2Hp <= 0) {
            player2.kill();
            player1.makeWinner();
            gameOverTimer -= Gdx.graphics.getDeltaTime();
            if (gameOverTimer <= 0) {
                game.setScreen(new GameOverScreen(game, "Player 2 Wins!"));
            }

            gameOver = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height);
        game.getCamera().position.set(game.getCamera().viewportWidth / 2, game.getCamera().viewportHeight / 2, 0);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        player1.dispose();
        player2.dispose();
        background.dispose();
        shapeRenderer.dispose();
    }
}
