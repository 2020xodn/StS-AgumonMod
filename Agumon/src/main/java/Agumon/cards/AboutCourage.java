package Agumon.cards;

import Agumon.AgumonMod;
import Agumon.powers.couragePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Agumon.characters.Agumon.logger;

public class AboutCourage {
    public static void gainCourage(int amount){
        logger.info("aboutCourage | gainCourage " + amount);
        int amountCourage = 0;
        if (AbstractDungeon.player.hasPower(couragePower.POWER_ID)){
            amountCourage = AbstractDungeon.player.getPower(couragePower.POWER_ID).amount;
        }
        if (amountCourage + amount > 20){
            amount = 20 - amountCourage;
        }

        AbstractDungeon.player.addPower(new couragePower(AbstractDungeon.player, AbstractDungeon.player, amount));
    }

    public static void gainCourageNotEnough(boolean upgraded){
        if (!upgraded){
            gainCourage(1);
        }
        else{
            gainCourage(2);
        }
    }

    public static boolean HasEnoughCourage(int amount){
        if (AbstractDungeon.player.hasPower(couragePower.POWER_ID)) {
            return AbstractDungeon.player.getPower(couragePower.POWER_ID).amount >= amount;
        }

        return false;
    }

    public static void ReduceCourage(int amount){
        AbstractPlayer p = AbstractDungeon.player;

        AbstractDungeon.player.getPower(couragePower.POWER_ID).amount -= amount;
        if (AbstractDungeon.player.getPower(couragePower.POWER_ID).amount == 0)
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, couragePower.POWER_ID));
    }

    public static String NotEnoughCourage(){
        return CardCrawlGame.languagePack.getCardStrings(AgumonMod.makeID(AboutCourage.class.getSimpleName())).DESCRIPTION;
    }


}
