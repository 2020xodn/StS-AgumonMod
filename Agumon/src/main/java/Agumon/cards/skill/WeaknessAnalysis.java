package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static Agumon.AgumonMod.makeSkillCardPath;

public class WeaknessAnalysis extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(WeaknessAnalysis.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(WeaknessAnalysis.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;;
    private static final int MAGIC_DAMAGE = 3;
    private static final int UPGRADE_PLUS_M_DMG = 1;


    public WeaknessAnalysis() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
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
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
