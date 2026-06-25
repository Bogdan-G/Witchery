/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLCommonHandler
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  cpw.mods.fml.common.gameevent.TickEvent$Phase
 *  cpw.mods.fml.common.gameevent.TickEvent$ServerTickEvent
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDoor
 *  net.minecraft.block.BlockSand
 *  net.minecraft.block.IGrowable
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.boss.IBossDisplayData
 *  net.minecraft.entity.item.EntityFireworkRocket
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.monster.EntityBlaze
 *  net.minecraft.entity.monster.EntityCreeper
 *  net.minecraft.entity.monster.EntityGolem
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.entity.monster.EntityWitch
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemDoor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.FurnaceRecipes
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EntityDamageSourceIndirect
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.storage.WorldInfo
 *  net.minecraftforge.fluids.FluidRegistry
 *  net.minecraftforge.fluids.FluidStack
 */
package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBarrier;
import com.emoniph.witchery.blocks.BlockBrazier;
import com.emoniph.witchery.blocks.BlockWickerBundle;
import com.emoniph.witchery.blocks.BlockWitchDoor;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.TileEntityCursedBlock;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.brewing.potions.PotionIllFitting;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.entity.EntityDarkMark;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionLight;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.item.ItemChalk;
import com.emoniph.witchery.item.ItemLeonardsUrn;
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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSand;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
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
    public static final SymbolEffect Accio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(1, "witchery.pott.accio"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, final EntitySpellEffect spell) {
            if (caster != null && mop != null) {
                double R = spell.getEffectLevel() == 1 ? 5.0 : (spell.getEffectLevel() == 2 ? 10.0 : 20.0);
                double R_SQ = R * R;
                AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(spell.posX - R), (double)(spell.posY - R), (double)(spell.posZ - R), (double)(spell.posX + R), (double)(spell.posY + R), (double)(spell.posZ + R));
                List entities = world.getEntitiesWithinAABB(EntityItem.class, bb);
                for (Object obj : entities) {
                    EntityItem item = (EntityItem)obj;
                    if (!(item.getDistanceSqToEntity((Entity)spell) <= R_SQ)) continue;
                    item.setPosition(caster.posX, caster.posY + 1.0, caster.posZ);
                }
                List living = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
                for (Object obj : living) {
                    EntityLivingBase entity = (EntityLivingBase)obj;
                    if (entity == caster || !(entity.getDistanceSqToEntity((Entity)spell) <= R_SQ)) continue;
                    EntityUtil.pullTowards(world, (Entity)entity, new EntityPosition((Entity)caster), 0.04, 0.1);
                }
            }
        }
    }.setColor(5322534).setSize(1.0f), new StrokeSet(1, new byte[]{(byte)3,(byte)0,(byte)2,(byte)2,(byte)1}), new StrokeSet(1, new byte[]{(byte)3,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1}), new StrokeSet(2, new byte[]{(byte)3,(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)3,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect Aguamenti = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(2, "witchery.pott.aguamenti"){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            if (player.isSneaking()) {
                EntitySpellEffect dummy = new EntitySpellEffect(world, (EntityLivingBase)player, 0.0, 0.0, 0.0, this, effectLevel);
                dummy.setPosition(player.posX, player.posY, player.posZ);
                this.onCollision(world, (EntityLivingBase)player, new MovingObjectPosition((Entity)player), dummy);
            } else {
                super.perform(world, player, effectLevel);
            }
        }

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, final EntitySpellEffect spell) {
            if (!(spell.getEffectLevel() == 1 && !world.provider.isHellWorld || world.provider.isHellWorld && spell.getEffectLevel() == 3)) {
                if (!world.provider.isHellWorld) {
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        int dx1 = MathHelper.floor_double((double)mop.entityHit.posX);
                        int dy = MathHelper.floor_double((double)mop.entityHit.posY);
                        int dz = MathHelper.floor_double((double)mop.entityHit.posZ);
                        this.setBlock(caster, world, dx1, dy, dz, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1, dy + 1, dz, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1 + 1, dy, dz, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1 - 1, dy, dz, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1, dy, dz + 1, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1, dy, dz - 1, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, dx1, dy - 1, dz, (Block)Blocks.flowing_water);
                    } else {
                        int dy = 0;
                        int dx1 = 0;
                        int n = mop.sideHit == 5 ? 1 : (dx1 = mop.sideHit == 4 ? -1 : 0);
                        int n2 = mop.sideHit == 0 ? -1 : (dy = mop.sideHit == 1 ? 1 : 0);
                        int dz = mop.sideHit == 3 ? 1 : (mop.sideHit == 2 ? -1 : 0);
                        int x = mop.blockX + dx1;
                        int y = mop.blockY + dy + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1 ? -1 : 0);
                        int z = mop.blockZ + dz;
                        this.setBlock(caster, world, x, y, z, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x, y + 1, z, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x + 1, y, z, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x - 1, y, z, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x, y, z + 1, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x, y, z - 1, (Block)Blocks.flowing_water);
                        this.setIfAir(caster, world, x, y - 1, z, (Block)Blocks.flowing_water);
                    }
                }
            } else if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                this.setBlock(caster, world, MathHelper.floor_double((double)mop.entityHit.posX), MathHelper.floor_double((double)mop.entityHit.posY), MathHelper.floor_double((double)mop.entityHit.posZ), (Block)Blocks.flowing_water);
            } else if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block dx = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                if (dx == Witchery.Blocks.CAULDRON) {
                    if (Witchery.Blocks.CAULDRON.tryFillWith(world, mop.blockX, mop.blockY, mop.blockZ, new FluidStack(FluidRegistry.WATER, 3000))) {
                        // empty if block
                    }
                } else if (dx == Witchery.Blocks.KETTLE) {
                    if (Witchery.Blocks.KETTLE.tryFillWith(world, mop.blockX, mop.blockY, mop.blockZ, new FluidStack(FluidRegistry.WATER, 1000))) {
                        // empty if block
                    }
                } else {
                    int dz = 0;
                    int dy = 0;
                    int n = mop.sideHit == 5 ? 1 : (dy = mop.sideHit == 4 ? -1 : 0);
                    int n3 = mop.sideHit == 0 ? -1 : (dz = mop.sideHit == 1 ? 1 : 0);
                    int x = mop.sideHit == 3 ? 1 : (mop.sideHit == 2 ? -1 : 0);
                    this.setBlock(caster, world, mop.blockX + dy, mop.blockY + dz + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1 ? -1 : 0), mop.blockZ + x, (Block)Blocks.flowing_water);
                }
            }
        }

        private void setBlock(EntityLivingBase caster, World world, int x, int y, int z, Block block) {
            if (BlockProtect.checkModsForBreakOK(world, x, y, z, caster)) {
                world.setBlock(x, y, z, block);
            }
        }

        private void setIfAir(EntityLivingBase caster, World world, int x, int y, int z, Block block) {
            if (world.isAirBlock(x, y, z)) {
                this.setBlock(caster, world, x, y, z, block);
            }
        }
    }.setColor(0x11F3FF).setSize(2.0f), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)2}), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)0}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect Alohomora = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(3, "witchery.pott.alohomora"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block blockID = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                if (blockID != Witchery.Blocks.DOOR_ALDER && blockID != Witchery.Blocks.DOOR_ROWAN) {
                    if (blockID instanceof BlockDoor) {
                        ((BlockDoor)blockID).func_150014_a(world, mop.blockX, mop.blockY, mop.blockZ, !((BlockDoor)blockID).func_150015_f((IBlockAccess)world, mop.blockX, mop.blockY, mop.blockZ));
                    }
                } else {
                    ((BlockWitchDoor)blockID).onBlockActivatedNormally(world, mop.blockX, mop.blockY, mop.blockZ, null, 1, mop.blockX, mop.blockY, mop.blockZ);
                }
            }
        }
    }.setColor(5322534).setSize(0.5f), new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)2,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)2,(byte)2,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)2,(byte)2,(byte)2,(byte)1,(byte)1}));
    public static final SymbolEffect AvadaKedavra = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(4, "witchery.pott.avadakedavra", 101, true, false, null, 0, true){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (mop != null && caster != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                if (mop.entityHit instanceof EntityPlayer) {
                    if (world.isRemote || !(caster instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled()) {
                        EntityPlayer hitCreature = (EntityPlayer)mop.entityHit;
                        EntityUtil.instantDeath((EntityLivingBase)hitCreature, caster);
                    }
                } else if (mop.entityHit instanceof EntityLiving) {
                    EntityLiving hitCreature1 = (EntityLiving)mop.entityHit;
                    if (caster instanceof EntityPlayer && ((EntityPlayer)caster).capabilities.isCreativeMode) {
                        EntityUtil.instantDeath((EntityLivingBase)hitCreature1, caster);
                    } else if ((PotionEnslaved.canCreatureBeEnslaved((EntityLivingBase)hitCreature1) || hitCreature1 instanceof EntityWitch || hitCreature1 instanceof EntityEnt || hitCreature1 instanceof EntityGolem) && hitCreature1.getMaxHealth() <= 200.0f) {
                        hitCreature1.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)effectEntity, (Entity)caster), 200.0f);
                    } else {
                        hitCreature1.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)effectEntity, (Entity)caster), 25.0f);
                    }
                }
            }
        }
    }.setColor(65280).setSize(2.0f), new StrokeSet(1, new byte[]{(byte)1,(byte)2,(byte)2,(byte)0,(byte)0,(byte)3,(byte)3,(byte)3,(byte)3,(byte)1,(byte)1,(byte)2}));
    public static final SymbolEffect CaveInimicum = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(5, "witchery.pott.caveinimicum"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, effectEntity.getEffectLevel(), new IBlockEffect(){

                    @Override
                    public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                        Block newBlockID = Blocks.air;
                        if (block == Blocks.dirt) {
                            newBlockID = Blocks.stone;
                        } else if (block == Blocks.grass) {
                            newBlockID = Blocks.stone;
                        } else if (block == Blocks.mycelium) {
                            newBlockID = Blocks.stone;
                        } else if (block == Blocks.cobblestone) {
                            newBlockID = Blocks.stone;
                        } else if (block == Blocks.planks) {
                            newBlockID = Blocks.stone;
                        } else if (block == Witchery.Blocks.PLANKS) {
                            newBlockID = Blocks.stone;
                        } else if (block == Blocks.stonebrick) {
                            newBlockID = Blocks.brick_block;
                        } else if (block == Blocks.sand) {
                            newBlockID = Blocks.sandstone;
                        } else if (block == Blocks.clay) {
                            newBlockID = Blocks.hardened_clay;
                        } else if (block == Blocks.wooden_door) {
                            int i1 = ((BlockDoor)block).func_150012_g((IBlockAccess)world, x, y, z);
                            if ((i1 & 8) != 0) {
                                --y;
                            }
                            world.setBlockToAir(x, y, z);
                            world.setBlockToAir(x, y + 1, z);
                            int pp1 = MathHelper.floor_double((double)((double)((actor.rotationYaw + 180.0f) * 4.0f / 360.0f) - 0.5)) & 3;
                            ItemDoor.placeDoorBlock((World)world, (int)x, (int)y, (int)z, (int)pp1, (Block)Blocks.iron_door);
                        }
                        if (newBlockID != Blocks.air) {
                            world.setBlock(x, y, z, newBlockID);
                        }
                    }
                });
            }
        }
    }.setColor(0x303030).setSize(3.0f), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)0,(byte)0,(byte)2}), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)0,(byte)0,(byte)0,(byte)2}), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)3,(byte)0,(byte)0,(byte)2,(byte)2}), new StrokeSet(2, new byte[]{(byte)0,(byte)3,(byte)3,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)3,(byte)3,(byte)0,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Colloportus = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(6, "witchery.pott.colloportus"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            int y;
            Block blockID;
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && caster != null && (blockID = world.getBlock(mop.blockX, y = mop.blockY, mop.blockZ)) instanceof BlockDoor) {
                int i1 = ((BlockDoor)blockID).func_150012_g((IBlockAccess)world, mop.blockX, y, mop.blockZ);
                if ((i1 & 8) != 0) {
                    --y;
                }
                world.setBlockToAir(mop.blockX, y, mop.blockZ);
                world.setBlockToAir(mop.blockX, y + 1, mop.blockZ);
                int pp1 = MathHelper.floor_double((double)((double)((caster.rotationYaw + 180.0f) * 4.0f / 360.0f) - 0.5)) & 3;
                ItemDoor.placeDoorBlock((World)world, (int)mop.blockX, (int)y, (int)mop.blockZ, (int)pp1, (Block)Witchery.Blocks.DOOR_ROWAN);
            }
        }
    }.setColor(5322534).setSize(1.0f), new StrokeSet(3, new byte[]{(byte)3,(byte)1,(byte)1,(byte)2}), new StrokeSet(3, new byte[]{(byte)3,(byte)1,(byte)1,(byte)1,(byte)2}), new StrokeSet(3, new byte[]{(byte)1,(byte)0,(byte)1,(byte)1,(byte)3}), new StrokeSet(1, new byte[]{(byte)1,(byte)2,(byte)2,(byte)3,(byte)3}));
    public static final SymbolEffect Flipendo = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(17, "witchery.pott.flipendo"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            int level = spell.getEffectLevel();
            if (level <= 1) {
                // Level 1: single-target knockback (classic Flipendo).
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                    double dX = target.posX - (caster != null ? caster.posX : spell.posX);
                    double dZ = target.posZ - (caster != null ? caster.posZ : spell.posZ);
                    double len = Math.sqrt(dX * dX + dZ * dZ);
                    if (len > 0.001) { dX /= len; dZ /= len; }
                    target.addVelocity(dX * 2.5, 0.6, dZ * 2.5);
                    target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(spell, caster), 2.0F);
                    target.velocityChanged = true;
                    ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, (Entity)target, 1.0D, 1.0D, 16);
                }
            } else {
                // Level 2/3: area-of-effect blast wave (absorbs the old Expulso).
                double radius = level == 2 ? 6.0 : 10.0;
                double force = level == 2 ? 2.0 : 3.0;
                List list = world.getEntitiesWithinAABB(EntityLivingBase.class, spell.boundingBox.expand(radius, radius, radius));
                boolean hit = false;
                for (Object obj : list) {
                    EntityLivingBase target = (EntityLivingBase)obj;
                    if (target != caster && target.getDistanceToEntity((Entity)spell) <= radius) {
                        double dX = target.posX - spell.posX;
                        double dZ = target.posZ - spell.posZ;
                        double len = Math.sqrt(dX * dX + dZ * dZ);
                        if (len > 0.001) { dX /= len; dZ /= len; }
                        target.addVelocity(dX * force, 1.5, dZ * force);
                        target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(spell, caster), 2.0F);
                        target.velocityChanged = true;
                        hit = true;
                    }
                }
                if (hit) {
                    ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_EXPLODE, world, spell.posX, spell.posY, spell.posZ, 2.0D, 2.0D, 16);
                }
            }
        }
    }.setColor(16777215).setSize(1.5F), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)1,(byte)0}), new StrokeSet(2, new byte[]{(byte)0,(byte)3,(byte)1,(byte)3,(byte)0}), new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)1,(byte)3,(byte)1}));
    public static final SymbolEffect Confundus = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(8, "witchery.pott.confundus"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 2.0 : 4.0);
            EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                @Override
                public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                    if (target instanceof EntityLivingBase && !target.isPotionActive(Potion.confusion)) {
                        target.addPotionEffect(new PotionEffect(Potion.confusion.id, 600));
                    }
                }
            });
        }
    }.setColor(16771328).setSize(1.5f), new StrokeSet(1, new byte[]{(byte)3,(byte)3,(byte)0,(byte)0,(byte)2}), new StrokeSet(1, new byte[]{(byte)3,(byte)3,(byte)3,(byte)0,(byte)0,(byte)2,(byte)2}), new StrokeSet(2, new byte[]{(byte)3,(byte)3,(byte)3,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)3,(byte)3,(byte)0,(byte)0,(byte)0,(byte)0,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Crucio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(9, "witchery.pott.crucio", 5, true, false, null, 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && caster != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                
                int corazones = 2; // Daño base de 2 corazones (4 puntos)
                if (caster instanceof net.minecraft.entity.player.EntityPlayer && com.emoniph.witchery.commands.CommandCrucio.CRUCIO_POWER.containsKey(caster)) {
                    corazones = com.emoniph.witchery.commands.CommandCrucio.CRUCIO_POWER.get(caster);
                }
                
                // Wither Nivel 3 (Amplificador 2) hace 1 de daño (0.5 corazones) cada 10 ticks.
                // Para quitar X corazones, necesita 20 ticks por corazón.
                int duracionTicks = corazones * 20;
                
                EntityLivingBase target = (EntityLivingBase) mop.entityHit;
                
                if (target instanceof net.minecraft.entity.player.EntityPlayer) {
                    if (world.isRemote || !(caster instanceof net.minecraft.entity.player.EntityPlayer) || net.minecraft.server.MinecraftServer.getServer().isPVPEnabled()) {
                        target.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.wither.id, duracionTicks, 2, false));
                    }
                } else {
                    target.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.wither.id, duracionTicks, 2, false));
                }
            }
        }
    }.setColor(0x6600FF).setSize(2.0f), new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)1,(byte)1,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)3,(byte)1,(byte)1,(byte)2,(byte)2}), new StrokeSet(2, new byte[]{(byte)1,(byte)3,(byte)1,(byte)1,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)1,(byte)3,(byte)3,(byte)1,(byte)1,(byte)1,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)1,(byte)3,(byte)3,(byte)3,(byte)1,(byte)1,(byte)1,(byte)1,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Defodio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(10, "witchery.pott.defodio", 3, false, false, null, 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, effectEntity.getEffectLevel(), new IBlockEffect(){

                    @Override
                    public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                        Material material = block.getMaterial();
                        if (material == Material.clay || material == Material.craftedSnow || material == Material.ground || material == Material.grass || material == Material.ice || material == Material.rock || material == Material.sand) {
                            world.setBlockToAir(x, y, z);
                            Item itemBlock = null;
                            int itemDamageValue = -1;
                            try {
                                itemBlock = block.getItemDropped(meta, world.rand, 0);
                                int itemDamageValue1 = block.damageDropped(meta);
                                int ex = block.quantityDropped(meta, 0, world.rand);
                                if (itemBlock != null && itemDamageValue1 >= 0 && ex > 0) {
                                    world.spawnEntityInWorld((Entity)new EntityItem(world, 0.5 + (double)x, 0.5 + (double)y, 0.5 + (double)z, new ItemStack(itemBlock, ex, itemDamageValue1)));
                                }
                            }
                            catch (Throwable var12) {
                                Log.instance().warning(var12, "Exception occured while spawning block as part of Defodio effect: new (" + itemBlock + ", " + itemDamageValue + ") old (" + block + ", " + meta + ")");
                            }
                        }
                    }
                });
            }
        }
    }.setColor(4008220).setSize(2.5f), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)3,(byte)1}), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)0,(byte)3,(byte)1,(byte)1}), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)3,(byte)3,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)3,(byte)3,(byte)1,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)3,(byte)3,(byte)1,(byte)1,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)3,(byte)3,(byte)3,(byte)1,(byte)1,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)3,(byte)3,(byte)3,(byte)1,(byte)1,(byte)1,(byte)2,(byte)2}));
    public static final SymbolEffect Ennervate = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(12, "witchery.pott.ennervate", 1, false, true, null, 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 2.0 : 4.0);
            EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                @Override
                public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                    if (target.isPotionActive(Potion.moveSlowdown)) {
                        target.removePotionEffect(Potion.moveSlowdown.id);
                    }
                    if (target.isPotionActive(Potion.digSlowdown)) {
                        target.removePotionEffect(Potion.digSlowdown.id);
                    }
                    if (target.isPotionActive(Potion.confusion)) {
                        target.removePotionEffect(Potion.confusion.id);
                    }
                }
            });
        }
    }.setColor(16713595).setSize(1.5f), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)0,(byte)2,(byte)3,(byte)0,(byte)2}), new StrokeSet(2, new byte[]{(byte)0,(byte)3,(byte)3,(byte)0,(byte)2,(byte)2,(byte)3,(byte)3,(byte)0,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)3,(byte)3,(byte)0,(byte)2,(byte)2,(byte)2,(byte)3,(byte)3,(byte)3,(byte)0,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Episkey = EffectRegistry.instance().addEffect(new SymbolEffect(13, "witchery.pott.episkey", 1, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            double radius = effectLevel == 1 ? 0.0 : (effectLevel == 2 ? 2.0 : 4.0);
            MovingObjectPosition mop = new MovingObjectPosition((Entity)player);
            EffectRegistry.applyEntityEffect(world, (EntityLivingBase)player, mop, player.posX, player.posY, player.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                @Override
                public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                    int currentFood;
                    boolean hasFood = target instanceof EntityPlayer;
                    int n = currentFood = hasFood ? ((EntityPlayer)target).getFoodStats().getFoodLevel() : 5;
                    if (currentFood > 1 && target.getHealth() < target.getMaxHealth()) {
                        target.heal((float)Math.min(5, currentFood));
                        if (hasFood) {
                            ((EntityPlayer)target).getFoodStats().addStats(-Math.min(5, currentFood), 0.0f);
                        }
                        if (!target.isPotionActive(Potion.confusion)) {
                            target.addPotionEffect(new PotionEffect(Potion.confusion.id, TimeUtil.secsToTicks(4)));
                        }
                        ParticleEffect.SPLASH.send(SoundEffect.MOB_SLIME_SMALL, (Entity)target, 1.0, 1.0, 16);
                    }
                }
            });
        }
    }, new StrokeSet(1, new byte[]{(byte)2,(byte)0,(byte)3,(byte)1,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)2,(byte)0,(byte)0,(byte)3,(byte)1,(byte)1,(byte)1,(byte)1,(byte)2}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)0,(byte)3,(byte)3,(byte)1,(byte)1,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)2,(byte)2,(byte)0,(byte)0,(byte)3,(byte)3,(byte)1,(byte)1,(byte)1,(byte)1,(byte)2,(byte)2}));
    public static final SymbolEffect Expelliarmus = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(15, "witchery.pott.expelliarmus"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 3.0 : 5.0);
            EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                @Override
                public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                    if (actor != target) {
                        if(target.getHeldItem()!=null){target.entityDropItem(target.getHeldItem(),0);target.setCurrentItemOrArmor(0,null);}
                    }
                }
            });
        }

        private void disarm(EntityLivingBase target) {
            ItemStack heldItem1;
            if (target instanceof EntityPlayer) {
                int heldItemIndex;
                EntityPlayer heldItem = (EntityPlayer)target;
                if ((heldItem.openContainer == null || heldItem.openContainer.windowId == 0) && heldItem.inventory.mainInventory[heldItemIndex = heldItem.inventory.currentItem] != null) {
                    heldItem.dropPlayerItemWithRandomChoice(heldItem.inventory.mainInventory[heldItemIndex], true);
                    heldItem.inventory.mainInventory[heldItemIndex] = null;
                }
            } else if (!PotionIllFitting.isTargetBanned(target) && (heldItem1 = target.getHeldItem()) != null) {
                if (target instanceof EntityPlayer) {
                    Infusion.dropEntityItemWithRandomChoice(target, heldItem1, true);
                } else {
                    target.entityDropItem(heldItem1, 0.5f);
                }
                target.setCurrentItemOrArmor(0, (ItemStack)null);
            }
        }
    }.setColor(16747778).setSize(3.0f), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)1}), new StrokeSet(1, new byte[]{(byte)0,(byte)0,(byte)0,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect Flagrate = EffectRegistry.instance().addEffect(new SymbolEffect(16, "witchery.pott.flagrate", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (mop != null) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    ItemChalk.drawGlyph(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, Witchery.Blocks.GLYPH_INFERNAL, player);
                } else {
                    SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
                }
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)3,(byte)0,(byte)2}));
    public static final SymbolEffect Impedimenta = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(19, "witchery.pott.impedimenta"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 3.0 : 6.0);
            double spellX = spell.motionX;
            double spellZ = spell.motionZ;
            EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                @Override
                public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                    if (target != actor && !target.isPotionActive(Potion.moveSlowdown)) {
                        target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 600, 1));
                    }
                }
            });
        }
    }.setColor(6191615).setSize(1.5f), new StrokeSet(1, new byte[]{(byte)3,(byte)3,(byte)2}), new StrokeSet(1, new byte[]{(byte)3,(byte)3,(byte)3,(byte)2,(byte)2}), new StrokeSet(2, new byte[]{(byte)3,(byte)3,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)3,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Imperio = EffectRegistry.instance().addEffect(new SymbolEffectImperio(20, "witchery.pott.imperio").setColor(10686463).setSize(1.5f), new StrokeSet(2, new byte[]{(byte)1,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect Incendio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(21, "witchery.pott.incendio"){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            if (player.isSneaking()) {
                if (!world.isRemote) {
                    int px = MathHelper.floor_double(player.posX);
                    int py = MathHelper.floor_double(player.posY);
                    int pz = MathHelper.floor_double(player.posZ);
                    int radius = effectLevel == 1 ? 1 : (effectLevel == 2 ? 2 : 3);
                    for (int x = -radius; x <= radius; ++x) {
                        for (int z = -radius; z <= radius; ++z) {
                            if (Math.abs(x) == radius || Math.abs(z) == radius) {
                                for (int y = -1; y <= 1; ++y) {
                                    if (world.isAirBlock(px + x, py + y, pz + z) && world.getBlock(px + x, py + y - 1, pz + z).getMaterial().isSolid()) {
                                        world.setBlock(px + x, py + y, pz + z, Blocks.fire);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, player, 1.0, 1.0, 16);
                }
            } else {
                super.perform(world, player, effectLevel);
            }
        }

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 3.0 : 6.0);
            final int level = spell.getEffectLevel();
            if (radius == 0.0) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    mop.entityHit.setFire(1);
                    mop.entityHit.attackEntityFrom(new EntityDamageSourceIndirect("onFire", (Entity)spell, (Entity)caster).setFireDamage(), 0.1f);
                } else if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int dy = 0;
                    int dx = 0;
                    Block side = BlockUtil.getBlock(world, mop);
                    if (side == Witchery.Blocks.WICKER_BUNDLE && BlockWickerBundle.limitToValidMetadata(world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)) == 1) {
                        if (BlockWickerBundle.tryIgniteMan(world, mop.blockX, mop.blockY, mop.blockZ, caster != null ? caster.rotationYaw : 0.0f)) {
                            return;
                        }
                    } else if (side == Witchery.Blocks.BRAZIER) {
                        BlockBrazier.tryIgnite(world, mop.blockX, mop.blockY, mop.blockZ);
                        return;
                    }
                    int n = mop.sideHit == 5 ? 1 : (dx = mop.sideHit == 4 ? -1 : 0);
                    int n2 = mop.sideHit == 0 ? -1 : (dy = mop.sideHit == 1 ? 1 : 0);
                    int dz = mop.sideHit == 3 ? 1 : (mop.sideHit == 2 ? -1 : 0);
                    world.setBlock(mop.blockX + dx, mop.blockY + dy + (!world.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial().isSolid() && mop.sideHit == 1 ? -1 : 0), mop.blockZ + dz, (Block)Blocks.fire);
                }
            } else {
                EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new IEntityEffect<EntityLivingBase>(){

                    @Override
                    public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                        if (target != actor) {
                            target.setFire(level);
                        }
                    }
                });
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    final int side1 = mop.sideHit;
                    EffectRegistry.applyBlockEffect(world, caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, level, new IBlockEffect(){

                        @Override
                        public void doAction(World world, EntityLivingBase actor, int x, int y, int z, Block block, int meta) {
                            if (side1 == 1) {
                                int dy = 0;
                                int dx = 0;
                                int n = side1 == 5 ? 1 : (dx = side1 == 4 ? -1 : 0);
                                int n2 = side1 == 0 ? -1 : (dy = side1 == 1 ? 1 : 0);
                                int nX = x + dx;
                                int nY = y + dy;
                                int dz = side1 == 3 ? 1 : (side1 == 2 ? -1 : 0);
                                int nZ = z + dz;
                                if (world.isAirBlock(nX, nY, nZ)) {
                                    world.setBlock(nX, nY, nZ, (Block)Blocks.fire);
                                }
                            }
                        }
                    });
                }
            }
        }
    }.setColor(16724023).setSize(2.0f), new StrokeSet(1, new byte[]{(byte)3,(byte)0,(byte)0,(byte)1,(byte)1,(byte)0}), new StrokeSet(2, new byte[]{(byte)3,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect Lumos = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(22, "witchery.pott.lumos"){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            if (player.isSneaking()) {
                EntitySpellEffect dummy = new EntitySpellEffect(world, (EntityLivingBase)player, 0.0, 0.0, 0.0, this, effectLevel);
                dummy.setPosition(player.posX, player.posY, player.posZ);
                this.onCollision(world, (EntityLivingBase)player, new MovingObjectPosition((Entity)player), dummy);
            } else {
                super.perform(world, player, effectLevel);
            }
        }

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (!world.isRemote && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityPlayer) {
                EntityPlayer target = (EntityPlayer)mop.entityHit;
                NBTTagCompound nbt = Infusion.getNBT(target);
                if (nbt.hasKey("WITCLumos")) {
                    int ox = nbt.getInteger("WITCLumosX");
                    int oy = nbt.getInteger("WITCLumosY");
                    int oz = nbt.getInteger("WITCLumosZ");
                    if (world.blockExists(ox, oy, oz) && world.getBlock(ox, oy, oz) == Witchery.Blocks.GLOW_GLOBE) {
                        world.setBlockToAir(ox, oy, oz);
                    }
                    nbt.removeTag("WITCLumos");
                    nbt.removeTag("WITCLumosX");
                    nbt.removeTag("WITCLumosY");
                    nbt.removeTag("WITCLumosZ");
                } else {
                    int nx = MathHelper.floor_double(target.posX);
                    int ny = MathHelper.floor_double(target.posY) + 2;
                    int nz = MathHelper.floor_double(target.posZ);
                    nbt.setBoolean("WITCLumos", true);
                    nbt.setInteger("WITCLumosX", nx);
                    nbt.setInteger("WITCLumosY", ny);
                    nbt.setInteger("WITCLumosZ", nz);
                    if (world.isAirBlock(nx, ny, nz)) {
                        world.setBlock(nx, ny, nz, Witchery.Blocks.GLOW_GLOBE);
                    }
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, target, 1.0, 1.0, 16);
                return;
            }
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int dy = 0;
                int dx = 0;
                int n = mop.sideHit == 5 ? 1 : (dx = mop.sideHit == 4 ? -1 : 0);
                int n2 = mop.sideHit == 0 ? -1 : (dy = mop.sideHit == 1 ? 1 : 0);
                int x = mop.blockX + 1 * dx;
                int y = mop.blockY + 1 * dy;
                int dz = mop.sideHit == 3 ? 1 : (mop.sideHit == 2 ? -1 : 0);
                int z = mop.blockZ + 1 * dz;
                int level = effectEntity.getEffectLevel();
                if (level <= 1) {
                    // Level 1: a single glow globe (classic Lumos).
                    Material material = world.getBlock(x, y, z).getMaterial();
                    if (material == Material.air || material == Material.snow) {
                        world.setBlock(x, y, z, Witchery.Blocks.GLOW_GLOBE);
                    }
                } else {
                    // Level 2/3: scatter glow globes around the impact (absorbs Lumos Maxima).
                    int radius = level == 2 ? 5 : 10;
                    for (int ox = -radius; ox <= radius; ++ox) {
                        for (int oy = -radius; oy <= radius; ++oy) {
                            for (int oz = -radius; oz <= radius; ++oz) {
                                if (world.rand.nextInt(10) != 0 || !world.isAirBlock(x + ox, y + oy, z + oz)) continue;
                                world.setBlock(x + ox, y + oy, z + oz, Witchery.Blocks.GLOW_GLOBE);
                            }
                        }
                    }
                    ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, effectEntity, 2.0, 2.0, 16);
                }
            }
        }
    }.setColor(0xFFFF3A).setSize(0.5f), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)2,(byte)0}), new StrokeSet(2, new byte[]{(byte)1,(byte)1,(byte)2,(byte)1,(byte)0}), new StrokeSet(3, new byte[]{(byte)1,(byte)1,(byte)2,(byte)1,(byte)1}));
    public static final SymbolEffect MeteolojinxRecanto = EffectRegistry.instance().addEffect(new SymbolEffect(23, "witchery.pott.meteolojinxrecanto", 100, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (world.isRaining()) {
                WorldServer worldserver = MinecraftServer.getServer().worldServers[0];
                if (worldserver != null) {
                    WorldInfo worldinfo = worldserver.getWorldInfo();
                    worldinfo.setRaining(false);
                    worldinfo.setThundering(false);
                }
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)2,(byte)2,(byte)1,(byte)0,(byte)2,(byte)2,(byte)1,(byte)1}));
    public static final SymbolEffect Nox = EffectRegistry.instance().addEffect(new SymbolEffect(26, "witchery.pott.nox", 50, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            int x0 = MathHelper.floor_double((double)player.posX);
            int y0 = MathHelper.floor_double((double)player.posY);
            int z0 = MathHelper.floor_double((double)player.posZ);
            int radius = 25;
            for (int y = y0 - radius; y <= y0 + radius; ++y) {
                for (int x = x0 - radius; x <= x0 + radius; ++x) {
                    for (int z = z0 - radius; z <= z0 + radius; ++z) {
                        int blockMeta;
                        Block blockID = world.getBlock(x, y, z);
                        if (!((double)blockID.getLightValue((IBlockAccess)world, x, y, z) > 0.8) || !BlockProtect.canBreak(blockID, world) || !BlockProtect.checkModsForBreakOK(world, x, y, z, blockID, blockMeta = world.getBlockMetadata(x, y, z), (EntityLivingBase)player)) continue;
                        world.setBlockToAir(x, y, z);
                        if (blockID.quantityDropped(world.rand) <= 0) continue;
                        blockID.dropBlockAsItem(world, x, y, z, blockMeta, 0);
                    }
                }
            }
            
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(x0 - radius), (double)(y0 - radius), (double)(z0 - radius), (double)(x0 + radius), (double)(y0 + radius), (double)(z0 + radius));
            List list = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
            for (Object obj : list) {
                EntityPlayer p = (EntityPlayer)obj;
                NBTTagCompound nbt = Infusion.getNBT(p);
                if (nbt.hasKey("WITCLumos")) {
                    int ox = nbt.getInteger("WITCLumosX");
                    int oy = nbt.getInteger("WITCLumosY");
                    int oz = nbt.getInteger("WITCLumosZ");
                    if (world.blockExists(ox, oy, oz) && world.getBlock(ox, oy, oz) == Witchery.Blocks.GLOW_GLOBE) {
                        world.setBlockToAir(ox, oy, oz);
                    }
                    nbt.removeTag("WITCLumos");
                    nbt.removeTag("WITCLumosX");
                    nbt.removeTag("WITCLumosY");
                    nbt.removeTag("WITCLumosZ");
                    p.addChatMessage(new net.minecraft.util.ChatComponentTranslation("witchery.pott.lumos.extinguished"));
                }
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)0,(byte)2,(byte)1,(byte)2,(byte)0}));
    public static final SymbolEffect Protego = EffectRegistry.instance().addEffect(new SymbolEffect(31, "witchery.pott.protego"){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            if (player.isSneaking()) {
                MovingObjectPosition mop = new MovingObjectPosition((Entity)player);
                InfusionLight.placeBarrierShield(world, player, mop);
                return;
            }
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (mop != null) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    InfusionLight.placeBarrierShield(world, player, mop);
                } else {
                    SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
                }
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(1, new byte[]{(byte)1,(byte)0,(byte)0}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)0,(byte)0}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0}));
    public static final SymbolEffect PetrificusTotalus = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(35, "witchery.pott.petrificustotalus"){
        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                if (target instanceof EntityPlayer) {
                    if (!world.isRemote && (!(caster instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled())) {
                        EntityPlayer pTarget = (EntityPlayer)target;
                        pTarget.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Integer.MAX_VALUE, 10));
                        pTarget.addPotionEffect(new PotionEffect(Potion.blindness.id, Integer.MAX_VALUE, 0));
                        pTarget.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, Integer.MAX_VALUE, 0));
                        pTarget.addPotionEffect(new PotionEffect(Potion.resistance.id, Integer.MAX_VALUE, 4));
                        ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_FIZZ, pTarget, 1.0D, 2.0D, 16);
                    }
                } else if (target instanceof EntityLiving) {
                    target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Integer.MAX_VALUE, 10));
                    target.addPotionEffect(new PotionEffect(Potion.blindness.id, Integer.MAX_VALUE, 0));
                    target.addPotionEffect(new PotionEffect(Potion.resistance.id, Integer.MAX_VALUE, 4));
                    ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_FIZZ, target, 1.0D, 2.0D, 16);
                }
            }
        }
    }.setColor(16755200).setSize(1.5F), new StrokeSet[]{new StrokeSet(new byte[]{(byte)3, (byte)1, (byte)3, (byte)1, (byte)3})});
    public static final SymbolEffect Glacius = EffectRegistry.instance().addEffect((new SymbolEffectProjectile(37, "witchery.pott.glacius") {
        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 300, 3));
                target.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 300, 2));
                target.extinguish();
                ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_FIZZ, target, 1.0D, 1.0D, 16);
            }
            if (!world.isRemote) {
                int px = (int)spell.posX;
                int py = (int)spell.posY;
                int pz = (int)spell.posZ;
                for (int x = -5; x <= 5; ++x) {
                    for (int y = -5; y <= 5; ++y) {
                        for (int z = -5; z <= 5; ++z) {
                            Block b = world.getBlock(px + x, py + y, pz + z);
                            if (b == net.minecraft.init.Blocks.water || b == net.minecraft.init.Blocks.flowing_water) {
                                world.setBlock(px + x, py + y, pz + z, net.minecraft.init.Blocks.ice);
                            } else if (b == net.minecraft.init.Blocks.lava || b == net.minecraft.init.Blocks.flowing_lava) {
                                world.setBlock(px + x, py + y, pz + z, net.minecraft.init.Blocks.cobblestone);
                            } else if (b == net.minecraft.init.Blocks.fire) {
                                world.setBlockToAir(px + x, py + y, pz + z);
                            }
                        }
                    }
                }
            }
        }
    }).setColor(0x88CCFF).setSize(1.5F), new StrokeSet(1, new byte[]{(byte)1,(byte)2,(byte)0}));
    public static final SymbolEffect Stupefy = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(36, "witchery.pott.stupefy", 5, false, true, null, 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entityLiving = (EntityLivingBase)mop.entityHit;
                if (!entityLiving.isPotionActive(Potion.moveSlowdown)) {
                    entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 6000, 9));
                }
                entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 60, 0));
                if (effectEntity != null) {
                    entityLiving.addVelocity(effectEntity.motionX * 2.0, 0.3, effectEntity.motionZ * 2.0);
                    entityLiving.velocityChanged = true;
                }
            }
        }
    }.setColor(1279).setSize(1.5f), new StrokeSet(1, new byte[]{(byte)2,(byte)2,(byte)0,(byte)3,(byte)0,(byte)2}), new StrokeSet(1, new byte[]{(byte)2,(byte)2,(byte)2,(byte)0,(byte)3,(byte)3,(byte)0,(byte)2,(byte)2}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)0,(byte)0,(byte)3,(byte)0,(byte)0,(byte)2}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)2,(byte)0,(byte)0,(byte)3,(byte)3,(byte)0,(byte)0,(byte)2,(byte)2}));
    public static final SymbolEffect Ignianima = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(39, "witchery.pott.ignianima", 2, true, false, "ignianima", 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect e) {
            double R = 1.5;
            double R_SQ = 2.25;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(e.posX - 1.5), (double)(e.posY - 1.5), (double)(e.posZ - 1.5), (double)(e.posX + 1.5), (double)(e.posY + 1.5), (double)(e.posZ + 1.5));
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            for (Object hit : entities) {
                float healthPct;
                float scale;
                EntityLivingBase hitEntity = (EntityLivingBase)hit;
                if (hitEntity == caster || !(e.getDistanceSqToEntity((Entity)hitEntity) <= 2.25)) continue;
                float damage = 10.0f;
                float f = scale = hitEntity instanceof EntityPlayer ? hitEntity.getMaxHealth() / 20.0f : 1.0f;
                if (caster != null && (damage = 20.0f * (1.0f - (healthPct = caster.getHealth() / caster.getMaxHealth()))) < 2.0f) {
                    damage = 2.0f;
                }
                float scaledDamage = damage * scale;
                hitEntity.attackEntityFrom((DamageSource)new DemonicDamageSource((Entity)caster), scaledDamage);
                ParticleEffect.FLAME.send(SoundEffect.FIRE_IGNITE, (Entity)hitEntity, 1.0, 2.0, 16);
            }
        }
    }.setColor(16770912).setSize(3.0f), new StrokeSet(3, new byte[]{(byte)3,(byte)0,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)0,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)3,(byte)0,(byte)1,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)3,(byte)3,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect CarnosaDiem = EffectRegistry.instance().addEffect(new SymbolEffect(40, "witchery.pott.carnosadiem", 1, true, false, "carnosadiem", 0){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            float damage = player.getMaxHealth() * 0.1f;
            player.attackEntityFrom((DamageSource)new DemonicDamageSource((Entity)player), damage);
            ParticleEffect.REDDUST.send(SoundEffect.MOB_ENDERDRAGON_GROWL, (Entity)player, 1.0, 2.0, 16);
            int currentPower = Infusion.getCurrentEnergy(player);
            int maxPower = Infusion.getMaxEnergy(player);
            Infusion.setCurrentEnergy(player, Math.min(currentPower + 10, maxPower));
            Witchery.modHooks.boostBloodPowers(player, damage);
        }
    }, new StrokeSet(2, new byte[]{(byte)2,(byte)0,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)2,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)0,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)2,(byte)0,(byte)1,(byte)1}), new StrokeSet(2, new byte[]{(byte)2,(byte)2,(byte)2,(byte)0,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1}));
    public static final SymbolEffect MORSMORDRE = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(41, "witchery.pott.morsmordre", 20, true, false, "morsmordre", 0){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect effectEntity) {
            if (!world.isRemote) {
                EntityDarkMark entity = new EntityDarkMark(world);
                entity.setLocationAndAngles(effectEntity.posX, effectEntity.posY, effectEntity.posZ, 0.0f, 0.0f);
                entity.func_110163_bv();
                world.spawnEntityInWorld((Entity)entity);
            }
        }
    }.setColor(0).setSize(3.0f).setTimeToLive(8), new StrokeSet(0, new byte[]{(byte)0,(byte)3,(byte)2,(byte)2}), new StrokeSet(0, new byte[]{(byte)0,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}), new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)3,(byte)2,(byte)2}), new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}), new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)0,(byte)3,(byte)2,(byte)2}), new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)0,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect Tormentum = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(42, "witchery.pott.tormentum", 25, true, true, "tormentum", TimeUtil.minsToTicks(30)){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect e) {
            if (!world.isRemote && e.dimension != Config.instance().dimensionTormentID) {
                double R = 2.0;
                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(e.posX - 2.0), (double)(e.posY - 2.0), (double)(e.posZ - 2.0), (double)(e.posX + 2.0), (double)(e.posY + 2.0), (double)(e.posZ + 2.0));
                List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
                boolean setCooldown = false;
                for (Object hitEntity : entities) {
                    if (hitEntity instanceof EntityPlayer) {
                        EntityPlayer hitLiving = (EntityPlayer)hitEntity;
                        WorldProviderTorment.setPlayerMustTorment(hitLiving, 1, -1);
                        setCooldown = true;
                        continue;
                    }
                    if (!(hitEntity instanceof EntityLiving) || hitEntity instanceof IBossDisplayData) continue;
                    EntityLiving hitLiving1 = (EntityLiving)hitEntity;
                    hitLiving1.setDead();
                    setCooldown = true;
                }
                if (setCooldown && caster != null && caster instanceof EntityPlayer) {
                    this.setOnCooldown((EntityPlayer)caster);
                }
            }
        }
    }.setColor(0x222222).setSize(4.0f), new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)2,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)3,(byte)2,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)1,(byte)3,(byte)2,(byte)2}), new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)1,(byte)3,(byte)3,(byte)2,(byte)2,(byte)2,(byte)2}));
    public static final SymbolEffect LEONARD_1 = EffectRegistry.instance().addEffect(new SymbolEffect(43, "witchery.pott.leonard1", 5, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int level) {
            EffectRegistry.castLeonardSpell(world, player, 0);
        }

        @Override
        public int getChargeCost(World world, EntityPlayer player, int level) {
            return EffectRegistry.costOfLeonardSpell(world, player, 0);
        }
    }, new StrokeSet(2, new byte[]{(byte)0,(byte)3,(byte)3,(byte)1}));
    public static final SymbolEffect LEONARD_2 = EffectRegistry.instance().addEffect(new SymbolEffect(44, "witchery.pott.leonard2", 5, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int level) {
            EffectRegistry.castLeonardSpell(world, player, 1);
        }

        @Override
        public int getChargeCost(World world, EntityPlayer player, int level) {
            return EffectRegistry.costOfLeonardSpell(world, player, 1);
        }
    }, new StrokeSet(3, new byte[]{(byte)1,(byte)2,(byte)2,(byte)0,(byte)1}));
    public static final SymbolEffect LEONARD_3 = EffectRegistry.instance().addEffect(new SymbolEffect(45, "witchery.pott.leonard3", 5, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int level) {
            EffectRegistry.castLeonardSpell(world, player, 2);
        }

        @Override
        public int getChargeCost(World world, EntityPlayer player, int level) {
            return EffectRegistry.costOfLeonardSpell(world, player, 2);
        }
    }, new StrokeSet(1, new byte[]{(byte)2,(byte)0,(byte)0,(byte)3,(byte)0}));
    public static final SymbolEffect LEONARD_4 = EffectRegistry.instance().addEffect(new SymbolEffect(46, "witchery.pott.leonard4", 5, false, false, null, 0){

        @Override
        public void perform(World world, EntityPlayer player, int level) {
            EffectRegistry.castLeonardSpell(world, player, 3);
        }

        @Override
        public int getChargeCost(World world, EntityPlayer player, int level) {
            return EffectRegistry.costOfLeonardSpell(world, player, 3);
        }
    }, new StrokeSet(0, new byte[]{(byte)3,(byte)1,(byte)1,(byte)2}));
    public static final SymbolEffect FlagrateRitualis = EffectRegistry.instance().addEffect(new SymbolEffect(48, "witchery.pott.flagrateritualis", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                ItemChalk.drawGlyph(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, Witchery.Blocks.GLYPH_RITUAL, player);
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)1,(byte)3,(byte)0}));
    public static final SymbolEffect FlagrateAureus = EffectRegistry.instance().addEffect(new SymbolEffect(49, "witchery.pott.flagrateaureus", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                ItemChalk.drawGlyph(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, Witchery.Blocks.CIRCLE, player);
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)1,(byte)3,(byte)2}));
    public static final SymbolEffect FlagrateAlibi = EffectRegistry.instance().addEffect(new SymbolEffect(50, "witchery.pott.flagratealibi", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 4.0);
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                ItemChalk.drawGlyph(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, Witchery.Blocks.GLYPH_OTHERWHERE, player);
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)1,(byte)3,(byte)1}));
    public static final SymbolEffect RevelioPotionis = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(51, "witchery.pott.revelopotionis"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                if (tile instanceof TileEntityCursedBlock) {
                    ((TileEntityCursedBlock)tile).applyToEntityAndDestroy((Entity)caster);
                }
            }
            if (!world.isRemote) {
                double r = 10.0;
                List list = world.getEntitiesWithinAABB(EntityLivingBase.class, spell.boundingBox.expand(r, r, r));
                for (Object obj : list) {
                    EntityLivingBase entity = (EntityLivingBase)obj;
                    if (!entity.isPotionActive(Potion.invisibility)) continue;
                    entity.removePotionEffect(Potion.invisibility.id);
                    ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_ORB, (Entity)entity, 1.0, 1.0, 16);
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, spell, 2.0, 2.0, 16);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)1,(byte)2,(byte)1}));
    public static final SymbolEffect Reparo = EffectRegistry.instance().addEffect(new SymbolEffect(52, "witchery.pott.reparo", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            ItemStack[][] inventories;
            int currentEnergy = Infusion.getCurrentEnergy(player);
            boolean repairedAny = false;
            for (ItemStack[] inv : inventories = new ItemStack[][]{player.inventory.mainInventory, player.inventory.armorInventory}) {
                for (int i = 0; i < inv.length; ++i) {
                    ItemStack stack = inv[i];
                    if (stack == null || !stack.isItemDamaged()) continue;
                    int damage = stack.getItemDamage();
                    int repairAmount = Math.min(damage, currentEnergy);
                    if (repairAmount > 0) {
                        stack.setItemDamage(stack.getItemDamage() - repairAmount);
                        currentEnergy -= repairAmount;
                        repairedAny = true;
                    }
                    if (currentEnergy <= 0) break;
                }
                if (currentEnergy <= 0) break;
            }
            if (repairedAny) {
                Infusion.setCurrentEnergy(player, currentEnergy);
                ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_ORB, (Entity)player, 1.0, 1.0, 16);
            } else {
                SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
        }
    }, new StrokeSet(3, new byte[]{(byte)1,(byte)2,(byte)3}));
    public static final SymbolEffect ExpectoPatronum = EffectRegistry.instance().addEffect(new SymbolEffect(53, "witchery.pott.expectopatronum", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            List list = world.getEntitiesWithinAABB(EntityMob.class, player.boundingBox.expand(15.0, 15.0, 15.0));
            for (Object obj : list) {
                EntityMob mob = (EntityMob)obj;
                if (mob.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && !(mob instanceof EntityCreeper)) continue;
                double d0 = mob.posX - player.posX;
                double d1 = mob.posZ - player.posZ;
                mob.addVelocity(d0 * 0.2, 0.5, d1 * 0.2);
            }
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_WITHER_SPAWN, (Entity)player, 2.0, 2.0, 16);
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)0,(byte)2}));
    public static final SymbolEffect Sectumsempra = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(54, "witchery.pott.sectumsempra"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            // Unified single-target damage spell (absorbs Ictus / Diffindo / basic attack).
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                int level = spell.getEffectLevel();
                float damage = level == 1 ? 5.0f : (level == 2 ? 8.0f : 12.0f);
                target.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)spell, (Entity)caster), damage);
                target.addPotionEffect(new PotionEffect(Potion.wither.id, 100 * level, level - 1));
                // Higher levels also drain a target player's infusion energy (the old Ictus effect).
                if (level >= 2 && target instanceof EntityPlayer) {
                    EntityPlayer pTarget = (EntityPlayer)target;
                    int currentEnergy = Infusion.getCurrentEnergy(pTarget);
                    Infusion.setCurrentEnergy(pTarget, Math.max(0, currentEnergy - 25 * level));
                }
                ParticleEffect.REDDUST.send(SoundEffect.DAMAGE_HIT, (Entity)target, 1.0, 1.0, 16);
            }
        }
    }, new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)1,(byte)3,(byte)1}), new StrokeSet(2, new byte[]{(byte)1,(byte)3,(byte)3,(byte)1,(byte)3,(byte)3,(byte)1}), new StrokeSet(3, new byte[]{(byte)1,(byte)3,(byte)3,(byte)3,(byte)1,(byte)3,(byte)3,(byte)3,(byte)1}));
    public static final SymbolEffect Obliviate = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(7, "witchery.pott.obliviate"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                if (target instanceof EntityPlayer) {
                    if (!world.isRemote && (!(caster instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled())) {
                        target.clearActivePotions();
                        target.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 1));
                        ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, (Entity)target, 1.0, 1.0, 16);
                    }
                } else if (target instanceof EntityLiving) {
                    EntityLiving mobTarget = (EntityLiving)target;
                    mobTarget.setAttackTarget(null);
                    mobTarget.setRevengeTarget(null);
                    ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, (Entity)target, 1.0, 1.0, 16);
                }
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)1,(byte)0}));
    public static final SymbolEffect Confringo = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(11, "witchery.pott.confringo"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            // Scalable blast: L1 small, L2 medium, L3 huge (absorbs the old Bombarda / Bombarda Maxima).
            int level = spell.getEffectLevel();
            float power = level == 1 ? 3.0f : (level == 2 ? 4.0f : 8.0f);
            boolean flaming = level >= 3;
            boolean smoking = level >= 2;
            world.newExplosion((Entity)caster, spell.posX, spell.posY, spell.posZ, power, flaming, smoking);
        }
    }, new StrokeSet(1, new byte[]{(byte)2,(byte)1,(byte)1,(byte)0}), new StrokeSet(2, new byte[]{(byte)2,(byte)1,(byte)1,(byte)2,(byte)0}), new StrokeSet(3, new byte[]{(byte)2,(byte)1,(byte)1,(byte)2,(byte)1}));
    public static final SymbolEffect WingardiumLeviosa = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(18, "witchery.pott.wingardiumleviosa"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            Entity target = null;
            if (mop != null) {
                if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    target = mop.entityHit;
                } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)mop.blockX, (double)mop.blockY, (double)mop.blockZ, (double)(mop.blockX + 1), (double)(mop.blockY + 1), (double)(mop.blockZ + 1)).expand(1.5, 1.5, 1.5);
                    List list = world.getEntitiesWithinAABBExcludingEntity(spell, bounds);
                    double closestDist = Double.MAX_VALUE;
                    for (Object obj : list) {
                        Entity e = (Entity)obj;
                        double d = e.getDistanceSqToEntity(spell);
                        if (d < closestDist) {
                            closestDist = d;
                            target = e;
                        }
                    }
                }
            }
            if (target != null) {
                if (target instanceof EntityPlayer && ((EntityPlayer)target).getEntityData().getInteger("WITCLeviosaTicks") > 0) {
                    ((EntityPlayer)target).getEntityData().setInteger("WITCLeviosaTicks", 0);
                    ParticleEffect.SMOKE.send(SoundEffect.RANDOM_FIZZ, target, 1.0D, 2.0D, 16);
                } else {
                    if (caster instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer)caster;
                        player.getEntityData().setInteger("WITCLeviosaEntity", target.getEntityId());
                        player.getEntityData().setInteger("WITCLeviosaTicks", Integer.MAX_VALUE);
                        if (!player.getEntityData().hasKey("WITCLeviosaDistance")) {
                            player.getEntityData().setFloat("WITCLeviosaDistance", 5.0f);
                        }
                    }
                    target.motionY = 0.5;
                    if (target instanceof EntityLivingBase) {
                        ((EntityLivingBase)target).addPotionEffect(new PotionEffect(Potion.resistance.id, 200, 4));
                    }
                }
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)2,(byte)2,(byte)1}));
    public static final SymbolEffect Descendo = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(102, "witchery.pott.descendo"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                // Slam the target violently into the ground and pin it.
                if (target.ridingEntity != null && target.ridingEntity instanceof com.emoniph.witchery.entity.EntityBroom) {
                    target.mountEntity(null);
                }
                boolean isFlyingMob = target instanceof net.minecraft.entity.EntityFlying || target instanceof net.minecraft.entity.passive.EntityBat || target instanceof com.emoniph.witchery.entity.EntityFlyingTameable || target instanceof com.emoniph.witchery.entity.EntityFlyingMob || target instanceof net.minecraft.entity.boss.EntityDragon || target instanceof net.minecraft.entity.boss.EntityWither || target instanceof net.minecraft.entity.monster.EntityGhast || target instanceof net.minecraft.entity.monster.EntityBlaze;
                boolean isFlyingPlayer = target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isFlying;
                
                if (isFlyingMob || isFlyingPlayer) {
                    if (isFlyingPlayer) {
                        ((EntityPlayer)target).capabilities.isFlying = false;
                        ((EntityPlayer)target).sendPlayerAbilities();
                    }
                    target.addVelocity(0.0, -5.0, 0.0);
                } else {
                    target.addVelocity(0.0, -2.0, 0.0);
                }
                
                target.velocityChanged = true;
                target.fallDistance += 4.0f;
                target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60 * spell.getEffectLevel(), 3));
                target.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)spell, (Entity)caster), 2.0f);
                ParticleEffect.SMOKE.send(SoundEffect.RANDOM_POP, (Entity)target, 1.0, 0.5, 16);
            }
        }
    }.setColor(0x6688AA).setSize(1.0f), new StrokeSet(1, new byte[]{(byte)3,(byte)2,(byte)1}), new StrokeSet(2, new byte[]{(byte)3,(byte)2,(byte)0,(byte)1}), new StrokeSet(3, new byte[]{(byte)3,(byte)2,(byte)0,(byte)0}));
    public static final SymbolEffect Geminio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(103, "witchery.pott.geminio"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote) {
                double R = 3.0 + 1.0 * (double)(spell.getEffectLevel() - 1);
                double R_SQ = R * R;
                AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(spell.posX - R), (double)(spell.posY - R), (double)(spell.posZ - R), (double)(spell.posX + R), (double)(spell.posY + R), (double)(spell.posZ + R));
                List entities = world.getEntitiesWithinAABB(EntityItem.class, bb);
                boolean duplicated = false;
                for (Object obj : entities) {
                    EntityItem item = (EntityItem)obj;
                    if (item.getDistanceSqToEntity((Entity)spell) > R_SQ) continue;
                    ItemStack stack = item.getEntityItem();
                    if (stack == null || stack.stackSize <= 0) continue;
                    if (!EffectRegistry.canDuplicate(stack)) continue;
                    ItemStack copy = stack.copy();
                    copy.stackSize = 1;
                    EntityItem dupe = new EntityItem(world, item.posX, item.posY + 0.2, item.posZ, copy);
                    dupe.delayBeforeCanPickup = 10;
                    dupe.motionX = (world.rand.nextDouble() - 0.5) * 0.1;
                    dupe.motionY = 0.2;
                    dupe.motionZ = (world.rand.nextDouble() - 0.5) * 0.1;
                    world.spawnEntityInWorld(dupe);
                    ParticleEffect.SPELL.send(SoundEffect.RANDOM_ORB, item, 0.5, 0.5, 16);
                    duplicated = true;
                    break;
                }

                if (!duplicated && caster != null) {
                    SoundEffect.NOTE_SNARE.playAt(world, caster.posX, caster.posY, caster.posZ);
                }
            }
        }
    }.setColor(0xC0FFC0).setSize(1.0f), new StrokeSet(1, new byte[]{(byte)0,(byte)2,(byte)0,(byte)1}), new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)0,(byte)0,(byte)1}), new StrokeSet(3, new byte[]{(byte)0,(byte)2,(byte)0,(byte)0,(byte)0}));
    public static final SymbolEffect Avis = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(25, "witchery.pott.avis"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote) {
                for (int i = 0; i < 3; ++i) {
                    EntityOwl owl = new EntityOwl(world);
                    owl.setLocationAndAngles(spell.posX, spell.posY, spell.posZ, 0.0f, 0.0f);
                    owl.setTimeToLive(200);
                    if (mop != null && mop.entityHit instanceof EntityLivingBase) {
                        owl.setAttackTarget((EntityLivingBase)mop.entityHit);
                    }
                    world.spawnEntityInWorld((Entity)owl);
                }
                ParticleEffect.SMOKE.send(SoundEffect.RANDOM_POP, spell, 1.0, 1.0, 16);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)3,(byte)1,(byte)0}));
    public static final SymbolEffect Oppugno = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(27, "witchery.pott.oppugno"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote) {
                for (int i = 0; i < 3; ++i) {
                    EntityWolf wolf = new EntityWolf(world);
                    wolf.setLocationAndAngles(spell.posX, spell.posY, spell.posZ, 0.0f, 0.0f);
                    wolf.setAngry(true);
                    if (mop.entityHit instanceof EntityLivingBase) {
                        wolf.setAttackTarget((EntityLivingBase)mop.entityHit);
                    }
                    world.spawnEntityInWorld((Entity)wolf);
                }
                ParticleEffect.SMOKE.send(SoundEffect.RANDOM_POP, spell, 1.0, 1.0, 16);
            }
        }
    }, new StrokeSet(1, new byte[]{(byte)3,(byte)1,(byte)2}));
    public static final SymbolEffect PiertotumLocomotor = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(29, "witchery.pott.piertotumlocomotor"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote) {
                EntityIronGolem golem = new EntityIronGolem(world);
                golem.setLocationAndAngles(spell.posX, spell.posY, spell.posZ, 0.0f, 0.0f);
                golem.setPlayerCreated(true);
                world.spawnEntityInWorld((Entity)golem);
                ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, spell, 2.0, 2.0, 16);
            }
        }
    }, new StrokeSet(2, new byte[]{(byte)3,(byte)3,(byte)1}));
    public static final SymbolEffect Reducto = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(30, "witchery.pott.reducto"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (!world.isRemote) {
                double r = 4.0;
                List list = world.getEntitiesWithinAABB(EntityItem.class, spell.boundingBox.expand(r, r, r));
                for (Object obj : list) {
                    ((EntityItem)obj).setDead();
                }
                List listXp = world.getEntitiesWithinAABB(EntityXPOrb.class, spell.boundingBox.expand(r, r, r));
                for (Object obj : listXp) {
                    ((EntityXPOrb)obj).setDead();
                }
                if (mop == null) return;
                int cx = mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ? mop.blockX : (int)spell.posX;
                int cy = mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ? mop.blockY : (int)spell.posY;
                int cz = mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ? mop.blockZ : (int)spell.posZ;
                for (int x = -2; x <= 2; ++x) {
                    for (int y = -2; y <= 2; ++y) {
                        for (int z = -2; z <= 2; ++z) {
                            Block b = world.getBlock(cx + x, cy + y, cz + z);
                            if (Math.abs(x) <= 1 && Math.abs(y) <= 1 && Math.abs(z) <= 1) {
                                if (b == Blocks.air || b == Blocks.bedrock) continue;
                                world.setBlockToAir(cx + x, cy + y, cz + z);
                                continue;
                            }
                            if (b != Blocks.water && b != Blocks.flowing_water && b != Blocks.lava && b != Blocks.flowing_lava && b != Blocks.fire && b != Blocks.web) continue;
                            world.setBlockToAir(cx + x, cy + y, cz + z);
                        }
                    }
                }
                world.playSoundEffect((double)cx, (double)cy, (double)cz, "random.explode", 1.0f, 1.0f);
                ParticleEffect.SMOKE.send(SoundEffect.RANDOM_FIZZ, spell, 2.0, 2.0, 16);
            }
        }
    }, new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)2,(byte)0}));
    public static final SymbolEffect AraniaExumai = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(32, "witchery.pott.araniaexumai"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                if (target instanceof EntitySpider) {
                    target.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)spell, (Entity)caster), 50.0f);
                } else {
                    target.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)spell, (Entity)caster), 2.0f);
                }
                ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_ORB, (Entity)target, 1.0, 1.0, 16);
            }
        }
    }, new StrokeSet(0, new byte[]{(byte)0,(byte)0,(byte)2,(byte)1}));
    public static final SymbolEffect Silencio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(33, "witchery.pott.silencio"){

        @Override
        public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                if (target instanceof EntityPlayer) {
                    target.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, 400, 0));
                }
                target.addPotionEffect(new PotionEffect(Potion.weakness.id, 400, 10));
                target.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 400, 10));
                ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, (Entity)target, 1.0, 1.0, 16);
            }
        }
    }, new StrokeSet(3, new byte[]{(byte)0,(byte)0,(byte)2,(byte)3}));
    public static final SymbolEffect ProtegoMaxima = EffectRegistry.instance().addEffect(new SymbolEffect(34, "witchery.pott.protegomaxima", 1, false, false, null, 0, true){

        @Override
        public void perform(World world, EntityPlayer player, int effectLevel) {
            int px = (int)player.posX;
            int py = (int)player.posY;
            int pz = (int)player.posZ;
            for (int x = -3; x <= 3; ++x) {
                for (int y = -3; y <= 3; ++y) {
                    for (int z = -3; z <= 3; ++z) {
                        if (Math.abs(x) != 3 && Math.abs(y) != 3 && Math.abs(z) != 3 || !world.isAirBlock(px + x, py + y, pz + z)) continue;
                        world.setBlock(px + x, py + y, pz + z, Witchery.Blocks.FORCE);
                    }
                }
            }
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, (Entity)player, 2.0, 2.0, 16);
        }
    }, new StrokeSet(2, new byte[]{(byte)0,(byte)0,(byte)3,(byte)0}));
    public static List<ErectoTask> erectoTasks = new ArrayList<ErectoTask>();
    public static final SymbolEffect Apparition;
    public static final SymbolEffect Fiendfyre;
    public static final SymbolEffect Expulso;
    public static final SymbolEffect Engorgio;
    public static final SymbolEffect FiniteIncantatem;
    public static final SymbolEffect Herbivicus;
    public static final SymbolEffect Impervius;
    public static final SymbolEffect Erecto;
    public static final SymbolEffect PortusPersonal;
    public static final SymbolEffect PortusTraslador;
    public static final SymbolEffect TaglockHex;
    public static final SymbolEffect SmeltRay;
    public static final SymbolEffect EarthPillar;
    public static final SymbolEffect Transmutation;
    public static final SymbolEffect Excavation;
    public static final SymbolEffect BroomSummon;
    public static final SymbolEffect ToadLeap;
    public static final SymbolEffect EtherealVault;
    public static final SymbolEffect Orchideous;
    public static final SymbolEffect Fumos;
    public static final SymbolEffect Incarcerous;
    public static final SymbolEffect Vermillious;

    public static final EffectRegistry instance() {
        return INSTANCE;
    }

    public SymbolEffect addEffect(SymbolEffect effect, StrokeSet ... strokeSets) {
        StrokeSet[] arr$ = strokeSets;
        int len$ = strokeSets.length;
        for (int i$ = 0; i$ < len$; ++i$) {
            StrokeSet strokes = arr$[i$];
            strokes.addTo(this.effects, this.enhanced, effect);
        }
        this.effectID.put(effect.getEffectID(), effect);
        strokeSets[0].setDefaultFor(effect);
        this.allEffects.add(effect);
        return effect;
    }

    public static boolean canDuplicate(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        // The Geminio charm only copies minor, non-precious items - never tools, armour,
        // enchanted gear or anything stacked beyond a single duplicate target.
        if (stack.isItemEnchanted() || stack.isItemStackDamageable()) {
            return false;
        }
        Item item = stack.getItem();
        if (item == Witchery.Items.GENERIC) {
            int dmg = stack.getItemDamage();
            return dmg == Witchery.Items.GENERIC.itemWaystone.damageValue || dmg == Witchery.Items.GENERIC.itemWaystoneBound.damageValue;
        }
        if (item == Witchery.Items.TAGLOCK_KIT) {
            return true;
        }
        // A small whitelist of cheap, common materials.
        return item == Items.string || item == Items.feather || item == Items.bone || item == Items.gunpowder || item == Items.paper || item == Items.stick || item == Items.clay_ball || item == Item.getItemFromBlock(Blocks.dirt) || item == Item.getItemFromBlock(Blocks.cobblestone) || item == Item.getItemFromBlock(Blocks.sand) || item == Items.wheat_seeds;
    }

    public boolean contains(byte[] strokes) {
        return this.getEffect(strokes) != null;
    }

    public boolean hasLongerSymbol(byte[] strokes) {
        Iterator i$ = this.effects.keySet().iterator();
        while (i$.hasNext()) {
            ByteBuffer key = (ByteBuffer)i$.next();
            byte[] candidate = key.array();
            if (candidate.length <= strokes.length) {
                continue;
            }
            boolean isPrefix = true;
            for (int n = 0; n < strokes.length; ++n) {
                if (candidate[n] == strokes[n]) continue;
                isPrefix = false;
                break;
            }
            if (isPrefix) {
                return true;
            }
        }
        return false;
    }

    public SymbolEffect getEffect(byte[] strokes) {
        return (SymbolEffect)this.effects.get(ByteBuffer.wrap(strokes));
    }

    public SymbolEffect getEffect(int effectID) {
        return (SymbolEffect)this.effectID.get(effectID);
    }

    public int getLevel(byte[] strokes) {
        return (Integer)this.enhanced.get(ByteBuffer.wrap(strokes));
    }

    public ArrayList getEffects() {
        return this.allEffects;
    }

    public static <T extends Entity> void applyEntityEffect(World world, EntityLivingBase actor, MovingObjectPosition mop, double xMid, double yMid, double zMid, double radius, Class clazz, IEntityEffect<T> effect) {
        if (radius == 0.0) {
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && clazz.isAssignableFrom(mop.entityHit.getClass())) {
                effect.doAction(world, actor, xMid, yMid, zMid, (T)mop.entityHit);
            }
        } else {
            double R_SQ = radius * radius;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(xMid - radius), (double)(yMid - radius), (double)(zMid - radius), (double)(xMid + radius), (double)(yMid + radius), (double)(zMid + radius));
            List entities = world.getEntitiesWithinAABB(clazz, bb);
            for (Object obj : entities) {
                Entity entity = (Entity)obj;
                if (!(entity.getDistanceSq(xMid, yMid, zMid) <= R_SQ)) continue;
                effect.doAction(world, actor, entity.posX, entity.posY, entity.posZ, (T)entity);
            }
        }
    }

    private static void applyBlockEffect(World world, EntityLivingBase actor, int midX, int midY, int midZ, int side, int radius, IBlockEffect effect) {
        if (radius == 1) {
            Block r = world.getBlock(midX, midY, midZ);
            int x = world.getBlockMetadata(midX, midY, midZ);
            if (r != Blocks.air && BlockProtect.canBreak(r, world) && BlockProtect.checkModsForBreakOK(world, midX, midY, midZ, r, x, actor)) {
                effect.doAction(world, actor, midX, midY, midZ, r, x);
            }
        } else {
            int var16 = Math.min(radius - 1, 3);
            int x = midX;
            int y = midY;
            int z = midZ;
            for (int k = -var16; k <= var16; ++k) {
                for (int j = -var16; j <= var16; ++j) {
                    switch (side) {
                        case 0: 
                        case 1: {
                            x = midX + k;
                            z = midZ + j;
                            break;
                        }
                        case 2: 
                        case 3: {
                            x = midX + k;
                            y = midY + j;
                            break;
                        }
                        case 4: 
                        case 5: {
                            y = midY + k;
                            z = midZ + j;
                        }
                    }
                    Block block = world.getBlock(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);
                    if (block == Blocks.air || !BlockProtect.canBreak(block, world) || !BlockProtect.checkModsForBreakOK(world, x, y, z, block, meta, actor)) continue;
                    effect.doAction(world, actor, x, y, z, block, meta);
                }
            }
        }
    }

    private static int costOfLeonardSpell(World world, EntityPlayer player, int spellSlot) {
        ItemStack urnStack;
        int slot = InvUtil.getSlotContainingItem(player.inventory, Witchery.Items.LEONARDS_URN);
        if (slot >= 0 && slot < player.inventory.getSizeInventory() && (urnStack = player.inventory.getStackInSlot(slot)) != null) {
            ItemStack potion;
            ItemLeonardsUrn.InventoryLeonardsUrn inv = new ItemLeonardsUrn.InventoryLeonardsUrn(player, urnStack);
            if (urnStack.getItemDamage() >= spellSlot && (potion = inv.getStackInSlot(spellSlot)) != null) {
                int baseLevel = WitcheryBrewRegistry.INSTANCE.getUsedCapacity(potion.getTagCompound());
                if (player.isPotionActive(Witchery.Potions.WORSHIP)) {
                    PotionEffect effect = player.getActivePotionEffect(Witchery.Potions.WORSHIP);
                    if (effect.getAmplifier() < 1) {
                        baseLevel += (int)Math.ceil((double)baseLevel * 0.5);
                    }
                } else {
                    baseLevel *= 2;
                }
                return Math.max(baseLevel, 4);
            }
        }
        return 5;
    }

    private static void castLeonardSpell(World world, EntityPlayer player, int spellSlot) {
        ItemStack urnStack;
        int slot = InvUtil.getSlotContainingItem(player.inventory, Witchery.Items.LEONARDS_URN);
        if (slot >= 0 && slot < player.inventory.getSizeInventory() && (urnStack = player.inventory.getStackInSlot(slot)) != null) {
            ItemStack potion;
            ItemLeonardsUrn.InventoryLeonardsUrn inv = new ItemLeonardsUrn.InventoryLeonardsUrn(player, urnStack);
            if (urnStack.getItemDamage() >= spellSlot && (potion = inv.getStackInSlot(spellSlot)) != null) {
                world.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
                if (player.isSneaking()) {
                    WitcheryBrewRegistry.INSTANCE.impactSplashPotion(world, potion, new MovingObjectPosition((Entity)player), new ModifiersImpact(new EntityPosition((Entity)player), false, 0, EntityUtil.playerOrFake(world, (EntityLivingBase)player)));
                    world.playAuxSFX(2002, MathHelper.floor_double((double)player.posX), MathHelper.floor_double((double)player.posY), MathHelper.floor_double((double)player.posZ), WitcheryBrewRegistry.INSTANCE.getBrewColor(potion.getTagCompound()));
                } else {
                    EntityBrew entity = new EntityBrew(world, (EntityLivingBase)player, potion, true);
                    world.spawnEntityInWorld((Entity)entity);
                }
                return;
            }
        }
        SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
    }

    static {
        FMLCommonHandler.instance().bus().register((Object)new ErectoTickHandler());
        Apparition = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(55, "witchery.pott.apparition"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote) {
                    double x = spell.posX;
                    double y = spell.posY;
                    double z = spell.posZ;
                    caster.setPositionAndUpdate(x, y + 1.0, z);
                    world.playSoundAtEntity((Entity)caster, "mob.endermen.portal", 1.0f, 1.0f);
                    ParticleEffect.PORTAL.send(SoundEffect.RANDOM_POP, spell, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)1,(byte)0}));
        Fiendfyre = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(56, "witchery.pott.fiendfyre"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                EntityPlayer player;
                if (!world.isRemote && caster instanceof EntityPlayer && Infusion.getInfusionID(player = (EntityPlayer)caster) == 4 && Infusion.aquireEnergy(world, player, 100, false)) {
                    for (int i = 0; i < 5; ++i) {
                        EntityBlaze blaze = new EntityBlaze(world);
                        blaze.setLocationAndAngles(spell.posX + (double)world.rand.nextInt(5) - 2.0, spell.posY, spell.posZ + (double)world.rand.nextInt(5) - 2.0, 0.0f, 0.0f);
                        blaze.addPotionEffect(new PotionEffect(Potion.wither.id, 200, 3));
                        if (mop.entityHit instanceof EntityLivingBase) {
                            blaze.setAttackTarget((EntityLivingBase)mop.entityHit);
                        }
                        world.spawnEntityInWorld((Entity)blaze);
                    }
                    for (int x = -3; x <= 3; ++x) {
                        for (int z = -3; z <= 3; ++z) {
                            if (world.rand.nextInt(3) != 0 || !world.isAirBlock(mop.blockX + x, mop.blockY + 1, mop.blockZ + z)) continue;
                            world.setBlock(mop.blockX + x, mop.blockY + 1, mop.blockZ + z, (Block)Blocks.fire);
                        }
                    }
                    ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, spell, 3.0, 3.0, 16);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)0,(byte)2,(byte)1,(byte)1}));
        Expulso = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(57, "witchery.pott.expulso"){
            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                double radius = 8.0D;
                List list = world.getEntitiesWithinAABB(EntityLivingBase.class, spell.boundingBox.expand(radius, radius, radius));
                boolean hit = false;
                for (Object obj : list) {
                    EntityLivingBase target = (EntityLivingBase)obj;
                    if (target != caster && target.getDistanceToEntity((Entity)spell) <= radius) {
                        // Disarm: drop whatever the target is holding.
                        ItemStack held = target.getHeldItem();
                        if (held != null) {
                            target.entityDropItem(held, 0.5f);
                            target.setCurrentItemOrArmor(0, (ItemStack)null);
                        }
                        // Knock the target away from the blast.
                        double dX = target.posX - spell.posX;
                        double dZ = target.posZ - spell.posZ;
                        double len = Math.sqrt(dX * dX + dZ * dZ);
                        if (len > 0.001D) { dX /= len; dZ /= len; }
                        target.addVelocity(dX * 2.0D, 1.5D, dZ * 2.0D);
                        target.velocityChanged = true;
                        hit = true;
                    }
                }
                if (hit) {
                    ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_EXPLODE, world, spell.posX, spell.posY, spell.posZ, 2.0D, 2.0D, 16);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)0,(byte)2,(byte)1,(byte)3}));
        Engorgio = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(58, "witchery.pott.engorgio"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                    target.addPotionEffect(new PotionEffect(Potion.field_76434_w.id, 1200, 4));
                    target.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 1200, 1));
                    ParticleEffect.SPELL.send(SoundEffect.RANDOM_LEVELUP, (Entity)target, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(0, new byte[]{(byte)0,(byte)2,(byte)2,(byte)0}));
        FiniteIncantatem = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(59, "witchery.pott.finiteincantatem"){
            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                if (player.isSneaking()) {
                    if (!world.isRemote) {
                        player.clearActivePotions();
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, player, 1.0D, 1.5D, 16);
                        com.emoniph.witchery.util.ChatUtil.sendTranslated(net.minecraft.util.EnumChatFormatting.GREEN, player, "witchery.pott.finiteincantatem.cleanse");
                    }
                } else {
                    super.perform(world, player, effectLevel);
                }
            }

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                List list = world.getEntitiesWithinAABB(EntityLivingBase.class, spell.boundingBox.expand(15.0D, 15.0D, 15.0D));
                for (Object obj : list) {
                    EntityLivingBase target = (EntityLivingBase)obj;
                    target.clearActivePotions();
                    com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectImperio.IMPERIO_TARGETS.remove(target);
                    com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectImperio.IMPERIO_STAYING_TARGETS.remove(target);
                    ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, (Entity)target, 1.0, 1.0, 16);
                }
                if (!world.isRemote) {
                    int px = (int)spell.posX;
                    int py = (int)spell.posY;
                    int pz = (int)spell.posZ;
                    for (int x = -15; x <= 15; ++x) {
                        for (int y = -15; y <= 15; ++y) {
                            for (int z = -15; z <= 15; ++z) {
                                Block _finB = world.getBlock(px + x, py + y, pz + z);
                                if (_finB == Witchery.Blocks.FORCE || _finB == Witchery.Blocks.BARRIER || _finB == Witchery.Blocks.GLOW_GLOBE
                                    || _finB == Witchery.Blocks.CIRCLE || _finB == Witchery.Blocks.BRAMBLE || _finB == Witchery.Blocks.VOID_BRAMBLE
                                    || _finB == Witchery.Blocks.PIT_DIRT || _finB == Witchery.Blocks.PIT_GRASS || _finB == net.minecraft.init.Blocks.web 
                                    || _finB == net.minecraft.init.Blocks.fire || _finB == net.minecraft.init.Blocks.water || _finB == net.minecraft.init.Blocks.flowing_water || _finB == net.minecraft.init.Blocks.ice) {
                                    world.setBlockToAir(px + x, py + y, pz + z);
                                    ParticleEffect.SMOKE.send(SoundEffect.RANDOM_FIZZ, world, px + x, py + y, pz + z, 0.5D, 0.5D, 16);
                                }
                            }
                        }
                    }
                }
            }
        }, new StrokeSet(3, new byte[]{(byte)0,(byte)2,(byte)2,(byte)3}));
        Herbivicus = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(60, "witchery.pott.herbivicus"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote) {
                    int px = (int)spell.posX;
                    int py = (int)spell.posY;
                    int pz = (int)spell.posZ;
                    for (int x = -5; x <= 5; ++x) {
                        for (int y = -2; y <= 2; ++y) {
                            for (int z = -5; z <= 5; ++z) {
                                IGrowable growable;
                                Block block = world.getBlock(px + x, py + y, pz + z);
                                if (!(block instanceof IGrowable) || !(growable = (IGrowable)block).func_149851_a(world, px + x, py + y, pz + z, world.isRemote)) continue;
                                growable.func_149853_b(world, world.rand, px + x, py + y, pz + z);
                                world.playAuxSFX(2005, px + x, py + y, pz + z, 0);
                            }
                        }
                    }
                }
            }
        }, new StrokeSet(2, new byte[]{(byte)0,(byte)2,(byte)3,(byte)1}));
        Impervius = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(62, "witchery.pott.impervius"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                    target.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
                    target.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 6000, 0));
                    target.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 2));
                    ParticleEffect.SPELL.send(SoundEffect.RANDOM_LEVELUP, (Entity)target, 1.0, 1.0, 16);
                } else if (caster != null) {
                    caster.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
                    caster.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 6000, 0));
                    caster.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 2));
                    ParticleEffect.SPELL.send(SoundEffect.RANDOM_LEVELUP, (Entity)caster, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(0, new byte[]{(byte)0,(byte)2,(byte)3,(byte)2}));
        Erecto = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(63, "witchery.pott.erecto"){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                if (player.isSneaking()) {
                    EntitySpellEffect dummy = new EntitySpellEffect(world, (EntityLivingBase)player, 0.0, 0.0, 0.0, this, effectLevel);
                    dummy.setPosition(player.posX, player.posY, player.posZ);
                    this.onCollision(world, (EntityLivingBase)player, new MovingObjectPosition((Entity)player), dummy);
                } else {
                    super.perform(world, player, effectLevel);
                }
            }

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote) {
                    int px = (int)spell.posX;
                    int py = (int)spell.posY;
                    int pz = (int)spell.posZ;
                    EntityPlayer player = caster instanceof EntityPlayer ? (EntityPlayer)caster : null;
                    for (int x = -2; x <= 2; ++x) {
                        for (int y = 0; y <= 4; ++y) {
                            for (int z = -2; z <= 2; ++z) {
                                if (Math.abs(x) == 2 || Math.abs(z) == 2 || y == 4 || y == 0) {
                                    if (!world.isAirBlock(px + x, py + y, pz + z)) continue;
                                    BlockBarrier.setBlock(world, px + x, py + y, pz + z, 600, true, player);
                                    continue;
                                }
                                if (world.getBlock(px + x, py + y, pz + z) != Witchery.Blocks.BARRIER && world.getBlock(px + x, py + y, pz + z) != Witchery.Blocks.FORCE) continue;
                                world.setBlockToAir(px + x, py + y, pz + z);
                            }
                        }
                    }
                    ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, spell, 2.0, 2.0, 16);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)0,(byte)2,(byte)3,(byte)3}));
        PortusPersonal = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(64, "witchery.pott.portuspersonal"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote && caster instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)caster;
                    NBTTagCompound nbt = player.getEntityData();
                    if (nbt.hasKey("PortusX")) {
                        int dx = nbt.getInteger("PortusX");
                        int dy = nbt.getInteger("PortusY");
                        int dz = nbt.getInteger("PortusZ");
                        int dim = nbt.getInteger("PortusDim");
                        if (player.dimension != dim) {
                            player.travelToDimension(dim);
                        }
                        player.setPositionAndUpdate((double)dx + 0.5, (double)dy + 1.0, (double)dz + 0.5);
                        nbt.removeTag("PortusX");
                        player.addChatMessage((IChatComponent)new ChatComponentText("\u00a7aTe has trasladado a tu punto de guardado personal. Punto borrado.\u00a7r"));
                        world.playSoundAtEntity((Entity)player, "mob.endermen.portal", 1.0f, 1.0f);
                    } else {
                        nbt.setInteger("PortusX", (int)player.posX);
                        nbt.setInteger("PortusY", (int)player.posY);
                        nbt.setInteger("PortusZ", (int)player.posZ);
                        nbt.setInteger("PortusDim", player.dimension);
                        player.addChatMessage((IChatComponent)new ChatComponentText("\u00a7aPunto de guardado personal creado. Vuelve a lanzar el hechizo para volver aqu\u00ed.\u00a7r"));
                        world.playSoundAtEntity((Entity)player, "random.levelup", 1.0f, 1.0f);
                    }
                }
            }
        }, new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)0,(byte)1}));
        PortusTraslador = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(65, "witchery.pott.portustraslador"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote && caster instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)caster;
                    ItemStack stack = Witchery.Items.GENERIC.itemChaliceEmpty.createStack();
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setInteger("PosX", (int)player.posX);
                    nbt.setInteger("PosY", (int)player.posY);
                    nbt.setInteger("PosZ", (int)player.posZ);
                    nbt.setInteger("PosD", player.dimension);
                    stack.setTagCompound(nbt);
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        player.dropPlayerItemWithRandomChoice(stack, false);
                    }
                    player.addChatMessage((IChatComponent)new ChatComponentText("\u00a7aSe ha creado un Traslador vinculado a tu posici\u00f3n actual. Haz clic derecho sosteni\u00e9ndolo para regresar aqu\u00ed.\u00a7r"));
                    world.playSoundAtEntity((Entity)player, "random.levelup", 1.0f, 1.0f);
                }
            }
        }, new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)0,(byte)3}));
        TaglockHex = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(72, "witchery.pott.taglockhex"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase && caster instanceof EntityPlayer) {
                    EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                    if (!world.isRemote) {
                        ItemStack taglock = new ItemStack((Item)Witchery.Items.TAGLOCK_KIT, 1, 1);
                        Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(taglock, (EntityPlayer)caster, (Entity)target, true, (Integer)1);
                        world.spawnEntityInWorld((Entity)new EntityItem(world, target.posX, target.posY, target.posZ, taglock));
                        ParticleEffect.MAGIC_CRIT.send(SoundEffect.WATER_SPLASH, (Entity)target, 0.5, 1.0, 16);
                    }
                }
            }
        }.setColor(0xFF0000).setSize(1.0f), new StrokeSet(1, new byte[]{(byte)0,(byte)3,(byte)2,(byte)0}));
        SmeltRay = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(73, "witchery.pott.smeltray"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (mop != null && !world.isRemote) {
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        ArrayList drops;
                        Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                        int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                        ItemStack stack = new ItemStack(block, 1, meta);
                        ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(stack);
                        if (smelted == null && (drops = block.getDrops(world, mop.blockX, mop.blockY, mop.blockZ, meta, 0)) != null && !drops.isEmpty() && (smelted = FurnaceRecipes.smelting().getSmeltingResult((ItemStack)drops.get(0))) == null && block instanceof BlockSand) {
                            smelted = new ItemStack(Blocks.glass);
                        }
                        if (smelted != null) {
                            world.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
                            world.spawnEntityInWorld((Entity)new EntityItem(world, (double)mop.blockX, (double)mop.blockY, (double)mop.blockZ, smelted.copy()));
                            ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, world, mop.blockX, mop.blockY, mop.blockZ, 1.0, 1.0, 16);
                        }
                    } else if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityItem) {
                        EntityItem eItem = (EntityItem)mop.entityHit;
                        ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(eItem.getEntityItem());
                        if (smelted != null) {
                            ItemStack res = smelted.copy();
                            res.stackSize = eItem.getEntityItem().stackSize;
                            eItem.setEntityItemStack(res);
                            ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, (Entity)eItem, 1.0, 1.0, 16);
                        }
                    }
                }
            }
        }.setColor(0xFFAA00).setSize(1.0f), new StrokeSet(3, new byte[]{(byte)0,(byte)3,(byte)2,(byte)1}));
        EarthPillar = EffectRegistry.instance().addEffect(new SymbolEffect(74, "witchery.pott.earthpillar", 1, false, false, null, 0, true){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 8.0);
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !world.isRemote) {
                    for (int x = -1; x <= 1; ++x) {
                        for (int z = -1; z <= 1; ++z) {
                            for (int y = 0; y < 3; ++y) {
                                if (!world.isAirBlock(mop.blockX + x, mop.blockY + y + 1, mop.blockZ + z)) continue;
                                world.setBlock(mop.blockX + x, mop.blockY + y + 1, mop.blockZ + z, Blocks.dirt);
                            }
                        }
                    }
                    if (player.posX >= (double)mop.blockX - 1.5 && player.posX <= (double)mop.blockX + 2.5 && player.posZ >= (double)mop.blockZ - 1.5 && player.posZ <= (double)mop.blockZ + 2.5 && player.posY >= (double)mop.blockY && player.posY <= (double)mop.blockY + 3.0) {
                        player.setPositionAndUpdate(player.posX, (double)mop.blockY + 4.0, player.posZ);
                    }
                    ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_EXPLODE, world, mop.blockX, mop.blockY, mop.blockZ, 2.0, 2.0, 16);
                }
            }
        }, new StrokeSet(0, new byte[]{(byte)0,(byte)3,(byte)2,(byte)3}));
        Transmutation = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(75, "witchery.pott.transmutation"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !world.isRemote) {
                    Block b = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                    Block newBlock = null;
                    if (b == Blocks.dirt) {
                        newBlock = Blocks.sand;
                    } else if (b == Blocks.sand) {
                        newBlock = Blocks.dirt;
                    } else if (b == Blocks.stone) {
                        newBlock = Blocks.cobblestone;
                    } else if (b == Blocks.cobblestone) {
                        newBlock = Blocks.stone;
                    } else if (b == Blocks.log || b == Blocks.log2) {
                        newBlock = Blocks.planks;
                    }
                    if (newBlock != null) {
                        world.setBlock(mop.blockX, mop.blockY, mop.blockZ, (Block)newBlock);
                        ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_LEVELUP, world, mop.blockX, mop.blockY, mop.blockZ, 1.0, 1.0, 16);
                    }
                }
            }
        }.setColor(0x9900CC).setSize(1.5f), new StrokeSet(2, new byte[]{(byte)1,(byte)1,(byte)0,(byte)1}));
        Excavation = EffectRegistry.instance().addEffect(new SymbolEffect(76, "witchery.pott.excavation", 1, false, false, null, 0, true){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 8.0);
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !world.isRemote) {
                    for (int x = -1; x <= 1; ++x) {
                        for (int y = -1; y <= 1; ++y) {
                            for (int z = -1; z <= 1; ++z) {
                                Block b = world.getBlock(mop.blockX + x, mop.blockY + y, mop.blockZ + z);
                                if (b == Blocks.air || b == Blocks.bedrock || !(b.getBlockHardness(world, mop.blockX + x, mop.blockY + y, mop.blockZ + z) >= 0.0f)) continue;
                                b.dropBlockAsItem(world, mop.blockX + x, mop.blockY + y, mop.blockZ + z, world.getBlockMetadata(mop.blockX + x, mop.blockY + y, mop.blockZ + z), 0);
                                world.setBlockToAir(mop.blockX + x, mop.blockY + y, mop.blockZ + z);
                            }
                        }
                    }
                    ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_EXPLODE, world, mop.blockX, mop.blockY, mop.blockZ, 2.0, 2.0, 16);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)1,(byte)1,(byte)0,(byte)2}));
        BroomSummon = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(77, "witchery.pott.broomsummon"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    EntityBroom broom = new EntityBroom(world);
                    broom.setLocationAndAngles(mop.blockX, mop.blockY + 1, mop.blockZ, 0.0f, 0.0f);
                    world.spawnEntityInWorld((Entity)broom);
                    ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, world, mop.blockX, mop.blockY, mop.blockZ, 1.0, 1.0, 16);
                }
            }
        }.setColor(8409152).setSize(2.0f), new StrokeSet(0, new byte[]{(byte)1,(byte)1,(byte)0,(byte)3}));
        ToadLeap = EffectRegistry.instance().addEffect(new SymbolEffect(78, "witchery.pott.toadleap", 1, false, false, null, 0, true){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                if (!world.isRemote) {
                    player.addVelocity(0.0, 1.5, 0.0);
                    player.velocityChanged = true;
                    player.fallDistance = -20.0f;
                    ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, (Entity)player, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(3, new byte[]{(byte)1,(byte)1,(byte)1,(byte)2}));
        EtherealVault = EffectRegistry.instance().addEffect(new SymbolEffect(79, "witchery.pott.etherealvault", 1, false, false, null, 0, true){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                if (!world.isRemote) {
                    player.displayGUIChest((IInventory)player.getInventoryEnderChest());
                    ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, (Entity)player, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(0, new byte[]{(byte)1,(byte)1,(byte)3,(byte)0}));
        Orchideous = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(81, "witchery.pott.orchideous"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int cx = mop.blockX;
                    int cy = mop.blockY;
                    int cz = mop.blockZ;
                    for (int x = -3; x <= 3; ++x) {
                        for (int z = -3; z <= 3; ++z) {
                            Block b;
                            if (world.rand.nextInt(3) != 0 || (b = world.getBlock(cx + x, cy, cz + z)) != Blocks.dirt && b != Blocks.grass || !world.isAirBlock(cx + x, cy + 1, cz + z)) continue;
                            world.setBlock(cx + x, cy, cz + z, (Block)Blocks.grass);
                            world.setBlock(cx + x, cy + 1, cz + z, (Block)Blocks.red_flower, world.rand.nextInt(9), 3);
                        }
                    }
                    ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, spell, 2.0, 2.0, 16);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)1,(byte)2,(byte)2,(byte)1}));
        Fumos = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(82, "witchery.pott.fumos"){

            @Override
            public void perform(World world, EntityPlayer player, int effectLevel) {
                if (player.isSneaking()) {
                    EntitySpellEffect dummy = new EntitySpellEffect(world, (EntityLivingBase)player, 0.0, 0.0, 0.0, this, effectLevel);
                    dummy.setPosition(player.posX, player.posY, player.posZ);
                    this.onCollision(world, (EntityLivingBase)player, new MovingObjectPosition((Entity)player), dummy);
                } else {
                    super.perform(world, player, effectLevel);
                }
            }

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote) {
                    for (int i = 0; i < 30; ++i) {
                        ParticleEffect.LARGE_SMOKE.send(SoundEffect.NONE, spell, 10.0, 10.0, 32);
                        ParticleEffect.SMOKE.send(SoundEffect.NONE, spell, 10.0, 10.0, 32);
                    }
                    ParticleEffect.LARGE_SMOKE.send(SoundEffect.RANDOM_FIZZ, spell, 10.0, 10.0, 32);
                    
                    List list = world.getEntitiesWithinAABB(EntityLivingBase.class, spell.boundingBox.expand(8.0D, 8.0D, 8.0D));
                    for (Object obj : list) {
                        EntityLivingBase target = (EntityLivingBase)obj;
                        if (target == caster || (caster instanceof EntityPlayer && target instanceof EntityPlayer && !MinecraftServer.getServer().isPVPEnabled())) {
                            target.addPotionEffect(new PotionEffect(Potion.invisibility.id, 200, 0));
                        } else {
                            target.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                        }
                    }
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)1,(byte)2,(byte)2,(byte)2}));
        Incarcerous = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(83, "witchery.pott.incarcerous"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase target = (EntityLivingBase)mop.entityHit;
                    if (target instanceof net.minecraft.entity.monster.EntitySpider) {
                        return; // Prevent crash when webs touch spiders
                    }
                    int cx = (int)target.posX;
                    int cy = (int)target.posY;
                    int cz = (int)target.posZ;
                    for (int x = -1; x <= 1; ++x) {
                        for (int y = 0; y <= 1; ++y) {
                            for (int z = -1; z <= 1; ++z) {
                                if (Math.abs(x) + Math.abs(z) > 1 || !world.isAirBlock(cx + x, cy + y, cz + z)) continue;
                                world.setBlock(cx + x, cy + y, cz + z, Blocks.web);
                            }
                        }
                    }
                    ParticleEffect.MAGIC_CRIT.send(SoundEffect.RANDOM_POP, (Entity)target, 1.0, 1.0, 16);
                }
            }
        }, new StrokeSet(3, new byte[]{(byte)1,(byte)3,(byte)1,(byte)0}));
        Vermillious = EffectRegistry.instance().addEffect(new SymbolEffectProjectile(84, "witchery.pott.vermillious"){

            @Override
            public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
                if (!world.isRemote) {
                    ItemStack itemstack = new ItemStack(Items.fireworks);
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    NBTTagList nbttaglist = new NBTTagList();
                    NBTTagCompound explosion = new NBTTagCompound();
                    explosion.setBoolean("Flicker", true);
                    explosion.setBoolean("Trail", true);
                    explosion.setByte("Type", (byte)1);
                    explosion.setIntArray("Colors", new int[]{0xFF0000});
                    nbttaglist.appendTag((NBTBase)explosion);
                    nbttagcompound1.setTag("Explosions", (NBTBase)nbttaglist);
                    nbttagcompound1.setByte("Flight", (byte)2);
                    nbttagcompound.setTag("Fireworks", (NBTBase)nbttagcompound1);
                    itemstack.setTagCompound(nbttagcompound);
                    EntityFireworkRocket rocket = new EntityFireworkRocket(world, spell.posX, spell.posY + 1.0, spell.posZ, itemstack);
                    world.spawnEntityInWorld((Entity)rocket);
                    world.playSoundAtEntity((Entity)rocket, "fireworks.launch", 3.0f, 1.0f);
                }
            }
        }, new StrokeSet(1, new byte[]{(byte)1,(byte)3,(byte)1,(byte)2}));
    }

    public static interface IEntityEffect<T extends Entity> {
        public void doAction(World var1, EntityLivingBase var2, double var3, double var5, double var7, T var9);
    }

    private static interface IBlockEffect {
        public void doAction(World var1, EntityLivingBase var2, int var3, int var4, int var5, Block var6, int var7);
    }

    public static class ErectoTickHandler {
        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                List<ErectoTask> list = erectoTasks;
                synchronized (list) {
                    Iterator<ErectoTask> it = erectoTasks.iterator();
                    while (it.hasNext()) {
                        WorldServer world;
                        ErectoTask task = it.next();
                        --task.ticks;
                        if (task.ticks > 0) continue;
                        MinecraftServer server = MinecraftServer.getServer();
                        if (server != null && (world = server.worldServerForDimension(task.dim)) != null) {
                            for (int x = -2; x <= 2; ++x) {
                                for (int y = 0; y <= 4; ++y) {
                                    for (int z = -2; z <= 2; ++z) {
                                        if (Math.abs(x) != 2 && Math.abs(z) != 2 && y != 4 && y != 0 || world.getBlock(task.x + x, task.y + y, task.z + z) != Witchery.Blocks.FORCE) continue;
                                        world.setBlockToAir(task.x + x, task.y + y, task.z + z);
                                    }
                                }
                            }
                        }
                        it.remove();
                    }
                }
            }
        }
    }

    public static class ErectoTask {
        public int ticks;
        public int x;
        public int y;
        public int z;
        public int dim;

        public ErectoTask(int t, int x, int y, int z, int d) {
            this.ticks = t;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dim = d;
        }
    }
}

