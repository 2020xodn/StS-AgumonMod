package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.cards.AboutDigivolution;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makePowerCardPath;

public class Digivolution extends AbstractDynamicCard {

    public static final String ID = AgumonMod.makeID(Digivolution.class.getSimpleName());
    public static final String IMG = makePowerCardPath(Digivolution.class.getSimpleName() + ".png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int MAGIC = 1;
    private static final int COST = 1;
    private static final int COST_COURAGE = 6;
    private static final int UPGRADE_COST = 0;

    public Digivolution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        this.selfRetain = true;
    }


    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        Agumon.logger.info("진화 | retain? : " + this.retain);
//        if (p.currentHealth <= p.maxHealth * 0.9) {
//            if (AboutCourage.HasEnoughCourage(COST_COURAGE)) {
//                return super.canUse(p, m);
//            } else {
//                this.cantUseMessage = AboutCourage.NotEnoughCourage();
//            }
//        }

        if (AboutCourage.HasEnoughCourage(COST_COURAGE)){
            return super.canUse(p, m);
        }
        else{
            this.cantUseMessage = AboutCourage.NotEnoughCourage();
        }
        return false;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Agumon.EVOLUTION_STAGE = 1;
        Agumon.AMOUNT_EVOLUTION++;

        AboutCourage.ReduceCourage(COST_COURAGE);
        AboutDigivolution.Digivolution(true);
        AboutDigivolution.EvolutionCards(true);

    }

//    public void applyPowers() {
//
//        this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + (int)(AbstractDungeon.player.maxHealth * 0.9);
//
//        this.initializeDescription();
//    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}