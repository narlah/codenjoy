package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CodeForReward {

    private CrudConnectionThreadPool pool;

    public CodeForReward(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS codeForReward (" +
                        "contact_mail varchar(255), " +
                        "hasCode varchar(255), " +
                        "code varchar(255));");
    }

    public String insertCode(String contact_mail) {
        String shortUUID = RandomStringUtils.randomAlphanumeric(8);
        pool.update("INSERT INTO codeForReward (contact_mail, hasCode, code) VALUES (?,?,?);",
                new Object[]{contact_mail,"true", shortUUID});
        return shortUUID;
    }

    public boolean hasCode(final String contact_mail) {
        return pool.select("SELECT count(*) AS total FROM codeForReward WHERE contact_mail = ? AND hasCode = ?;",
                new Object[]{contact_mail, "true"},
                resultSet -> {
                    if (!resultSet.next()) {
                        return false;
                    }
                    int count = resultSet.getInt("total");
                    if (count > 1) {
                        throw new IllegalStateException("Found more than one user with email " + contact_mail);
                    }
                    return count > 0;
                }
        );
    }


    public String getCode(final String contact_mail) {
        return pool.select("SELECT code FROM codeForReward WHERE contact_mail = ? AND hasCode = ?;",
                new Object[]{contact_mail, "true"},
                resultSet -> {
                    if (!resultSet.next()) {
                        return "false";
                    }
                    return resultSet.getString("code");
                }
        );
    }


    public List<CodeReward> getAll() {
        return pool.select("SELECT * FROM codeForReward;",
                resultSet -> {
                    List<CodeReward> result = new LinkedList<>();
                    while (resultSet.next()) {
                        result.add(new CodeReward(
                                resultSet.getString("contact_mail"),
                                resultSet.getString("hasCode"),
                                resultSet.getString("code")));
                    }
                    return result;
                }
        );
    }
}
