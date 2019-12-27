package fr.cocoraid.prodigygui.nms.wrapper.living;

import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WrapperEntitySlime extends WrappedEntityLiving {

    //current 1.14.2
    private static int id = 68;
   static {
        if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_12_R1)) {
            id = 55;
        } else if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_13_R2)) {
            id = 64;
        } else if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_14_R1)) {
            id = 67;
        }

    }

    public WrapperEntitySlime(Location location, Player player) {
        super(location,player, id);

    }



    public void setSize(int size) {
            setDataWatcherObject(Integer.class, 12, size); //this code is working fine with 1.13.2
    }


}
