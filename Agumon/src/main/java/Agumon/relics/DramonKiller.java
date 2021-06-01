package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import static Agumon.AgumonMod.makeRelicOutlinePath;
import static Agumon.AgumonMod.makeRelicPath;

public class DramonKiller extends CustomRelic {

    public static final String ID = AgumonMod.makeID(DramonKiller.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(DramonKiller.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(DramonKiller.class.getSimpleName() + ".png"));

    public DramonKiller() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot((new ApplyPowerAction(p, p, new StrengthPower(p, 3), 3)));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
