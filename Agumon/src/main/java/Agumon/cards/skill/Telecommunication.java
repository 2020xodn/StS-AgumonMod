package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.actions.TelecommunicationAction;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.util.CardFontSize;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Agumon.AgumonMod.makeSkillCardPath;

public class Telecommunication extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Telecommunication.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(Telecommunication.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 1;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public Telecommunication() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.JPN);
        lanList.add(Settings.GameLanguage.ENG);

        return CardFontSize.getProperSizeForLanguage(lanList);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p.drawPile.size() > 0){
            return super.canUse(p, m);
        }
        this.cantUseMessage = CardCrawlGame.languagePack.getCharacterString(Agumon.ID).TEXT[4];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TelecommunicationAction(magicNumber));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            initializeDescription();
        }
    }
}
