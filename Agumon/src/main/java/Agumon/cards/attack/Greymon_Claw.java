package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.power.Digivolution;
import Agumon.characters.Agumon;
import Agumon.util.UI_CardsPreview;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeAttackCardPath;

public class Greymon_Claw extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Greymon_Claw.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(Greymon_Claw.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;
    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Greymon_Claw() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;

        this.tags.add(CardTags.STRIKE);
        this.cardsToPreview = new Digivolution();
    }

    @Override
    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        UI_CardsPreview.MyRenderCardsPreviewInSingleView(sb, "Claw", this.upgraded, 1);
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {     // Not Using
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Greymon_Claw();
    }
}