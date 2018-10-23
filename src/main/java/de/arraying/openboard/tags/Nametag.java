package de.arraying.openboard.tags;

import de.arraying.openboard.OpenBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
final class Nametag {

    private final String name;
    private final String prefix;
    private final String suffix;

    /**
     * Creates a new nametag.
     * @param name The name.
     * @param prefix The prefix.
     * @param suffix The suffix.
     */
    Nametag(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = ChatColor.translateAlternateColorCodes('&',
                prefix.length() > MAX_TEAM ? prefix.substring(0, MAX_TEAM + 1) : prefix);
        this.suffix = ChatColor.translateAlternateColorCodes('&',
                suffix.length() > MAX_TEAM ? suffix.substring(0, MAX_TEAM + 1) : suffix);
    }

    /**
     * Gets the name of the nametag.
     * @return The name.
     */
    String getName() {
        return name;
    }

    /**
     * Gets the permission for the tag.
     * @return The permission.
     */
    String getPermission() {
        return PERM_NAMETAG + name;
    }

    /**
     * Adds the target player to the nametag for the specified scoreboard.
     * @param scoreboard The scoreboard.
     * @param target The target.
     */
    void add(Scoreboard scoreboard, Player target) {
        Team team = scoreboard.getTeam(getEffectiveName());
        if(team == null) {
            team = scoreboard.registerNewTeam(getEffectiveName());
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        if(Bukkit.getVersion().contains("1.13")) {
            team.setColor(fromPrefix(prefix));
        }
        team.addEntry(target.getName());
    }

    /**
     * Gets the effective team name.
     * @return The effective team name.
     */
    private String getEffectiveName() {
        return OpenBoard.ID_PREFIX_NAMETAG + name;
    }


    /**
     * Gets the last chat/formatting code from the prefix.
     * @param prefix The prefix.
     * @return The chat colour.
     */
    private ChatColor fromPrefix(String prefix) {
        char colour = 0;
        char[] chars = prefix.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            char at = chars[i];
            if((at == '§'
                    || at == '&')
                    && i + 1 < chars.length) {
                char code = chars[i + 1];
                if(ChatColor.getByChar(code) != null) {
                    colour = code;
                }
            }
        }
        return colour == 0 ? ChatColor.RESET : ChatColor.getByChar(colour);
    }


}
