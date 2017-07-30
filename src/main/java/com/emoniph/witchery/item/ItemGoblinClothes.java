package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.client.model.ModelGoblinClothes;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.ServerTickEvents;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.ItemUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemGoblinClothes extends ItemArmor {

   @SideOnly(Side.CLIENT)
   private ModelGoblinClothes modelClothesChest;
   @SideOnly(Side.CLIENT)
   private ModelGoblinClothes modelClothesLegs;


   public ItemGoblinClothes(int armorSlot) {
      super(armorSlot == 0?ArmorMaterial.IRON:ArmorMaterial.CLOTH, 1, armorSlot);
      this.setMaxDamage(armorSlot == 0?ArmorMaterial.DIAMOND.getDurability(armorSlot):ArmorMaterial.IRON.getDurability(armorSlot));
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
      double R2;
      AxisAlignedBB bb2;
      List entities;
      Iterator i$;
      Object obj;
      if(!world.isRemote && world.getTotalWorldTime() % 100L == 0L) {
         R2 = 8.0D;
         bb2 = AxisAlignedBB.getBoundingBox(player.posX - 8.0D, player.posY - 8.0D, player.posZ - 8.0D, player.posX + 8.0D, player.posY + 8.0D, player.posZ + 8.0D);
         entities = world.getEntitiesWithinAABB(EntityPlayer.class, bb2);
         i$ = entities.iterator();

         while(i$.hasNext()) {
            obj = i$.next();
            EntityPlayer otherPlayer = (EntityPlayer)obj;
            if(player != otherPlayer && (isQuiverWorn(player) && isBeltWorn(otherPlayer) || isQuiverWorn(otherPlayer) && isBeltWorn(player))) {
               if(player.isPotionActive(Potion.resistance)) {
                  player.removePotionEffect(Potion.resistance.id);
               }

               player.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, 1));
               return;
            }
         }
      }

      if(!world.isRemote && isHelmWorn(player) && world.getTotalWorldTime() % 5L == 1L) {
         R2 = 16.0D;
         bb2 = AxisAlignedBB.getBoundingBox(player.posX - 16.0D, player.posY - 16.0D, player.posZ - 16.0D, player.posX + 16.0D, player.posY + 16.0D, player.posZ + 16.0D);
         entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb2);
         i$ = entities.iterator();

         while(i$.hasNext()) {
            obj = i$.next();
            EntityLivingBase otherPlayer1 = (EntityLivingBase)obj;
            if(player != otherPlayer1 && this.shouldAffectTarget(player, otherPlayer1)) {
               if(otherPlayer1 instanceof EntityPlayer) {
                  double yawRadians = Math.atan2(otherPlayer1.posZ - player.posZ, otherPlayer1.posX - player.posZ);
                  double yaw = Math.toDegrees(yawRadians) + 180.0D;
                  float rev = ((float)yaw + 90.0F) % 360.0F;
                  if(otherPlayer1 instanceof EntityPlayerMP) {
                     S08PacketPlayerPosLook packet = new S08PacketPlayerPosLook(otherPlayer1.posX, otherPlayer1.posY, otherPlayer1.posZ, rev, otherPlayer1.rotationPitch, false);
                     Witchery.packetPipeline.sendTo((Packet)packet, (EntityPlayer)((EntityPlayerMP)otherPlayer1));
                  }

                  if(!otherPlayer1.isPotionActive(Potion.hunger)) {
                     otherPlayer1.addPotionEffect(new PotionEffect(Potion.hunger.id, 100, 0));
                  }
               } else if(!otherPlayer1.isPotionActive(Potion.weakness)) {
                  otherPlayer1.addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 0));
               }
            }
         }
      }

   }

   private boolean shouldAffectTarget(EntityLivingBase player, EntityLivingBase target) {
      ItemStack itemstack = target.getEquipmentInSlot(1);
      if(itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
         return false;
      } else {
         Vec3 vec3 = target.getLook(1.0F).normalize();
         Vec3 vec31 = Vec3.createVectorHelper(player.posX - target.posX, player.boundingBox.minY + (double)(player.height / 2.0F) - (target.posY + (double)target.getEyeHeight()), player.posZ - target.posZ);
         double d0 = vec31.lengthVector();
         vec31 = vec31.normalize();
         double d1 = vec3.dotProduct(vec31);
         return d1 > 1.0D - 0.025D / d0 && target.canEntityBeSeen(player);
      }
   }

   public boolean hasEffect(ItemStack stack, int pass) {
      return super.hasEffect(stack, pass) || stack.getItem() != Witchery.Items.KOBOLDITE_HELM;
   }

   public boolean hasColor(ItemStack stack) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      return super.getColorFromItemStack(stack, par2);
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return false;
   }

   public int getColor(ItemStack stack) {
      return !this.hasColor(stack)?16777215:super.getColor(stack);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return super.armorType != 0?EnumRarity.epic:EnumRarity.rare;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      if(stack != null) {
         String localText = Witchery.resource(this.getUnlocalizedName() + ".tip");
         if(localText != null) {
            String[] arr$ = localText.split("\n");
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String s = arr$[i$];
               if(!s.isEmpty()) {
                  list.add(s);
               }
            }
         }
      }

   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return stack != null?(slot == 0?"witchery:textures/entities/goblinclothes_head" + (type == null?"":"_overlay") + ".png":(slot == 2?"witchery:textures/entities/goblinclothes_legs" + (type == null?"":"_overlay") + ".png":"witchery:textures/entities/goblinclothes" + (type == null?"":"_overlay") + ".png")):null;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
      if(this.modelClothesChest == null) {
         this.modelClothesChest = new ModelGoblinClothes(0.61F);
      }

      if(this.modelClothesLegs == null) {
         this.modelClothesLegs = new ModelGoblinClothes(0.0F);
      }

      ModelGoblinClothes armorModel = null;
      if(stack != null && stack.getItem() instanceof ItemArmor) {
         int type = ((ItemArmor)stack.getItem()).armorType;
         if(type != 2) {
            armorModel = this.modelClothesChest;
         } else {
            armorModel = this.modelClothesLegs;
         }

         if(armorModel != null) {
            boolean isVisible = true;
            armorModel.bipedHead.showModel = isVisible && armorSlot == 0;
            armorModel.bipedHeadwear.showModel = isVisible && armorSlot == 0;
            armorModel.bipedBody.showModel = isVisible && (armorSlot == 1 || armorSlot == 2);
            armorModel.bipedRightArm.showModel = isVisible && armorSlot == 1;
            armorModel.bipedLeftArm.showModel = isVisible && armorSlot == 1;
            armorModel.bipedRightLeg.showModel = isVisible && (armorSlot == 3 || armorSlot == 2);
            armorModel.bipedLeftLeg.showModel = isVisible && (armorSlot == 3 || armorSlot == 2);
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            ItemStack heldStack = entityLiving.getEquipmentInSlot(0);
            armorModel.heldItemRight = heldStack != null?1:0;
            armorModel.aimedBow = false;
            if(entityLiving instanceof EntityPlayer && heldStack != null && ((EntityPlayer)entityLiving).getItemInUseDuration() > 0) {
               EnumAction enumaction = heldStack.getItemUseAction();
               if(enumaction == EnumAction.block) {
                  armorModel.heldItemRight = 3;
               }

               armorModel.aimedBow = enumaction == EnumAction.bow;
            }

            return armorModel;
         }
      }

      return null;
   }

   private static boolean isQuiverWorn(EntityPlayer player) {
      ItemStack currentArmor = player.getCurrentArmor(2);
      return currentArmor != null && currentArmor.getItem() == Witchery.Items.MOGS_QUIVER;
   }

   private static boolean isHelmWorn(EntityPlayer player) {
      ItemStack currentArmor = player.getCurrentArmor(3);
      return currentArmor != null && currentArmor.getItem() == Witchery.Items.KOBOLDITE_HELM;
   }

   private static boolean isBeltWorn(EntityPlayer player) {
      ItemStack currentArmor = player.getCurrentArmor(1);
      return currentArmor != null && currentArmor.getItem() == Witchery.Items.GULGS_GURDLE;
   }

   // $FF: synthetic method
   static Random access$200() {
      return Item.itemRand;
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onLivingHurt(LivingHurtEvent event) {
         if(!event.entityLiving.worldObj.isRemote && !event.isCanceled()) {
            if(event.source.isProjectile()) {
               if(event.source.getSourceOfDamage() != null) {
                  boolean source = event.source.getSourceOfDamage().getEntityData().getBoolean("WITCMogged");
                  if(source) {
                     if(event.entityLiving.isAirBorne) {
                        event.ammount *= 3.0F;
                     }

                     if(event.entityLiving.isPotionActive(Potion.weakness)) {
                        event.entityLiving.removePotionEffect(Potion.weakness.id);
                     }

                     event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 0));
                  }
               }
            } else if(event.source.getDamageType().equals("player")) {
               Entity source1 = event.source.getEntity();
               if(source1 != null && source1 instanceof EntityPlayer) {
                  EntityPlayer player = (EntityPlayer)source1;
                  if(ItemGoblinClothes.isBeltWorn(player) && player.getHeldItem() == null) {
                     event.ammount = 5.0F;
                     final EntityLivingBase entity = event.entityLiving;
                     if(entity instanceof EntityPlayer) {
                        Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(entity.motionX, 1.0D, entity.motionZ)), (EntityPlayer)entity);
                     } else {
                        ServerTickEvents.TASKS.add(new ServerTickEvents.ServerTickTask(player.worldObj) {
                           public boolean process() {
                              if(entity != null && !entity.isDead) {
                                 entity.motionY = 1.0D;
                              }

                              return true;
                           }
                        });
                     }
                  }
               }
            }
         }

      }

      @SubscribeEvent
      public void onArrowLoose(ArrowLooseEvent event) {
         if(!event.isCanceled() && ItemGoblinClothes.isQuiverWorn(event.entityPlayer)) {
            float f = (float)event.charge / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if((double)f < 0.1D) {
               return;
            }

            if(f > 1.0F) {
               f = 1.0F;
            }

            EntityArrow entityarrow = new EntityArrow(event.entityPlayer.worldObj, event.entityPlayer, f * 3.0F);
            entityarrow.getEntityData().setBoolean("WITCMogged", true);
            if(f == 1.0F) {
               entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
            if(k > 0) {
               entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
            if(l > 0) {
               entityarrow.setKnockbackStrength(l);
            }

            if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, event.bow) > 0) {
               entityarrow.setFire(100);
            }

            event.bow.damageItem(1, event.entityPlayer);
            event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "random.bow", 1.0F, 1.0F / (ItemGoblinClothes.access$200().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            entityarrow.canBePickedUp = 2;
            if(!event.entityPlayer.worldObj.isRemote) {
               event.entityPlayer.worldObj.spawnEntityInWorld(entityarrow);
            }

            event.setCanceled(true);
         }

      }

      @SubscribeEvent
      public void onArrowNock(ArrowNockEvent event) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(event.entityPlayer);
         if(playerEx.getCreatureType() != TransformCreature.NONE) {
            event.setCanceled(true);
         } else {
            if(!event.isCanceled() && ItemGoblinClothes.isQuiverWorn(event.entityPlayer)) {
               event.entityPlayer.setItemInUse(event.result, event.result.getItem().getMaxItemUseDuration(event.result));
            }

         }
      }
   }
}
