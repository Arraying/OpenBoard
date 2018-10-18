package de.arraying.openboard.board;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static de.arraying.openboard.OpenBoard.*;

/**
 * Copyright 2018 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
final class Sboard {
    
    private final Set<Integer> runnables = new HashSet<>();
    private final Scoreboard scoreboard;

    /**
     * Creates a new scoreboard.
     * This will first create the scoreboard itself through Bukkit.
     * Then, it will get all entries (title + lines) and group them by interval.
     * After that, each interval has a scheduler assigned that will update them accordingly.
     * @param player The player.
     * @param template The template.
     */
    Sboard(Player player, SboardTemplate template) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(getEffectiveName(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        template.getTasks(objective, player).stream()
                .collect(Collectors.groupingBy(it -> it.entry.refresh))
                .forEach((k, v) ->
                        runnables.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(),
                            () -> {
                                for(Task task : v) {
                                    task.execute(task.nextValue());
                                }
                            },
                            0,
                            k)
                ));
    }

    /**
     * Gets the scoreboard.
     * @return The scoreboard.
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * Destroys the scoreboard.
     */
    void destroy() {
        for(int runnable : runnables) {
            Bukkit.getScheduler().cancelTask(runnable);
        }
    }

    /**
     * Gets the effective name.
     * @return The effective name.
     */
    private String getEffectiveName() {
        return ID_PREFIX_OBJECTIVE + getInstance().getSboardManager().newId();
    }

    /**
     * Entry.
     */
    static final class Entry {

        private final List<String> values;
        private final int refresh;
        private final boolean random;
        private int id;

        /**
         * Creates a new entry.
         * @param values The values.
         * @param refresh The refresh rate, in ticks.
         * @param random Whether or not the entries should be random.
         */
        Entry(List<String> values, int refresh, boolean random) {
            this.values = values;
            this.refresh = refresh;
            this.random = random;
        }

        /**
         * Gets the values.
         * @return The values.
         */
        List<String> getValues() {
            return values;
        }

        /**
         * Gets the refresh.
         * @return The refresh rate.
         */
        int getRefresh() {
            return refresh;
        }

        /**
         * Sets the ID.
         * @param id The ID.
         */
        void setId(int id) {
            this.id = id;
        }

    }

    /**
     * The task that will handle the updating.
     */
    static abstract class Task {

        final Objective objective;
        final Entry entry;
        private int index;

        /**
         * Creates a new task.
         * @param objective The objective.
         * @param entry The entry.
         */
        Task(Objective objective, Entry entry) {
            this.objective = objective;
            this.entry = entry;
        }

        /**
         * Executes the task update.
         * @param value The value to use.
         */
        abstract void execute(String value);

        /**
         * Gets the next value.
         * @return The next value.
         */
        final String nextValue() {
            if(entry.random) {
                return entry.values.get(ThreadLocalRandom.current().nextInt(0, entry.values.size()));
            }
            index++;
            if(index >= entry.values.size()) {
                index = 0;
            }
            return entry.values.get(index);
        }

    }

    /**
     * The task that updates the scoreboard title.
     */
    static final class TitleTask extends Task {
        
        /**
         * Creates a new task.
         * @param objective The objective.
         * @param entry The entry.
         */
        TitleTask(Objective objective, Entry entry) {
            super(objective, entry);
        }

        /**
         * Updates the scoreboard title.
         * @param value The value to use.
         */
        @Override
        void execute(String value) {
            objective.setDisplayName(getInstance().getSboardParser().parse(null, value));
        }

    }

    /**
     * The task that will update the scoreboard lines.
     */
    static final class LineTask extends Task {

        private final Player player;
        private String previous;

        /**
         * Creates a new task.
         * @param objective The objective.
         * @param entry The entry.
         * @param player The player.
         */
        LineTask(Objective objective, Entry entry, Player player) {
            super(objective, entry);
            this.player = player;
        }

        /**
         * Updates the scoreboard lines.
         * @param value The value to use.
         */
        @Override
        void execute(String value) {
            if(previous != null) {
                objective.getScoreboard().resetScores(previous);
            }
            previous = getInstance().getSboardParser().parse(player, value);
            while(objective.getScoreboard().getEntries().contains(previous)) {
                previous += ChatColor.RESET;
            }
            objective.getScore(previous).setScore(entry.id);
        }

    }

}
