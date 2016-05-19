package com.flansmod.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.flansmod.client.FlansModResourceHandler;
import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveablePart;
import com.flansmod.common.driveables.DriveablePosition;
import com.flansmod.common.driveables.DriveableType;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EnumDriveablePart;
import com.flansmod.common.driveables.ItemVehicle;
import com.flansmod.common.driveables.VehicleType;
import com.flansmod.common.guns.Paintjob;

public class RenderVehicle extends Render implements IItemRenderer
{
	
	private Minecraft mc;
  public EntitySeat seatP;              
  public EntityVehicle driveP;
  public EntitySeat seatT;              
  public EntityVehicle driveT;
	
	public RenderVehicle(RenderManager renderManager) 
	{
		super(renderManager);
		shadowSize = 0.5F;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
    public void render(EntityVehicle vehicle, double d, double d1, double d2, float f, float f1)
    {
    	bindEntityTexture(vehicle);
    	VehicleType type = vehicle.getVehicleType();
        GL11.glPushMatrix();
        {
	        GL11.glTranslatef((float)d, (float)d1, (float)d2);
	        float dYaw = (vehicle.axes.getYaw() - vehicle.prevRotationYaw);
	        for(; dYaw > 180F; dYaw -= 360F) {}
	        for(; dYaw <= -180F; dYaw += 360F) {}
	        float dPitch = (vehicle.axes.getPitch() - vehicle.prevRotationPitch);
	        for(; dPitch > 180F; dPitch -= 360F) {}
	        for(; dPitch <= -180F; dPitch += 360F) {}
	        float dRoll = (vehicle.axes.getRoll() - vehicle.prevRotationRoll);
	        for(; dRoll > 180F; dRoll -= 360F) {}
	        for(; dRoll <= -180F; dRoll += 360F) {}
	        GL11.glRotatef(180F - vehicle.prevRotationYaw - dYaw * f1, 0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(vehicle.prevRotationPitch + dPitch * f1, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(vehicle.prevRotationRoll + dRoll * f1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
	
			float modelScale = type.modelScale;
			GL11.glPushMatrix();
			{
				GL11.glScalef(modelScale, modelScale, modelScale);
				ModelVehicle modVehicle = (ModelVehicle)type.model;
				if(modVehicle != null)
					modVehicle.render(vehicle, f1);
				
				GL11.glPushMatrix();
				if(type.turretOrigin != null && vehicle.isPartIntact(EnumDriveablePart.turret) && vehicle.seats != null && vehicle.seats[0] != null)
				{
					dYaw = (vehicle.seats[0].looking.getYaw() - vehicle.seats[0].prevLooking.getYaw());
					for(; dYaw > 180F; dYaw -= 360F) {}
					for(; dYaw <= -180F; dYaw += 360F) {}
					float yaw = vehicle.seats[0].prevLooking.getYaw() + dYaw * f1;

					GL11.glTranslatef(type.turretOrigin.x, type.turretOrigin.y, type.turretOrigin.z);
					GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(-type.turretOrigin.x, -type.turretOrigin.y, -type.turretOrigin.z);
					
					if(modVehicle != null)
						modVehicle.renderTurret(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, vehicle, f1);
					
					if(FlansMod.DEBUG)
					{					
						GL11.glTranslatef(type.turretOrigin.x, type.turretOrigin.y, type.turretOrigin.z);
						GL11.glRotatef(-vehicle.seats[0].looking.getPitch(), 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(-type.turretOrigin.x, -type.turretOrigin.y, -type.turretOrigin.z);
						
						//Render shoot points
						GL11.glColor4f(0F, 0F, 1F, 0.3F);
						for(DriveablePosition point : type.shootPointsPrimary)			
							if(point.part == EnumDriveablePart.turret)
								renderOffsetAABB(new AxisAlignedBB(point.position.x - 0.25F, point.position.y - 0.25F, point.position.z - 0.25F, point.position.x + 0.25F, point.position.y + 0.25F, point.position.z + 0.25F), 0, 0, 0);
						
						GL11.glColor4f(0F, 1F, 0F, 0.3F);
						for(DriveablePosition point : type.shootPointsSecondary)	
							if(point.part == EnumDriveablePart.turret)
								renderOffsetAABB(new AxisAlignedBB(point.position.x - 0.25F, point.position.y - 0.25F, point.position.z - 0.25F, point.position.x + 0.25F, point.position.y + 0.25F, point.position.z + 0.25F), 0, 0, 0);
					}
				}
				GL11.glPopMatrix();
				if(modVehicle != null)
				{
					GL11.glPushMatrix();
					
					GL11.glTranslatef(modVehicle.drillHeadOrigin.x, modVehicle.drillHeadOrigin.y, modVehicle.drillHeadOrigin.z);
					GL11.glRotatef(vehicle.harvesterAngle * 50F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-modVehicle.drillHeadOrigin.x, -modVehicle.drillHeadOrigin.y, -modVehicle.drillHeadOrigin.z);
					modVehicle.renderDrillBit(vehicle, f1);
					
					GL11.glPopMatrix();
				}
			}
			GL11.glPopMatrix();
			
			if(FlansMod.DEBUG)
			{
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1F, 0F, 0F, 0.3F);
				GL11.glScalef(1F, 1F, 1F);
				for(DriveablePart part : vehicle.getDriveableData().parts.values())
				{
					if(part.box == null)
						continue;
					
					renderOffsetAABB(new AxisAlignedBB(part.box.x, part.box.y, part.box.z, (part.box.x + part.box.w), (part.box.y + part.box.h), (part.box.z + part.box.d)), 0, 0, 0);
				}
				//GL11.glColor4f(0F, 1F, 0F, 0.3F);
				//if(type.barrelPosition != null)
				//	renderAABB(AxisAlignedBB.getBoundingBox(type.barrelPosition.x - 0.25F, type.barrelPosition.y - 0.25F, type.barrelPosition.z - 0.25F, type.barrelPosition.x + 0.25F, type.barrelPosition.y + 0.25F, type.barrelPosition.z + 0.25F));
				
				//Render shoot points
				GL11.glColor4f(0F, 0F, 1F, 0.3F);
				for(DriveablePosition point : type.shootPointsPrimary)			
					if(point.part != EnumDriveablePart.turret)
						renderOffsetAABB(new AxisAlignedBB(point.position.x - 0.25F, point.position.y - 0.25F, point.position.z - 0.25F, point.position.x + 0.25F, point.position.y + 0.25F, point.position.z + 0.25F), 0, 0, 0);
				
				GL11.glColor4f(0F, 1F, 0F, 0.3F);
				for(DriveablePosition point : type.shootPointsSecondary)	
					if(point.part != EnumDriveablePart.turret)
						renderOffsetAABB(new AxisAlignedBB(point.position.x - 0.25F, point.position.y - 0.25F, point.position.z - 0.25F, point.position.x + 0.25F, point.position.y + 0.25F, point.position.z + 0.25F), 0, 0, 0);

				
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}
		}
		GL11.glPopMatrix();
	}

	//@Override
	//public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	//{
		//render((EntityVehicle)entity, d, d1, d2, f, f1);
	//}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		DriveableType type = ((EntityDriveable)entity).getDriveableType();
		Paintjob paintjob = type.getPaintjob(((EntityDriveable)entity).getDriveableData().paintjobID);
		return FlansModResourceHandler.getPaintjobTexture(paintjob);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) 
	{
		switch(type)
		{
		case EQUIPPED : case EQUIPPED_FIRST_PERSON : case ENTITY : return Minecraft.getMinecraft().gameSettings.fancyGraphics && item != null && item.getItem() instanceof ItemVehicle && ((ItemVehicle)item.getItem()).type.model != null;
		default : break;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) 
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) 
	{
		GL11.glPushMatrix();
		if(item != null && item.getItem() instanceof ItemVehicle)
		{
			VehicleType vehicleType = ((ItemVehicle)item.getItem()).type;
			if(vehicleType.model != null)
			{
				float scale = 1F;
				switch(type)
				{
				case ENTITY:
				{
					scale = 1.5F;
					//GL11.glRotatef(((EntityItem)data[1]).ticksExisted, 0F, 1F, 0F);
					break;
				}
				case INVENTORY:
				{
					scale = 0.70F;
					GL11.glTranslatef(0F, -0.05F, 0F);
					break;
				}
				case EQUIPPED:
				{
					GL11.glRotatef(0F, 0F, 0F, 1F);
					GL11.glRotatef(270F, 1F, 0F, 0F);
					GL11.glRotatef(270F, 0F, 1F, 0F);
					GL11.glTranslatef(0F, 0.25F, 0F);
					scale = 0.5F;
					break;
				}
				case EQUIPPED_FIRST_PERSON:
				{
					//GL11.glRotatef(25F, 0F, 0F, 1F); 
					GL11.glRotatef(45F, 0F, 1F, 0F);
					GL11.glTranslatef(-0.5F, 0.5F, -0.5F);
					break;
				}
				default : break;
				}
				
				GL11.glScalef(scale / vehicleType.cameraDistance, scale / vehicleType.cameraDistance, scale / vehicleType.cameraDistance);
				Minecraft.getMinecraft().renderEngine.bindTexture(FlansModResourceHandler.getTexture(vehicleType));
				ModelDriveable model = vehicleType.model;
				model.render(vehicleType);
			}
		}
		GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent event)
	{
		//Get the world
		World world = Minecraft.getMinecraft().theWorld;
		mc = Minecraft.getMinecraft();    		
    if(world == null)
			return;

		//Get the camera frustrum for clipping
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
    //Get client side player
        EntityPlayer entityplayer = (EntityPlayer)mc.thePlayer; // minecraft.thePlayer;
        if (entityplayer.ridingEntity instanceof EntitySeat) seatP = (EntitySeat)entityplayer.ridingEntity;                        
        if  (seatP != null)
        { 
          if ((seatP.driveable != null) && (seatP.driveable instanceof EntityVehicle))
          {
            driveP = (EntityVehicle)seatP.driveable;
          }
          else driveP = null;
        }
        else driveP = null;         

        double x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        double y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        double z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;
                
        //Frustum frustrum = new Frustum();
        //frustrum.setPosition(x, y, z);
        
        //Push
        GL11.glPushMatrix();
        //Setup lighting
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_LIGHTING);
        
        RenderHelper.enableStandardItemLighting();
        
        GL11.glTranslatef(-(float)x, -(float)y, -(float)z);
		for(Object entity : world.loadedEntityList)
		{
				float ir = 1.0F;
        float ig = 1.0F;
        float ib = 1.0F;
				float it = 1.0F;
                
 			//Modified code:
      /** Change rendering for passengers of submarine */
      if(entity instanceof EntityLivingBase)
      {
        EntityLivingBase living = (EntityLivingBase)entity;
                                                  
        if (living.ridingEntity instanceof EntitySeat)
        {
          seatT = (EntitySeat)living.ridingEntity;                                
            if ((seatT.driveable != null) && (seatT.driveable instanceof EntityVehicle))
            {
              driveT = (EntityVehicle)seatT.driveable;        
              if (driveT.getVehicleType().moveInWater)
              {  
                boolean seatInfo = driveT.getDriveableType().moveInWater;
                double lx = seatT.prevPosX + (seatT.posX - seatT.prevPosX) * event.partialTicks;
                double ly = seatT.prevPosY + (seatT.posY - seatT.prevPosY) * event.partialTicks;
                double lz = seatT.prevPosZ + (seatT.posZ - seatT.prevPosZ) * event.partialTicks;
                double dx = x - lx;
                double dy = y - ly;
                double dz = z - lz;
                double dxyz = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);                 
                              
                if (driveT.isInWater() || living.isInWater())
                {
                  if (((entityplayer.isInWater()) && ((dxyz > 8) || (dy > 4))) || (!(entityplayer.isInWater()) && ((dxyz > 4) || (dy > 2))))
                  {                                                   
                    if (driveP != driveT)
                    {
						          ir = 0.1F;
							       ig = 0.1F;
							       ib = 0.6F;               
                      GlStateManager.color(ir, ig, ib, 0.1F);
  					         doRender(living, living.prevPosX + (living.posX - living.prevPosX) * event.partialTicks, living.prevPosY + (living.posY - living.prevPosY) * event.partialTicks, living.prevPosZ + (living.posZ - living.prevPosZ) * event.partialTicks, 0F, event.partialTicks);                                                       
                    }                                                
                  }              
                }
              }                                                
            }                                 
        }
      }      
                   
      /** ---------------------------------- */       
			if(entity instanceof EntityVehicle)
			{
				EntityVehicle vehicle = (EntityVehicle)entity;
				        
        double vx = vehicle.prevPosX + (vehicle.posX - vehicle.prevPosX) * event.partialTicks;
        double vy = vehicle.prevPosY + (vehicle.posY - vehicle.prevPosY) * event.partialTicks;
        double vz = vehicle.prevPosZ + (vehicle.posZ - vehicle.prevPosZ) * event.partialTicks;
        double dx = x - vx;
        double dy = y - vy;
        double dz = z - vz;
        double dxyz = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);

		        int i = vehicle.getBrightnessForRender(event.partialTicks);                                          
            // float l = 1.0F;
            if (vehicle.isBurning())
		        {
		            i = 15728880;
		        }

            int j = i % 65536;		          
            int k = i / 65536;

						//Modified code:
            /** Change rendering for submarine */

            if (vehicle.getVehicleType().moveInWater)
            {
             if (vehicle.isInWater())             
		         {		                                                                                        
              if (driveP != vehicle)
              {
							  if (((entityplayer.isInWater()) && ((dxyz > 8) || (dy > 4))) || (!(entityplayer.isInWater()) && ((dxyz > 4) || (dy > 2))))
                GL11.glEnable(GL11.GL_BLEND);
								GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);                       
								ir = 0.1F;
								ig = 0.1F;
								ib = 0.6F;
                it = 0.5F; 
						  }    																							                                                 
   						if (((dxyz < 16) && (dy < 8) && !(entityplayer.isInWater())) || ((dxyz < 24) && (dy < 16) && (entityplayer.isInWater())) || (driveP == vehicle))
              {               
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
		            GlStateManager.color(ir, ig, ib, it);
                render(vehicle, vehicle.prevPosX + (vehicle.posX - vehicle.prevPosX) * event.partialTicks, vehicle.prevPosY + (vehicle.posY - vehicle.prevPosY) * event.partialTicks, vehicle.prevPosZ + (vehicle.posZ - vehicle.prevPosZ) * event.partialTicks, 0F, event.partialTicks);
              }
             }             
             else
             {            
              OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
		          GlStateManager.color(ir, ig, ib, it);
              render(vehicle, vehicle.prevPosX + (vehicle.posX - vehicle.prevPosX) * event.partialTicks, vehicle.prevPosY + (vehicle.posY - vehicle.prevPosY) * event.partialTicks, vehicle.prevPosZ + (vehicle.posZ - vehicle.prevPosZ) * event.partialTicks, 0F, event.partialTicks);
             }
            }
            else
            {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
		        GlStateManager.color(ir, ig, ib, it);
            render(vehicle, vehicle.prevPosX + (vehicle.posX - vehicle.prevPosX) * event.partialTicks, vehicle.prevPosY + (vehicle.posY - vehicle.prevPosY) * event.partialTicks, vehicle.prevPosZ + (vehicle.posZ - vehicle.prevPosZ) * event.partialTicks, 0F, event.partialTicks);            
            }		        													      
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
            /** ---------------------------------- */
      }
		}
		
		//Reset Lighting
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);		
    GL11.glDisable(GL11.GL_LIGHTING);   
		//Pop
		GL11.glPopMatrix();
	}
}

