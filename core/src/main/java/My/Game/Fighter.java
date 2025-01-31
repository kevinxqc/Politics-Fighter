package My.Game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Fighter {
    public float x, y;
    private float speedY = 0;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> hitAnimation;
    private TextureRegion idleFrame;
    private TextureRegion currentFrame;
    private TextureRegion duckFrame;
    private TextureRegion redFrame;
    private TextureRegion winFrame;
    private TextureRegion deadFrame;

    private float stateTime = 0;

    private boolean isMoving = false;
    private boolean isJumping = false;
    private boolean isHitting = false;
    public boolean isDucking = false;

    private Texture texture;
    private boolean hasAnimation;
    private int hp;
    private int range;
    private int damage;
    private int PLAYER_HEIGHT = 100;
    private int PLAYER_WIDTH = 64;

    private float abilityHeight, abilityWidth, abilitySpeed, abilityY;
    private int abilityDamage;

    private static final float GRAVITY = -0.3f;
    private static final float JUMP_STRENGTH = 9;
    private static final float MIN_GROUND_Y = 50;
    private float hitDelayTime = 0;

    private Ability ability;
    private TextureRegion abilityTexture;

    private float abilityCooldownTime;
    private float abilityCooldownTimer;
    private boolean abilityOnCooldown;

    private boolean isHit = false;
    private float hitTimer = 0;
    private boolean gameOver = false;



    public Fighter(int fighterIndex, float startX, float startY) {
        this.x = startX;
        this.y = Math.max(startY, MIN_GROUND_Y);

        //Skapa animationer för att gå
        Texture spriteSheet = new Texture("spritesheet"+fighterIndex+".png");
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, 651, 1024);
        this.walkAnimation = new Animation<>(0.1f, frames[0]);
        this.idleFrame = frames[0][0];

        //Skapa animationer för slag
        Texture hitSpriteSheet = new Texture("fighter"+fighterIndex+"hit.png");
        TextureRegion[][] hitFrames = TextureRegion.split(hitSpriteSheet, 769, 1024);
        this.hitAnimation = new Animation<>(0.1f, hitFrames[0]);

        this.currentFrame = idleFrame;
        this.hasAnimation = true;

        this.ability = null;
        this.abilityTexture = new TextureRegion(new Texture("ability"+fighterIndex+".png"));

        this.abilityCooldownTimer = 0f;
        this.abilityCooldownTime = 5f;
        this.abilityOnCooldown = false;

        this.duckFrame = new TextureRegion(new Texture("fighter"+fighterIndex+"duck.png"));
        this.redFrame = new TextureRegion(new Texture("fighter"+fighterIndex+"red.png"));
        this.winFrame = new TextureRegion(new Texture("fighter"+fighterIndex+"win.png"));
        this.deadFrame = new TextureRegion(new Texture("fighter"+fighterIndex+"dead.png"));

        initStats(fighterIndex);
    }

    public void initStats(int fighterIndex) {
        switch (fighterIndex) {
            case 1:
                hp = 1050;
                range = 15;
                damage = 70;
                break;
            case 2:
                hp = 1100;
                range = 25;
                damage = 75;
                abilityDamage = 150;
                abilitySpeed = 7;
                abilityHeight = 60;
                abilityWidth = 60;
                abilityY = PLAYER_HEIGHT;
                break;
            case 3:
                hp = 1000;
                range = 20;
                damage = 80;
                abilityDamage = 220;
                abilityHeight = 100;
                abilityWidth = 100;
                abilitySpeed = 5;
                abilityY = 30;
                break;
            default:
                hp = 1000;
                range = 25;
                damage = 50;
                break;
        }
    }

    public int getHp() {
        return hp;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getRange(){
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public int getAbilityDamage(){
        return abilityDamage;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public int getPlayerWidth(){
        return PLAYER_WIDTH;
    }

    public int getPlayerHeight(){
        return PLAYER_HEIGHT;
    }

    public TextureRegion getFrame() {
        return currentFrame;
    }

    public void move(float dx, float dy, float minX, float maxX, float minY, float maxY) {
        if(!gameOver) {
            x = Math.max(minX, Math.min(x + dx, maxX));
            y = Math.max(minY, Math.min(y + dy, maxY));
            isMoving = hasAnimation && dx != 0;
        }
    }

    public void jump() {
        if (!isJumping && !gameOver) {
            speedY = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void hit(){
        isHitting = true;
    }

    public void duck(){
        isDucking = true;
    }
    public void stopDuck(){
        isDucking = false;
    }

    public void gotHit(){
        isHit = true;
        hitTimer = 0;
    }

    public void useAbility() {
        if (!abilityOnCooldown && (ability == null || !ability.isActive())) {
            float projectileX = this.x + (currentFrame.isFlipX() ? -PLAYER_WIDTH : PLAYER_WIDTH);

            // Skapa en ny projektil
            ability = new Ability(projectileX, abilityY, currentFrame.isFlipX(), abilityTexture, abilityWidth, abilityHeight, abilitySpeed);
            abilityOnCooldown = true;
            abilityCooldownTimer = abilityCooldownTime;
        }
    }

    public void updateAbility (float delta) {
        if (ability != null && ability.isActive()) {
            ability.update(delta);
        }
        if (abilityOnCooldown) {
            abilityCooldownTimer -= delta;
            if (abilityCooldownTimer <= 0) {
                abilityOnCooldown = false; // Cooldown över
                abilityCooldownTimer = 0;  // Återställ timer
            }
        }
    }

    public void renderAbility() {
        if (ability != null && ability.isActive()) {
            ability.render();
        }
    }

    public boolean checkProjectileCollision(Fighter opponent) {
        if (ability != null && ability.isActive() && ability.getBounds().overlaps(opponent.getBounds())) {
            ability.deactivate();
            return true;
        }
        return false;
    }

    public void makeWinner(){
        currentFrame = winFrame;
        gameOver = true;
    }
    public void kill(){
        currentFrame = deadFrame;
        gameOver = true;
    }

    public void update(float delta) {

        if(!gameOver) {


        if (isHit) {
            TextureRegion oldFrame = currentFrame;
            currentFrame = redFrame;

            // Flippa framen så att man slår mot rätt håll
            if (oldFrame.isFlipX() && !currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            } else if (!oldFrame.isFlipX() && currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }

            hitTimer += delta;
            if (hitTimer > 0.3f) { // Om mer än 0,3 sekunder har gått
                isHit = false;
            }
        }
        if(!isDucking) PLAYER_HEIGHT = 100;
        if (isJumping) {
            speedY += GRAVITY;
            y += speedY;

            if (y <= MIN_GROUND_Y) {
                y = MIN_GROUND_Y;
                isJumping = false;
                speedY = 0;
            }
        }


        if (hasAnimation) {
            if(isHitting){
                stateTime += delta;

                TextureRegion oldFrame = currentFrame;
                currentFrame = hitAnimation.getKeyFrame(stateTime, true);

                // Flippa framen så att man slår mot rätt håll
                if (oldFrame.isFlipX() && !currentFrame.isFlipX()) {
                    currentFrame.flip(true, false);
                } else if (!oldFrame.isFlipX() && currentFrame.isFlipX()) {
                    currentFrame.flip(true, false);
                }

                // Vänta i 250 ms före man går till vanliga framen
                if (hitAnimation.isAnimationFinished(stateTime)) {
                    hitDelayTime += delta;
                    if (hitDelayTime >= 0.2f) { // 250 millisekunder
                        isHitting = false;
                        hitDelayTime = 0;
                    }
                }

            } else if (isDucking) {
                TextureRegion oldFrame = currentFrame;
                currentFrame = duckFrame;
                PLAYER_HEIGHT = 70; //ducka

                // Flippa framen så att man slår mot rätt håll
                if (oldFrame.isFlipX() && !currentFrame.isFlipX()) {
                    currentFrame.flip(true, false);
                } else if (!oldFrame.isFlipX() && currentFrame.isFlipX()) {
                    currentFrame.flip(true, false);
                }

            } else if (isMoving) {
                stateTime += delta;
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            } else if(!isHit){
                currentFrame = walkAnimation.getKeyFrame(stateTime, false);
            }
        }
        updateAbility(delta);
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);

    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
