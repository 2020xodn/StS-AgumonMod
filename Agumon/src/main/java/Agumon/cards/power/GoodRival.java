package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.characters.Agumon;
import Agumon.powers.goodRivalPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makePowerCardPath;

public class GoodRival extends CustomCard {

    public static final String ID = AgumonMod.makeID(GoodRival.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(GoodRival.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;


    private static final int COST = 1;
    private static final int MAGIC = 7;                 // Courage
    private static final int UPGRADE_PLUS_M_DMG = 3;
    private int getStrengthAmount = 3;                  // Strength
    private int plusStrength = 3;

    public GoodRival() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new goodRivalPower(p, magicNumber, getStrengthAmount, 20), 0));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            getStrengthAmount += plusStrength;
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}