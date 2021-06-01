package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeSkillCardPath;

public class Crisis extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Crisis.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(Crisis.class.getSimpleName() + ".png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 0;

    private static int amountDifferent = 0;

    public Crisis() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        amountDifferent = 0;    // Clear

        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();
        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                if ((monster.currentHealth - p.currentHealth) > amountDifferent) {
                    amountDifferent = (int) ((monster.currentHealth - p.currentHealth) * 0.1);

                    if (amountDifferent == 0)   // amountDifferent MIN -> 1
                        amountDifferent = 1;
                }

            }
        }

        if (amountDifferent > 0) {
            return super.canUse(p, m);
        }

        this.cantUseMessage = CardCrawlGame.languagePack.getCharacterString(Agumon.ID).TEXT[3];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((new HealAction(p, p, amountDifferent)));
//        aboutCourage.gainCourage(amountDifferent / 2);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!upgraded) {
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + amountDifferent;
        }
        else {
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION + amountDifferent;
        }


        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
