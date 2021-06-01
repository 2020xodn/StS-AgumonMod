package Agumon.powers;

import Agumon.AgumonMod;
import Agumon.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Agumon.AgumonMod.makePowerPath;

public class burnPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = AgumonMod.makeID(burnPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(burnPower.class.getSimpleName() + "84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(burnPower.class.getSimpleName() + "32.png"));

    private AbstractMonster.EnemyType monsterType;
    private float damagePerType;
    public burnPower(final AbstractCreature owner, final AbstractCreature source, final int amount, AbstractMonster.EnemyType monsterType) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.monsterType = monsterType;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        if (monsterType == AbstractMonster.EnemyType.ELITE || monsterType == AbstractMonster.EnemyType.BOSS)
            damagePerType = 0.05F;
        else
            damagePerType = 0.10F;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public void atStartOfTurn() {
        this.flash();
        amount = amount - 1;
        int burning = (int)(owner.maxHealth * damagePerType);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(owner, new DamageInfo(owner, burning, DamageInfo.DamageType.HP_LOSS),
                        AbstractGameAction.AttackEffect.FIRE));

        if (amount == 0)
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (int)(this.owner.maxHealth * damagePerType);
    }

    @Override
    public AbstractPower makeCopy() {
        return new burnPower(owner, source, amount, monsterType);
    }
}
