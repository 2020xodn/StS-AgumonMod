package Agumon.events;

import Agumon.AgumonMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static Agumon.AgumonMod.makeEventPath;

public class PalmTreeEvent extends AbstractImageEvent {
    public static final String ID = AgumonMod.makeID(PalmTreeEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS;
    private static final String[] OPTIONS;
    private static final String DIALOG_START;
    private static final String DIALOG_HUNGRY;
    private static final String DIALOG_FRUITS;
    private static final String DIALOG_BREAKING;
    private static final String DIALOG_AVOID;
    public static final String IMG = makeEventPath(PalmTreeEvent.class.getSimpleName() + "Intro.png");

    private PalmTreeEvent.SCREEN screen;

    private boolean attack;
    private boolean skill;
    private boolean power;
    private AbstractCard attackCard;
    private AbstractCard skillCard;
    private AbstractCard powerCard;
    private int healthDamage;
    private int healthHeal;

    public PalmTreeEvent() {
        super(NAME, DIALOG_START, IMG);

        AgumonMod.logger.info("PalmTreeEvent | Constructor");
        this.setCards();
        this.screen = SCREEN.INTRO;
        healthDamage = (int) (AbstractDungeon.player.maxHealth * 0.05);
        healthHeal = healthDamage * 6;

        imageEventText.setDialogOption(OPTIONS[0] + healthHeal + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2] + healthDamage + OPTIONS[3]);
    }

    private void setCards() {
        this.attack = CardHelper.hasCardWithType(AbstractCard.CardType.ATTACK);
        this.skill = CardHelper.hasCardWithType(AbstractCard.CardType.SKILL);
        this.power = CardHelper.hasCardWithType(AbstractCard.CardType.POWER);
        if (this.attack) {
            this.attackCard = CardHelper.returnCardOfType(AbstractCard.CardType.ATTACK, AbstractDungeon.miscRng);
        }

        if (this.skill) {
            this.skillCard = CardHelper.returnCardOfType(AbstractCard.CardType.SKILL, AbstractDungeon.miscRng);
        }

        if (this.power) {
            this.powerCard = CardHelper.returnCardOfType(AbstractCard.CardType.POWER, AbstractDungeon.miscRng);
        }

    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:

                        double randomValue = Math.random();
                        if (randomValue <= 0.5 && !(!this.skill && !this.power && !this.attack)) {
                            imageEventText.loadImage(makeEventPath(PalmTreeEvent.class.getSimpleName() + "Cut.png"));
                            this.imageEventText.updateBodyText(DIALOG_BREAKING);
                            screen = SCREEN.SWING;

                            this.imageEventText.clearAllDialogs();

                            if (this.attack) {
                                this.imageEventText.setDialogOption(OPTIONS[5] + this.attackCard.name, this.attackCard.makeStatEquivalentCopy());
                            } else {
                                this.imageEventText.setDialogOption(OPTIONS[6], true);
                            }

                            if (this.power) {
                                this.imageEventText.setDialogOption(OPTIONS[7] + this.powerCard.name, this.powerCard.makeStatEquivalentCopy());
                            } else {
                                this.imageEventText.setDialogOption(OPTIONS[8], true);
                            }

                            if (this.skill) {
                                this.imageEventText.setDialogOption(OPTIONS[9] + this.skillCard.name, this.skillCard.makeStatEquivalentCopy());
                            } else {
                                this.imageEventText.setDialogOption(OPTIONS[10], true);
                            }

                        }
                        else{
                            imageEventText.loadImage(makeEventPath(PalmTreeEvent.class.getSimpleName() + "Food.png"));
                            this.imageEventText.updateBodyText(DIALOG_FRUITS);
                            AbstractDungeon.player.heal(healthHeal);
                            screen = SCREEN.LEAVE;
                            this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                            this.imageEventText.clearRemainingOptions();
                        }

                        break;

                    case 1:
                        this.imageEventText.updateBodyText(DIALOG_HUNGRY);
                        screen = SCREEN.LEAVE;
                        AbstractDungeon.player.currentHealth -= healthDamage;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        break;
                }
                break;

            case SWING: // Swing the Tree
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.attackCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.attackCard);
                        break;

                    case 1:
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.powerCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.powerCard);
                        break;

                    case 2:
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.skillCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.skillCard);
                        break;
                }

                this.imageEventText.updateBodyText(DIALOG_AVOID);
                screen = SCREEN.LEAVE;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
                break;

            case LEAVE: // Only For Leave
                AgumonMod.logger.info("PalmTreeEvent | Leave");
                this.openMap();
                break;
        }
    }

    static{
        OPTIONS = eventStrings.OPTIONS;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        DIALOG_START = DESCRIPTIONS[0];
        DIALOG_HUNGRY = DESCRIPTIONS[1];
        DIALOG_FRUITS = DESCRIPTIONS[2];
        DIALOG_BREAKING = DESCRIPTIONS[3];
        DIALOG_AVOID = DESCRIPTIONS[4];
    }

    private static enum SCREEN{
        INTRO,
        SWING,
        LEAVE
    }
}
