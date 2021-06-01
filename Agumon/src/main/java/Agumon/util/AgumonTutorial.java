package Agumon.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

import java.util.ArrayList;

import static Agumon.AgumonMod.*;
import static Agumon.util.TutorialSkipButton.*;

public class AgumonTutorial extends FtueTip {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Agumon");
    public static final String[] TXT = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;

    private float imageXPosition;

    private float scrollCount = 0.0F;

    private int PAGE = 1;
    private final int PAGE_SIZE = 9;
    private boolean nextPage = false;
    private boolean previousPage = false;

    // Image
    public static String pathPerLan = getLanImagePath();
    private static ArrayList<Texture> IMAGE = new ArrayList<>();

    // Page Text
    private static ArrayList<String[]> PAGE_TXT = new ArrayList();

    // Label
    private static final String TUTORIAL_LABEL = LABEL[0];
    private static final String PROCEEDBUTTON_LABEL = LABEL[1];
    private static final String CANCELBUTTON_LABEL = LABEL[2];
    private static final String SKIPBUTTON_LABEL = LABEL[3];

    // My Skip Buttton
    public TutorialSkipButton skipButton = new TutorialSkipButton();

    public AgumonTutorial(){
        logger.info("AgumonTutorial | Constructor");

        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();

        this.imageXPosition = 0.0F;

        AbstractDungeon.overlayMenu.proceedButton.show();
        AbstractDungeon.overlayMenu.proceedButton.setLabel(PROCEEDBUTTON_LABEL);
        AbstractDungeon.overlayMenu.cancelButton.show(CANCELBUTTON_LABEL);
        skipButton.show();
        skipButton.updateText(SKIPBUTTON_LABEL);
    }

    @Override
    public void update() {
        if (AbstractDungeon.overlayMenu.cancelButton.hb.hovered && InputHelper.justClickedLeft || CInputActionSet.cancel.isJustPressed()){
//            logger.info("튜토리얼 | 이전 누름 !! 페이지 : " + (this.PAGE - 1));
            if (this.previousPage && this.PAGE > 1)  // if Double Click
                this.PAGE--;

            if (this.PAGE > 1) {
                this.previousPage = true;
                this.scrollCount = 0.3F;
            }
            CardCrawlGame.sound.play("UI_HOVER");
        }

        skipButton.update();
        if (InputHelper.justClickedLeft && skipButton.hb.hovered) {     // Skip Button
//            logger.info("스킵 버튼 위에 있어 !");
            CardCrawlGame.sound.play("UI_HOVER");

            logger.info("AgumonTutorial | Tutorial Finish by skip");
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.overlayMenu.hideBlackScreen();
            AbstractDungeon.effectList.clear();
        }

        if (AbstractDungeon.overlayMenu.proceedButton.isHovered && InputHelper.justClickedLeft || CInputActionSet.proceed.isJustPressed()) {
//            logger.info("튜토리얼 | 다음 누름 !! 페이지 : " + (this.PAGE + 1));
            if (this.nextPage && this.PAGE <= this.PAGE_SIZE)      // if Double Click
                this.PAGE++;

            this.nextPage = true;
            this.scrollCount = 0.3F;
            CardCrawlGame.sound.play("UI_HOVER");
        }

        if (this.PAGE > this.PAGE_SIZE) {
            logger.info("AgumonTutorial | Tutorial Finish");
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.overlayMenu.hideBlackScreen();
            AbstractDungeon.effectList.clear();
        }

        if (this.scrollCount > 0.0F){
            this.scrollCount -= 0.02F;
        }
        else{
            this.scrollCount = 0.0F;
        }

        if (this.nextPage)
            this.imageXPosition = Interpolation.fade.apply(-(float)(Settings.WIDTH), 0, scrollCount / 0.3F);
        if (this.previousPage) {
            this.imageXPosition = Interpolation.fade.apply((float)(Settings.WIDTH), 0, scrollCount / 0.3F);
        }

        if (this.imageXPosition <= -(Settings.WIDTH - 10) || this.imageXPosition >= (Settings.WIDTH - 10)){
            if (this.nextPage)
                this.PAGE++;
            if (this.previousPage)
                this.PAGE--;
            this.imageXPosition = 0;
            this.nextPage = false;
            this.previousPage = false;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.PAGE < 1)      // Just for java.lang.IndexOutOfBoundsException
            this.PAGE = 1;

        if (this.PAGE <= this.PAGE_SIZE) {
            FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, TUTORIAL_LABEL, Settings.WIDTH / 2, Settings.HEIGHT - 100 * Settings.scale);  // Mod Tutiral
            FontHelper.renderFontCenteredWidth(sb, FontHelper.tipBodyFont,  + this.PAGE + " Page / " + this.PAGE_SIZE + " Pages",                // Page
                    (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 500.0F * Settings.scale, Settings.CREAM_COLOR);
            AbstractDungeon.overlayMenu.cancelButton.render(sb);
            AbstractDungeon.overlayMenu.proceedButton.render(sb);
            skipButton.render(sb);

            sb.setColor(Color.WHITE);
            sb.draw(this.IMAGE.get(this.PAGE - 1), this.imageXPosition, (float)Settings.HEIGHT / 2.0F - 300 * Settings.scale, 1200 * Settings.scale, 700 * Settings.scale);
            for (int count = 0, yPosition = 250; count < PAGE_TXT.get(this.PAGE - 1).length; count++, yPosition = yPosition - 50){
                FontHelper.renderFont(sb, FontHelper.buttonLabelFont, PAGE_TXT.get(this.PAGE - 1)[count], this.imageXPosition + (float)Settings.WIDTH / 2 + 270 * Settings.scale, (float)Settings.HEIGHT / 2 + yPosition * Settings.scale, Color.WHITE);
            }
        }
    }

    public static String getLanImagePath(){
        String pathByLanguage;
        switch(Settings.language){  // get path by language
            case KOR:
                pathByLanguage = "kor/";
                break;
            case JPN:
                pathByLanguage = "jpn/";
                break;
            case ZHS:
                pathByLanguage = "zhs/";
                break;
            default:
                pathByLanguage = "eng/";
        }
        return pathByLanguage;
    }

    static{
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "01.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "02.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "03.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "03-2.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "05.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "06.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "07.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "08.png")));
        IMAGE.add(ImageMaster.loadImage(makeTutorialPath( pathPerLan + "09.png")));

        PAGE_TXT.add(new String[]{TXT[0], TXT[1], TXT[2]});
        PAGE_TXT.add(new String[]{TXT[3], TXT[4], TXT[5], TXT[6], TXT[7]});
        PAGE_TXT.add(new String[]{TXT[8], TXT[9], TXT[10]});
        PAGE_TXT.add(new String[]{TXT[11], TXT[12], TXT[13]});
        PAGE_TXT.add(new String[]{TXT[14], TXT[15], TXT[16], TXT[17], TXT[18], TXT[19]});
        PAGE_TXT.add(new String[]{TXT[20], TXT[21], TXT[22]});
        PAGE_TXT.add(new String[]{TXT[23], TXT[24], TXT[25]});
        PAGE_TXT.add(new String[]{TXT[26], TXT[27], TXT[28], TXT[29], TXT[30]});
        PAGE_TXT.add(new String[]{TXT[31], TXT[32], TXT[33], TXT[34], TXT[35], TXT[36], TXT[37], TXT[38]});
        PAGE_TXT.add(new String[]{""}); // Nothing, Just for java.lang.IndexOutOfBoundsException
    }
}
