package com.github.imalwayscoding.party.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class TabCompleteParty : TabCompleter {
    override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {

        if (cmd.name.equals("파티", true)) {

            if (args.size == 1) {
                val arguments = mutableListOf<String>()
                arguments.add("생성")
                arguments.add("삭제")
                arguments.add("목록")
                arguments.add("탈퇴")
                arguments.add("채팅")
                arguments.add("초대")
                arguments.add("수락")
                arguments.add("거절")
                arguments.add("추방")
                arguments.add("위임")
                return arguments
            }

            if (args.size == 2) {
                val arguments = mutableListOf<String>()
                val players: Array<Player?> = arrayOfNulls(Bukkit.getServer().onlinePlayers.size)
                Bukkit.getServer().onlinePlayers.toTypedArray()
                for (player in players) {
                    if (player != null) arguments.add(player.name)
                }
                return arguments
            }

        }

        return mutableListOf()
    }
}