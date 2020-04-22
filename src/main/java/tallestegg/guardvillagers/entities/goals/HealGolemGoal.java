package tallestegg.guardvillagers.entities.goals;

import java.util.List;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

public class HealGolemGoal extends Goal 
{
	public final MobEntity healer;
	public IronGolemEntity golem;	
	
	public HealGolemGoal(MobEntity mob)
	{
		healer = mob;
	}

	@Override
	public boolean shouldExecute() 
	{
		if (((VillagerEntity)this.healer).getVillagerData().getProfession() != VillagerProfession.WEAPONSMITH)
		{
		  return false;
		}
		 List<IronGolemEntity> list = this.healer.world.getEntitiesWithinAABB(IronGolemEntity.class, this.healer.getBoundingBox().grow((double)10.0D));
	      if (!list.isEmpty()) {
	         for(IronGolemEntity golem : list) {
	            if (!golem.isInvisible()) {
	               this.golem = golem;
	               if (golem.getHealth() < golem.getMaxHealth()) {
	                 this.healGolem();
	               }
	               return true;
	            }
	         }
	      }
		return false;
	}
	
	@Override
	public void resetTask()
	{
	  if (golem == null);
	  if (golem.getHealth() >= golem.getMaxHealth());
	  super.resetTask();
	}
	
	@Override
	public void tick()
	{
		if (golem.getHealth() < golem.getMaxHealth())
		{
			this.healGolem();
		}
	}
	
	public void healGolem()
	{
		healer.getNavigator().tryMoveToEntityLiving(golem, 0.5);
		if (healer.getDistance(golem) <= 2.0D)
		{
			healer.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_INGOT));
			healer.swingArm(Hand.MAIN_HAND);
			golem.heal(15.0F);
			golem.playSound(SoundEvents.field_226143_fP_, 1.0F, 1.0F);
		}
	}

}