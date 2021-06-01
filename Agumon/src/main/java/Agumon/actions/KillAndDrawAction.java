package Agumon.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class KillAndDrawAction extends AbstractGameAction {
    private DamageInfo info;
    private int amountDraw;
    private AbstractPlayer p;

    public KillAndDrawAction(AbstractPlayer p, AbstractCreature target, DamageInfo info, int amountDraw) {
        this.p = p;
        this.info = info;
        this.setValues(target, info);
        this.amountDraw = amountDraw;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }
    
    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
                addToBot(new DrawCardAction(p, amountDraw));
                addToBot(new GainEnergyAction(1));
            }
        }
        this.tickDuration();
    }
}
