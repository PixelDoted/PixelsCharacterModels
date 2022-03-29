package me.PixelDots.PixelsCharacterModels.server;

public class ServerEventHook {

    /*public static final ResourceLocation SKIN_LOCATION = new ResourceLocation(Reference.MOD_ID, "skin");
    public static final ResourceLocation ELYTRA_LOCATION = new ResourceLocation(Reference.MOD_ID, "elytra");
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(Reference.MOD_ID, "model");
    
    //LimbData:[Head:{Scale:1,X:0,Y:0,Z:0},Body:{Scale:1,X:0,Y:0,Z:0}],
    //GlobalData:[eyeHeight:1,Scale:1]
    
    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
        	ISkinPath skipat = (ISkinPath) event.getEntity().getCapability(SkinPathProvider.SKIN_LOC, null);
            ((ISkinPath) event.getPlayer().getCapability(SkinPathProvider.SKIN_LOC, null)).setSkin(
                   skipat.getSkin());
            
            IModelPath modpat = (IModelPath) event.getEntity().getCapability(ModelPathProvider.MODEL_LOC, null);
            ((IModelPath) event.getPlayer().getCapability(ModelPathProvider.MODEL_LOC, null)).setModel(
            		modpat.getModel());
            
            IElytraPath elypat = (IElytraPath) event.getEntity().getCapability(ElytraPathProvider.ELYTRA_LOC, null);
            ((IElytraPath) event.getPlayer().getCapability(ElytraPathProvider.ELYTRA_LOC, null)).setElytra(
            		elypat.getElytra());
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(SKIN_LOCATION, new SkinPathProvider());
            event.addCapability(MODEL_LOCATION, new ModelPathProvider());
            event.addCapability(ELYTRA_LOCATION, new ElytraPathProvider());
        }
    }

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
        	
        	ISkinPath skipat = (ISkinPath) event.getEntity().getCapability(SkinPathProvider.SKIN_LOC, null);
        	GlobalModelManager.Skin.setSkin((PlayerEntity) event.getEntity(),
                    skipat.getSkin());
        	
        	IModelPath modpat = (IModelPath) event.getEntity().getCapability(ModelPathProvider.MODEL_LOC, null);
        	GlobalModelManager.Model.setModel((PlayerEntity) event.getEntity(),
        					modpat.getModel());
        	
        	IElytraPath elypat = (IElytraPath) event.getEntity().getCapability(ElytraPathProvider.ELYTRA_LOC, null);
        	GlobalModelManager.Elytra.setElytra((PlayerEntity) event.getEntity(), 
        			elypat.getElytra());
        	
            if(event.getEntity() instanceof PlayerEntity) {
            	//GlobalModelManager.Skin.sendAllToPlayer((ServerPlayerEntity) event.getEntity(), true);
            	GlobalModelManager.Model.sendAllToPlayer((ServerPlayerEntity) event.getEntity(), true);
            	//GlobalModelManager.Elytra.sendAllToPlayer((ServerPlayerEntity) event.getEntity(), true);
            }
        }
    }

    @SubscribeEvent
    public void playerLeave(PlayerLoggedOutEvent event) {
        //GlobalModelManager.Skin.playerLoggedOut(event.getPlayer().getUniqueID());
        GlobalModelManager.Model.playerLoggedOut(event.getPlayer().getUniqueID());
        //GlobalModelManager.Elytra.playerLoggedOut(event.getPlayer().getUniqueID());
    }*/

}