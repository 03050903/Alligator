package me.aartikov.alligator.defaultimpementation;

import me.aartikov.alligator.NavigationErrorListener;
import me.aartikov.alligator.exceptions.CommandExecutionException;

/**
 * Date: 12.03.2017
 * Time: 15:16
 *
 * @author Artur Artikov
 */

/**
 * Default implementation of {@link NavigationErrorListener}. Wraps {@link CommandExecutionException} to {@code RuntimeException} and throws it.
 */
public class DefaultNavigationErrorListener implements NavigationErrorListener {
	@Override
	public void onNavigationError(CommandExecutionException e) {
		throw new RuntimeException(e);
	}
}
