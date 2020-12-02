package com.github.imalwayscoding.party

import com.github.imalwayscoding.party.command.CommandChatChannel
import com.github.imalwayscoding.party.command.CommandParty
import com.github.imalwayscoding.party.command.TabCompleteChatChannel
import com.github.imalwayscoding.party.command.TabCompleteParty
import com.github.imalwayscoding.party.system.listner.ChatListener
import com.github.imalwayscoding.party.system.listner.PlayerListener
import org.bukkit.plugin.java.JavaPlugin

class PartyPlugin : JavaPlugin() {

    companion object {
    lateinit var plugin: PartyPlugin
    }

    override fun onEnable() {
        plugin = this
        register()
        server.consoleSender.sendMessage("[Party] Party Plugin Enabled.")
    }

    override fun onDisable() {
        server.consoleSender.sendMessage("[Party] Party Plugin Disabled.")
    }

    fun register() {
        getCommand("파티")?.setExecutor(CommandParty())
        getCommand("p")?.setExecutor(CommandParty())
        getCommand("chatchannel")?.setExecutor(CommandChatChannel())
        getCommand("cc")?.setExecutor(CommandChatChannel())
        getCommand("파티")?.tabCompleter = TabCompleteParty()
        getCommand("p")?.tabCompleter = TabCompleteParty()
        getCommand("chatchannel")?.tabCompleter = TabCompleteChatChannel()
        getCommand("cc")?.tabCompleter = TabCompleteChatChannel()
        this.server.pluginManager.registerEvents(ChatListener(), this)
        this.server.pluginManager.registerEvents(PlayerListener(), this)
    }
}