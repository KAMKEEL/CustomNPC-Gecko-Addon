package com.goodbird.npcgecko.data;

/**
 * Holds per-context display transforms for a gecko item model.
 * Loaded from JSON files in assets/{domain}/item_displays/.
 * <p>
 * JSON format:
 * <pre>
 * {
 *   "model": "npcgecko:geo/sword.geo.json",
 *   "display": {
 *     "first_person":  { "translation": [x,y,z], "rotation": [x,y,z], "scale": [x,y,z] },
 *     "third_person":  { "translation": [x,y,z], "rotation": [x,y,z], "scale": [x,y,z] },
 *     "inventory":     { "translation": [x,y,z], "rotation": [x,y,z], "scale": [x,y,z] },
 *     "ground":        { "translation": [x,y,z], "rotation": [x,y,z], "scale": [x,y,z] }
 *   }
 * }
 * </pre>
 */
public class ItemDisplayData {
    private ItemDisplayTransform firstPerson;
    private ItemDisplayTransform thirdPerson;
    private ItemDisplayTransform inventory;
    private ItemDisplayTransform ground;

    public ItemDisplayTransform getFirstPerson() { return firstPerson; }
    public void setFirstPerson(ItemDisplayTransform t) { this.firstPerson = t; }

    public ItemDisplayTransform getThirdPerson() { return thirdPerson; }
    public void setThirdPerson(ItemDisplayTransform t) { this.thirdPerson = t; }

    public ItemDisplayTransform getInventory() { return inventory; }
    public void setInventory(ItemDisplayTransform t) { this.inventory = t; }

    public ItemDisplayTransform getGround() { return ground; }
    public void setGround(ItemDisplayTransform t) { this.ground = t; }
}
