package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPoppetShelf;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemPoppet extends ItemBase {

   private final ArrayList poppetTypes = new ArrayList();
   public final ItemPoppet.PoppetType unboundPoppet;
   public final ItemPoppet.PoppetType earthPoppet;
   public final ItemPoppet.PoppetType waterPoppet;
   public final ItemPoppet.PoppetType firePoppet;
   public final ItemPoppet.PoppetType foodPoppet;
   public final ItemPoppet.PoppetType toolPoppet;
   public final ItemPoppet.PoppetType deathPoppet;
   public final ItemPoppet.PoppetType antiVoodooPoppet;
   public final ItemPoppet.PoppetType voodooPoppet;
   public final ItemPoppet.PoppetType vampiricPoppet;
   public final ItemPoppet.PoppetType poppetProtectionPoppet;
   public final ItemPoppet.PoppetType armorPoppet;
   private static final String KEY_DAMAGE = "WITCDamage";
   private static final int MAX_DAMAGE = 1000;
   private static final float AUTO_REPAIR_THRESHOLD = 0.1F;


   public ItemPoppet() {
      this.unboundPoppet = ItemPoppet.PoppetType.register(new ItemPoppet.PoppetType(0, "poppet", "Poppet", 0, (ItemPoppet.NamelessClass1604341018)null), this.poppetTypes);
      this.earthPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(1, "protectEarth", "Earth Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.waterPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(2, "protectWater", "Water Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.firePoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(3, "protectFire", "Fire Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.foodPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(4, "protectStarvation", "Hunger Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.toolPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(5, "protectTool", "Tool Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.deathPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(6, "protectDeath", "Death Protection Poppet", 2, (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.antiVoodooPoppet = ItemPoppet.PoppetType.register(new ItemPoppet.PoppetType(7, "protectVoodoo", "Voodoo Protection Poppet", (ItemPoppet.NamelessClass1604341018)null), this.poppetTypes);
      this.voodooPoppet = ItemPoppet.PoppetType.register(new ItemPoppet.PoppetType(8, "voodoo", "Voodoo Poppet", (ItemPoppet.NamelessClass1604341018)null), this.poppetTypes);
      this.vampiricPoppet = ItemPoppet.PoppetType.register(new ItemPoppet.PoppetType(9, "vampiric", "Vampiric Poppet", 2, (ItemPoppet.NamelessClass1604341018)null), this.poppetTypes);
      this.poppetProtectionPoppet = ItemPoppet.PoppetType.register(new ItemPoppet.PoppetType(10, "protectPoppet", "Poppet Protection", 2, (ItemPoppet.NamelessClass1604341018)null), this.poppetTypes);
      this.armorPoppet = ItemPoppet.PoppetType.register((new ItemPoppet.PoppetType(11, "protectArmor", "Armor Protection Poppet", (ItemPoppet.NamelessClass1604341018)null)).setDestroyOnUse(true), this.poppetTypes);
      this.setNoRepair();
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.values()[((ItemPoppet.PoppetType)this.poppetTypes.get(itemstack.getItemDamage())).rarity];
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      int damage = itemStack.getItemDamage();

      assert damage >= 0 && damage < this.poppetTypes.size() : "damage value is too large";

      return damage >= 0 && damage < this.poppetTypes.size()?((ItemPoppet.PoppetType)this.poppetTypes.get(damage)).getUnlocalizedName():"";
   }

   public String getItemStackDisplayName(ItemStack itemstack) {
      String entityID;
      String localizedName;
      if(this.vampiricPoppet.isMatch(itemstack)) {
         entityID = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(itemstack, Integer.valueOf(1));
         localizedName = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(itemstack, Integer.valueOf(2));
         String localizedName1 = super.getItemStackDisplayName(itemstack);
         return !entityID.isEmpty() && !localizedName.isEmpty()?String.format("%s (%s -> %s)", new Object[]{localizedName1, entityID, localizedName}):(!entityID.isEmpty()?String.format("%s (%s -> ??)", new Object[]{localizedName1, entityID}):(!localizedName.isEmpty()?String.format("%s (?? -> %s)", new Object[]{localizedName1, localizedName}):localizedName1));
      } else {
         entityID = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(itemstack, Integer.valueOf(1));
         localizedName = super.getItemStackDisplayName(itemstack);
         return !entityID.isEmpty()?String.format("%s (%s)", new Object[]{localizedName, entityID}):localizedName;
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advTooltips) {
      String entityID;
      if(this.vampiricPoppet.isMatch(stack)) {
         entityID = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
         String localizedName = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(2));
         super.getItemStackDisplayName(stack);
         if(!entityID.isEmpty() && !localizedName.isEmpty()) {
            list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{String.format("%s -> %s", new Object[]{entityID, localizedName})}));
         } else if(!entityID.isEmpty()) {
            list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{String.format("%s -> ??", new Object[]{entityID})}));
         } else if(!localizedName.isEmpty()) {
            list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{String.format("?? -> %s", new Object[]{localizedName})}));
         } else {
            list.add(Witchery.resource("item.witcheryTaglockKit.unbound"));
         }
      } else {
         entityID = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
         super.getItemStackDisplayName(stack);
         if(entityID != null && !entityID.isEmpty()) {
            list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{entityID}));
         } else {
            list.add(Witchery.resource("item.witcheryTaglockKit.unbound"));
         }
      }

   }

   public void registerIcons(IIconRegister iconRegister) {
      Iterator i$ = this.poppetTypes.iterator();

      while(i$.hasNext()) {
         ItemPoppet.PoppetType poppetType = (ItemPoppet.PoppetType)i$.next();
         poppetType.registerIcon(iconRegister, this);
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damage) {
      if(damage < 0 || damage >= this.poppetTypes.size()) {
         damage = 0;
      }

      return ((ItemPoppet.PoppetType)this.poppetTypes.get(damage)).icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List itemList) {
      Iterator i$ = this.poppetTypes.iterator();

      while(i$.hasNext()) {
         ItemPoppet.PoppetType poppetType = (ItemPoppet.PoppetType)i$.next();
         itemList.add(new ItemStack(item, 1, poppetType.damageValue));
      }

   }

   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      this.ensureNBT(stack);
      super.onCreated(stack, world, player);
   }

   public boolean isDamageable() {
      return true;
   }

   public boolean isDamaged(ItemStack stack) {
      this.ensureNBT(stack);
      return this.getDamageNBT(stack) > 0;
   }

   public void setDamage(ItemStack stack, int damage) {}

   public int getDisplayDamage(ItemStack stack) {
      this.ensureNBT(stack);
      return this.getDamageNBT(stack);
   }

   public int getMaxDamage() {
      return 1000;
   }

   private int getDamageNBT(ItemStack stack) {
      this.ensureNBT(stack);
      return stack.getTagCompound().getInteger("WITCDamage");
   }

   private void setDamageNBT(IInventory inventory, ItemStack stack, int damage) {
      this.ensureNBT(stack);
      damage = Math.min(damage, 1000);
      stack.getTagCompound().setInteger("WITCDamage", damage);
      if(damage == 1000) {
         stack.stackSize = 0;
         if(inventory != null) {
            for(int i = 0; i < inventory.getSizeInventory(); ++i) {
               if(inventory.getStackInSlot(i) == stack) {
                  inventory.setInventorySlotContents(i, (ItemStack)null);
                  break;
               }
            }
         }
      }

   }

   private void ensureNBT(ItemStack stack) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      if(!stack.getTagCompound().hasKey("WITCDamage")) {
         stack.getTagCompound().setInteger("WITCDamage", 0);
      }

   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
      if(!world.isRemote && this.voodooPoppet.isMatch(stack) && entity.isInsideOfMaterial(Material.water) && entity.getAir() <= 0) {
         EntityLivingBase boundEntity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, entity, stack, Integer.valueOf(1));
         if(boundEntity != null && boundEntity.isEntityAlive() && !this.voodooProtectionActivated(entity instanceof EntityPlayer?(EntityPlayer)entity:null, stack, boundEntity, true, false)) {
            if(entity instanceof EntityPlayer) {
               EntityWitchHunter.blackMagicPerformed((EntityPlayer)entity);
            }

            boolean damageDisabled = boundEntity instanceof EntityPlayer && ((EntityPlayer)boundEntity).capabilities.disableDamage;
            if(!ItemHunterClothes.isMagicalProtectionActive(boundEntity) && !boundEntity.canBreatheUnderwater() && !boundEntity.isPotionActive(Potion.waterBreathing.id) && !damageDisabled) {
               for(int i = 0; i < 8; ++i) {
                  float f = world.rand.nextFloat() - world.rand.nextFloat();
                  float f1 = world.rand.nextFloat() - world.rand.nextFloat();
                  float f2 = world.rand.nextFloat() - world.rand.nextFloat();
                  world.spawnParticle("bubble", boundEntity.posX + (double)f, boundEntity.posY + (double)f1, boundEntity.posZ + (double)f2, boundEntity.motionX, boundEntity.motionY, boundEntity.motionZ);
               }

               boundEntity.attackEntityFrom(DamageSource.drown, 1.0F);
            }

            boundEntity.extinguish();
         }
      }

      super.onUpdate(stack, world, entity, par4, par5);
   }

   public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
      if(this.voodooPoppet.isMatch(itemstack)) {
         player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
      }

      return super.onItemRightClick(itemstack, world, player);
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 80;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.bow;
   }

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int ticks) {
      if(!world.isRemote) {
         if(this.voodooPoppet.isMatch(itemstack)) {
            EntityLivingBase entity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, player, itemstack, Integer.valueOf(1));
            if(entity != null) {
               EntityWitchHunter.blackMagicPerformed(player);
               MovingObjectPosition hitObject = this.getMovingObjectPositionFromPlayer(world, player, true);
               if(hitObject != null && hitObject.typeOfHit == MovingObjectType.BLOCK) {
                  Material look = world.getBlock(hitObject.blockX, hitObject.blockY, hitObject.blockZ).getMaterial();
                  world.getBlock(hitObject.blockX, hitObject.blockY, hitObject.blockZ);
                  if(look == Material.lava) {
                     if(!this.voodooProtectionActivated(player, itemstack, entity, true, false) && !ItemHunterClothes.isMagicalProtectionActive(player)) {
                        entity.setFire(10);
                     }

                     this.setDamageNBT(player.inventory, itemstack, 1000);
                     world.playSoundAtEntity(player, "random.fizz", 0.4F, 2.0F + world.rand.nextFloat() * 0.4F);
                     return;
                  }
               }

               if(!player.isSneaking()) {
                  if(!this.voodooProtectionActivated(player, itemstack, entity, true, false) && !ItemHunterClothes.isMagicalProtectionActive(player)) {
                     Vec3 look2 = player.getLookVec();
                     float scaling = (float)((this.getMaxItemUseDuration(itemstack) - ticks) / 20);
                     double motionX = look2.xCoord * 0.9D * (double)scaling;
                     double motionY = look2.yCoord * 0.3D * (double)scaling;
                     double motionZ = look2.zCoord * 0.9D * (double)scaling;
                     if(entity instanceof EntityPlayer) {
                        EntityPlayer targetPlayer = (EntityPlayer)entity;
                        Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(motionX, motionY, motionZ)), targetPlayer);
                     } else {
                        entity.motionX = motionX;
                        entity.motionY = motionY;
                        entity.motionZ = motionZ;
                     }

                     this.setDamageNBT(player.inventory, itemstack, this.getDamageNBT(itemstack) + 10);
                  }

                  return;
               }

               if(player.capabilities.isCreativeMode || Witchery.Items.GENERIC.itemBoneNeedle.isItemInInventory(player.inventory)) {
                  ItemPoppet.PoppetDamageSource look1 = new ItemPoppet.PoppetDamageSource(DamageSource.magic, player, (ItemPoppet.NamelessClass1604341018)null);
                  if(!this.voodooProtectionActivated(player, itemstack, entity, true, false)) {
                     entity.attackEntityFrom(look1, 0.5F);
                     if(!player.capabilities.isCreativeMode) {
                        Witchery.Items.GENERIC.itemBoneNeedle.consumeItemFromInventory(player.inventory);
                        this.setDamageNBT(player.inventory, itemstack, this.getDamageNBT(itemstack) + 10);
                     }
                  }
               }

               return;
            }
         }

         super.onPlayerStoppedUsing(itemstack, world, player, ticks);
      }

   }

   public boolean voodooProtectionActivated(EntityPlayer attackingEntity, ItemStack voodooStack, EntityLivingBase targetEntity, int strength) {
      if(strength > 1 && (!(targetEntity instanceof EntityPlayer) || !InfusedSpiritEffect.POPPET_ENHANCEMENT.isNearTo((EntityPlayer)targetEntity))) {
         for(int i = 1; i <= strength; ++i) {
            boolean allowLightning = i == strength;
            if(!this.voodooProtectionActivated(attackingEntity, voodooStack, targetEntity, allowLightning, false)) {
               return false;
            }
         }

         return true;
      } else {
         return this.voodooProtectionActivated(attackingEntity, voodooStack, targetEntity, true, false);
      }
   }

   public boolean voodooProtectionActivated(EntityPlayer attackingEntity, ItemStack voodooStack, EntityLivingBase targetEntity, boolean allowLightning, boolean onlyBoosted) {
      boolean ITEM_DAMAGE = true;
      if(targetEntity instanceof EntityPlayer) {
         EntityPlayer targetPlayer = (EntityPlayer)targetEntity;
         ItemStack defenseStack = findBoundPoppetInWorld(this.antiVoodooPoppet, targetPlayer, 350, false, onlyBoosted);
         if(defenseStack != null && !targetPlayer.worldObj.isRemote) {
            if(attackingEntity != null && voodooStack != null) {
               this.setDamageNBT(attackingEntity.inventory, voodooStack, this.getDamageNBT(voodooStack) + 350);
            }

            if(attackingEntity != null && allowLightning) {
               EntityLightningBolt lightning = new EntityLightningBolt(attackingEntity.worldObj, attackingEntity.posX, attackingEntity.posY, attackingEntity.posZ);
               attackingEntity.worldObj.addWeatherEffect(lightning);
            }

            return true;
         }
      }

      return false;
   }

   public boolean poppetProtectionActivated(EntityPlayer attackingEntity, ItemStack voodooStack, EntityLivingBase targetEntity, boolean allowLightning) {
      boolean ITEM_DAMAGE = true;
      if(targetEntity instanceof EntityPlayer) {
         EntityPlayer targetPlayer = (EntityPlayer)targetEntity;
         ItemStack defenseStack = findBoundPoppetInWorld(this.poppetProtectionPoppet, targetPlayer, 350);
         if(defenseStack != null && !attackingEntity.worldObj.isRemote) {
            if(voodooStack != null) {
               this.setDamageNBT(attackingEntity.inventory, voodooStack, this.getDamageNBT(voodooStack) + 350);
            }

            if(attackingEntity != null && allowLightning) {
               EntityLightningBolt lightning = new EntityLightningBolt(attackingEntity.worldObj, attackingEntity.posX, attackingEntity.posY, attackingEntity.posZ);
               attackingEntity.worldObj.addWeatherEffect(lightning);
            }

            return true;
         }
      }

      return false;
   }

   public void destroyAntiVoodooPoppets(EntityPlayer attackingEntity, EntityLivingBase targetEntity, int poppetsToDestroy) {
      boolean ITEM_DAMAGE = true;
      boolean MAX = true;
      if(targetEntity instanceof EntityPlayer) {
         EntityPlayer targetPlayer = (EntityPlayer)targetEntity;

         for(int i = 0; i < poppetsToDestroy; ++i) {
            ItemStack defenseStack = findBoundPoppetInWorld(this.antiVoodooPoppet, targetPlayer, 1000);
            if(defenseStack == null) {
               return;
            }
         }
      }

   }

   public static ItemStack findBoundPoppetInWorld(ItemPoppet.PoppetType poppetType, EntityPlayer player, int foundItemDamage) {
      return findBoundPoppetInWorld(poppetType, player, foundItemDamage, false, false);
   }

   public static ItemStack findBoundPoppetInWorld(ItemPoppet.PoppetType poppetType, EntityPlayer player, int foundItemDamage, boolean allIndices, boolean onlyBoosted) {
      if(ItemHunterClothes.isFullSetWorn(player, false)) {
         return null;
      } else {
         int damageValue = poppetType.damageValue;
         ItemStack poppetStack = findBoundPoppetInInventory(Witchery.Items.POPPET, damageValue, player, player.inventory, foundItemDamage, allIndices, onlyBoosted);
         if(poppetStack != null) {
            return poppetStack;
         } else {
            if(!player.worldObj.isRemote && !onlyBoosted) {
               MinecraftServer server = MinecraftServer.getServer();
               WorldServer[] arr$ = server.worldServers;
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  WorldServer world = arr$[i$];
                  if(!Config.instance().restrictPoppetShelvesToVanillaAndSpiritDimensions || world.provider.dimensionId == 0 || world.provider.dimensionId == -1 || world.provider.dimensionId == 1 || world.provider.dimensionId == Config.instance().dimensionDreamID) {
                     Iterator i$1 = world.loadedTileEntityList.iterator();

                     while(i$1.hasNext()) {
                        Object obj = i$1.next();
                        if(obj instanceof BlockPoppetShelf.TileEntityPoppetShelf) {
                           poppetStack = findBoundPoppetInInventory(Witchery.Items.POPPET, damageValue, player, (IInventory)obj, foundItemDamage, allIndices, false);
                           if(poppetStack != null) {
                              return poppetStack;
                           }
                        }
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   private static ItemStack findBoundPoppetInInventory(Item item, int damage, EntityPlayer player, IInventory inventory, int foundItemDamage, boolean allIndices, boolean onlyBoosted) {
      for(int i = 0; i < inventory.getSizeInventory(); ++i) {
         ItemStack itemstack = inventory.getStackInSlot(i);
         if(itemstack != null && itemstack.getItem() == item && itemstack.getItemDamage() == damage && Witchery.Items.TAGLOCK_KIT.containsTaglockForEntity(itemstack, player, Integer.valueOf(1)) && (!allIndices || Witchery.Items.TAGLOCK_KIT.isTaglockPresent(itemstack, Integer.valueOf(2)))) {
            if(onlyBoosted) {
               if(InfusedSpiritEffect.POPPET_ENHANCEMENT.isNearTo(player)) {
                  ((ItemPoppet.PoppetType)Witchery.Items.POPPET.poppetTypes.get(damage)).applyDamageOnUse(inventory, i, itemstack, foundItemDamage);
                  return itemstack;
               } else {
                  return null;
               }
            } else {
               ((ItemPoppet.PoppetType)Witchery.Items.POPPET.poppetTypes.get(damage)).applyDamageOnUse(inventory, i, itemstack, foundItemDamage);
               return itemstack;
            }
         }
      }

      return null;
   }

   public void addDamageToPoppet(ItemStack sourcePoppet, ItemStack destPoppet) {
      this.setDamageNBT((IInventory)null, destPoppet, this.getDamageNBT(sourcePoppet));
   }

   public void cancelEventIfPoppetFound(EntityPlayer player, ItemPoppet.PoppetType poppetType, LivingHurtEvent event, boolean heal) {
      this.cancelEventIfPoppetFound(player, poppetType, event, heal, false);
   }

   public void cancelEventIfPoppetFound(EntityPlayer player, ItemPoppet.PoppetType poppetType, LivingHurtEvent event, boolean heal, boolean onlyHandheld) {
      ItemStack stack = findBoundPoppetInWorld(poppetType, player, 1000, false, onlyHandheld);
      if(stack != null) {
         event.setCanceled(true);
         if(heal && player.getHealth() < 10.0F) {
            player.setHealth(10.0F);
         }

         SoundEffect.RANDOM_ORB.playAtPlayer(player.worldObj, player);
      }

   }

   public void checkForArmorProtection(EntityPlayer player) {
      for(int i = 0; i < player.inventory.armorInventory.length; ++i) {
         ItemStack armorPiece = player.inventory.armorInventory[i];
         if(armorPiece != null && armorPiece.isItemStackDamageable()) {
            int itemDamage = armorPiece.getItemDamage();
            int maxDamage = armorPiece.getMaxDamage();
            int repairThreshold = (int)((float)maxDamage * 0.9F);
            if(itemDamage >= repairThreshold) {
               ItemPoppet var10000 = Witchery.Items.POPPET;
               ItemStack protectStack = findBoundPoppetInWorld(Witchery.Items.POPPET.armorPoppet, player, 1000);
               if(protectStack != null) {
                  armorPiece.setItemDamage(0);
                  player.worldObj.playSoundAtEntity(player, "random.orb", 0.5F, 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F));
               }
            }
         }
      }

   }


   public static class PoppetEventHooks {

      @SubscribeEvent
      public void onPlayerInteract(PlayerInteractEvent event) {
         if(!event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            ItemStack heldItem = player.getHeldItem();
            if(heldItem != null && heldItem.isItemStackDamageable()) {
               int itemDamage = heldItem.getItemDamage();
               int maxDamage = heldItem.getMaxDamage();
               int repairThreshold = (int)((float)maxDamage * 0.9F);
               if(itemDamage >= repairThreshold) {
                  ItemStack protectStack = ItemPoppet.findBoundPoppetInWorld(Witchery.Items.POPPET.toolPoppet, player, 1000);
                  if(protectStack != null) {
                     heldItem.setItemDamage(0);
                     player.worldObj.playSoundAtEntity(player, "random.orb", 0.5F, 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F));
                  }
               }
            }
         }

      }
   }

   private static class PoppetDamageSource extends EntityDamageSource {

      private PoppetDamageSource(DamageSource damageType, Entity source) {
         super(damageType.getDamageType(), source);
         this.setDamageBypassesArmor();
         this.setMagicDamage();
      }

      // $FF: synthetic method
      PoppetDamageSource(DamageSource x0, Entity x1, ItemPoppet.NamelessClass1604341018 x2) {
         this(x0, x1);
      }
   }

   public static class PoppetType {

      public final int damageValue;
      private final String unlocalizedName;
      private final String localizedName;
      private final int rarity;
      @SideOnly(Side.CLIENT)
      private IIcon icon;
      private boolean destroyOnUse;


      private static ItemPoppet.PoppetType register(ItemPoppet.PoppetType poppetType, ArrayList poppetTypes) {
         poppetTypes.add(poppetType);
         return poppetType;
      }

      private PoppetType(int damageValue, String unlocalizedName, String localizedName) {
         this(damageValue, unlocalizedName, localizedName, 1);
      }

      private PoppetType(int damageValue, String unlocalizedName, String localizedName, int rarity) {
         this.damageValue = damageValue;
         this.unlocalizedName = unlocalizedName;
         this.localizedName = localizedName;
         this.rarity = rarity;
      }

      public ItemStack createStack() {
         return this.createStack(1);
      }

      public ItemStack createStack(int quantity) {
         return new ItemStack(Witchery.Items.POPPET, quantity, this.damageValue);
      }

      public boolean isMatch(ItemStack itemstack) {
         return itemstack != null && itemstack.getItemDamage() == this.damageValue;
      }

      private String getUnlocalizedName() {
         return this.damageValue > 0?String.format("%s.%s", new Object[]{Witchery.Items.POPPET.getUnlocalizedName(), this.unlocalizedName}):Witchery.Items.POPPET.getUnlocalizedName();
      }

      @SideOnly(Side.CLIENT)
      private ItemPoppet.PoppetType registerIcon(IIconRegister iconRegister, ItemPoppet item) {
         if(this.unlocalizedName.equals("poppet")) {
            this.icon = iconRegister.registerIcon(item.getIconString());
         } else {
            this.icon = iconRegister.registerIcon(item.getIconString() + "." + this.unlocalizedName);
         }

         return this;
      }

      public ItemPoppet.PoppetType setDestroyOnUse(boolean destroyOnUse) {
         this.destroyOnUse = destroyOnUse;
         return this;
      }

      private boolean applyDamageOnUse(IInventory inventory, int i, ItemStack itemstack, int itemDamage) {
         if(this.destroyOnUse) {
            inventory.setInventorySlotContents(i, (ItemStack)null);
            itemstack.stackSize = 0;
         } else {
            Witchery.Items.POPPET.setDamageNBT(inventory, itemstack, Witchery.Items.POPPET.getDamageNBT(itemstack) + itemDamage);
         }

         return false;
      }

      // $FF: synthetic method
      PoppetType(int x0, String x1, String x2, int x3, ItemPoppet.NamelessClass1604341018 x4) {
         this(x0, x1, x2, x3);
      }

      // $FF: synthetic method
      PoppetType(int x0, String x1, String x2, ItemPoppet.NamelessClass1604341018 x3) {
         this(x0, x1, x2);
      }
   }

   // $FF: synthetic class
   static class NamelessClass1604341018 {
   }
}
