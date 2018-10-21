package de.arraying.openboard.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
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
public final class EventListener implements Listener {

    private final List<EventAction> actions;

    /**
     * Creates a new event listener.
     * @param actions A set of event actions.
     */
    public EventListener(List<EventAction> actions) {
        this.actions = actions;
    }

    /**
     * When a plugin is loaded.
     * This is used so that the classloader has had a chance to load all plugins.
     * They need to be loaded to avoid issues with alphabetical
     * @param event The event.
     */
    @org.bukkit.event.EventHandler
    public void onPluginLoad(PluginEnableEvent event) {
        if(Arrays.stream(Bukkit.getPluginManager().getPlugins()).allMatch(Plugin::isEnabled)) {
            for(EventAction action : actions) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends PlayerEvent> eventClass = (Class<? extends PlayerEvent>) Class.forName(action.getName()
                            .replace(" ", "."));
                    Bukkit.getPluginManager().registerEvent(eventClass,
                            this,
                            EventPriority.MONITOR,
                            new EventHandler(action),
                            getInstance());
                    getInstance().log("Listening to event %s.", eventClass.getSimpleName());
                } catch(Exception exception) {
                    getInstance().log("Unable to listen to event %s: (%s) %s", action.getName(), exception.getClass().getName(), exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * When a player joins.
     * @param event The event.
     */
    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getInstance().getSboardManager().apply(player, DEFAULT_SCOREBOARD, false);
        getInstance().getNametagManager().display(player);
    }

    /**
     * When the player leaves.
     * @param event The event.
     */
    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        getInstance().getSboardManager().unapply(event.getPlayer());
    }

}
