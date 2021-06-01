package Agumon.util;

import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;

public class CardFontSize {
    public static float getProperSizeForLanguage(ArrayList<Settings.GameLanguage> lanList){
        if(lanList.contains(Settings.language))
            return 19.0F;
        else
            return 22.0F;
    }
}
