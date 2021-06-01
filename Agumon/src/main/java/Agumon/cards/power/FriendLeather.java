package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static Agumon.AgumonMod.makePowerCardPath;

public class FriendLeather extends CustomCard {

    public static final String ID = AgumonMod.makeID(FriendLeather.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(FriendLeather.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int MAGIC_DAMAGE = 3;
    private static final int UPGRADE_PLUS_M_DMG = 1;
    private static final int COST = 1;
    private static final int COST_COURAGE = 3;

    public FriendLeather() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
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
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));

        AboutCourage.ReduceCourage(COST_COURAGE);

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