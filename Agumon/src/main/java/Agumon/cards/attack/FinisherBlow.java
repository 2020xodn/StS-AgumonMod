package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.actions.DamagePerCardPlayedAction;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeAttackCardPath;
import static Agumon.cards.AboutCourage.*;

public class FinisherBlow extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(FinisherBlow.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(FinisherBlow.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int COST_COURAGE = 5;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int MAGIC_DAMAGE = 1;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public FinisherBlow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

//    @Override
//    public void triggerOnGlowCheck() {
//        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        if (!HasEnoughCourage(COST_COURAGE)) {
//            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        }
//    }
//
//    @Override
//    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        if (HasEnoughCourage(COST_COURAGE)){
//            setCostForTurn(COST);
//        }
//        else{
//            setCostForTurn(0);
//        }
//        return super.canUse(p, m);
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamagePerCardPlayedAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION;
        this.initializeDescription();
    }

    public void applyPowers() {
        super.applyPowers();
        int count = 0;
        Iterator cardListPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();

        while(cardListPlayedThisTurn.hasNext()) {
            AbstractCard c = (AbstractCard)cardListPlayedThisTurn.next();
            ++count;
        }

        this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + (damage * count);

        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}