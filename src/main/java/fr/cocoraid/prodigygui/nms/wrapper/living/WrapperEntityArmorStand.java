package fr.cocoraid.prodigygui.nms.wrapper.living;

import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WrapperEntityArmorStand extends WrappedEntityLiving {

    private boolean small = false;
    private boolean noBasePlate = true;
    private boolean marker = false;

    private static int armorIndex = VersionChecker.isLowerOrEqualThan(VersionChecker.v1_9_R2) ? 10 : 11;
    private static int headPosIndex = VersionChecker.isLowerOrEqualThan(VersionChecker.v1_9_R2) ? 11 : 12;

    private static byte markerMask = (byte) (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R2) ? 0x16 : 0x10);


    //current 1.14.2
    private static int id = 1;
    static {
      if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_12_R1)) {
            id = 30;
        }

    }



    public WrapperEntityArmorStand(Location location, Player player) {
        super(location,player, id);
    }

    /**
     * Parameters in degree
     * @param x
     * @param y
     * @param z
     */
    public void setHeadPose(float x, float y, float z) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getVectorSerializer();
        getDataWatcher().setObject(headPosIndex,serializer,new Vector3F(x,y,z));
    }

    public void setSmall(boolean small) {
        this.small = small;
        setDataWatcherObject(Byte.class,armorIndex, (byte) ((small ? 0x01 : 0) | (noBasePlate ? 0x08 : 0) | (marker ? markerMask : 0)));
    }

    /**
     * //isSmall, noBasePlate, set Marker
     * (0x01 | 0x08 | 0x10)
     * @param marker
     */

    public void setMarker(boolean marker) {
        this.marker = marker;
        setDataWatcherObject(Byte.class,armorIndex, (byte) ((small ? 0x01 : 0) | (noBasePlate ? 0x08 : 0) | (marker ? markerMask : 0)));
    }

    public void setNoBasePlate(boolean noBasePlate) {
        this.noBasePlate = noBasePlate;
       setDataWatcherObject(Byte.class,armorIndex, (byte) ((small ? 0x01 : 0) | (noBasePlate ? 0x08 : 0) | (marker ? markerMask : 0)));
    }
}
