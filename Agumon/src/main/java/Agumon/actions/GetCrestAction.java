package Agumon.actions;

import Agumon.relics.CrestOfCourage;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static Agumon.characters.Agumon.logger;

public class GetCrestAction extends AbstractGameAction {

    public GetCrestAction() {
    }

    public void update() {
        logger.info("GetCrestAction | Running");
        this.isDone = true;
        if(!AbstractDungeon.player.hasRelic(CrestOfCourage.ID)) {
            AbstractRelic relicAdd = RelicLibrary.getRelic(CrestOfCourage.ID).makeCopy();
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relicAdd);

        }
    }
}