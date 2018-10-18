package de.arraying.openboard.event;

import de.arraying.openboard.OpenBoard;
import de.arraying.openboard.OpenBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

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
final class EventHandler implements EventExecutor {

    private final EventAction eventAction;

    /**
     * Creates a new event action.
     * @param eventAction The event action.
     */
    EventHandler(EventAction eventAction) {
        this.eventAction = eventAction;
    }

    /**
     * Executes the event.
     * This uses the API as it deals with user input.
     * @param listener The listener.
     * @param event The event.
     */
    @Override
    public void execute(Listener listener, Event event) {
        Player player = ((PlayerEvent) event).getPlayer();
        OpenBoardAPI.setScoreboard(player, eventAction.getTargetScoreboard());
        if(eventAction.getTargetDuration() > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(OpenBoard.getInstance(),
                    () -> OpenBoardAPI.setScoreboard(player, eventAction.getCompletedScoreboard()),
                    eventAction.getTargetDuration());
        }
    }

}
