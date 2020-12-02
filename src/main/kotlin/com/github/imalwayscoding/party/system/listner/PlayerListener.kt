package com.github.imalwayscoding.party.system.listner

import com.github.imalwayscoding.party.system.Party
import com.github.imalwayscoding.party.system.PartyPlayer
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) { PartyPlayer.registerPartyPlayer(event.player) }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val pp = PartyPlayer.getPartyPlayer(event.player)
        if (pp?.hasParty() == true) {
            val party = Party.getParty(pp.getPartyId())
            if (party?.getPlayers()?.size == 1) {
                party.removeParty()
                pp.leaveParty()
            } else {
                party?.removePlayer(event.player)
                if (party?.isMaster(event.player) == true) {
                    party.changeMaster()
                    party.sendMessage("${ChatColor.BLUE}Party > ${ChatColor.GREEN}INFORMATION > ${ChatColor.WHITE}파티장이 접속을 종료하여, 파티장이 ${party.getMasterPlayer().name}님으로 변경되었습니다.")
                } else {
                    party?.sendMessage("${ChatColor.BLUE}Party > ${ChatColor.GREEN}INFORMATION > ${ChatColor.WHITE}${event.player.name}님이 파티를 나갔습니다.")
                }
                pp.leaveParty()
            }
        }
    }

}