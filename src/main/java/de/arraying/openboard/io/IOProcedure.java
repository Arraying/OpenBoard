package de.arraying.openboard.io;

import de.arraying.openboard.OpenBoard;
import de.arraying.openboard.procedure.Procedure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
public final class IOProcedure implements Procedure {

    /**
     * Readies all the files.
     * @return False if there is an IO error.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean launch() {
        File root = OpenBoard.getInstance().getDataFolder();
        File scoreboards = new File(root, OpenBoard.DIR_SCOREBOARDS);
        root.mkdirs();
        scoreboards.mkdirs();
        File events = new File(root, OpenBoard.FILE_EVENTS);
        File nametags = new File(root, OpenBoard.FILE_NAMETAGS);
        File scoreboardDefault = new File(scoreboards, OpenBoard.FILE_SCOREBOARD_DEFAULT);
        try {
            if(events.createNewFile()) {
                Files.write(events.toPath(), Resources.EVENTS.get("").getBytes());
            }
            if(nametags.createNewFile()) {
                Files.write(nametags.toPath(), Resources.NAMETAGS.get("").getBytes());
            }
            if(scoreboardDefault.createNewFile()) {
                Files.write(scoreboardDefault.toPath(), Resources.DEFAULT.get("").getBytes());
            }
        } catch(IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

}
