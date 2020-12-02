package com.github.imalwayscoding.party.system.listner

import com.github.imalwayscoding.party.system.Party
import com.github.imalwayscoding.party.system.PartyPlayer
import com.github.imalwayscoding.party.util.ChatChannel
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {

        val pp = PartyPlayer.getPartyPlayer(event.player)

        if (pp?.getState() == ChatChannel.PARTY) {
            if (!pp.hasParty()) {
                event.isCancelled = true
                pp.setState(ChatChannel.DEFAULT)
                event.player.sendMessage("${ChatColor.RED}파티가 없어, 채팅 채널이 파티에 채널에서 기본 채널로 변경 되었습니다.")
                return
            }
        }

        val party = Party.getParty(pp!!.getPartyId())!!

        party.channelSendMessage(event.message, "${getPrefix(party, event.player)}${event.player.name}")

    }

    fun getPrefix(party: Party, player: Player): String {
        return if (party.isMaster(player)) {
            "${ChatColor.YELLOW}파티장"
        } else {
            "${ChatColor.WHITE}파티원"
        }
    }

}