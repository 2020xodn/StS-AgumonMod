package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.characters.Agumon;
import Agumon.powers.surprisedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makePowerCardPath;

public class SurprisedParty extends CustomCard {

    public static final String ID = AgumonMod.makeID(SurprisedParty.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(SurprisedParty.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int MAGIC_DAMAGE = 1;
    private static final int COST = 1;
    private static final int NEEDBRAVE = 3;
    private static final int UPGRADE_COST = 0;

    public SurprisedParty() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
        baseBlock = NEEDBRAVE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new surprisedPower(p, this.magicNumber), this.magicNumber));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}