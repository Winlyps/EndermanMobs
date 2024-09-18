package winlyps.endermanMobs

import org.bukkit.plugin.java.JavaPlugin

class EndermanMobs : JavaPlugin() {

    companion object {
        lateinit var instance: EndermanMobs
            private set
    }

    override fun onEnable() {
        instance = this
        // Register the event listener
        server.pluginManager.registerEvents(MobTeleportListener(), this)
        logger.info("EndermanMobs plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("EndermanMobs plugin has been disabled.")
    }
}