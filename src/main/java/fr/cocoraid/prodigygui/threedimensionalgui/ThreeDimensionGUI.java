package fr.cocoraid.prodigygui.threedimensionalgui;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.EconomyBridge;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.config.CoreConfig;
import fr.cocoraid.prodigygui.language.Language;
import fr.cocoraid.prodigygui.loader.CommandDeserializer;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntityArmorStand;
import fr.cocoraid.prodigygui.threedimensionalgui.item.InteractableItem;
import fr.cocoraid.prodigygui.threedimensionalgui.item.Item3D;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by cocoraid on 30/06/2016.
 */
public class ThreeDimensionGUI {

    private static CoreConfig config = ProdigyGUI.getInstance().getConfiguration();
    private static Language lang = ProdigyGUI.getInstance().getLanguage();

    protected Player player;

    private static final int maxItemNumber = 5;

    private LinkedList<InteractableItem> items = new LinkedList<>();
    private List<InteractableItem> allItems = new ArrayList<>();

    private Item3D lastSelected;
    private int selected = 0;
    private Location center;
    private ProdigyGUIPlayer pp;

    private WrapperEntityArmorStand displayName;

    protected Location[] positions = new Location[maxItemNumber];
    protected Location[] bar = new Location[2];

    private Item3D nextButton;
    private Item3D previousButton;

    private boolean isSpawned;

    private int currentPage = 0;
    private Location previous;
    private Location next;


    private double radius;
    private int angle_step;
    private ThreeDimensionalMenu menu;


    public ThreeDimensionGUI(Player player, ThreeDimensionalMenu menu) {
        this.player = player;
        this.center = player.getLocation();
        this.menu = menu;
        this.pp = ProdigyGUIPlayer.instanceOf(player);
        this.radius = menu.getRadius();
        this.angle_step = menu.getAngleStep();
    }

    public ThreeDimensionGUI setRotation(float yaw) {
        center.setYaw(yaw);
        return this;
    }

    public ThreeDimensionGUI setCenter(double x, double y, double z) {
        center.setX(x);
        center.setY(y);
        center.setZ(z);
        return this;
    }

    public void inizializeGUI() {

        menu.getItemDataList().forEach(i -> {
            int moneyPrice = i.getPrice();
            final Item3D i3D = new Item3D(player, i);
            items.add(new InteractableItem(i3D, new InteractableItem.InteractClickable() {
                @Override
                public void interact(InteractableItem listener) {

                    if(i.getPermission() != null) {
                        if(!player.hasPermission(i.getPermission())) {
                            player.sendMessage(i.getNopermissionmessage() != null ? i.getNopermissionmessage() : lang.no_permission);
                            return;
                        }
                    }

                    if (moneyPrice > 0) {
                        if (!EconomyBridge.hasValidEconomy()) {
                            player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
                            return;
                        }

                        if (!EconomyBridge.hasMoney(player, moneyPrice)) {
                            player.sendMessage(new String(lang.no_money).replace("{money}", EconomyBridge.formatMoney(moneyPrice)));
                            return;
                        }
                        if(!EconomyBridge.takeMoney(player,moneyPrice)) {
                            return;
                        }
                    }
                    if(i.getCommands() != null)
                        new CommandDeserializer(player, i.getCommands()).execute();

                }
            }));
        });
        setupItems();
        allItems.addAll(items);
    }

