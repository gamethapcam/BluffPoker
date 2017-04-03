package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.logic.DiceLocation;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.stages.GameStage;

import java.util.Random;

public class Dice extends Stack implements Lockable {
    private Cup cup;
    private Texture[] diceTextures;
    private int diceValue;
    private DiceLocation location;
    private boolean underCup = true;
    private Group dicesBeforeCupActors, dicesUnderCupActors;
    private Image diceImage;
    private Image lockImage;
    private boolean lock = false;

    public enum ThrowResult {
        // Dice was locked, so it was not thrown.
        LOCKED,
        // Dice was thrown outside of the cup.
        OPEN,
        // Dice was thrown under the cup.
        UNDER_CUP;
    }

    private SpriteDrawable[] spriteDrawables = new SpriteDrawable[6];

    public Dice(Cup cup, Texture[] diceTextures, Texture lockTexture, int initialValue, DiceLocation location, Group dicesBeforeCupActors, Group dicesUnderCupActors) {
        diceImage = new Image(diceTextures[initialValue - 1]);
        lockImage = new Image(lockTexture);
        lockImage.setVisible(false);
        add(diceImage);
        add(lockImage);
        this.cup = cup;
        this.diceTextures = diceTextures;
        diceValue = initialValue;
        this.location = location;
        this.dicesBeforeCupActors = dicesBeforeCupActors;
        this.dicesUnderCupActors = dicesUnderCupActors;

        dicesUnderCupActors.addActor(this);

        setWidth(getDiceWidth() / 2);
        setHeight(getDiceHeight() / 2);

        initializeSpriteDrawables();
    }

    private void initializeSpriteDrawables() {
        for (int i = 0; i < diceTextures.length; i++) {
            spriteDrawables[i] = new SpriteDrawable(new Sprite(diceTextures[i]));
        }
    }

    public ThrowResult throwDice() {
        if ((isUnderCup() && !cup.isLocked()) || (!isUnderCup() && !isLocked())) {
            generateRandomNumber();
            if (isUnderCup()) {
                return ThrowResult.UNDER_CUP;
            } else {
                return ThrowResult.OPEN;
            }
        } else {
            if (diceValue != 6) {
                unlock();
            }
            return ThrowResult.LOCKED;
        }
    }

    private void generateRandomNumber() {
        Random randomDiceNumber = new Random();
        int randomNumber = randomDiceNumber.nextInt(6);

        diceImage.setDrawable(spriteDrawables[randomNumber]);
        diceValue = randomNumber;
    }

    public void calculateAndSetPosition() {
        float x = (GameStage.getMiddleX() / BluffPokerGame.getDivideScreenByThis()) - ((getDiceWidth() / 2) / 2);
        switch (location) {
            case LEFT:
                // This is the left dice, so we place it slightly left of the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                x = x - (getDiceWidth() / 2);
                break;
            case MIDDLE:
                // This is the middle dice, so we place it in the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                break;
            case RIGHT:
                x = x + (getDiceWidth() / 2);
                break;
        }

        float y = cup.getMiddleYForCup() + (getDiceHeight() / (3 + BluffPokerGame.getDivideScreenByThis()));
        setPosition(x, y);
    }

    private int getDiceWidth() {
        return diceTextures[0].getWidth() / 2;
    }

    private int getDiceHeight() {
        return diceTextures[0].getHeight() / 2;
    }

    public boolean isUnderCup() {
        return underCup;
    }

    public void pullAwayFromCup() {
        if ((cup.isBelieving() || cup.isWatchingOwnThrow()) && isUnderCup()) {
            underCup = false;
            moveBy(0, -getDiceHeight() / 2);
            dicesUnderCupActors.removeActor(this);
            dicesBeforeCupActors.addActor(this);
        }
    }

    public void putBackUnderCup() {
        if (cup.isBelieving() || cup.isWatchingOwnThrow()) {
            reset();
        }
    }

    public void reset() {
        if (!underCup) {
            if (cup.isLocked()) {
                lock();
            } else {
                unlock();
            }
            underCup = true;
            moveBy(0, getDiceHeight() / 2);
            dicesBeforeCupActors.removeActor(this);
            dicesUnderCupActors.addActor(this);
        }
    }

    public int getDiceValue() {
        return diceValue + 1;
    }

    @Override
    public void lock() {
        if (!isUnderCup() || cup.isLocked()) {
            lock = true;
            lockImage.setVisible(true);
        }
    }

    @Override
    public void unlock() {
        lock = false;
        lockImage.setVisible(false);
    }

    @Override
    public boolean isLocked() {
        return lock;
    }
}
