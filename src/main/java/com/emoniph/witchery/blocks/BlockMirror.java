package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityMirrorFace;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemSunGrenade;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockMirror extends BlockBaseContainer {

   private final boolean unbreakable;
   private static String sendMeHome = null;
   private static String iGiveUp = null;


   public BlockMirror(boolean unbreakable) {
      super(Material.glass, BlockMirror.TileEntityMirror.class);
      this.unbreakable = unbreakable;
      super.registerWithCreateTab = false;
      this.setLightLevel(0.7F);
      this.disableStats();
      this.setStepSound(Block.soundTypeGlass);
      if(unbreakable) {
         this.setBlockUnbreakable();
         this.setResistance(9999.0F);
      } else {
         this.setHardness(1.0F);
         this.setResistance(9999.0F);
      }

   }

   public static int getDirection(int meta) {
      return meta & 3;
   }

   public static boolean isBlockTopOfMirror(int meta) {
      return (meta & 4) != 0;
   }

   public boolean trySayMirrorMirrorSendMeHome(EntityPlayer player, String message) {
      if(player != null && !player.worldObj.isRemote && player.dimension == Config.instance().dimensionMirrorID) {
         if(sendMeHome == null) {
            sendMeHome = Witchery.resource("witchery.rite.mirrormirrorsendmehome").toLowerCase().replace("\'", "").replace(",", "").trim();
         }

         if(iGiveUp == null) {
            iGiveUp = Witchery.resource("witchery.rite.mirrormirrorigiveup").toLowerCase().replace("\'", "").replace(",", "").trim();
         }

         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         ItemGeneral var10000;
         if(message.toLowerCase().replace("\'", "").replace(",", "").trim().startsWith(sendMeHome)) {
            if(!playerEx.canEscapeMirrorWorld(1)) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.mirrormirror.escapecooldown", new Object[]{Long.valueOf(playerEx.getCooldownSecs(1)).toString()});
               return true;
            }

            Coord coords = playerEx.getMirrorWorldEntryPoint();
            if(coords != null) {
               playerEx.escapedMirrorWorld(1);
               player.setPositionAndRotation(player.posX, player.posY, player.posZ, 270.0F, player.rotationPitch);
               var10000 = Witchery.Items.GENERIC;
               ItemGeneral.teleportToLocation(player.worldObj, 0.5D + (double)coords.x, 0.01D + (double)coords.y, 0.5D + (double)coords.z, player.dimension, player, true, ParticleEffect.SPLASH, SoundEffect.RANDOM_SPLASH);
               return true;
            }
         } else if(message.toLowerCase().replace("\'", "").replace(",", "").trim().startsWith(iGiveUp)) {
            if(!playerEx.canEscapeMirrorWorld(2)) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.mirrormirror.escapecooldown", new Object[]{Long.valueOf(playerEx.getCooldownSecs(2)).toString()});
               return true;
            }

            ChunkCoordinates var7 = player.getBedLocation(player.dimension);
            int dimension = player.dimension;
            Object world = player.worldObj;
            if(var7 == null) {
               var7 = player.getBedLocation(0);
               dimension = 0;
               world = MinecraftServer.getServer().worldServerForDimension(0);
               if(var7 == null) {
                  for(var7 = ((World)world).getSpawnPoint(); ((World)world).getBlock(var7.posX, var7.posY, var7.posZ).isNormalCube() && var7.posY < 255; ++var7.posY) {
                     ;
                  }
               }
            }

            if(var7 != null) {
               var7 = Blocks.bed.getBedSpawnPosition((IBlockAccess)world, var7.posX, var7.posY, var7.posZ, (EntityPlayer)null);
               if(var7 != null) {
                  playerEx.escapedMirrorWorld(2);
                  var10000 = Witchery.Items.GENERIC;
                  ItemGeneral.teleportToLocation(player.worldObj, (double)var7.posX, (double)(var7.posY + 1), (double)var7.posZ, dimension, player, true);
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity.ticksExisted % 5 == 1 && this.isTransportableEntity(entity)) {
         int i1 = world.getBlockMetadata(x, y, z);
         byte hitZoneyShift = 0;
         if(!isBlockTopOfMirror(i1)) {
            ++y;
            if(entity.height <= 1.0F) {
               hitZoneyShift = -1;
            }

            if(world.getBlock(x, y, z) != this) {
               return;
            }
         }

         AxisAlignedBB box = this.getServerSelectedBoundingBoxFromPool(world, x, y + hitZoneyShift, z);
         double f = (double)entity.width * 0.5D;
         double f1 = (double)entity.height;
         AxisAlignedBB entityBox = AxisAlignedBB.getBoundingBox(entity.posX - f, entity.posY - (double)entity.yOffset + (double)entity.ySize, entity.posZ - f, entity.posX + f, entity.posY - (double)entity.yOffset + (double)entity.ySize + f1, entity.posZ + f);
         if(entityBox.intersectsWith(box)) {
            BlockMirror.TileEntityMirror tile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(world, x, y, z, BlockMirror.TileEntityMirror.class);
            if(tile != null) {
               int side = getDirection(world.getBlockMetadata(x, y, z));
               int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
               int dx = 0;
               int dz = 0;
               float shift = 0.7F;
               float xShift = 0.0F;
               float zShift = 0.0F;
               byte scale = 1;
               byte requiredSide = 0;
               boolean isLiving = entity instanceof EntityLivingBase;
               if(side == 0) {
                  dz = scale;
                  zShift = -shift;
                  requiredSide = 1;
                  if(isLiving && facing != 0) {
                     return;
                  }
               } else if(side == 1) {
                  dz = -scale;
                  zShift = shift;
                  requiredSide = 0;
                  if(isLiving && facing != 2) {
                     return;
                  }
               } else if(side == 2) {
                  dx = scale;
                  xShift = -shift;
                  requiredSide = 3;
                  if(isLiving && facing != 3) {
                     return;
                  }
               } else if(side == 3) {
                  dx = -scale;
                  requiredSide = 2;
                  xShift = shift;
                  if(isLiving && facing != 1) {
                     return;
                  }
               }

               boolean inMirrorWorld = entity.dimension == Config.instance().dimensionMirrorID;
               ItemGeneral var10000;
               int var64;
               if(!this.unbreakable) {
                  int playerEx;
                  int player;
                  int dimX;
                  Block dimY;
                  if(inMirrorWorld || tile.demonKilled) {
                     for(player = 1; player < 32; ++player) {
                        playerEx = x + dx * player;
                        dimX = z + dz * player;
                        dimY = world.getBlock(playerEx, y, dimX);
                        if(dimY == this && getDirection(world.getBlockMetadata(playerEx, y, dimX)) == requiredSide) {
                           var10000 = Witchery.Items.GENERIC;
                           ItemGeneral.teleportToLocation(world, 0.5D + (double)playerEx - (double)xShift, (double)(y - 1) + 0.01D, 0.5D + (double)dimX - (double)zShift, world.provider.dimensionId, entity, true, ParticleEffect.SPLASH, SoundEffect.RANDOM_SPLASH);
                           return;
                        }
                     }
                  }

                  int var59;
                  if(inMirrorWorld) {
                     for(player = 1; player < 10; ++player) {
                        playerEx = x + dx * player;
                        dimX = z + dz * player;
                        if(world.isAirBlock(playerEx, y, dimX) && world.isAirBlock(playerEx, y - 1, dimX)) {
                           boolean var58 = false;
                           if(entity instanceof EntityPlayer) {
                              EntityPlayer dimZ = (EntityPlayer)entity;
                              ExtendedPlayer targetDimension = ExtendedPlayer.get(dimZ);
                              var58 = targetDimension.isMirrorWorldEntryPoint(playerEx, y, dimX);
                           }

                           var59 = (playerEx >> 4 << 4) + 4;
                           var64 = (y >> 4 << 4) + 8;
                           int otherWorld = (dimX >> 4 << 4) + 8;
                           boolean face = world.getBlock(var59, var64, otherWorld) == Witchery.Blocks.MIRROR_UNBREAKABLE;
                           if(!face || var58) {
                              IPowerSource targetX = PowerSources.findClosestPowerSource(world, x, y, z);
                              if(targetX != null && targetX.consumePower(3000.0F)) {
                                 var10000 = Witchery.Items.GENERIC;
                                 ItemGeneral.teleportToLocation(world, 0.5D + (double)playerEx - (double)xShift, (double)y + 0.01D, 0.5D + (double)dimX - (double)zShift, world.provider.dimensionId, entity, true, ParticleEffect.SPLASH, SoundEffect.RANDOM_SPLASH);
                              }
                           }

                           return;
                        }
                     }
                  } else if(tile.demonKilled) {
                     int dimCoords;
                     S08PacketPlayerPosLook mside;
                     double var63;
                     float var71;
                     EntityPlayer var66;
                     for(player = 2; player < 16; ++player) {
                        dimCoords = y + player;
                        dimY = world.getBlock(x, dimCoords, z);
                        if(dimY == this) {
                           var59 = world.getBlockMetadata(x, dimCoords, z);
                           if(getDirection(var59) == side) {
                              if(isBlockTopOfMirror(var59)) {
                                 --dimCoords;
                              }

                              var10000 = Witchery.Items.GENERIC;
                              ItemGeneral.teleportToLocation(world, 0.5D + (double)x + (double)xShift, (double)dimCoords + 0.01D, 0.5D + (double)z + (double)zShift, world.provider.dimensionId, entity, true, ParticleEffect.SPLASH, SoundEffect.RANDOM_SPLASH);
                              if(entity instanceof EntityPlayer) {
                                 var66 = (EntityPlayer)entity;
                                 var63 = (double)(var66.rotationYaw + 180.0F);
                                 var71 = (float)var63 % 360.0F;
                                 mside = new S08PacketPlayerPosLook(var66.posX, var66.posY, var66.posZ, var71, var66.rotationPitch, false);
                                 Witchery.packetPipeline.sendTo((Packet)mside, var66);
                              }

                              return;
                           }
                        }
                     }

                     for(player = 2; player < 16; ++player) {
                        dimCoords = y - player;
                        dimY = world.getBlock(x, dimCoords, z);
                        if(dimY == this) {
                           var59 = world.getBlockMetadata(x, dimCoords, z);
                           if(getDirection(var59) == side) {
                              if(isBlockTopOfMirror(var59)) {
                                 --dimCoords;
                              }

                              var10000 = Witchery.Items.GENERIC;
                              ItemGeneral.teleportToLocation(world, 0.5D + (double)x + (double)xShift, (double)dimCoords + 0.01D, 0.5D + (double)z + (double)zShift, world.provider.dimensionId, entity, true, ParticleEffect.SPLASH, SoundEffect.RANDOM_SPLASH);
                              if(entity instanceof EntityPlayer) {
                                 var66 = (EntityPlayer)entity;
                                 var63 = (double)(var66.rotationYaw + 180.0F);
                                 var71 = (float)var63 % 360.0F;
                                 mside = new S08PacketPlayerPosLook(var66.posX, var66.posY, var66.posZ, var71, var66.rotationPitch, false);
                                 Witchery.packetPipeline.sendTo((Packet)mside, var66);
                              }

                              return;
                           }
                        }
                     }
                  }
               }

               if(entity instanceof EntityPlayer) {
                  EntityPlayer var57 = (EntityPlayer)entity;
                  ExtendedPlayer var56 = ExtendedPlayer.get(var57);
                  if(!inMirrorWorld || var56.isMirrorWorldEntryPoint(x, y, z)) {
                     Coord var60 = tile.getDimCoords();
                     if(var60 != null) {
                        float var61 = (float)var60.x + 0.5F;
                        float var67 = (float)var60.y + 0.01F;
                        float var65 = (float)var60.z + 0.5F;
                        var64 = !inMirrorWorld?Config.instance().dimensionMirrorID:tile.dim;
                        WorldServer var62 = MinecraftServer.getServer().worldServerForDimension(var64);
                        float var72 = 0.0F;
                        if(var62 != null) {
                           Block var70 = var62.getBlock(var60.x, var60.y, var60.z);
                           if(var70 == Witchery.Blocks.MIRROR || var70 == Witchery.Blocks.MIRROR_UNBREAKABLE) {
                              int var69 = getDirection(var62.getBlockMetadata(var60.x, var60.y, var60.z));
                              float targetY = 1.0F;
                              if(var69 == 0) {
                                 var72 = 180.0F;
                                 var65 -= targetY;
                              } else if(var69 == 1) {
                                 var72 = 0.0F;
                                 var65 += targetY;
                              } else if(var69 == 2) {
                                 var72 = 90.0F;
                                 var61 -= targetY;
                              } else if(var69 == 3) {
                                 var72 = 270.0F;
                                 var61 += targetY;
                              }

                              var57.rotationYaw = var72;
                              BlockMirror.TileEntityMirror otherTile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(var62, var60.x, var60.y, var60.z, BlockMirror.TileEntityMirror.class);
                              if(otherTile != null) {
                                 if(otherTile.onCooldown()) {
                                    return;
                                 }

                                 otherTile.addCooldown(60);
                              }
                           }
                        }

                        ParticleEffect.SPLASH.send(SoundEffect.RANDOM_SPLASH, entity, 0.5D, 2.0D, 16);
                        double var68;
                        double var74;
                        if(entity.dimension != Config.instance().dimensionMirrorID) {
                           if(!tile.demonKilled) {
                              var68 = 7.0D;
                              var74 = 6.0D;
                              float targetZ = (float)(var60.x + 4);
                              float cellMidY = (float)var60.y;
                              float targetDim = (float)var60.z;
                              AxisAlignedBB CHECK_PLAYER_INV = AxisAlignedBB.getBoundingBox((double)targetZ - var68, (double)cellMidY - var74, (double)targetDim - var68, (double)targetZ + var68, (double)cellMidY + var74, (double)targetDim + var68);
                              List server = var62.getEntitiesWithinAABB(EntityReflection.class, CHECK_PLAYER_INV);
                              if(server.size() == 0) {
                                 EntityReflection arr$ = new EntityReflection(var62);
                                 arr$.setPositionAndRotation(0.5D + (double)targetZ, 1.1D + (double)cellMidY, 0.5D + (double)targetDim, 0.0F, 0.0F);
                                 arr$.func_110163_bv();
                                 arr$.worldObj.spawnEntityInWorld(arr$);
                              }
                           }

                           var56.setMirrorWorldEntryPoint(var60.x, var60.y, var60.z);
                           var57.setPositionAndRotation((double)var61, (double)(var67 - 1.0F), (double)var65, var72, var57.rotationPitch);
                           var10000 = Witchery.Items.GENERIC;
                           ItemGeneral.travelToDimension(var57, Config.instance().dimensionMirrorID);
                           var57.setPositionAndUpdate((double)var61, (double)(var67 - 1.0F), (double)var65);
                        } else if(tile.isConnected) {
                           var57.setPositionAndRotation((double)var61, (double)(var67 - 1.0F), (double)var65, var72, var57.rotationPitch);
                           var10000 = Witchery.Items.GENERIC;
                           ItemGeneral.travelToDimension(var57, tile.dim);
                           var57.setPositionAndUpdate((double)var61, (double)(var67 - 1.0F), (double)var65);
                        } else {
                           var68 = (double)var61;
                           var74 = (double)(var67 - 1.0F);
                           double var73 = (double)var65;
                           int var75 = tile.dim;
                           boolean var76 = true;
                           MinecraftServer var77 = MinecraftServer.getServer();
                           WorldServer[] var78 = var77.worldServers;
                           int len$ = var78.length;

                           for(int i$ = 0; i$ < len$; ++i$) {
                              WorldServer worldServer = var78[i$];
                              Iterator i$1 = worldServer.playerEntities.iterator();

                              while(i$1.hasNext()) {
                                 Object obj = i$1.next();
                                 EntityPlayer otherPlayer = (EntityPlayer)obj;
                                 ItemStack[] arr$1 = otherPlayer.inventory.mainInventory;
                                 int len$1 = arr$1.length;

                                 for(int i$2 = 0; i$2 < len$1; ++i$2) {
                                    ItemStack stack = arr$1[i$2];
                                    if(stack != null && stack.getItem() == Witchery.Items.MIRROR) {
                                       boolean isMirror = tile.isTargettedBy(stack);
                                       if(isMirror) {
                                          if(otherPlayer.dimension != Config.instance().dimensionMirrorID) {
                                             var68 = otherPlayer.posX;
                                             var74 = otherPlayer.posY;
                                             var73 = otherPlayer.posZ;
                                             var75 = otherPlayer.dimension;
                                          }
                                          break;
                                       }
                                    }
                                 }
                              }
                           }

                           var57.setPositionAndRotation(var68, var74, var73, var72, var57.rotationPitch);
                           var10000 = Witchery.Items.GENERIC;
                           ItemGeneral.travelToDimension(var57, var75);
                           var57.setPositionAndUpdate(var68, var74, var73);
                        }

                        ParticleEffect.SPLASH.send(SoundEffect.RANDOM_SPLASH, entity, 0.5D, 2.0D, 16);
                     }
                  }
               }
            }
         }
      }

   }

   public void demonSlain(World world, double posX, double posY, double posZ) {
      if(!world.isRemote) {
         double R = 7.0D;
         double RY = 6.0D;
         int x = (MathHelper.floor_double(posX) >> 4 << 4) + 4;
         int xmid = x + 4;
         int y = (MathHelper.floor_double(posY) >> 4 << 4) + 8;
         int z = (MathHelper.floor_double(posZ) >> 4 << 4) + 8;
         if(world.getBlock(x, y, z) == Witchery.Blocks.MIRROR_UNBREAKABLE) {
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)xmid - R, (double)y - RY, (double)z - R, (double)xmid + R, (double)y + RY, (double)z + R);
            List reflections = world.getEntitiesWithinAABB(EntityReflection.class, bounds);
            int livingDemons = 0;
            Iterator tile = reflections.iterator();

            while(tile.hasNext()) {
               EntityReflection dimCoords = (EntityReflection)tile.next();
               if(dimCoords != null && dimCoords.isEntityAlive()) {
                  ++livingDemons;
               }
            }

            if(livingDemons == 0) {
               BlockMirror.TileEntityMirror var24 = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(world, x, y, z, BlockMirror.TileEntityMirror.class);
               if(var24 != null) {
                  Coord var25 = var24.getDimCoords();
                  int dim = var24.dim;
                  WorldServer otherWorld = MinecraftServer.getServer().worldServerForDimension(dim);
                  if(otherWorld != null) {
                     BlockMirror.TileEntityMirror otherTile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(otherWorld, var25.x, var25.y, var25.z, BlockMirror.TileEntityMirror.class);
                     if(otherTile != null) {
                        otherTile.demonKilled = true;
                     }
                  }
               }
            }
         }
      }

   }

   private boolean isTransportableEntity(Entity entity) {
      return !(entity instanceof EntityMirrorFace) && (entity instanceof EntityLivingBase || entity instanceof EntityItem);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         if(!this.unbreakable) {
            int i1 = world.getBlockMetadata(x, y, z);
            if(!isBlockTopOfMirror(i1)) {
               ++y;
               if(world.getBlock(x, y, z) != this) {
                  return true;
               }
            }

            BlockMirror.TileEntityMirror tile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(world, x, y, z, BlockMirror.TileEntityMirror.class);
            if(tile == null) {
               return true;
            }

            tile.depolyDemon(player);
         }

         return true;
      }
   }

   public int getRenderType() {
      return -1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      int side = getDirection(world.getBlockMetadata(x, y, z));
      float w = 0.15F;
      if(side == 0) {
         this.setBlockBounds(0.0F, 0.0F, 0.85F, 1.0F, 1.0F, 1.0F);
      } else if(side == 1) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.15F);
      } else if(side == 2) {
         this.setBlockBounds(0.85F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else if(side == 3) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.15F, 1.0F, 1.0F);
      }

      AxisAlignedBB bounds = super.getCollisionBoundingBoxFromPool(world, x, y, z);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return bounds;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      int side = getDirection(world.getBlockMetadata(x, y, z));
      float w = 0.32F;
      if(side == 0) {
         this.setBlockBounds(0.0F, 0.0F, 0.68F, 1.0F, 1.0F, 1.0F);
      } else if(side == 1) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.32F);
      } else if(side == 2) {
         this.setBlockBounds(0.68F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else if(side == 3) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.32F, 1.0F, 1.0F);
      }

      AxisAlignedBB bounds = super.getSelectedBoundingBoxFromPool(world, x, y, z);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return bounds;
   }

   public AxisAlignedBB getServerSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      int side = getDirection(world.getBlockMetadata(x, y, z));
      float w = 0.32F;
      float minX = 0.0F;
      float minY = 0.0F;
      float minZ = 0.0F;
      float maxX = 1.0F;
      float maxY = 1.0F;
      float maxZ = 1.0F;
      if(side == 0) {
         minZ = 0.68F;
      } else if(side == 1) {
         maxZ = 0.32F;
      } else if(side == 2) {
         minX = 0.68F;
      } else if(side == 3) {
         maxZ = 0.32F;
      }

      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)((float)x + minX), (double)((float)y + minY), (double)((float)z + minZ), (double)((float)x + maxX), (double)((float)y + maxY), (double)((float)z + maxZ));
      return bounds;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int l = world.getBlockMetadata(x, y, z);
      int i1 = getDirection(l);
      if(isBlockTopOfMirror(l)) {
         if(world.getBlock(x, y - 1, z) != this) {
            if(!world.isRemote) {
               this.dropBlockAsItem(world, x, y, z, l, 0);
            }

            world.setBlockToAir(x, y, z);
         }
      } else if(world.getBlock(x, y + 1, z) != this) {
         world.setBlockToAir(x, y, z);
      }

   }

   public Item getItemDropped(int meta, Random rand, int p_149650_3_) {
      return isBlockTopOfMirror(meta)?Witchery.Items.MIRROR:Item.getItemById(0);
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
      if(isBlockTopOfMirror(p_149690_5_)) {
         super.dropBlockAsItemWithChance(world, x, y, z, p_149690_5_, p_149690_6_, 0);
      }

   }

   public int getMobilityFlag() {
      return super.getMobilityFlag();
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World world, int x, int y, int z) {
      return Witchery.Items.MIRROR;
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {}

   public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
      if(player.capabilities.isCreativeMode && isBlockTopOfMirror(meta)) {
         if(world.getBlock(x, y - 1, z) == this) {
            world.setBlockToAir(x, y - 1, z);
         }

         meta |= 8;
         world.setBlockMetadataWithNotify(x, y, z, meta, 4);
      }

      this.dropBlockAsItem(world, x, y, z, meta, 0);
      super.onBlockHarvested(world, x, y, z, meta, player);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int meta, int fortune) {
      ArrayList drops = new ArrayList();
      boolean brokenInCreativeMode = (meta & 8) != 0;
      if(!brokenInCreativeMode) {
         BlockMirror.TileEntityMirror tile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(world, x, y, z, BlockMirror.TileEntityMirror.class);
         if(tile != null) {
            ItemStack stack = new ItemStack(Witchery.Items.MIRROR);
            NBTTagCompound nbtRoot = new NBTTagCompound();
            tile.writeItemDataToNBT(nbtRoot);
            stack.setTagCompound(nbtRoot);
            if(world.provider.dimensionId != Config.instance().dimensionMirrorID && tile.isDimLinked()) {
               Coord dimCoords = tile.getDimCoords();
               WorldServer otherWorld = MinecraftServer.getServer().worldServerForDimension(Config.instance().dimensionMirrorID);
               if(otherWorld != null && otherWorld.getBlock(dimCoords.x, dimCoords.y, dimCoords.z) == Witchery.Blocks.MIRROR) {
                  BlockMirror.TileEntityMirror otherTile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(otherWorld, dimCoords.x, dimCoords.y, dimCoords.z, BlockMirror.TileEntityMirror.class);
                  if(otherTile != null) {
                     otherTile.isConnected = false;
                     otherTile.markBlockForUpdate(false);
                  }
               }
            }

            drops.add(stack);
         }
      }

      return drops;
   }

   public void loadFromItem(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
      BlockMirror.TileEntityMirror tile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(world, x, y, z, BlockMirror.TileEntityMirror.class);
      if(tile != null) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         if(nbtRoot != null) {
            tile.readItemDataFromNBT(nbtRoot);
            if(world.provider.dimensionId != Config.instance().dimensionMirrorID && tile.isDimLinked()) {
               Coord dimCoords = tile.getDimCoords();
               WorldServer otherWorld = MinecraftServer.getServer().worldServerForDimension(Config.instance().dimensionMirrorID);
               if(otherWorld != null && otherWorld.getBlock(dimCoords.x, dimCoords.y, dimCoords.z) == Witchery.Blocks.MIRROR_UNBREAKABLE) {
                  BlockMirror.TileEntityMirror otherTile = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(otherWorld, dimCoords.x, dimCoords.y, dimCoords.z, BlockMirror.TileEntityMirror.class);
                  if(otherTile != null) {
                     otherTile.isConnected = true;
                     otherTile.dimCoords = new Coord(x, y, z);
                     otherTile.markBlockForUpdate(false);
                  }
               }
            }
         }
      }

   }


   public static class TileEntityMirror extends TileEntityBase {

      public int men;
      private Coord dimCoords;
      private int dim;
      private boolean isConnected;
      private boolean demonKilled;
      private GameProfile favorite;
      private UUID fairest;
      private Set playersSeen = new HashSet();
      long cooldown;
      long lastFairestSpawn = 0L;


      public void updateEntity() {
         super.updateEntity();
         if(super.ticks % (long)(super.worldObj.isRemote?10:40) == 1L) {
            int side = BlockMirror.getDirection(super.worldObj.getBlockMetadata(super.xCoord, super.yCoord, super.zCoord));
            byte xMin = -1;
            byte xMax = 1;
            byte zMin = -1;
            byte zMax = 1;
            boolean scale = true;
            if(side == 0) {
               zMin = -4;
               zMax = 0;
            } else if(side == 1) {
               zMin = 0;
               zMax = 4;
            } else if(side == 2) {
               xMin = -4;
               xMax = 0;
            } else if(side == 3) {
               xMin = 0;
               xMax = 4;
            }

            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(super.xCoord + xMin), (double)super.yCoord, (double)(super.zCoord + zMin), (double)(super.xCoord + xMax + 1), (double)(super.yCoord + 1), (double)(super.zCoord + zMax + 1));
            List entities = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            this.men = entities.size();
            if(!super.worldObj.isRemote) {
               Iterator i$ = entities.iterator();

               while(i$.hasNext()) {
                  EntityLivingBase entity = (EntityLivingBase)i$.next();
                  if(entity instanceof EntityPlayer) {
                     this.playersSeen.add(entity.getCommandSenderName());
                  }
               }
            }
         }

      }

      public void addCooldown(int i) {
         this.cooldown = super.ticks + (long)i;
      }

      public boolean onCooldown() {
         return super.ticks < this.cooldown;
      }

      public boolean isTargettedBy(ItemStack stack) {
         if(stack != null && stack.getItem() == Witchery.Items.MIRROR && stack.getTagCompound() != null) {
            NBTTagCompound nbtRoot = stack.getTagCompound();
            if(nbtRoot.hasKey("DimCoords") && nbtRoot.hasKey("Dimension") && super.worldObj.provider.dimensionId == nbtRoot.getInteger("Dimension")) {
               Coord coords = Coord.fromTagNBT(nbtRoot.getCompoundTag("DimCoords"));
               if(coords != null) {
                  return coords.isMatch(super.xCoord, super.yCoord, super.zCoord);
               }
            }
         }

         return false;
      }

      private void depolyDemon(EntityPlayer player) {
         if(!this.demonKilled && super.worldObj.provider.dimensionId != Config.instance().dimensionMirrorID) {
            if(player.getHeldItem() != null && player.getHeldItem().getItem() == Witchery.Items.TAGLOCK_KIT) {
               ExtendedPlayer var20 = ExtendedPlayer.get(player);
               TransformCreature var22 = var20.getCreatureType();
               if(var22 == TransformCreature.NONE || var22 == TransformCreature.PLAYER) {
                  String var23 = Witchery.Items.TAGLOCK_KIT.getBoundUsername(player.getHeldItem(), Integer.valueOf(1));
                  if(var23 != null && !var23.isEmpty() && !var23.equals(player.getCommandSenderName())) {
                     IPowerSource var24 = PowerSources.findClosestPowerSource(this);
                     if(var24 != null && var24.consumePower(4000.0F)) {
                        ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, player, 0.5D, 2.0D, 16);
                        var20.setOtherPlayerSkin(var23);
                        Shapeshift.INSTANCE.shiftTo(player, TransformCreature.PLAYER);
                     } else {
                        ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, player, 0.5D, 2.0D, 16);
                     }
                  } else if(var22 == TransformCreature.PLAYER) {
                     ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, player, 0.5D, 2.0D, 16);
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
                  } else {
                     ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, player, 0.5D, 2.0D, 16);
                  }
               }
            } else if(player.getHeldItem() != null && Witchery.Items.GENERIC.itemQuartzSphere.isMatch(player.getHeldItem())) {
               IPowerSource var19 = PowerSources.findClosestPowerSource(this);
               if(var19 != null && var19.consumePower(2000.0F)) {
                  ParticleEffect.SMOKE.send(SoundEffect.RANDOM_ORB, player, 0.5D, 2.0D, 16);
                  ItemStack var21 = player.getHeldItem();
                  ItemStack newStack;
                  if(var21.stackSize > 1) {
                     newStack = new ItemStack(Witchery.Items.DUP_GRENADE);
                     ItemSunGrenade.setOwnerName(newStack, player.getCommandSenderName());
                     --var21.stackSize;
                     if(var21.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                     }

                     if(!player.inventory.addItemStackToInventory(newStack)) {
                        super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, newStack));
                     } else if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }
                  } else {
                     newStack = new ItemStack(Witchery.Items.DUP_GRENADE);
                     ItemSunGrenade.setOwnerName(newStack, player.getCommandSenderName());
                     player.setCurrentItemOrArmor(0, newStack);
                     if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }
                  }
               } else {
                  ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, player, 0.5D, 2.0D, 16);
               }
            } else {
               List faces = super.worldObj.getEntitiesWithinAABB(EntityMirrorFace.class, Witchery.Blocks.MIRROR.getCollisionBoundingBoxFromPool(super.worldObj, super.xCoord, super.yCoord, super.zCoord));
               if(faces.size() == 0) {
                  this.showMirrorHead(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                  ParticleEffect.SPELL_COLORED.send(SoundEffect.WITCHERY_MOB_REFLECTION_SPEECH, (TileEntity)this, 0.5D, 0.5D, 16, 7829503);
                  double RANGE = 64.0D;
                  List players = super.worldObj.playerEntities;
                  Iterator fairestFound = players.iterator();

                  while(fairestFound.hasNext()) {
                     EntityPlayer seen = (EntityPlayer)fairestFound.next();
                     if(player.getDistanceSq((double)super.xCoord, (double)super.yCoord, (double)super.zCoord) <= RANGE * RANGE) {
                        ChatUtil.sendTranslated(seen, "witchery.rite.mirrormirror", new Object[]{player.getCommandSenderName()});
                     }
                  }

                  boolean var25 = false;
                  if(this.fairest != null) {
                     double var26 = 100.0D;
                     double i$ = 32.0D;
                     AxisAlignedBB followerType = AxisAlignedBB.getBoundingBox((double)super.xCoord - 100.0D, (double)super.yCoord - 32.0D, (double)super.zCoord - 100.0D, (double)super.xCoord + 100.0D, (double)super.yCoord + 32.0D, (double)super.zCoord + 100.0D);
                     List coord = super.worldObj.getEntitiesWithinAABB(EntityFollower.class, followerType);
                     Iterator minRange = coord.iterator();

                     while(minRange.hasNext()) {
                        EntityFollower i = (EntityFollower)minRange.next();
                        if(i.getPersistentID().equals(this.fairest) && i.isEntityAlive()) {
                           this.sayNotFairest(player, i);
                           var25 = true;
                           break;
                        }
                     }
                  }

                  if(!var25) {
                     boolean var28 = super.worldObj.getTotalWorldTime() > this.lastFairestSpawn + (long)TimeUtil.minsToTicks(2);
                     this.fairest = null;
                     if(this.favorite != null && !this.isFavorite(player)) {
                        ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.anotherplayer", new Object[0]);
                        EntityPlayer var30 = super.worldObj.getPlayerEntityByName(this.favorite.getName());
                        if(var30 != null) {
                           this.sayBearing(player, var30);
                        }
                     } else {
                        this.favorite = player.getGameProfile();
                        double sb = Config.instance().fairestSpawnChance;
                        if(var28 && super.worldObj.rand.nextDouble() < sb) {
                           EntityFollower s = new EntityFollower(super.worldObj);
                           int var32 = super.worldObj.rand.nextInt(4) + 1;
                           s.setCustomNameTag(EntityFollower.generateFollowerName(var32));
                           s.func_110163_bv();
                           s.setFollowerType(var32);
                           Coord var34 = null;
                           boolean var35 = true;

                           for(int var36 = 0; var36 < 25 && var34 == null; ++var36) {
                              int x = super.xCoord + (super.worldObj.rand.nextBoolean()?1:-1) * (50 + super.worldObj.rand.nextInt(50));
                              int z = super.zCoord + (super.worldObj.rand.nextBoolean()?1:-1) * (50 + super.worldObj.rand.nextInt(50));
                              int y = Math.min(super.yCoord + 20, 250);

                              for(int yMin = Math.max(super.yCoord - 20, 2); y >= yMin; --y) {
                                 if(super.worldObj.getBlock(x, y, z).isNormalCube() && super.worldObj.getBlock(x, y + 1, z).getMaterial().isReplaceable() && super.worldObj.isAirBlock(x, y + 2, z)) {
                                    var34 = new Coord(x, y, z);
                                    break;
                                 }
                              }
                           }

                           if(var34 != null) {
                              s.setPositionAndRotation(0.5D + (double)var34.x, 1.01D + (double)var34.y, 0.5D + (double)var34.z, 0.0F, 0.0F);
                              super.worldObj.spawnEntityInWorld(s);
                              this.fairest = s.getPersistentID();
                              var25 = true;
                              this.lastFairestSpawn = super.worldObj.getTotalWorldTime();
                              this.sayNotFairest(player, s);
                           }
                        }

                        if(!var25) {
                           ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.you", new Object[0]);
                        }
                     }
                  }

                  if(this.playersSeen.size() > 1) {
                     ArrayList var27 = new ArrayList(this.playersSeen);
                     Collections.sort(var27);
                     StringBuffer var29 = new StringBuffer();
                     Iterator var31 = var27.iterator();

                     while(var31.hasNext()) {
                        String var33 = (String)var31.next();
                        if(!var33.equals(player.getCommandSenderName())) {
                           if(var29.length() > 0) {
                              var29.append(", ");
                           }

                           var29.append(var33);
                        }
                     }

                     if(var29.length() > 0) {
                        ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.playersseen", new Object[]{var29.toString()});
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.playersnotseen", new Object[0]);
                     }
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.playersnotseen", new Object[0]);
                  }

                  if(this.isFavorite(player)) {
                     this.playersSeen.clear();
                  }
               }
            }
         }

      }

      public void sayNotFairest(EntityPlayer player, EntityFollower follower) {
         if(follower.getFollowerType() == 4) {
            ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.anotherm", new Object[0]);
         } else {
            ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.anotherf", new Object[0]);
         }

         this.sayBearing(player, follower);
      }

      public void sayBearing(EntityPlayer player, EntityLivingBase otherEntity) {
         double bearingRadians = Math.atan2(0.5D + (double)super.zCoord - otherEntity.posZ, 0.5D + (double)super.xCoord - otherEntity.posX);
         double bearing = (Math.toDegrees(bearingRadians) + 180.0D + 90.0D) % 360.0D;
         if(bearing < 0.0D) {
            bearing += 360.0D;
         }

         int bearingIndex = (int)bearing / 45;
         if(bearingIndex > 7 || bearingIndex < 0) {
            bearingIndex = 0;
         }

         ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "witchery.rite.mirrormirror.bearing" + bearingIndex, new Object[0]);
      }

      private void showMirrorHead(World world, int x, int y, int z) {
         int side = BlockMirror.getDirection(world.getBlockMetadata(x, y, z));
         float dx = 0.0F;
         float dz = 0.0F;
         float scale = 0.4F;
         float rotation = 0.0F;
         if(side == 0) {
            dz = 0.4F;
            rotation = -90.0F;
         } else if(side == 1) {
            dz = -0.4F;
            rotation = 90.0F;
         } else if(side == 2) {
            dx = 0.4F;
            rotation = 0.0F;
         } else if(side == 3) {
            dx = -0.4F;
            rotation = -90.0F;
         }

         EntityMirrorFace face = new EntityMirrorFace(world);
         face.setPosition((double)x + 0.5D + (double)dx, (double)y + 0.1D, (double)z + 0.5D + (double)dz);
         world.spawnEntityInWorld(face);
      }

      private boolean isDimLinked() {
         return this.dimCoords != null;
      }

      private Coord getDimCoords() {
         if(this.dimCoords == null && super.worldObj.provider.dimensionId != Config.instance().dimensionMirrorID) {
            WorldServer mworld = MinecraftServer.getServer().worldServerForDimension(Config.instance().dimensionMirrorID);
            if(mworld != null) {
               int[][] map = new int[][]{{0, 1}, {1, 0}};
               int cellX = 0;
               int cellZ = 0;
               int sign = 1;

               for(int i = 0; i < 256; ++i) {
                  for(int spin = 0; spin <= i; ++spin) {
                     for(int j = 0; j < map.length; ++j) {
                        if(i > 0) {
                           cellX += map[j][0] * sign;
                           cellZ += map[j][1] * sign;
                        }

                        int Y_LEVELS = Config.instance().shrinkMirrorWorld?8:15;

                        for(int cellY = 0; cellY < Y_LEVELS; ++cellY) {
                           int dimX = (cellX << 4) + 4;
                           int dimY = (cellY << 4) + 8;
                           int dimZ = (cellZ << 4) + 8;
                           if(mworld.isAirBlock(dimX, dimY, dimZ) && mworld.isAirBlock(dimX, dimY - 1, dimZ)) {
                              boolean stop = false;

                              for(int mirror = dimY - 1; mirror <= dimY + 6 && !stop; ++mirror) {
                                 int meta = dimX;

                                 while(meta <= dimX + 8 && !stop) {
                                    int tile = dimZ - 4;

                                    while(true) {
                                       if(tile <= dimZ + 4 && !stop) {
                                          mworld.getBlock(meta, mirror, tile);
                                          if(mworld.isAirBlock(meta, mirror, tile)) {
                                             ++tile;
                                             continue;
                                          }

                                          stop = true;
                                       }

                                       ++meta;
                                       break;
                                    }
                                 }
                              }

                              if(!stop) {
                                 BlockMirror var20 = Witchery.Blocks.MIRROR_UNBREAKABLE;
                                 byte var19 = 3;
                                 mworld.setBlock(dimX, dimY, dimZ, var20, var19 | 4, 3);
                                 BlockMirror.TileEntityMirror var21 = (BlockMirror.TileEntityMirror)BlockUtil.getTileEntity(mworld, dimX, dimY, dimZ, BlockMirror.TileEntityMirror.class);
                                 if(var21 != null) {
                                    var21.dimCoords = new Coord(super.xCoord, super.yCoord, super.zCoord);
                                    var21.dim = super.worldObj.provider.dimensionId;
                                 }

                                 if(mworld.getBlock(dimX, dimY, dimZ) == var20) {
                                    mworld.setBlock(dimX, dimY - 1, dimZ, var20, var19, 3);
                                 }

                                 this.dimCoords = new Coord(dimX, dimY, dimZ);
                                 return this.dimCoords;
                              }
                           }
                        }
                     }
                  }

                  sign *= -1;
               }
            }
         }

         return this.dimCoords;
      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         NBTTagCompound nbtTag = packet.func_148857_g();
         this.readFromNBT(nbtTag);
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setLong("LastFairestSpawnTime", this.lastFairestSpawn);
         this.writeItemDataToNBT(nbtRoot);
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.lastFairestSpawn = nbtRoot.getLong("LastFairestSpawnTime");
         this.readItemDataFromNBT(nbtRoot);
      }

      private void writeItemDataToNBT(NBTTagCompound nbtRoot) {
         NBTTagCompound players;
         if(this.dimCoords != null) {
            players = this.dimCoords.toTagNBT();
            players.setInteger("Dimension", this.dim);
            nbtRoot.setTag("DimCoords", players);
         }

         nbtRoot.setBoolean("DemonSlain", this.demonKilled);
         if(this.favorite != null) {
            players = new NBTTagCompound();
            NBTUtil.func_152460_a(players, this.favorite);
            nbtRoot.setTag("Favorite", players);
         }

         if(this.fairest != null) {
            nbtRoot.setString("Fairest", this.fairest.toString());
         }

         NBTTagList players1 = new NBTTagList();
         Iterator i$ = this.playersSeen.iterator();

         while(i$.hasNext()) {
            String player = (String)i$.next();
            players1.appendTag(new NBTTagString(player));
         }

         nbtRoot.setTag("PlayersSeen", players1);
      }

      public void readItemDataFromNBT(NBTTagCompound nbtRoot) {
         if(nbtRoot.hasKey("DimCoords")) {
            NBTTagCompound players = nbtRoot.getCompoundTag("DimCoords");
            this.dimCoords = Coord.fromTagNBT(players);
            this.dim = players.getInteger("Dimension");
         }

         this.demonKilled = nbtRoot.getBoolean("DemonSlain");
         if(nbtRoot.hasKey("Favorite", 10)) {
            this.favorite = NBTUtil.func_152459_a(nbtRoot.getCompoundTag("Favorite"));
         } else {
            this.favorite = null;
         }

         if(nbtRoot.hasKey("Fairest")) {
            this.fairest = UUID.fromString(nbtRoot.getString("Fairest"));
         } else {
            this.fairest = null;
         }

         this.playersSeen.clear();
         if(nbtRoot.hasKey("PlayersSeen")) {
            NBTTagList var4 = nbtRoot.getTagList("PlayersSeen", 8);

            for(int i = 0; i < var4.tagCount(); ++i) {
               this.playersSeen.add(var4.getStringTagAt(i));
            }
         }

      }

      public boolean isFavorite(EntityPlayer player) {
         return this.favorite != null && player != null && this.favorite.equals(player.getGameProfile());
      }
   }
}
