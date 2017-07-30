package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityLilith;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFollower extends EntityTameable {

   private EntityAIWander aiWander = new EntityAIWander(this, 0.8D);
   int ticksToLive = -1;
   int transformCount;
   @SideOnly(Side.CLIENT)
   private ThreadDownloadImageData downloadImageSkin;
   @SideOnly(Side.CLIENT)
   private ResourceLocation locationSkin;
   private static final String[] FIRST_NAMES_M = new String[]{"Abraham", "Adam", "Adrian", "Alex", "Alexander", "Allen", "Ambrose", "Andrew", "Anthony", "Arthur", "Avery", "Barnaby", "Bartholomew", "Benedict", "Benjamin", "Bernard", "Billy", "Bobby", "Charles", "Charley", "Christopher", "Colin", "Conrad", "Cuthbert", "Daniel", "Danny", "Davey", "David", "Edmund", "Edward", "Francis", "Fred", "Freddy", "Geoffrey", "George", "Georgie", "Gerard", "Gilbert", "Giles", "Gregory", "Hans", "Hansel", "Heinrich", "Henry", "Hugh", "Humphrey", "Isaac", "Jack", "Jacques", "James", "Jamie", "Jerome", "Jim", "Jimmy", "John", "Johnny", "Joseph", "Julian", "Lancelot", "Lawrence", "Leonard", "Lou", "Luke", "Mark", "Martin", "Mathias", "Matthew", "Merlin", "Michael", "Miles", "Nat", "Nathan", "Nathaniel", "Ned", "Nicholas", "Oliver", "Oswyn", "Patrick", "Paul", "Peter", "Philip", "Piers", "Ralph", "Reynold", "Richard", "Ricky", "Robert", "Robin", "Roger", "Rowland", "Samuel", "Simon", "Solomon", "Stephen", "Terence", "Thomas", "Tim", "Tobias", "Tom", "Tommy", "Valentine", "Walter", "Wendell", "Will", "William", "Willie"};
   private static final String[] FIRST_NAMES_F = new String[]{"Agnes", "Alice", "Amy", "Anna", "Annabella", "Anne", "Arabella", "Audrey", "Avis", "Barbara", "Beatrice", "Becky", "Bella", "Belle", "Bertha", "Bessy", "Betty", "Blanche", "Bo", "Bonny", "Bridget", "Catalina", "Catherine", "Cecily", "Charity", "Charlotte", "Christina", "Christine", "Cinderella", "Cindy", "Clara", "Clarissa", "Clemence", "Clementine", "Constance", "Daisy", "Denise", "Dorothy", "Edith", "Elinor", "Elizabeth", "Ella", "Ellen", "Elsa", "Elsie", "Emma", "Eve", "Evelyn", "Fawn", "Flora", "Florence", "Floretta", "Fortune", "Frances", "Frideswide", "Gertrude", "Gillian", "Ginger", "Goat", "Goatley", "Goldie", "Grace", "Gretel", "Helen", "Hilda", "Hazel", "Isabel", "Jane", "Janet", "Jemima", "Jill", "Joan", "Joyce", "Judith", "Julia", "Juliet", "Katherine", "Katie", "Kitty", "Lena", "Lily", "Liza", "Lizzie", "Lucy", "Mabel", "Maggie", "Margaret", "Margery", "Maria", "Marion", "Marlene", "Martha", "Mary", "Maud", "Mildred", "Millicent", "Molly", "Odette", "Pansy", "Pearl", "Petunia", "Philippa", "Polly", "Rachel", "Rapunzel", "Rebecca", "Rose", "Rosie", "Ruth", "Sarah", "Shiela", "Snow", "Susanna", "Susie", "Sybil", "Talia", "Thomasina", "Trudy", "Ursula", "Winifred"};
   private static final String[] SURNAMES = new String[]{"Apple", "Applegreen", "Applerose", "Appleseed", "Appleton", "Applewhite", "Baker", "Barnes", "Bean", "Beanblossom", "Beanstock", "Beaste", "Beasten", "Bell", "Berry", "Bird", "Blackbird", "Blackwood", "Boar", "Botter", "Bowers", "Bremen", "Brockett", "Buckle", "Butcher", "Candle", "Castle", "Castleton", "Cherry", "Cherrytree", "Cherrywood", "Cherrywell", "Cottage", "Daw", "Deer", "Dilly", "Dove", "Duck", "Duckfield", "Duckling", "Faery", "Fairy", "Fiddle", "Fiddler", "Fisher", "Fitcher", "Flinders", "Friday", "Frogg", "Frogley", "Frost", "Gold", "Goldencrown", "Goldhair", "Goodfellow", "Goose", "Gooseberry", "Gosling", "Gray", "Green", "Greengrass", "Griggs", "Grimm", "Grundy", "Hare", "Hay", "Hazeltree", "Hickory", "Hood", "Horner", "Hubbard", "Hunter", "Korbes", "Lamb", "Lampkin", "Lark", "Locket", "Locks", "MacDonald", "Mack", "Malone", "Marsh", "McDiddler", "Meadow", "Meadows", "Merrypips", "Miller", "Mills", "Mockingbird", "Monday", "Mouse", "Mouseley", "Muffet", "Mulberry", "Nimble", "Nutt", "O\'Hare", "O\'Lairy", "Pea", "Peartree", "Pease", "Peep", "Pie", "Pigeon", "Pinchme", "Piper", "Porgy", "Porridge", "Pott", "Pumpkin", "Pumpkinseed", "Reynard", "River", "Rivers", "Roley", "Rooster", "Roseberry", "Rosebottom", "Roseleaf", "Shoe", "Shoemaker", "Shorter", "Silver", "Slipper", "Sprat", "Saturday", "Sparrow", "Spindle", "Spindler", "Spinner", "Star", "Stone", "Stonebridge", "Sunday", "Swan", "Tailor", "Thatcher", "Thumb", "Thursday", "Toad", "Tower", "Towers", "Trot", "Tucker", "Tuesday", "Twist", "Wednesday", "White", "Whittington", "Winkie", "Wolf", "Wolfram", "Wolfson", "Wolfwood", "Woodcroft", "Woods"};


   public EntityFollower(World world) {
      super(world);
      this.setSize(0.6F, 1.8F);
      this.getNavigator().setCanSwim(true);
      this.getNavigator().setAvoidsWater(false);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 2.0D));
      super.tasks.addTask(3, new EntityAIFollowOwner(this, 1.0D, 2.0F, 4.0F));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.experienceValue = 0;
   }

   protected int getExperiencePoints(EntityPlayer p_70693_1_) {
      return 0;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
   }

   public void setTTL(int ticks) {
      this.ticksToLive = ticks;
   }

   public EntityAgeable createChild(EntityAgeable lover) {
      return null;
   }

   public String getCommandSenderName() {
      switch(this.getFollowerType()) {
      case 0:
         return Witchery.resource("entity.witchery.follower.elle.name");
      default:
         return super.getCommandSenderName();
      }
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(18, Integer.valueOf(0));
      super.dataWatcher.addObject(19, String.valueOf(""));
   }

   public String getSkin() {
      return super.dataWatcher.getWatchableObjectString(19);
   }

   public void setSkin(String mode) {
      super.dataWatcher.updateObject(19, mode);
   }

   public int getFollowerType() {
      return super.dataWatcher.getWatchableObjectInt(18);
   }

   public void onEntityUpdate() {
      super.onEntityUpdate();
      if(!super.worldObj.isRemote && super.ticksExisted == 1 && this.getFollowerType() == 5) {
         String skin = this.getSkin();
         if(skin != null && !skin.isEmpty()) {
            EntityPlayer player = super.worldObj.getPlayerEntityByName(this.getSkin());
            if(player != null) {
               for(int i = 0; i <= 4; ++i) {
                  ItemStack stack = player.getEquipmentInSlot(i);
                  if(stack != null) {
                     this.setCurrentItemOrArmor(i, stack.copy());
                  } else {
                     this.setCurrentItemOrArmor(i, (ItemStack)null);
                  }
               }
            }
         }
      }

      if(!super.worldObj.isRemote && super.ticksExisted % 40 == 5 && this.getFollowerType() == 5) {
         this.attractAttention();
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(!super.worldObj.isRemote && this.isEntityAlive() && this.ticksToLive >= 0 && --this.ticksToLive == 0) {
         this.setDead();
      }

   }

   public void setFollowerType(int followerType) {
      super.dataWatcher.updateObject(18, Integer.valueOf(followerType));
      if(followerType == 0) {
         super.isImmuneToFire = true;
      } else if(followerType <= 5) {
         super.tasks.addTask(5, this.aiWander);
      }

   }

   protected int decreaseAirSupply(int par1) {
      return this.getFollowerType() == 0?par1:super.decreaseAirSupply(par1);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}

   protected void dropFewItems(boolean par1, int par2) {
      if(this.getFollowerType() >= 1 && this.getFollowerType() <= 4) {
         this.entityDropItem(Witchery.Items.GENERIC.itemHeartOfGold.createStack(), 0.1F);
      }

   }

   private boolean isCourseTraversable(double waypointX, double waypointY, double waypointZ, double p_70790_7_) {
      double d4 = (waypointX - super.posX) / p_70790_7_;
      double d5 = (waypointY - super.posY) / p_70790_7_;
      double d6 = (waypointZ - super.posZ) / p_70790_7_;
      AxisAlignedBB axisalignedbb = super.boundingBox.copy();

      for(int i = 1; (double)i < p_70790_7_; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!super.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   protected void updateAITasks() {
      super.updateAITasks();
      if(!super.worldObj.isRemote && super.ticksExisted % 10 == 1 && this.getFollowerType() == 0) {
         this.doElleAI();
      }

   }

   public void doElleAI() {
      if(this.hasHome()) {
         if(this.isWithinHomeDistanceCurrentPosition()) {
            ++this.transformCount;
            if(this.transformCount == 20) {
               EntityLivingBase i = this.getOwner();
               if(i != null && i instanceof EntityPlayer) {
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, (EntityPlayer)i, "item.witchery:glassgoblet.lilithquestsummon", new Object[0]);
                  SoundEffect.WITCHERY_MOB_LILITH_TALK.playAt((EntityLiving)this);
               }
            } else if(this.transformCount == 40) {
               this.transformCount = 0;
               ParticleEffect.INSTANT_SPELL.send(SoundEffect.FIREWORKS_BLAST1, this, 1.0D, 1.0D, 16);
               EntityLilith var15 = new EntityLilith(super.worldObj);
               var15.func_110163_bv();
               var15.copyLocationAndAnglesFrom(this);
               var15.onSpawnWithEgg((IEntityLivingData)null);
               super.worldObj.removeEntity(this);
               super.worldObj.spawnEntityInWorld(var15);
               var15.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)var15.posX, (int)var15.posY, (int)var15.posZ, 0);
               EntityLivingBase j = this.getOwner();
               if(j != null && j instanceof EntityPlayer) {
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, (EntityPlayer)j, "item.witchery:glassgoblet.lilithquestsummon2", new Object[0]);
                  SoundEffect.WITCHERY_MOB_LILITH_TALK.playAt((EntityLiving)var15);
               }

               super.worldObj.createExplosion(var15, var15.posX, var15.posY, var15.posZ, 6.0F, true);
            }
         } else {
            double var16 = (double)this.getHomePosition().posX - super.posX;
            double k = (double)this.getHomePosition().posY - super.posY;
            double d2 = (double)this.getHomePosition().posZ - super.posZ;
            double d3 = var16 * var16 + k * k + d2 * d2;
            if(d3 > 0.0D) {
               d3 = (double)MathHelper.sqrt_double(d3);
               double waypointX = super.posX + (double)((super.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
               double waypointY = super.posY + (double)((super.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
               double waypointZ = super.posZ + (double)((super.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
               if(this.isCourseTraversable(waypointX, waypointY, waypointZ, d3)) {
                  super.motionX += var16 / d3 * 0.2D;
                  super.motionY += k / d3 * 0.2D;
                  super.motionZ += d2 / d3 * 0.2D;
               }
            }
         }
      } else {
         for(int var17 = 0; var17 < 10; ++var17) {
            int var18 = MathHelper.floor_double(super.posX + (double)super.rand.nextInt(30) - 15.0D);
            int var19 = MathHelper.floor_double(super.boundingBox.minY + (double)super.rand.nextInt(6) - 3.0D);
            int l = MathHelper.floor_double(super.posZ + (double)super.rand.nextInt(30) - 15.0D);
            if(isLavaPool(super.worldObj, var18, var19, l, 6)) {
               this.setHomeArea(var18, var19, l, 2);
               this.func_152115_b("");
               break;
            }
         }
      }

   }

   private static boolean isLavaPool(World world, int x, int y, int z, int max) {
      if(isLavaPoolColumn(world, x, y, z) && isLavaPoolColumn(world, x + 1, y, z) && isLavaPoolColumn(world, x - 1, y, z) && isLavaPoolColumn(world, x, y, z + 1) && isLavaPoolColumn(world, x, y, z - 1)) {
         int max2 = max * max;

         for(int dx = x - max; dx <= x + max; ++dx) {
            for(int dz = z - max; dz <= z + max; ++dz) {
               double dist = Coord.distanceSq((double)x, (double)y, (double)z, (double)dx, (double)y, (double)dz);
               if(dist <= (double)max2 && world.getBlock(dx, y, dz) != Blocks.lava) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean isLavaPoolColumn(World world, int x, int y, int z) {
      if(world.getBlock(x, y, z) == Blocks.lava && world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z)) {
         byte depth = 4;

         for(int dy = y - depth; dy < dy; ++dy) {
            if(world.getBlock(x, y, z) != Blocks.lava) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void setOwner(EntityPlayer player) {
      this.setTamed(true);
      this.setPathToEntity((PathEntity)null);
      this.setAttackTarget((EntityLivingBase)null);
      this.setHealth(20.0F);
      this.func_152115_b(player.getUniqueID().toString());
      super.worldObj.setEntityState(this, (byte)7);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.setFollowerType(nbtRoot.getInteger("FollowerType"));
      if(nbtRoot.hasKey("TicksToLive")) {
         this.ticksToLive = nbtRoot.getInteger("TicksToLive");
      } else {
         this.ticksToLive = -1;
      }

      if(nbtRoot.hasKey("Skin")) {
         this.setSkin(nbtRoot.getString("Skin"));
      }

   }

   public void writeToNBT(NBTTagCompound nbtRoot) {
      super.writeToNBT(nbtRoot);
      nbtRoot.setInteger("FollowerType", this.getFollowerType());
      nbtRoot.setInteger("TicksToLive", this.ticksToLive);
      nbtRoot.setString("Skin", this.getSkin());
   }

   public static String generateFollowerName(int followerType) {
      Random ra = new Random();
      return followerType != 4?String.format("%s %s", new Object[]{FIRST_NAMES_F[ra.nextInt(FIRST_NAMES_F.length)], SURNAMES[ra.nextInt(SURNAMES.length)]}):String.format("%s %s", new Object[]{FIRST_NAMES_M[ra.nextInt(FIRST_NAMES_M.length)], SURNAMES[ra.nextInt(SURNAMES.length)]});
   }

   @SideOnly(Side.CLIENT)
   public ResourceLocation getLocationSkin() {
      if(this.locationSkin == null) {
         this.setupCustomSkin();
      }

      return this.locationSkin != null?this.locationSkin:AbstractClientPlayer.locationStevePng;
   }

   @SideOnly(Side.CLIENT)
   private void setupCustomSkin() {
      String owner = this.getSkin();
      if(owner != null && !owner.isEmpty()) {
         this.locationSkin = AbstractClientPlayer.getLocationSkin(owner);
         this.downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(this.locationSkin, owner);
      }

   }

   public void attractAttention() {
      if(!super.worldObj.isRemote) {
         String owner = this.getSkin();
         if(owner != null && !owner.isEmpty()) {
            EntityPlayer player = super.worldObj.getPlayerEntityByName(owner);
            if(player != null) {
               List list = super.worldObj.getEntitiesWithinAABB(EntityMob.class, super.boundingBox.expand(16.0D, 8.0D, 16.0D));
               Iterator i$ = list.iterator();

               while(i$.hasNext()) {
                  EntityMob mob = (EntityMob)i$.next();
                  if(mob.getAttackTarget() == player) {
                     mob.setRevengeTarget(this);
                     mob.setAttackTarget(this);
                     mob.setTarget(this);
                  }
               }
            }
         }
      }

   }

}
