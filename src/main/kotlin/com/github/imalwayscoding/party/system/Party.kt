package com.github.imalwayscoding.party.system

import org.bukkit.ChatColor
import org.bukkit.entity.Player

class Party (player: Player) {

    companion object {

        private val partyMap = hashMapOf<Int, Party>()

        private var lastId = 0

        fun getParty(id: Int): Party? {
            return if (partyMap.containsKey(id)) partyMap[id] else null
        }

    }

    private var id = 0

    private lateinit var masterPlayer: Player

    private lateinit var players: Set<Player>

    init {
        masterPlayer = player
        players = hashSetOf()
        players.plus(player)
        id = lastId++
        partyMap[id] = this
    }

    fun getId(): Int {
        return id
    }

    fun getMasterPlayer(): Player {
        return masterPlayer
    }

    fun isMaster(player: Player): Boolean {
        return masterPlayer == player
    }

    fun changeMaster() {
        for (p in players) {
            masterPlayer = p
            return
        }
    }

    fun promoteMaster(player: Player) {
        masterPlayer = player
    }

    fun addPlayer(player: Player) {
        players.plus(player)
    }

    fun removePlayer(player: Player) {
        players.minus(player)
    }

    fun removeParty() {
        partyMap.minus(id)
    }

    fun getPlayers(): Set<Player> {
        return players
    }

    fun sendMessage(message: String) {
        for (p in players) {
            p.sendMessage("${ChatColor.BLUE}파티 > ${ChatColor.YELLOW}알림 > ${ChatColor.WHITE}$message")
        }
    }

    fun commandSendMessage(message: String, sender: Player) {
        val prefix = getPrefix(this, sender)

        sendMessage(message)
        // Party > Name: Message
    }

    fun channelSendMessage(message: String, displayname: String) {
        sendMessage(message)
    }

    fun getPrefix(party: Party, player: Player): String {
        return if (party.isMaster(player)) {
            "${ChatColor.YELLOW}파티장"
        } else {
            "${ChatColor.WHITE}파티원"
        }
    }

}