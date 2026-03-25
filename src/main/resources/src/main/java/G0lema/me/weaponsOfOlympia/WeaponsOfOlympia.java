package G0lema.me.weaponsOfOlympia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WeaponsOfOlympia extends JavaPlugin implements Listener {

    private TideCallerWeapon tideCaller;
    private ThunderboltWeapon thunderbolt;
    private BladeOfWarWeapon bladeOfWar;
    private NyxbowWeapon nyxbow;
    private StaffOfTheUnderworldWeapon hadesStaff;
    private CaduceusWeapon caduceus;
    private Aegisweapon aegis;

    // Track which weapons each player has crafted (persisted to file)
    private final Map<UUID, Set<String>> craftedWeapons = new HashMap<>();
    private File craftedDataFile;
    private FileConfiguration craftedDataConfig;

    // All weapon recipe keys
    private static final String[] RECIPE_KEYS = {
        "tide_caller_recipe",
        "thunderbolt_recipe",
        "blade_of_war_recipe",
        "nyxbow_recipe",
        "staff_of_the_underworld_recipe",
        "caduceus_recipe",
        "aegis_recipe"
    };

    // Weapon names for /weapon command tab completion
    private static final String[] WEAPON_NAMES = {
        "tidecaller", "thunderbolt", "bladeofwar", "nyxbow", "staff", "caduceus", "aegis"
    };

    @Override
    public void onEnable() {
        getLogger().info("Weapons of Olympia loading...");

        // Load crafted weapons data
        loadCraftedData();

        // Remove vanilla shield recipe
        Bukkit.removeRecipe(NamespacedKey.minecraft("shield"));

        // Register weapons
        tideCaller = new TideCallerWeapon(this);
        getServer().getPluginManager().registerEvents(tideCaller, this);
        tideCaller.registerRecipe();

        thunderbolt = new ThunderboltWeapon(this);
        getServer().getPluginManager().registerEvents(thunderbolt, this);
        thunderbolt.registerRecipe();

        bladeOfWar = new BladeOfWarWeapon(this);
        getServer().getPluginManager().registerEvents(bladeOfWar, this);
        bladeOfWar.registerRecipe();

        nyxbow = new NyxbowWeapon(this);
        getServer().getPluginManager().registerEvents(nyxbow, this);
        nyxbow.registerRecipe();
        nyxbow.startStillTickCounter();

        hadesStaff
