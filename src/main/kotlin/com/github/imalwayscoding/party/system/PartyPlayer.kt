package com.github.imalwayscoding.party.system

import com.github.imalwayscoding.party.util.ChatChannel
import org.bukkit.entity.Player

class PartyPlayer (player: Player) {

    companion object {

        private val ppMap = hashMapOf<Player, PartyPlayer>()

        fun registerPartyPlayer(player: Player) { if (!ppMap.containsKey(player)) PartyPlayer(player) }

        fun getPartyPlayer(player: Player): PartyPlayer? {
            return if (ppMap.containsKey(player)) ppMap[player] else PartyPlayer(player)
        }

    }

    private var partyId = 0

    private var state = ChatChannel.DEFAULT

    init {
        partyId = -1
        ppMap[player] = this
    }

    fun getPartyId(): Int {
        return partyId
    }

    fun hasParty(): Boolean {
        return partyId != -1
    }

    fun joinParty(partyId: Int) {
        this.partyId = partyId
    }

    fun leaveParty() {
        partyId = -1
    }

    fun setState(state: ChatChannel) {
        this.state = state
    }

    fun getState(): ChatChannel {
        return state
    }

}