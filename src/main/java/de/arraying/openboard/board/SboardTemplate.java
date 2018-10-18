package de.arraying.openboard.board;

import de.arraying.openboard.block.Block;
import de.arraying.openboard.block.BlockParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
final class SboardTemplate {

    private final String name;
    private final Sboard.Entry title;
    private final List<Sboard.Entry> lines = new ArrayList<>();

    /**
     * Creates a new scoreboard template.
     * @param name The name.
     * @param fileConfiguration The file configuration.
     */
    SboardTemplate(String name, FileConfiguration fileConfiguration) {
        this.name = name;
        BlockParser parser = new BlockParser(fileConfiguration);
        Block titleBlock = parser.getIndividual(CONFIG_SCOREBOARD_TITLE);
        this.title = blockToEntry(titleBlock);
        parser.getList(CONFIG_SCOREBOARD_LINES)
                .forEach(it -> lines.add(blockToEntry(it)));
        int i = lines.size();
        for(Sboard.Entry entry : lines) {
            entry.setId(i--);
        }
    }

    /**
     * Creates the scoreboard for the player.
     * @param player The player.
     * @return The scoreboard.
     */
    Sboard create(Player player) {
        return new Sboard(player, this);
    }

    /**
     * Converts the scoreboard to a set of tasks for the player.
     * @param objective The objective.
     * @param player The player.
     * @return A set of tasks.
     */
    Set<Sboard.Task> getTasks(Objective objective, Player player) {
        Set<Sboard.Task> tasks = new HashSet<>();
        tasks.add(new Sboard.TitleTask(objective, title));
        tasks.addAll(lines.stream()
                .map(it -> new Sboard.LineTask(objective, it, player))
                .collect(Collectors.toSet()));
        return tasks;
    }

    /**
     * Whether or not the template is valid.
     * @return True if it is, false otherwise.
     */
    boolean isValid() {
        return !title.getValues().isEmpty() && title.getRefresh() > 0
                && lines.stream()
                    .allMatch(it -> !it.getValues().isEmpty() && it.getRefresh() > 0);
    }

    /**
     * Whether or not the player has permission for the board.
     * @param player The player.
     * @return The board.
     */
    boolean hasPermission(Player player) {
        return player.hasPermission(PERM_SCOREBOARD + name);
    }

    /**
     * Converts the block to an entry.
     * @param block The block.
     * @return The entry.
     */
    @SuppressWarnings("unchecked")
    private Sboard.Entry blockToEntry(Block block) {
        return new Sboard.Entry((List<String>) block.retrieve(CONFIG_SCOREBOARD_VALUES, new ArrayList<String>()),
                (int) block.retrieve(CONFIG_SCOREBOARD_REFRESH, 100),
                (boolean) block.retrieve(CONFIG_SCOREBOARD_RANDOM, false));
    }

}
