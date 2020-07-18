package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class StatsCollector {
    private static StatsCollector instance;
    private List<User> beforeShuffle;
    private List<User> afterShuffle;

    public List<User> getBeforeShuffle() {
        return beforeShuffle;
    }

    public void setBeforeShuffle(List<User> beforeShuffle) {
        this.beforeShuffle = beforeShuffle;
    }

    public List<User> getAfterShuffle() {
        return afterShuffle;
    }

    public void setAfterShuffle(List<User> afterShuffle) {
        this.afterShuffle = afterShuffle;
    }

    private StatsCollector() {
    }

    public static StatsCollector getInstance() {
        if (instance == null)
            instance = new StatsCollector();
        return instance;
    }

    public String showResults() {
        StringBuffer sb = new StringBuffer();
        sb.append("Число участников: ").append(beforeShuffle.size()).append("\nСписок людей в их порядке до шаффла:");
        for (int i = 0; i < beforeShuffle.size(); i++) {
            sb.append("\n").append((i + 1)).append(". ").append(beforeShuffle.get(i).getAsTag());
        }
        sb.append("\nСписок людей в их порядке после шаффла: ");
        for (int i = 0; i < afterShuffle.size(); i++) {
            sb.append("\n").append((i + 1)).append(". ").append(afterShuffle.get(i).getAsTag());
        }
        return sb.toString();
    }
}
