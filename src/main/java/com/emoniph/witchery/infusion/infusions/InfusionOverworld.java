package com.emoniph.witchery.infusion.infusions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ServerTickEvents;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EarthItems;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class InfusionOverworld extends Infusion {

   public InfusionOverworld(int infusionID) {
      super(infusionID);
   }

   public IIcon getPowerBarIcon(EntityPlayer player, int index) {
      return Blocks.dirt.getIcon(0, 0);
   }

   public void onFalling(World world, EntityPlayer player, LivingFallEvent event) {
      if(event.distance > 3.0F) {
         int blockX = MathHelper.floor_double(player.posX);
         int blockY = MathHelper.floor_double(player.posY) - 1;
         int blockZ = MathHelper.floor_double(player.posZ);
         Block blockID = world.getBlock(blockX, blockY, blockZ);
         if(blockID == Blocks.grass || blockID == Blocks.grass || blockID == Blocks.mycelium || blockID == Blocks.gravel || blockID == Blocks.sand || blockID == Blocks.snow) {
            if(player.isSneaking()) {
               if(this.consumeCharges(world, player, 10, true)) {
                  event.distance = 0.0F;
                  boolean itemstack = true;
                  world.createExplosion(player, player.posX, (double)blockY + 0.5D, player.posZ, 3.0F, true);
               }
            } else if(this.consumeCharges(world, player, 5, true)) {
               event.distance = 0.0F;
               world.setBlockToAir(blockX, blockY, blockZ);
               ItemStack itemstack1 = new ItemStack(blockID, 1, 0);
               EntityItem blockEntity = new EntityItem(world, (double)blockX, (double)blockY, (double)blockZ, itemstack1);
               world.spawnEntityInWorld(blockEntity);
            }
         }
      }

   }

   public void onLeftClickEntity(ItemStack itemstack, World world, EntityPlayer player, Entity otherEntity) {
      if(!world.isRemote) {
         if(otherEntity instanceof EntityLivingBase) {
            EntityLivingBase otherLivingEntity = (EntityLivingBase)otherEntity;
            int posX = MathHelper.floor_double(player.posX);
            int posY = MathHelper.floor_double(player.posY);
            int posZ = MathHelper.floor_double(player.posZ);
            boolean isWearingMetalArmour = false;

            for(int ACCELERATION = 0; ACCELERATION < 5; ++ACCELERATION) {
               ItemStack heldStack = otherLivingEntity.getEquipmentInSlot(ACCELERATION);
               if(EarthItems.instance().isMatch(heldStack)) {
                  isWearingMetalArmour = true;
                  break;
               }
            }

            if(isWearingMetalArmour) {
               double var20 = 3.0D;
               Vec3 look;
               double motionX;
               double motionY;
               double motionZ;
               EntityPlayer targetPlayer;
               if(player.isSneaking()) {
                  if(this.consumeCharges(world, player, 4, true)) {
                     look = player.getLookVec();
                     motionX = look.xCoord * 0.8D * 3.0D;
                     motionY = 1.5D;
                     motionZ = look.zCoord * 0.8D * 3.0D;
                     if(otherLivingEntity instanceof EntityPlayer) {
                        targetPlayer = (EntityPlayer)otherLivingEntity;
                        Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(motionX, 1.5D, motionZ)), targetPlayer);
                     } else {
                        otherLivingEntity.motionX = motionX;
                        otherLivingEntity.motionY = 1.5D;
                        otherLivingEntity.motionZ = motionZ;
                     }
                  }
               } else if(this.consumeCharges(world, player, 2, true)) {
                  look = player.getLookVec();
                  motionX = look.xCoord * 0.8D * 3.0D;
                  motionY = 0.30000000000000004D;
                  motionZ = look.zCoord * 0.8D * 3.0D;
                  if(otherLivingEntity instanceof EntityPlayer) {
                     targetPlayer = (EntityPlayer)otherLivingEntity;
                     Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(motionX, 0.30000000000000004D, motionZ)), targetPlayer);
                  } else {
                     otherLivingEntity.motionX = motionX;
                     otherLivingEntity.motionY = 0.30000000000000004D;
                     otherLivingEntity.motionZ = motionZ;
                  }
               }
            }
         }

      }
   }

   public void onUsingItemTick(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         int seconds = elapsedTicks / 20;
         if(player.isSneaking()) {
            if(seconds >= 2 && elapsedTicks % 4 == 0 && this.consumeCharges(world, player, 1, true)) {
               boolean AreaOfEffect = true;
               List entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(player.posX - 6.0D, player.posY - 6.0D, player.posZ - 6.0D, player.posX + 6.0D, player.posY + 6.0D, player.posZ + 6.0D));

               for(int AreaOfEffect2 = 0; AreaOfEffect2 < entities.size(); ++AreaOfEffect2) {
                  EntityItem x = (EntityItem)entities.get(AreaOfEffect2);
                  if(EarthItems.instance().isMatch(x.getEntityItem())) {
                     double y = 8.0D;
                     double id = 0.0D;
                     double motionY = 0.0D;
                     double motionZ = 0.0D;
                     double d1 = (player.posX - x.posX) / y;
                     double d2 = (player.posY + (double)player.getEyeHeight() - x.posY) / y;
                     double d3 = (player.posZ - x.posZ) / y;
                     Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                     double d5 = 2.0D;
                     if(d5 > 0.0D) {
                        d5 *= d5;
                        id += d1 / Math.max(Math.abs(d1), 0.0D) * 1.0D;
                        motionY += d2 / Math.max(Math.abs(d1), 0.0D) * 1.0D;
                        motionZ += d3 / Math.max(Math.abs(d1), 0.0D) * 1.0D;
                     }

                     boolean oldClip = x.noClip;
                     x.noClip = true;
                     x.moveEntity(id, motionY, motionZ);
                     x.noClip = oldClip;
                  }
               }

               boolean var30 = true;

               for(int var32 = (int)player.posX - 6; var32 <= (int)player.posX + 6; ++var32) {
                  for(int var31 = (int)player.posY - 3; var31 <= (int)player.posY + 3; ++var31) {
                     for(int z = (int)player.posZ - 6; z <= (int)player.posZ + 6; ++z) {
                        Block var33 = world.getBlock(var32, var31, z);
                        if(var33 != Blocks.air) {
                           Item ingot = EarthItems.instance().oreToIngot(var33);
                           if(ingot != null && !world.isRemote && this.consumeCharges(world, player, 2, true)) {
                              world.setBlock(var32, var31, z, Blocks.stone, 0, 3);
                              world.spawnEntityInWorld(new EntityItem(world, (double)var32, (double)var31, (double)z, new ItemStack(ingot)));
                           }
                        }
                     }
                  }
               }
            }
         } else if(seconds >= 2 && elapsedTicks % 20 == 0) {
            this.playSound(world, player, "random.orb");
         }
      }

   }

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         MovingObjectPosition hit = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0D);
         if(hit != null) {
            switch(InfusionOverworld.NamelessClass1890422574.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[hit.typeOfHit.ordinal()]) {
            case 1:
               if(!player.isSneaking() && hit.entityHit instanceof EntityLiving && this.consumeCharges(world, player, 2, true)) {
                  EntityLiving var16 = (EntityLiving)hit.entityHit;
                  ItemStack var21 = var16.getHeldItem();
                  if(var21 != null && EarthItems.instance().isMatch(var21) && !world.isRemote) {
                     var16.entityDropItem(var21, 2.0F);
                     var16.setCurrentItemOrArmor(0, (ItemStack)null);
                  }

                  return;
               }
               break;
            case 2:
               boolean seconds = true;
               if(!player.isSneaking() && BlockSide.TOP.isEqual(hit.sideHit) && world.getBlock(hit.blockX, hit.blockY - 9 - 1, hit.blockZ).getMaterial().isSolid() && this.consumeCharges(world, player, 2, true)) {
                  for(int var18 = 0; var18 < 6; ++var18) {
                     int var20 = hit.blockY - var18;
                     Block blockID1 = world.getBlock(hit.blockX, var20, hit.blockZ);
                     if(BlockProtect.canBreak(blockID1, world)) {
                        int blockMetadata = world.getBlockMetadata(hit.blockX, var20, hit.blockZ);
                        world.setBlockToAir(hit.blockX, var20, hit.blockZ);
                        if(BlockProtect.canBreak(hit.blockX, var20 + 3, hit.blockZ, world)) {
                           world.setBlock(hit.blockX, var20 + 3, hit.blockZ, blockID1, blockMetadata, 3);
                        }

                        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)hit.blockX, (double)hit.blockY, (double)hit.blockZ, (double)(hit.blockX + 1), (double)(hit.blockY + 2), (double)(hit.blockZ + 1));
                        Iterator i$ = world.getEntitiesWithinAABB(Entity.class, bounds).iterator();

                        while(i$.hasNext()) {
                           Object obj = i$.next();
                           Entity entity = (Entity)obj;
                           if(entity instanceof EntityLivingBase) {
                              ((EntityLivingBase)entity).setPositionAndUpdate(entity.posX, entity.posY + 3.0D, entity.posZ);
                           } else {
                              entity.setPosition(entity.posX, entity.posY + 3.0D, entity.posZ);
                           }
                        }
                     }
                  }
               } else if(!player.isSneaking() && !BlockSide.BOTTOM.isEqual(hit.sideHit) && !BlockSide.TOP.isEqual(hit.sideHit)) {
                  if(this.isThrowableRock(world, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit) && this.consumeCharges(world, player, 3, true)) {
                     world.setBlockToAir(hit.blockX, hit.blockY, hit.blockZ);
                     ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_EXPLODE, world, (double)hit.blockX, (double)hit.blockY, (double)hit.blockZ, 0.5D, 0.5D, 8);
                     EntityWitchProjectile var19 = new EntityWitchProjectile(world, player, Witchery.Items.GENERIC.itemRock);
                     var19.setPosition((double)hit.blockX + 0.5D, (double)hit.blockY + 0.5D, (double)hit.blockZ + 0.5D);
                     world.spawnEntityInWorld(var19);
                  }
               } else if(player.isSneaking() && this.consumeCharges(world, player, 2, true)) {
                  Block blockID = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);
                  Item ingot = EarthItems.instance().oreToIngot(blockID);
                  if(ingot != null) {
                     world.setBlock(hit.blockX, hit.blockY, hit.blockZ, Blocks.stone, 0, 3);
                     if(!world.isRemote) {
                        world.spawnEntityInWorld(new EntityItem(world, (double)hit.blockX, (double)hit.blockY, (double)hit.blockZ, new ItemStack(ingot, 2, 0)));
                     }
                  }
               }

               return;
            case 3:
            }
         }

         int var17 = elapsedTicks / 20;
         if(var17 >= 2 && !player.isSneaking() && this.consumeCharges(world, player, 6 * var17, true)) {
            ServerTickEvents.TASKS.add(new InfusionOverworld.ShockwaveTask(player, 2 * var17));
         } else {
            this.playFailSound(world, player);
         }

      }
   }

   private boolean isThrowableRock(World world, int blockX, int blockY, int blockZ, int sideHit) {
      Block[] blocks = new Block[]{Blocks.dirt, Blocks.grass, Blocks.stone, Blocks.cobblestone, Blocks.sand, Blocks.gravel, Blocks.sandstone, Blocks.stone_slab, Blocks.brick_block, Blocks.mossy_cobblestone, Blocks.grass, Blocks.stone_stairs, Blocks.clay, Blocks.soul_sand, Blocks.stonebrick, Blocks.brick_stairs, Blocks.stone_brick_stairs, Blocks.mycelium, Blocks.nether_brick, Blocks.nether_brick_stairs, Blocks.sandstone_stairs, Blocks.hardened_clay, Blocks.coal_block, Blocks.netherrack};
      Block blockID = world.getBlock(blockX, blockY, blockZ);
      if(!Arrays.asList(blocks).contains(blockID)) {
         return false;
      } else {
         boolean northValid = BlockSide.NORTH.isEqual(sideHit) && !world.getBlock(blockX + 1, blockY, blockZ).getMaterial().isSolid();
         boolean southValid = BlockSide.SOUTH.isEqual(sideHit) && !world.getBlock(blockX - 1, blockY, blockZ).getMaterial().isSolid();
         boolean eastValid = BlockSide.EAST.isEqual(sideHit) && !world.getBlock(blockX, blockY, blockZ + 1).getMaterial().isSolid();
         boolean westValid = BlockSide.WEST.isEqual(sideHit) && !world.getBlock(blockX, blockY, blockZ - 1).getMaterial().isSolid();
         return northValid || southValid || eastValid || westValid;
      }
   }

   private static class ShockwaveTask extends ServerTickEvents.ServerTickTask {

      final Coord center;
      final EntityPlayer creator;
      final int maxRadius;
      final int MIN_RADIUS = 2;
      int stage = 0;


      public ShockwaveTask(EntityPlayer creator, int maxRadius) {
         super(creator.worldObj);
         this.center = new Coord((int)creator.posX, (int)creator.posY - 1, (int)creator.posZ);
         this.creator = creator;
         this.maxRadius = maxRadius + 2;
      }

      public boolean process() {
         ++this.stage;
         Block centerBlock = this.center.getBlock(super.world);
         if(this.stage == 1) {
            this.drawCircle(super.world, this.center.x, this.center.y, this.center.z, this.stage + 2, 2, 1);
         } else {
            this.drawCircle(super.world, this.center.x, this.center.y + 2, this.center.z, this.stage + 2, 2, -1);
            this.drawCircle(super.world, this.center.x, this.center.y + 1, this.center.z, this.stage + 2 - 1, 2, -1);
         }

         if(this.stage < this.maxRadius) {
            this.drawCircle(super.world, this.center.x, this.center.y, this.center.z, this.stage + 2 + 1, 2, 2);
         } else {
            this.drawCircle(super.world, this.center.x, this.center.y + 1, this.center.z, this.stage + 2, 2, -1);
         }

         int r = this.stage + 2;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(this.center.x - r), (double)(this.center.y + 1), (double)(this.center.z - r), (double)(this.center.x + r), (double)(this.center.y + 3), (double)(this.center.z + r));
         Iterator i$ = super.world.getEntitiesWithinAABB(EntityLivingBase.class, bounds).iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityLivingBase entity = (EntityLivingBase)obj;
            Coord position = new Coord(entity);
            double dist = this.center.distanceTo(position);
            if(dist <= (double)(r + 1) && dist >= (double)r) {
               entity.attackEntityFrom(DamageSource.causePlayerDamage(this.creator), 8.0F);
               RiteProtectionCircleRepulsive.push(super.world, entity, (double)this.center.x, (double)this.center.y, (double)this.center.z);
            }
         }

         return this.stage == this.maxRadius;
      }

      protected void drawCircle(World world, int x0, int y0, int z0, int radius, int blocksToMove, int direction) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawPixel(world, x + x0, y0, z + z0, blocksToMove, direction);
            this.drawPixel(world, z + x0, y0, x + z0, blocksToMove, direction);
            this.drawPixel(world, -x + x0, y0, z + z0, blocksToMove, direction);
            this.drawPixel(world, -z + x0, y0, x + z0, blocksToMove, direction);
            this.drawPixel(world, -x + x0, y0, -z + z0, blocksToMove, direction);
            this.drawPixel(world, -z + x0, y0, -x + z0, blocksToMove, direction);
            this.drawPixel(world, x + x0, y0, -z + z0, blocksToMove, direction);
            this.drawPixel(world, z + x0, y0, -x + z0, blocksToMove, direction);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawPixel(World world, int x, int y, int z, int blocksToMove, int direction) {
         int i;
         Block blockID;
         int blockMetadata;
         if(direction > 0) {
            if(world.isAirBlock(x, y - blocksToMove + 1, z) || world.getBlock(x, y + 1, z).getMaterial().isSolid()) {
               return;
            }

            for(i = 0; i < blocksToMove; ++i) {
               blockID = world.getBlock(x, y - i, z);
               blockMetadata = world.getBlockMetadata(x, y - i, z);
               if(BlockProtect.canBreak(blockID, world)) {
                  world.setBlockToAir(x, y - i, z);
               }

               if(BlockProtect.canBreak(x, y - i + direction, z, world)) {
                  world.setBlock(x, y - i + direction, z, blockID, blockMetadata, 3);
               }
            }
         } else {
            if(world.isAirBlock(x, y, z) || world.getBlock(x, y + direction - 1, z).getMaterial().isSolid()) {
               return;
            }

            for(i = blocksToMove - 1; i >= 0; --i) {
               blockID = world.getBlock(x, y - i, z);
               blockMetadata = world.getBlockMetadata(x, y - i, z);
               if(BlockProtect.canBreak(blockID, world)) {
                  world.setBlockToAir(x, y - i, z);
               }

               if(BlockProtect.canBreak(x, y - i + direction, z, world)) {
                  world.setBlock(x, y - i + direction, z, blockID, blockMetadata, 3);
               }
            }
         }

      }
   }

   // $FF: synthetic class
   static class NamelessClass1890422574 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
