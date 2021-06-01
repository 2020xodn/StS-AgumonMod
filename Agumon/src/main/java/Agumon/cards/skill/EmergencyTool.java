package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;
import static Agumon.cards.AboutCourage.*;

public class EmergencyTool extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(EmergencyTool.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(EmergencyTool.class.getSimpleName() + ".png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_DAMAGE = 4;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;
    private static final int COST_COURAGE = 5;


    public EmergencyTool() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
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
            if (!isTmpCostStored && !isCostModifiedForTurn){
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
            ReduceCourage(COST_COURAGE);

            int countHand = AbstractDungeon.player.hand.size();
            addToBot(new DiscardAction(p, p, countHand, true));
            addToBot(new DrawCardAction(p, magicNumber));
            addToBot(new GainEnergyAction(magicNumber));
        }
        else{
            gainCourageNotEnough(upgraded);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