    public void openGui() {

        if (pp.getThreeDimensionGUI() != null) {
            center.setYaw(pp.getPreviousYaw());

            if (!pp.getThreeDimensionGUI().isSpawned) return;

            if (pp.getThreeDimensionGUI().menu.getFileName().equals(menu.getFileName())) {
                pp.getThreeDimensionGUI().closeGui();
                return;
            }
            pp.getThreeDimensionGUI().closeGui();

        } else
            pp.setPreviousYaw(center.getYaw());

        double buttonsHeight = 0.5;



        Location l = center.clone();
        l.setPitch(0);
        l.setYaw(center.getYaw() - (angle_step + 17));
        previous = l.clone().add(0, buttonsHeight, 0).toVector().add(l.getDirection().multiply(radius)).toLocation(center.getWorld());
        previous.setDirection(player.getLocation().toVector().subtract(previous.toVector()));
        bar[0] = previous.clone().add(0,2.3,0);
        l.setYaw(center.getYaw() + (angle_step + 17));

        next = l.clone().add(0, buttonsHeight, 0).toVector().add(l.getDirection().multiply(radius )).toLocation(center.getWorld());
        next.setDirection(player.getLocation().toVector().subtract(next.toVector()));
        bar[1] = next.clone().add(0,2.3,0);

        l.setYaw(center.getYaw());
        this.displayName = new WrapperEntityArmorStand(l.toVector().add(l.getDirection().multiply(radius + 0.2)).toLocation(center.getWorld()).add(0,2,0),player);

        displayName.setCustomNameVisible(true);
        displayName.setCustomName(PlaceholderAPIBridge.setPlaceholders(menu.getTitle(),player));
        displayName.setInvisible(true);
        displayName.setSmall(true);
        displayName.setMarker(true);

        pp.setThreeDimensionGUI(this);
        inizializeGUI();


        this.currentPage = 0;
        //Place next page or not ?
        if (Math.ceil(items.size() / 5f) >= 2f) {
            addNextPageButton();
        }

        if(menu.getPreviousMenu() != null) {
            addPreviousPageButton();
        }

        int i = 0;
        for (ItemData barItem : menu.getBarItemsList()) {
            if(i <= 1) {
                int moneyPrice = barItem.getPrice();
                final Item3D i3D = new Item3D(player, barItem);
                allItems.add(new InteractableItem(i3D, (InteractableItem.InteractClickable) (e) -> {
                    if(barItem.getPermission() != null) {
                        if(!player.hasPermission(barItem.getPermission())) {
                            player.sendMessage(barItem.getNopermissionmessage() != null ? barItem.getNopermissionmessage() : lang.no_permission);
                            return;
                        }
                    }

                    if (moneyPrice > 0) {
                        if (!EconomyBridge.hasValidEconomy()) {
                            player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
                            return;
                        }

                        if (!EconomyBridge.hasMoney(player, moneyPrice)) {
                            player.sendMessage(new String(lang.no_money).replace("{money}", EconomyBridge.formatMoney(moneyPrice)));
                            return;
                        }
                        if(!EconomyBridge.takeMoney(player,moneyPrice)) {
                            return;
                        }
                    }
                    if(barItem.getCommands() != null)
                        new CommandDeserializer(player, barItem.getCommands()).execute();
                }));
                Location loc = bar[i].clone();
                loc.setDirection(center.toVector().subtract(loc.toVector()).normalize());
                i3D.setPosition(loc);
            }
            i++;
        }



        displayName.spawn();
        displayName.sendUpdatedmetatada();
        this.isSpawned = true;

    }

    private void addPreviousPageButton() {
        ItemData data = new ItemData(lang.button_previous_name, config.button_previous_texture);
        previousButton = new Item3D(player, data);
        previousButton.setSmall();
        previousButton.setPosition(previous);



        allItems.add(new InteractableItem(previousButton, (InteractableItem.InteractClickable) (e) -> {
            if(currentPage <= 0) {
                if(menu.getPreviousMenu() != null) {
                    if(menu.getPreviousMenu() != null) {
                        ThreeDimensionalMenu previous = menu.getPreviousMenu();
                        new ThreeDimensionGUI(player,previous).openGui();
                    }
                }
            } else {
                currentPage--;
                switchPage();
            }
        }));
    }

    private void addNextPageButton() {
        ItemData data = new ItemData(lang.button_next_name, config.button_next_texture);
        nextButton = new Item3D(player,data);
        nextButton.setSmall();
        nextButton.setPosition(next);

        allItems.add(new InteractableItem(nextButton, (InteractableItem.InteractClickable) (e) -> {
            currentPage++;
            switchPage();
        }));
    }

