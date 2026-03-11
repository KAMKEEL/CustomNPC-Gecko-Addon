package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoBone;
import com.goodbird.npcgecko.api.IGeckoModel;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around GeckoLib's {@link GeoModel} that implements {@link IGeckoModel}.
 */
public class GeckoModelWrapper implements IGeckoModel {
    private final GeoModel model;

    public GeckoModelWrapper(GeoModel model) {
        this.model = model;
    }

    @Override
    public IGeckoBone getBone(String name) {
        if (model.getBone(name).isPresent()) {
            return new GeckoBoneWrapper(model.getBone(name).get());
        }
        return null;
    }

    @Override
    public String[] getBoneNames() {
        List<String> names = new ArrayList<>();
        collectBoneNames(model.topLevelBones, names);
        return names.toArray(new String[0]);
    }

    private void collectBoneNames(List<GeoBone> bones, List<String> names) {
        for (GeoBone bone : bones) {
            names.add(bone.name);
            if (bone.childBones != null && !bone.childBones.isEmpty()) {
                collectBoneNames(bone.childBones, names);
            }
        }
    }
}
