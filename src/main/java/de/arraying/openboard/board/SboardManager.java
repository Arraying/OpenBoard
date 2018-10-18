package de.arraying.openboard.board;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
public final class SboardManager {

    private final Map<String, SboardTemplate> templates = new HashMap<>();
    private final Map<Player, Sboard> boards = new HashMap<>();
    private final AtomicInteger integer = new AtomicInteger(0);

    /**
     * Registers scoreboards.
     */
    public synchronized void registerScoreboards() {
        templates.clear();
        for(File file : Objects.requireNonNull(new File(getInstance().getDataFolder(), DIR_SCOREBOARDS)
                .listFiles((dir, name) -> name.toLowerCase().endsWith(".yml")))) {
            String name = file.getName().toLowerCase();
            name = name.substring(0, name.lastIndexOf(".")).replaceAll("\\s", "");
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            SboardTemplate template = new SboardTemplate(name, configuration);
            if(!template.isValid()) {
                getInstance().log("Scoreboard '%s' is invalid. Double check the values are defined, and refreshes are > 0.", name);
                continue;
            }
            templates.put(name, template);
            getInstance().log("Registered scoreboard '%s'.", name);
        }
        for(Map.Entry<Player, Sboard> set : this.boards.entrySet()) {
            set.getValue().destroy();
            apply(set.getKey(), null);
            apply(set.getKey(), DEFAULT_SCOREBOARD);
        }
    }

    /**
     * Applies a scoreboard to the player.
     * @param player The player.
     * @param name The name, null to reset.
     */
    public void apply(Player player, String name) {
        if(player == null
                || !player.isOnline()) {
            return;
        }
        Sboard current = boards.get(player);
        if(current != null) {
            current.destroy();
        }
        if(name == null) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            updateNametag(player);
            return;
        }
        SboardTemplate template = templates.get(name);
        if(template == null
                || !template.hasPermission(player)) {
            return;
        }
        Sboard board = template.create(player);
        player.setScoreboard(board.getScoreboard());
        boards.put(player, board);
        updateNametag(player);
    }

    /**
     * Unapplies (destroys) the scoreboard.
     * @param player The player.
     */
    public void unapply(Player player) {
        Sboard sboard = boards.get(player);
        if(sboard != null) {
            sboard.destroy();
            boards.remove(player);
        }
    }

    /**
     * Generates a new ID.
     * @return The ID.
     */
    int newId() {
        return integer.getAndIncrement();
    }

    /**
     * Updates the nametags for a player after a scoreboard change.
     * @param player The player.
     */
    private void updateNametag(Player player) {
        getInstance().getNametagManager().display(player);
    }

}
