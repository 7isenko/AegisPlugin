package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class StatsCollector {
    private static StatsCollector instance;
    private List<String> beforeShuffle;
    private List<String> afterShuffle;

    public List<String> getBeforeShuffle() {
        return beforeShuffle;
    }

    public void setBeforeShuffle(List<String> beforeShuffle) {
        this.beforeShuffle = beforeShuffle;
    }

    public List<String> getAfterShuffle() {
        return afterShuffle;
    }

    public void setAfterShuffle(List<String> afterShuffle) {
        this.afterShuffle = afterShuffle;
    }

    private StatsCollector() {
    }

    public static StatsCollector getInstance() {
        if (instance == null)
            instance = new StatsCollector();
        return instance;
    }

    public String showBefore() {
        StringBuilder sb = new StringBuilder();
        sb.append("Число участников: ").append(beforeShuffle.size()).append("\nСписок людей в их порядке до шаффла:");
        for (int i = 0; i < beforeShuffle.size(); i++) {
            sb.append("\n").append((i + 1)).append(". ").append(beforeShuffle);
        }
        return sb.toString();
    }

    public String showAfter() {
        StringBuilder sb = new StringBuilder();
        sb.append("Список людей в их порядке после шаффла: ");
        for (int i = 0; i < afterShuffle.size(); i++) {
            sb.append("\n").append((i + 1)).append(". ").append(afterShuffle);
        }
        return sb.toString();
    }
}
