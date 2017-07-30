package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.rites.RiteCurseCreature;
import com.emoniph.witchery.ritual.rites.RiteTeleportEntity;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class BlockAreaMarker extends BlockBaseContainer {

   public BlockAreaMarker(Class clazzTile) {
      super(Material.rock, clazzTile);
      this.setBlockUnbreakable();
      this.setResistance(9999.0F);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.15F, 0.0F, 0.15F, 0.85F, 0.5F, 0.85F);
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l == 0) {
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
      }

      if(l == 1) {
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
      }

      if(l == 2) {
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
      }

      if(l == 3) {
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);
      }

      if(!world.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockAreaMarker.TileEntityAreaMarker) {
            BlockAreaMarker.TileEntityAreaMarker marker = (BlockAreaMarker.TileEntityAreaMarker)tile;
            marker.setOwner(player.getCommandSenderName());
         }
      }

   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
      if(!world.isRemote & player != null) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockAreaMarker.TileEntityAreaMarker) {
            BlockAreaMarker.TileEntityAreaMarker marker = (BlockAreaMarker.TileEntityAreaMarker)tile;
            if(player.capabilities.isCreativeMode || player.getCommandSenderName().equals(marker.getOwner()) && player.isSneaking()) {
               for(int dy = y; world.getBlock(x, dy, z) == this; ++dy) {
                  world.setBlockToAir(x, dy, z);
                  world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 0.5D + (double)dy, 0.5D + (double)z, new ItemStack(this)));
               }
            }
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if(tile != null && tile instanceof BlockAreaMarker.TileEntityAreaMarker) {
         BlockAreaMarker.TileEntityAreaMarker marker = (BlockAreaMarker.TileEntityAreaMarker)tile;
         return marker.activateBlock(world, x, y, z, player, side);
      } else {
         return false;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return 0;
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      super.blockIcon = par1IconRegister.registerIcon("stone");
   }

   public static class AreaMarkerEventHooks {

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void onLivingDeath(LivingDeathEvent event) {
         if(!event.isCanceled() && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityPlayer && event.source.getSourceOfDamage() != event.entityLiving) {
            EntityPlayer attacker = (EntityPlayer)event.source.getSourceOfDamage();
            Iterator i$ = BlockAreaMarker.AreaMarkerRegistry.instance().tiles.iterator();

            while(i$.hasNext()) {
               BlockAreaMarker.TileEntityAreaMarker tile = (BlockAreaMarker.TileEntityAreaMarker)i$.next();
               if(tile.isNear(attacker)) {
                  tile.setKiller(attacker);
               }
            }
         }

      }
   }

   public abstract static class TileEntityAreaMarker extends TileEntityBase {

      private static final String OWNER_KEY = "WITCPlacer";
      private String owner;
      private ArrayList killers = new ArrayList();


      protected void initiate() {
         super.initiate();
         if(!super.worldObj.isRemote) {
            if(super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord) == this.getExpectedBlockType()) {
               BlockAreaMarker.AreaMarkerRegistry.instance().add(this);
            } else {
               Log.instance().warning("Area Marker tile entity exists without a corresponding block at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
            }
         }

      }

      public void invalidate() {
         super.invalidate();
         if(!super.worldObj.isRemote) {
            BlockAreaMarker.AreaMarkerRegistry.instance().remove(this);
         }

      }

      public void setOwner(String username) {
         this.owner = username;
      }

      public String getOwner() {
         return this.owner != null?this.owner:"";
      }

      public void setKiller(EntityPlayer player) {
         String username = player.getCommandSenderName();
         if(!this.killers.contains(username)) {
            this.killers.add(username);
         }

      }

      public boolean checkIsProtected(EntityLivingBase entity, Rite rite) {
         if(!this.isNear(entity)) {
            return false;
         } else {
            boolean killer = entity instanceof EntityPlayer && this.killers.contains(entity.getCommandSenderName());
            return this.isProtected(entity, killer, rite);
         }
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         nbtTag.setString("WITCPlacer", this.getOwner());
         NBTTagList nbtKillers = new NBTTagList();
         Iterator i$ = this.killers.iterator();

         while(i$.hasNext()) {
            String killer = (String)i$.next();
            nbtKillers.appendTag(new NBTTagString(killer));
         }

         nbtTag.setTag("Killers", nbtKillers);
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         if(nbtTag.hasKey("WITCPlacer")) {
            this.owner = nbtTag.getString("WITCPlacer");
         } else {
            this.owner = "";
         }

         NBTTagList nbtKillers = nbtTag.getTagList("Killers", 8);
         int i = 0;

         for(int count = nbtKillers.tagCount(); i < count; ++i) {
            this.killers.add(nbtKillers.getStringTagAt(i));
         }

      }

      public abstract boolean activateBlock(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6);

      protected abstract boolean isNear(EntityLivingBase var1);

      protected abstract boolean isProtected(EntityLivingBase var1, boolean var2, Rite var3);

      protected abstract Block getExpectedBlockType();
   }

   public static class TileEntityAreaTeleportPullProtect extends BlockAreaMarker.TileEntityAreaMarker {

      public boolean activateBlock(World world, int x, int y, int z, EntityPlayer player, int side) {
         return false;
      }

      protected boolean isProtected(EntityLivingBase entity, boolean killer, Rite rite) {
         return !killer && Config.instance().allowDecurseTeleport && (rite == null || rite instanceof RiteTeleportEntity);
      }

      protected boolean isNear(EntityLivingBase entity) {
         int RADIUS = Config.instance().decurseTeleportPullRadius;
         int RADIUS_SQ = RADIUS * RADIUS;
         boolean inRange = Coord.distanceSq(entity.posX, 1.0D, entity.posZ, (double)super.xCoord, 1.0D, (double)super.zCoord) <= (double)RADIUS_SQ;
         return inRange && super.worldObj.provider.dimensionId == entity.dimension;
      }

      protected Block getExpectedBlockType() {
         return Witchery.Blocks.DECURSE_TELEPORT;
      }
   }

   public static class TileEntityAreaCurseProtect extends BlockAreaMarker.TileEntityAreaMarker {

      public boolean activateBlock(World world, int x, int y, int z, EntityPlayer player, int side) {
         return false;
      }

      protected boolean isProtected(EntityLivingBase entity, boolean killer, Rite rite) {
         return !killer && Config.instance().allowDecurseDirected && (rite == null || rite instanceof RiteCurseCreature);
      }

      protected boolean isNear(EntityLivingBase entity) {
         int RADIUS = Config.instance().decurseDirectedRadius;
         int RADIUS_SQ = RADIUS * RADIUS;
         boolean inRange = Coord.distanceSq(entity.posX, 1.0D, entity.posZ, (double)super.xCoord, 1.0D, (double)super.zCoord) <= (double)RADIUS_SQ;
         return inRange && super.worldObj.provider.dimensionId == entity.dimension;
      }

      protected Block getExpectedBlockType() {
         return Witchery.Blocks.DECURSE_DIRECTED;
      }
   }

   public static class AreaMarkerRegistry {

      private static BlockAreaMarker.AreaMarkerRegistry INSTANCE_CLIENT;
      private static BlockAreaMarker.AreaMarkerRegistry INSTANCE_SERVER;
      private final ArrayList tiles = new ArrayList();


      public static BlockAreaMarker.AreaMarkerRegistry instance() {
         return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER?INSTANCE_SERVER:INSTANCE_CLIENT;
      }

      public static void serverStart() {
         INSTANCE_CLIENT = new BlockAreaMarker.AreaMarkerRegistry();
         INSTANCE_SERVER = new BlockAreaMarker.AreaMarkerRegistry();
      }

      private void add(BlockAreaMarker.TileEntityAreaMarker tile) {
         if(!this.tiles.contains(tile)) {
            try {
               Iterator e = this.tiles.iterator();

               while(e.hasNext()) {
                  BlockAreaMarker.TileEntityAreaMarker source = (BlockAreaMarker.TileEntityAreaMarker)e.next();
                  if(source == null || source.isInvalid() || source.xCoord == tile.xCoord && source.yCoord == tile.yCoord && source.zCoord == tile.zCoord) {
                     e.remove();
                  }
               }
            } catch (Throwable var4) {
               Log.instance().warning(var4, "Exception occured validating existing power source entries");
            }

            this.tiles.add(tile);
         }

      }

      private void remove(BlockAreaMarker.TileEntityAreaMarker tile) {
         if(this.tiles.contains(tile)) {
            this.tiles.remove(tile);
         }

         try {
            Iterator e = this.tiles.iterator();

            while(e.hasNext()) {
               BlockAreaMarker.TileEntityAreaMarker source = (BlockAreaMarker.TileEntityAreaMarker)e.next();
               if(source != null && !source.isInvalid()) {
                  if(source.getWorldObj().getTileEntity(source.xCoord, source.yCoord, source.zCoord) != source) {
                     e.remove();
                  }
               } else {
                  e.remove();
               }
            }
         } catch (Throwable var4) {
            Log.instance().warning(var4, "Exception occured removing existing power source entries");
         }

      }

      public boolean isProtectionActive(EntityLivingBase entity, Rite rite) {
         Iterator i$ = this.tiles.iterator();

         BlockAreaMarker.TileEntityAreaMarker tile;
         do {
            if(!i$.hasNext()) {
               return false;
            }

            tile = (BlockAreaMarker.TileEntityAreaMarker)i$.next();
         } while(!tile.checkIsProtected(entity, rite));

         return true;
      }
   }
}
