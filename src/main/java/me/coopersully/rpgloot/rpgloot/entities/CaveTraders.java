package me.coopersully.rpgloot.rpgloot.entities;

import me.coopersully.rpgloot.rpgloot.ItemKeys;
import me.coopersully.rpgloot.rpgloot.RPGLoot;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class CaveTraders {

    static {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RPGLoot.getPlugin(), CaveTraders::randomlySpawnVillager, 1200);
    }

    public static List<Player> cavePlayers = new ArrayList<>();

    private static void refreshCavePlayers() {
        var players = Bukkit.getServer().getOnlinePlayers();
        for (var player : players) {
            World world = player.getWorld();
            // If the player is not in the overworld, ignore them.
            if (!world.isBedWorks()) continue;

            Location location = player.getLocation();
            // If the player is not below sea level, ignore them.
            if (location.getBlock().getY() >= world.getSeaLevel()) continue;

            cavePlayers.add(player);
        }
    }

    private static void randomlySpawnVillager() {
        refreshCavePlayers();
        if (cavePlayers.isEmpty()) {
            Bukkit.getLogger().log(Level.INFO, "A cave trader attempted to spawn but no players were found in caves; trying again in 60 seconds.");
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RPGLoot.getPlugin(), CaveTraders::randomlySpawnVillager, 1200);
            return;
        }

        Random random = new Random();
        Player player = cavePlayers.get(random.nextInt(cavePlayers.size()));

        spawnVillager(player.getLocation());

        int runtime = (20 * (60 * 20)) / cavePlayers.size();
        if (runtime <= 1200) runtime = 1200;
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RPGLoot.getPlugin(), CaveTraders::randomlySpawnVillager, runtime);
        Bukkit.getLogger().log(Level.INFO, "A cave trader spawned near " + player.displayName() + ". Another one will spawn in " + (runtime / 20) + " seconds near a random cave player.");
    }

    public static void spawnVillager(@NotNull Location location) {

        World world = location.getWorld();
        WanderingTrader wanderingTrader = world.spawn(location, WanderingTrader.class);

        List<MerchantRecipe> recipes = new ArrayList<>();

        var recipe2 = new MerchantRecipe(new ItemStack(Material.IRON_INGOT, 2), 5);
        recipe2.addIngredient(new ItemStack(Material.RAW_IRON, 1));
        recipes.add(recipe2);

        var recipe1 = new MerchantRecipe(new ItemStack(Material.GOLD_INGOT, 3), 7);
        recipe1.addIngredient(new ItemStack(Material.RAW_GOLD, 2));
        recipes.add(recipe1);

        var recipe3 = new MerchantRecipe(new ItemStack(Material.COPPER_INGOT, 4), 9);
        recipe3.addIngredient(new ItemStack(Material.RAW_COPPER, 3));
        recipes.add(recipe3);

        var recipe4 = new MerchantRecipe(new ItemStack(Material.EMERALD, 1), 11);
        recipe4.addIngredient(new ItemStack(Material.COBBLESTONE, 48));
        recipes.add(recipe4);

        var recipe5 = new MerchantRecipe(new ItemStack(Material.EMERALD, 1), 13);
        recipe5.addIngredient(new ItemStack(Material.COBBLED_DEEPSLATE, 32));
        recipes.add(recipe5);

        var recipe6 = new MerchantRecipe(new ItemStack(Material.COAL, 1), 16);
        recipe6.addIngredient(new ItemStack(Material.ROTTEN_FLESH, 4));
        recipes.add(recipe6);

        Random random = new Random();
        Material randomDisc;
        randomDisc = switch (random.nextInt(13)) {
            case 0 -> Material.MUSIC_DISC_11;
            case 1 -> Material.MUSIC_DISC_13;
            case 2 -> Material.MUSIC_DISC_CAT;
            case 3 -> Material.MUSIC_DISC_BLOCKS;
            case 4 -> Material.MUSIC_DISC_CHIRP;
            case 5 -> Material.MUSIC_DISC_FAR;
            case 6 -> Material.MUSIC_DISC_MALL;
            case 7 -> Material.MUSIC_DISC_MELLOHI;
            case 8 -> Material.MUSIC_DISC_STAL;
            case 9 -> Material.MUSIC_DISC_STRAD;
            case 10 -> Material.MUSIC_DISC_WARD;
            case 11 -> Material.MUSIC_DISC_WAIT;
            case 12 -> Material.MUSIC_DISC_OTHERSIDE;
            case 13 -> Material.MUSIC_DISC_PIGSTEP;
            default -> throw new RuntimeException("Music disc incorrectly selected in CaveDetector class");
        };

        var recipe7 = new MerchantRecipe(new ItemStack(randomDisc, 1), 1);
        recipe7.addIngredient(new ItemStack(Material.EMERALD, 32));
        recipes.add(recipe7);

        ItemStack minersHelmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemMeta minersHelmetMeta = minersHelmet.getItemMeta();
        minersHelmetMeta.displayName(
                Component.text(ChatColor.LIGHT_PURPLE + "Miner's Hard Hat" + ChatColor.RESET)
        );
        minersHelmetMeta.lore(List.of(
                Component.text( ChatColor.ITALIC + "Your guide into the depths." + ChatColor.RESET).color(TextColor.color(196, 164, 132)),
                Component.text(""),
                Component.text(ChatColor.GRAY + "Statistics:" + ChatColor.RESET),
                Component.text(ChatColor.DARK_GREEN +" Living Source" + ChatColor.RESET),
                Component.text(ChatColor.DARK_GREEN +" Epic Treasure Item" + ChatColor.RESET),
                Component.text(""),
                Component.text(ChatColor.GRAY + "Abilities:" + ChatColor.RESET),
                Component.text(ChatColor.DARK_GREEN +" PASSIVE - Illuminates nearby area" + ChatColor.RESET)
        ));
        minersHelmetMeta.getPersistentDataContainer().set(ItemKeys.treasureItem, PersistentDataType.STRING, "1");
        minersHelmetMeta.getPersistentDataContainer().set(ItemKeys.minersHat, PersistentDataType.STRING, "1");
        minersHelmet.setItemMeta(minersHelmetMeta);

        var recipe8 = new MerchantRecipe(minersHelmet, 1);
        recipe8.addIngredient(new ItemStack(Material.EMERALD, 16));
        recipes.add(recipe8);

        wanderingTrader.setRecipes(recipes);
    }

}
