package com.goodbird.npcgecko.utils;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ClientCacheHandler;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.client.renderer.ImageBufferDownloadAlt;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

public class NpcTextureUtils {
    public static ResourceLocation getNpcTexture(EntityNPCInterface npc){
        if (npc.textureLocation == null) {
            if (npc.display.skinType == 0) {
                if (npc instanceof EntityCustomNpc && ((EntityCustomNpc) npc).modelData.entityClass == null) {
                    if (!(npc.display.texture).isEmpty()) {
                        try {
                            npc.textureLocation = adjustLocalTexture(npc, new ResourceLocation(npc.display.texture));
                        } catch (IOException ignored) {
                        }
                    }
                } else {
                    npc.textureLocation = new ResourceLocation(npc.display.texture);
                }
            } else if(RenderNPCInterface.LastTextureTick < 5) { //fixes request flood somewhat
                return AbstractClientPlayer.locationStevePng;
            } else if(npc.display.skinType == 1 && npc.display.playerProfile != null) {
                Minecraft minecraft = Minecraft.getMinecraft();
                Map map = minecraft.func_152342_ad().func_152788_a(npc.display.playerProfile);
                if (map.containsKey(MinecraftProfileTexture.Type.SKIN)){
                    npc.textureLocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                }
                RenderNPCInterface.LastTextureTick = 0;
            } else if (npc.display.skinType == 2 || npc.display.skinType == 3) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    byte[] hash = digest.digest(npc.display.url.getBytes(StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder(2*hash.length);
                    for (byte b : hash) {
                        sb.append(String.format("%02x", b&0xff));
                    }
                    if (npc.display.skinType == 2) {
                        npc.textureLocation = new ResourceLocation("skins/" + sb);
                        ClientCacheHandler.getNPCTexture(npc.display.url, false, npc.textureLocation);
                    } else {
                        npc.textureLocation = new ResourceLocation("skins64/" + sb);
                        ClientCacheHandler.getNPCTexture(npc.display.url, true, npc.textureLocation);
                    }
                    RenderNPCInterface.LastTextureTick = 0;
                } catch(Exception ignored){}
            } else {
                return AbstractClientPlayer.locationStevePng;
            }
        }
        return npc.textureLocation;
    }

    private static ResourceLocation adjustLocalTexture(EntityNPCInterface npc, ResourceLocation location) throws IOException {
        InputStream inputstream = null;

        ResourceLocation var22;
        try {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            texturemanager.deleteTexture(location);
            IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(location);
            inputstream = iresource.getInputStream();
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            int totalWidth = bufferedimage.getWidth();
            int totalHeight = bufferedimage.getHeight();
            if (totalHeight > 32 && npc.display.modelType == 0) {
                bufferedimage = bufferedimage.getSubimage(0, 0, totalWidth, 32);
            }

            ImageDownloadAlt object = new ImageDownloadAlt((File)null, npc.display.texture, SkinManager.field_152793_a, new ImageBufferDownloadAlt(false));
            object.setBufferedImage(bufferedimage);

            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] hash = digest.digest(npc.display.texture.getBytes("UTF-8"));
                StringBuilder sb = new StringBuilder(2 * hash.length);
                byte[] var13 = hash;
                int var14 = hash.length;

                for(int var15 = 0; var15 < var14; ++var15) {
                    byte b = var13[var15];
                    sb.append(String.format("%02x", b & 255));
                }

                if (totalHeight > 32 && npc.display.modelType == 0) {
                    location = new ResourceLocation("skin/" + sb.toString());
                } else {
                    location = new ResourceLocation("skin64/" + sb.toString());
                }
            } catch (Exception var20) {
            }

            texturemanager.loadTexture(location, object);
            var22 = location;
        } finally {
            if (inputstream != null) {
                inputstream.close();
            }

        }

        return var22;
    }

}
