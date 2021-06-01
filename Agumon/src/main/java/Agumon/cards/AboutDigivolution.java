package Agumon.cards;

import Agumon.AgumonMod;
import Agumon.actions.AddHPAction;
import Agumon.cards.attack.*;
import Agumon.cards.power.Digivolution;
import Agumon.cards.power.SuperDigivolution;
import Agumon.cards.power.UltimateDigivolution;
import Agumon.cards.power.Wargreymon_BraveShield;
import Agumon.cards.skill.Agumon_Defend;
import Agumon.cards.skill.Greymon_Defend;
import Agumon.cards.skill.Metalgreymon_Defend;
import Agumon.cards.skill.Wargreymon_Defend;
import Agumon.characters.Agumon;
import Agumon.powers.evolvePower;
import Agumon.relics.CrestOfCourage;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;
import java.util.Iterator;

import static Agumon.characters.Agumon.*;

public class AboutDigivolution {
    public static final String ID = AgumonMod.makeID(AboutDigivolution.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static void ClearStage(){
        isMetalgreymon = false;
        isWargreymon = false;
    }

    public static void ClearHigherCards(){
        hasBraveShield.clear();
        hasGigaDestroyer.clear();
        removedDigivolution.clear();
    }

    public static void RemoveHigherCards(){
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> cardsToRemoveList = new ArrayList();
        Iterator cardInDeck = p.drawPile.group.iterator();

        AbstractCard c;
        while (cardInDeck.hasNext()){
            c = (AbstractCard)cardInDeck.next();

            if (c.cardID == Wargreymon_BraveShield.ID){     // Wargreymon_BraveShield
                if (EVOLUTION_STAGE != 3) {
                    boolean bool_tmp = bool_tmp = c.upgraded;

                    hasBraveShield.add(bool_tmp);
                    cardsToRemoveList.add(c);
                }
            }

            else if (c.cardID == Metalgreymon_GigaDestroyer.ID){     // Metalgreymon_GigaDestroyer
                if (EVOLUTION_STAGE != 2) {
                    boolean bool_tmp = c.upgraded;

                    hasGigaDestroyer.add(bool_tmp);
                    cardsToRemoveList.add(c);
                }
            }

            else if (c.cardID == Digivolution.ID){     // Digivolution
                if (EVOLUTION_STAGE == 1 && !AbstractDungeon.player.hasRelic(CrestOfCourage.ID)) {
                    boolean bool_tmp = c.upgraded;

                    removedDigivolution.add(bool_tmp);
                    cardsToRemoveList.add(c);
                }
            }
        }

        Iterator removeIterator = cardsToRemoveList.iterator();  // Empty now
        while(removeIterator.hasNext()) {
            c = (AbstractCard)removeIterator.next();
            p.drawPile.removeCard(c);
        }

    }

    public static int[] CardsToRemove(String certainCardID, String where){
        ArrayList<AbstractCard> cardsToRemove = new ArrayList();    // 0 -> Nonupgraded, 1 -> Upgraded
        Iterator var2 = cardsToRemove.iterator();  // Empty now
        if (where.equals("Deck"))
            var2 = AbstractDungeon.player.drawPile.group.iterator();
        if (where.equals("Hand"))
            var2 = AbstractDungeon.player.hand.group.iterator();
        if (where.equals("Discard"))
            var2 = AbstractDungeon.player.discardPile.group.iterator();
        if (where.equals("Exhaust"))
            var2 = AbstractDungeon.player.exhaustPile.group.iterator();

        int[] cardAmount = new int[]{0, 0}; // Normal Card, Upgraded Card

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.cardID == certainCardID){
                if (c.upgraded)
                    cardAmount[1]++;
                else
                    cardAmount[0]++;

                cardsToRemove.add(c);
            }
        }

