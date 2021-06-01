package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.actions.PostureCorrectionAction;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;

public class PostureCorrection extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(PostureCorrection.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(PostureCorrection.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 9;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int MAGIC_DAMAGE = 1;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public PostureCorrection() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        if (Settings.language == Settings.GameLanguage.ENG){
            return 19.0f;
        }else{
            return 21.0F;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new PostureCorrectionAction(p, magicNumber));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
