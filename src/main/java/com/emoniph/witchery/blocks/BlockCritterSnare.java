package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.item.ItemPolynesiaCharm;
import com.emoniph.witchery.util.MultiItemBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCritterSnare extends BlockBaseBush {

   private static final String[] CAUGHT_TYPES = new String[]{"empty", "bat", "silverfish", "slime", "magmacube"};
   private static final String[] CAUGHT_TYPES_SOUNDS = new String[]{"", "mob.bat.idle", "mob.silverfish.say", "mob.slime.small", "mob.magmacube.small"};
   @SideOnly(Side.CLIENT)
   private IIcon[] critterIcons;


   public BlockCritterSnare() {
      super(Material.plants, BlockCritterSnare.ClassItemBlock.class);
      this.setStepSound(Block.soundTypeGrass);
      float f = 0.45F;
      this.setBlockBounds(0.050000012F, 0.0F, 0.050000012F, 0.95F, 1.0F, 0.95F);
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      int meta = world.getBlockMetadata(posX, posY, posZ);
      if(meta == 0 && !world.isRemote && entity != null && entity.isEntityAlive()) {
         if(entity instanceof EntityBat) {
            boolean hasStock = ItemPolynesiaCharm.hasStockInventory((EntityBat)entity);
            world.setBlockMetadataWithNotify(posX, posY, posZ, hasStock?9:1, 3);
            entity.setDead();
         } else if(entity instanceof EntitySilverfish) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 2, 3);
            entity.setDead();
         } else if(entity instanceof EntityMagmaCube && ((EntityMagmaCube)entity).getSlimeSize() == 1) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 4, 3);
            entity.setDead();
         } else if(entity instanceof EntitySlime && ((EntitySlime)entity).getSlimeSize() == 1) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 3, 3);
            entity.setDead();
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
      int meta = world.getBlockMetadata(x, y, z);
      if(!world.isRemote && meta > 0 && player.isSneaking()) {
         world.setBlockMetadataWithNotify(x, y, z, 0, 3);
         boolean tries = false;
         int var16;
         switch(this.getCritterFromMeta(meta)) {
         case 1:
            EntityBat bat = new EntityBat(world);
            bat.setLocationAndAngles(0.5D + (double)x, 1.5D + (double)y, 0.5D + (double)z, 0.0F, 0.0F);
            if((meta & 8) == 8) {
               ItemPolynesiaCharm.setEmptyStockInventory(world, bat);
            }

            world.spawnEntityInWorld(bat);
            break;
         case 2:
            EntitySilverfish silverfish = new EntitySilverfish(world);
            silverfish.setLocationAndAngles(player.posX < (double)x?(double)x - 0.5D:(double)x + 1.5D, player.posY + 0.5D, player.posZ < (double)z?(double)z - 0.5D:(double)z + 1.5D, 0.0F, 0.0F);
            world.spawnEntityInWorld(silverfish);
            break;
         case 3:
            EntitySlime slime = null;
            var16 = 20;

            do {
               slime = new EntitySlime(world);
               if(slime.getSlimeSize() == 1) {
                  break;
               }

               --var16;
            } while(var16 > 0);

            if(var16 > 0) {
               slime.setLocationAndAngles(player.posX < (double)x?(double)x - 0.5D:(double)x + 1.5D, player.posY + 0.5D, player.posZ < (double)z?(double)z - 0.5D:(double)z + 1.5D, 0.0F, 0.0F);
               world.spawnEntityInWorld(slime);
            } else {
               world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 1.5D + (double)y, 0.5D + (double)z, new ItemStack(Items.slime_ball)));
            }
            break;
         case 4:
            EntityMagmaCube cube = null;
            var16 = 20;

            do {
               cube = new EntityMagmaCube(world);
               if(cube.getSlimeSize() == 1) {
                  break;
               }

               --var16;
            } while(var16 > 0);

            if(var16 > 0) {
               cube.setLocationAndAngles(player.posX < (double)x?(double)x - 0.5D:(double)x + 1.5D, player.posY + 0.5D, player.posZ < (double)z?(double)z - 0.5D:(double)z + 1.5D, 0.0F, 0.0F);
               world.spawnEntityInWorld(cube);
            } else {
               world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 1.5D + (double)y, 0.5D + (double)z, new ItemStack(Items.magma_cream)));
            }
         }
      }

      return true;
   }

   private int getCritterFromMeta(int meta) {
      int critter = meta & 7;
      if(critter < 0 || critter >= CAUGHT_TYPES.length) {
         critter = 0;
      }

      return critter;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      int critterType = this.getCritterFromMeta(meta);
      return this.critterIcons[critterType];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.critterIcons = new IIcon[CAUGHT_TYPES.length];

      for(int i = 0; i < CAUGHT_TYPES.length; ++i) {
         this.critterIcons[i] = iconRegister.registerIcon(this.getTextureName() + "_" + CAUGHT_TYPES[i]);
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if(rand.nextInt(24) == 0) {
         int meta = world.getBlockMetadata(x, y, z);
         int critterType = this.getCritterFromMeta(meta);
         if(critterType > 0 && critterType < CAUGHT_TYPES_SOUNDS.length) {
            String sound = CAUGHT_TYPES_SOUNDS[critterType];
            world.playSound((double)x, (double)y, (double)z, sound, 0.5F, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F), false);
         }
      }

   }

   public Item getItemDropped(int par1, Random rand, int fortune) {
      return Item.getItemFromBlock(this);
   }

   public int damageDropped(int metadata) {
      return metadata;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(item, 1, 0));
   }

   protected ItemStack createStackedBlock(int par1) {
      return new ItemStack(this, 1, par1);
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.canBlockStay(par1World, par2, par3, par4);
   }

   protected boolean canPlaceBlockOn(Block block) {
      return block != null && block.isOpaqueCube();
   }

   public boolean canBlockStay(World world, int posX, int posY, int posZ) {
      Material material = world.getBlock(posX, posY - 1, posZ).getMaterial();
      return material != null && material.isSolid();
   }

   public void harvestBlock(World par3World, EntityPlayer player, int par4, int par5, int par6, int damageValue) {
      super.harvestBlock(par3World, player, par4, par5, par6, damageValue);
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockCritterSnare.CAUGHT_TYPES;
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIconFromDamage(int par1) {
         return super.field_150939_a.getIcon(0, par1);
      }
   }
}
