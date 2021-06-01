package Agumon.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class PostureCorrectionAction extends AbstractGameAction {
    public int amount;

    public PostureCorrectionAction(AbstractPlayer p, int amount) {
        this.amount = amount;
        this.target = p;
    }

    public void update() {
        this.isDone = true;

        Iterator cardListPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();

        while(cardListPlayedThisTurn.hasNext()) {
            AbstractCard c = (AbstractCard) cardListPlayedThisTurn.next();
            if (c.type == AbstractCard.CardType.ATTACK) {
                addToBot(new DrawCardAction(target, amount));


                break;
            }
        }
    }
}