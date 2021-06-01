package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.powers.couragePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Agumon.AgumonMod.makeSkillCardPath;
import static Agumon.cards.AboutCourage.HasEnoughCourage;
import static Agumon.cards.AboutCourage.gainCourageNotEnough;

public class InheritDragon extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(InheritDragon.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(InheritDragon.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;

    private static int costEffected = COST;
    public static int countCourage;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;

    public InheritDragon() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (!HasEnoughCourage(1)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(1) && !isTmpCostStored){
            setCostForTurn(cost);
            magicNumber = baseMagicNumber = AbstractDungeon.player.getPower(couragePower.POWER_ID).amount;
        }
        else if(HasEnoughCourage(1)){
            setCostForTurn(tmpCost);
            magicNumber = baseMagicNumber = AbstractDungeon.player.getPower(couragePower.POWER_ID).amount;
        }
        else{
            if (!isTmpCostStored && isCostModifiedForTurn){
                tmpCost = this.costForTurn;
                isTmpCostStored = true;
            }
            setCostForTurn(0);
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(1)) {
            addToBot(new RemoveSpecificPowerAction(p, p, couragePower.POWER_ID));
            addToBot((new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));
        }
        else{
            gainCourageNotEnough(upgraded);
        }
        isTmpCostStored = false;
    }

    @Override
    public void atTurnStart() {
        isTmpCostStored = false;
    }

    @Override
    public void applyPowers() {
//        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;

        if (p.hasPower(couragePower.POWER_ID)) {
            countCourage = p.getPower(couragePower.POWER_ID).amount;   // Normal -> 33%
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + countCourage;
        }
        else{
            countCourage = 0;
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + countCourage;
        }

        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}