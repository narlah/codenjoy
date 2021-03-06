package com.codenjoy.dojo.web.rest;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping(value = "/rest")
public class RestRegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RestRegistrationController.class);

    @Autowired
    private Registration registration;
    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/player/{playerName}/check/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUserLogin(@PathVariable("playerName") String playerName, @PathVariable("code") String code) {
        return registration.checkUser(playerName, code);
    }

    static class PlayerInfoPublic {
        private final String gameType;
        private final String score;
        private final String name;
        private final String data;

        PlayerInfoPublic(Player player) {
            String playerData = player.getData();
            gameType = player.getGameType().name();
            name = (playerData != null && playerData.length() > 2) ? playerData.substring(0, playerData.indexOf('|')) : "Anonymous";
            score = String.valueOf(player.getScore());
            data = "";

        }

        public String getGameType() {
            return gameType;
        }

        public String getScore() {
            return score;
        }

        public String getData() {
            return data;
        }

        public String getName() {
            return name;
        }
    }


    static class PlayerInfoPrivate {
        private final String gameType;
        private final String callbackUrl;
        private final String name;
        private final String score;
        private final String code;
        private final String data;

        PlayerInfoPrivate(Player player) {
            String playerData = player.getData();
            gameType = player.getGameType().name();
            callbackUrl = player.getCallbackUrl();
            name = player.getName();
            data = (playerData != null && playerData.length() > 2) ? playerData.substring(0, playerData.indexOf('|')) : "Anonymous";
            score = String.valueOf(player.getScore());
            code = player.getCode();
        }

        public String getGameType() {
            return gameType;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }

        public String getCode() {
            return code;
        }

        public String getData() {
            return data;
        }
    }

   /* @RequestMapping(value = "/game/{gameName}/players", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInfo> getPlayerForGame(@PathVariable("gameName") String gameName) {
        return playerService.getAll(gameName).stream()
                .map(PlayerInfo::new)
                .collect(toList());
    }*/

    @RequestMapping(value = "/game/{gameName}/players", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInfoPublic> getPlayerForGamePublic(@PathVariable("gameName") String gameName) {
        List<Player> players = playerService.getAll(gameName);
        Collections.sort(players, new PlayerSortBy());
        return players.stream().map(PlayerInfoPublic::new).collect(toList());
    }

    @RequestMapping(value = "/game/{gameName}/players", params = "email", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInfoPrivate> getPlayerForGamePrivate(@PathVariable("gameName") String gameName, @RequestParam("email") String email) {
        List<Player> players = playerService.getAll(gameName);
        return players.stream().filter(player -> player.getName().equals(email)).map(PlayerInfoPrivate::new).collect(toList());
    }
}