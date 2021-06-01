package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import Agumon.powers.burnPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeAttackCardPath;

public class BigExplosion extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(BigExplosion.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(BigExplosion.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;
    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 2;

    public BigExplosion() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();

        int weight;
        if (!upgraded)
            weight = 2;
        else
            weight = 3;

        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying && monster.hasPower(burnPower.POWER_ID)) {
                monster.getPower(burnPower.POWER_ID).amount *= weight;
                addToBot(new DamageAction(monster, new DamageInfo(p, magicNumber * monster.getPower(burnPower.POWER_ID).amount,
                        damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new BigExplosion();
    }
}