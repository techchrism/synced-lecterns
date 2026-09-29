package me.techchrism.syncedlecterns

import io.papermc.paper.event.player.PlayerLecternPageChangeEvent
import org.bukkit.Bukkit
import org.bukkit.block.Lectern
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class SyncedLecterns : JavaPlugin(), Listener {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private fun onPageTurn(event: PlayerLecternPageChangeEvent) {
        val meta = event.book.itemMeta as BookMeta
        val title = meta.title ?: return
        val author = meta.author ?: return

        for(world in Bukkit.getWorlds()) {
            for(chunk in world.loadedChunks) {
                for(lectern in chunk.tileEntities) {
                    if(lectern !is Lectern || lectern == event.lectern) continue

                    val otherBook = (lectern.inventory.getItem(0) ?: continue).itemMeta
                    if(otherBook !is BookMeta) continue
                    val otherTitle = otherBook.title ?: continue
                    val otherAuthor = otherBook.author ?: continue

                    if(title == otherTitle && author == otherAuthor) {
                        lectern.page = event.newPage
                        lectern.update()
                    }
                }
            }
        }
    }
}