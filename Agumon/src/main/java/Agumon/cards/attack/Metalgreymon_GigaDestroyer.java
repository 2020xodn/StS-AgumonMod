package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.util.CardFontSize;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Agumon.AgumonMod.makeAttackCardPath;

public class Metalgreymon_GigaDestroyer extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Metalgreymon_GigaDestroyer.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(Metalgreymon_GigaDestroyer.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.RARE;

    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DMG = 4;

    public Metalgreymon_GigaDestroyer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.JPN);

        return CardFontSize.getProperSizeForLanguage(lanList);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new DrawCardAction(p, 2));
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
        return new Metalgreymon_GigaDestroyer();
    }
}