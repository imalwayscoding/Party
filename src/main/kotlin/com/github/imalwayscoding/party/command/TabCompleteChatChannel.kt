package com.github.imalwayscoding.party.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleteChatChannel : TabCompleter{

    override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {

        if (cmd.name.equals("chatchannel", true) || cmd.name.equals("cc", true)) {

            if (args.size == 1) {
                val arguments = mutableListOf<String>()
                arguments.add("party")
                arguments.add("default")
            }

        }

        return mutableListOf()
    }

}