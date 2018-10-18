package de.arraying.openboard.event;

import de.arraying.openboard.block.Block;

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
public final class EventAction {

    private final String name;
    private final String targetScoreboard;
    private final int targetDuration;
    private final String completedScoreboard;

    /**
     * Creates a new event action from a block.
     * @param block The block.
     */
    public EventAction(Block block) {
        this.name = block.getName();
        this.targetScoreboard = block.retrieve(CONFIG_EVENT_GIVE, DEFAULT_SCOREBOARD).toString();
        this.targetDuration = (int) block.retrieve(CONFIG_EVENT_FOR, 100);
        this.completedScoreboard = block.retrieve(CONFIG_EVENT_THEN, DEFAULT_SCOREBOARD).toString();
    }

    /**
     * Gets the target name.
     * @return The name.
     */
    String getName() {
        return name;
    }

    /**
     * Gets the target scoreboard.
     * @return The target scoreboard.
     */
    String getTargetScoreboard() {
        return targetScoreboard;
    }

    /**
     * Gets the target duration.
     * @return The target duration.
     */
    int getTargetDuration() {
        return targetDuration;
    }

    /**
     * Gets the completed scoreboard.
     * @return The target scoreboard.
     */
    String getCompletedScoreboard() {
        return completedScoreboard;
    }
    
}
