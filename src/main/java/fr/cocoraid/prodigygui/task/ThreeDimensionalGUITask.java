package fr.cocoraid.prodigygui.task;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.SoundData;
import fr.cocoraid.prodigygui.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ThreeDimensionalGUITask {

    private ProdigyGUI instance;
    public ThreeDimensionalGUITask(ProdigyGUI instance) {
        this.instance = instance;

        runAsync();

    }

    public void runSync() {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                i%=100;

                ProdigyGUIPlayer.getProdigyPlayers().values().forEach(p -> {
                    ThreeDimensionGUI gui = p.getThreeDimensionGUI();
                    Player player = p.getPlayer();
                    if (player != null && p.getPlayer().isOnline() && gui != null && gui.isSpawned()) {


                    }
                });
            }
        }.runTaskTimer(ProdigyGUI.getInstance(),20 * 5,20);
    }


    public void runAsync() {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                i%=100;

                ProdigyGUIPlayer.getProdigyPlayers().values().forEach(p -> {

                    ThreeDimensionGUI gui = p.getThreeDimensionGUI();
                    Player player = p.getPlayer();
                    Location location = p.getPlayer().getLocation();
                    if (player != null && p.getPlayer().isOnline() && gui != null && gui.isSpawned()) {


                        if(i % 20 * 5 == 0) {
                            if(!location.getWorld().equals(gui.getCenter().getWorld())) {
                                gui.closeGui();
                                return;
                            }

                            if(location.distance(gui.getCenter()) > 10) {
                                gui.closeGui();
                                return;
                            }
                        }


                        if(i % 20 == 0)
                            p.getThreeDimensionGUI().setSelected(0);

                       /* if (gui.getCoin() != null) {
                            if (isLookingAt(player, gui.getCoin().getSelector().getLocation())) {
                                new ColoredParticle(Particle.REDSTONE, gui.getCoin().getSelector().getLocation().clone().add(UtilMath.randomRange(-0.5, 0.5), UtilMath.randomRange(0.3, 0.5), UtilMath.randomRange(-0.5, 0.5)), 255, 255, 0).send(p.getPlayer());
                                gui.getCoin().getItemDisplay().setHeadPose(new EulerAngle(Math.toRadians(UtilMath.randomRange(-10, 10)), Math.toRadians(UtilMath.randomRange(-10, 10)), 0));
                                gui.getCoin().getItemDisplay().updateMetadata();
                            }
                        }*/

                       if(!location.getWorld().equals(gui.getCenter().getWorld())) return;
                       if(location.distance(gui.getCenter()) > 2) return;

                        gui.getAllItems().stream().filter(i ->
                                i.getItem().getLocation() != null
                                        && isLookingAt(p.getPlayer(), i.getItem().getSelector().getLocation())
                                        && gui.getSelected() <= 4
                                        && i.getItem().isSpawned()
                                        && (gui.getLastSelected() == null || !p.getThreeDimensionGUI().getLastSelected().equals(i.getItem()))).findAny().ifPresent(i -> {

                            gui.addSelected();
                            if (gui.getLastSelected() != null) {
                                gui.getLastSelected().move(true);
                            }
                            gui.setLastSelected(i.getItem());

                            if(i.getItem().getData().getLore() != null)
                                Utils.sendActionMessage(player, PlaceholderAPIBridge.setPlaceholders(i.getItem().getData().getLore(),player));

                            Bukkit.getScheduler().runTask(instance, () -> {
                                ThreeDimensionalMenu menu = gui.getMenu();
                                if (menu.getDefaultChangeSound() != null) {
                                    player.playSound(player.getLocation(),
                                            menu.getDefaultChangeSound().getSound(),
                                            menu.getDefaultChangeSound().getVolume(),
                                            menu.getDefaultChangeSound().getPitch());
                                }
                            });


                            gui.getLastSelected().move(false);

                        });
                    }
                });
            }
        }.runTaskTimerAsynchronously(ProdigyGUI.getInstance(),0,0);
    }


    private boolean isLookingAt(Player player, Location l) {
        Location eye = player.getEyeLocation();
        Vector toEntity = l.toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.96D;
    }
}


