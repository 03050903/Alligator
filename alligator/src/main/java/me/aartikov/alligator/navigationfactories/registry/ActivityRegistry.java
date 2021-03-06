package me.aartikov.alligator.navigationfactories.registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import me.aartikov.alligator.Screen;
import me.aartikov.alligator.functions.Function;
import me.aartikov.alligator.functions.Function2;

/**
 * Date: 25.03.2017
 * Time: 16:52
 *
 * @author Artur Artikov
 */

/**
 * Registry for screens represented by activities.
 */
public class ActivityRegistry {
	private Map<Class<? extends Screen>, Element> mElements = new LinkedHashMap<>();

	public <ScreenT extends Screen> void register(Class<ScreenT> screenClass, Class<? extends Activity> activityClass,
	                                              Function2<Context, ScreenT, Intent> intentCreationFunction, Function<Intent, ScreenT> screenGettingFunction) {
		checkThatNotRegistered(screenClass);
		mElements.put(screenClass, new Element(activityClass, intentCreationFunction, screenGettingFunction));
	}

	public boolean isRegistered(Class<? extends Screen> screenClass) {
		return mElements.containsKey(screenClass);
	}

	public Class<? extends Activity> getActivityClass(Class<? extends Screen> screenClass) {
		checkThatRegistered(screenClass);
		Element element = mElements.get(screenClass);
		return element.getActivityClass();
	}

	@SuppressWarnings("unchecked")
	public Intent createActivityIntent(Context context, Screen screen) {
		checkThatRegistered(screen.getClass());
		Element element = mElements.get(screen.getClass());
		return ((Function2<Context, Screen, Intent>) element.getIntentCreationFunction()).call(context, screen);
	}

	public <ScreenT extends Screen> ScreenT getScreen(Activity activity, Class<ScreenT> screenClass) {
		checkThatRegistered(screenClass);
		Element element = mElements.get(screenClass);
		return (ScreenT) element.getScreenGettingFunction().call(activity.getIntent());
	}

	public Set<Class<? extends Screen>> getScreenClasses() {
		return mElements.keySet();
	}

	private void checkThatNotRegistered(Class<? extends Screen> screenClass) {
		if (isRegistered(screenClass)) {
			throw new IllegalArgumentException("Screen " + screenClass.getSimpleName() + " is is already registered.");
		}
	}

	private void checkThatRegistered(Class<? extends Screen> screenClass) {
		if (!isRegistered(screenClass)) {
			throw new IllegalArgumentException("Screen " + screenClass.getSimpleName() + " is not represented by an activity.");
		}
	}

	private static class Element {
		private Class<? extends Activity> mActivityClass;
		private Function2<Context, ? extends Screen, Intent> mIntentCreationFunction;
		private Function<Intent, ? extends Screen> mScreenGettingFunction;

		Element(Class<? extends Activity> activityClass, Function2<Context, ? extends Screen, Intent> intentCreationFunction, Function<Intent, ? extends Screen> screenGettingFunction) {
			mActivityClass = activityClass;
			mIntentCreationFunction = intentCreationFunction;
			mScreenGettingFunction = screenGettingFunction;
		}

		Class<? extends Activity> getActivityClass() {
			return mActivityClass;
		}

		Function2<Context, ? extends Screen, Intent> getIntentCreationFunction() {
			return mIntentCreationFunction;
		}

		Function<Intent, ? extends Screen> getScreenGettingFunction() {
			return mScreenGettingFunction;
		}
	}
}
