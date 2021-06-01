package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.power.Digivolution;
import Agumon.characters.Agumon;
import Agumon.util.UI_CardsPreview;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;

public class Greymon_Defend extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Greymon_Defend.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(Greymon_Defend.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Greymon_Defend() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        this.cardsToPreview = new Digivolution();
    }

    @Override
    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        UI_CardsPreview.MyRenderCardsPreviewInSingleView(sb, "Defend", this.upgraded, 1);
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {     // Not Using
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Greymon_Defend();
    }
}
