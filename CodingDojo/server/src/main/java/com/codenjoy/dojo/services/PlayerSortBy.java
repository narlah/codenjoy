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


import java.util.Comparator;

public class PlayerSortBy implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        if (o1 == null || o2 == null) try {
            throw new Exception("o1 == null || o2 == null");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (o1 == NullPlayer.INSTANCE || o2 == NullPlayer.INSTANCE) try {
            throw new Exception("(o1 == NullPlayer.INSTANCE || o2 == NullPlayer.INSTANCE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(o1.getScore() instanceof Integer) || !(o2.getScore() instanceof Integer))
            try {
                throw new Exception("Not Interger!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        Integer score1 = o1.getScore()==null ? 0 :(Integer) o1.getScore();
        Integer score2 = o2.getScore()==null ? 0 : (Integer) o2.getScore();

        return score1 - score2;
    }
}
