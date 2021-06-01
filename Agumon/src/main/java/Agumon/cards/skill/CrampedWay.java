package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Iterator;

import static Agumon.AgumonMod.makeSkillCardPath;
import static Agumon.cards.AboutCourage.*;

public class CrampedWay extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(CrampedWay.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(CrampedWay.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int COST_COURAGE = 4;
    private static final int MAGIC_DAMAGE = 2;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;

    public CrampedWay() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
//        this.exhaust = true;
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
        }
        else if(HasEnoughCourage(COST_COURAGE)){
            setCostForTurn(tmpCost);
        }
        else{
            if (!isTmpCostStored && !isCostModifiedForTurn){
                tmpCost = this.costForTurn;
                isTmpCostStored = true;
            }
            setCostForTurn(0);
            return super.canUse(p, m);
        }

        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();   // monster single check
        int countMonster = 0;
        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                countMonster++;
            }

        }

        if (countMonster == 1){
            return super.canUse(p, m);
        }
        else{
            this.cantUseMessage = CardCrawlGame.languagePack.getCharacterString(Agumon.ID).TEXT[4];
            return false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE)) {
            AboutCourage.ReduceCourage(COST_COURAGE);

            this.addToBot(new DrawCardAction(p, 1));
            this.addToBot((new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber)));
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
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
