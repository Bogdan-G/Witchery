package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBrazier;
import com.emoniph.witchery.blocks.BlockWickerBundle;
import com.emoniph.witchery.blocks.BlockWitchDoor;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.brewing.potions.PotionIllFitting;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.entity.EntityDarkMark;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionLight;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.item.ItemChalk;
import com.emoniph.witchery.item.ItemLeonardsUrn;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.DemonicDamageSource;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.InvUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class EffectRegistry {

   private static final EffectRegistry INSTANCE = new EffectRegistry();
   private Hashtable effects = new Hashtable();
   private Hashtable enhanced = new Hashtable();
   private Hashtable effectID = new Hashtable();
   private ArrayList allEffects = new ArrayList();
   public static final SymbolEffect Accio = instance().addEffect((new SymbolEffectProjectile(1, "witchery.pott.accio") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         if(caster != null && mop != null) {
            double R = spell.getEffectLevel() == 1?0.8D:(spell.getEffectLevel() == 2?3.0D:9.0D);
            double R_SQ = R * R;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(spell.posX - R, spell.posY - R, spell.posZ - R, spell.posX + R, spell.posY + R, spell.posZ + R);
            List entities = world.getEntitiesWithinAABB(EntityItem.class, bb);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityItem item = (EntityItem)obj;
               if(item.getDistanceSqToEntity(spell) <= R_SQ) {
                  item.setPosition(caster.posX, caster.posY + 1.0D, caster.posZ);
               }
            }
         }

      }
   }).setColor(5322534).setSize(1.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)3, (byte)0, (byte)2, (byte)2, (byte)1}), new StrokeSet(1, new byte[]{(byte)3, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1}), new StrokeSet(2, new byte[]{(byte)3, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)1}), new StrokeSet(2, new byte[]{(byte)3, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)3, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)3, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect Aguamenti = instance().addEffect((new SymbolEffectProjectile(2, "witchery.pott.aguamenti") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         int dy;
         int dz;
         int x;
         if((spell.getEffectLevel() != 1 || world.provider.isHellWorld) && (!world.provider.isHellWorld || spell.getEffectLevel() != 3)) {
            if(!world.provider.isHellWorld) {
               int dx1;
               if(mop.typeOfHit == MovingObjectType.ENTITY) {
                  dx1 = MathHelper.floor_double(mop.entityHit.posX);
                  dy = MathHelper.floor_double(mop.entityHit.posY);
                  dz = MathHelper.floor_double(mop.entityHit.posZ);
                  this.setBlock(caster, world, dx1, dy, dz, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1, dy + 1, dz, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1 + 1, dy, dz, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1 - 1, dy, dz, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1, dy, dz + 1, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1, dy, dz - 1, Blocks.flowing_water);
                  this.setIfAir(caster, world, dx1, dy - 1, dz, Blocks.flowing_water);
               } else {
                  dx1 = mop.sideHit == 5?1:(mop.sideHit == 4?-1:0);
                  dy = mop.sideHit == 0?-1:(mop.sideHit == 1?1:0);
                  dz = mop.sideHit == 3?1:(mop.sideHit == 2?-1:0);
                  x = mop.blockX + dx1;
                  int y = mop.blockY + dy + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1?-1:0);
                  int z = mop.blockZ + dz;
                  this.setBlock(caster, world, x, y, z, Blocks.flowing_water);
                  this.setIfAir(caster, world, x, y + 1, z, Blocks.flowing_water);
                  this.setIfAir(caster, world, x + 1, y, z, Blocks.flowing_water);
                  this.setIfAir(caster, world, x - 1, y, z, Blocks.flowing_water);
                  this.setIfAir(caster, world, x, y, z + 1, Blocks.flowing_water);
                  this.setIfAir(caster, world, x, y, z - 1, Blocks.flowing_water);
                  this.setIfAir(caster, world, x, y - 1, z, Blocks.flowing_water);
               }
            }
         } else if(mop.typeOfHit == MovingObjectType.ENTITY) {
            this.setBlock(caster, world, MathHelper.floor_double(mop.entityHit.posX), MathHelper.floor_double(mop.entityHit.posY), MathHelper.floor_double(mop.entityHit.posZ), Blocks.flowing_water);
         } else if(mop.typeOfHit == MovingObjectType.BLOCK) {
            Block dx = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if(dx == Witchery.Blocks.CAULDRON) {
               if(Witchery.Blocks.CAULDRON.tryFillWith(world, mop.blockX, mop.blockY, mop.blockZ, new FluidStack(FluidRegistry.WATER, 3000))) {
                  ;
               }
            } else if(dx == Witchery.Blocks.KETTLE) {
               if(Witchery.Blocks.KETTLE.tryFillWith(world, mop.blockX, mop.blockY, mop.blockZ, new FluidStack(FluidRegistry.WATER, 1000))) {
                  ;
               }
            } else {
               dy = mop.sideHit == 5?1:(mop.sideHit == 4?-1:0);
               dz = mop.sideHit == 0?-1:(mop.sideHit == 1?1:0);
               x = mop.sideHit == 3?1:(mop.sideHit == 2?-1:0);
               this.setBlock(caster, world, mop.blockX + dy, mop.blockY + dz + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1?-1:0), mop.blockZ + x, Blocks.flowing_water);
            }
         }

      }
      private void setBlock(EntityLivingBase caster, World world, int x, int y, int z, Block block) {
         if(BlockProtect.checkModsForBreakOK(world, x, y, z, caster)) {
            world.setBlock(x, y, z, block);
         }

      }
      private void setIfAir(EntityLivingBase caster, World world, int x, int y, int z, Block block) {
         if(world.isAirBlock(x, y, z)) {
            this.setBlock(caster, world, x, y, z, block);
         }

      }
   }).setColor(1176575).setSize(2.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)2, (byte)2, (byte)1}), new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)1}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect Alohomora = instance().addEffect((new SymbolEffectProjectile(3, "witchery.pott.alohomora") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            Block blockID = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if(blockID != Witchery.Blocks.DOOR_ALDER && blockID != Witchery.Blocks.DOOR_ROWAN) {
               if(blockID instanceof BlockDoor) {
                  ((BlockDoor)blockID).func_150014_a(world, mop.blockX, mop.blockY, mop.blockZ, !((BlockDoor)blockID).func_150015_f(world, mop.blockX, mop.blockY, mop.blockZ));
               }
            } else {
               ((BlockWitchDoor)blockID).onBlockActivatedNormally(world, mop.blockX, mop.blockY, mop.blockZ, (EntityPlayer)null, 1, (float)mop.blockX, (float)mop.blockY, (float)mop.blockZ);
            }
         }

      }
   }).setColor(5322534).setSize(0.5F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)2, (byte)2, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2, (byte)1, (byte)1})});
   public static final SymbolEffect AvadaKedavra = instance().addEffect((new SymbolEffectProjectile(4, "witchery.pott.avadakedavra", 101, true, false, (String)null, 0, false) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop != null && caster != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
            if(mop.entityHit instanceof EntityPlayer) {
               if(world.isRemote || !(caster instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled()) {
                  EntityPlayer hitCreature = (EntityPlayer)mop.entityHit;
                  EntityUtil.instantDeath(hitCreature, caster);
               }
            } else if(mop.entityHit instanceof EntityLiving) {
               EntityLiving hitCreature1 = (EntityLiving)mop.entityHit;
               if(caster instanceof EntityPlayer && ((EntityPlayer)caster).capabilities.isCreativeMode) {
                  EntityUtil.instantDeath(hitCreature1, caster);
               } else if((PotionEnslaved.canCreatureBeEnslaved(hitCreature1) || hitCreature1 instanceof EntityWitch || hitCreature1 instanceof EntityEnt || hitCreature1 instanceof EntityGolem) && hitCreature1.getMaxHealth() <= 200.0F) {
                  hitCreature1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(effectEntity, caster), 200.0F);
               } else {
                  hitCreature1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(effectEntity, caster), 25.0F);
               }
            }
         }

      }
   }).setColor('\uff00').setSize(2.0F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)2, (byte)2, (byte)0, (byte)0, (byte)3, (byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2})});
   public static final SymbolEffect CaveInimicum = instance().addEffect((new SymbolEffectProjectile(5, "witchery.pott.caveinimicum") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, effectEntity.getEffectLevel(), new EffectRegistry.IBlockEffect() {
               public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                  Block newBlockID = Blocks.air;
                  if(block == Blocks.dirt) {
                     newBlockID = Blocks.stone;
                  } else if(block == Blocks.grass) {
                     newBlockID = Blocks.stone;
                  } else if(block == Blocks.mycelium) {
                     newBlockID = Blocks.stone;
                  } else if(block == Blocks.cobblestone) {
                     newBlockID = Blocks.stone;
                  } else if(block == Blocks.planks) {
                     newBlockID = Blocks.stone;
                  } else if(block == Witchery.Blocks.PLANKS) {
                     newBlockID = Blocks.stone;
                  } else if(block == Blocks.stonebrick) {
                     newBlockID = Blocks.brick_block;
                  } else if(block == Blocks.sand) {
                     newBlockID = Blocks.sandstone;
                  } else if(block == Blocks.clay) {
                     newBlockID = Blocks.hardened_clay;
                  } else if(block == Blocks.wooden_door) {
                     int i1 = ((BlockDoor)block).func_150012_g(world, x, y, z);
                     if((i1 & 8) != 0) {
                        --y;
                     }

                     world.setBlockToAir(x, y, z);
                     world.setBlockToAir(x, y + 1, z);
                     int pp1 = MathHelper.floor_double((double)((actor.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                     ItemDoor.placeDoorBlock(world, x, y, z, pp1, Blocks.iron_door);
                  }

                  if(newBlockID != Blocks.air) {
                     world.setBlock(x, y, z, newBlockID);
                  }

               }
            });
         }

      }
   }).setColor(3158064).setSize(3.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)3, (byte)0, (byte)0, (byte)2}), new StrokeSet(1, new byte[]{(byte)0, (byte)3, (byte)0, (byte)0, (byte)0, (byte)2}), new StrokeSet(1, new byte[]{(byte)0, (byte)3, (byte)3, (byte)0, (byte)0, (byte)2, (byte)2}), new StrokeSet(2, new byte[]{(byte)0, (byte)3, (byte)3, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)0, (byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Colloportus = instance().addEffect((new SymbolEffectProjectile(6, "witchery.pott.colloportus") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop.typeOfHit == MovingObjectType.BLOCK && caster != null) {
            int y = mop.blockY;
            Block blockID = world.getBlock(mop.blockX, y, mop.blockZ);
            if(blockID instanceof BlockDoor) {
               int i1 = ((BlockDoor)blockID).func_150012_g(world, mop.blockX, y, mop.blockZ);
               if((i1 & 8) != 0) {
                  --y;
               }

               world.setBlockToAir(mop.blockX, y, mop.blockZ);
               world.setBlockToAir(mop.blockX, y + 1, mop.blockZ);
               int pp1 = MathHelper.floor_double((double)((caster.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
               ItemDoor.placeDoorBlock(world, mop.blockX, y, mop.blockZ, pp1, Witchery.Blocks.DOOR_ROWAN);
            }
         }

      }
   }).setColor(5322534).setSize(1.0F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)1, (byte)1, (byte)2}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)2}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2, (byte)1, (byte)2})});
   public static final SymbolEffect Confundus = instance().addEffect((new SymbolEffectProjectile(8, "witchery.pott.confundus") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?2.0D:4.0D);
         EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new EffectRegistry.IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               if(target instanceof EntityLivingBase && !target.isPotionActive(Potion.confusion)) {
                  target.addPotionEffect(new PotionEffect(Potion.confusion.id, 600));
               }

            }
         });
      }
   }).setColor(16771328).setSize(1.5F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)3, (byte)3, (byte)0, (byte)0, (byte)2}), new StrokeSet(1, new byte[]{(byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)2, (byte)2}), new StrokeSet(2, new byte[]{(byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)3, (byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Crucio = instance().addEffect((new SymbolEffectProjectile(9, "witchery.pott.crucio", 5, true, false, (String)null, 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         if(mop != null && caster != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
            if(mop.entityHit instanceof EntityPlayer) {
               if(world.isRemote || !(caster instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled()) {
                  EntityPlayer hitCreature = (EntityPlayer)mop.entityHit;
                  hitCreature.attackEntityFrom(DamageSource.causeIndirectMagicDamage(spell, caster), (float)(4 + 4 * (spell.getEffectLevel() - 1)));
               }
            } else if(mop.entityHit instanceof EntityLiving) {
               EntityLiving hitCreature1 = (EntityLiving)mop.entityHit;
               hitCreature1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(spell, caster), 4.0F);
            }
         }

      }
   }).setColor(6684927).setSize(2.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)1, (byte)3, (byte)1, (byte)1, (byte)2}), new StrokeSet(1, new byte[]{(byte)1, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2, (byte)2}), new StrokeSet(2, new byte[]{(byte)1, (byte)3, (byte)1, (byte)1, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)1, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)1, (byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Defodio = instance().addEffect((new SymbolEffectProjectile(10, "witchery.pott.defodio", 3, false, false, (String)null, 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, effectEntity.getEffectLevel(), new EffectRegistry.IBlockEffect() {
               public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                  Material material = block.getMaterial();
                  if(material == Material.clay || material == Material.craftedSnow || material == Material.ground || material == Material.grass || material == Material.ice || material == Material.rock || material == Material.sand) {
                     world.setBlockToAir(x, y, z);
                     Item itemBlock = null;
                     byte itemDamageValue = -1;

                     try {
                        itemBlock = block.getItemDropped(meta, world.rand, 0);
                        int itemDamageValue1 = block.damageDropped(meta);
                        int ex = block.quantityDropped(meta, 0, world.rand);
                        if(itemBlock != null && itemDamageValue1 >= 0 && ex > 0) {
                           world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, new ItemStack(itemBlock, ex, itemDamageValue1)));
                        }
                     } catch (Throwable var12) {
                        Log.instance().warning(var12, "Exception occured while spawning block as part of Defodio effect: new (" + itemBlock + ", " + itemDamageValue + ") old (" + block + ", " + meta + ")");
                     }
                  }

               }
            });
         }

      }
   }).setColor(4008220).setSize(2.5F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)3, (byte)1}), new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)0, (byte)3, (byte)1, (byte)1}), new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)3, (byte)3, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2})});
   public static final SymbolEffect Ennervate = instance().addEffect((new SymbolEffectProjectile(12, "witchery.pott.ennervate", 1, false, true, (String)null, 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?2.0D:4.0D);
         EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               if(target.isPotionActive(Potion.moveSlowdown)) {
                  target.removePotionEffect(Potion.moveSlowdown.id);
               }

               if(target.isPotionActive(Potion.digSlowdown)) {
                  target.removePotionEffect(Potion.digSlowdown.id);
               }

               if(target.isPotionActive(Potion.confusion)) {
                  target.removePotionEffect(Potion.confusion.id);
               }

            }
         });
      }
   }).setColor(16713595).setSize(1.5F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)3, (byte)0, (byte)2, (byte)3, (byte)0, (byte)2}), new StrokeSet(2, new byte[]{(byte)0, (byte)3, (byte)3, (byte)0, (byte)2, (byte)2, (byte)3, (byte)3, (byte)0, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)0, (byte)3, (byte)3, (byte)3, (byte)0, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3, (byte)0, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Episkey = instance().addEffect(new SymbolEffect(13, "witchery.pott.episkey", 1, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         double radius = effectLevel == 1?0.0D:(effectLevel == 2?2.0D:4.0D);
         MovingObjectPosition mop = new MovingObjectPosition(player);
         EffectRegistry.applyEntityEffect(world, player, mop, player.posX, player.posY, player.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               boolean hasFood = target instanceof EntityPlayer;
               int currentFood = hasFood?((EntityPlayer)target).getFoodStats().getFoodLevel():5;
               if(currentFood > 1 && target.getHealth() < target.getMaxHealth()) {
                  target.heal((float)Math.min(5, currentFood));
                  if(hasFood) {
                     ((EntityPlayer)target).getFoodStats().addStats(-Math.min(5, currentFood), 0.0F);
                  }

                  if(!target.isPotionActive(Potion.confusion)) {
                     target.addPotionEffect(new PotionEffect(Potion.confusion.id, TimeUtil.secsToTicks(4)));
                  }

                  ParticleEffect.SPLASH.send(SoundEffect.MOB_SLIME_SMALL, target, 1.0D, 1.0D, 16);
               }

            }
         });
      }
   }, new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)2, (byte)0, (byte)3, (byte)1, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)2, (byte)0, (byte)0, (byte)3, (byte)1, (byte)1, (byte)1, (byte)1, (byte)2}), new StrokeSet(2, new byte[]{(byte)2, (byte)2, (byte)0, (byte)3, (byte)3, (byte)1, (byte)1, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)2, (byte)2, (byte)0, (byte)0, (byte)3, (byte)3, (byte)1, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2})});
   public static final SymbolEffect Expelliarmus = instance().addEffect((new SymbolEffectProjectile(15, "witchery.pott.expelliarmus") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?3.0D:5.0D);
         EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               if(actor != target) {
                  disarm(target);
               }

            }
         });
      }
      private void disarm(EntityLivingBase target) {
         if(target instanceof EntityPlayer) {
            EntityPlayer heldItem = (EntityPlayer)target;
            if(heldItem.openContainer == null || heldItem.openContainer.windowId == 0) {
               int heldItemIndex = heldItem.inventory.currentItem;
               if(heldItem.inventory.mainInventory[heldItemIndex] != null) {
                  heldItem.dropPlayerItemWithRandomChoice(heldItem.inventory.mainInventory[heldItemIndex], true);
                  heldItem.inventory.mainInventory[heldItemIndex] = null;
               }
            }
         } else if(!PotionIllFitting.isTargetBanned(target)) {
            ItemStack heldItem1 = target.getHeldItem();
            if(heldItem1 != null) {
               if(target instanceof EntityPlayer) {
                  Infusion.dropEntityItemWithRandomChoice(target, heldItem1, true);
               } else {
                  target.entityDropItem(heldItem1, 0.5F);
               }

               target.setCurrentItemOrArmor(0, (ItemStack)null);
            }
         }

      }
   }).setColor(16747778).setSize(3.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)1}), new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)0, (byte)1, (byte)1}), new StrokeSet(2, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect Flagrate = instance().addEffect(new SymbolEffect(16, "witchery.pott.flagrate", 1, false, false, (String)null, 0, false) {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0D);
         if(mop != null) {
            if(mop.typeOfHit == MovingObjectType.BLOCK) {
               ItemChalk.drawGlyph(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, Witchery.Blocks.GLYPH_INFERNAL, player);
            } else {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)2, (byte)3, (byte)0, (byte)2})});
   public static final SymbolEffect Flipendo = instance().addEffect((new SymbolEffectProjectile(17, "witchery.pott.flipendo") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         final double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?3.0D:6.0D);
         final double spellX = spell.motionX;
         final double spellZ = spell.motionZ;
         EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               if(radius == 3.0D || target != actor) {
                  double ACCELERATION = 2.0D;
                  if(target.isPotionActive(Potion.moveSlowdown)) {
                     ACCELERATION += 0.5D;
                  }

                  double motionX = spellX * ACCELERATION;
                  double motionY = 0.3D;
                  double motionZ = spellZ * ACCELERATION;
                  if(target instanceof EntityPlayer) {
                     EntityPlayer targetPlayer = (EntityPlayer)target;
                     Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(motionX, 0.3D, motionZ)), targetPlayer);
                  } else {
                     target.motionX = motionX;
                     target.motionY = 0.3D;
                     target.motionZ = motionZ;
                  }
               }

            }
         });
      }
   }).setColor(16775577).setSize(3.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)2, (byte)2, (byte)3}), new StrokeSet(1, new byte[]{(byte)2, (byte)2, (byte)2, (byte)3, (byte)3}), new StrokeSet(2, new byte[]{(byte)2, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3}), new StrokeSet(3, new byte[]{(byte)2, (byte)2, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3, (byte)3})});
   public static final SymbolEffect Impedimenta = instance().addEffect((new SymbolEffectProjectile(19, "witchery.pott.impedimenta") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?3.0D:6.0D);
         double spellX = spell.motionX;
         double spellZ = spell.motionZ;
         EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
               if(target != actor && !target.isPotionActive(Potion.moveSlowdown)) {
                  target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 600, 1));
               }

            }
         });
      }
   }).setColor(6191615).setSize(1.5F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)3, (byte)3, (byte)2}), new StrokeSet(1, new byte[]{(byte)3, (byte)3, (byte)3, (byte)2, (byte)2}), new StrokeSet(2, new byte[]{(byte)3, (byte)3, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2}), new StrokeSet(3, new byte[]{(byte)3, (byte)3, (byte)3, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Imperio = instance().addEffect((new SymbolEffectProjectile(20, "witchery.pott.imperio", 10, true, false, (String)null, 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop != null && caster != null && caster instanceof EntityPlayer && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase)mop.entityHit;
            if(PotionEnslaved.canCreatureBeEnslaved(entityLiving)) {
               EntityPlayer player = (EntityPlayer)caster;
               EntityLiving creature = (EntityLiving)entityLiving;
               NBTTagCompound nbt = entityLiving.getEntityData();
               if(PotionEnslaved.setEnslaverForMob(creature, player)) {
                  ParticleEffect.SPELL.send(SoundEffect.MOB_ZOMBIE_INFECT, creature, 1.0D, 2.0D, 8);
               }
            }
         }

      }
   }).setColor(10686463).setSize(1.5F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)2, (byte)1, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect Incendio = instance().addEffect((new SymbolEffectProjectile(21, "witchery.pott.incendio") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         double radius = spell.getEffectLevel() == 1?0.0D:(spell.getEffectLevel() == 2?3.0D:6.0D);
         final int level = spell.getEffectLevel();
         if(radius == 0.0D) {
            if(mop.typeOfHit == MovingObjectType.ENTITY) {
               mop.entityHit.setFire(1);
               mop.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("onFire", spell, caster)).setFireDamage(), 0.1F);
            } else if(mop.typeOfHit == MovingObjectType.BLOCK) {
               Block side = BlockUtil.getBlock(world, mop);
               if(side == Witchery.Blocks.WICKER_BUNDLE && BlockWickerBundle.limitToValidMetadata(world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)) == 1) {
                  if(BlockWickerBundle.tryIgniteMan(world, mop.blockX, mop.blockY, mop.blockZ, caster != null?caster.rotationYaw:0.0F)) {
                     return;
                  }
               } else if(side == Witchery.Blocks.BRAZIER) {
                  BlockBrazier.tryIgnite(world, mop.blockX, mop.blockY, mop.blockZ);
                  return;
               }

               int dx = mop.sideHit == 5?1:(mop.sideHit == 4?-1:0);
               int dy = mop.sideHit == 0?-1:(mop.sideHit == 1?1:0);
               int dz = mop.sideHit == 3?1:(mop.sideHit == 2?-1:0);
               world.setBlock(mop.blockX + dx, mop.blockY + dy + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1?-1:0), mop.blockZ + dz, Blocks.fire);
            }
         } else {
            EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, (IEntityEffect)new IEntityEffect<EntityLivingBase>() {
               public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                  if(target != actor) {
                     target.setFire(level);
                  }

               }
            });
            if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
               final int side1 = mop.sideHit;
               EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, level, new EffectRegistry.IBlockEffect() {
                  public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                     if(side1 == 1) {
                        int dx = side1 == 5?1:(side1 == 4?-1:0);
                        int dy = side1 == 0?-1:(side1 == 1?1:0);
                        int dz = side1 == 3?1:(side1 == 2?-1:0);
                        int nX = x + dx;
                        int nY = y + dy;
                        int nZ = z + dz;
                        if(world.isAirBlock(nX, nY, nZ)) {
                           world.setBlock(nX, nY, nZ, Blocks.fire);
                        }
                     }

                  }
               });
            }
         }

      }
   }).setColor(16724023).setSize(2.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)3, (byte)0, (byte)0, (byte)1, (byte)1}), new StrokeSet(2, new byte[]{(byte)3, (byte)0, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1}), new StrokeSet(3, new byte[]{(byte)3, (byte)0, (byte)0, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect Lumos = instance().addEffect((new SymbolEffectProjectile(22, "witchery.pott.lumos") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            int dx = mop.sideHit == 5?1:(mop.sideHit == 4?-1:0);
            int dy = mop.sideHit == 0?-1:(mop.sideHit == 1?1:0);
            int dz = mop.sideHit == 3?1:(mop.sideHit == 2?-1:0);
            int x = mop.blockX + 1 * dx;
            int y = mop.blockY + 1 * dy;
            int z = mop.blockZ + 1 * dz;
            Material material = world.getBlock(x, y, z).getMaterial();
            if(material == Material.air || material == Material.snow) {
               world.setBlock(x, y, z, Witchery.Blocks.GLOW_GLOBE);
            }
         }

      }
   }).setColor(16777018).setSize(0.5F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)2})});
   public static final SymbolEffect MeteolojinxRecanto = instance().addEffect(new SymbolEffect(23, "witchery.pott.meteolojinxrecanto", 100, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0D);
         if(world.isRaining()) {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[0];
            if(worldserver != null) {
               WorldInfo worldinfo = worldserver.getWorldInfo();
               worldinfo.setRaining(false);
               worldinfo.setThundering(false);
            }
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)0, (byte)2, (byte)2, (byte)1, (byte)1})});
   public static final SymbolEffect Nox = instance().addEffect(new SymbolEffect(26, "witchery.pott.nox", 50, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         int x0 = MathHelper.floor_double(player.posX);
         int y0 = MathHelper.floor_double(player.posY);
         int z0 = MathHelper.floor_double(player.posZ);
         byte radius = 10;

         for(int y = y0 - radius; y <= y0 + radius; ++y) {
            for(int x = x0 - radius; x <= x0 + radius; ++x) {
               for(int z = z0 - radius; z <= z0 + radius; ++z) {
                  Block blockID = world.getBlock(x, y, z);
                  if((double)blockID.getLightValue(world, x, y, z) > 0.8D && BlockProtect.canBreak(blockID, world)) {
                     int blockMeta = world.getBlockMetadata(x, y, z);
                     if(BlockProtect.checkModsForBreakOK(world, x, y, z, blockID, blockMeta, player)) {
                        world.setBlockToAir(x, y, z);
                        if(blockID.quantityDropped(world.rand) > 0) {
                           blockID.dropBlockAsItem(world, x, y, z, blockMeta, 0);
                        }
                     }
                  }
               }
            }
         }

      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)2, (byte)1, (byte)2, (byte)0})});
   public static final SymbolEffect Protego = instance().addEffect(new SymbolEffect(31, "witchery.pott.protego") {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0D);
         if(mop != null) {
            if(mop.typeOfHit == MovingObjectType.BLOCK) {
               InfusionLight.placeBarrierShield(world, player, mop);
            } else {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)0}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)0, (byte)0}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)1, (byte)0, (byte)0, (byte)0})});
   public static final SymbolEffect Stupefy = instance().addEffect((new SymbolEffectProjectile(36, "witchery.pott.stupefy", 5, false, true, (String)null, 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase)mop.entityHit;
            if(!entityLiving.isPotionActive(Potion.moveSlowdown)) {
               entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 6000, 9));
            }
         }

      }
   }).setColor(1279).setSize(1.5F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)2, (byte)2, (byte)0, (byte)3, (byte)0, (byte)2}), new StrokeSet(1, new byte[]{(byte)2, (byte)2, (byte)2, (byte)0, (byte)3, (byte)3, (byte)0, (byte)2, (byte)2}), new StrokeSet(2, new byte[]{(byte)2, (byte)2, (byte)0, (byte)0, (byte)3, (byte)0, (byte)0, (byte)2}), new StrokeSet(2, new byte[]{(byte)2, (byte)2, (byte)2, (byte)0, (byte)0, (byte)3, (byte)3, (byte)0, (byte)0, (byte)2, (byte)2})});
   public static final SymbolEffect Ignianima = instance().addEffect((new SymbolEffectProjectile(39, "witchery.pott.ignianima", 2, true, false, "ignianima", 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect e) {
         double R = 1.5D;
         double R_SQ = 2.25D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(e.posX - 1.5D, e.posY - 1.5D, e.posZ - 1.5D, e.posX + 1.5D, e.posY + 1.5D, e.posZ + 1.5D);
         List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            Object hit = i$.next();
            EntityLivingBase hitEntity = (EntityLivingBase)hit;
            if(e.getDistanceSqToEntity(hitEntity) <= 2.25D) {
               float damage = 4.0F;
               float scale = hitEntity instanceof EntityPlayer?hitEntity.getMaxHealth() / 20.0F:1.0F;
               float scaledDamage;
               if(caster != null) {
                  scaledDamage = 20.0F * (caster.getHealth() / caster.getMaxHealth());
                  if(scaledDamage > 19.0F) {
                     damage = 2.0F;
                  } else if(scaledDamage > 15.0F) {
                     damage = 3.0F;
                  } else if(scaledDamage > 10.0F) {
                     damage = 5.0F;
                  } else {
                     damage = 6.0F + (12.0F - scaledDamage) / 2.0F;
                  }
               }

               scaledDamage = damage * scale;
               hitEntity.attackEntityFrom(new DemonicDamageSource(caster), scaledDamage);
               ParticleEffect.FLAME.send(SoundEffect.FIRE_IGNITE, hitEntity, 1.0D, 2.0D, 16);
            }
         }

      }
   }).setColor(16770912).setSize(3.0F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)3, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)3, (byte)3, (byte)3, (byte)3, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect CarnosaDiem = instance().addEffect(new SymbolEffect(40, "witchery.pott.carnosadiem", 1, true, false, "carnosadiem", 0) {
      public void perform(World world, EntityPlayer player, int effectLevel) {
         float damage = player.getMaxHealth() * 0.1F;
         player.attackEntityFrom(new DemonicDamageSource(player), damage);
         ParticleEffect.REDDUST.send(SoundEffect.MOB_ENDERDRAGON_GROWL, player, 1.0D, 2.0D, 16);
         int currentPower = Infusion.getCurrentEnergy(player);
         int maxPower = Infusion.getMaxEnergy(player);
         Infusion.setCurrentEnergy(player, Math.min(currentPower + 10, maxPower));
         Witchery.modHooks.boostBloodPowers(player, damage);
      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)2, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)2, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)2, (byte)2, (byte)0, (byte)1, (byte)1}), new StrokeSet(new byte[]{(byte)2, (byte)2, (byte)2, (byte)2, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1})});
   public static final SymbolEffect MORSMORDRE = instance().addEffect((new SymbolEffectProjectile(41, "witchery.pott.morsmordre", 20, true, false, "morsmordre", 0) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
         if(!world.isRemote) {
            EntityDarkMark entity = new EntityDarkMark(world);
            entity.setLocationAndAngles(effectEntity.posX, effectEntity.posY, effectEntity.posZ, 0.0F, 0.0F);
            entity.func_110163_bv();
            world.spawnEntityInWorld(entity);
         }

      }
   }).setColor(0).setSize(3.0F).setTimeToLive(8), new StrokeSet[]{new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)0, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect Tormentum = instance().addEffect((new SymbolEffectProjectile(42, "witchery.pott.tormentum", 25, true, true, "tormentum", TimeUtil.minsToTicks(30)) {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect e) {
         if(!world.isRemote && e.dimension != Config.instance().dimensionTormentID) {
            double R = 2.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(e.posX - 2.0D, e.posY - 2.0D, e.posZ - 2.0D, e.posX + 2.0D, e.posY + 2.0D, e.posZ + 2.0D);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            boolean setCooldown = false;
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object hitEntity = i$.next();
               if(hitEntity instanceof EntityPlayer) {
                  EntityPlayer hitLiving = (EntityPlayer)hitEntity;
                  WorldProviderTorment.setPlayerMustTorment(hitLiving, 1, -1);
                  setCooldown = true;
               } else if(hitEntity instanceof EntityLiving && !(hitEntity instanceof IBossDisplayData)) {
                  EntityLiving hitLiving1 = (EntityLiving)hitEntity;
                  hitLiving1.setDead();
                  setCooldown = true;
               }
            }

            if(setCooldown && caster != null && caster instanceof EntityPlayer) {
               this.setOnCooldown((EntityPlayer)caster);
            }
         }

      }
   }).setColor(2236962).setSize(4.0F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)1, (byte)3, (byte)2, (byte)2}), new StrokeSet(new byte[]{(byte)1, (byte)1, (byte)1, (byte)1, (byte)3, (byte)3, (byte)2, (byte)2, (byte)2, (byte)2})});
   public static final SymbolEffect LEONARD_1 = instance().addEffect(new SymbolEffect(43, "witchery.pott.leonard1", 5, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int level) {
         EffectRegistry.castLeonardSpell(world, player, 0);
      }
      public int getChargeCost(World world, EntityPlayer player, int level) {
         return EffectRegistry.costOfLeonardSpell(world, player, 0);
      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)2, (byte)0, (byte)3, (byte)3, (byte)1})});
   public static final SymbolEffect LEONARD_2 = instance().addEffect(new SymbolEffect(44, "witchery.pott.leonard2", 5, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int level) {
         EffectRegistry.castLeonardSpell(world, player, 1);
      }
      public int getChargeCost(World world, EntityPlayer player, int level) {
         return EffectRegistry.costOfLeonardSpell(world, player, 1);
      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)3, (byte)1, (byte)2, (byte)2, (byte)0})});
   public static final SymbolEffect LEONARD_3 = instance().addEffect(new SymbolEffect(45, "witchery.pott.leonard3", 5, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int level) {
         EffectRegistry.castLeonardSpell(world, player, 2);
      }
      public int getChargeCost(World world, EntityPlayer player, int level) {
         return EffectRegistry.costOfLeonardSpell(world, player, 2);
      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)1, (byte)2, (byte)0, (byte)0, (byte)3})});
   public static final SymbolEffect LEONARD_4 = instance().addEffect(new SymbolEffect(46, "witchery.pott.leonard4", 5, false, false, (String)null, 0) {
      public void perform(World world, EntityPlayer player, int level) {
         EffectRegistry.castLeonardSpell(world, player, 3);
      }
      public int getChargeCost(World world, EntityPlayer player, int level) {
         return EffectRegistry.costOfLeonardSpell(world, player, 3);
      }
   }, new StrokeSet[]{new StrokeSet(new byte[]{(byte)0, (byte)3, (byte)1, (byte)1, (byte)2})});
   public static final SymbolEffect Attraho = instance().addEffect((new SymbolEffectProjectile(47, "witchery.pott.attraho") {
      public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
         if(caster != null && mop != null) {
            double R = spell.getEffectLevel() == 1?2.0D:(spell.getEffectLevel() == 2?3.0D:9.0D);
            double R_SQ = R * R;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(spell.posX - R, spell.posY - R, spell.posZ - R, spell.posX + R, spell.posY + R, spell.posZ + R);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               EntityLivingBase entity = (EntityLivingBase)i$.next();
               if(entity.getDistanceSqToEntity(spell) <= R_SQ) {
                  EntityUtil.pullTowards(world, entity, new EntityPosition(caster), 0.04D, 0.1D);
               }
            }
         }

      }
   }).setColor(5322534).setSize(1.0F), new StrokeSet[]{new StrokeSet(1, new byte[]{(byte)0, (byte)0, (byte)0, (byte)2, (byte)2, (byte)1, (byte)3})});


   public static final EffectRegistry instance() {
      return INSTANCE;
   }

   public SymbolEffect addEffect(SymbolEffect effect, StrokeSet ... strokeSets) {
      StrokeSet[] arr$ = strokeSets;
      int len$ = strokeSets.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         StrokeSet strokes = arr$[i$];
         strokes.addTo(this.effects, this.enhanced, effect);
      }

      this.effectID.put(Integer.valueOf(effect.getEffectID()), effect);
      strokeSets[0].setDefaultFor(effect);
      this.allEffects.add(effect);
      return effect;
   }

   public boolean contains(byte[] strokes) {
      return this.getEffect(strokes) != null;
   }

   public SymbolEffect getEffect(byte[] strokes) {
      return (SymbolEffect)this.effects.get(ByteBuffer.wrap(strokes));
   }

   public SymbolEffect getEffect(int effectID) {
      return (SymbolEffect)this.effectID.get(Integer.valueOf(effectID));
   }

   public int getLevel(byte[] strokes) {
      return ((Integer)this.enhanced.get(ByteBuffer.wrap(strokes))).intValue();
   }

   public ArrayList getEffects() {
      return this.allEffects;
   }

   private static <T extends Entity> void applyEntityEffect(World world, EntityLivingBase actor, MovingObjectPosition mop, double xMid, double yMid, double zMid, double radius, Class clazz, EffectRegistry.IEntityEffect<T> effect) {
      if(radius == 0.0D) {
         if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != null && clazz.isAssignableFrom(mop.entityHit.getClass())) {
            effect.doAction(world, actor, xMid, yMid, zMid, (T)mop.entityHit);
         }
      } else {
         double R_SQ = radius * radius;
         AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xMid - radius, yMid - radius, zMid - radius, xMid + radius, yMid + radius, zMid + radius);
         List entities = world.getEntitiesWithinAABB(clazz, bb);
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            T entity = (T)obj;
            if(entity.getDistanceSq(xMid, yMid, zMid) <= R_SQ) {
               effect.doAction(world, actor, entity.posX, entity.posY, entity.posZ, entity);
            }
         }
      }

   }

   private static void applyBlockEffect(World world, EntityLivingBase actor, int midX, int midY, int midZ, int side, int radius, EffectRegistry.IBlockEffect effect) {
      int x;
      if(radius == 1) {
         Block r = world.getBlock(midX, midY, midZ);
         x = world.getBlockMetadata(midX, midY, midZ);
         if(r != Blocks.air && BlockProtect.canBreak(r, world) && BlockProtect.checkModsForBreakOK(world, midX, midY, midZ, r, x, actor)) {
            effect.doAction(world, actor, midX, midY, midZ, r, x);
         }
      } else {
         int var16 = Math.min(radius - 1, 3);
         x = midX;
         int y = midY;
         int z = midZ;

         for(int k = -var16; k <= var16; ++k) {
            for(int j = -var16; j <= var16; ++j) {
               switch(side) {
               case 0:
               case 1:
                  x = midX + k;
                  z = midZ + j;
                  break;
               case 2:
               case 3:
                  x = midX + k;
                  y = midY + j;
                  break;
               case 4:
               case 5:
                  y = midY + k;
                  z = midZ + j;
               }

               Block block = world.getBlock(x, y, z);
               int meta = world.getBlockMetadata(x, y, z);
               if(block != Blocks.air && BlockProtect.canBreak(block, world) && BlockProtect.checkModsForBreakOK(world, x, y, z, block, meta, actor)) {
                  effect.doAction(world, actor, x, y, z, block, meta);
               }
            }
         }
      }

   }

   private static int costOfLeonardSpell(World world, EntityPlayer player, int spellSlot) {
      int slot = InvUtil.getSlotContainingItem(player.inventory, Witchery.Items.LEONARDS_URN);
      if(slot >= 0 && slot < player.inventory.getSizeInventory()) {
         ItemStack urnStack = player.inventory.getStackInSlot(slot);
         if(urnStack != null) {
            ItemLeonardsUrn.InventoryLeonardsUrn inv = new ItemLeonardsUrn.InventoryLeonardsUrn(player, urnStack);
            if(urnStack.getItemDamage() >= spellSlot) {
               ItemStack potion = inv.getStackInSlot(spellSlot);
               if(potion != null) {
                  int baseLevel = WitcheryBrewRegistry.INSTANCE.getUsedCapacity(potion.getTagCompound());
                  if(player.isPotionActive(Witchery.Potions.WORSHIP)) {
                     PotionEffect effect = player.getActivePotionEffect(Witchery.Potions.WORSHIP);
                     if(effect.getAmplifier() < 1) {
                        baseLevel += (int)Math.ceil((double)baseLevel * 0.5D);
                     }
                  } else {
                     baseLevel *= 2;
                  }

                  return Math.max(baseLevel, 4);
               }
            }
         }
      }

      return 5;
   }

   private static void castLeonardSpell(World world, EntityPlayer player, int spellSlot) {
      int slot = InvUtil.getSlotContainingItem(player.inventory, Witchery.Items.LEONARDS_URN);
      if(slot >= 0 && slot < player.inventory.getSizeInventory()) {
         ItemStack urnStack = player.inventory.getStackInSlot(slot);
         if(urnStack != null) {
            ItemLeonardsUrn.InventoryLeonardsUrn inv = new ItemLeonardsUrn.InventoryLeonardsUrn(player, urnStack);
            if(urnStack.getItemDamage() >= spellSlot) {
               ItemStack potion = inv.getStackInSlot(spellSlot);
               if(potion != null) {
                  world.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
                  EntityBrew entity = new EntityBrew(world, player, potion, true);
                  world.spawnEntityInWorld(entity);
                  return;
               }
            }
         }
      }

      SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
   }

   // $FF: synthetic method
   /*static void access$100(World x0, EntityLivingBase x1, MovingObjectPosition x2, double x3, double x4, double x5, double x6, Class x7, IEntityEffect x8) {
      applyEntityEffect(x0, x1, x2, x3, x4, x5, x6, x7, x8);
   }*/


   private interface IBlockEffect {

      void doAction(World var1, EntityLivingBase var2, int var3, int var4, int var5, Block var6, int var7);
   }

   private interface IEntityEffect<T extends Entity> {

      void doAction(World var1, EntityLivingBase var2, double var3, double var5, double var7, T var9);
   }
}
