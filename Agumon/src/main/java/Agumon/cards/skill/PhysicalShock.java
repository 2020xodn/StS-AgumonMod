package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeSkillCardPath;

public class PhysicalShock extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(PhysicalShock.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(PhysicalShock.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 4;
    private static final int UPGRADE_PLUS_M_DMG = 3;

    public PhysicalShock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, 2));
        addToBot(new RemoveDebuffsAction(AbstractDungeon.player));
        AboutCourage.gainCourage(magicNumber);

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
