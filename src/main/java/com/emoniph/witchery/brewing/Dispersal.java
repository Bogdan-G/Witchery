package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.RitualStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class Dispersal {

   public abstract void onImpactSplashPotion(World var1, NBTTagCompound var2, MovingObjectPosition var3, ModifiersImpact var4);

   public abstract RitualStatus onUpdateRitual(World var1, int var2, int var3, int var4, NBTTagCompound var5, ModifiersRitual var6, ModifiersImpact var7);

   public abstract String getUnlocalizedName();
}