        var2 = cardsToRemove.iterator();
        AbstractPlayer p = AbstractDungeon.player;

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (where.equals("Deck"))
                p.drawPile.removeCard(c);
            if (where.equals("Hand"))
                p.hand.removeCard(c);
            if (where.equals("Discard"))
                p.discardPile.removeCard(c);
        }

        return cardAmount;
    }

    public static void CardsToRemoveEverywhereAddEvolved(String beforeID, String after){
        int []AmountInDeck = CardsToRemove(beforeID, "Deck");   // [normal amount, upgraded amount]
        int []AmountInHand = CardsToRemove(beforeID, "Hand");
        int []AmountInDiscard = CardsToRemove(beforeID, "Discard");

        try {
            AbstractCard upgradedEvolution = (AbstractCard)Class.forName(after).newInstance();
            upgradedEvolution.upgrade();

            if (AmountInDeck[0] != 0)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard)Class.forName(after).newInstance(), AmountInDeck[0], true, true));
            if (AmountInHand[0] != 0)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction((AbstractCard)Class.forName(after).newInstance(), AmountInHand[0], false));
            if (AmountInDiscard[0] != 0)
               AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction((AbstractCard)Class.forName(after).newInstance(), AmountInDiscard[0]));

            if (AmountInDeck[1] != 0)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(upgradedEvolution, AmountInDeck[1], true, true));
            if (AmountInHand[1] != 0)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(upgradedEvolution, AmountInHand[1], false));
            if (AmountInDiscard[1] != 0)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(upgradedEvolution, AmountInDiscard[1]));

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
        }
    }

    public static void RemoveEverywhere(String certainCardID){
        CardsToRemove(certainCardID, "Deck");
        CardsToRemove(certainCardID, "Hand");
        CardsToRemove(certainCardID, "Discard");
    }

    // About Evolution
    public static void Digivolution(boolean oneStep){
        AbstractPlayer p = AbstractDungeon.player;

        if (oneStep){
            switch (EVOLUTION_STAGE){
                case 1:         // Agumon -> Greymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_GREYMON));
                    ((Agumon)AbstractDungeon.player).animationEvolveAgumonToGreymon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.GREYMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.GREYMON_SHOULDER_2);
                    break;

                case 2:         // Greymon -> Metalgreymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_METALGREYMON));
                    ((Agumon)AbstractDungeon.player).animationEvolveGreymonToMetalgreymon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.METALGREYMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.METALGREYMON_SHOULDER_2);
                    break;
                case 3:         // Metalgreymon -> Wargreymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_WARGREYMON));
                    ((Agumon)AbstractDungeon.player).animationEvolveMetalgreymonToWargreymon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.WARGREYMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.WARGREYMON_SHOULDER_2);
                    break;

                default:
                    logger.info("Digivolution ERROR!, EVOLUTION STAGE is " + EVOLUTION_STAGE);

            }

        }
        else{   // Agumon -> X
            switch(EVOLUTION_STAGE){
                case 1:     // Agumon -> Greymon
                    Devolution(true);
                    break;

//                case 2:     // Agumon -> Metalgreymon     // NOT USING
//                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -TheDefault.ADDHP_GREYMON));
//                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -TheDefault.ADDHP_METALGREYMON));
//
//                    ((TheDefault) AbstractDungeon.player).animationDevolveMetalgreymonToAgumon();
//                    break;

                case 3:     // Agumon -> Wargreymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_GREYMON));
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_METALGREYMON));
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, Agumon.ADDHP_WARGREYMON));

                    ((Agumon) AbstractDungeon.player).animationEvolveAgumonToWargreymon();
                    break;
            }

        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new evolvePower(p, p, 1), 1));
    }

    public static void makeCertainCards(String CardclassGetName, Iterator <Boolean> tmpIterator){
        try {
            AbstractCard nonupgradedCard = (AbstractCard)Class.forName(CardclassGetName).newInstance();
            AbstractCard upgradedCard = (AbstractCard)Class.forName(CardclassGetName).newInstance();
            nonupgradedCard.upgrade();

//            boolean [] bool_tmp = new boolean[2];
            boolean bool_tmp;
            while(tmpIterator.hasNext()) {
                bool_tmp = tmpIterator.next();

                if (bool_tmp){
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(nonupgradedCard,
                            1, true, true));
                }
                else{
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(upgradedCard,
                            1, true, true));
                }
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ignored) {
        }

        logger.info(CardclassGetName + "생성 완료 !");
    }

    public static void EvolutionCards(boolean oneStep){
        if (oneStep) {
            switch (EVOLUTION_STAGE) {
                case 1: // Agumon -> Greymon
                    if (AbstractDungeon.player.hasRelic(CrestOfCourage.ID)) {
                        AboutDigivolution.CardsToRemoveEverywhereAddEvolved(Digivolution.ID, SuperDigivolution.class.getName());
                    } else {
//                        RemoveEverywhere(Digivolution.ID);
                    }
                    CardsToRemoveEverywhereAddEvolved(Agumon_Claw.ID, Greymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_Defend.ID, Greymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyFlame.ID, Greymon_MegaFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyBurner.ID, Greymon_MegaBurst.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_FuryClaw.ID, Greymon_TailWhip.class.getName());
                    break;

                case 2: // Greymon -> Metalgreymon
                    makeCertainCards(Metalgreymon_GigaDestroyer.class.getName(), hasGigaDestroyer.iterator());
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new UltimateDigivolution(), 1, true, true));

                    CardsToRemoveEverywhereAddEvolved(Greymon_Claw.ID, Metalgreymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_Defend.ID, Metalgreymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_MegaFlame.ID, Metalgreymon_OverFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_MegaBurst.ID, Metalgreymon_GigaStorm.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_TailWhip.ID, Metalgreymon_TailWhip.class.getName());
                    break;

                case 3: // Metalgreymon -> Wargreymon
                    makeCertainCards(Wargreymon_BraveShield.class.getName(), hasBraveShield.iterator());

                    RemoveEverywhere(Metalgreymon_GigaDestroyer.ID);

                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Claw.ID, Wargreymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Defend.ID, Wargreymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_OverFlame.ID, Wargreymon_Gaia1.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_GigaStorm.ID, Wargreymon_Gaia2.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_TailWhip.ID, Wargreymon_BraveTornado.class.getName());

                    break;

                default:
                    logger.info("EvolutionCards ERROR!, EVOLUTION STAGE is " + EVOLUTION_STAGE);
                    break;
            }
        }
        else{   // Agumon -> X
            switch (EVOLUTION_STAGE){
                case 1:
                    EvolutionCards(true);
                    break;
                case 2: // Agumon -> MetalGreymon

                    makeCertainCards(Metalgreymon_GigaDestroyer.class.getName(), hasGigaDestroyer.iterator());

//                    RemoveEverywhere(Digivolution.ID);
//                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new UltimateDigivolution(), 1, true, true));

                    CardsToRemoveEverywhereAddEvolved(Digivolution.ID, UltimateDigivolution.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_Claw.ID, Metalgreymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_Defend.ID, Metalgreymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyFlame.ID, Metalgreymon_OverFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyBurner.ID, Metalgreymon_GigaStorm.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_FuryClaw.ID, Metalgreymon_TailWhip.class.getName());
                    break;

                case 3: // Agumon -> Wargreymon    Warp Evolution
                    makeCertainCards(Wargreymon_BraveShield.class.getName(), hasBraveShield.iterator());
                    for (AbstractCard c : AbstractDungeon.player.hand.group){
                        if (c.cardID == Digivolution.ID)
                            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                    RemoveEverywhere(Digivolution.ID);

                    CardsToRemoveEverywhereAddEvolved(Agumon_Claw.ID, Wargreymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_Defend.ID, Wargreymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyFlame.ID, Wargreymon_Gaia1.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_BabyBurner.ID, Wargreymon_Gaia2.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Agumon_FuryClaw.ID, Wargreymon_BraveTornado.class.getName());
                    break;

                default:
                    logger.info("EvolutionCards ERROR!, EVOLUTION STAGE is " + EVOLUTION_STAGE);
                    break;
            }
        }

    }

    // About Devolution
    public static void DevolutionCards(boolean oneStep){
        if (oneStep) {
            switch(EVOLUTION_STAGE){
                case 1: // Greymon -> Agumon

                    CardsToRemoveEverywhereAddEvolved(SuperDigivolution.ID, Digivolution.class.getName());

                    int []AmountInDeck1 = CardsToRemove(SuperDigivolution.ID, "Deck");   // [normal amount, upgraded amount]
                    int []AmountInHand1 = CardsToRemove(SuperDigivolution.ID, "Hand");
                    int []AmountInDiscard1 = CardsToRemove(SuperDigivolution.ID, "Discard");
                    if (AmountInDeck1[0] + AmountInDeck1[1] + AmountInDiscard1[0] + AmountInDiscard1[1] + AmountInHand1[0] + AmountInHand1[1] != 0){
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Digivolution()));
                    }

                    CardsToRemoveEverywhereAddEvolved(Greymon_Claw.ID, Agumon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_Defend.ID, Agumon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_MegaFlame.ID, Agumon_BabyFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_MegaBurst.ID, Agumon_BabyBurner.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Greymon_TailWhip.ID, Agumon_FuryClaw.class.getName());
                    break;

                case 2: // Metalgreymon -> Greymon
                    RemoveEverywhere(Metalgreymon_GigaDestroyer.ID);
                    CardsToRemoveEverywhereAddEvolved(UltimateDigivolution.ID, SuperDigivolution.class.getName());

                    int []AmountInDeck2 = CardsToRemove(UltimateDigivolution.ID, "Deck");   // [normal amount, upgraded amount]
                    int []AmountInHand2 = CardsToRemove(UltimateDigivolution.ID, "Hand");
                    int []AmountInDiscard2 = CardsToRemove(UltimateDigivolution.ID, "Discard");
                    if (AmountInDeck2[0] + AmountInDeck2[1] + AmountInDiscard2[0] + AmountInDiscard2[1] + AmountInHand2[0] + AmountInHand2[1] != 0){
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new SuperDigivolution()));
                    }

                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Claw.ID, Greymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Defend.ID, Greymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_OverFlame.ID, Greymon_MegaFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_GigaStorm.ID, Greymon_MegaBurst.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_TailWhip.ID, Greymon_TailWhip.class.getName());
                    break;

                case 3:     // Wargreymon -> Metalgreymon
                    RemoveEverywhere(Wargreymon_BraveShield.ID);
                    makeCertainCards(Metalgreymon_GigaDestroyer.class.getName(), hasGigaDestroyer.iterator());
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new UltimateDigivolution()));

                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Claw.ID, Metalgreymon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Defend.ID, Metalgreymon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Gaia1.ID, Metalgreymon_OverFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Gaia2.ID, Metalgreymon_GigaStorm.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_BraveTornado.ID, Metalgreymon_TailWhip.class.getName());

                    break;

                default:
                    logger.info("DevolutionCards ERROR!, EVOLUTION STAGE is " + EVOLUTION_STAGE);
                    break;

            }
        }
        else{   // X -> Agumon
            switch(EVOLUTION_STAGE){
                case 1:     // Greymon -> Agumon
                    DevolutionCards(true);
                    break;

                case 2:     // Metalgreymon -> Agumon
                    RemoveEverywhere(Metalgreymon_GigaDestroyer.ID);

                    CardsToRemoveEverywhereAddEvolved(UltimateDigivolution.ID, Digivolution.class.getName());

                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Claw.ID, Agumon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_Defend.ID, Agumon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_OverFlame.ID, Agumon_BabyFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_GigaStorm.ID, Agumon_BabyBurner.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Metalgreymon_TailWhip.ID, Agumon_FuryClaw.class.getName());
                    break;

                case 3:     // Wargreymon -> Agumon
                    RemoveEverywhere(Wargreymon_BraveShield.ID);

                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Digivolution()));

                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Claw.ID, Agumon_Claw.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Defend.ID, Agumon_Defend.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Gaia1.ID, Agumon_BabyFlame.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_Gaia2.ID, Agumon_BabyBurner.class.getName());
                    CardsToRemoveEverywhereAddEvolved(Wargreymon_BraveTornado.ID, Agumon_FuryClaw.class.getName());
                    break;
            }
        }
    }

    public static void Devolution(boolean oneStep){
        AbstractPlayer p = AbstractDungeon.player;

        if (oneStep){
            switch (EVOLUTION_STAGE){
                case 1:         // Greymon -> Agumon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_GREYMON));
                    ((Agumon) AbstractDungeon.player).animationDevolveGreymonToAgumon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_2);
                    break;

                case 2:         // Metalgreymon -> Greymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_METALGREYMON));
                    ((Agumon) AbstractDungeon.player).animationDevolveMetalgreymonToGreymon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.GREYMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.GREYMON_SHOULDER_2);
                    break;
                case 3:         // Wargreymon -> Metalgreymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_WARGREYMON));
                    ((Agumon) AbstractDungeon.player).animationDevolveWargreymonToMetalgreymon();
                    p.shoulderImg = ImageMaster.loadImage(AgumonMod.METALGREYMON_SHOULDER_1);
                    p.shoulder2Img = ImageMaster.loadImage(AgumonMod.METALGREYMON_SHOULDER_2);
                    break;

                default:
                    logger.info("Devolution ERROR!, EVOLUTION STAGE is " + EVOLUTION_STAGE);

            }

        }
        else{   // X -> Agumon
            switch(EVOLUTION_STAGE){
                case 1:     // Greymon -> Agumon
                    Devolution(true);
                    break;

                case 2:     // Metalgreymon -> Greymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_GREYMON));
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_METALGREYMON));

                    ((Agumon) AbstractDungeon.player).animationDevolveMetalgreymonToAgumon();
                    break;

                case 3:     // Wargreymon -> Metalgreymon
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_GREYMON));
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_METALGREYMON));
                    AbstractDungeon.actionManager.addToBottom(new AddHPAction(p, -Agumon.ADDHP_WARGREYMON));

                    ((Agumon) AbstractDungeon.player).animationDevolveWargreymonToAgumon();
                    break;
            }
            p.shoulderImg = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_1);
            p.shoulder2Img = ImageMaster.loadImage(AgumonMod.AGUMON_SHOULDER_1);

        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new evolvePower(p, p, 1), 1));
    }

}
