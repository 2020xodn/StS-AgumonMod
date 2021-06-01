package Agumon.actions;

import Agumon.cards.skill.Telecommunication;
import Agumon.characters.Agumon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class TelecommunicationAction extends AbstractGameAction {
    public int amount;
    private AbstractPlayer p;
    private final String tipMsg = CardCrawlGame.languagePack.getCardStrings(Telecommunication.ID).EXTENDED_DESCRIPTION[0];

    public TelecommunicationAction(int amount) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.amount > p.drawPile.size()){
                this.amount = p.drawPile.size();
            }
//            Agumon.logger.info("TeleAction | this.amount = " + this.amount);
//            Agumon.logger.info("TeleAction | p.drawpile.size = " + p.drawPile.size());
            AbstractDungeon.gridSelectScreen.open(this.p.drawPile, this.amount, this.tipMsg, false);
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    this.p.hand.addToHand(c);
                    this.p.drawPile.removeCard(c);
                    c.setCostForTurn(c.cost - 1);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

        }
        this.tickDuration();
    }

}