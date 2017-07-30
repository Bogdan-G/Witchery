package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.TileEntityBrewFluid;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockBrewLiquidEffect extends BlockBaseContainer implements IFluidBlock {

   protected static final Map defaultDisplacements = Maps.newHashMap();
   protected Map displacements = Maps.newHashMap();
   protected int quantaPerBlock = 6;
   protected float quantaPerBlockFloat = 8.0F;
   protected int density = 1;
   protected int densityDir = -1;
   protected int temperature = 295;
   protected int tickRate = 20;
   protected int renderPass = 1;
   protected int maxScaledLight = 0;
   protected boolean[] isOptimalFlowDirection = new boolean[4];
   protected int[] flowCost = new int[4];
   protected FluidStack stack;
   protected final String fluidName;
   @SideOnly(Side.CLIENT)
   protected IIcon[] icons;


   public BlockBrewLiquidEffect() {
      super(Material.water, TileEntityBrewFluid.class);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setTickRandomly(true);
      super.registerWithCreateTab = false;
      this.disableStats();
      Fluid fluid = Witchery.Fluids.BREW_LIQUID;
      this.fluidName = fluid.getName();
      this.density = fluid.getDensity();
      this.temperature = fluid.getTemperature();
      this.maxScaledLight = fluid.getLuminosity();
      this.tickRate = fluid.getViscosity() / 200;
      this.densityDir = fluid.getDensity() > 0?-1:1;
      fluid.setBlock(this);
      this.stack = new FluidStack(fluid, 1000);
      this.displacements.putAll(defaultDisplacements);
   }

   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      TileEntityBrewFluid fluid = (TileEntityBrewFluid)BlockUtil.getTileEntity(world, x, y, z, TileEntityBrewFluid.class);
      return fluid != null?fluid.color:68;
   }

   public BlockBrewLiquidEffect setFluidStack(FluidStack stack) {
      this.stack = stack;
      return this;
   }

   public BlockBrewLiquidEffect setFluidStackAmount(int amount) {
      this.stack.amount = amount;
      return this;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side != 0 && side != 1?this.icons[1]:this.icons[0];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[]{iconRegister.registerIcon(this.getTextureName() + "_still"), iconRegister.registerIcon(this.getTextureName() + "_flow")};
   }

   public BlockBrewLiquidEffect setQuantaPerBlock(int quantaPerBlock) {
      if(quantaPerBlock > 16 || quantaPerBlock < 1) {
         quantaPerBlock = 8;
      }

      this.quantaPerBlock = quantaPerBlock;
      this.quantaPerBlockFloat = (float)quantaPerBlock;
      return this;
   }

   public BlockBrewLiquidEffect setDensity(int density) {
      if(density == 0) {
         density = 1;
      }

      this.density = density;
      this.densityDir = density > 0?-1:1;
      return this;
   }

   public BlockBrewLiquidEffect setTemperature(int temperature) {
      this.temperature = temperature;
      return this;
   }

   public BlockBrewLiquidEffect setTickRate(int tickRate) {
      if(tickRate <= 0) {
         tickRate = 20;
      }

      this.tickRate = tickRate;
      return this;
   }

   public BlockBrewLiquidEffect setRenderPass(int renderPass) {
      this.renderPass = renderPass;
      return this;
   }

   public BlockBrewLiquidEffect setMaxScaledLight(int maxScaledLight) {
      this.maxScaledLight = maxScaledLight;
      return this;
   }

   public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
      if(world.getBlock(x, y, z).isAir(world, x, y, z)) {
         return true;
      } else if(world.getBlock(x, y, z).getMaterial().isLiquid()) {
         return false;
      } else {
         Block block = world.getBlock(x, y, z);
         if(block == this) {
            return false;
         } else if(this.displacements.containsKey(block)) {
            return ((Boolean)this.displacements.get(block)).booleanValue();
         } else {
            Material material = block.getMaterial();
            if(!material.blocksMovement() && material != Material.portal) {
               int density = getDensity(world, x, y, z);
               return density == Integer.MAX_VALUE?true:this.density > density;
            } else {
               return false;
            }
         }
      }
   }

   public boolean displaceIfPossible(World world, int x, int y, int z) {
      if(world.getBlock(x, y, z).isAir(world, x, y, z)) {
         return true;
      } else if(world.getBlock(x, y, z).getMaterial().isLiquid()) {
         return false;
      } else {
         Block block = world.getBlock(x, y, z);
         if(block == this) {
            return false;
         } else if(this.displacements.containsKey(block)) {
            if(((Boolean)this.displacements.get(block)).booleanValue()) {
               block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
               return true;
            } else {
               return false;
            }
         } else {
            Material material = block.getMaterial();
            if(!material.blocksMovement() && material != Material.portal) {
               int density = getDensity(world, x, y, z);
               if(density == Integer.MAX_VALUE) {
                  block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                  return true;
               } else {
                  return this.density > density;
               }
            } else {
               return false;
            }
         }
      }
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      world.scheduleBlockUpdate(x, y, z, this, this.tickRate);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      world.scheduleBlockUpdate(x, y, z, this, this.tickRate);
   }

   public boolean func_149698_L() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return null;
   }

   public int quantityDropped(Random par1Random) {
      return 0;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   public int tickRate(World world) {
      return this.tickRate;
   }

   public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
      if(this.densityDir <= 0) {
         Vec3 vec_flow = this.getFlowVector(world, x, y, z);
         vec.xCoord += vec_flow.xCoord * (double)(this.quantaPerBlock * 4);
         vec.yCoord += vec_flow.yCoord * (double)(this.quantaPerBlock * 4);
         vec.zCoord += vec_flow.zCoord * (double)(this.quantaPerBlock * 4);
      }
   }

   public int getRenderType() {
      return Witchery.proxy.getBrewLiquidRenderId();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
      int lightThis = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
      int lightUp = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
      int lightThisBase = lightThis & 255;
      int lightUpBase = lightUp & 255;
      int lightThisExt = lightThis >> 16 & 255;
      int lightUpExt = lightUp >> 16 & 255;
      return (lightThisBase > lightUpBase?lightThisBase:lightUpBase) | (lightThisExt > lightUpExt?lightThisExt:lightUpExt) << 16;
   }

   public int getRenderBlockPass() {
      return this.renderPass;
   }

   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      Block block = world.getBlock(x, y, z);
      return block != this?!block.isOpaqueCube():(block.getMaterial() == this.getMaterial()?false:super.shouldSideBeRendered(world, x, y, z, side));
   }

   public static final int getDensity(IBlockAccess world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return !(block instanceof BlockBrewLiquidEffect)?Integer.MAX_VALUE:((BlockBrewLiquidEffect)block).density;
   }

   public static final int getTemperature(IBlockAccess world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return !(block instanceof BlockBrewLiquidEffect)?Integer.MAX_VALUE:((BlockBrewLiquidEffect)block).temperature;
   }

   public static double getFlowDirection(IBlockAccess world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      if(!block.getMaterial().isLiquid()) {
         return -1000.0D;
      } else {
         Vec3 vec = ((BlockBrewLiquidEffect)block).getFlowVector(world, x, y, z);
         return vec.xCoord == 0.0D && vec.zCoord == 0.0D?-1000.0D:Math.atan2(vec.zCoord, vec.xCoord) - 1.5707963267948966D;
      }
   }

   public final int getQuantaValueBelow(IBlockAccess world, int x, int y, int z, int belowThis) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return quantaRemaining >= belowThis?-1:quantaRemaining;
   }

   public final int getQuantaValueAbove(IBlockAccess world, int x, int y, int z, int aboveThis) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return quantaRemaining <= aboveThis?-1:quantaRemaining;
   }

   public final float getQuantaPercentage(IBlockAccess world, int x, int y, int z) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return (float)quantaRemaining / this.quantaPerBlockFloat;
   }

   public Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
      Vec3 vec = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
      int decay = this.quantaPerBlock - this.getQuantaValue(world, x, y, z);

      for(int flag = 0; flag < 4; ++flag) {
         int x2 = x;
         int z2 = z;
         switch(flag) {
         case 0:
            x2 = x - 1;
            break;
         case 1:
            z2 = z - 1;
            break;
         case 2:
            x2 = x + 1;
            break;
         case 3:
            z2 = z + 1;
         }

         int otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y, z2);
         int power;
         if(otherDecay >= this.quantaPerBlock) {
            if(!world.getBlock(x2, y, z2).getMaterial().blocksMovement()) {
               otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y - 1, z2);
               if(otherDecay >= 0) {
                  power = otherDecay - (decay - this.quantaPerBlock);
                  vec = vec.addVector((double)((x2 - x) * power), (double)((y - y) * power), (double)((z2 - z) * power));
               }
            }
         } else if(otherDecay >= 0) {
            power = otherDecay - decay;
            vec = vec.addVector((double)((x2 - x) * power), (double)((y - y) * power), (double)((z2 - z) * power));
         }
      }

      if(world.getBlock(x, y + 1, z) == this) {
         boolean var12 = this.isBlockSolid(world, x, y, z - 1, 2) || this.isBlockSolid(world, x, y, z + 1, 3) || this.isBlockSolid(world, x - 1, y, z, 4) || this.isBlockSolid(world, x + 1, y, z, 5) || this.isBlockSolid(world, x, y + 1, z - 1, 2) || this.isBlockSolid(world, x, y + 1, z + 1, 3) || this.isBlockSolid(world, x - 1, y + 1, z, 4) || this.isBlockSolid(world, x + 1, y + 1, z, 5);
         if(var12) {
            vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
         }
      }

      vec = vec.normalize();
      return vec;
   }

   public Fluid getFluid() {
      return FluidRegistry.getFluid(this.fluidName);
   }

   public float getFilledPercentage(World world, int x, int y, int z) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z) + 1;
      float remaining = (float)quantaRemaining / this.quantaPerBlockFloat;
      if(remaining > 1.0F) {
         remaining = 1.0F;
      }

      return remaining * (float)(this.density > 0?1:-1);
   }

   public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
      if(world.getBlock(x, y, z) == Blocks.air) {
         return 0;
      } else if(world.getBlock(x, y, z) != this) {
         return -1;
      } else {
         int quantaRemaining = this.quantaPerBlock - world.getBlockMetadata(x, y, z);
         return quantaRemaining;
      }
   }

   public boolean canCollideCheck(int meta, boolean fullHit) {
      return fullHit && meta == 0;
   }

   public int getMaxRenderHeightMeta() {
      return 0;
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      if(this.maxScaledLight == 0) {
         return super.getLightValue(world, x, y, z);
      } else {
         int data = this.quantaPerBlock - world.getBlockMetadata(x, y, z) - 1;
         return (int)((float)data / this.quantaPerBlockFloat * (float)this.maxScaledLight);
      }
   }

   private boolean isTargetBlock(World world, Block block, int x, int y, int z) {
      return block != null && (block != Blocks.air || world.getBlock(x, y - 1, z).getMaterial().isSolid()) && block != this;
   }

   public boolean isFlowingVertically(IBlockAccess world, int x, int y, int z) {
      return world.getBlock(x, y + this.densityDir, z) == this || world.getBlock(x, y, z) == this && this.canFlowInto(world, x, y + this.densityDir, z);
   }

   public boolean isSourceBlock(IBlockAccess world, int x, int y, int z) {
      return world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 0;
   }

   protected boolean[] getOptimalFlowDirections(World world, int x, int y, int z) {
      int min;
      int side;
      for(min = 0; min < 4; ++min) {
         this.flowCost[min] = 1000;
         side = x;
         int z2 = z;
         switch(min) {
         case 0:
            side = x - 1;
            break;
         case 1:
            side = x + 1;
            break;
         case 2:
            z2 = z - 1;
            break;
         case 3:
            z2 = z + 1;
         }

         if(this.canFlowInto(world, side, y, z2) && !this.isSourceBlock(world, side, y, z2)) {
            if(this.canFlowInto(world, side, y + this.densityDir, z2)) {
               this.flowCost[min] = 0;
            } else {
               this.flowCost[min] = this.calculateFlowCost(world, side, y, z2, 1, min);
            }
         }
      }

      min = this.flowCost[0];

      for(side = 1; side < 4; ++side) {
         if(this.flowCost[side] < min) {
            min = this.flowCost[side];
         }
      }

      for(side = 0; side < 4; ++side) {
         this.isOptimalFlowDirection[side] = this.flowCost[side] == min;
      }

      return this.isOptimalFlowDirection;
   }

   protected int calculateFlowCost(World world, int x, int y, int z, int recurseDepth, int side) {
      int cost = 1000;

      for(int adjSide = 0; adjSide < 4; ++adjSide) {
         if((adjSide != 0 || side != 1) && (adjSide != 1 || side != 0) && (adjSide != 2 || side != 3) && (adjSide != 3 || side != 2)) {
            int x2 = x;
            int z2 = z;
            switch(adjSide) {
            case 0:
               x2 = x - 1;
               break;
            case 1:
               x2 = x + 1;
               break;
            case 2:
               z2 = z - 1;
               break;
            case 3:
               z2 = z + 1;
            }

            if(this.canFlowInto(world, x2, y, z2) && !this.isSourceBlock(world, x2, y, z2)) {
               if(this.canFlowInto(world, x2, y + this.densityDir, z2)) {
                  return recurseDepth;
               }

               if(recurseDepth < 4) {
                  int min = this.calculateFlowCost(world, x2, y, z2, recurseDepth + 1, adjSide);
                  if(min < cost) {
                     cost = min;
                  }
               }
            }
         }
      }

      return cost;
   }

   protected void flowIntoBlock(World world, int x, int y, int z, int meta, TileEntityBrewFluid sourceFluid) {
      if(meta >= 0) {
         if(this.displaceIfPossible(world, x, y, z)) {
            world.setBlock(x, y, z, this, meta, 3);
            TileEntityBrewFluid targetFluid = (TileEntityBrewFluid)BlockUtil.getTileEntity(world, x, y, z, TileEntityBrewFluid.class);
            if(targetFluid != null && sourceFluid != null && sourceFluid.nbtEffect != null) {
               targetFluid.nbtEffect = (NBTTagCompound)sourceFluid.nbtEffect.copy();
               targetFluid.expansion = sourceFluid.expansion;
               targetFluid.color = sourceFluid.color;
               targetFluid.duration = sourceFluid.duration;
               targetFluid.thrower = sourceFluid.thrower;
            }
         }

      }
   }

   protected boolean canFlowInto(IBlockAccess world, int x, int y, int z) {
      if(world.getBlock(x, y, z).isAir(world, x, y, z)) {
         return true;
      } else {
         Block block = world.getBlock(x, y, z);
         if(block == this) {
            return true;
         } else if(this.displacements.containsKey(block)) {
            return ((Boolean)this.displacements.get(block)).booleanValue();
         } else {
            Material material = block.getMaterial();
            if(!material.blocksMovement() && material != Material.water && material != Material.lava && material != Material.portal) {
               int density = getDensity(world, x, y, z);
               return density == Integer.MAX_VALUE?true:this.density > density;
            } else {
               return false;
            }
         }
      }
   }

   protected int getLargerQuanta(IBlockAccess world, int x, int y, int z, int compare) {
      int quantaRemaining = this.getQuantaValue(world, x, y, z);
      return quantaRemaining <= 0?compare:(quantaRemaining >= compare?quantaRemaining:compare);
   }

   public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
      return null;
   }

   public boolean canDrain(World world, int x, int y, int z) {
      return false;
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         boolean evaporated = false;
         TileEntityBrewFluid fluid = (TileEntityBrewFluid)BlockUtil.getTileEntity(world, x, y, z, TileEntityBrewFluid.class);
         if(!world.isRemote && fluid != null && this.isSourceBlock(world, x, y, z)) {
            if(++fluid.updateCount > 3 && (fluid.duration == 0 || rand.nextInt(fluid.duration) == 0)) {
               world.setBlockToAir(x, y, z);
               evaporated = true;
            } else {
               world.scheduleBlockUpdate(x, y, z, this, this.tickRate);
            }
         }

         if(!evaporated) {
            int quantaRemaining = this.quantaPerBlock - world.getBlockMetadata(x, y, z);
            boolean expQuanta = true;
            int flowMeta;
            if(quantaRemaining < this.quantaPerBlock) {
               flowMeta = y - this.densityDir;
               int var19;
               if(world.getBlock(x, flowMeta, z) != this && world.getBlock(x - 1, flowMeta, z) != this && world.getBlock(x + 1, flowMeta, z) != this && world.getBlock(x, flowMeta, z - 1) != this && world.getBlock(x, flowMeta, z + 1) != this) {
                  byte arr$ = -100;
                  int var20 = this.getLargerQuanta(world, x - 1, y, z, arr$);
                  var20 = this.getLargerQuanta(world, x + 1, y, z, var20);
                  var20 = this.getLargerQuanta(world, x, y, z - 1, var20);
                  var20 = this.getLargerQuanta(world, x, y, z + 1, var20);
                  var19 = var20 - 1;
               } else {
                  var19 = this.quantaPerBlock - 1;
               }

               if(var19 != quantaRemaining) {
                  quantaRemaining = var19;
                  if(var19 <= 0) {
                     world.setBlock(x, y, z, Blocks.air);
                  } else {
                     world.setBlockMetadataWithNotify(x, y, z, this.quantaPerBlock - var19, 3);
                     world.scheduleBlockUpdate(x, y, z, this, this.tickRate);
                     world.notifyBlocksOfNeighborChange(x, y, z, this);
                  }
               }
            } else if(quantaRemaining >= this.quantaPerBlock) {
               world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            }

            if(this.canDisplace(world, x, y + this.densityDir, z)) {
               this.flowIntoBlock(world, x, y + this.densityDir, z, 1, fluid);
               return;
            }

            flowMeta = this.quantaPerBlock - quantaRemaining + 1;
            if(flowMeta >= this.quantaPerBlock) {
               return;
            }

            if(this.isSourceBlock(world, x, y, z) || !this.isFlowingVertically(world, x, y, z)) {
               if(world.getBlock(x, y - this.densityDir, z) == this) {
                  flowMeta = 1;
               }

               boolean[] var21 = this.getOptimalFlowDirections(world, x, y, z);
               if(var21[0]) {
                  this.flowIntoBlock(world, x - 1, y, z, flowMeta, fluid);
               }

               if(var21[1]) {
                  this.flowIntoBlock(world, x + 1, y, z, flowMeta, fluid);
               }

               if(var21[2]) {
                  this.flowIntoBlock(world, x, y, z - 1, flowMeta, fluid);
               }

               if(var21[3]) {
                  this.flowIntoBlock(world, x, y, z + 1, flowMeta, fluid);
               }
            }

            if(fluid != null && fluid.nbtEffect != null) {
               ForgeDirection[] var22 = ForgeDirection.VALID_DIRECTIONS;
               int len$ = var22.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ForgeDirection direction = var22[i$];
                  int x2 = x + direction.offsetX;
                  int y2 = y + direction.offsetY;
                  int z2 = z + direction.offsetZ;
                  if(world.rand.nextDouble() < 0.01D && this.isTargetBlock(world, world.getBlock(x2, y2, z2), x2, y2, z2)) {
                     ModifiersEffect modifiers = new ModifiersEffect(1.0D, 1.0D, false, new EntityPosition((double)x + 0.5D, (double)y, (double)z + 0.5D), false, 0, EntityUtil.playerOrFake(world, fluid.thrower));
                     ++modifiers.strengthPenalty;
                     WitcheryBrewRegistry.INSTANCE.applyToBlock(world, x2, y2, z2, direction.getOpposite(), 1, fluid.nbtEffect, modifiers);
                  }
               }

               world.scheduleBlockUpdate(x, y, z, this, this.tickRate);
            }
         }

      }
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(entity != null && entity instanceof EntityLivingBase && !world.isRemote && world.rand.nextInt(10) == 4) {
         TileEntityBrewFluid liquid = (TileEntityBrewFluid)BlockUtil.getTileEntity(world, x, y, z, TileEntityBrewFluid.class);
         if(liquid != null && liquid.nbtEffect != null) {
            EntityLivingBase living = (EntityLivingBase)entity;
            WitcheryBrewRegistry.INSTANCE.applyToEntity(world, living, liquid.nbtEffect, new ModifiersEffect(0.25D, 0.5D, false, new EntityPosition(x, y, z), false, 0, EntityUtil.playerOrFake(world, liquid.thrower)));
         }
      }

   }

   static {
      defaultDisplacements.put(Blocks.wooden_door, Boolean.valueOf(false));
      defaultDisplacements.put(Blocks.iron_door, Boolean.valueOf(false));
      defaultDisplacements.put(Blocks.standing_sign, Boolean.valueOf(false));
      defaultDisplacements.put(Blocks.wall_sign, Boolean.valueOf(false));
      defaultDisplacements.put(Blocks.reeds, Boolean.valueOf(false));
   }
}
