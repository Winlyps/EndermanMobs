package winlyps.endermanMobs

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector
import java.util.Random

class MobTeleportListener : Listener {

    private val random = Random()
    private val teleportedWithinRangeKey = "teleportedWithinRange"

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is LivingEntity && entity.type != EntityType.PLAYER && event.cause == DamageCause.ENTITY_ATTACK && !isAnimal(entity)) {
            teleportTowardPlayer(entity)
        }
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity
        if (entity is LivingEntity && entity.type != EntityType.PLAYER && !isAnimal(entity)) {
            teleportTowardPlayer(entity)
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val nearbyEntities = player.getNearbyEntities(10.0, 10.0, 10.0)
        for (entity in nearbyEntities) {
            if (entity is LivingEntity && entity.type != EntityType.PLAYER && !isAnimal(entity)) {
                teleportTowardPlayer(entity)
            }
        }
    }

    private fun teleportTowardPlayer(entity: LivingEntity) {
        if (entity.getMetadata(teleportedWithinRangeKey).isNotEmpty()) {
            return
        }

        val player = entity.world.players.firstOrNull { it.location.distance(entity.location) <= 20 }
        if (player != null) {
            val direction = player.location.toVector().subtract(entity.location.toVector()).normalize()
            val newLocation = entity.location.add(direction.multiply(random.nextDouble(5.0, 10.0)))
            if (newLocation.distance(player.location) <= 4) {
                entity.setMetadata(teleportedWithinRangeKey, org.bukkit.metadata.FixedMetadataValue(EndermanMobs.instance, true))
            } else {
                entity.teleport(newLocation)
            }
        }
    }

    private fun isAnimal(entity: LivingEntity): Boolean {
        return entity.type in setOf(
                EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN, EntityType.HORSE,
                EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.WOLF, EntityType.CAT,
                EntityType.RABBIT, EntityType.POLAR_BEAR, EntityType.PANDA, EntityType.FOX, EntityType.BEE
        )
    }
}