package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.powers.couragePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;
import static Agumon.cards.AboutCourage.*;

public class RewardingBreak extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(RewardingBreak.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(RewardingBreak.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int COST_COURAGE = 1;
    public static int countCourage;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;

    public RewardingBreak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (!HasEnoughCourage(COST_COURAGE)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE) && !isTmpCostStored){
            setCostForTurn(cost);
            this.exhaust = true;
        }
        else if(HasEnoughCourage(COST_COURAGE)){
            setCostForTurn(tmpCost);
            this.exhaust = true;
        }
        else{
            if (!isTmpCostStored && isCostModifiedForTurn){
                tmpCost = this.costForTurn;
                isTmpCostStored = true;
            }
            setCostForTurn(0);
            this.exhaust = false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE)) {
            addToBot((new HealAction(p, p, countCourage)));
            addToBot(new DrawCardAction(p, 1));
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
            if (!upgraded) {
                countCourage = p.getPower(couragePower.POWER_ID).amount / 2;   // Normal -> 50%
                rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + countCourage;
            } else {
                countCourage = p.getPower(couragePower.POWER_ID).amount;   // Upgraded -> 100%
                rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION + countCourage;
            }
        }
        else{
            countCourage = 0;
            if (!upgraded) {
                rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + countCourage;
            } else {
                rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION + countCourage;
            }
        }

        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
