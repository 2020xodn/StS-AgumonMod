package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeAttackCardPath;
import static Agumon.cards.AboutCourage.*;

public class HeadButt extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(HeadButt.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(HeadButt.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;
    private static final int COST = 2;
    private static int COST_COURAGE = 2;
    private static final int DAMAGE = 13;
    private static final int MAGIC_DAMAGE = 2;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;

    public HeadButt() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
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
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE)) {
            ReduceCourage(COST_COURAGE);

            if (m != null && m.getIntentBaseDmg() >= 0) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage * magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
            } else {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
            }
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