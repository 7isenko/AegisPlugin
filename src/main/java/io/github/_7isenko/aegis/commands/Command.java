package io.github._7isenko.aegis.commands;

import org.jetbrains.annotations.Nullable;

/**
 * Does what you want
 */
public interface Command {
    /**
     * Executes by {@link CommandInvoker} with given arguments and returns the result message
     *
     * @param args - command arguments
     * @return Result message
     */
    String call(@Nullable String... args);
//
//    /**
//     * @return Command description for the help message
//     */
//    String getDescription();
}
