package de.arraying.openboard;

import de.arraying.openboard.board.SboardManager;
import de.arraying.openboard.board.SboardParser;
import de.arraying.openboard.io.Resources;
import de.arraying.openboard.procedure.Procedures;
import de.arraying.openboard.tags.NametagManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
@SuppressWarnings("WeakerAccess")
public final class OpenBoard extends JavaPlugin {

    /**
     * The scoreboards directory.
     */
    public static final String DIR_SCOREBOARDS = "scoreboards";

    /**
     * The events configuration file.
     */
    public static final String FILE_EVENTS = "events.yml";

    /**
     * The nametags configuration file.
     */
    public static final String FILE_NAMETAGS = "nametags.yml";

    /**
     * The default scoreboard name.
     */
    public static final String DEFAULT_SCOREBOARD = "default";

    /**
     * The default scoreboard file.
     */
    public static final String FILE_SCOREBOARD_DEFAULT = DEFAULT_SCOREBOARD + ".yml";

    /**
     * The base permission.
     */
    public static final String PERM_BASE = "openboard.";

    /**
     * The base for the scoreboard permission.
     */
    public static final String PERM_SCOREBOARD = PERM_BASE + "board.";

    /**
     * The base for the nametag permission.
     */
    public static final String PERM_NAMETAG = PERM_BASE + "nametag.";

    /**
     * The prefix for nametag teams.
     */
    public static final String ID_PREFIX_NAMETAG = "ob_t_";

    /**
     * The prefix for scoreboard objective names.
     */
    public static final String ID_PREFIX_OBJECTIVE = "ob_o_";

    /**
     * The config key for the scoreboard title.
     */
    public static final String CONFIG_SCOREBOARD_TITLE = "title";

    /**
     * The config key for the scoreboard lines.
     */
    public static final String CONFIG_SCOREBOARD_LINES = "lines";

    /**
     * The config key for the values.
     */
    public static final String CONFIG_SCOREBOARD_VALUES = "values";

    /**
     * The config key for the refresh rate.
     */
    public static final String CONFIG_SCOREBOARD_REFRESH = "refresh";

    /**
     * The config key for the random flag.
     */
    public static final String CONFIG_SCOREBOARD_RANDOM = "random";

    /**
     * the config key for the refresh rate.
     */
    public static final String CONFIG_NAMETAGS_REFRESH = "refresh";

    /**
     * The config key for nametags.
     */
    public static final String CONFIG_NAMETAGS_TAGS = "tags";

    /**
     * The config key for the nametag prefix.
     */
    public static final String CONFIG_NAMETAGS_PREFIX = "prefix";

    /**
     * The config key for the nametag suffix.
     */
    public static final String CONFIG_NAMETAGS_SUFFIX = "suffix";

    /**
     * The config key for the scoreboard to give at an event.
     */
    public static final String CONFIG_EVENT_GIVE = "give";

    /**
     * The config key for the duration a scoreboard will be displayed for at an event.
     */
    public static final String CONFIG_EVENT_FOR = "for";

    /**
     * The config key for the scoreboard to give after an event.
     */
    public static final String CONFIG_EVENT_THEN = "then";

    /**
     * The maximum prefix/suffix length for teams.
     */
    public static final int MAX_TEAM = 16;

    private static OpenBoard instance;
    private final SboardManager sboardManager = new SboardManager();
    private final NametagManager nametagManager = new NametagManager();
    private final SboardParser sboardParser = new SboardParser();

    /**
     * Gets the instance of the plugin.
     * @return The plugin.
     */
    public static OpenBoard getInstance() {
        return instance;
    }

    /**
     * When the plugin enables.
     * This will load up the procedures and then do a few misc. tasks itself.
     */
    @Override
    public void onEnable() {
        instance = this;
        log(Resources.SPLASH.get(""), getDescription().getVersion(), getDescription().getAuthors().get(0));
        for(Procedures procedures : Procedures.values()) {
            if(!procedures.getProcedure().launch()) {
                log("Fatal error loading procedure %s.", procedures.getClass().getName());
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        sboardParser.initialize();
        getCommand("obreload").setExecutor(this);
    }

    /**
     * When the plugin disables.
     * Cancels all of its own tasks so there aren't any tasks going in in the background
     * after a reload has been initiated (even though this is not recommended).
     */
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    /**
     * When a command is executed.
     * @param sender The sender.
     * @param command The command.
     * @param label The label.
     * @param args The arguments.
     * @return True.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(PERM_BASE + "reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
            return true;
        }
        sboardManager.registerScoreboards();
        nametagManager.registerNametags();
        sender.sendMessage(ChatColor.GREEN + "Scoreboards and nametags have been reloaded!");
        return true;
    }

    /**
     * Logs a formatable message.
     * @param message The message.
     * @param format The format.
     */
    public void log(String message, String... format) {
        getLogger().info(String.format(message, (Object[]) format));
    }

    /**
     * Gets the scoreboard manager.
     * @return The scoreboard manager.
     */
    public SboardManager getSboardManager() {
        return sboardManager;
    }

    /**
     * Gets the nametag manager.
     * @return The nametag manager.
     */
    public NametagManager getNametagManager() {
        return nametagManager;
    }

    /**
     * Gets the scoreboard parser.
     * @return The scoreboard parser.
     */
    public SboardParser getSboardParser() {
        return sboardParser;
    }

}