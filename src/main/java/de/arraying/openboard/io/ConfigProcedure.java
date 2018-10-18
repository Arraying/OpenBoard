package de.arraying.openboard.io;

import de.arraying.openboard.OpenBoard;
import de.arraying.openboard.block.BlockParser;
import de.arraying.openboard.event.EventAction;
import de.arraying.openboard.event.EventListener;
import de.arraying.openboard.procedure.Procedure;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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
public final class ConfigProcedure implements Procedure {

    /**
     * Loads all the configured data into the cache.
     * @return False if an error occurs.
     */
    @Override
    public boolean launch() {
        OpenBoard openBoard = OpenBoard.getInstance();
        openBoard.getSboardManager().registerScoreboards();
        openBoard.getNametagManager().registerNametags();
        File events = new File(OpenBoard.getInstance().getDataFolder(), OpenBoard.FILE_EVENTS);
        FileConfiguration eventsConfiguration = YamlConfiguration.loadConfiguration(events);
        BlockParser blockParser = new BlockParser(eventsConfiguration);
        List<EventAction> action = blockParser.getList(null).stream()
                .map(EventAction::new)
                .collect(Collectors.toList());
        Bukkit.getPluginManager().registerEvents(new EventListener(action), OpenBoard.getInstance());
        return true;
    }

}
