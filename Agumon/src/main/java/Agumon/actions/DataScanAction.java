package Agumon.actions;

import Agumon.cards.AboutCourage;
import Agumon.cards.skill.DataScan;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

import static Agumon.characters.Agumon.logger;

public class DataScanAction extends AbstractGameAction {
    private AbstractPlayer p;
    public static int numExhausted;
    public static int upgradedAmount;

    public DataScanAction(int amount, int upgraded) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        upgradedAmount = upgraded;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }


    public void update() {
        String tipMsg = CardCrawlGame.languagePack.getCardStrings(DataScan.ID).EXTENDED_DESCRIPTION[0];
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            numExhausted = this.amount;
            AbstractDungeon.handCardSelectScreen.open(tipMsg, this.amount, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();


            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                AboutCourage.gainCourage((c.cost > 0)? c.cost * upgradedAmount: 0);
                p.hand.moveToExhaustPile(c);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }
}