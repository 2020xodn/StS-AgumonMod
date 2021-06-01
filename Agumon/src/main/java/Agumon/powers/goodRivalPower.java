package Agumon.powers;

import Agumon.AgumonMod;
import Agumon.cards.AboutCourage;
import Agumon.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Iterator;

import static Agumon.AgumonMod.makePowerPath;

public class goodRivalPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = AgumonMod.makeID(goodRivalPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(goodRivalPower.class.getSimpleName() + "84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(goodRivalPower.class.getSimpleName() + "32.png"));
    private static int courageAmount = 0;
    private static int strengthAmount = 0;
    private int needCardAmount;
    public goodRivalPower(final AbstractCreature owner, final int courageAmount, final int strengthAmount, final int needCardAmount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.courageAmount += courageAmount;
        this.strengthAmount += strengthAmount;
        this.needCardAmount = needCardAmount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        int count = 0;
        Iterator cardListPlayedThisGame = AbstractDungeon.actionManager.cardsPlayedThisCombat.iterator();

        while(cardListPlayedThisGame.hasNext()) {
            AbstractCard c = (AbstractCard)cardListPlayedThisGame.next();
            ++count;
        }
        this.amount = needCardAmount - count;
        checkUsed();
        updateDescription();

    }

    public void checkUsed(){
        if (amount <= 0){
            addToBot(new RemoveSpecificPowerAction(owner, owner, goodRivalPower.POWER_ID));
            AboutCourage.gainCourage(courageAmount);
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmount), strengthAmount));
            clearAmount();
        }
        else{
            updateDescription();
        }
    }

    public void clearAmount(){
        courageAmount = 0;
        strengthAmount = 0;
    }

    @Override
    public void onVictory() {
        clearAmount();
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        amount--;
        checkUsed();
    }

    @Override
    public void updateDescription() {
        if (amount > 0)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + courageAmount + DESCRIPTIONS[2] + strengthAmount + DESCRIPTIONS[3];
        else {
            description = DESCRIPTIONS[4];
            addToBot(new RemoveSpecificPowerAction(owner, owner, goodRivalPower.POWER_ID));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new goodRivalPower(owner, courageAmount, strengthAmount, needCardAmount);
    }
}
