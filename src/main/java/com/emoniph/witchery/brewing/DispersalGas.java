package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.Dispersal;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.TileEntityBrewFluid;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.BlockPosition;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DispersalGas extends Dispersal {

   public void onImpactSplashPotion(World world, NBTTagCompound nbtBrew, MovingObjectPosition mop, ModifiersImpact modifiers) {
      Coord coord = new Coord(mop, modifiers.impactPosition, true);
      boolean replaceable = BlockUtil.isReplaceableBlock(world, coord.x, coord.y, coord.z, modifiers.thrower);
      if(replaceable) {
         coord.setBlock(world, Witchery.Blocks.BREW_GAS);
         TileEntityBrewFluid gas = (TileEntityBrewFluid)coord.getTileEntity(world, TileEntityBrewFluid.class);
         if(gas != null) {
            gas.initalise(modifiers, nbtBrew);
         }
      }

   }

   public String getUnlocalizedName() {
      return "witchery:brew.dispersal.gas";
   }

   public RitualStatus onUpdateRitual(World world, int x, int y, int z, final NBTTagCompound nbtBrew, final ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      BlockPosition target = modifiers.getTarget();
      World targetWorld = target.getWorld(MinecraftServer.getServer());
      boolean height = true;
      boolean blackMagic = false;
      (new BlockActionCircle() {
         public void onBlock(World world, int x, int y, int z) {
            BlockPosition coords = null;

            for(int player = 0; player < 8; ++player) {
               if(world.getBlock(x, y + player, z).getMaterial() != Material.air && world.isAirBlock(x, y + player + 1, z)) {
                  coords = new BlockPosition(world, x, y, z);
                  break;
               }

               if(player > 0 && world.getBlock(x, y - player, z).getMaterial() != Material.air && world.isAirBlock(x, y - player + 1, z)) {
                  coords = new BlockPosition(world, x, y, z);
                  break;
               }
            }

            if(coords != null) {
               DispersalGas.showSpellParticles(world, coords.x, coords.y, coords.z, false);
               EntityPlayer var11 = EntityUtil.playerOrFake(world, (EntityLivingBase)((EntityPlayer)null));
               ModifiersEffect effectModifiers = new ModifiersEffect(0.0D, 1.0D, false, new EntityPosition(coords), true, modifiers.covenSize, var11);
               if(world.rand.nextDouble() < 0.01D) {
                  WitcheryBrewRegistry.INSTANCE.applyToBlock(world, coords.x, coords.y, coords.z, ForgeDirection.UP, 1, nbtBrew, effectModifiers);
               }

               List entities = EntityUtil.getEntitiesInRadius(EntityLivingBase.class, world, (double)coords.x, (double)coords.y, (double)coords.z, 1.5D);
               Iterator i$ = entities.iterator();

               while(i$.hasNext()) {
                  EntityLivingBase entity = (EntityLivingBase)i$.next();
                  effectModifiers = new ModifiersEffect(1.0D, 1.0D, false, new EntityPosition(coords), true, modifiers.covenSize, var11);
                  WitcheryBrewRegistry.INSTANCE.applyToEntity(world, entity, nbtBrew, effectModifiers);
               }
            }

         }
      }).processHollowCircle(targetWorld, target.x, target.y, target.z, modifiers.pulses);
      return modifiers.pulses < 8 + impactModifiers.extent * 8?RitualStatus.ONGOING:RitualStatus.COMPLETE;
   }

   private static void showSpellParticles(World world, int x, int y, int z, boolean blackMagic) {
      if(blackMagic) {
         ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y + 1), 0.5D + (double)z, 1.0D, 1.0D, 16);
      } else {
         ParticleEffect.SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y + 1), 0.5D + (double)z, 1.0D, 1.0D, 16);
      }

   }
}