    private void setupItems() {

        float yaw = center.getYaw();
        //setupAlgorithm();
        //Calculate max Page number                  // Last number    //0,1,2,3,4
        int maxNumber = Math.ceil(items.size() / 5) <= currentPage ? items.size() : (5 * (currentPage + 1));
        int p = 0;
        for (int k = 0; k < items.size(); k++) {
            if (k % 5 == 0) {
                if (p == currentPage) {
                    //Open current page;
                    int pos = 0;
                    for (int i = (p * 5); i < maxNumber; i++)
                        pos++;


                    //n = number of division example: 2 for impair or pair of 4/5 items
                    int n = (pos % 2) == 0 ? pos / 2 : (pos - 1) / 2;

                    Location l = center.clone().add(0, 1.5, 0);
                    l.setPitch(0);
                    //if impair place 1 item to the center
                    if (pos % 2 != 0)
                        positions[0] = l.toVector().add(l.getDirection().multiply(radius)).toLocation(center.getWorld());

                    int po = 0;
                    for (int b = 1; b <= n; b++) {
                        for (int g = 1; g <= 2; g++) {
                            int check = g % 2 == 0 ? -1 : 1;
                            int degree = angle_step * check * b;
                            po++;
                            l.setYaw(yaw - (degree / (float) (pos % 2 == 0 ? 1.5 : 1)) * (float) (pos % 2 == 0 && b == 2 ? 1.4 : 1));
                            int i = pos % 2 == 0 ? po - 1 : po;
                            positions[i] = l.toVector().add(l.getDirection().multiply(radius)).toLocation(center.getWorld());
                        }
                    }

                    int nbToDisplay = 0;
                    for (int i = (p * 5); i < maxNumber; i++)
                        nbToDisplay++;

                    int index = 0;
                    Map<Item3D, Location> map = new HashMap<>();
                    for (int i = (p * 5); i < maxNumber; i++) {
                        Item3D it = items.get(i).getItem();
                        map.put(it, positions[indexConverter(nbToDisplay,index)]);

                        index++;
                    }
                    if (pp.getThreeDimensionGUI() != null) {
                        map.keySet().forEach(it -> {
                            Location loc = map.get(it);
                            loc.setDirection(center.toVector().subtract(loc.toVector()).normalize());
                            it.setPosition(loc);
                        });
                    }


                    break;
                } else
                    p++;

            }
        }

    }


    private int indexConverter(int  nbToDisplay, int currentIndex) {
        if(nbToDisplay == 5) {
            if(currentIndex == 0) return 3;
            if(currentIndex == 2) return 0;
            if(currentIndex == 3) return 2;
        } else if(nbToDisplay == 4) {
            if(currentIndex == 0) return 2;
            if(currentIndex == 1) return 0;
            if(currentIndex == 2) return 1;
        } else if(nbToDisplay == 3) {
            if(currentIndex == 1) return 0;
            if(currentIndex == 0) return 1;
        }
        return currentIndex;
    }

    private void switchPage() {

        //Remove malefic items
        getAllItems().stream().filter(item -> items.contains(item)).forEach(item -> {
            if (item.getItem().isSpawned())
                item.getItem().remove();
        });



        //Setup pageNext or not
        if (currentPage >= (int) Math.ceil(items.size() / 5f) - 1) {
            if (nextButton != null && nextButton.isSpawned())
                nextButton.remove();
        } else {
            if (nextButton == null ||  (nextButton != null && !nextButton.isSpawned()))
                addNextPageButton();
        }


        if(currentPage >= 1) {
            if(previousButton == null || (previousButton != null && !previousButton.isSpawned())) {
                addPreviousPageButton();
            }
        } else {
            if(menu.getPreviousMenu() != null) {
                if(previousButton == null || (previousButton != null && !previousButton.isSpawned())) {
                    addPreviousPageButton();
                }
            } else {
                if(previousButton != null && previousButton.isSpawned())
                    previousButton.remove();
            }
        }


        //Cool algo again
        setupItems();


    }

    private void reset() {
        displayName.despawn();
        if (previousButton != null)
            previousButton.remove();
        if (nextButton != null)
            nextButton.remove();

        items.clear();
        allItems.clear();

        pp.setThreeDimensionGUI(null);


    }

    public Location getCenter() {
        return center;
    }

    public void closeGui() {
        if (pp.getThreeDimensionGUI() == null) return;

        getAllItems().forEach(item -> {
            if (item.getItem().isSpawned())
                item.getItem().remove();
        });
        reset();
        this.isSpawned = false;
    }


    public List<InteractableItem> getAllItems() {
        return allItems;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public void setLastSelected(Item3D lastSelected) {
        this.lastSelected = lastSelected;
    }

    public Item3D getLastSelected() {
        return lastSelected;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void addSelected() {
        this.selected++;
    }

    public ThreeDimensionalMenu getMenu() {
        return menu;
    }


}
