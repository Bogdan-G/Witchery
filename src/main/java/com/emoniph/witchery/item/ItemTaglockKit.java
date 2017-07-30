package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBloodRose;
import com.emoniph.witchery.blocks.BlockCrystalBall;
import com.emoniph.witchery.blocks.BlockLeechChest;
import com.emoniph.witchery.entity.EntityEye;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.entity.EntityTreefyd;
import com.emoniph.witchery.entity.EntityWingedMonkey;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.network.PacketCamPos;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemTaglockKit extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon emptyIcon;
   @SideOnly(Side.CLIENT)
   private IIcon fullIcon;
   static final String KEY_PLAYER_NAME = "WITCPlayer";
   static final String KEY_DISPLAY_NAME = "WITCDisplayName";
   static final String KEY_ENTITY_ID_MOST = "WITCEntityIDm";
   static final String KEY_ENTITY_ID_LEAST = "WITCEntityIDl";


   public ItemTaglockKit() {
      this.setMaxStackSize(16);
      this.setMaxDamage(0);
   }

   public String getItemStackDisplayName(ItemStack itemStack) {
      String entityID = this.getBoundEntityDisplayName(itemStack, Integer.valueOf(1));
      String localizedName = super.getItemStackDisplayName(itemStack);
      return !entityID.isEmpty()?String.format("%s (%s)", new Object[]{localizedName, entityID}):localizedName;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advTooltips) {
      super.addInformation(stack, player, list, advTooltips);
      String entityID = this.getBoundEntityDisplayName(stack, Integer.valueOf(1));
      if(entityID != null && !entityID.isEmpty()) {
         list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{entityID}));
      } else {
         list.add(Witchery.resource("item.witcheryTaglockKit.unbound"));
      }

   }

   public void registerIcons(IIconRegister par1IconRegister) {
      this.emptyIcon = par1IconRegister.registerIcon(this.getIconString());
      this.fullIcon = par1IconRegister.registerIcon(this.getIconString() + ".full");
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 1200;
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      World world = player.worldObj;
      int elapsedTicks = this.getMaxItemUseDuration(stack) - countdown;
      if(!world.isRemote && elapsedTicks % 20 == 0) {
         EntityLivingBase entity = this.getBoundEntity(world, player, stack, Integer.valueOf(1));
         if(entity != null && entity.dimension == player.dimension) {
            if(entity == player) {
               if(elapsedTicks == 0) {
                  EntityEye eye = new EntityEye(world);
                  eye.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, 90.0F);
                  world.spawnEntityInWorld(eye);
                  Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(true, elapsedTicks == 0, eye)), player);
               } else {
                  Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(true, false, (Entity)null)), player);
               }
            } else {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(true, elapsedTicks == 0, entity)), player);
            }
         } else {
            Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(false, false, (Entity)null)), player);
         }
      }

   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.none;
   }

   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      if(!world.isRemote) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(false, false, (Entity)null)), player);
      }

      return stack;
   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(false, false, (Entity)null)), player);
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damageValue) {
      return damageValue == 1?this.fullIcon:this.emptyIcon;
   }

   public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      Block block = world.getBlock(x, y, z);
      if(block != Blocks.bed && block != Witchery.Blocks.COFFIN && !block.getUnlocalizedName().equals("tile.blockCarpentersBed") && !block.isBed(world, x, y, z, player)) {
         TileEntity tile1;
         String username1;
         if(block == Witchery.Blocks.LEECH_CHEST) {
            if(!world.isRemote) {
               tile1 = world.getTileEntity(x, y, z);
               if(tile1 != null && tile1 instanceof BlockLeechChest.TileEntityLeechChest) {
                  BlockLeechChest.TileEntityLeechChest chest2 = (BlockLeechChest.TileEntityLeechChest)tile1;
                  username1 = chest2.popUserExcept(player);
                  if(username1 != null && !username1.isEmpty()) {
                     this.setTaglockForEntity(world, player, itemstack, username1);
                     return !world.isRemote;
                  }
               }

               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }

            return !world.isRemote;
         } else if(block == Witchery.Blocks.BLOOD_ROSE) {
            if(!world.isRemote) {
               tile1 = world.getTileEntity(x, y, z);
               if(tile1 != null && tile1 instanceof BlockBloodRose.TileEntityBloodRose) {
                  BlockBloodRose.TileEntityBloodRose chest1 = (BlockBloodRose.TileEntityBloodRose)tile1;
                  username1 = chest1.popUserExcept(player, false);
                  if(username1 != null && !username1.isEmpty()) {
                     this.setTaglockForEntity(world, player, itemstack, username1);
                     return !world.isRemote;
                  }
               }

               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }

            return !world.isRemote;
         } else if(block != Witchery.Blocks.CRYSTAL_BALL) {
            return super.onItemUseFirst(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
         } else {
            if(itemstack.getItemDamage() > 0) {
               if(!world.isRemote && BlockCrystalBall.tryConsumePower(world, player, x, y, z)) {
                  player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
               } else if(world.isRemote) {
                  player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
               }
            } else {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }

            return !world.isRemote;
         }
      } else {
         int tile = world.getBlockMetadata(x, y, z);
         Log.instance().debug(String.format("Using taglock on bed [%s] meta %d", new Object[]{block.getUnlocalizedName(), Integer.valueOf(tile)}));
         if(block == Blocks.bed && !BlockBed.isBlockHeadOfBed(tile)) {
            int chest = BlockBed.getDirection(tile);
            x += BlockBed.field_149981_a[chest][0];
            z += BlockBed.field_149981_a[chest][1];
            if(world.getBlock(x, y, z) != Blocks.bed) {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
               return !world.isRemote;
            }
         }

         ChunkCoordinates chest3 = new ChunkCoordinates(x, y, z);
         if(player.isSneaking()) {
            if(!world.isRemote) {
               this.setTaglockForEntity(world, player, itemstack, player);
            }

            return !world.isRemote;
         } else {
            if(!world.isRemote) {
               boolean username = this.tryBindTaglockFromBed(itemstack, player, world, chest3);
               if(!username && block != Blocks.bed) {
                  if(world.getBlock(x + 1, y, z) == block) {
                     username = this.tryBindTaglockFromBed(itemstack, player, world, new ChunkCoordinates(x + 1, y, z));
                  }

                  if(!username && world.getBlock(x, y, z + 1) == block) {
                     username = this.tryBindTaglockFromBed(itemstack, player, world, new ChunkCoordinates(x, y, z + 1));
                  }

                  if(!username && world.getBlock(x - 1, y, z) == block) {
                     username = this.tryBindTaglockFromBed(itemstack, player, world, new ChunkCoordinates(x - 1, y, z));
                  }

                  if(!username && world.getBlock(x, y, z - 1) == block) {
                     username = this.tryBindTaglockFromBed(itemstack, player, world, new ChunkCoordinates(x, y, z - 1));
                  }
               }

               if(username) {
                  return !world.isRemote;
               }
            }

            if(!world.isRemote) {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }

            return !world.isRemote;
         }
      }
   }

   private boolean tryBindTaglockFromBed(ItemStack itemstack, EntityPlayer player, World world, ChunkCoordinates clickedBedLocation) {
      String boundName = "";
      EntityLivingBase boundEntity = this.getBoundEntity(world, player, itemstack, Integer.valueOf(1));
      if(boundEntity != null && boundEntity instanceof EntityPlayer) {
         boundName = ((EntityPlayer)boundEntity).getCommandSenderName();
      }

      ArrayList players = new ArrayList();
      Iterator taglockSaved = world.playerEntities.iterator();

      while(taglockSaved.hasNext()) {
         Object found = taglockSaved.next();
         EntityPlayer i = (EntityPlayer)found;
         ChunkCoordinates playerBedLocation = i.getBedLocation(player.dimension);
         if(playerBedLocation != null && playerBedLocation.equals(clickedBedLocation)) {
            players.add(i);
         }
      }

      Collections.sort(players, new ItemTaglockKit.PlayerComparitor());
      boolean var12 = false;
      if(players.size() > 0) {
         if(boundName.isEmpty()) {
            var12 = this.setTaglockForEntity(world, player, itemstack, (EntityPlayer)players.get(0));
         } else {
            boolean var13 = false;

            for(int var14 = 0; var14 < players.size() && !var13; ++var14) {
               if(((EntityPlayer)players.get(var14)).getCommandSenderName().equals(boundName)) {
                  if(var14 == players.size() - 1) {
                     var12 = this.setTaglockForEntity(world, player, itemstack, (EntityPlayer)players.get(0));
                  } else {
                     var12 = this.setTaglockForEntity(world, player, itemstack, (EntityPlayer)players.get(var14 + 1));
                  }

                  var13 = true;
               }
            }

            if(!var13) {
               var12 = this.setTaglockForEntity(world, player, itemstack, (EntityPlayer)players.get(0));
            }
         }
      }

      return var12;
   }

   public static boolean isTaglockRestricted(EntityPlayer collector, EntityLivingBase target) {
      if(target instanceof EntityPlayer && !collector.equals(target)) {
         if(Config.instance().restrictTaglockCollectionOnNonPVPServers && !MinecraftServer.getServer().isPVPEnabled()) {
            return true;
         } else {
            EntityPlayer targetPlayer = (EntityPlayer)target;
            return Config.instance().restrictTaglockCollectionForStaffMembers && MinecraftServer.getServer().getConfigurationManager().func_152596_g(targetPlayer.getGameProfile());
         }
      } else {
         return false;
      }
   }

   private boolean setTaglockForEntity(World world, EntityPlayer player, ItemStack itemstack, EntityPlayer victim) {
      if(!isTaglockRestricted(player, victim)) {
         this.setTaglockForEntity(world, player, itemstack, victim.getCommandSenderName());
         return true;
      } else {
         return false;
      }
   }

   private void setTaglockForEntity(World world, EntityPlayer player, ItemStack itemstack, String victimUsername) {
      if(!world.isRemote) {
         if(itemstack.stackSize > 1) {
            ItemStack newStack = new ItemStack(this, 1, 1);
            this.setTaglockForEntity(newStack, player, victimUsername, true, Integer.valueOf(1));
            --itemstack.stackSize;
            if(itemstack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }

            if(!player.inventory.addItemStackToInventory(newStack)) {
               world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, newStack));
            } else if(player instanceof EntityPlayerMP) {
               ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }
         } else {
            this.setTaglockForEntity(itemstack, player, victimUsername, true, Integer.valueOf(1));
            itemstack.setItemDamage(1);
            if(player instanceof EntityPlayerMP) {
               ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }
         }
      }

   }

   public void onEntityInteract(World world, EntityPlayer player, ItemStack stack, EntityInteractEvent event) {
      if(!world.isRemote && stack != null && stack.getItem() == Witchery.Items.TAGLOCK_KIT && event.target != null && event.target instanceof EntityLivingBase) {
         EntityLivingBase target = (EntityLivingBase)event.target;
         if(target instanceof EntityPlayer && !this.isSneakSuccessful(player, target)) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, event.entityPlayer, "witchery.taglockkit.taglockfailed", new Object[0]);
            if(target instanceof EntityPlayer) {
               ChatUtil.sendTranslated(EnumChatFormatting.GREEN, (EntityPlayer)target, "witchery.taglockkit.taglockdiscovered", new Object[0]);
            }
         } else {
            if(target instanceof EntityTreefyd || target instanceof EntityImp || target instanceof EntityWingedMonkey && !player.isSneaking()) {
               return;
            }

            if(stack.stackSize > 1) {
               ItemStack newStack = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
               Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(newStack, player, (Entity)target, true, Integer.valueOf(1));
               --stack.stackSize;
               if(stack.stackSize <= 0) {
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               }

               if(!player.inventory.addItemStackToInventory(newStack)) {
                  world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, newStack));
               } else if(player instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
               }
            } else {
               Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(stack, player, (Entity)target, true, Integer.valueOf(1));
               stack.setItemDamage(1);
               if(player instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
               }
            }
         }

         event.setCanceled(true);
      }

   }

   private boolean isSneakSuccessful(EntityPlayer sneaker, EntityLivingBase target) {
      if(isTaglockRestricted(sneaker, target)) {
         return false;
      } else {
         double sneakerFacing = (double)((sneaker.rotationYawHead + 90.0F) % 360.0F);
         if(sneakerFacing < 0.0D) {
            sneakerFacing += 360.0D;
         }

         double targetFacing = (double)((target.rotationYawHead + 90.0F) % 360.0F);
         if(targetFacing < 0.0D) {
            targetFacing += 360.0D;
         }

         double ARC = 45.0D;
         double diff = Math.abs(targetFacing - sneakerFacing);
         double chance = 0.0D;
         if(360.0D - diff % 360.0D >= 45.0D && diff % 360.0D >= 45.0D) {
            chance = sneaker.isSneaking()?0.1D:0.01D;
            if(sneaker.isInvisible()) {
               chance += 0.1D;
            }
         } else {
            chance = sneaker.isSneaking()?0.6D:0.3D;
         }

         return sneaker.worldObj.rand.nextDouble() < chance;
      }
   }

   public void setTaglockForEntity(ItemStack stack, EntityPlayer player, Entity entity, boolean playSoundAtPlayer, Integer index) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      if(entity instanceof EntityPlayer) {
         EntityPlayer id = (EntityPlayer)entity;
         stack.getTagCompound().setString("WITCPlayer" + index.toString(), id.getCommandSenderName());
         stack.getTagCompound().removeTag("WITCEntityIDm" + index.toString());
         stack.getTagCompound().removeTag("WITCEntityIDl" + index.toString());
      } else {
         if(!(entity instanceof EntityLiving)) {
            return;
         }

         stack.getTagCompound().removeTag("WITCPlayer" + index.toString());
         UUID id1 = entity.getPersistentID();
         ((EntityLiving)entity).func_110163_bv();
         stack.getTagCompound().setLong("WITCEntityIDm" + index.toString(), id1.getMostSignificantBits());
         stack.getTagCompound().setLong("WITCEntityIDl" + index.toString(), id1.getLeastSignificantBits());
         stack.getTagCompound().setString("WITCDisplayName" + index.toString(), entity.getCommandSenderName());
      }

      if(playSoundAtPlayer) {
         player.worldObj.playSoundAtEntity(player, "random.orb", 0.5F, 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F));
      }

   }

   public void clearTaglock(ItemStack stack, Integer index) {
      if(stack != null) {
         stack.getTagCompound().removeTag("WITCEntityIDm" + index.toString());
         stack.getTagCompound().removeTag("WITCEntityIDl" + index.toString());
         stack.getTagCompound().removeTag("WITCPlayer" + index.toString());
         stack.getTagCompound().removeTag("WITCDisplayName" + index.toString());
      }

   }

   public void setTaglockForEntity(ItemStack stack, EntityPlayer player, String username, boolean playSoundAtPlayer, Integer index) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      if(username != null && !username.isEmpty()) {
         stack.getTagCompound().setString("WITCPlayer" + index.toString(), username);
         stack.getTagCompound().removeTag("WITCEntityIDm" + index.toString());
         stack.getTagCompound().removeTag("WITCEntityIDl" + index.toString());
         if(playSoundAtPlayer) {
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.5F, 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F));
         }

      }
   }

   public boolean isTaglockPresent(ItemStack itemStack, Integer index) {
      if(itemStack.hasTagCompound()) {
         if(itemStack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
            String playerName = itemStack.getTagCompound().getString("WITCPlayer" + index.toString());
            if(playerName != null && !playerName.isEmpty()) {
               return true;
            }
         }

         if(itemStack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && itemStack.getTagCompound().hasKey("WITCEntityIDl" + index.toString())) {
            return true;
         }
      }

      return false;
   }

   public String getBoundEntityDisplayName(ItemStack itemStack, Integer index) {
      if(itemStack.hasTagCompound()) {
         String displayName;
         if(itemStack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
            displayName = itemStack.getTagCompound().getString("WITCPlayer" + index.toString());
            return displayName;
         }

         if(itemStack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && itemStack.getTagCompound().hasKey("WITCEntityIDl" + index.toString()) && itemStack.getTagCompound().hasKey("WITCDisplayName" + index.toString())) {
            displayName = itemStack.getTagCompound().getString("WITCDisplayName" + index.toString());
            return displayName;
         }
      }

      return "";
   }

   public ItemTaglockKit.BoundType getBoundEntityType(ItemStack itemStack, Integer index) {
      if(itemStack.hasTagCompound()) {
         if(itemStack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
            return ItemTaglockKit.BoundType.PLAYER;
         }

         if(itemStack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && itemStack.getTagCompound().hasKey("WITCEntityIDl" + index.toString()) && itemStack.getTagCompound().hasKey("WITCDisplayName" + index.toString())) {
            ItemTaglockKit.BoundType var10000 = ItemTaglockKit.BoundType.PLAYER;
            return ItemTaglockKit.BoundType.CREATURE;
         }
      }

      return ItemTaglockKit.BoundType.NONE;
   }

   public String getBoundUsername(ItemStack itemStack, Integer index) {
      if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
         String playerName = itemStack.getTagCompound().getString("WITCPlayer" + index.toString());
         return playerName;
      } else {
         return "";
      }
   }

   public UUID getBoundCreatureID(ItemStack itemStack, Integer index) {
      if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && itemStack.getTagCompound().hasKey("WITCEntityIDl" + index.toString()) && itemStack.getTagCompound().hasKey("WITCDisplayName" + index.toString())) {
         String displayName = itemStack.getTagCompound().getString("WITCDisplayName" + index.toString());
         UUID entityID = new UUID(itemStack.getTagCompound().getLong("WITCEntityIDm" + index.toString()), itemStack.getTagCompound().getLong("WITCEntityIDl" + index.toString()));
         return entityID;
      } else {
         return new UUID(0L, 0L);
      }
   }

   public void addTagLockToPoppet(ItemStack stackTaglockKit, ItemStack stackPoppet, Integer index) {
      assert stackTaglockKit != null : "stack of taglock kit cannot be null";

      assert stackPoppet != null : "stack poppet cannot be null";

      Integer tagLockIndex = Integer.valueOf(1);
      if(!stackPoppet.hasTagCompound()) {
         stackPoppet.setTagCompound(new NBTTagCompound());
      }

      if(stackTaglockKit.hasTagCompound()) {
         if(stackTaglockKit.getTagCompound().hasKey("WITCPlayer" + tagLockIndex.toString())) {
            String playerName = stackTaglockKit.getTagCompound().getString("WITCPlayer" + tagLockIndex.toString());
            if(playerName != null) {
               stackPoppet.getTagCompound().setString("WITCPlayer" + index.toString(), playerName);
            }
         } else if(stackTaglockKit.getTagCompound().hasKey("WITCEntityIDm" + tagLockIndex.toString()) && stackTaglockKit.getTagCompound().hasKey("WITCEntityIDl" + tagLockIndex.toString()) && stackTaglockKit.getTagCompound().hasKey("WITCDisplayName" + tagLockIndex.toString())) {
            stackPoppet.getTagCompound().setLong("WITCEntityIDm" + index.toString(), stackTaglockKit.getTagCompound().getLong("WITCEntityIDm" + tagLockIndex.toString()));
            stackPoppet.getTagCompound().setLong("WITCEntityIDl" + index.toString(), stackTaglockKit.getTagCompound().getLong("WITCEntityIDl" + tagLockIndex.toString()));
            stackPoppet.getTagCompound().setString("WITCDisplayName" + index.toString(), stackTaglockKit.getTagCompound().getString("WITCDisplayName" + tagLockIndex.toString()));
         }
      }

   }

   public boolean containsTaglockForEntity(ItemStack itemStack, Entity entity, Integer index) {
      if(itemStack.hasTagCompound()) {
         if(entity instanceof EntityPlayer) {
            EntityPlayer entityID = (EntityPlayer)entity;
            if(itemStack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
               String playerName = itemStack.getTagCompound().getString("WITCPlayer" + index.toString());
               if(playerName != null && playerName.equals(entityID.getCommandSenderName())) {
                  return true;
               }
            }
         } else if(entity instanceof EntityLiving && itemStack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && itemStack.getTagCompound().hasKey("WITCEntityIDl" + index.toString())) {
            UUID entityID1 = new UUID(itemStack.getTagCompound().getLong("WITCEntityIDm" + index.toString()), itemStack.getTagCompound().getLong("WITCEntityIDl" + index.toString()));
            if(entityID1.equals(entity.getPersistentID())) {
               return true;
            }
         }
      }

      return false;
   }

   public EntityLivingBase getBoundEntity(World world, Entity entity, ItemStack stack, Integer index) {
      if(stack.hasTagCompound()) {
         MinecraftServer i$;
         WorldServer[] obj;
         int living;
         int livingID;
         WorldServer worldServer;
         Iterator i$1;
         Object obj1;
         Object var17;
         Iterator var16;
         if(stack.getTagCompound().hasKey("WITCPlayer" + index.toString())) {
            String entityID = stack.getTagCompound().getString("WITCPlayer" + index.toString());
            if(entityID != null && !entityID.isEmpty()) {
               if(!world.isRemote) {
                  i$ = MinecraftServer.getServer();
                  obj = i$.worldServers;
                  living = obj.length;

                  for(livingID = 0; livingID < living; ++livingID) {
                     worldServer = obj[livingID];
                     i$1 = worldServer.playerEntities.iterator();

                     while(i$1.hasNext()) {
                        obj1 = i$1.next();
                        EntityPlayer var21 = (EntityPlayer)obj1;
                        if(var21.getCommandSenderName().equalsIgnoreCase(entityID)) {
                           return var21;
                        }
                     }
                  }
               } else {
                  var16 = world.playerEntities.iterator();

                  while(var16.hasNext()) {
                     var17 = var16.next();
                     EntityPlayer var19 = (EntityPlayer)var17;
                     if(var19.getCommandSenderName().equalsIgnoreCase(entityID)) {
                        return var19;
                     }
                  }
               }

               return null;
            }
         }

         if(stack.getTagCompound().hasKey("WITCEntityIDm" + index.toString()) && stack.getTagCompound().hasKey("WITCEntityIDl" + index.toString())) {
            UUID var15 = new UUID(stack.getTagCompound().getLong("WITCEntityIDm" + index.toString()), stack.getTagCompound().getLong("WITCEntityIDl" + index.toString()));
            if(!world.isRemote) {
               i$ = MinecraftServer.getServer();
               obj = i$.worldServers;
               living = obj.length;

               for(livingID = 0; livingID < living; ++livingID) {
                  worldServer = obj[livingID];
                  i$1 = worldServer.loadedEntityList.iterator();

                  while(i$1.hasNext()) {
                     obj1 = i$1.next();
                     if(obj1 instanceof EntityLivingBase) {
                        EntityLivingBase living1 = (EntityLivingBase)obj1;
                        UUID livingID1 = living1.getPersistentID();
                        if(var15.equals(livingID1) && living1.isEntityAlive()) {
                           return living1;
                        }
                     }
                  }
               }
            } else {
               var16 = world.loadedEntityList.iterator();

               while(var16.hasNext()) {
                  var17 = var16.next();
                  if(var17 instanceof EntityLivingBase) {
                     EntityLivingBase var18 = (EntityLivingBase)var17;
                     UUID var20 = var18.getPersistentID();
                     if(var15.equals(var20) && var18.isEntityAlive()) {
                        return var18;
                     }
                  }
               }
            }

            return null;
         }
      }

      return null;
   }


   public static enum BoundType {

      NONE("NONE", 0),
      PLAYER("PLAYER", 1),
      CREATURE("CREATURE", 2);
      // $FF: synthetic field
      private static final ItemTaglockKit.BoundType[] $VALUES = new ItemTaglockKit.BoundType[]{NONE, PLAYER, CREATURE};


      private BoundType(String var1, int var2) {}

   }

   public static class PlayerComparitor implements Comparator<EntityPlayer> {

      @Override
      public int compare(EntityPlayer p1, EntityPlayer p2) {
         return p1.getCommandSenderName().compareTo(p2.getCommandSenderName());
      }
   }
}
