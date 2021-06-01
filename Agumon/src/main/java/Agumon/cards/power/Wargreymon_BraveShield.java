package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import Agumon.powers.braveShieldPower;
import Agumon.util.CardFontSize;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Agumon.AgumonMod.makePowerCardPath;

public class Wargreymon_BraveShield extends CustomCard {

    public static final String ID = AgumonMod.makeID(Wargreymon_BraveShield.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(Wargreymon_BraveShield.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;


    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int COST_COURAGE = 3;
    private static final int MAGIC_DAMAGE = 7;

    public Wargreymon_BraveShield() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.JPN);

        return CardFontSize.getProperSizeForLanguage(lanList);
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
        AboutCourage.ReduceCourage(COST_COURAGE);

        this.addToBot(new ApplyPowerAction(p, p, new braveShieldPower(p, this.magicNumber), this.magicNumber));

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