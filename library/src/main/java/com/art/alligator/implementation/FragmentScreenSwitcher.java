package com.art.alligator.implementation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.art.alligator.ScreenSwitcher;
import com.art.alligator.TransitionAnimation;

/**
 * Date: 01/30/2016
 * Time: 10:13
 *
 * @author Artur Artikov
 */
public abstract class FragmentScreenSwitcher implements ScreenSwitcher {
	private FragmentManager mFragmentManager;
	private int mContainerId;
	private Fragment mCurrentFragment;

	public FragmentScreenSwitcher(FragmentManager fragmentManager, int containerId) {
		mFragmentManager = fragmentManager;
		mContainerId = containerId;
		mCurrentFragment = mFragmentManager.findFragmentById(mContainerId);
	}

	protected abstract Fragment createFragment(String screenName);

	protected void onScreenSwitched(String screenName) {
	}

	protected TransitionAnimation getAnimation(String screenNameFrom, String screenNameTo) {
		return TransitionAnimation.NONE;
	}

	@Override
	public boolean switchTo(String screenName) {
		Fragment newFragment = mFragmentManager.findFragmentByTag(screenName);
		if (newFragment == null) {
			newFragment = createFragment(screenName);
			if(newFragment == null) {
				return false;
			}

			mFragmentManager.beginTransaction()
					.add(mContainerId, newFragment, screenName)
					.detach(newFragment)
					.commitNow();
		}

		if(mCurrentFragment == newFragment) {
			return true;
		}

		Fragment previousFragment = mCurrentFragment;
		mCurrentFragment = newFragment;
		onScreenSwitched(screenName);

		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if(previousFragment != null) {
			TransitionAnimation animation = getAnimation(previousFragment.getTag(), screenName);
			if(animation != null && animation != TransitionAnimation.DEFAULT) {
				transaction.setCustomAnimations(animation.getEnterAnimation(), animation.getExitAnimation());
			}
			transaction.detach(previousFragment);
		}

		transaction.attach(newFragment);
		transaction.commitNow();
		return true;
	}

	public Fragment getCurrentFragment() {
		return mCurrentFragment;
	}
}
