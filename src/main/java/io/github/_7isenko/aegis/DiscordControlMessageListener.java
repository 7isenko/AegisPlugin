package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscordControlMessageListener extends ListenerAdapter {
    private DiscordManager dm;
    private final Guild guild;
    private final TextChannel controlChannel;

    public DiscordControlMessageListener(DiscordManager dm) {
        super();
        this.dm = dm;
        this.guild = dm.getGuild();
        this.controlChannel = guild.getTextChannelById(Aegis.config.getString("control_channel_id"));
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        if (!event.getChannel().equals(controlChannel) || event.getMember().getUser().isBot())
            return;

        if (rawMessage.startsWith("!")) {
            String[] fullCommand = rawMessage.split(" ");
            String command = fullCommand[0];
            switch (command) {
                case "!start":
                    dm.start();
                    logResult("Бот запущен");
                    break;
                case "!stop":
                    dm.stop();
                    logResult("Бот выключен, вайтлист очищен. Роли сейчас заберу");
                    break;
                case "!kick":
                    if (Aegis.config.getBoolean("allow_kick")) {
                        dm.kickWithoutRoles();
                        logResult("Массовый безрольный кик запущен");
                    } else logResult("Кик выключен");
                    break;
                case "!emote":
                    dm.start();
                    dm.startEmoteMode();
                    break;
                case "!add":
                    if (dm.isEmoteMode()) {
                        try {
                            int num = Integer.parseInt(fullCommand[1]);
                            dm.setChosenRoles(num);
                            logResult("Запущен процесс добавления " + num + " игроков");
                        } catch (Exception e) {
                            logResult("Ошибочка вышла, !help");
                        }
                    } else logResult("Сначала напиши !emote и подожди");
                    break;
                case "!link": {
                    if (fullCommand.length == 1) {
                        logResult("Я понимаю только !link <число>/status/remove");
                        return;
                    }
                    if (LinkManager.hasInstance()) {
                        try {
                            if (fullCommand[1].equalsIgnoreCase("check") || fullCommand[1].equalsIgnoreCase("status")) {
                                logResult("Статус: " + LinkManager.getInstance().getUses());
                                return;
                            } else if (fullCommand[1].equalsIgnoreCase("remove")) {
                                LinkManager.getInstance().clear();
                                logResult("Я ударил ссылку, и она умерла.");
                                return;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            logResult("Я понимаю только !link <число>/status/remove");
                        }
                    }
                    try {
                        int num = Integer.parseInt(fullCommand[1]);
                        LinkManager lm = LinkManager.setInstance(dm.getAnnounceChannel(), num);
                        logResult("Ссылка на " + num + " человек: " + lm.getLink());
                        new Thread(lm).start();
                    } catch (NumberFormatException e) {
                        logResult("Я понимаю только !link <число>/status/remove");
                    } catch (InsufficientPermissionException e) {
                        logResult("Прав нет, дай права `CREATE_INSTANT_INVITE`");
                    }
                }
                break;
                case "!help":
                    logResult("!start - Запуск простого вайтлист-бота (включать).\n" +
                            "!emote - Запуск крутого бота, работающего через реацию на сообщении. И обычного вайтлист-бота.\n" +
                            "!add <число> - дает роль \"избранного\" <числу> людей, оставивших реакцию.\n" +
                            "!stop - оба бота стопаются.\n" +
                            "!kick - кикает из дискорда всех челиков без ролей.\n" +
                            "!link <число> - дает ссылочку для приглашения нужного числа людей.\n" +
                            "!link check - выводит информацию о ссылочке.\n" +
                            "!link remove - насильно стопает ссылочку\n");
                    break;
                default:
                    logResult("Команда была введена неправильно, чек !help");
            }
        }
    }

    private void logResult(String answer) {
        controlChannel.sendMessage(answer).queue();
    }
}
