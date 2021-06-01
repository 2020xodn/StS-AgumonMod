package Agumon.util;

import Agumon.cards.attack.*;
import Agumon.cards.power.Digivolution;
import Agumon.cards.skill.Agumon_Defend;
import Agumon.cards.skill.Greymon_Defend;
import Agumon.cards.skill.Metalgreymon_Defend;
import Agumon.cards.skill.Wargreymon_Defend;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

import static Agumon.characters.Agumon.logger;

public class UI_CardsPreview {
    private static final UIStrings uiStringsLevel = CardCrawlGame.languagePack.getUIString("EvolutionCardsPreview");
    private static final String []TEXT = uiStringsLevel.TEXT;
    private static ArrayList<AbstractCard> BlankCardsList = new ArrayList();
    private static ArrayList<AbstractCard> ClawCardsList = new ArrayList();
    private static ArrayList<AbstractCard> DefendCardsList = new ArrayList();
    private static ArrayList<AbstractCard> FuryClawCardsList = new ArrayList();
    private static ArrayList<AbstractCard> FlameCardsList = new ArrayList();
    private static ArrayList<AbstractCard> BurstCardsList = new ArrayList();

    public static void MyRenderCardsPreviewInSingleView(SpriteBatch sb, String techTreeName,
                                                        boolean isUpgraded, int nowEvolutionStage){

        ArrayList<Integer> int_renderList = new ArrayList();
        int_renderList.add(0);
        int_renderList.add(1);
        int_renderList.add(2);
        int_renderList.add(3);

        // Main String
        FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, TEXT[int_renderList.get(nowEvolutionStage)],
                960 * Settings.xScale, 1030 * Settings.scale,
                Settings.CREAM_COLOR, 2.0F);

        // ArrayList Copy
        ArrayList <AbstractCard> cardsToRenderList = new ArrayList<>();
        switch(techTreeName){
            case "Claw":
                cardsToRenderList.addAll(ClawCardsList);
                break;

            case "Defend":
                cardsToRenderList.addAll(DefendCardsList);
                break;

            case "FuryClaw":
                cardsToRenderList.addAll(FuryClawCardsList);
                break;

            case "Flame":
                cardsToRenderList.addAll(FlameCardsList);
                break;

            case "Burst":
                cardsToRenderList.addAll(BurstCardsList);
                break;

            default:
                logger.info("UI_CardPreview | switch Error : " + techTreeName);

        }

        // Remove Main Card from ArrayList
        AbstractCard mainCard = cardsToRenderList.get(int_renderList.get(nowEvolutionStage)).makeCopy();
        cardsToRenderList.remove(nowEvolutionStage);
        int_renderList.remove(nowEvolutionStage);

        float[][] XY_Position = new float[][]{
                {200.0F, 1030.0F, 200.0F, 830.0F}, // Left Up
                {480.0F, 1030.0F, 480.0F, 830.0F},  // Right up
                {480.0F, 470.0F, 480.0F, 270.0F}   // Right Down
        };

        for (int count = 0; count < 3; count++){    // Left Up, Left Down, Right Up
            FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, TEXT[int_renderList.get(count)],
                    XY_Position[count][0] * Settings.xScale, XY_Position[count][1] * Settings.scale,
                    Settings.CREAM_COLOR, 1.2F);
            mainCard.cardsToPreview = cardsToRenderList.get(count).makeCopy();
            if (isUpgraded)
                mainCard.cardsToPreview.upgrade();
            mainCard.cardsToPreview.current_x = XY_Position[count][2] * Settings.scale;
            mainCard.cardsToPreview.current_y = XY_Position[count][3] * Settings.scale;
            mainCard.cardsToPreview.drawScale = 0.8F;
            mainCard.cardsToPreview.render(sb);
        }
    }

//    void test(SpriteBatch sb, boolean isUpgraded){
//        ArrayList <AbstractCard> cardsToRenderList = new ArrayList<>();
//        cardsToRenderList.add(new Agumon_Claw());
//        cardsToRenderList.add(new Greymon_Claw());
//        cardsToRenderList.add(new Metalgreymon_Claw());
//        cardsToRenderList.add(new Wargreymon_Claw());
//
//        float[][] XY_Position = new float[][]{
//                {200.0F, 1030.0F, 200.0F, 830.0F}, // Left Up
//                {480.0F, 1030.0F, 480.0F, 830.0F},  // Right up
//                {480.0F, 470.0F, 480.0F, 270.0F}   // Right Down
//        };
//
//        AbstractCard mainCard = cardsToRenderList.get(0).makeCopy();
//        cardsToRenderList.remove(0);                                // 메인으로 쓸 카드는 리스트에서 제거
//        FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, "띄울 텍스트",
//                XY_Position[0][0] * Settings.xScale, XY_Position[0][1] * Settings.scale,    // XY 좌표
//                Settings.CREAM_COLOR, 1.2F);                                                // 폰트랑 크기
//        mainCard.cardsToPreview = cardsToRenderList.get(0).makeCopy();
//
//        if (isUpgraded)
//            mainCard.cardsToPreview.upgrade();
//        mainCard.cardsToPreview.current_x = XY_Position[0][2] * Settings.scale;
//        mainCard.cardsToPreview.current_y = XY_Position[0][3] * Settings.scale;
//        mainCard.cardsToPreview.drawScale = 0.8F;
//        mainCard.cardsToPreview.render(sb);
//    }

    static{
        BlankCardsList.add(new Digivolution());
        BlankCardsList.add(new Digivolution());
        BlankCardsList.add(new Digivolution());
        BlankCardsList.add(new Digivolution());

        ClawCardsList.add(new Agumon_Claw());
        ClawCardsList.add(new Greymon_Claw());
        ClawCardsList.add(new Metalgreymon_Claw());
        ClawCardsList.add(new Wargreymon_Claw());

        DefendCardsList.add(new Agumon_Defend());
        DefendCardsList.add(new Greymon_Defend());
        DefendCardsList.add(new Metalgreymon_Defend());
        DefendCardsList.add(new Wargreymon_Defend());

        FuryClawCardsList.add(new Agumon_FuryClaw());
        FuryClawCardsList.add(new Greymon_TailWhip());
        FuryClawCardsList.add(new Metalgreymon_TailWhip());
        FuryClawCardsList.add(new Wargreymon_BraveTornado());

        FlameCardsList.add(new Agumon_BabyFlame());
        FlameCardsList.add(new Greymon_MegaFlame());
        FlameCardsList.add(new Metalgreymon_OverFlame());
        FlameCardsList.add(new Wargreymon_Gaia1());

        BurstCardsList.add(new Agumon_BabyBurner());
        BurstCardsList.add(new Greymon_MegaBurst());
        BurstCardsList.add(new Metalgreymon_GigaStorm());
        BurstCardsList.add(new Wargreymon_Gaia2());
    }
}
