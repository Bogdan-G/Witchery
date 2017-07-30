package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.ExtendedVillager;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityVampire;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemGlassGoblet extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon iconFull;


   public ItemGlassGoblet() {
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      if(!world.isRemote) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(!this.hasBlood(stack)) {
            if(playerEx.getVampireLevel() >= 9) {
               if(playerEx.decreaseBloodPower(125, true)) {
                  this.setBloodOwner(stack, player);
                  ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, world, player.posX, player.posY + (double)(player.height * 0.85F), player.posZ, 0.8D, 0.3D, 16);
               } else {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.notenoughblood", new Object[0]);
                  SoundEffect.NOTE_SNARE.playOnlyTo(player);
               }
            } else if(playerEx.isVampire()) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.nothighenoughlevel", new Object[0]);
               SoundEffect.NOTE_SNARE.playOnlyTo(player);
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.nothinghappens", new Object[0]);
               SoundEffect.NOTE_SNARE.playOnlyTo(player);
            }
         } else {
            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            if(!playerEx.isVampire() && !this.hasBloodType(stack, ItemGlassGoblet.BloodSource.CHICKEN)) {
               if(!Config.instance().allowVampireWolfHybrids && playerEx.getWerewolfLevel() > 0) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.wolfcurse.hybridsnotallow", new Object[0]);
                  return stack;
               }

               if(playerEx.getBloodPower() == 0) {
                  playerEx.setVampireLevel(1);
                  ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, player, 0.8D, 1.5D, 16);
               } else if(CreatureUtil.isInSunlight(player)) {
                  player.setFire(5);
               } else {
                  player.addPotionEffect(new PotionEffect(Potion.hunger.id, TimeUtil.secsToTicks(30)));
                  player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, TimeUtil.secsToTicks(30), 1));
               }
            }

            this.setBloodOwner(stack, ItemGlassGoblet.BloodSource.EMPTY);
         }
      }

      return stack;
   }

   public String getItemStackDisplayName(ItemStack stack) {
      return this.hasBlood(stack)?("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".full")).trim():super.getItemStackDisplayName(stack);
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advTooltips) {
      super.addInformation(stack, player, list, advTooltips);
      if(this.hasBlood(stack)) {
         list.add(String.format(Witchery.resource(this.getUnlocalizedNameInefficiently(stack) + ".tip"), new Object[]{this.getBloodName(stack)}));
      }

   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 32;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return this.hasBlood(stack)?EnumAction.drink:EnumAction.block;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      MovingObjectPosition mop = InfusionOtherwhere.raytraceBlocks(world, player, true, 2.0D);
      if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK && world.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Blocks.skull) {
         TileEntitySkull skull = (TileEntitySkull)BlockUtil.getTileEntity(world, mop.blockX, mop.blockY, mop.blockZ, TileEntitySkull.class);
         if(!world.isRemote && skull != null && skull.func_145904_a() == 0) {
            if(this.hasBloodType(stack, ItemGlassGoblet.BloodSource.CHICKEN) && world.provider.dimensionId == 0 && this.isRitual(world, mop.blockX, mop.blockY, mop.blockZ) && world.canBlockSeeTheSky(mop.blockX, mop.blockY, mop.blockZ) && !world.isDaytime() && Config.instance().allowVampireRitual && !this.isElleNear(world, (double)mop.blockX, (double)(mop.blockY - 1), (double)mop.blockZ, 32.0D)) {
               this.setBloodOwner(stack, ItemGlassGoblet.BloodSource.EMPTY);
               EntityLightningBolt bolt = new EntityLightningBolt(world, 0.5D + (double)mop.blockX, (double)mop.blockY + 0.05D, 0.5D + (double)mop.blockZ);
               world.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
               world.addWeatherEffect(bolt);
               EntityFollower follower = new EntityFollower(world);
               follower.setFollowerType(0);
               follower.func_110163_bv();
               follower.setPositionAndRotation(0.5D + (double)mop.blockX, (double)mop.blockY + 1.05D, 0.5D + (double)mop.blockZ, 0.0F, 0.0F);
               follower.setOwner(player);
               ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_MOB_LILITH_TALK, world, 0.5D + (double)mop.blockX, (double)mop.blockY + 1.05D, 0.5D + (double)mop.blockZ, 1.0D, 2.0D, 16);
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.lilithquest", new Object[0]);
               world.spawnEntityInWorld(follower);
            } else if(!world.isRemote) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.seemswrong", new Object[0]);
               SoundEffect.NOTE_SNARE.playOnlyTo(player);
            }
         }

         return stack;
      } else {
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
         return stack;
      }
   }

   private boolean isElleNear(World world, double x, double y, double z, double range) {
      AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0.5D + x - range, y - range, 0.5D + z - range, 0.5D + x + range, y + range, 0.5D + z + range);
      List followers = world.getEntitiesWithinAABB(EntityFollower.class, bb);
      return followers != null && followers.size() > 0;
   }

   private boolean isRitual(World world, int x, int y, int z) {
      if(world.getBlock(x, y, z) != Blocks.skull) {
         return false;
      } else {
         TileEntitySkull skull = (TileEntitySkull)BlockUtil.getTileEntity(world, x, y, z, TileEntitySkull.class);
         if(skull != null && skull.func_145904_a() == 0) {
            Block string = Blocks.tripwire;
            boolean b = true;
            b &= world.getBlock(x, y, z - 3) == string;
            b &= world.getBlock(x + 1, y, z - 3) == string;
            b &= world.getBlock(x + 2, y, z - 3) == string;
            b &= world.getBlock(x + 2, y, z - 2) == string;
            b &= world.getBlock(x + 3, y, z - 2) == string;
            b &= world.getBlock(x + 3, y, z - 1) == string;
            b &= world.getBlock(x + 3, y, z) == string;
            b &= world.getBlock(x + 3, y, z + 1) == string;
            b &= world.getBlock(x + 3, y, z + 2) == string;
            b &= world.getBlock(x + 2, y, z + 2) == string;
            if(!b) {
               return false;
            } else {
               b &= world.getBlock(x + 2, y, z + 3) == string;
               b &= world.getBlock(x + 1, y, z + 3) == string;
               b &= world.getBlock(x, y, z + 3) == string;
               b &= world.getBlock(x - 1, y, z + 3) == string;
               b &= world.getBlock(x - 2, y, z + 3) == string;
               b &= world.getBlock(x - 2, y, z + 2) == string;
               b &= world.getBlock(x - 3, y, z + 2) == string;
               b &= world.getBlock(x - 3, y, z + 1) == string;
               b &= world.getBlock(x - 3, y, z) == string;
               b &= world.getBlock(x - 3, y, z + 1) == string;
               b &= world.getBlock(x - 3, y, z + 2) == string;
               b &= world.getBlock(x - 2, y, z + 2) == string;
               b &= world.getBlock(x - 2, y, z + 3) == string;
               b &= world.getBlock(x - 1, y, z + 3) == string;
               if(!b) {
                  return false;
               } else {
                  Block candle = Blocks.torch;
                  b &= world.getBlock(x - 3, y, z + 3) == candle;
                  b &= world.getBlock(x - 3, y, z - 3) == candle;
                  b &= world.getBlock(x + 3, y, z + 3) == candle;
                  b &= world.getBlock(x + 3, y, z - 3) == candle;
                  BlockRedstoneWire redstone = Blocks.redstone_wire;
                  b &= world.getBlock(x - 1, y, z) == redstone;
                  b &= world.getBlock(x + 1, y, z) == redstone;
                  b &= world.getBlock(x, y, z + 1) == redstone;
                  b &= world.getBlock(x, y, z - 1) == redstone;
                  b &= world.getBlock(x - 1, y, z - 1) == redstone;
                  b &= world.getBlock(x - 1, y, z + 1) == redstone;
                  b &= world.getBlock(x + 1, y, z - 1) == redstone;
                  b &= world.getBlock(x + 1, y, z + 1) == redstone;

                  for(int dx = x - 3; dx <= x + 3; ++dx) {
                     for(int dz = z - 3; dz <= z + 3; ++dz) {
                        if(!world.getBlock(dx, y - 1, dz).isNormalCube()) {
                           return false;
                        }

                        if(!world.isAirBlock(dx, y + 1, dz)) {
                           return false;
                        }

                        if(!world.isAirBlock(dx, y + 2, dz)) {
                           return false;
                        }
                     }
                  }

                  return b;
               }
            }
         } else {
            return false;
         }
      }
   }

   public void handleCreatureDeath(World world, EntityPlayer player, EntityLivingBase victim) {
      if(victim instanceof EntityChicken && player.getHeldItem() != null && player.getHeldItem().getItem() == Witchery.Items.BOLINE) {
         for(int i = 0; i < 9; ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() == this) {
               int x = MathHelper.floor_double(victim.posX);
               int y = MathHelper.floor_double(victim.posY);
               int z = MathHelper.floor_double(victim.posZ);

               for(int dx = x - 1; dx <= x + 1; ++dx) {
                  for(int dz = z - 1; dz <= z + 1; ++dz) {
                     for(int dy = y - 1; dy <= y + 1; ++dy) {
                        if(this.isRitual(world, dx, dy, dz)) {
                           this.setBloodOwner(stack, ItemGlassGoblet.BloodSource.CHICKEN);
                           ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, world, victim.posX, victim.posY + (double)(victim.height * 0.85F), victim.posZ, 0.5D, 0.5D, 16);
                           return;
                        }
                     }
                  }
               }

               return;
            }
         }
      }

   }

   public void onEntityInteract(World world, EntityPlayer player, ItemStack stack, EntityInteractEvent event) {
      if(!event.entityPlayer.worldObj.isRemote && this.hasBlood(stack) && !CreatureUtil.isWerewolf(event.target, true) && !CreatureUtil.isVampire(event.target)) {
         boolean success = false;
         if(event.target instanceof EntityVillager) {
            EntityVillager entity = (EntityVillager)event.target;
            if(this.tryConvertToVampire(entity, ExtendedVillager.get(entity).getBlood(), player, stack)) {
               success = true;
            }
         } else if(event.target instanceof EntityVillageGuard) {
            EntityVillageGuard entity1 = (EntityVillageGuard)event.target;
            if(this.tryConvertToVampire(entity1, entity1.getBlood(), player, stack)) {
               success = true;
            }
         }

         if(success) {
            ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, world, event.target.posX, event.target.posY, event.target.posZ, (double)event.target.width, (double)event.target.height, 16);
            this.setBloodOwner(stack, ItemGlassGoblet.BloodSource.EMPTY);
         } else {
            SoundEffect.NOTE_SNARE.playOnlyTo(player);
         }

         event.setCanceled(true);
      }

   }

   private boolean tryConvertToVampire(EntityLiving target, int blood, EntityPlayer player, ItemStack stack) {
      PotionEffect effect = target.getActivePotionEffect(Witchery.Potions.PARALYSED);
      if(effect != null && effect.getAmplifier() >= 5) {
         if(blood == 0) {
            if(this.isCoffinNear(player.worldObj, target, 4)) {
               this.convertToVampire(target);
               ExtendedPlayer playerEx = ExtendedPlayer.get(player);
               if(playerEx.getVampireLevel() == 9 && playerEx.canIncreaseVampireLevel() && this.getBloodOwner(stack, player.worldObj) == player) {
                  playerEx.increaseVampireLevel();
               }

               return true;
            }

            ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.nocoffinnear", new Object[0]);
         } else {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.targetnotdrained", new Object[0]);
         }
      } else {
         ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.targetnottransfixed", new Object[0]);
      }

      return false;
   }

   private boolean isCoffinNear(World world, Entity entity, int radius) {
      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);

      for(int dx = x - radius; dx <= x + radius; ++dx) {
         for(int dz = z - radius; dz <= z + radius; ++dz) {
            for(int dy = y - radius; dy <= y + radius; ++dy) {
               if(world.getBlock(dx, dy, dz) == Witchery.Blocks.COFFIN) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void convertToVampire(EntityLiving entity) {
      EntityVampire vampire = new EntityVampire(entity.worldObj);
      vampire.func_110163_bv();
      vampire.copyLocationAndAnglesFrom(entity);
      vampire.onSpawnWithEgg((IEntityLivingData)null);
      entity.worldObj.removeEntity(entity);
      entity.worldObj.spawnEntityInWorld(vampire);
      entity.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)vampire.posX, (int)vampire.posY, (int)vampire.posZ, 0);
   }

   public IIcon getIconFromDamage(int meta) {
      return meta == 0?super.getIconFromDamage(meta):this.iconFull;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.iconFull = iconRegister.registerIcon(this.getIconString() + "full");
   }

   private boolean hasBlood(ItemStack stack) {
      return stack.getItemDamage() == 1;
   }

   private boolean hasBloodType(ItemStack stack, ItemGlassGoblet.BloodSource source) {
      return stack.hasTagCompound() && stack.getTagCompound().hasKey("WITCBloodUUID") && stack.getTagCompound().getString("WITCBloodUUID").equals(source.KEY);
   }

   private EntityPlayer getBloodOwner(ItemStack stack, World world) {
      if(stack.hasTagCompound()) {
         String s = stack.getTagCompound().getString("WITCBloodUUID");
         if(s != null && !s.isEmpty()) {
            if(ItemGlassGoblet.BloodSource.isOneOf(s)) {
               return null;
            }

            UUID uuid = UUID.fromString(s);
            return uuid != null?world.func_152378_a(uuid):null;
         }
      }

      return null;
   }

   private String getBloodName(ItemStack stack) {
      return stack.hasTagCompound()?stack.getTagCompound().getString("WITCBloodName"):"";
   }

   public void setBloodOwner(ItemStack stack, EntityPlayer player) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot;
      if(player != null) {
         nbtRoot = stack.getTagCompound();
         nbtRoot.setString("WITCBloodUUID", player.getGameProfile().getId().toString());
         nbtRoot.setString("WITCBloodName", player.getGameProfile().getName());
         stack.setItemDamage(1);
      } else {
         nbtRoot = stack.getTagCompound();
         nbtRoot.removeTag("WITCBloodUUID");
         nbtRoot.removeTag("WITCBloodName");
         stack.setItemDamage(0);
      }

   }

   public void setBloodOwner(ItemStack stack, ItemGlassGoblet.BloodSource source) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot;
      if(source == ItemGlassGoblet.BloodSource.EMPTY) {
         nbtRoot = stack.getTagCompound();
         nbtRoot.removeTag("WITCBloodUUID");
         nbtRoot.removeTag("WITCBloodName");
         stack.setItemDamage(0);
      } else {
         nbtRoot = stack.getTagCompound();
         nbtRoot.setString("WITCBloodUUID", source.KEY);
         nbtRoot.setString("WITCBloodName", source.DISPLAY_NAME);
         stack.setItemDamage(1);
      }

   }

   public static enum BloodSource {

      EMPTY("EMPTY", 0, "", ""),
      CHICKEN("CHICKEN", 1, "__chicken", "item.witchery:glassgoblet.chicken"),
      LILITH("LILITH", 2, "__lilith", "item.witchery:glassgoblet.lilith");
      public final String KEY;
      public final String DISPLAY_NAME;
      // $FF: synthetic field
      private static final ItemGlassGoblet.BloodSource[] $VALUES = new ItemGlassGoblet.BloodSource[]{EMPTY, CHICKEN, LILITH};


      private BloodSource(String var1, int var2, String nbtKey, String resourceKey) {
         this.KEY = nbtKey;
         this.DISPLAY_NAME = Witchery.resource(resourceKey);
      }

      public static boolean isOneOf(String key) {
         return CHICKEN.KEY.equals(key) || LILITH.KEY.equals(key);
      }

   }
}
