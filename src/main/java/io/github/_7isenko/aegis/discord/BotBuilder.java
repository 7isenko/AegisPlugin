package io.github._7isenko.aegis.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class BotBuilder {
    public static JDA build(String token) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setActivity(Activity.listening("чат"));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setRelativeRateLimit(false); // Если будут проблемы с временными ограничениями на API, то поставить true или сделать NTP-синхронизацию

        JDA jda = builder.build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            jda.shutdown();
            return build(token);
        }
        return jda;
    }
}
