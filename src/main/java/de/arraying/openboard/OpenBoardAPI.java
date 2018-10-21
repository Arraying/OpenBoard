package de.arraying.openboard;

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
@SuppressWarnings("ALL")
public final class OpenBoardAPI {

    /**
     * Gets the nametag name of a player.
     * @param player The player.
     * @return The nametag, or null if the player does not have a nametag.
     */
    public static String getNametag(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("player is null");
        }
        return OpenBoard.getInstance().getNametagManager().getEffectiveNametagName(player);
    }

    /**
     * Refreshes all nametags.
     */
    public static void refreshNametags() {
        OpenBoard.getInstance().getNametagManager().refreshForAll();
    }

    /**
     * Refreshes the nametags of the other users for a particular player.
     * @param player The player.
     */
    public static void refreshNametag(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("player is null");
        }
        OpenBoard.getInstance().getNametagManager().display(player);
    }

    /**
     * Sets the scoreboard for a specific player.
     * This will force the board even if the player does not have permission.
     * @param player The player.
     * @param scoreboard The scoreboard name, without file extension.
     */
    public static void setScoreboard(Player player, String scoreboard) {
        if(player == null) {
            throw new IllegalArgumentException("player is null");
        }
        if(scoreboard != null) {
            scoreboard = scoreboard.toLowerCase();
        }
        OpenBoard.getInstance().getSboardManager().apply(player, scoreboard, true);
    }

    /**
     * Resets the scoreboard for a specific player.
     * @param player The player.
     */
    public static void resetScoreboard(Player player) {
        setScoreboard(player, null);
    }

}
