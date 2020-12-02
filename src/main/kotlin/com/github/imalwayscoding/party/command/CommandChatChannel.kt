package com.github.imalwayscoding.party.command

import com.github.imalwayscoding.party.system.PartyPlayer
import com.github.imalwayscoding.party.util.ChatChannel
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandChatChannel : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        if (cmd.name.equals("chatchannel", true) || cmd.name.equals("cc", true)) {
            if (sender is Player) {
                val player: Player = sender
                val pp = PartyPlayer.getPartyPlayer(player)
                if (args.isEmpty()) {
                    return false
                }
                if (args.size > 1) {
                    return false
                }
                when (args[0]) {
                    "party" -> {
                        if (pp?.hasParty() == true) {
                            if (pp.getState() == ChatChannel.PARTY) {
                                player.sendMessage("${ChatColor.RED}이미 채팅 채널이 파티 입니다.")
                                return true
                            }
                            pp.setState(ChatChannel.PARTY)
                        }
                    }
                    "default" -> {
                        if (pp?.getState() == ChatChannel.DEFAULT) {
                            player.sendMessage("${ChatColor.RED}이미 전체 채널이 파티 입니다.")
                            return true
                        }
                        pp?.setState(ChatChannel.DEFAULT)
                    }
                }
            }
        }

        return false
    }
}