package Agumon.relics;

import Agumon.AgumonMod;
import Agumon.actions.GetCrestAction;
import Agumon.cards.AboutCourage;
import Agumon.cards.AboutDigivolution;
import Agumon.cards.power.Digivolution;
import Agumon.characters.Agumon;
import Agumon.powers.burnPower;
import Agumon.powers.evolvePower;
import Agumon.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Iterator;

import static Agumon.AgumonMod.logger;
import static Agumon.AgumonMod.*;
import static Agumon.characters.Agumon.*;

public class defaultDigivice extends CustomRelic {

    public static final String ID = AgumonMod.makeID(defaultDigivice.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(defaultDigivice.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(defaultDigivice.class.getSimpleName() + ".png"));

    private static boolean isFirstTurn = false;
    private static boolean challengethespire = false;
    private static boolean isChallengethespire = false;

    public defaultDigivice() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);

        if (Loader.isModLoaded("challengethespire")){
            challengethespire = true;
        }
    }

    @Override
    public void atBattleStart() {
        isFirstTurn = true;
    }

    @Override
    public void atBattleStartPreDraw() {
//        if (isChallengethespire)
//            addToBot(new GetCrestAction());

        if (AbstractDungeon.actNum >= 2 && !AbstractDungeon.player.hasRelic(CrestOfCourage.ID) || isChallengethespire){
            double randomValue = Math.random();
            logger.info("defaultDigivice | Your number = " + randomValue);
            if (randomValue <= 0.60 | isChallengethespire) {   // Get Crest of Courage  randomly after clear 1 Stage.
                addToBot(new GetCrestAction());
                logger.info("defaultDigivice | after GetCrestAction");
            }
            else{
                logger.info("defaultDigivice | fail to GetCrestAction");
            }
        }

        AbstractPlayer p = AbstractDungeon.player;
        flash();

        if (Agumon.EVOLUTION_STAGE == 0){
            switch(this.counter){
                case 1:
                    logger.info("Greymon <- Agumon");
                    ((Agumon)AbstractDungeon.player).animationEvolveAgumonToGreymon();
                    Agumon.EVOLUTION_STAGE = 1;
                    break;

                case 2:
                    logger.info("Metalgreymon <- Agumon");
                    ((Agumon)AbstractDungeon.player).animationEvolveAgumonToMetalgreymon();
                    Agumon.EVOLUTION_STAGE = 2;
                    break;

                case 3:
                    logger.info("Wargreymon <- Agumon");
                    ((Agumon)AbstractDungeon.player).animationEvolveAgumonToWargreymon();
                    Agumon.EVOLUTION_STAGE = 3;
                    break;

                default:
                    logger.info("defaultDigivice | atBattleStartPreDraw case default ERROR :" + this.counter);
            }
        }
        AboutDigivolution.RemoveHigherCards();

        addToBot(new ApplyPowerAction(p, p, new evolvePower(p, p, 1), 1));  // Display Generation

        // Heal or apply Weak
        switch(Agumon.EVOLUTION_STAGE){
            case 3:
                Iterator monsterList1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(monsterList1.hasNext()) {
                    AbstractMonster monster = (AbstractMonster)monsterList1.next();
                    if (!monster.isDead && !monster.isDying) {
                        addToBot(new ApplyPowerAction(monster, p, new burnPower(monster, p, 2, monster.type), 2));
                    }
                }
            case 2:
                Iterator monsterList2 = AbstractDungeon.getMonsters().monsters.iterator();

                while(monsterList2.hasNext()) {
                    AbstractMonster monster = (AbstractMonster)monsterList2.next();
                    if (!monster.isDead && !monster.isDying) {
                        addToBot(new ApplyPowerAction(monster, monster, new WeakPower(monster, 2, false), 2));
                    }
                }
            case 1:
                addToBot((new HealAction(p, p, 2)));
            default:
                break;
        }
    }

    @Override
    public void atTurnStart() {
        logger.info("defaultDigivice | atTurnStart !");
        AboutCourage.gainCourage(2);
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));  // Just Effect?
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {    // First Turn Evolve Card
        if (isFirstTurn){
            int masterHand = AbstractDungeon.player.masterHandSize;
            int masterDeck = AbstractDungeon.player.masterDeck.size();

            if (EVOLUTION_STAGE != 2){
                masterDeck -= hasGigaDestroyer.size();
            }
            if (EVOLUTION_STAGE != 3){
                masterDeck -= hasBraveShield.size();
            }
            if (masterDeck < masterHand)
                masterHand = masterDeck;

            if (AbstractDungeon.player.hand.size() >= masterHand){
                logger.info("defaultDigivice | Card Evolution !");
                isFirstTurn = false;
//            AboutDigivolution.RemoveHigherCards();
                AboutDigivolution.EvolutionCards(false);
            }
        }
    }

    @Override
    public void onVictory() {   // Return To Agumon
        this.counter = Agumon.EVOLUTION_STAGE;
        logger.info("Digivice | Victory counter : " + this.counter);
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            this.counter = 0;
        }
    }

    @Override
    public void onEquip() {
        AbstractCard newCard = new Digivolution();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(newCard, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));


        if (challengethespire){
            for (AbstractDailyMod mod : ModHelper.enabledMods){
                if (mod.name.equals("Elite Rush") ||
                    mod.name.equals("Modded Elite Rush") ||
                    mod.name.equals("Boss Rush") ||
                    mod.name.equals("Modded Boss Rush") ||
                    mod.name.equals("Sneaky Strike")){
                    isChallengethespire = true;
                    break;
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
