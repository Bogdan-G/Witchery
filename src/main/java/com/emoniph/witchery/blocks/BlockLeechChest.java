package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.ChatUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLeechChest extends BlockBaseContainer {

   private final Random random = new Random();
   public final int chestType = 1;


   public BlockLeechChest() {
      super(Material.rock, BlockLeechChest.TileEntityLeechChest.class);
      this.setHardness(25.0F);
      this.setResistance(1000.0F);
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      if(par1IBlockAccess.getBlock(par2, par3, par4 - 1) == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
      } else if(par1IBlockAccess.getBlock(par2, par3, par4 + 1) == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
      } else if(par1IBlockAccess.getBlock(par2 - 1, par3, par4) == this) {
         this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      } else if(par1IBlockAccess.getBlock(par2 + 1, par3, par4) == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
      } else {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      }

   }

   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      Block l = par1World.getBlock(par2, par3, par4 - 1);
      Block i1 = par1World.getBlock(par2, par3, par4 + 1);
      Block j1 = par1World.getBlock(par2 - 1, par3, par4);
      Block k1 = par1World.getBlock(par2 + 1, par3, par4);
      byte b0 = 0;
      int l1 = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l1 == 0) {
         b0 = 2;
      }

      if(l1 == 1) {
         b0 = 5;
      }

      if(l1 == 2) {
         b0 = 3;
      }

      if(l1 == 3) {
         b0 = 4;
      }

      if(l != this && i1 != this && j1 != this && k1 != this) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
      } else {
         if((l == this || i1 == this) && (b0 == 4 || b0 == 5)) {
            if(l == this) {
               par1World.setBlockMetadataWithNotify(par2, par3, par4 - 1, b0, 3);
            } else {
               par1World.setBlockMetadataWithNotify(par2, par3, par4 + 1, b0, 3);
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
         }

         if((j1 == this || k1 == this) && (b0 == 2 || b0 == 3)) {
            if(j1 == this) {
               par1World.setBlockMetadataWithNotify(par2 - 1, par3, par4, b0, 3);
            } else {
               par1World.setBlockMetadataWithNotify(par2 + 1, par3, par4, b0, 3);
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
         }
      }

      TileEntity tile = par1World.getTileEntity(par2, par3, par4);
      if(tile != null && tile instanceof BlockLeechChest.TileEntityLeechChest) {
         BlockLeechChest.TileEntityLeechChest chest = (BlockLeechChest.TileEntityLeechChest)tile;
         if(par6ItemStack.hasDisplayName()) {
            chest.setChestGuiName(par6ItemStack.getDisplayName());
         }

         if(!par1World.isRemote && par6ItemStack.hasTagCompound() && par6ItemStack.getTagCompound().hasKey("WITCPlayers")) {
            NBTTagList nbtPlayersList = par6ItemStack.getTagCompound().getTagList("WITCPlayers", 10);
            chest.players.clear();

            for(int i = 0; i < nbtPlayersList.tagCount(); ++i) {
               NBTTagCompound nbtPlayer = nbtPlayersList.getCompoundTagAt(i);
               String s = nbtPlayer.getString("Player");
               if(s != null && !s.isEmpty()) {
                  chest.players.add(s);
               }
            }

            chest.sync();
         }
      }

   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return true;
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
      TileEntity tile = par1World.getTileEntity(par2, par3, par4);
      if(tile != null && tile instanceof BlockLeechChest.TileEntityLeechChest) {
         BlockLeechChest.TileEntityLeechChest tileentitychest = (BlockLeechChest.TileEntityLeechChest)tile;
         tileentitychest.updateContainingBlockInfo();
      }

   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
      if(tileentity != null && tileentity instanceof BlockLeechChest.TileEntityLeechChest) {
         BlockLeechChest.TileEntityLeechChest tileentitychest = (BlockLeechChest.TileEntityLeechChest)tileentity;

         for(int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1) {
            ItemStack itemstack = tileentitychest.getStackInSlot(j1);
            if(itemstack != null) {
               float f = this.random.nextFloat() * 0.8F + 0.1F;
               float f1 = this.random.nextFloat() * 0.8F + 0.1F;

               EntityItem entityitem;
               for(float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
                  int k1 = this.random.nextInt(21) + 10;
                  if(k1 > itemstack.stackSize) {
                     k1 = itemstack.stackSize;
                  }

                  itemstack.stackSize -= k1;
                  entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                  float f3 = 0.05F;
                  entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                  entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                  entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);
                  if(itemstack.hasTagCompound()) {
                     entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                  }
               }
            }
         }

         par1World.func_147453_f(par2, par3, par4, par5);
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList drops = new ArrayList();
      return drops;
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      if(!par1World.isRemote) {
         ItemStack itemstack = new ItemStack(this);
         TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
         if(tileentity != null && tileentity instanceof BlockLeechChest.TileEntityLeechChest) {
            BlockLeechChest.TileEntityLeechChest chest = (BlockLeechChest.TileEntityLeechChest)tileentity;
            if(chest.players.size() > 0) {
               itemstack.setTagCompound(new NBTTagCompound());
               NBTTagList nbtPlayers = new NBTTagList();

               for(int i = 0; i < chest.players.size(); ++i) {
                  NBTTagCompound nbtPlayer = new NBTTagCompound();
                  nbtPlayer.setString("Player", (String)chest.players.get(i));
                  nbtPlayers.appendTag(nbtPlayer);
               }

               itemstack.getTagCompound().setTag("WITCPlayers", nbtPlayers);
            }
         }

         this.dropBlockAsItem(par1World, par2, par3, par4, itemstack);
      }

   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(par1World.isRemote) {
         return true;
      } else {
         IInventory iinventory = this.getInventory(par1World, par2, par3, par4);
         if(iinventory != null) {
            TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
            if(tileEntity != null && tileEntity instanceof BlockLeechChest.TileEntityLeechChest) {
               BlockLeechChest.TileEntityLeechChest chest = (BlockLeechChest.TileEntityLeechChest)tileEntity;
               chest.storePlayer(player);
            }

            player.displayGUIChest(iinventory);
         }

         return true;
      }
   }

   public IInventory getInventory(World par1World, int par2, int par3, int par4) {
      TileEntity object = par1World.getTileEntity(par2, par3, par4);
      return object == null?null:(par1World.isSideSolid(par2, par3 + 1, par4, ForgeDirection.DOWN)?null:(isOcelotBlockingChest(par1World, par2, par3, par4)?null:(par1World.getBlock(par2 - 1, par3, par4) == this && (par1World.isSideSolid(par2 - 1, par3 + 1, par4, ForgeDirection.DOWN) || isOcelotBlockingChest(par1World, par2 - 1, par3, par4))?null:(par1World.getBlock(par2 + 1, par3, par4) == this && (par1World.isSideSolid(par2 + 1, par3 + 1, par4, ForgeDirection.DOWN) || isOcelotBlockingChest(par1World, par2 + 1, par3, par4))?null:(par1World.getBlock(par2, par3, par4 - 1) == this && (par1World.isSideSolid(par2, par3 + 1, par4 - 1, ForgeDirection.DOWN) || isOcelotBlockingChest(par1World, par2, par3, par4 - 1))?null:(par1World.getBlock(par2, par3, par4 + 1) == this && (par1World.isSideSolid(par2, par3 + 1, par4 + 1, ForgeDirection.DOWN) || isOcelotBlockingChest(par1World, par2, par3, par4 + 1))?null:(IInventory)object))))));
   }

   public boolean canProvidePower() {
      return this.chestType == 1;
   }

   public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      if(!this.canProvidePower()) {
         return 0;
      } else {
         TileEntity tile = par1IBlockAccess.getTileEntity(par2, par3, par4);
         if(tile != null && tile instanceof BlockLeechChest.TileEntityLeechChest) {
            int i1 = ((BlockLeechChest.TileEntityLeechChest)tile).numUsingPlayers;
            return MathHelper.clamp_int(i1, 0, 15);
         } else {
            return 0;
         }
      }
   }

   public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return par5 == 1?this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5):0;
   }

   public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3) {
      Iterator iterator = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox((double)par1, (double)(par2 + 1), (double)par3, (double)(par1 + 1), (double)(par2 + 2), (double)(par3 + 1))).iterator();

      while(iterator.hasNext()) {
         EntityOcelot entityocelot1 = (EntityOcelot)iterator.next();
         if(entityocelot1.isSitting()) {
            return true;
         }
      }

      return false;
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      super.blockIcon = par1IconRegister.registerIcon("planks_oak");
   }

   public static class TileEntityLeechChest extends TileEntity implements IInventory {

      private ItemStack[] chestContents = new ItemStack[36];
      public boolean adjacentChestChecked;
      public TileEntity adjacentChestZNeg;
      public TileEntity adjacentChestXPos;
      public TileEntity adjacentChestXNeg;
      public TileEntity adjacentChestZPosition;
      public float lidAngle;
      public float prevLidAngle;
      public int numUsingPlayers;
      private int ticksSinceSync;
      private int cachedChestType;
      private String customName;
      public ArrayList players = new ArrayList();


      public TileEntityLeechChest() {
         this.cachedChestType = -1;
      }

      public void sync() {
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      public void storePlayer(EntityPlayer player) {
         if(!super.worldObj.isRemote && player != null && !this.players.contains(player.getCommandSenderName())) {
            this.players.add(player.getCommandSenderName());

            while(this.players.size() > 3) {
               this.players.remove(0);
            }

            this.sync();
         }

      }

      public String popUserExcept(EntityPlayer usingPlayer) {
         String missingPlayers = "";

         for(int i = this.players.size() - 1; i >= 0; --i) {
            String foundPlayerName = (String)this.players.get(i);
            if(!foundPlayerName.equals(usingPlayer.getCommandSenderName())) {
               if(usingPlayer.worldObj.getPlayerEntityByName(foundPlayerName) != null) {
                  this.players.remove(i);
                  this.sync();
                  return foundPlayerName;
               }

               missingPlayers = missingPlayers + foundPlayerName + " ";
            } else if(this.players.size() == 1) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, usingPlayer, "tile.witcheryLeechChest.onlyowntaglock", new Object[0]);
               return null;
            }
         }

         if(!missingPlayers.isEmpty()) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, usingPlayer, "tile.witcheryLeechChest.playernotloggedin", new Object[]{missingPlayers});
         }

         return null;
      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         this.readFromNBT(packet.func_148857_g());
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }

      @SideOnly(Side.CLIENT)
      public TileEntityLeechChest(int par1) {
         this.cachedChestType = par1;
      }

      public int getSizeInventory() {
         return 27;
      }

      public ItemStack getStackInSlot(int par1) {
         return this.chestContents[par1];
      }

      public ItemStack decrStackSize(int par1, int par2) {
         if(this.chestContents[par1] != null) {
            ItemStack itemstack;
            if(this.chestContents[par1].stackSize <= par2) {
               itemstack = this.chestContents[par1];
               this.chestContents[par1] = null;
               this.markDirty();
               return itemstack;
            } else {
               itemstack = this.chestContents[par1].splitStack(par2);
               if(this.chestContents[par1].stackSize == 0) {
                  this.chestContents[par1] = null;
               }

               this.markDirty();
               return itemstack;
            }
         } else {
            return null;
         }
      }

      public ItemStack getStackInSlotOnClosing(int par1) {
         if(this.chestContents[par1] != null) {
            ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
         } else {
            return null;
         }
      }

      public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
         this.chestContents[par1] = par2ItemStack;
         if(par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
         }

         this.markDirty();
      }

      public String getInventoryName() {
         return this.hasCustomInventoryName()?this.customName:"container.chest";
      }

      public boolean hasCustomInventoryName() {
         return this.customName != null && this.customName.length() > 0;
      }

      public void setChestGuiName(String par1Str) {
         this.customName = par1Str;
      }

      public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
         super.readFromNBT(par1NBTTagCompound);
         NBTTagList nbtItemsList = par1NBTTagCompound.getTagList("Items", 10);
         this.chestContents = new ItemStack[this.getSizeInventory()];
         if(par1NBTTagCompound.hasKey("CustomName")) {
            this.customName = par1NBTTagCompound.getString("CustomName");
         }

         for(int nbtPlayersList = 0; nbtPlayersList < nbtItemsList.tagCount(); ++nbtPlayersList) {
            NBTTagCompound i = nbtItemsList.getCompoundTagAt(nbtPlayersList);
            int nbtPlayer = i.getByte("Slot") & 255;
            if(nbtPlayer >= 0 && nbtPlayer < this.chestContents.length) {
               this.chestContents[nbtPlayer] = ItemStack.loadItemStackFromNBT(i);
            }
         }

         this.players.clear();
         NBTTagList var7 = par1NBTTagCompound.getTagList("WITCPlayers", 10);

         for(int var8 = 0; var8 < var7.tagCount(); ++var8) {
            NBTTagCompound var9 = var7.getCompoundTagAt(var8);
            String s = var9.getString("Player");
            if(s != null && !s.isEmpty()) {
               this.players.add(s);
            }
         }

      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         NBTTagList nbtItemsList = new NBTTagList();

         for(int nbtPlayers = 0; nbtPlayers < this.chestContents.length; ++nbtPlayers) {
            if(this.chestContents[nbtPlayers] != null) {
               NBTTagCompound i = new NBTTagCompound();
               i.setByte("Slot", (byte)nbtPlayers);
               this.chestContents[nbtPlayers].writeToNBT(i);
               nbtItemsList.appendTag(i);
            }
         }

         nbtTag.setTag("Items", nbtItemsList);
         if(this.hasCustomInventoryName()) {
            nbtTag.setString("CustomName", this.customName);
         }

         NBTTagList var6 = new NBTTagList();

         for(int var7 = 0; var7 < this.players.size(); ++var7) {
            NBTTagCompound nbtPlayer = new NBTTagCompound();
            nbtPlayer.setString("Player", (String)this.players.get(var7));
            var6.appendTag(nbtPlayer);
         }

         nbtTag.setTag("WITCPlayers", var6);
      }

      public int getInventoryStackLimit() {
         return 64;
      }

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void updateContainingBlockInfo() {
         super.updateContainingBlockInfo();
         this.adjacentChestChecked = false;
      }

      public void updateEntity() {
         super.updateEntity();
         ++this.ticksSinceSync;
         float f;
         if(!super.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + super.xCoord + super.yCoord + super.zCoord) % 200 == 0) {
            this.numUsingPlayers = 0;
            f = 5.0F;
            List d0 = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)((float)super.xCoord - f), (double)((float)super.yCoord - f), (double)((float)super.zCoord - f), (double)((float)(super.xCoord + 1) + f), (double)((float)(super.yCoord + 1) + f), (double)((float)(super.zCoord + 1) + f)));
            Iterator iterator = d0.iterator();

            while(iterator.hasNext()) {
               EntityPlayer f1 = (EntityPlayer)iterator.next();
               if(f1.openContainer instanceof ContainerChest) {
                  IInventory f2 = ((ContainerChest)f1.openContainer).getLowerChestInventory();
                  if(f2 == this) {
                     ++this.numUsingPlayers;
                  }
               }
            }
         }

         this.prevLidAngle = this.lidAngle;
         f = 0.1F;
         double var8;
         if(this.numUsingPlayers > 0 && this.lidAngle == 0.0F) {
            double var9 = (double)super.xCoord + 0.5D;
            var8 = (double)super.zCoord + 0.5D;
            super.worldObj.playSoundEffect(var9, (double)super.yCoord + 0.5D, var8, "random.chestopen", 0.5F, super.worldObj.rand.nextFloat() * 0.1F + 0.9F);
         }

         if(this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F) {
            float var10 = this.lidAngle;
            if(this.numUsingPlayers > 0) {
               this.lidAngle += f;
            } else {
               this.lidAngle -= f;
            }

            if(this.lidAngle > 1.0F) {
               this.lidAngle = 1.0F;
            }

            float var11 = 0.5F;
            if(this.lidAngle < var11 && var10 >= var11) {
               var8 = (double)super.xCoord + 0.5D;
               double d2 = (double)super.zCoord + 0.5D;
               super.worldObj.playSoundEffect(var8, (double)super.yCoord + 0.5D, d2, "random.chestclosed", 0.5F, super.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if(this.lidAngle < 0.0F) {
               this.lidAngle = 0.0F;
            }
         }

      }

      public boolean receiveClientEvent(int par1, int par2) {
         if(par1 == 1) {
            this.numUsingPlayers = par2;
            return true;
         } else {
            return super.receiveClientEvent(par1, par2);
         }
      }

      public void openInventory() {
         if(this.numUsingPlayers < 0) {
            this.numUsingPlayers = 0;
         }

         ++this.numUsingPlayers;
         super.worldObj.addBlockEvent(super.xCoord, super.yCoord, super.zCoord, this.getBlockType(), 1, this.numUsingPlayers);
         super.worldObj.notifyBlocksOfNeighborChange(super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
         super.worldObj.notifyBlocksOfNeighborChange(super.xCoord, super.yCoord - 1, super.zCoord, this.getBlockType());
      }

      public void closeInventory() {
         if(this.getBlockType() != null && this.getBlockType() instanceof BlockLeechChest) {
            --this.numUsingPlayers;
            super.worldObj.addBlockEvent(super.xCoord, super.yCoord, super.zCoord, this.getBlockType(), 1, this.numUsingPlayers);
            super.worldObj.notifyBlocksOfNeighborChange(super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
            super.worldObj.notifyBlocksOfNeighborChange(super.xCoord, super.yCoord - 1, super.zCoord, this.getBlockType());
         }

      }

      public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
         return true;
      }

      public void invalidate() {
         super.invalidate();
         this.updateContainingBlockInfo();
      }

      public int getChestType() {
         if(this.cachedChestType == -1) {
            if(super.worldObj == null || !(this.getBlockType() instanceof BlockLeechChest)) {
               return 0;
            }

            this.cachedChestType = ((BlockLeechChest)this.getBlockType()).chestType;
         }

         return this.cachedChestType;
      }
   }
}
