package de.arraying.openboard.board;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
public final class SboardParser {

    private boolean hasPAPI;

    /**
     * Initializes the plugin.
     */
    public void initialize() {
        hasPAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Parses a value.
     * @param player The player, nullable. Only used for placeholders.
     * @param value A value.
     * @return The value.
     */
    String parse(Player player, String value) {
        value = ChatColor.translateAlternateColorCodes('&', value);
        if(player != null
                && hasPAPI) {
            value = PlaceholderAPI.setPlaceholders(player, value);
        }
        return value;
    }

}
