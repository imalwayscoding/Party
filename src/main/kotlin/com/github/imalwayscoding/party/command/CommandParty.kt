package com.github.imalwayscoding.party.command

import com.github.imalwayscoding.party.PartyPlugin
import com.github.imalwayscoding.party.system.Party
import com.github.imalwayscoding.party.system.PartyPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class CommandParty : CommandExecutor {

    companion object {
        private val inviteMap = hashMapOf<Player, Int>()
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {

            val player: Player = sender

            val pp = PartyPlayer.getPartyPlayer(player)
            lateinit var party: Party

            if (cmd.name.equals("파티", true) || cmd.name.equals("p", true)) {
                if (args.isEmpty()) {
                    help(player)
                    return true
                }

                when (args[0]) {
                    "생성" -> {
                        if (pp?.hasParty() == true) {
                            player.sendMessage("${ChatColor.RED}이미 가입된 파티가 있습니다.")
                            return true
                        }
                        party = Party(player)
                        pp?.joinParty(party.getId())
                        player.sendMessage("${ChatColor.YELLOW}파티를 생성하였습니다.")
                        return true
                    }
                    "삭제" -> {
                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }
                        party = Party.getParty(pp!!.getPartyId())!!
                        if (!party.isMaster(player)) {
                            player.sendMessage("${ChatColor.RED}권한이 없습니다.")
                            return true
                        }
                        for (partyPlayer in party.getPlayers()) {
                            PartyPlayer.getPartyPlayer(partyPlayer)?.leaveParty()
                            partyPlayer.sendMessage("${ChatColor.RED}파티가 삭제 되었습니다.")
                        }
                        party.removeParty()
                    }
                    "목록" -> {
                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }
                        party = Party.getParty(pp!!.getPartyId())!!

                        for (partyPlayer in party.getPlayers()) {
                            player.sendMessage("${partyPlayer.name} ${isMasterColor(party, partyPlayer)}")
                        }
                    }
                    "탈퇴" -> {
                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }
                        party = Party.getParty(pp!!.getPartyId())!!

                        if (party.isMaster(player)) {
                            party.removePlayer(player)
                            party.changeMaster()
                            party.sendMessage("파티장이 파티에서 탈퇴하여, 파티장이 ${party.getMasterPlayer().name}님으로 변경되었습니다.")
                            player.sendMessage("파티를 탈퇴했습니다.")
                            pp.leaveParty()
                            return true
                        }
                        party.removePlayer(player)
                        party.sendMessage("${player.name}님이 파티에서 탈퇴 하셨습니다.")
                        player.sendMessage("파티를 탈퇴했습니다.")
                        return true
                    }
                    "채팅" -> {
                        if (args.size < 2) {
                            player.sendMessage("${ChatColor.RED}메세지를 입력하세요.")
                            return true
                        }

                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }

                        val message = args[1]
                        party = Party.getParty(pp!!.getPartyId())!!

                        party.commandSendMessage(message, player)
                    }
                    "초대" -> {
                        if (args.size < 2) {
                            player.sendMessage("${ChatColor.RED}닉네임을 입력하세요.")
                            return true
                        }
                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }
                        party = Party.getParty(pp!!.getPartyId())!!
                        if (!party.isMaster(player)) {
                            player.sendMessage("${ChatColor.RED}권한이 없습니다.")
                            return true
                        }

                        val target = Bukkit.getPlayer(args[1])
                        if (target == null) {
                            player.sendMessage("${ChatColor.RED}찾을 수 없는 플레이어 입니다.")
                            return true
                        }
                        val partyPlayerTarget = PartyPlayer.getPartyPlayer(target)
                        if (partyPlayerTarget?.hasParty() == true) {
                            player.sendMessage("${ChatColor.RED}이미 가입된 파티가 있는 플레이어 입니다.")
                            return true
                        }

                        if (inviteMap.containsKey(target.name)) {
                            player.sendMessage("${ChatColor.RED}이미 다른 곳에서 초대가 된 플레이어 입니다.")
                            return true
                        }

                        target.sendMessage("${player.name}님이 당신을 파티에 초대 했습니다. 이 초대는 60초 동안 유지 됩니다.")
                        target.sendMessage("/파티 수락, 파티 거절 명령어로 수락 또는 거절 하세요.")
                        party.sendMessage("${target}님이 파티에 초대 되었습니다.")


                        inviteMap[target] = party.getId()

                        object : BukkitRunnable() {
                            override fun run() {
                                if (inviteMap.containsKey(target) && inviteMap[target] == party.getId()) {
                                    inviteMap.minus(target)
                                    player.sendMessage("${ChatColor.YELLOW}파티 초대가 만료 되었습니다.")
                                }
                            }
                        }.runTaskLaterAsynchronously(PartyPlugin.plugin, 20L * 60L)
                        return true
                    }
                    "추방" -> {
                        if (args.size < 2) {
                            player.sendMessage("${ChatColor.RED}닉네임을 입력하세요.")
                            return true
                        }
                        if (args.size < 2) {
                            player.sendMessage("${ChatColor.RED}닉네임을 입력하세요.")
                            return true
                        }
                        if (pp?.hasParty() == false) {
                            player.sendMessage("${ChatColor.RED}가입 된 파티가 없습니다.")
                            return true
                        }
                        party = Party.getParty(pp!!.getPartyId())!!
                        if (!party.isMaster(player)) {
                            player.sendMessage("${ChatColor.RED}권한이 없습니다.")
                            return true
                        }

                        val target = Bukkit.getPlayer(args[1])
                        if (target == null) {
                            player.sendMessage("${ChatColor.RED}찾을 수 없는 플레이어 입니다.")
                            return true
                        }
                        val partyPlayerTarget = PartyPlayer.getPartyPlayer(target)
                        if (partyPlayerTarget?.hasParty() == true || partyPlayerTarget?.getPartyId() != pp.getPartyId()) {
                            player.sendMessage("${ChatColor.RED}같은 파티원이 아닙니다.")
                            return true
                        }
                        if (party.isMaster(target)) {
                            player.sendMessage("${ChatColor.RED}파티장은 추방 할 수 없습니다.")
                            return true
                        }
                        party.removePlayer(target)
                        partyPlayerTarget.leaveParty()
                        target.sendMessage("${ChatColor.RED}파티에서 추방 당하였습니다.")
                        party.sendMessage("${target.name}님이 파티에서 추방 당하셨습니다.")
                        return true
                    }
                    "위임" -> {
                        if (args.size < 2) {
                            player.sendMessage("${ChatColor.RED}닉네임을 입력하세요.")
                            return true
                        }
                        if (!party.isMaster(player)) {
                            player.sendMessage("${ChatColor.RED}권한이 없습니다.")
                            return true
                        }
                        val target = Bukkit.getPlayer(args[1])

                        party.promoteMaster(target!!)
                        return true
                    }
                    "수락" -> {
                        if (!(inviteMap.containsKey(player.name))) {
                            player.sendMessage("${ChatColor.RED}초대가 만료 되었거나, 초대를 받지 않았습니다.")
                            return true
                        }

                        if (pp?.hasParty() == true) {
                            player.sendMessage("${ChatColor.RED}이미 가입된 파티가 있습니다.")
                        }

                        if (Party.getParty(inviteMap[player.name]!!) != null) {
                            party = Party.getParty(inviteMap[player.name]!!)!!
                        } else {
                            player.sendMessage("${ChatColor.RED}파티를 찾을 수 없습니다.")
                            inviteMap.minus(player.name)
                            return true
                        }

                        party.addPlayer(player)
                        party.sendMessage("${player.name}님이 파티에 들어오셨습니다.")
                        pp?.joinParty(party.getId())
                        inviteMap.minus(player.name)
                        return true
                    }
                    "거절" -> {
                        if (!(inviteMap.containsKey(player.name))) {
                            player.sendMessage("${ChatColor.RED}초대가 만료 되었거나, 초대를 받지 않았습니다.")
                            return true
                        }

                        if (Party.getParty(inviteMap[player.name]!!) != null) {
                            party = Party.getParty(inviteMap[player.name]!!)!!
                        } else {
                            player.sendMessage("${ChatColor.RED}파티를 찾을 수 없습니다.")
                            inviteMap.minus(player.name)
                            return true
                        }

                        party.sendMessage("${player}님이 초대를 거절했습니다.")
                        inviteMap.minus(player.name)
                        player.sendMessage("${ChatColor.YELLOW}파티 초대를 거부 했습니다.")
                    }
                }
            }
        }

        return false
    }

    private fun help(player: Player) {
        player.sendMessage("======================================================")
        player.sendMessage("")
        player.sendMessage("/파티 생성 : 파티를 생성합니다.")
        player.sendMessage("/파티 삭제 : 파티를 삭제합니다.")
        player.sendMessage("/파티 목록 : 파티 목록을 보여줍니다.")
        player.sendMessage("/파티 탈퇴 : 파티에서 나갑니다.")
        player.sendMessage("/파티 채팅 [메세지] : [메세지]를 파티원들에게 전송합니다.")
        player.sendMessage("/파티 초대 [닉네임] : 파티에 초대합니다.")
        player.sendMessage("    ->    /파티 수락 : 받은 초대를 수락합니다.")
        player.sendMessage("    ->    /파티 거절 : 받은 초대를 거절합니다.")
        player.sendMessage("/파티 추방 [닉네임] : 파티에서 추방합니다.")
        player.sendMessage("/파티 위임 [닉네임] : 파티장을 위임합니다.")
        player.sendMessage("")
        player.sendMessage("======================================================")
    }

    fun isMasterColor(party: Party, player: Player): String {
        return if (party.isMaster(player)) {
            "${ChatColor.YELLOW}파티장"
        } else {
            "${ChatColor.WHITE}파티원"
        }
    }

}