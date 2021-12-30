package Agumon.cards.power;

import Agumon.AgumonMod;
import Agumon.cards.AbstractDynamicCard;
import Agumon.cards.AboutCourage;
import Agumon.cards.AboutDigivolution;
import Agumon.characters.Agumon;
import Agumon.util.CardFontSize;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.Iterator;

import static Agumon.AgumonMod.makePowerCardPath;

public class SuperDigivolution extends AbstractDynamicCard {

    public static final String ID = AgumonMod.makeID(SuperDigivolution.class.getSimpleName());
    public static final String IMG = makePowerCardPath(SuperDigivolution.class.getSimpleName() + ".png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Agumon.Enums.COLOR_GRAY;

    private static final int MAGIC = 1;
    private static final int COST = 1;
    private static final int COST_COURAGE = 10;
    private static final int UPGRADE_COST = 0;

    public SuperDigivolution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        this.selfRetain = true;
    }

    @Override
    public float getTitleFontSize() {
        ArrayList<Settings.GameLanguage> lanList = new ArrayList<>();
        lanList.add(Settings.GameLanguage.ENG);

        return CardFontSize.getProperSizeForLanguage(lanList);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            if (AboutCourage.HasEnoughCourage(COST_COURAGE)) {
                return super.canUse(p, m);
            }
            else{
                this.cantUseMessage = AboutCourage.NotEnoughCourage();
            }
        }
        else{
            this.cantUseMessage = CardCrawlGame.languagePack.getCharacterString(Agumon.ID).TEXT[5];
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Agumon.EVOLUTION_STAGE = 2;
        Agumon.AMOUNT_EVOLUTION++;
        AboutCourage.ReduceCourage(COST_COURAGE);

        AboutDigivolution.Digivolution(true);
        AboutDigivolution.EvolutionCards(true);

        // Give WeakPower to all Enemy.
        Iterator monsterList = AbstractDungeon.getMonsters().monsters.iterator();

        while(monsterList.hasNext()) {
            AbstractMonster monster = (AbstractMonster)monsterList.next();
            if (!monster.isDead && !monster.isDying) {
                addToBot(new ApplyPowerAction(monster, monster, new WeakPower(monster, 2, false), 2));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}