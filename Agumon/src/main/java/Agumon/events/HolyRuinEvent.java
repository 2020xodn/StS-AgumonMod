package Agumon.events;

import Agumon.AgumonMod;
import Agumon.relics.HolyPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

import static Agumon.AgumonMod.makeEventPath;

public class HolyRuinEvent extends AbstractImageEvent {
    public static final String ID = AgumonMod.makeID(HolyRuinEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS;
    private static final String[] OPTIONS;
    private static final String DIALOG_START;
    private static final String DIALOG_FOLLOWLIGHT;
    private static final String DIALOG_CHOSE_GETRELIC;
    private static final String DIALOG_LEAVE;

    public static final String IMG = makeEventPath(HolyRuinEvent.class.getSimpleName() + "Intro.png");

    private HolyRuinEvent.SCREEN screen;

    private int healthdamage;
    private int gainGoldAmount;
    private AbstractRelic relicAdd = null;

    public HolyRuinEvent() {
        super(NAME, DIALOG_START, IMG);

        AgumonMod.logger.info("HolyRuinEvent | Constructor");
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.gainGoldAmount = 100;
        } else {
            this.gainGoldAmount = 50;
        }
        healthdamage = (int) (AbstractDungeon.player.maxHealth * 0.1);

        this.screen = SCREEN.INTRO;
        imageEventText.setDialogOption(OPTIONS[0] + healthdamage + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        imageEventText.loadImage(makeEventPath(HolyRuinEvent.class.getSimpleName() + "Meet.png"));
                        this.imageEventText.updateBodyText(DIALOG_FOLLOWLIGHT);
                        screen = SCREEN.FOLLOWLIGHT;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3], new HolyPower());
                        this.imageEventText.setDialogOption(OPTIONS[4] + gainGoldAmount + OPTIONS[5]);

                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        AbstractDungeon.player.currentHealth -= healthdamage;
                        break;

                    case 1:
                        this.imageEventText.updateBodyText(DIALOG_LEAVE);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        screen = SCREEN.LEAVE;
                        break;
                }
                break;
            case FOLLOWLIGHT: // Follow Light
                switch (buttonPressed) {
                    case 0:
                        imageEventText.loadImage(makeEventPath(HolyRuinEvent.class.getSimpleName() + "Get.png"));
                        if (AbstractDungeon.player.hasRelic(HolyPower.ID)) {
                            this.relicAdd = RelicLibrary.getRelic("Circlet").makeCopy();
                        } else {
                            this.relicAdd = RelicLibrary.getRelic(HolyPower.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relicAdd);

                        this.imageEventText.updateBodyText(DIALOG_CHOSE_GETRELIC);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        screen = SCREEN.LEAVE;
                        break;

                    case 1:
                        this.imageEventText.updateBodyText(DIALOG_LEAVE);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        screen = SCREEN.LEAVE;

                        AbstractDungeon.effectList.add(new RainingGoldEffect(gainGoldAmount));
                        AbstractDungeon.player.gainGold(gainGoldAmount);
                        break;
                }
                break;

            case LEAVE: // Only For Leave
                this.openMap();
                break;

            default:
                this.openMap();
        }
    }

    static{
        OPTIONS = eventStrings.OPTIONS;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        DIALOG_START = DESCRIPTIONS[0];
        DIALOG_FOLLOWLIGHT = DESCRIPTIONS[1];
        DIALOG_CHOSE_GETRELIC = DESCRIPTIONS[2];
        DIALOG_LEAVE = DESCRIPTIONS[3];
    }

    private static enum SCREEN{
        INTRO,
        FOLLOWLIGHT,
        LEAVE;
    }
}
