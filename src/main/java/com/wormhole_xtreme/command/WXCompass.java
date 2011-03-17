/**
 *   Wormhole X-Treme Plugin for Bukkit
 *   Copyright (C) 2011  Ben Echols
 *                       Dean Bailey
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wormhole_xtreme.command;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.WormholeXTreme;
import com.wormhole_xtreme.config.ConfigManager;
import com.wormhole_xtreme.config.ConfigManager.StringTypes;
import com.wormhole_xtreme.model.Stargate;
import com.wormhole_xtreme.model.StargateManager;

/**
 * @author alron
 *
 */
public class WXCompass implements CommandExecutor {

    /**
     * Instantiates a new wX compass.
     *
     * @param wormholeXTreme the wormhole x treme
     */
    public WXCompass(WormholeXTreme wormholeXTreme) {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        Player p = null;
        if (CommandUtlities.playerCheck(sender))
        {
            p = (Player)sender;
        } 
        else
        {
            return true;
        }
        boolean allowed = false;
        if (WormholeXTreme.Permissions != null)
        {
            if (WormholeXTreme.Permissions.has(p, "wormhole.use.compass"))
            {
                allowed = true;
            }
        }
        if (p.isOp() || allowed )
        {
            Location current = p.getLocation();
            // HaNieL from Bukkit.org gave me this!
            ArrayList<Stargate> gates = StargateManager.GetAllGates();
            double man = Double.MAX_VALUE;
            Stargate closest = null;
        
            for(Stargate s : gates)
            {
                Location t = s.TeleportLocation;
                double distance = Math.sqrt( Math.pow(current.getX() - t.getX(), 2) + 
                                             Math.pow(current.getY() - t.getY(), 2) +
                                             Math.pow(current.getZ() - t.getZ(), 2) );
                if(distance < man)
                {
                    man = distance;
                    closest = s;
                }
            }
        
            if(closest != null)
            {
                p.setCompassTarget(closest.TeleportLocation);
                p.sendMessage("Compass set to wormhole: " + closest.Name);
            }
            else
            {
                p.sendMessage("No wormholes to track!");
            }
        }
        else 
        {
            p.sendMessage( ConfigManager.output_strings.get(StringTypes.PERMISSION_NO));
        }
        return true;
    }

}
