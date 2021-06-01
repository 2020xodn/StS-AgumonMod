package Agumon.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class AddHPAction extends AbstractGameAction {
    public int amount;

    public AddHPAction(AbstractPlayer p, int amount) {
        this.amount = amount;
        this.target = p;
    }

    public void update() {
        this.isDone = true;
        target.maxHealth += amount;
        target.currentHealth += amount;
    }
}