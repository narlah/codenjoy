package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component("codeSaver")
public class CodeSaver {

    private static final Logger logger = LoggerFactory.getLogger(CodeSaver.class);

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss-SSS");

    private static File dir;

    static {
        dir = new File("code");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void save(String user, String gameName, long date, String code) {
        user = clean(user);
        String time = formatter.format(new Date(date));

        try (FileWriter fw = new FileWriter(dir.getAbsolutePath() + "/" + user + "_" + time + "_" + gameName + ".src");
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String clean(String user) {
        return user.replaceAll("[^\\dA-Za-z0-9@\\.]", "_");
    }
}
