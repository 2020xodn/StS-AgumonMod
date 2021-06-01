package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;
import static Agumon.cards.AboutDigivolution.cardStrings;

public class LastHope extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(LastHope.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(LastHope.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_DAMAGE = 8;
    private static final int UPGRADE_PLUS_M_DMG = 4;

    public LastHope() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (p.drawPile.size() > 0) {
            this.cantUseMessage = cardStrings.UPGRADE_DESCRIPTION;
            return false;
        } else {
            return canUse;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AboutCourage.gainCourage(magicNumber);
    }


    public AbstractCard makeCopy() {
        return new LastHope();
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
