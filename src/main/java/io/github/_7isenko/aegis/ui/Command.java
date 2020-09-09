package io.github._7isenko.aegis.ui;

import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Does what you want
 */
@ThreadSafe
public interface Command {
    /**
     * Executes by {@link CommandInvoker} with given arguments and returns the result message
     *
     * @param args - command arguments
     * @return Result message or null if it is not needed
     */
    String call(String... args);
//
//    /**
//     * @return Command description for the help message
//     */
//    String getDescription();
}
