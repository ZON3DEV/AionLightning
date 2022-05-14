/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package pirate.debug;

/**

 @author flashman
 */
public class DebugInfo<T extends Enum> {

    protected T state;
    protected String message = null;

    public DebugInfo(T state) {
        this.state = state;
    }

    public DebugInfo(T state, String message) {
        this.state = state;
        this.message = message;
    }

    public T getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
