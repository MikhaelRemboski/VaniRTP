package cmds;

import main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import utils.Constants;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;


public class RtpCMD implements CommandExecutor {
    private final Main instance;

    public RtpCMD(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {





        if (!sender.hasPermission(Constants.ADMIN_RTP)) {
            String msg = instance.getConfig().getString(Constants.NO_PERMISSION_MESSAGE_PATH);
            sender.sendMessage(Main.colorize(msg));
            return true;
        }
        if (args.length != 2) {
            String msg = instance.getConfig().getString(Constants.USAGE_MESSAGE);
            sender.sendMessage(Main.colorize(msg));
            return true;
        }

        int minRange = instance.getConfig().getInt(Constants.MIN_RANGE);
        int maxRange = instance.getConfig().getInt(Constants.MAX_RANGE);
        Player player = Bukkit.getPlayer(args[0]);
        World world = Bukkit.getWorld(args[1]);
        AtomicReference<Double> zMax = new AtomicReference<>((double) 0);
        AtomicReference<Double> zMin = new AtomicReference<>((double) 0);
        AtomicReference<Double> xMax = new AtomicReference<>((double) 0);
        AtomicReference<Double> xMin = new AtomicReference<>((double) 0);
        if (player == null && world == null) {
            String msg = instance.getConfig().getString(Constants.NOT_EXIST_MESSAGE);
            sender.sendMessage(Main.colorize(msg));
            return true;
        }

        if (world.getName().equals(player.getWorld().getName())) {
            Location currentLocation = player.getLocation();
            zMax.set(currentLocation.getZ());
            zMin.set(currentLocation.getZ());
            xMax.set(currentLocation.getX());
            xMin.set(currentLocation.getX());
        } else {

            List<String> worlds = instance.getConfig().getStringList(Constants.WORLD_LIST);
            worlds.forEach(wName -> {
                if (wName.equals(args[1])) {
                    zMax.set((double) instance.getConfig().getInt(Constants.WORLD_LIST + "." + args[1] + ".z"));
                    zMin.set((double) instance.getConfig().getInt(Constants.WORLD_LIST + "." + args[1] + ".z"));
                    xMax.set((double) instance.getConfig().getInt(Constants.WORLD_LIST + "." + args[1] + ".x"));
                    xMin.set((double) instance.getConfig().getInt(Constants.WORLD_LIST + "." + args[1] + ".x"));
                }
            });


        }
        teleport(sender, minRange, maxRange, player, world, zMax.get(), zMin.get(), xMax.get(), xMin.get());


        return true;
    }

    private void teleport(CommandSender sender, int minRange, int maxRange, Player player, World world, double zMax, double zMin, double xMax, double xMin) {
        int caseNumber = randomNumber(1, 4);
        switch (caseNumber) {
            case 1:
                zMin += minRange;
                zMax += maxRange;
                xMin += minRange;
                xMax += maxRange;
                break;
            case 2:
                zMin -= minRange;
                zMax -= maxRange;
                xMin -= minRange;
                xMax -= maxRange;
                break;
            case 3:
                zMin -= minRange;
                zMax -= maxRange;
                xMin += minRange;
                xMax += maxRange;
                break;
            case 4:
                zMin += minRange;
                zMax += maxRange;
                xMin -= minRange;
                xMax -= maxRange;
                break;
            default:
                String msg = instance.getConfig().getString(Constants.UNEXPECTED_ERROR_MESSAGE);
                sender.sendMessage(Main.colorize(msg));
                break;
        }
        Block blockTeleport = getValidBlock(world, zMax, zMin, xMax, xMin);
        String tpMsg = instance.getConfig().getString(Constants.TELEPORT_MESSAGE);
        player.sendMessage(Main.colorize(tpMsg));
        player.teleport(blockTeleport.getLocation());
    }

    private Block getValidBlock(World world, double zMax, double zMin, double xMax, double xMin) {
        int z;
        int x;
        if (zMin > zMax) {
            z = randomNumber((int) zMax, (int) zMin);
        } else {
            z = randomNumber((int) zMin, (int) zMax);
        }
        if (xMin > xMax) {
            x = randomNumber((int) xMax, (int) xMin);
        } else {
            x = randomNumber((int) xMin, (int) xMax);
        }
        Block blockTeleport = world.getHighestBlockAt(x, z);

        Material type = blockTeleport.getType();

        if (!type.isSolid() || type.isBurnable() || type.isFlammable()) {
            return getValidBlock(world, zMax, zMin, xMax, xMin);

        }
        return blockTeleport;


    }


    private static int randomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}

