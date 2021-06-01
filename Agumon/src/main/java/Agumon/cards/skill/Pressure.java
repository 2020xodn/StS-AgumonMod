package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;

import static Agumon.AgumonMod.makeSkillCardPath;

public class Pressure extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Pressure.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(Pressure.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int MAGIC_DAMAGE = 2;

    public Pressure() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
        this.exhaust = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (Agumon.EVOLUTION_STAGE >= 2)
            return super.canUse(p, m);

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AboutCourage.gainCourage(3);

        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();

        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                addToBot(new ApplyPowerAction(m, m, new WeakPower(m, this.magicNumber, false), this.magicNumber));
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.magicNumber, false), this.magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
