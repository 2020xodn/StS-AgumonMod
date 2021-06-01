package Agumon.cards.attack;

import Agumon.AgumonMod;
import Agumon.cards.power.Digivolution;
import Agumon.characters.Agumon;
import Agumon.powers.burnPower;
import Agumon.util.UI_CardsPreview;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static Agumon.AgumonMod.makeAttackCardPath;
import static Agumon.cards.AboutCourage.*;

public class Greymon_MegaBurst extends CustomCard {
    public static final String ID = AgumonMod.makeID(Greymon_MegaBurst.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeAttackCardPath(Greymon_MegaBurst.class.getSimpleName() + ".png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int COST_COURAGE = 3;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int MAGIC_DAMAGE = 1;
    private static final int UPGRADE_PLUS_M_DMG = 1;

    public int tmpCost = COST;
    public boolean isTmpCostStored = false;

    public Greymon_MegaBurst() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC_DAMAGE;
        this.cardsToPreview = new Digivolution();
    }

    @Override
    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        UI_CardsPreview.MyRenderCardsPreviewInSingleView(sb, "Burst", this.upgraded, 1);
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {     // Not Using
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (!HasEnoughCourage(COST_COURAGE)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE) && !isTmpCostStored){
            setCostForTurn(cost);
        }
        else if(HasEnoughCourage(COST_COURAGE)){
            setCostForTurn(tmpCost);
        }
        else{
            if (!isTmpCostStored && !isCostModifiedForTurn){
                tmpCost = this.costForTurn;
                isTmpCostStored = true;
            }
            setCostForTurn(0);
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (HasEnoughCourage(COST_COURAGE)) {
            ReduceCourage(COST_COURAGE);

            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));

            Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();

            while (monsterList.hasNext()) {
                AbstractMonster monster = (AbstractMonster) monsterList.next();
                if (!monster.isDead && !monster.isDying) {
                    addToBot(new ApplyPowerAction(monster, p, new burnPower(monster, p, this.magicNumber, monster.type), this.magicNumber));
                }
            }
        }
        else{
            gainCourageNotEnough(upgraded);
        }
        isTmpCostStored = false;
    }

    @Override
    public void atTurnStart() {
        isTmpCostStored = false;
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

    public AbstractCard makeCopy() {
        return new Greymon_MegaBurst();
    }
}
