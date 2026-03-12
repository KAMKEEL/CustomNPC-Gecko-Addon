package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.data.ItemDisplayData;
import com.goodbird.npcgecko.data.ItemDisplayTransform;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.file.ItemDisplayFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads item display data from GeckoLibCache (which handles loading from
 * resource packs and server-to-client syncing).
 *
 * <p>Display JSONs are looked up by filename (e.g. "sword.json"), which is
 * explicitly assigned to items via {@code GeckoAPI.setDisplayJSON(item, "sword.json")}.</p>
 */
public class ItemDisplayLoader {
    private static final ItemDisplayLoader INSTANCE = new ItemDisplayLoader();

    public static ItemDisplayLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Look up display data by filename (e.g. "sword.json").
     * Searches all loaded item display files in GeckoLibCache for a matching filename.
     */
    public ItemDisplayData getDisplayData(String filename) {
        if (filename == null || filename.isEmpty()) return null;

        HashMap<ResourceLocation, ItemDisplayFile> displays = GeckoLibCache.getInstance().getItemDisplays();
        for (Map.Entry<ResourceLocation, ItemDisplayFile> entry : displays.entrySet()) {
            String path = entry.getKey().getResourcePath();
            String entryFilename = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
            if (entryFilename.equals(filename)) {
                return convert(entry.getValue());
            }
        }
        return null;
    }

    /**
     * Get all loaded display file names.
     */
    public String[] getDisplayFileList() {
        HashMap<ResourceLocation, ItemDisplayFile> displays = GeckoLibCache.getInstance().getItemDisplays();
        List<String> names = new ArrayList<>();
        for (ResourceLocation loc : displays.keySet()) {
            String path = loc.getResourcePath();
            String filename = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
            names.add(filename);
        }
        return names.toArray(new String[0]);
    }

    /**
     * Convert GeckoLib's ItemDisplayFile to the addon's ItemDisplayData.
     */
    private ItemDisplayData convert(ItemDisplayFile file) {
        ItemDisplayData data = new ItemDisplayData();

        if (file.hasFirstPerson()) {
            data.setFirstPerson(convertTransform(
                file.getFirstPersonTranslation(),
                file.getFirstPersonRotation(),
                file.getFirstPersonScale()));
        }
        if (file.hasThirdPerson()) {
            data.setThirdPerson(convertTransform(
                file.getThirdPersonTranslation(),
                file.getThirdPersonRotation(),
                file.getThirdPersonScale()));
        }
        if (file.hasInventory()) {
            data.setInventory(convertTransform(
                file.getInventoryTranslation(),
                file.getInventoryRotation(),
                file.getInventoryScale()));
        }
        if (file.hasGround()) {
            data.setGround(convertTransform(
                file.getGroundTranslation(),
                file.getGroundRotation(),
                file.getGroundScale()));
        }

        return data;
    }

    /**
     * Convert Blockbench/modern MC display values to 1.7.10 GL units.
     * - Translation: multiplied by 0.0625 (÷16) matching modern MC deserialization
     * - Rotation Y: offset by -90° to cancel GeoItemStackRenderer's built-in rotate(90, Y)
     * - Scale: used directly
     */
    private ItemDisplayTransform convertTransform(float[] translation, float[] rotation, float[] scale) {
        ItemDisplayTransform t = new ItemDisplayTransform();
        if (translation != null) {
            t.setTranslation(
                translation[0] * 0.0625F,
                translation[1] * 0.0625F,
                translation[2] * 0.0625F);
        }
        if (rotation != null) {
            t.setRotation(rotation[0], rotation[1], rotation[2]);
        }
        if (scale != null) {
            t.setScale(scale[0], scale[1], scale[2]);
        }
        return t;
    }
}
