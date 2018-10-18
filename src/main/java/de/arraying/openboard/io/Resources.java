package de.arraying.openboard.io;

import de.arraying.openboard.OpenBoard;

import java.io.*;

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
public enum Resources {

    /**
     * The default scoreboard file content.
     */
    DEFAULT(OpenBoard.FILE_SCOREBOARD_DEFAULT),

    /**
     * The default events file content.
     */
    EVENTS(OpenBoard.FILE_EVENTS),

    /**
     * The default nametags file content.
     */
    NAMETAGS(OpenBoard.FILE_NAMETAGS),

    /**
     * The splash text.
     */
    SPLASH("splash.txt");

    private final String path;

    /**
     * Creates a new resource.
     * @param path The path.
     */
    Resources(String path) {
        this.path = path;
    }

    /**
     * Gets the resource.
     * @param orElse An alternative if it was not found.
     * @return The resource.
     */
    public String get(String orElse) {
        try(InputStream stream = OpenBoard.class.getResourceAsStream("/" + path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch(IOException exception) {
            exception.printStackTrace();
            return orElse;
        }
    }

}
