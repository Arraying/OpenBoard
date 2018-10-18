package de.arraying.openboard.tags;

import de.arraying.openboard.block.BlockParser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
public final class NametagManager {

    private final List<Nametag> nametags = new LinkedList<>();
    private int refreshTask;

    /**
     * Registers all nametags.
     */
    public void registerNametags() {
        nametags.clear();
        Bukkit.getScheduler().cancelTask(refreshTask);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(getInstance().getDataFolder(), FILE_NAMETAGS));
        new BlockParser(configuration)
                .getList(CONFIG_NAMETAGS_TAGS)
                .forEach(data -> {
                    Nametag nametag = new Nametag(data.getName(),
                            data.retrieve(CONFIG_NAMETAGS_PREFIX, "").toString(),
                            data.retrieve(CONFIG_NAMETAGS_SUFFIX, "").toString());
                    nametags.add(nametag);
                    getInstance().log("Registered nametag '%s'.", nametag.getName());
                });
        int refreshRate = configuration.getInt(CONFIG_NAMETAGS_REFRESH, 100);
        getInstance().log("Refreshing nametags every %s ticks.", String.valueOf(refreshRate));
        refreshTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(),
                this::refreshForAll,
                refreshRate,
                refreshRate);
    }

    /**
     * Refreshes the nametags for all players.
     */
    public void refreshForAll() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            display(player);
        }
    }

    /**
     * Displays the nametag of everyone to a player.
     * @param player The player to display the nametags to.
     */
    public void display(Player player) {
        Nametag selfNametag = getEffectiveNametag(player);
        for(Player online : Bukkit.getOnlinePlayers()) {
            Nametag otherNametag = getEffectiveNametag(online);
            if(otherNametag != null) {
                otherNametag.add(player.getScoreboard(), online);
            } else {
                purge(player.getScoreboard(), online);
            }
            if(selfNametag != null) {
                selfNametag.add(online.getScoreboard(), player);
            } else {
                purge(online.getScoreboard(), player);
            }
        }
    }

    /**
     * Gets the effective nametag name.
     * @param player The player.
     * @return The nametag name, potentially null.
     */
    public String getEffectiveNametagName(Player player) {
        Nametag nametag = getEffectiveNametag(player);
        return nametag == null ? null : nametag.getName();
    }

    /**
     * Gets the effective nametag for a player.
     * This will return the highest nametag possible, or null.
     * @param player The player.
     * @return The nametag or null.
     */
    private Nametag getEffectiveNametag(Player player) {
        for(Nametag nametag : nametags) {
            if(player.hasPermission(nametag.getPermission())) {
                return nametag;
            }
        }
        return null;
    }

    /**
     * Purges a nametag for a player.
     * @param scoreboard The scoreboard.
     * @param player The player.
     */
    private void purge(Scoreboard scoreboard, Player player) {
        scoreboard.getTeams().stream()
                .filter(it -> it.hasEntry(player.getName()))
                .forEach(it -> it.removeEntry(player.getName()));
    }

}
