package de.arraying.openboard.block;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

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
public final class Block {

    private final String name;
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Creates a new block.
     * @param name The name of the block.
     */
    private Block(String name) {
        this.name = name;
    }

    /**
     * Creates a new block from data.
     * @param configuration The configuration.
     * @param name The name of the section.
     * @param base The base path so far. Includes a trailing ".", null for root.
     * @return A block.
     */
    public static Block of(FileConfiguration configuration, String name, String base) {
        Block block = new Block(name);
        for(String data : configuration.getConfigurationSection(base.substring(0, base.length() - 1)).getKeys(false)) {
            block.set(data.toLowerCase(), configuration.get(String.format("%s.%s", base, data)));
        }
        return block;
    }

    /**
     * Gets the name of the section.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets an object by key.
     * @param key The key.
     * @param fallback The fallback, default value.
     * @return The value.
     */
    public Object retrieve(String key, Object fallback) {
        return data.getOrDefault(key.toLowerCase(), fallback);
    }

    /**
     * Sets a key to a value.
     * @param key The key.
     * @param value The value.
     */
    private void set(String key, Object value) {
        data.put(key, value);
    }

}
