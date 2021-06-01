package Agumon.characters;

import Agumon.AgumonMod;
import Agumon.cards.attack.Agumon_BabyFlame;
import Agumon.cards.attack.Agumon_Claw;
import Agumon.cards.AboutDigivolution;
import Agumon.cards.power.Digivolution;
import Agumon.cards.skill.Agumon_Defend;
import Agumon.relics.defaultDigivice;
import Agumon.util.AgumonTutorial;
import basemod.abstracts.CustomPlayer;
import basemod.abstracts.CustomSavable;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static Agumon.AgumonMod.*;
import static Agumon.characters.Agumon.Enums.COLOR_GRAY;

public class Agumon extends CustomPlayer implements CustomSavable <Integer> {
    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        sr.setPremultipliedAlpha(false);
        super.renderPlayerImage(sb);
        sr.setPremultipliedAlpha(true);
    }

    public static final Logger logger = LogManager.getLogger(AgumonMod.class.getName());

    @Override
    public Integer onSave() {
        return AMOUNT_EVOLUTION;
    }

    @Override
    public void onLoad(Integer integer) {
        AMOUNT_EVOLUTION = integer;
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass AGUMON_PLAYERCLASS;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_GRAY;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 65;
    public static final int MAX_HP = 65;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================

    public static int EVOLUTION_STAGE = 0;
    public static int AMOUNT_EVOLUTION = 0;
    public static int ADDHP_GREYMON = 15;
    public static int ADDHP_METALGREYMON = 5;
    public static int ADDHP_WARGREYMON = 5;

    public static boolean isMetalgreymon = false;
    public static boolean isWargreymon = false;

    public static boolean didTutorial = false;

    public static ArrayList<Boolean> hasBraveShield = new ArrayList<>();    // {true, false} -> nonUpgraded , {true, true} -> upgraded
    public static ArrayList<Boolean> hasGigaDestroyer = new ArrayList<>();    // {true, false} -> nonUpgraded , {true, true} -> upgraded
    public static ArrayList<Boolean> removedDigivolution = new ArrayList<>();


    // =============== /MY STATS/ =================


    // =============== STRINGS =================

    public static final String ID = makeID("Agumon");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "AgumonResources/images/char/Agumon/orb/layer1.png",
            "AgumonResources/images/char/Agumon/orb/layer2.png",
            "AgumonResources/images/char/Agumon/orb/layer3.png",
            "AgumonResources/images/char/Agumon/orb/layer4.png",
            "AgumonResources/images/char/Agumon/orb/layer5.png",
            "AgumonResources/images/char/Agumon/orb/layer6.png",
            "AgumonResources/images/char/Agumon/orb/layer1d.png",
            "AgumonResources/images/char/Agumon/orb/layer2d.png",
            "AgumonResources/images/char/Agumon/orb/layer3d.png",
            "AgumonResources/images/char/Agumon/orb/layer4d.png",
            "AgumonResources/images/char/Agumon/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public Agumon(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "AgumonResources/images/char/Agumon/orb/vfx.png", null,
                new SpineAnimation("AgumonResources/images/char/Agumon/AgumonAnimation.atlas",
                        "AgumonResources/images/char/Agumon/AgumonAnimation.json", 1f));

        this.initializeClass(null,
                AGUMON_SHOULDER_2, // campfire pose
                AGUMON_SHOULDER_1, // another campfire pose
                AGUMON_CORPSE, // dead corpse
                this.getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== ANIMATIONS =================

        logger.info("Before loading animation");
        animationAgumon();

        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== variable Clear =================

        EVOLUTION_STAGE = 0;
        hasBraveShield.clear();
        AboutDigivolution.ClearStage();
        AboutDigivolution.ClearHigherCards();
        didTutorial = false;
    }
    // =================== Animation ===========================
    public void animationAgumon(){
        loadAnimation(
                AGUMON_ATLAS,
                AGUMON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "baseAnimation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void animationGreymonBase(){
        loadAnimation(
                GREYMON_ATLAS,
                GREYMON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "baseGreymonAnimation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void animationMetalgreymonBase(){
        loadAnimation(
                METALGREYMON_ATLAS,
                METALGREYMON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "baseMetalGreymonAnimation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void animationWargreymonBase(){
        loadAnimation(
                WARGREYMON_ATLAS,
                WARGREYMON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "baseWargreymonAnimation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void animationEvolveAgumonToGreymon(){
        loadAnimation(
                GREYMON_ATLAS,
                GREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "evolveAgumonAnimation", false);
        this.state.addAnimation(0, "baseGreymonAnimation", true, 2.0F);
    }

    public void animationEvolveGreymonToMetalgreymon(){
        loadAnimation(
                METALGREYMON_ATLAS,
                METALGREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "evolve_GreymonToMetalgreymon1", false);
        this.state.addAnimation(0, "baseMetalGreymonAnimation", true, 2.0F);
    }

    public void animationEvolveAgumonToMetalgreymon(){
        loadAnimation(
                METALGREYMON_ATLAS,
                METALGREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "evolve_AgumonToMetalgreymon", false);
        this.state.addAnimation(0, "baseMetalGreymonAnimation", true, 2.0F);
    }

    public void animationEvolveMetalgreymonToWargreymon(){
        loadAnimation(
                WARGREYMON_ATLAS,
                WARGREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "evolve_MetalgreymonToWargreymon", false);
        this.state.addAnimation(0, "baseWargreymonAnimation", true, 2.0F);
    }

    public void animationEvolveAgumonToWargreymon(){
        loadAnimation(
                WARGREYMON_ATLAS,
                WARGREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "evolve_AgumonToWargreymon", false);
        this.state.addAnimation(0, "baseWargreymonAnimation", true, 2.0F);
    }

    public void animationDevolveGreymonToAgumon(){
        loadAnimation(
                AGUMON_ATLAS,
                AGUMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "devolveGreymonToAgumon", false);
        this.state.addAnimation(0, "baseAnimation", true, 2.0F);
    }

    public void animationDevolveMetalgreymonToGreymon(){
        loadAnimation(
                GREYMON_ATLAS,
                GREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "devolveMetalgreymonToGreymon", false);
        this.state.addAnimation(0, "baseGreymonAnimation", true, 2.0F);
    }

    public void animationDevolveMetalgreymonToAgumon(){
        loadAnimation(
                AGUMON_ATLAS,
                AGUMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "devolveMetalgreymonToAgumon", false);
        this.state.addAnimation(0, "baseAnimation", true, 2.0F);
    }

    public void animationDevolveWargreymonToMetalgreymon(){
        loadAnimation(
                METALGREYMON_ATLAS,
                METALGREYMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "devolve_WargreymonToMetalgreymon", false);
        this.state.addAnimation(0, "baseMetalGreymonAnimation", true, 2.0F);
    }

    public void animationDevolveWargreymonToAgumon(){
        loadAnimation(
                AGUMON_ATLAS,
                AGUMON_JSON,
                1.0f);

        this.state.clearTrack(0);
        this.state.setAnimation(0, "devolve_WargreymonToAgumon", false);
        this.state.addAnimation(0, "baseAnimation", true, 2.0F);
    }




    // =================== Animation END ===========================

    // =============== /CHARACTER CLASS END/ =================

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0], STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }


    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        logger.info("Begin loading starter Deck Strings");
        // BASIC
        retVal.add(Agumon_Claw.ID);
        retVal.add(Agumon_Claw.ID);
        retVal.add(Agumon_Claw.ID);
        retVal.add(Agumon_Claw.ID);
        retVal.add(Agumon_BabyFlame.ID);

        retVal.add(Agumon_Defend.ID);
        retVal.add(Agumon_Defend.ID);
        retVal.add(Agumon_Defend.ID);
        retVal.add(Agumon_Defend.ID);
//        retVal.add(Digivolution.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(defaultDigivice.ID);
        UnlockTracker.markRelicAsSeen(defaultDigivice.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_GRAY;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return AgumonMod.AGUMON_COLOR;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Agumon_Claw();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Agumon(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return AgumonMod.AGUMON_COLOR;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return AgumonMod.AGUMON_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public void preBattlePrep() {   // Tutorial Start
        logger.info("Agumon | preBattlePreq Tutorial Start !");
        if (!didTutorial){
            didTutorial = true;
            AbstractDungeon.ftue = new AgumonTutorial();
        }

        super.preBattlePrep();

    }


    @Override
    public void onVictory() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
            switch(EVOLUTION_STAGE){
                case 3:
                    ((Agumon) AbstractDungeon.player).animationDevolveWargreymonToAgumon();
                    AbstractDungeon.player.maxHealth -= (ADDHP_WARGREYMON + ADDHP_METALGREYMON + ADDHP_GREYMON);
                    AbstractDungeon.player.currentHealth -= (ADDHP_WARGREYMON + ADDHP_METALGREYMON + ADDHP_GREYMON);
                    break;

                case 2:
                    ((Agumon) AbstractDungeon.player).animationDevolveMetalgreymonToAgumon();
                    AbstractDungeon.player.maxHealth -= (ADDHP_METALGREYMON + ADDHP_GREYMON);
                    AbstractDungeon.player.currentHealth -= (ADDHP_METALGREYMON + ADDHP_GREYMON);
                    break;

                case 1:
                    ((Agumon) AbstractDungeon.player).animationDevolveGreymonToAgumon();
                    AbstractDungeon.player.maxHealth -= (ADDHP_GREYMON);
                    AbstractDungeon.player.currentHealth -= (ADDHP_GREYMON);
                    break;

                default:
                    break;
            }
            EVOLUTION_STAGE = 0;
            AbstractDungeon.player.shoulderImg = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_1);
            AbstractDungeon.player.shoulder2Img = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_1);
        }

        AboutDigivolution.ClearHigherCards();

        switch(EVOLUTION_STAGE){
            case 0:
                AbstractDungeon.player.shoulderImg = ImageMaster.loadImage(AGUMON_SHOULDER_1);
                AbstractDungeon.player.shoulder2Img = ImageMaster.loadImage(AGUMON_SHOULDER_1);
                break;
            case 1:
                AbstractDungeon.player.shoulderImg = ImageMaster.loadImage(GREYMON_SHOULDER_1);
                AbstractDungeon.player.shoulder2Img = ImageMaster.loadImage(GREYMON_SHOULDER_1);
                break;
            case 2:
                AbstractDungeon.player.shoulderImg = ImageMaster.loadImage(METALGREYMON_SHOULDER_1);
                AbstractDungeon.player.shoulder2Img = ImageMaster.loadImage(METALGREYMON_SHOULDER_1);
                break;
            case 3:
                AbstractDungeon.player.shoulderImg = ImageMaster.loadImage(WARGREYMON_SHOULDER_1);
                AbstractDungeon.player.shoulder2Img = ImageMaster.loadImage(WARGREYMON_SHOULDER_1);
                break;

        }

        super.onVictory();
        logger.info("Agumon | onvictory..");

    }
}


