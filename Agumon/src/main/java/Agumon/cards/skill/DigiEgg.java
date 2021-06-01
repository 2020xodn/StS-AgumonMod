package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutDigivolution;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeSkillCardPath;

public class DigiEgg extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(DigiEgg.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(DigiEgg.class.getSimpleName() + ".png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_DAMAGE = 1;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    private static int countMonster = 0;

    public DigiEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;

        this.tags.add(CardTags.HEALING);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (Agumon.EVOLUTION_STAGE >= 2)
            return super.canUse(p, m);

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.maxHealth += countMonster * magicNumber;

        AboutDigivolution.Devolution(false);
        AboutDigivolution.DevolutionCards(false);

        Agumon.EVOLUTION_STAGE = 0;

        addToBot((new HealAction(p, p, (int)Math.round(p.maxHealth * 0.3))));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        countMonster = 0;

        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();   // monster single check
        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            countMonster++;
        }

        rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + countMonster;
        this.initializeDescription();
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
