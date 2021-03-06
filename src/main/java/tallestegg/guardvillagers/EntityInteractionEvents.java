package tallestegg.guardvillagers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tallestegg.guardvillagers.entities.GuardEntity;

public class EntityInteractionEvents 
{
	 @SubscribeEvent
	 public void onEntityInteract(PlayerInteractEvent.EntityInteract event) 
	 {
	     ItemStack itemstack = event.getItemStack();
	     if (itemstack.getItem() instanceof SwordItem && event.getPlayer().isSneaking() || itemstack.getItem() instanceof CrossbowItem && event.getPlayer().isSneaking()) 
	     {
	       Entity target = event.getTarget();
	       if (target instanceof VillagerEntity)
	       {
	    	   VillagerEntity villager = (VillagerEntity) event.getTarget();
		         if (!villager.isChild() && villager.getVillagerData().getProfession() == VillagerProfession.NONE || villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) 
		         {
		          this.VillagerConvert(villager, event.getPlayer());
		          if (!event.getPlayer().abilities.isCreativeMode)
		          itemstack.shrink(1);
		         }
	           } 
	       }
           if (itemstack.getItem() == Items.IRON_INGOT) 
	       {
        	 Entity target = event.getTarget();
	    	 if (target instanceof IronGolemEntity) {
	    		IronGolemEntity golem = (IronGolemEntity)event.getTarget();
	    		if (golem.getHealth() < golem.getMaxHealth()) {
	    		  golem.heal(25.0F);
	    		}
	    	 }
	     } 
      }
  
	    private void VillagerConvert(LivingEntity entity, PlayerEntity player)
	    {
	        ItemStack itemstack = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
	        GuardEntity guard = GuardEntityType.GUARD.create(entity.world);
	        VillagerEntity villager = (VillagerEntity)entity;
	        guard.copyLocationAndAnglesFrom(villager);
	        guard.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
	        int i = GuardEntity.getRandomTypeForBiome(guard.world, guard.getPosition());
	        guard.setGuardVariant(i);
	        guard.enablePersistence();
	        if (villager.hasCustomName())
	        {
	            guard.setCustomName(villager.getCustomName());
	            guard.setCustomNameVisible(villager.isCustomNameVisible());
	        }
	        villager.world.addEntity(guard);
	        villager.func_213742_a(MemoryModuleType.HOME);
	        villager.func_213742_a(MemoryModuleType.JOB_SITE);
	        villager.func_213742_a(MemoryModuleType.MEETING_POINT);
	        villager.remove();
	    }
}
