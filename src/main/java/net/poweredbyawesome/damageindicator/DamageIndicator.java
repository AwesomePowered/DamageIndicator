package net.poweredbyawesome.damageindicator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Random;

public final class DamageIndicator extends JavaPlugin implements Listener {

    Random r = new Random();
    ItemStack is = new ItemStack(Material.GOLD_INGOT, 1);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamagee(EntityDamageByEntityEvent ev) { //for testing only
        Item item = ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), is);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(new Vector((r.nextFloat() * 1.69 - 1), 0.9, (r.nextFloat() * 1.69 - 1)).multiply(0.666));
        item.setCustomName(ChatColor.RED + new DecimalFormat("0.000").format(ev.getFinalDamage()));
        item.setCustomNameVisible(true);
        item.setMetadata("INDICATOR", new FixedMetadataValue(this, true));
        deleteItem(item, 30);
    }

    public void deleteItem(final Item item, int time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                item.remove();
            }
        }.runTaskLater(this, time);
    }

    @EventHandler
    public void onPick(InventoryPickupItemEvent ev) {
        if (ev.getItem().hasMetadata("INDICATOR")) {
            ev.setCancelled(true);
        }
    }
}