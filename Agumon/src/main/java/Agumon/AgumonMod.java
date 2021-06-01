package Agumon;

import Agumon.characters.Agumon;
import Agumon.events.HolyRuinEvent;
import Agumon.events.PalmTreeEvent;
import Agumon.relics.*;
import Agumon.util.IDCheckDontTouchPls;
import Agumon.util.TextureLoader;
import Agumon.variables.DefaultCustomVariable;
import Agumon.variables.DefaultSecondMagicNumber;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class AgumonMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {

    public static final Logger logger = LogManager.getLogger(AgumonMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Agumon";
    private static final String AUTHOR = "gAsOk"; // And pretty soon - You!
    private static final String DESCRIPTION = "Description in Defaultmod.java";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color AGUMON_COLOR = CardHelper.getColor(234.0f, 141.0f, 75.0f);
//    public static final Color AGUMON_COLOR = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    
    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Card backgrounds - The actual rectangular card.
    private static final String AGUMON_ATTACK = "AgumonResources/images/512/Agumon_attack_bg.png";
    private static final String AGUMON_SKILL = "AgumonResources/images/512/Agumon_skill_bg.png";
    private static final String AGUMON_POWER = "AgumonResources/images/512/Agumon_power_bg.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = "AgumonResources/images/512/card_Agumon_orb.png";
    private static final String CARD_ENERGY_ORB = "AgumonResources/images/512/card_small_orb.png";
    
    private static final String AGUMON_ATTACK_PORTRAIT = "AgumonResources/images/1024/Agumon_attack_bg.png";
    private static final String AGUMON_SKILL_PORTRAIT = "AgumonResources/images/1024/Agumon_skill_bg.png";
    private static final String AGUMON_POWER_PORTRAIT = "AgumonResources/images/1024/Agumon_power_bg.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "AgumonResources/images/1024/card_Agumon_Orb.png";
    
    // Character assets
    private static final String AGUMON_BUTTON = "AgumonResources/images/charSelect/AgumonCharacterButton.png";
    private static final String AGUMON_PORTRAIT = "AgumonResources/images/charSelect/AgumonCharacterPortraitBG.png";
    public static final String AGUMON_SHOULDER_1 = "AgumonResources/images/char/Agumon/Agumon_shoulder.png";
    public static final String AGUMON_SHOULDER_2 = "AgumonResources/images/char/Agumon/Agumon_shoulder.png";
    public static final String GREYMON_SHOULDER_1 = "AgumonResources/images/char/Agumon/Greymon_shoulder.png";
    public static final String GREYMON_SHOULDER_2 = "AgumonResources/images/char/Agumon/Greymon_shoulder.png";
    public static final String METALGREYMON_SHOULDER_1 = "AgumonResources/images/char/Agumon/Metalgreymon_Shoulder.png";
    public static final String METALGREYMON_SHOULDER_2 = "AgumonResources/images/char/Agumon/Metalgreymon_Shoulder.png";
    public static final String WARGREYMON_SHOULDER_1 = "AgumonResources/images/char/Agumon/Wargreymon_shoulder.png";
    public static final String WARGREYMON_SHOULDER_2 = "AgumonResources/images/char/Agumon/Wargreymon_shoulder.png";
    public static final String AGUMON_CORPSE = "AgumonResources/images/char/Agumon/corpse.png";
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "AgumonResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String AGUMON_ATLAS = "AgumonResources/images/char/Agumon/AgumonAnimation.atlas";
    public static final String AGUMON_JSON = "AgumonResources/images/char/Agumon/AgumonAnimation.json";
    public static final String GREYMON_ATLAS = "AgumonResources/images/char/Agumon/GreymonAnimation.atlas";
    public static final String GREYMON_JSON = "AgumonResources/images/char/Agumon/GreymonAnimation.json";
    public static final String METALGREYMON_ATLAS = "AgumonResources/images/char/Agumon/MetalGreymonAnimation.atlas";
    public static final String METALGREYMON_JSON = "AgumonResources/images/char/Agumon/MetalGreymonAnimation.json";
    public static final String WARGREYMON_ATLAS = "AgumonResources/images/char/Agumon/WargreymonAnimation.atlas";
    public static final String WARGREYMON_JSON = "AgumonResources/images/char/Agumon/WargreymonAnimation.json";
    
    // =============== MAKE IMAGE PATHS =================

    public static String makeAttackCardPath(String resourcePath){
        return getModID() + "Resources/images/cards/attack/" + resourcePath;
    }

    public static String makeSkillCardPath(String resourcePath){
        return getModID() + "Resources/images/cards/skill/" + resourcePath;
    }

    public static String makePowerCardPath(String resourcePath){
        return getModID() + "Resources/images/cards/power/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath){
        return getModID() + "Resources/images/ui/" + resourcePath;
    }

    public static String makeTutorialPath(String resourcePath){
        return getModID() + "Resources/images/tutorial/" + resourcePath;
    }
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================


    public AgumonMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        setModID("Agumon");

        logger.info("Done subscribing");
        
        logger.info("Creating the color " + Agumon.Enums.COLOR_GRAY.toString());
        
        BaseMod.addColor(Agumon.Enums.COLOR_GRAY, AGUMON_COLOR, AGUMON_COLOR, AGUMON_COLOR,
                AGUMON_COLOR, AGUMON_COLOR, AGUMON_COLOR, AGUMON_COLOR,
                AGUMON_ATTACK, AGUMON_SKILL, AGUMON_POWER, ENERGY_ORB_DEFAULT_GRAY,
                AGUMON_ATTACK_PORTRAIT, AGUMON_SKILL_PORTRAIT, AGUMON_POWER_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("Agumon", "AgumonConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }


    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = AgumonMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = AgumonMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = AgumonMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing Agumon Mod. Hi. =========================");
        AgumonMod agumonmod = new AgumonMod();
        logger.info("========================= /Agumon Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Agumon.Enums.AGUMON_PLAYERCLASS.toString());
        
        BaseMod.addCharacter(new Agumon("Agumon", Agumon.Enums.AGUMON_PLAYERCLASS),
                AGUMON_BUTTON, AGUMON_PORTRAIT, Agumon.Enums.AGUMON_PLAYERCLASS);
        
        receiveEditPotions();
        logger.info("Added " + Agumon.Enums.AGUMON_PLAYERCLASS.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("agumonmod", "theDefaultConfig", theDefaultDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("AgumonMod | Adding Events");
        BaseMod.addEvent(HolyRuinEvent.ID, HolyRuinEvent.class);
        BaseMod.addEvent(PalmTreeEvent.ID, PalmTreeEvent.class);

        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
//        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, Agumon.Enums.AGUMON_PLAYERCLASS);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        BaseMod.addRelicToCustomPool(new defaultDigivice(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new CrestOfCourage(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BlueDragonPower(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new HolyRing(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new HolyPower(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Ruby(), Agumon.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new DramonKiller(), Agumon.Enums.COLOR_GRAY);
        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        pathCheck();
        logger.info("Add variables");
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        logger.info("Adding cards");

        new AutoAdd("Agumon")
            .packageFilter("Agumon.cards")
            .setDefaultSeen(true)
            .cards();

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    public String getPathByLanguage(){
        String pathByLanguage;
        switch(Settings.language){  // get path by language
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/kor/";
                break;
            case JPN:
                pathByLanguage = getModID() + "Resources/localization/jpn/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/eng/";
        }
        return pathByLanguage;
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        String pathByLanguage = getPathByLanguage();

        BaseMod.loadCustomStringsFile(CardStrings.class, pathByLanguage + "AgumonMod-Card-Strings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, pathByLanguage + "AgumonMod-Power-Strings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, pathByLanguage + "AgumonMod-Relic-Strings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, pathByLanguage + "AgumonMod-Event-Strings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, pathByLanguage + "AgumonMod-Potion-Strings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, pathByLanguage + "AgumonMod-Character-Strings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, pathByLanguage + "AgumonMod-Orb-Strings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, pathByLanguage + "AgumonMod-Ui-Strings.json");
        BaseMod.loadCustomStringsFile(TutorialStrings.class, pathByLanguage + "AgumonMod-Tutorial-Strings.json");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getPathByLanguage() + "AgumonMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

}

