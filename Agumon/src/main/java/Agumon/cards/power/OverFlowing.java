package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.characters.Agumon;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Agumon.AgumonMod.makePowerCardPath;

public class OverFlowing extends CustomCard {

    public static final String ID = AgumonMod.makeID(OverFlowing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(OverFlowing.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;


    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 30;
    private static final int UPGRADE_PLUS_M_DMG = -5;

    private static int amountIncrease;

    public OverFlowing() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((new ApplyPowerAction(p, p, new StrengthPower(p, amountIncrease), amountIncrease)));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        amountIncrease = (int)(AbstractDungeon.player.maxHealth / magicNumber);
        Agumon.logger.info("OverFlowing | MAXHP : " + AbstractDungeon.player.maxHealth);
        Agumon.logger.info("OverFlowing | amountIncrease : " + amountIncrease);
        this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + (amountIncrease);
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            initializeDescription();
        }
    }
}