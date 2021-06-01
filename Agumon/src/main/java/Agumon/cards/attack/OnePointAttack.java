package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.actions.KillAndDrawAction;
import Agumon.characters.Agumon;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Agumon.AgumonMod.makeAttackCardPath;

public class OnePointAttack extends CustomCard {
    public static final String ID = AgumonMod.makeID(OnePointAttack.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeAttackCardPath(OnePointAttack.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 17;
    private static final int UPGRADE_PLUS_DMG = 6;
    private static final int MAGIC_DAMAGE = 2;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public OnePointAttack() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new KillAndDrawAction(p, m,  new DamageInfo(p, this.damage, this.damageTypeForTurn), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_M_DMG);
            initializeDescription();
        }
    }
}
