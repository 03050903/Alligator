package com.art.navigationsample;

import com.art.alligator.AnimationData;
import com.art.alligator.Screen;
import com.art.alligator.TransitionAnimation;
import com.art.alligator.TransitionAnimationProvider;
import com.art.alligator.TransitionType;
import com.art.alligator.animations.transition.SimpleTransitionAnimation;

/**
 * Date: 25.02.2017
 * Time: 12:00
 *
 * @author Artur Artikov
 */

public class SampleTransitionAnimationProvider implements TransitionAnimationProvider {
	@Override
	public TransitionAnimation getAnimation(TransitionType transitionType, Class<? extends Screen> screenClassFrom, Class<? extends Screen> screenClassTo, boolean isActivity, AnimationData animationData) {
		switch (transitionType) {
			case FORWARD:
				return new SimpleTransitionAnimation(R.anim.slide_in_right, R.anim.slide_out_left);
			case BACK:
				return new SimpleTransitionAnimation(R.anim.slide_in_left, R.anim.slide_out_right);
			case REPLACE:
			case RESET:
				return new SimpleTransitionAnimation(R.anim.stay, R.anim.fade_out);
			default:
				return TransitionAnimation.DEFAULT;
		}
	}
}
