package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static Agumon.AgumonMod.makeRelicOutlinePath;
import static Agumon.AgumonMod.makeRelicPath;

public class CrestOfCourage extends CustomRelic {

    public static final String ID = AgumonMod.makeID(CrestOfCourage.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(CrestOfCourage.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(CrestOfCourage.class.getSimpleName() + ".png"));

    public CrestOfCourage() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
