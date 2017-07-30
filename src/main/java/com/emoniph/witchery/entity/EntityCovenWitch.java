package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.brewing.TileEntityCauldron;
import com.emoniph.witchery.entity.ai.EntityAIMoveToRestrictionAndSit;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.network.PacketSound;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.emoniph.witchery.util.TargetPointUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityCovenWitch extends EntityTameable implements IRangedAttackMob {

   private static final UUID field_110184_bp = UUID.fromString("DA2E2747-8768-4F9A-9135-258E93B077A4");
   private static final AttributeModifier field_110185_bq = (new AttributeModifier(field_110184_bp, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
   private static final Item[] witchDrops = new Item[]{Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick};
   private int witchAttackTimer;
   private String questOfferedTo = "";
   private boolean questAccepted = false;
   private int questType = 0;
   private int questItemsNeeded = 0;
   private int timeToLive = -1;
   public static final String COVEN_KEY = "WITCCoven";
   private static final int MAX_COVEN_SIZE = 6;
   private static final EntityCovenWitch.Quest[] QUESTS = new EntityCovenWitch.Quest[]{new EntityCovenWitch.FightPetQuest(Witchery.resource("witchery.witch.quest.fightspider"), ""), new EntityCovenWitch.FightZombiePetQuest(Witchery.resource("witchery.witch.quest.fightzombie"), ""), new EntityCovenWitch.FetchQuest(Witchery.resource("witchery.witch.quest.getdemonheart"), Witchery.resource("witchery.witch.quest.go"), Witchery.Items.GENERIC.itemDemonHeart.createStack()), new EntityCovenWitch.FetchQuest(Witchery.resource("witchery.witch.quest.makecrystalball"), Witchery.resource("witchery.witch.quest.go"), new ItemStack(Witchery.Blocks.CRYSTAL_BALL)), new EntityCovenWitch.FetchQuest(Witchery.resource("witchery.witch.quest.getbones"), Witchery.resource("witchery.witch.quest.go"), new ItemStack(Items.bone, 30)), new EntityCovenWitch.FetchQuest(Witchery.resource("witchery.witch.quest.makegrotesquebrew"), Witchery.resource("witchery.witch.quest.go"), Witchery.Items.GENERIC.itemBrewGrotesque.createStack(5)), new EntityCovenWitch.FetchQuest(Witchery.resource("witchery.witch.quest.makenecrostone"), Witchery.resource("witchery.witch.quest.go"), Witchery.Items.GENERIC.itemNecroStone.createStack())};
   private static final String[] FIRST_NAMES = new String[]{"Abigail", "Agatha", "Agony", "Alcina", "Alcyone", "Alexandra", "Alexandria", "Alvira", "Amanita", "Amaranth", "Amarantha", "Ambrosia", "Amelia", "Amethyst", "Anastasia", "Andromeda", "Angel", "Angela", "Angelica", "Angelique", "Anna", "Arachne", "Aradia", "Aria", "Arianna", "Ariadne", "Ariel", "Artemis", "Artemisia", "Astrea", "Astrid", "Astoria", "Autumn", "Aurora", "Beatrix", "Bella", "Belladonna", "Belle", "Bernadette", "Beryl", "Bianca", "Blanche", "Bliss", "Calliope", "Callypso", "Calpurnia", "Camilla", "Carlotta", "Carmilla", "Caroline", "Carrie", "Cassandra", "Cassiopeia", "Catherine", "Cathy", "Cecelia", "Celeste", "Celia", "Charlotte", "Christine", "Circe", "Clara", "Claudia", "Cleopatra", "Columbia", "Coraline", "Cordelia", "Cornelia", "Crystal", "Daphne", "Daria", "Darla", "Delia", "Delilah", "Della", "Demetria", "Demonica", "Desdemona", "Desire", "Dolores", "Dora", "Dove", "Drusilla", "Dusk", "Ebony", "Echo", "Eden", "Elanore", "Electra", "Eldora", "Elena", "Eliza", "Eloise", "Elphaba", "Elspeth", "Elsinore", "Elvira", "Ember", "Emilie", "Ephemera", "Eranthe", "Eris", "Esmerelda", "Estrella", "Esther", "Eterna", "Eternity", "Eudora", "Euphemia", "Eva", "Evalina", "Evangeline", "Eve", "Granny", "Gabriella", "Gabrielle", "Garnet", "Genevieve", "Godiva", "Hathor", "Hecate", "Hecuba", "Helena", "Hepzibah", "Hesperia", "Hippolita", "Ianthe", "Icie", "Icy", "Inez", "Infinity", "Ione", "Iris", "Isabeau", "Isabella", "Isabelle", "Isadora", "Isis", "Isolde", "Istar", "Ivy", "Izora", "Jane", "Jeanette", "Jinx", "Jocasta", "Juliet", "Katrina", "Lavinia", "Layla", "Leona", "Lenora", "Lenore", "Leona", "Libitina", "Ligeia", "Lilah", "Lilith", "Lillian", "Lily", "Lolita", "Lorraine", "Lucinda", "Lucretia", "Luna", "Lydia", "Lyra", "Madeline", "Magdalena", "Magenta", "Mara", "Marcella", "Margaret", "Marguerita", "Maria", "Marie", "Marissa", "Martha", "Matilda", "Medea", "Medusa", "Melanie", "Melantha", "Melanthe", "Melinda", "Mercedes", "Merula", "Mildred", "Mina", "Minerva", "Miranda", "Miriam", "Moira", "Mordea", "Morgan", "Morgana", "Morticia", "Nadia", "Nadine", "Nerezza", "Nora", "Nyx", "Obsidia", "Octavia", "Odessa", "Olivia", "Opal", "Ophelia", "Pandora", "Patience", "Pearl", "Penelope", "Perenelle", "Permelia", "Persephone", "Pixie", "Phoenix", "Poppy", "Priscilla", "Prudence", "Rachel", "Rain", "Raven", "Regina", "Rita", "Rosa", "Rose", "Rosemary", "Rowena", "Ruby", "Sabrina", "Salem", "Samantha", "Sangria", "Scarlet", "Selena", "Selene", "Sephora", "Seraphina", "Serena", "Serenity", "Shannon", "Silver", "Simone", "Sophia", "Sybella", "Sybil", "Sylvia", "Tabitha", "Tempest", "Theda", "Theresa", "Thora", "Threnody", "Trinity", "Twilight", "Umbra", "Vaitiare", "Valerie", "Vanessa", "Verna", "Verona", "Veronica", "Vesta", "Victoria", "Violet", "Whisper", "Willow", "Winter", "Xenobia", "Zillah", "Zinnia"};
   private static final String[] SURNAMES = new String[]{"Adams", "Addams", "Argent", "Ashwood", "Balfour", "Barker", "Batby", "Bathory", "Batsford", "Batson", "Batstone", "Batt", "Baudelaire", "Black", "Blackbird", "Blackburn", "Blackcat", "Blacklock", "Blackmoore", "Blackstone", "Blackthorn", "Blackwell", "Blackwing", "Blackwolf", "Blackwood", "Blair", "Blood", "Bloodgood", "Bloodhart", "Bloodmoore", "Bloodsaw", "Bloodstone", "Bloodsworth", "Bloodwine", "Bloodworth", "Boggart", "Boggarty", "Bonebrake", "Bonehart", "Bonehill", "Bonella", "Boneman", "Bones", "Bonesmith", "Bonewits", "Borden", "Broom", "Broomwood", "Burton", "Byron", "Cackler", "Cain", "Calamity", "Castle", "Castleton", "Cemetary", "Chill", "Chillingwood", "Cobweb", "Coffin", "Coffinberry", "Coffins", "Cold", "Coldbridge", "Coldeman", "Coldstone", "Coldwell", "Cole", "Collins", "Constantine", "Corbett", "Corbin", "Corpse", "Corpseley", "Creak", "Creakey", "Creep", "Creeper", "Creeps", "Crepsley", "Crimson", "Cross", "Crossway", "Crosswicks", "Crow", "Crowden", "Crowe", "Crowley", "Darcy", "Dark", "Darke", "Darken", "Darkenwald", "Darkes", "Darkmoore", "Darkwell", "Darkwood", "Deadman", "Deadmond", "Deadmore", "Deadrick", "Deadwood", "DeAngelus", "Dearborn", "Death", "Deathridge", "Deathrow", "December", "Delambre", "DeLioncourt", "Demond", "Demonde", "Demonte", "DeMort", "DeRavin", "Devall", "Devane", "DeVille", "DeWinter", "Dracul", "Drago", "Drake", "Dread", "Drear", "Dreary", "Drelincourt", "DuLac", "Dumaine", "Dunsany", "Eldritch", "Fang", "Fanger", "Fate", "Faust", "February", "Fear", "Fearfield", "Fears", "Frankenstein", "Frost", "Fury", "Gautier", "Ghoul", "Ghoulson", "Ghost", "Ghosten", "Ghostley", "Giger", "Goblin", "Goth", "Gotham", "Gothard", "Gothberg", "Gravedigger", "Gravemaker", "Graves", "Gravesen", "Gravesgard", "Grey", "Greyson", "Greystone", "Grimmauld", "Grimm", "Grimmer", "Grimmes", "Grimmins", "Grimsbro", "Grimsby", "Grimsman", "Grimwood", "Harker", "Hart", "Haunt", "Haunter", "Haunton", "Haunty", "Hawk", "Hawke", "Havelock", "Heart", "Heartstrom", "Hemlock", "Hex", "Hexem", "Hexter", "Hexwood", "Hollow", "Holmes", "Holmwood", "Hugo", "Hunter", "Hyde", "January", "Jekyll", "Kenrick", "Kilgore", "Killar", "Killewich", "Killings", "LaCroix", "Lapidus", "LaRue", "LeFay", "LeStrange", "LeStrange", "Locke", "London", "Loveless", "Lovelock", "Lovett", "Lycan", "MacBeth", "Mandrake", "Marrow", "Masters", "Mist", "Misteri", "Moan", "Moon", "Moones", "Moonie", "Moonly", "Monet", "Monster", "Monstery", "Montague", "Montresor", "Morgan", "Morgue", "Moriarty", "Murdoc", "Murray", "Morrow", "Mort", "Mortella", "Munster", "Mysterios", "Night", "Nightchase", "Nightengale", "Nightingdale", "Nightman", "Nightwalker", "Nightwine", "Nocton", "Nox", "October", "Odd", "Odder", "Oddman", "Oddson", "Owl", "Owley", "Owlford", "Owlsey", "Pale", "Pale", "Paine", "Pains", "Payne", "Plague", "Poe", "Poe", "Poe", "Pyre", "Pyre", "Pyre", "Radcliffe", "Rain", "Raven", "Ravencraft", "Ravendale", "Ravenhorst", "Ravensloft", "Ravenway", "Rayne", "Reaper", "Redbone", "Redcross", "Redd", "Redfern", "Redgrave", "Redmond", "Redwine", "Redwolf", "Renfield", "Riven", "Rookwood", "Roth", "Ripley", "Ripper", "Salvatore", "Scar", "Scare", "Scarebrook", "Scares", "Scarey", "Scarlati", "Setzer", "Seward", "Shade", "Shademoore", "Shadow", "Shadows", "Shadowton", "Shelley", "Skeleton", "Skelinen", "Skellington", "Skelton", "Skull", "Skullman", "Specter", "Spectre", "Spellman", "Spider", "Spinner", "Spirite", "Spook", "Spook", "Spook", "Song", "Snow", "St. Claire", "St. Germaine", "Steele", "Sterling", "Stoker", "Storm", "Storme", "Stormfelt", "Stormwind", "Stormyr", "Stone", "Stonewall", "Strange", "Strangeman", "Strangeway", "Striker", "Swan", "Swann", "Teeth", "Tombs", "Tombstone", "Towers", "Trick", "Valancourt", "Valdemar", "Valentine", "Valentino", "Vamper", "Vamplers", "Vampouille", "Vamprine", "Vampyr", "Van Allen", "Van Gogh", "Van Halen", "Van Helgen", "Van Helsing", "Voorhees", "Webb", "Weird", "Weird", "West", "Westenra", "White", "Whitebone", "Whitemoon", "Whitewing", "Widdowes", "Wild", "Wildblood", "Wilde", "Winchester", "Windgate", "Windholm", "Windward", "Wing", "Wingblade", "Wingfield", "Winter", "Winterford", "Winterrose", "Winterwood", "Winters", "Witche", "Witcher", "Witchery", "Witchey", "Witchman", "Wither", "Wolf", "Wolfen", "Wolfmann", "Wolfram", "Wolfstone", "Wolftooth"};


   public EntityCovenWitch(World world) {
      super(world);
      this.setSize(0.6F, 1.95F);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(1, super.aiSit);
      super.tasks.addTask(2, new EntityAIMoveToRestrictionAndSit(this, 0.6D));
      super.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
      super.tasks.addTask(4, new EntityAIMoveIndoors(this));
      super.tasks.addTask(5, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(6, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 0.6D));
      super.tasks.addTask(8, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(10, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      super.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
      this.setTameSkin(super.rand.nextInt(5));
      this.setTamed(false);
   }

   public boolean getCanSpawnHere() {
      boolean living = super.worldObj.checkNoEntityCollision(super.boundingBox) && super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox);
      int i = MathHelper.floor_double(super.posX);
      int j = MathHelper.floor_double(super.boundingBox.minY);
      int k = MathHelper.floor_double(super.posZ);
      boolean creature = living && this.getBlockPathWeight(i, j, k) >= 0.0F && (Config.instance().covenWitchSpawnWeight == 1 || super.worldObj.rand.nextInt(Config.instance().covenWitchSpawnWeight) == 0);
      return creature;
   }

   public void setCustomNameTag(String par1Str) {}

   private void setInnerCustomNameTag(String s) {
      super.dataWatcher.updateObject(10, s);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      this.setTameSkin(super.worldObj.rand.nextInt(5));
      return super.onSpawnWithEgg(par1EntityLivingData);
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(18, Byte.valueOf((byte)0));
      this.getDataWatcher().addObject(21, Byte.valueOf((byte)0));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("SkinType", this.getTameSkin());
      nbtRoot.setString("QuestOffered", this.questOfferedTo);
      nbtRoot.setBoolean("QuestAccepted", this.questAccepted);
      nbtRoot.setInteger("QuestType", this.questType);
      nbtRoot.setInteger("QuestItemsNeeded", this.questItemsNeeded);
      nbtRoot.setInteger("SuicideIn", this.timeToLive);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.setTameSkin(nbtRoot.getInteger("SkinType"));
      this.questOfferedTo = nbtRoot.getString("QuestOffered");
      this.questAccepted = nbtRoot.getBoolean("QuestAccepted");
      this.questType = nbtRoot.getInteger("QuestType");
      this.questItemsNeeded = nbtRoot.getInteger("QuestItemsNeeded");
      if(nbtRoot.hasKey("SuicideIn")) {
         this.timeToLive = nbtRoot.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

      if(nbtRoot.hasKey("CustomName") && nbtRoot.getString("CustomName").length() > 0) {
         this.setInnerCustomNameTag(nbtRoot.getString("CustomName"));
      }

   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.covenwitch.name");
   }

   public void setTimeToLive(int i) {
      this.timeToLive = i;
   }

   public void updateAITick() {
      super.updateAITick();
      if(super.worldObj != null && !super.isDead && !super.worldObj.isRemote && this.timeToLive != -1) {
         if(this.timeToLive > 0) {
            --this.timeToLive;
         }

         if(this.getAttackTarget() == null && this.timeToLive == 0) {
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 1.0D, 16);
            this.setDead();
         }
      }

   }

   public void onDeath(DamageSource damageSource) {
      if(!super.worldObj.isRemote && this.isTamed()) {
         EntityLivingBase owner = this.getOwner();
         if(owner != null && owner instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)owner;
            NBTTagCompound nbtPlayer = Infusion.getNBT(player);
            if(nbtPlayer.hasKey("WITCCoven")) {
               NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);

               for(int i = 0; i < nbtCovenList.tagCount(); ++i) {
                  NBTTagCompound nbtWitch = nbtCovenList.getCompoundTagAt(i);
                  if(nbtWitch.getString("WitchName").equalsIgnoreCase(this.getCustomNameTag())) {
                     nbtCovenList.removeTag(i);
                     break;
                  }
               }
            }
         }
      }

      super.onDeath(damageSource);
   }

   public int getTameSkin() {
      return super.dataWatcher.getWatchableObjectByte(18);
   }

   public void setTameSkin(int par1) {
      super.dataWatcher.updateObject(18, Byte.valueOf((byte)par1));
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "mob.witch.hurt";
   }

   protected String getDeathSound() {
      return "mob.witch.death";
   }

   public void setAggressive(boolean aggressive) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(21);
      if(aggressive) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 &= -2;
      }

      super.dataWatcher.updateObject(21, Byte.valueOf(b0));
   }

   public boolean getAggressive() {
      return (super.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
   }

   public void setQuestOffered(boolean aggressive) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(21);
      if(aggressive) {
         b0 = (byte)(b0 | 4);
      } else {
         b0 &= -5;
      }

      super.dataWatcher.updateObject(21, Byte.valueOf(b0));
   }

   public boolean isQuestOffered() {
      return (super.dataWatcher.getWatchableObjectByte(21) & 2) != 0;
   }

   public boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      if(!super.worldObj.isRemote) {
         if(this.getAggressive()) {
            if(this.witchAttackTimer-- <= 0) {
               this.setAggressive(false);
               ItemStack short1 = this.getHeldItem();
               this.setCurrentItemOrArmor(0, (ItemStack)null);
               if(short1 != null && short1.getItem() == Items.potionitem) {
                  List attributeinstance = Items.potionitem.getEffects(short1);
                  if(attributeinstance != null) {
                     Iterator iterator = attributeinstance.iterator();

                     while(iterator.hasNext()) {
                        PotionEffect potioneffect = (PotionEffect)iterator.next();
                        this.addPotionEffect(new PotionEffect(potioneffect));
                     }
                  }
               }

               this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
            }
         } else {
            short var5 = -1;
            if(super.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
               var5 = 16307;
            } else if(super.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
               var5 = 16341;
            } else if(super.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var5 = 16274;
            } else if(super.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var5 = 16274;
            }

            if(var5 > -1) {
               this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, var5));
               this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
               this.setAggressive(true);
               IAttributeInstance var6 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
               var6.removeModifier(field_110185_bq);
               var6.applyModifier(field_110185_bq);
            }
         }

         if(super.rand.nextFloat() < 7.5E-4F) {
            super.worldObj.setEntityState(this, (byte)15);
         }
      }

      super.onLivingUpdate();
   }

   protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
      par2 = super.applyPotionDamageCalculations(par1DamageSource, par2);
      if(par1DamageSource.getEntity() == this) {
         par2 = 0.0F;
      }

      if(par1DamageSource.isMagicDamage()) {
         par2 = (float)((double)par2 * 0.15D);
      }

      return par2;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 15) {
         for(int i = 0; i < super.rand.nextInt(35) + 10; ++i) {
            super.worldObj.spawnParticle("witchMagic", super.posX + super.rand.nextGaussian() * 0.12999999523162842D, super.boundingBox.maxY + 0.5D + super.rand.nextGaussian() * 0.12999999523162842D, super.posZ + super.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
         }
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public boolean interact(EntityPlayer player) {
      if(!super.worldObj.isRemote && player != null) {
         if(!this.isTamed() && !this.getAggressive() && player.dimension != Config.instance().dimensionDreamID) {
            if(this.questAccepted) {
               if(this.questOfferedTo.equalsIgnoreCase(player.getCommandSenderName())) {
                  if(this.isCovenFull(player)) {
                     this.questAccepted = false;
                     this.questType = 0;
                     this.questOfferedTo = "";
                     playWitchTalk(super.worldObj, this, 0.4F);
                     ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.covenfull")}));
                  } else if(isQuestItemForEntity(player.getHeldItem(), this)) {
                     --player.inventory.mainInventory[player.inventory.currentItem].stackSize;
                     if(player.inventory.mainInventory[player.inventory.currentItem].stackSize == 0) {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                     }

                     if(--this.questItemsNeeded == 0) {
                        if(this.addToPlayerCoven(player)) {
                           ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.joinedcoven")}));
                        } else {
                           ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.tricked")}));
                           this.setRevengeTarget(player);
                           this.setAttackTarget(player);
                           this.setTarget(player);
                           this.setAggressive(true);
                           this.questAccepted = false;
                           this.questType = 0;
                           this.questOfferedTo = "";
                        }
                     } else {
                        ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), String.format(Witchery.resource("witchery.witch.say.questitemsremaining"), new Object[]{Integer.valueOf(this.questItemsNeeded).toString()})}));
                     }

                     playWitchTalk(super.worldObj, this, 0.8F);
                  } else {
                     ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.questnotfinished")}));
                     playWitchTalk(super.worldObj, this, 0.4F);
                  }
               } else {
                  ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.begone")}));
                  playWitchTalk(super.worldObj, this, 0.4F);
               }
            } else if(!this.questOfferedTo.equalsIgnoreCase(player.getCommandSenderName())) {
               this.func_110163_bv();
               if(!this.hasCustomNameTag()) {
                  this.setInnerCustomNameTag(generateWitchName());
               }

               if(this.isCovenFull(player)) {
                  playWitchTalk(super.worldObj, this, 0.4F);
                  ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.covenfull")}));
               } else if(!Familiar.hasActiveFamiliar(player)) {
                  String s;
                  switch(super.worldObj.rand.nextInt(3)) {
                  case 0:
                  default:
                     s = Witchery.resource("witchery.witch.say.notinterested1");
                     break;
                  case 1:
                     s = Witchery.resource("witchery.witch.say.notinterested2");
                     break;
                  case 2:
                     s = Witchery.resource("witchery.witch.say.notinterested3");
                  }

                  playWitchTalk(super.worldObj, this, 0.4F);
                  ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), s}));
               } else {
                  this.questOfferedTo = player.getCommandSenderName();
                  this.questType = super.worldObj.rand.nextInt(QUESTS.length);
                  this.questItemsNeeded = QUESTS[this.questType].getItemsNeeded();
                  ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), QUESTS[this.questType].getDescriptionText()}));
                  playWitchTalk(super.worldObj, this, 0.4F);
               }
            } else if(this.isCovenFull(player)) {
               this.questOfferedTo = "";
               playWitchTalk(super.worldObj, this, 0.4F);
               ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), Witchery.resource("witchery.witch.say.covenfull")}));
            } else {
               this.questAccepted = true;
               QUESTS[this.questType].activate(super.worldObj, this, player);
               if(!QUESTS[this.questType].getStartText().isEmpty()) {
                  ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, String.format("<%s> %s", new Object[]{this.getCustomNameTag(), QUESTS[this.questType].getStartText()}));
               }

               playWitchTalk(super.worldObj, this, 1.0F);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isCovenFull(EntityPlayer player) {
      return getCovenSize(player) >= 6;
   }

   public static int getCovenSize(EntityPlayer player) {
      if(player == null) {
         return 0;
      } else {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(!nbtPlayer.hasKey("WITCCoven")) {
            return 0;
         } else {
            NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);
            return nbtCovenList.tagCount();
         }
      }
   }

   protected boolean canDespawn() {
      if(this.isTamed()) {
         EntityLivingBase player = this.getOwner();
         return player == null?true:(player.getDistanceSqToEntity(this) > 4096.0D?true:super.canDespawn());
      } else {
         return super.canDespawn();
      }
   }

   private boolean addToPlayerCoven(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(!nbtPlayer.hasKey("WITCCoven")) {
         nbtPlayer.setTag("WITCCoven", new NBTTagList());
      }

      NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);
      if(this.isWitchInList(nbtCovenList)) {
         return false;
      } else {
         this.setTamed(true);
         TameableUtil.setOwner(this, player);
         NBTTagCompound nbtWitch = new NBTTagCompound();
         this.writeToPlayerNBT(nbtWitch);
         nbtCovenList.appendTag(nbtWitch);
         ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
         this.setDead();
         return true;
      }
   }

   private boolean isWitchInList(NBTTagList nbtCovenList) {
      for(int i = 0; i < nbtCovenList.tagCount(); ++i) {
         NBTTagCompound nbtWitch = nbtCovenList.getCompoundTagAt(i);
         if(nbtWitch != null) {
            String name = nbtWitch.getString("WitchName");
            if(name != null && name.equalsIgnoreCase(this.getCustomNameTag())) {
               return true;
            }
         }
      }

      return false;
   }

   private void writeToPlayerNBT(NBTTagCompound nbtWitch) {
      nbtWitch.setString("WitchName", this.getCustomNameTag());
      nbtWitch.setInteger("Skin", this.getTameSkin());
      nbtWitch.setInteger("Quest", this.questType);
   }

   private void readFromPlayerNBT(NBTTagCompound nbtWitch) {
      this.setTameSkin(nbtWitch.getInteger("Skin"));
      this.questType = nbtWitch.getInteger("Quest");
      this.setInnerCustomNameTag(nbtWitch.getString("WitchName"));
   }

   public static void summonCoven(ArrayList ritualSteps, World world, EntityPlayer player, int[][] pos) {
      double RADIUS_XZ = 64.0D;
      double RADIUS_Y = 16.0D;
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 64.0D, player.posY - 16.0D, player.posZ - 64.0D, player.posX + 64.0D, player.posY + 16.0D, player.posZ + 64.0D);
      List entities = world.getEntitiesWithinAABB(EntityCovenWitch.class, bounds);

      for(int nbtPlayer = 0; nbtPlayer < entities.size(); ++nbtPlayer) {
         EntityCovenWitch nbtCovenList = (EntityCovenWitch)entities.get(nbtPlayer);
         if(nbtCovenList.isTamed() && nbtCovenList.getOwner() == player) {
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, nbtCovenList, 1.0D, 2.0D, 16);
            nbtCovenList.setDead();
         }
      }

      NBTTagCompound var14 = Infusion.getNBT(player);
      if(var14.hasKey("WITCCoven")) {
         NBTTagList var13 = var14.getTagList("WITCCoven", 10);

         for(int index = 0; index < var13.tagCount() && index < pos.length; ++index) {
            ritualSteps.add(new EntityCovenWitch.StepSummonCovenMemeber(player, index, pos[index]));
         }
      }

   }

   public static void summonCoven(World world, EntityPlayer player, Coord target, int ticks) {
      if(ticks % 20 == 0 && ticks / 20 > 0) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer.hasKey("WITCCoven")) {
            double RADIUS_XZ = 64.0D;
            double RADIUS_Y = 16.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 64.0D, player.posY - 16.0D, player.posZ - 64.0D, player.posX + 64.0D, player.posY + 16.0D, player.posZ + 64.0D);
            List entities = world.getEntitiesWithinAABB(EntityCovenWitch.class, bounds);
            NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);
            int witchIndex = ticks / 20 - 1;
            if(witchIndex > nbtCovenList.tagCount()) {
               return;
            }

            NBTTagCompound nbtWitch = nbtCovenList.getCompoundTagAt(witchIndex);
            EntityCovenWitch witch = null;

            for(int spawn = 0; spawn < entities.size(); ++spawn) {
               EntityCovenWitch closest = (EntityCovenWitch)entities.get(spawn);
               if(closest.isTamed() && closest.getOwner() == player && closest.getCustomNameTag().equalsIgnoreCase(nbtWitch.getString("WitchName"))) {
                  witch = closest;
                  break;
               }
            }

            boolean var31 = false;
            if(witch == null) {
               witch = new EntityCovenWitch(world);
               witch.readFromPlayerNBT(nbtWitch);
               witch.setTamed(true);
               TameableUtil.setOwner(witch, player);
               var31 = true;
            }

            TileEntity var30 = null;
            double bestDistSq = 0.0D;
            Iterator cauldronRange = world.loadedTileEntityList.iterator();

            while(cauldronRange.hasNext()) {
               Object obj = cauldronRange.next();
               TileEntity cauldronRangeSq = (TileEntity)obj;
               if(cauldronRangeSq instanceof TileEntityCauldron) {
                  double dist = player.getDistanceSq((double)cauldronRangeSq.xCoord, (double)cauldronRangeSq.yCoord, (double)cauldronRangeSq.zCoord);
                  if(var30 == null || dist < bestDistSq) {
                     var30 = cauldronRangeSq;
                     bestDistSq = dist;
                  }
               }
            }

            double var32 = 9.0D;
            double var33 = 81.0D;
            if(var30 != null && bestDistSq <= 81.0D) {
               witch.setHomeArea(var30.xCoord, var30.yCoord, var30.zCoord, 3);
               byte maxRange = 3;
               byte minRange = 1;
               int activeRadius = maxRange - minRange;
               int ax = world.rand.nextInt(activeRadius * 2 + 1);
               if(ax > activeRadius) {
                  ax += minRange * 2;
               }

               int nx = var30.xCoord - maxRange + ax;
               int az = world.rand.nextInt(activeRadius * 2 + 1);
               if(az > activeRadius) {
                  az += minRange * 2;
               }

               int nz = var30.zCoord - maxRange + az;
               witch.setPositionAndRotation((double)nx, 0.01D + (double)var30.yCoord, (double)nz, 0.0F, 0.0F);
            } else {
               witch.setPositionAndRotation(0.5D + (double)target.x + (double)world.rand.nextInt(3) - 1.5D, 0.01D + (double)target.y, 0.5D + (double)target.z + (double)world.rand.nextInt(3) - 1.5D, 0.0F, 0.0F);
            }

            witch.aiSit.resetTask();
            witch.setTimeToLive(1200);
            if(var31) {
               world.spawnEntityInWorld(witch);
            }

            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, witch, 1.0D, 2.0D, 16);
         }
      }

   }

   public static void summonCovenMember(World world, EntityPlayer player, int ttlSecs) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer.hasKey("WITCCoven")) {
         NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);
         if(nbtCovenList.tagCount() > 0) {
            double RADIUS_XZ = 64.0D;
            double RADIUS_Y = 16.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 64.0D, player.posY - 16.0D, player.posZ - 64.0D, player.posX + 64.0D, player.posY + 16.0D, player.posZ + 64.0D);
            List entities = world.getEntitiesWithinAABB(EntityCovenWitch.class, bounds);
            Collections.shuffle(entities);

            EntityCovenWitch witch;
            int i;
            int j;
            int k;
            int i1;
            int l;
            for(int nbtWitch = 0; nbtWitch < entities.size(); ++nbtWitch) {
               witch = (EntityCovenWitch)entities.get(nbtWitch);
               if(witch.isTamed() && witch.getOwner() == player) {
                  i = MathHelper.floor_double(player.posX) - 2;
                  j = MathHelper.floor_double(player.posZ) - 2;
                  k = MathHelper.floor_double(player.boundingBox.minY);

                  for(l = 0; l <= 4; ++l) {
                     for(i1 = 0; i1 <= 4; ++i1) {
                        if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && world.getBlock(i + l, k - 1, j + i1).isSideSolid(world, i + l, k - 1, j + i1, ForgeDirection.UP) && !world.getBlock(i + l, k, j + i1).isNormalCube() && !world.getBlock(i + l, k + 1, j + i1).isNormalCube()) {
                           ItemGeneral var10000 = Witchery.Items.GENERIC;
                           ItemGeneral.teleportToLocation(world, (double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), player.dimension, witch, true);
                           witch.getNavigator().clearPathEntity();
                           return;
                        }
                     }
                  }
               }
            }

            NBTTagCompound var18 = nbtCovenList.getCompoundTagAt(world.rand.nextInt(nbtCovenList.tagCount()));
            witch = new EntityCovenWitch(world);
            i = MathHelper.floor_double(player.posX) - 2;
            j = MathHelper.floor_double(player.posZ) - 2;
            k = MathHelper.floor_double(player.boundingBox.minY);

            for(l = 0; l <= 4; ++l) {
               for(i1 = 0; i1 <= 4; ++i1) {
                  if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && world.getBlock(i + l, k - 1, j + i1).isSideSolid(world, i + l, k - 1, j + i1, ForgeDirection.UP) && !world.getBlock(i + l, k, j + i1).isNormalCube() && !world.getBlock(i + l, k + 1, j + i1).isNormalCube()) {
                     witch.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), 0.0F, 0.0F);
                     break;
                  }
               }
            }

            witch.setTamed(true);
            TameableUtil.setOwner(witch, player);
            witch.readFromPlayerNBT(var18);
            witch.setTimeToLive(ttlSecs * 20);
            world.spawnEntityInWorld(witch);
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, witch, 1.0D, 2.0D, 16);
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }
      } else {
         SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
      }

   }

   private static void playWitchTalk(World world, Entity where, float volume) {
      Witchery.packetPipeline.sendToAllAround(new PacketSound(world.rand.nextBoolean()?SoundEffect.WITCHERY_MOB_BABA_DEATH:SoundEffect.WITCHERY_MOB_BABA_LIVING, where, 1.0F, 1.0F), TargetPointUtil.from(where, 8.0D));
   }

   private static boolean isQuestItemForEntity(ItemStack stack, EntityCovenWitch questGiver) {
      if(questGiver != null && stack != null) {
         if(QUESTS[questGiver.questType].isQuestItem(stack)) {
            return true;
         }

         if(stack.hasTagCompound()) {
            NBTTagCompound nbtRoot = stack.getTagCompound();
            if(nbtRoot.hasKey("WITCQuestOwnerIDLeast") && nbtRoot.hasKey("WITCQuestOwnerIDMost")) {
               UUID questGiverID = new UUID(nbtRoot.getLong("WITCQuestOwnerIDMost"), nbtRoot.getLong("WITCQuestOwnerIDLeast"));
               return questGiverID.equals(questGiver.getPersistentID());
            }
         }
      }

      return false;
   }

   protected void dropFewItems(boolean par1, int par2) {
      int j = super.rand.nextInt(3) + 1;

      for(int k = 0; k < j; ++k) {
         int l = super.rand.nextInt(3);
         Item i1 = witchDrops[super.rand.nextInt(witchDrops.length)];
         if(par2 > 0) {
            l += super.rand.nextInt(par2 + 1);
         }

         for(int j1 = 0; j1 < l; ++j1) {
            this.dropItem(i1, 1);
         }
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2) {
      if(!this.getAggressive()) {
         EntityPotion entitypotion = new EntityPotion(super.worldObj, this, 32732);
         entitypotion.rotationPitch -= -20.0F;
         double d0 = par1EntityLivingBase.posX + par1EntityLivingBase.motionX - super.posX;
         double d1 = par1EntityLivingBase.posY + (double)par1EntityLivingBase.getEyeHeight() - 1.100000023841858D - super.posY;
         double d2 = par1EntityLivingBase.posZ + par1EntityLivingBase.motionZ - super.posZ;
         float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
         if(f1 >= 8.0F && !par1EntityLivingBase.isPotionActive(Potion.moveSlowdown)) {
            entitypotion.setPotionDamage(32698);
         } else if(par1EntityLivingBase.getHealth() >= 8.0F && !par1EntityLivingBase.isPotionActive(Potion.poison)) {
            entitypotion.setPotionDamage(32660);
         } else if(f1 <= 3.0F && !par1EntityLivingBase.isPotionActive(Potion.weakness) && super.rand.nextFloat() < 0.25F) {
            entitypotion.setPotionDamage(32696);
         }

         entitypotion.setThrowableHeading(d0, d1 + (double)(f1 * 0.2F), d2, 0.75F, 8.0F);
         super.worldObj.spawnEntityInWorld(entitypotion);
      }

   }

   public EntityAgeable createChild(EntityAgeable entityageable) {
      return null;
   }

   public static String generateWitchName() {
      Random ra = new Random();
      return String.format("%s %s", new Object[]{FIRST_NAMES[ra.nextInt(FIRST_NAMES.length)], SURNAMES[ra.nextInt(SURNAMES.length)]});
   }

   public void standStill() {
      super.aiSit.setSitting(true);
   }

   // $FF: synthetic method
   static EntityAISit access$100(EntityCovenWitch x0) {
      return x0.aiSit;
   }


   private static class FightPetQuest extends EntityCovenWitch.Quest {

      public FightPetQuest(String descriptionText, String startText) {
         super(descriptionText, startText, 1);
      }

      public void activate(World world, EntityCovenWitch witch, EntityPlayer player) {
         EntitySpider spider = new EntitySpider(world);
         spider.setLocationAndAngles(witch.posX, witch.posY, witch.posZ, witch.rotationPitch, witch.rotationYaw);
         world.spawnEntityInWorld(spider);
         spider.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
         spider.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
         spider.setHealth((float)spider.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue());
         spider.setAttackTarget(player);
         spider.setTarget(player);
         spider.setRevengeTarget(player);
         spider.setCustomNameTag(String.format(Witchery.resource("witchery.witch.pet"), new Object[]{witch.getCustomNameTag()}));
         ItemStack stack = new ItemStack(Items.spider_eye);
         stack.setStackDisplayName(String.format(Witchery.resource("witchery.witch.peteye"), new Object[]{witch.getCustomNameTag()}));
         if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtRoot = stack.getTagCompound();
         nbtRoot.setLong("WITCQuestOwnerIDLeast", witch.getUniqueID().getLeastSignificantBits());
         nbtRoot.setLong("WITCQuestOwnerIDMost", witch.getUniqueID().getMostSignificantBits());
         NBTTagCompound nbtExtraDrop = new NBTTagCompound();
         stack.writeToNBT(nbtExtraDrop);
         NBTTagCompound nbtSpider = spider.getEntityData();
         if(!nbtSpider.hasKey("WITCExtraDrops")) {
            nbtSpider.setTag("WITCExtraDrops", new NBTTagList());
         }

         NBTTagList nbtExtraDrops = nbtSpider.getTagList("WITCExtraDrops", 10);
         nbtExtraDrops.appendTag(nbtExtraDrop);
         ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, spider, 2.0D, 2.0D, 16);
      }
   }

   private static class FightZombiePetQuest extends EntityCovenWitch.Quest {

      public FightZombiePetQuest(String descriptionText, String startText) {
         super(descriptionText, startText, 1);
      }

      public void activate(World world, EntityCovenWitch witch, EntityPlayer player) {
         EntityZombie spider = new EntityZombie(world);
         spider.setLocationAndAngles(witch.posX, witch.posY, witch.posZ, witch.rotationPitch, witch.rotationYaw);
         world.spawnEntityInWorld(spider);
         spider.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
         spider.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
         spider.setCurrentItemOrArmor(4, new ItemStack(Items.skull));
         spider.setHealth((float)spider.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue());
         spider.setAttackTarget(player);
         spider.setTarget(player);
         spider.setRevengeTarget(player);
         spider.setCustomNameTag(String.format(Witchery.resource("witchery.witch.pet"), new Object[]{witch.getCustomNameTag()}));
         ItemStack stack = new ItemStack(Items.rotten_flesh);
         stack.setStackDisplayName(String.format(Witchery.resource("witchery.witch.petflesh"), new Object[]{witch.getCustomNameTag()}));
         if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtRoot = stack.getTagCompound();
         nbtRoot.setLong("WITCQuestOwnerIDLeast", witch.getUniqueID().getLeastSignificantBits());
         nbtRoot.setLong("WITCQuestOwnerIDMost", witch.getUniqueID().getMostSignificantBits());
         NBTTagCompound nbtExtraDrop = new NBTTagCompound();
         stack.writeToNBT(nbtExtraDrop);
         NBTTagCompound nbtSpider = spider.getEntityData();
         if(!nbtSpider.hasKey("WITCExtraDrops")) {
            nbtSpider.setTag("WITCExtraDrops", new NBTTagList());
         }

         NBTTagList nbtExtraDrops = nbtSpider.getTagList("WITCExtraDrops", 10);
         nbtExtraDrops.appendTag(nbtExtraDrop);
         ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, spider, 2.0D, 2.0D, 16);
      }
   }

   private abstract static class Quest {

      private final String questDescriptionText;
      private final String questStartText;
      private final int itemsNeeded;


      public Quest(String descriptionText, String startText, int itemsNeeded) {
         this.questStartText = startText;
         this.questDescriptionText = descriptionText;
         this.itemsNeeded = itemsNeeded;
      }

      public int getItemsNeeded() {
         return this.itemsNeeded;
      }

      public String getDescriptionText() {
         return this.questDescriptionText;
      }

      public String getStartText() {
         return this.questStartText;
      }

      public abstract void activate(World var1, EntityCovenWitch var2, EntityPlayer var3);

      public boolean isQuestItem(ItemStack stack) {
         return false;
      }
   }

   private static class FetchQuest extends EntityCovenWitch.Quest {

      final ItemStack stack;


      public FetchQuest(String descriptionText, String startText, ItemStack stack) {
         super(descriptionText, startText, stack.stackSize);
         this.stack = stack;
      }

      public void activate(World world, EntityCovenWitch witch, EntityPlayer player) {}

      public boolean isQuestItem(ItemStack stack) {
         return this.stack.isItemEqual(stack);
      }
   }

   private static class StepSummonCovenMemeber extends RitualStep {

      private final int index;
      private final int[] position;


      public StepSummonCovenMemeber(EntityPlayer player, int index, int[] position) {
         super(false);
         this.index = index;
         this.position = position;
      }

      public RitualStep.Result process(World world, int xCoord, int yCoord, int zCoord, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            EntityPlayer player = ritual.getInitiatingPlayer(world);
            if(player != null) {
               NBTTagCompound nbtPlayer = Infusion.getNBT(player);
               if(nbtPlayer.hasKey("WITCCoven")) {
                  double RADIUS_XZ = 64.0D;
                  double RADIUS_Y = 16.0D;
                  AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 64.0D, player.posY - 16.0D, player.posZ - 64.0D, player.posX + 64.0D, player.posY + 16.0D, player.posZ + 64.0D);
                  List entities = world.getEntitiesWithinAABB(EntityCovenWitch.class, bounds);
                  NBTTagList nbtCovenList = nbtPlayer.getTagList("WITCCoven", 10);
                  NBTTagCompound nbtWitch = nbtCovenList.getCompoundTagAt(this.index);
                  EntityCovenWitch witch = null;

                  for(int spawn = 0; spawn < entities.size(); ++spawn) {
                     EntityCovenWitch FACING = (EntityCovenWitch)entities.get(spawn);
                     if(FACING.isTamed() && FACING.getOwner() == player && FACING.getCustomNameTag().equalsIgnoreCase(nbtWitch.getString("WitchName"))) {
                        witch = FACING;
                        break;
                     }
                  }

                  boolean var22 = false;
                  if(witch == null) {
                     witch = new EntityCovenWitch(world);
                     witch.readFromPlayerNBT(nbtWitch);
                     witch.setTamed(true);
                     TameableUtil.setOwner(witch, player);
                     var22 = true;
                  }

                  float[] var21 = new float[]{-45.0F, 45.0F, -135.0F, 135.0F, 180.0F, 0.0F};
                  witch.setPositionAndRotation(0.5D + (double)this.position[0], 0.01D + (double)this.position[1], 0.5D + (double)this.position[2], var21[this.index], 0.0F);
                  witch.rotationYawHead = witch.rotationYaw;
                  witch.renderYawOffset = witch.rotationYaw;
                  witch.prevRotationYaw = witch.rotationYaw;
                  EntityCovenWitch.access$100(witch).setSitting(true);
                  witch.setTimeToLive(300);
                  if(var22) {
                     world.spawnEntityInWorld(witch);
                  }

                  ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, witch, 1.0D, 2.0D, 16);
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
