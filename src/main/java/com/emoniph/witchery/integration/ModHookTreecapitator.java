package com.emoniph.witchery.integration;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.integration.ModHook;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ModHookTreecapitator extends ModHook {

   public String getModID() {
      return "TreeCapitator";
   }

   protected void doInit() {
      NBTTagCompound tpModCfg = new NBTTagCompound();
      tpModCfg.setString("modID", "witchery");
      NBTTagList treeList = new NBTTagList();
      NBTTagCompound treeDef = new NBTTagCompound();
      treeDef.setString("treeName", "rowan");
      String logName = GameData.getBlockRegistry().getNameForObject(Witchery.Blocks.LOG);
      String leafName = GameData.getBlockRegistry().getNameForObject(Witchery.Blocks.LEAVES);
      treeDef.setString("logs", String.format("%s,0; %s,4; %s,8", new Object[]{logName, logName, logName}));
      treeDef.setString("leaves", String.format("%s,0", new Object[]{leafName}));
      treeList.appendTag(treeDef);
      treeDef = new NBTTagCompound();
      treeDef.setString("treeName", "alder");
      treeDef.setString("logs", String.format("%s,1; %s,5; %s,9", new Object[]{logName, logName, logName}));
      treeDef.setString("leaves", String.format("%s,1", new Object[]{leafName}));
      treeList.appendTag(treeDef);
      treeDef = new NBTTagCompound();
      treeDef.setString("treeName", "hawthorn");
      treeDef.setString("logs", String.format("%s,2; %s,6; %s,10", new Object[]{logName, logName, logName}));
      treeDef.setString("leaves", String.format("%s,2", new Object[]{leafName}));
      treeList.appendTag(treeDef);
      tpModCfg.setTag("trees", treeList);
      FMLInterModComms.sendMessage(this.getModID(), "ThirdPartyModConfig", tpModCfg);
   }

   protected void doPostInit() {}

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {}
}
