package Agumon.powers;

import Agumon.AgumonMod;
import Agumon.characters.Agumon;
import Agumon.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Agumon.AgumonMod.makePowerPath;

public class evolvePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = AgumonMod.makeID(evolvePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(evolvePower.class.getSimpleName() + "84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(evolvePower.class.getSimpleName() + "32.png"));

    public evolvePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (Agumon.EVOLUTION_STAGE == 0)
            description = DESCRIPTIONS[0];
        else if (Agumon.EVOLUTION_STAGE == 1)
            description = DESCRIPTIONS[1];
        else if (Agumon.EVOLUTION_STAGE == 2)
            description = DESCRIPTIONS[2];
        else if (Agumon.EVOLUTION_STAGE == 3)
            description = DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[4];

        this.amount = 0;
        AgumonMod.logger.info("evolvePower | Stage : " + Agumon.EVOLUTION_STAGE);
        AgumonMod.logger.info("evolvePower | Ev Amount : " + Agumon.AMOUNT_EVOLUTION);
    }

    @Override
    public AbstractPower makeCopy() {
        return new evolvePower(owner, source, amount);
    }
}
