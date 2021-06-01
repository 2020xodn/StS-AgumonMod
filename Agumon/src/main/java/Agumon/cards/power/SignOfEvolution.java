package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.characters.Agumon;
import Agumon.powers.signPower;
import Agumon.util.CardFontSize;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Agumon.AgumonMod.makePowerCardPath;

public class SignOfEvolution extends CustomCard {

    public static final String ID = AgumonMod.makeID(SignOfEvolution.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makePowerCardPath(SignOfEvolution.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 1;
    public SignOfEvolution() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.ENG);

        return CardFontSize.getProperSizeForLanguage(lanList);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.addPower(new signPower(p, p, 1));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}