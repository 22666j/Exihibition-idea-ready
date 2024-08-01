package exhibition.event.impl;

import exhibition.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
   private Entity entity;
   private boolean preAttack;

   public void fire(Entity targetEntity, boolean preAttack) {
      this.entity = targetEntity;
      this.preAttack = preAttack;
      super.fire();
   }

   public Entity getEntity() {
      return this.entity;
   }

   public boolean isPreAttack() {
      return this.preAttack;
   }

   public boolean isPostAttack() {
      return !this.preAttack;
   }
}
