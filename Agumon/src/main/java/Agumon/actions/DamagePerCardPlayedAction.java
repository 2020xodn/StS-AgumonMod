package Agumon.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class DamagePerCardPlayedAction extends AbstractGameAction {
    private DamageInfo info;

    public DamagePerCardPlayedAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
    }

    public DamagePerCardPlayedAction(AbstractCreature target, DamageInfo info) {
        this(target, info, AttackEffect.NONE);
    }

    public void update() {
        this.isDone = true;
        if (this.target != null && this.target.currentHealth > 0) {
            int count = 0;
            Iterator var2 = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();
            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                ++count;
            }
            --count;    // Minus for FinisherBlow !

            for(int i = 0; i < count; ++i) {
                this.addToTop(new DamageAction(this.target, this.info, this.attackEffect));
            }
        }
    }
}