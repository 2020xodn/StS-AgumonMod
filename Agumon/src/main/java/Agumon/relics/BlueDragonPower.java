package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.cards.AboutDigivolution;
import Agumon.characters.Agumon;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import static Agumon.AgumonMod.*;

public class BlueDragonPower extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib

    public static final String ID = AgumonMod.makeID(BlueDragonPower.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(BlueDragonPower.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(BlueDragonPower.class.getSimpleName() + ".png"));

    private boolean usedThisGame = false;
    private boolean isPlayerTurn = false;

    public BlueDragonPower() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void onRightClick() {// On right click
        if (!isObtained || usedThisGame || !isPlayerTurn) {
            return;
        }

        if (canUse()){
            usedThisGame = true;
            flash();
            stopPulse();

            this.grayscale = true;
            this.usedUp = true;
            this.description = this.DESCRIPTIONS[1];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();

            Agumon.EVOLUTION_STAGE = 3;
            Agumon.AMOUNT_EVOLUTION++;

            AboutDigivolution.Digivolution(false);
            AboutDigivolution.EvolutionCards(false);
            AboutDigivolution.ClearStage();
            Agumon.isWargreymon = true;

        }

    }
    
    @Override
    public void atPreBattle() {
        if (!usedThisGame && canUse())
            beginLongPulse();
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;

        if (!usedThisGame && canUse())
            beginLongPulse();
    }
    
    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;

        if (!usedThisGame && canUse())
            stopPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public static boolean canUse(){
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite){
                if(Agumon.EVOLUTION_STAGE == 0){
                    return true;
                }
            }
        }

        return false;
    }

}
