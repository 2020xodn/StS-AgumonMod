package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import Agumon.powers.dataAbsorbPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makePowerCardPath;

public class DataAbsorb extends AbstractDynamicCard {

    public static final String ID = AgumonMod.makeID(DataAbsorb.class.getSimpleName());
    public static final String IMG = makePowerCardPath(DataAbsorb.class.getSimpleName() + "b.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int MAGIC = 1;
    private static final int COST = 1;
    private static final int COST_COURAGE = 3;

    public DataAbsorb() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (AboutCourage.HasEnoughCourage(COST_COURAGE)){
            return super.canUse(p, m);
        }
        this.cantUseMessage = AboutCourage.NotEnoughCourage();
        return false;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new dataAbsorbPower(p, p, magicNumber), magicNumber));

        AboutCourage.ReduceCourage(COST_COURAGE);

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}