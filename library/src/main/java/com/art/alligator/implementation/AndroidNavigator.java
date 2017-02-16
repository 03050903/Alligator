package com.art.alligator.implementation;

import java.util.LinkedList;
import java.util.Queue;

import com.art.alligator.NavigationContext;
import com.art.alligator.NavigationContextBinder;
import com.art.alligator.NavigationFactory;
import com.art.alligator.Navigator;
import com.art.alligator.Screen;
import com.art.alligator.implementation.commands.*;

/**
 * Date: 29.12.2016
 * Time: 10:13
 *
 * @author Artur Artikov
 */
public class AndroidNavigator implements NavigationContextBinder, Navigator {
	private NavigationFactory mNavigationFactory;
	private NavigationContext mNavigationContext;
	private Queue<Command> mPendingCommands = new LinkedList<>();
	private boolean mCanExecuteCommands;

	public AndroidNavigator(NavigationFactory navigationFactory) {
		mNavigationFactory = navigationFactory;
	}

	@Override
	public void bind(NavigationContext navigationContext) {
		mNavigationContext = navigationContext;
		mCanExecuteCommands = true;
		executePendingCommands();
	}

	@Override
	public void unbind() {
		mNavigationContext = null;
		mCanExecuteCommands = false;
	}

	@Override
	public void goForward(Screen screen) {
		executeCommand(new ForwardCommand(screen));
	}

	@Override
	public void goBack() {
		executeCommand(new BackCommand());
	}

	@Override
	public void goBackTo(Class<? extends Screen> screenClass) {
		executeCommand(new BackToCommand(screenClass));
	}

	@Override
	public void replace(Screen screen) {
		executeCommand(new ReplaceCommand(screen));
	}

	@Override
	public void resetTo(Screen screen) {
		executeCommand(new ResetToCommand(screen));
	}

	@Override
	public void finish() {
		executeCommand(new FinishCommand());
	}

	@Override
	public void switchTo(String screenName) {
		executeCommand(new SwitchToCommand(screenName));
	}

	private void executeCommand(Command command) {
		if (mCanExecuteCommands) {
			mCanExecuteCommands = command.execute(mNavigationContext, mNavigationFactory);
		} else {
			mPendingCommands.add(command);
		}
	}

	private void executePendingCommands() {
		while (mCanExecuteCommands && !mPendingCommands.isEmpty()) {
			Command command = mPendingCommands.remove();
			mCanExecuteCommands = command.execute(mNavigationContext, mNavigationFactory);
		}
	}
}
