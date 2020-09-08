package io.github._7isenko.aegis.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that used to collect and execute any commands
 */
public class CommandInvoker {
    private static CommandInvoker instance;
    private final Map<String, Command> commands;

    private CommandInvoker() {
        commands = new HashMap<>();
    }

    public static CommandInvoker getInstance() {
        if (instance == null)
            instance = new CommandInvoker();
        return instance;
    }

    /**
     * Adds a new or replaces an existing one command
     *
     * @param name    - Name of the command
     * @param command - {@link Command} to set by name
     */
    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Tries to add a new command
     *
     * @param name    - Name of the command
     * @param command - {@link Command} to set by name
     * @return true on success and false if command with given name already exists
     */
    public boolean addCommandSafe(String name, Command command) {
        if (commands.get(name) == null) {
            commands.put(name, command);
            return true;
        }
        return false;
    }

    /**
     * Executes a command with given arguments and returns the result message
     *
     * @param name - Name of the command to execute
     * @param args - Passed command arguments
     * @return Result of command execution
     */
    public String execute(@NotNull String name, @Nullable String... args) {
        Command command = commands.get(name);
        if (command != null)
            return command.call(args);
        else return "Неизвестная команда";
    }
}