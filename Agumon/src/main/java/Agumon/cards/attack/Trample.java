package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeAttackCardPath;

public class Trample extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Trample.class.getSimpleName());
    public static final String IMG = makeAttackCardPath(Trample.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;
    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int MAGIC_DAMAGE = 2;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public Trample() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (Agumon.EVOLUTION_STAGE >= 1)
            return super.canUse(p, m);

        return false;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();   // monster single check
        int countMonster = 0;
        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                countMonster++;
            }
        }

        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AboutCourage.gainCourage(magicNumber * countMonster);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();   // monster single check
        int countMonster = 0;
        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                countMonster++;
            }
        }

        this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + (magicNumber * countMonster);

        this.initializeDescription();
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