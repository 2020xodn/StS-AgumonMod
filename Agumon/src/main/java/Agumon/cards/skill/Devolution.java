package Agumon.cards.skill;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutDigivolution;
import Agumon.characters.Agumon;
import Agumon.powers.couragePower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeSkillCardPath;

public class Devolution extends AbstractDynamicCard {
    public static final String ID = AgumonMod.makeID(Devolution.class.getSimpleName());
    public static final String IMG = makeSkillCardPath(Devolution.class.getSimpleName() + ".png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 1;

    public Devolution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
        this.selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (Agumon.EVOLUTION_STAGE >= 1)
            return super.canUse(p, m);

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RemoveSpecificPowerAction(p, p, couragePower.POWER_ID));


        AboutDigivolution.Devolution(true);
        AboutDigivolution.DevolutionCards(true);


        Agumon.EVOLUTION_STAGE--;
        addToBot((new HealAction(p, p, reduceHP())));
        Agumon.logger.info("Devolution Reduce Heal : " + reduceHP());

        if (!upgraded) {// HP Healing
            addToBot(new HealAction(p, p, (int) Math.round((p.maxHealth + reduceHP()) * 0.2)));
            Agumon.logger.info("Devolution Heal : " + (int) Math.round((p.maxHealth + reduceHP()) * 0.2));
        }
        else {
            addToBot(new HealAction(p, p, (int) Math.round((p.maxHealth + reduceHP()) * 0.4)));
            Agumon.logger.info("Devolution Heal : " + (int) Math.round((p.maxHealth + reduceHP()) * 0.4));
        }

    }
    
    public void applyPowers() {
        super.applyPowers();

        AbstractPlayer p = AbstractDungeon.player;
        if (!upgraded)
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION + (int)Math.round(p.maxHealth * 0.2);
        else
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION + (int)Math.round(p.maxHealth * 0.4);

        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public int reduceHP(){
        int reduceHPAmount;
        switch(Agumon.EVOLUTION_STAGE){
            case 2:
                reduceHPAmount = Agumon.ADDHP_WARGREYMON;
                break;

            case 1:
                reduceHPAmount = Agumon.ADDHP_METALGREYMON;
                break;

            case 0:
                reduceHPAmount = Agumon.ADDHP_GREYMON;
                break;

            default:
                reduceHPAmount = 0;
        }

        return reduceHPAmount;
    }
}
