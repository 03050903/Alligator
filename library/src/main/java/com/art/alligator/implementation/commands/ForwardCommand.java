package com.art.alligator.implementation.commands;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.art.alligator.Command;
import com.art.alligator.CommandExecutionException;
import com.art.alligator.NavigationContext;
import com.art.alligator.NavigationFactory;
import com.art.alligator.Screen;
import com.art.alligator.TransitionAnimation;
import com.art.alligator.TransitionType;
import com.art.alligator.implementation.CommandUtils;
import com.art.alligator.implementation.ScreenUtils;

/**
 * Date: 29.12.2016
 * Time: 10:22
 *
 * @author Artur Artikov
 */
public class ForwardCommand implements Command {
	private Screen mScreen;
	private TransitionAnimation mAnimation;

	public ForwardCommand(Screen screen, TransitionAnimation animation) {
		mScreen = screen;
		mAnimation = animation;
	}

	@Override
	public boolean execute(NavigationContext navigationContext, NavigationFactory navigationFactory) throws CommandExecutionException {
		Intent intent = navigationFactory.createActivityIntent(navigationContext.getActivity(), mScreen);
		Fragment fragment = navigationFactory.createFragment(mScreen);

		if (intent != null) {
			Activity activity = navigationContext.getActivity();
			ScreenUtils.putScreenClass(intent, mScreen.getClass());
			ScreenUtils.putPreviousScreenClass(intent, ScreenUtils.getScreenClass(activity, navigationFactory));
			activity.startActivity(intent);
			CommandUtils.applyActivityAnimation(activity, getActivityAnimation(navigationContext, navigationFactory));
			return false;

		} else if (fragment != null) {
			FragmentManager fragmentManager = navigationContext.getFragmentManager();
			if (fragmentManager == null) {
				throw new CommandExecutionException("FragmentManager is not bound.");
			}

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			Fragment currentFragment = CommandUtils.getCurrentFragment(navigationContext);
			if(currentFragment != null) {
				CommandUtils.applyFragmentAnimation(transaction, getFragmentAnimation(navigationContext, currentFragment));
				transaction.detach(currentFragment);
			}

			ScreenUtils.putScreenClass(fragment, mScreen.getClass());
			int index = CommandUtils.getFragmentCount(navigationContext);
			String tag = CommandUtils.getFragmentTag(navigationContext, index);
			transaction.add(navigationContext.getContainerId(), fragment, tag);
			transaction.commitNow();
			return true;

		} else {
			throw new CommandExecutionException("Screen " + mScreen.getClass().getSimpleName() + " is not registered.");
		}
	}

	private TransitionAnimation getActivityAnimation(NavigationContext navigationContext, NavigationFactory navigationFactory) {
		if(mAnimation != null) {
			return mAnimation;
		}

		Class<? extends Screen> screenClassFrom = ScreenUtils.getScreenClass(navigationContext.getActivity(), navigationFactory);
		Class<? extends Screen> screenClassTo = mScreen.getClass();
		return navigationContext.getAnimationProvider().getAnimation(TransitionType.FORWARD, true, screenClassFrom, screenClassTo);
	}

	private TransitionAnimation getFragmentAnimation(NavigationContext navigationContext, Fragment currentFragment) {
		if(mAnimation != null) {
			return mAnimation;
		}

		Class<? extends Screen> screenClassFrom = ScreenUtils.getScreenClass(currentFragment);
		Class<? extends Screen> screenClassTo = mScreen.getClass();
		return navigationContext.getAnimationProvider().getAnimation(TransitionType.FORWARD, false, screenClassFrom, screenClassTo);
	}
}
