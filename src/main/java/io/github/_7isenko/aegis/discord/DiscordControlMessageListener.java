package io.github._7isenko.aegis.discord;

import io.github._7isenko.aegis.Aegis;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

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
                    if (!dm.isStarted()) dm.start();
                    logResult("Бот запущен");
                    break;
                case "!stop":
                    dm.stop();
                    logResult("Бот выключен, вайтлист очищен. Роли сейчас заберу");
                    break;
                case "!kick":
                    if (dm.isAllowKick()) {
                        dm.kickWithoutRoles();
                        logResult("Массовый безрольный кик запущен");
                    } else logResult("Кик выключен");
                    break;
                case "!add":
                    try {
                        int num = Integer.parseInt(fullCommand[1]);
                        if (!dm.isStarted()) dm.start();
                        dm.setChosenRoles(num);
                        logResult("Запущен процесс добавления " + num + " игроков");
                    } catch (Exception e) {
                        logResult("Вылезла ошибка: " + e.getMessage());
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        logResult("```" + sw.toString() + "```");
                    }
                    break;
                case "!help":
                    logResult("!start - Запуск простого вайтлист-бота (включать).\n" +
                            "!add <число> - дает роль \"избранного\" <числу> людей, оставивших реакцию.\n" +
                            "!stop - бот стопается.\n" +
                            "!kick - кикает из дискорда всех челиков без ролей.\n" +
                            "!link <число> - дает ссылочку для приглашения нужного числа людей.\n" +
                            "!link remove - насильно стопает ссылочку\n" +
                            "!fakeadd <число>/re/set/clear - подбор подходящего списка участников");
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
