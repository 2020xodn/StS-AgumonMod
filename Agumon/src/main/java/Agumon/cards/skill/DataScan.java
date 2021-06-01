package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.actions.DataScanAction;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.util.CardFontSize;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Agumon.AgumonMod.makeSkillCardPath;

public class DataScan extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(DataScan.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(DataScan.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_DAMAGE = 3;
    private static final int UPGRADE_PLUS_M_DMG = 2;


    public DataScan() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.JPN);

        return CardFontSize.getProperSizeForLanguage(lanList);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DataScanAction(1, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            initializeDescription();
        }
    }
}
