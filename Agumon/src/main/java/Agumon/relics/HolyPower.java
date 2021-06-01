package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.cards.attack.WarOfPast;
import Agumon.cards.power.Digivolution;
import Agumon.cards.power.SuperDigivolution;
import Agumon.cards.power.UltimateDigivolution;
import Agumon.characters.Agumon;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Agumon.AgumonMod.makeRelicOutlinePath;
import static Agumon.AgumonMod.makeRelicPath;

public class HolyPower extends CustomRelic {

    public static final String ID = AgumonMod.makeID(HolyPower.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(HolyPower.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(HolyPower.class.getSimpleName() + ".png"));

    public HolyPower() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
//        Agumon.AMOUNT_EVOLUTION = this.counter;
    }


//    @Override
//    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
//        if (targetCard.cardID == Digivolution.ID ||
//            targetCard.cardID == SuperDigivolution.ID ||
//            targetCard.cardID == UltimateDigivolution.ID){
////            counter++;
////            Agumon.AMOUNT_EVOLUTION = this.counter;
//            this.flash();
//        }else if(targetCard.cardID == WarOfPast.ID){    // Flash
//            this.flash();
//        }
//    }

    @Override
    public void onEquip() {
//        this.counter = 0;

        AbstractCard newCard = new WarOfPast();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(newCard, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
