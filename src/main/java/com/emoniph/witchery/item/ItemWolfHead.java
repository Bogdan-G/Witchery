package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockWolfHead;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.MultiItemBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemWolfHead extends MultiItemBlock {

   private static final String[] skullTypes = new String[]{"wolf", "hellhound"};
   public static final String[] field_94587_a = new String[]{"wolf", "hellhound"};
   @SideOnly(Side.CLIENT)
   private IIcon[] field_94586_c;


   public ItemWolfHead(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setTextureName("witchery:wolfhead");
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if(side == 0) {
         return false;
      } else if(!world.getBlock(x, y, z).getMaterial().isSolid()) {
         return false;
      } else {
         if(side == 1) {
            ++y;
         }

         if(side == 2) {
            --z;
         }

         if(side == 3) {
            ++z;
         }

         if(side == 4) {
            --x;
         }

         if(side == 5) {
            ++x;
         }

         if(!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
         } else if(!Witchery.Blocks.WOLFHEAD.canPlaceBlockAt(world, x, y, z)) {
            return false;
         } else {
            if(!world.isRemote) {
               world.setBlock(x, y, z, Witchery.Blocks.WOLFHEAD, side, 3);
               int i1 = 0;
               if(side == 1) {
                  i1 = MathHelper.floor_double((double)(player.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
               }

               BlockWolfHead.TileEntityWolfHead tile = (BlockWolfHead.TileEntityWolfHead)BlockUtil.getTileEntity(world, x, y, z, BlockWolfHead.TileEntityWolfHead.class);
               if(tile != null) {
                  tile.setSkullType(stack.getItemDamage());
                  tile.setRotation(i1);
               }
            }

            --stack.stackSize;
            return true;
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
      for(int i = 0; i < skullTypes.length; ++i) {
         p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
      }

   }

   public int getMetadata(int p_77647_1_) {
      return p_77647_1_;
   }

   public String getUnlocalizedName(ItemStack p_77667_1_) {
      int i = p_77667_1_.getItemDamage();
      if(i < 0 || i >= skullTypes.length) {
         i = 0;
      }

      return super.getUnlocalizedName() + "." + skullTypes[i];
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int p_77617_1_) {
      if(p_77617_1_ < 0 || p_77617_1_ >= skullTypes.length) {
         p_77617_1_ = 0;
      }

      return this.field_94586_c[p_77617_1_];
   }

   public String getItemStackDisplayName(ItemStack p_77653_1_) {
      return super.getItemStackDisplayName(p_77653_1_);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister p_94581_1_) {
      this.field_94586_c = new IIcon[field_94587_a.length];

      for(int i = 0; i < field_94587_a.length; ++i) {
         this.field_94586_c[i] = p_94581_1_.registerIcon(this.getIconString() + "_" + field_94587_a[i]);
      }

   }

   protected String[] getNames() {
      return skullTypes;
   }

}
