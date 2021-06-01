package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Agumon.AgumonMod.makeRelicOutlinePath;
import static Agumon.AgumonMod.makeRelicPath;

public class HolyRing extends CustomRelic {

    public static final String ID = AgumonMod.makeID(HolyRing.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(HolyRing.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(HolyRing.class.getSimpleName() + ".png"));

    public HolyRing() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        ++this.counter;
        if (this.counter % 4 == 0) {
            this.counter = 0;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1));
        }

    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
