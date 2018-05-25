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
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.web.restParamClasses.HackathonRegistrant;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class HackathonRegistration {

    private CrudConnectionThreadPool pool;

    public HackathonRegistration(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS hackathonRegistration (" +
                        "contactEmail varchar(255), " +
                        "contactName varchar(255), " +
                        "contactPhone varchar(255), " +
                        "teamName varchar(255), " +
                        "teamDescription varchar(255));");
        //TODO do somethng for the "PRIMARY KEY(`contactEmail`)"); ?
    }

    public void registerForHackathon(final HackathonRegistrant hackRegistrant) {
        pool.update("INSERT INTO hackathonRegistration (contactEmail, contactName, contactPhone, teamName, teamDescription) VALUES (?,?,?,?,?);",
                new Object[]{hackRegistrant.getContactEmail(), hackRegistrant.getContactName(), hackRegistrant.getContactPhone(), hackRegistrant.getTeamName(), hackRegistrant.getTeamDescription()});
    }


    public boolean isRegistered(final String email) {
        return pool.select("SELECT count(*) AS total FROM hackathonRegistration WHERE contactEmail = ?;",
                new Object[]{email},
                new ObjectMapper<Boolean>() {
                    @Override
                    public Boolean mapFor(ResultSet resultSet) throws SQLException {
                        if (!resultSet.next()) {
                            return false;
                        }
                        int count = resultSet.getInt("total");
                        if (count > 1) {
                            throw new IllegalStateException("Found more than one user with email " + email);
                        }
                        return count > 0;
                    }
                }
        );
    }

    public HackathonRegistrant getRegistrant(final String contact_mail) {
        return pool.select("SELECT * FROM hackathonRegistration WHERE contactEmail = ?;",
                new Object[]{contact_mail},
                new ObjectMapper<HackathonRegistrant>() {
                    @Override
                    public HackathonRegistrant mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return new HackathonRegistrant(
                                    resultSet.getString("contactEmail"),
                                    resultSet.getString("contactName"),
                                    resultSet.getString("contactPhone"),
                                    resultSet.getString("teamName"),
                                    resultSet.getString("teamDescription"));
                        } else {
                            return null;
                        }
                    }
                }
        );
    }

    public List<HackathonRegistrant> getHackathonRegistrants() {
        return pool.select("SELECT * FROM hackathonRegistration;",
                new ObjectMapper<List<HackathonRegistrant>>() {
                    @Override
                    public List<HackathonRegistrant> mapFor(ResultSet resultSet) throws SQLException {
                        List<HackathonRegistrant> result = new LinkedList<>();
                        while (resultSet.next()) {
                            result.add(new HackathonRegistrant(
                                    resultSet.getString("contactEmail"),
                                    resultSet.getString("contactName"),
                                    resultSet.getString("contactPhone"),
                                    resultSet.getString("teamName"),
                                    resultSet.getString("teamDescription")));
                        }
                        return result;
                    }
                }
        );
    }
}
