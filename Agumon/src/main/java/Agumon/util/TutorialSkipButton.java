//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Agumon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.helpers.ShaderHelper.Shader;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.EndTurnGlowEffect;
import java.util.ArrayList;
import java.util.Iterator;

import static Agumon.characters.Agumon.logger;

public class TutorialSkipButton {
    private String label;
    private static final Color DISABLED_COLOR;
    private static final float SHOW_X;
    private static final float SHOW_Y;
    private static final float HIDE_X;
    public float current_x;
    public float current_y;
    private float target_x;
    private boolean isHidden;
    public boolean enabled;
    private boolean isDisabled;
    private Color textColor;
    private ArrayList<EndTurnGlowEffect> glowList;
    private static final float GLOW_INTERVAL = 1.2F;
    private float glowTimer;
    public boolean isGlowing;
    public boolean isWarning;
    public Hitbox hb;
    private float holdProgress;
    private static final float HOLD_DUR = 0.4F;
    private Color holdBarColor;

    public TutorialSkipButton() {
        this.current_x = HIDE_X;
        this.current_y = SHOW_Y;
        this.target_x = this.current_x;
        this.isHidden = true;
        this.enabled = true;
        this.isDisabled = false;
        this.glowList = new ArrayList();
        this.glowTimer = 0.0F;
        this.isGlowing = false;
        this.isWarning = false;
        this.hb = new Hitbox(0.0F, 0.0F, 230.0F * Settings.scale, 110.0F * Settings.scale);
        this.holdProgress = 0.0F;
        this.holdBarColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.update();
    }

    public void update() {
        this.glow();
        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
            }
        }

        this.hb.move(this.current_x, this.current_y);
        if (this.enabled) {
//            this.isDisabled = false;
//            if (AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode) {
//                this.isDisabled = true;
//            }

            if (AbstractDungeon.player.hoveredCard == null) {
                this.hb.update();
            }

            if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
        }
    }

    public void updateText(String msg) {
        this.label = msg;
    }

    private void glow() {
        if (this.isGlowing && !this.isHidden) {
            if (this.glowTimer < 0.0F) {
                this.glowList.add(new EndTurnGlowEffect());
                this.glowTimer = 1.2F;
            } else {
                this.glowTimer -= Gdx.graphics.getDeltaTime();
            }
        }

        Iterator i = this.glowList.iterator();

        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }


    public void show() {
        if (this.isHidden) {
            this.target_x = SHOW_X;
            this.isHidden = false;
            if (this.isGlowing) {
                this.glowTimer = -1.0F;
            }
        }

    }

    public void render(SpriteBatch sb) {
        if (!Settings.hideEndTurn) {
            float tmpY = this.current_y;
            this.renderHoldEndTurn(sb);
            if (!this.isDisabled && this.enabled) {
                if (this.hb.hovered) {
                    if (this.isWarning) {
                        this.textColor = Settings.RED_TEXT_COLOR;
                    } else {
                        this.textColor = Color.CYAN;
                    }
                } else if (this.isGlowing) {
                    this.textColor = Settings.GOLD_COLOR;
                } else {
                    this.textColor = Settings.CREAM_COLOR;
                }

            }else {
                this.textColor = Color.LIGHT_GRAY;
            }

            if (this.hb.clickStarted && !AbstractDungeon.isScreenUp) {
                tmpY -= 2.0F * Settings.scale;
            } else if (this.hb.hovered && !AbstractDungeon.isScreenUp) {
                tmpY += 2.0F * Settings.scale;
            }

            if (!this.enabled) {
                ShaderHelper.setShader(sb, Shader.GRAYSCALE);
            } else if (!this.isDisabled && (!this.hb.clickStarted || !this.hb.hovered)) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(DISABLED_COLOR);
            }

            Texture buttonImg;
            if (this.isGlowing && !this.hb.clickStarted) {
                buttonImg = ImageMaster.END_TURN_BUTTON_GLOW;
            } else {
                buttonImg = ImageMaster.END_TURN_BUTTON;
            }

            if (this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                sb.draw(ImageMaster.END_TURN_HOVER, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            }

            sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (!this.enabled) {
                ShaderHelper.setShader(sb, Shader.DEFAULT);
            }

            this.renderGlowEffect(sb, this.current_x, this.current_y);
            if ((this.hb.hovered || this.holdProgress > 0.0F) && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                sb.setBlendFunction(770, 1);
                sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
                sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }

            if (Settings.isControllerMode && this.enabled) {
                sb.setColor(Color.WHITE);
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.current_x - 32.0F - 42.0F * Settings.scale - FontHelper.getSmartWidth(FontHelper.panelEndTurnFont, this.label, 99999.0F, 0.0F) / 2.0F, tmpY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, this.label, this.current_x - 0.0F * Settings.scale, tmpY - 3.0F * Settings.scale, this.textColor);
            if (!this.isHidden) {
                this.hb.render(sb);
            }
        }

    }

    private void renderHoldEndTurn(SpriteBatch sb) {
        if (Settings.USE_LONG_PRESS) {
            this.holdBarColor.r = 0.0F;
            this.holdBarColor.g = 0.0F;
            this.holdBarColor.b = 0.0F;
            this.holdBarColor.a = this.holdProgress * 1.5F;
            sb.setColor(this.holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 107.0F * Settings.scale, this.current_y + 53.0F * Settings.scale - 7.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress + 14.0F * Settings.scale, 20.0F * Settings.scale);
            this.holdBarColor.r = this.holdProgress * 2.5F;
            this.holdBarColor.g = 0.6F + this.holdProgress;
            this.holdBarColor.b = 0.6F;
            this.holdBarColor.a = 1.0F;
            sb.setColor(this.holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 100.0F * Settings.scale, this.current_y + 53.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress, 6.0F * Settings.scale);
        }
    }

    private void renderGlowEffect(SpriteBatch sb, float x, float y) {
        Iterator var4 = this.glowList.iterator();

        while(var4.hasNext()) {
            EndTurnGlowEffect e = (EndTurnGlowEffect)var4.next();
            e.render(sb, x, y);
        }

    }

    static {
        DISABLED_COLOR = new Color(0.7F, 0.7F, 0.7F, 1.0F);
        SHOW_X = 1600.0F * Settings.xScale;
        SHOW_Y = 150.0F * Settings.yScale;
        HIDE_X = SHOW_X + 500.0F * Settings.xScale;
    }
}
