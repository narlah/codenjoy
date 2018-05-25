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
﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SnakeClient
{
    internal class Board
    {
        public string RawBoard { get; private set; }

        public int MapSize { get; private set; }

        public void Parse(string input)
        {
            if (input.StartsWith("board="))
                input = input.Substring(6);

            RawBoard = input
                .Replace('�?�', '#')  // wall
                .Replace('▲', '0').Replace('◄', '0').Replace('►', '0').Replace('▼', '0')  // head
                .Replace('║', 'o').Replace('═', 'o').Replace('╙', 'o').Replace('�?', 'o')  // body
                .Replace('╓', 'o').Replace('╕', 'o')
                .Replace('╗', 'o').Replace('╝', 'o').Replace('╔', 'o').Replace('╚', 'o')  // body
                .Replace('�?�', 'X')  // bad apple
                .Replace('�?�', '$'); // good apple
            int length = RawBoard.Length;
            MapSize = (int) Math.Sqrt(length);
        }

        public char GetAt(int x, int y)
        {
            return RawBoard[x + y * MapSize];
        }

        public string GetDisplay()
        {
            StringBuilder sb = new StringBuilder();

            for (int line = 0; line < MapSize; line++)
            {
                if (line > 0)
                    sb.AppendLine();
                sb.Append("  ");
                sb.Append(RawBoard.Substring(MapSize * line, MapSize));
            }

            return sb.ToString();
        }
    }
}
