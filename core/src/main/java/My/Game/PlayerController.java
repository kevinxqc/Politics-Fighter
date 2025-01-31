package My.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerController {
    private Fighter fighter;
    private int upKey, downKey, leftKey, rightKey, abilityKey, abilityKey2;


    public PlayerController(Fighter fighter, int upKey, int downKey, int leftKey, int rightKey, int abilityKey, int abilityKey2) {
        this.fighter = fighter;
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.abilityKey = abilityKey;
        this.abilityKey2 = abilityKey2;
    }

    public void handleInput(float delta) {
        float dx = 0;
        float minX = 0;
        float maxX = 800 - fighter.getPlayerWidth();
        float minY = 0;
        float maxY = 480 - fighter.getPlayerHeight();


        if (Gdx.input.isKeyPressed(leftKey)) {
            if (!fighter.getFrame().isFlipX()) {
                fighter.getFrame().flip(true, false);
            }
            if(fighter.isDucking) dx = -0.5f;
            else dx = -2;
        }

        else if (Gdx.input.isKeyPressed(rightKey)) {
            if (fighter.getFrame().isFlipX()) {
                fighter.getFrame().flip(true, false);
            }
            if(fighter.isDucking) dx = 0.5f;
            else dx = 2;

        }

        if (Gdx.input.isKeyJustPressed(upKey) && !fighter.isDucking) {
            fighter.jump();
        }

        if(Gdx.input.isKeyJustPressed(abilityKey)){
            fighter.useAbility();
        }

        if(Gdx.input.isKeyJustPressed(abilityKey2)){
            if(fighter.getAbility() != null && fighter.getAbility().isActive()){
                fighter.getAbility().goFaster();
            }

        }
        if (Gdx.input.isKeyPressed(downKey)) {
            fighter.duck();
        }
        else{
            fighter.stopDuck();
        }






        fighter.move(dx, 0, minX, maxX, minY, maxY);
        fighter.update(delta);
    }
}

