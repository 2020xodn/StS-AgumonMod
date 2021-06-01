package Agumon.powers;

import Agumon.AgumonMod;
import Agumon.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Agumon.AgumonMod.makePowerPath;

public class surprisedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = AgumonMod.makeID(surprisedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(surprisedPower.class.getSimpleName() + "84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(surprisedPower.class.getSimpleName() + "32.png"));

    public surprisedPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.tags.contains(AbstractCard.CardTags.HEALING)){
            this.flash();
            this.addToBot(new GainEnergyAction(this.amount));
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
    }

    @Override
    public void updateDescription() {
        String plusDescription = "";
        for (int count = 0; count < amount; count++) {
            plusDescription = plusDescription + DESCRIPTIONS[1];
        }
        description = DESCRIPTIONS[0] + plusDescription;
    }

    @Override
    public AbstractPower makeCopy() {
        return new surprisedPower(owner, amount);
    }
}
