
package com.rafaskoberg.generic.typinglabel.effects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntFloatMap;
import com.rafaskoberg.generic.typinglabel.Effect;
import com.rafaskoberg.generic.typinglabel.TypingGlyph;
import com.rafaskoberg.generic.typinglabel.TypingLabelGeneric;

/** Moves the text horizontally easing it into the final position. Doesn't repeat itself. */
public class SlideEffect extends Effect {
    private static final float DEFAULT_DISTANCE  = 2f;
    private static final float DEFAULT_INTENSITY = 0.375f;

    private float   distance  = 1; // How much of their height they should move
    private float   intensity = 1; // How fast the glyphs should move
    private boolean elastic   = false; // Whether or not the glyphs have an elastic movement

    private IntFloatMap timePassedByGlyphIndex = new IntFloatMap();

    public SlideEffect(TypingLabelGeneric label, String[] params) {
        super(label);

        // Distance
        if(params.length > 0) {
            this.distance = paramAsFloat(params[0], 1);
        }

        // Intensity
        if(params.length > 1) {
            this.intensity = paramAsFloat(params[1], 1);
        }

        // Elastic
        if(params.length > 2) {
            this.elastic = paramAsBoolean(params[2]);
        }
    }

    @Override
    protected void onApply(TypingGlyph glyph, int localIndex, float delta) {
        // Calculate real intensity
        float realIntensity = intensity * (elastic ? 3f : 1f) * DEFAULT_INTENSITY;

        // Calculate progress
        float timePassed = timePassedByGlyphIndex.getAndIncrement(localIndex, 0, delta);
        float progress = MathUtils.clamp(timePassed / realIntensity, 0, 1);

        // Calculate offset
        Interpolation interpolation = elastic ? Interpolation.swingOut : Interpolation.sine;
        float interpolatedValue = interpolation.apply(1, 0, progress);
        float x = getLineHeight() * distance * interpolatedValue * DEFAULT_DISTANCE;

        // Apply changes
        glyph.xoffset += x;
    }

}
