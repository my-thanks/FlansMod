package co.uk.flansmods.client.model;

import com.overminddl1.minecraft.libs.NMT.NMTGlobal;
import com.overminddl1.minecraft.libs.NMT.NMTModelBase;
import com.overminddl1.minecraft.libs.NMT.NMTModelBox;
import com.overminddl1.minecraft.libs.NMT.NMTModelRenderer;
import com.overminddl1.minecraft.libs.NMT.NMTModelTrapezoid;

public class ModelKubel extends ModelVehicle
{
    public ModelKubel()
    {
		int textureX = 128;
		int textureY = 128;
	
        bodyModel = new NMTModelRenderer[10];	
		
	    bodyModel[0] = new NMTModelRenderer(this, 0, 0, textureX, textureY);
        bodyModel[1] = new NMTModelRenderer(this, 0, 94, textureX, textureY);
        bodyModel[2] = new NMTModelRenderer(this, 0, 40, textureX, textureY);
        bodyModel[3] = new NMTModelRenderer(this, 0, 40, textureX, textureY);
		bodyModel[4] = new NMTModelRenderer(this, 60, 12, textureX, textureY);
        bodyModel[5] = new NMTModelRenderer(this, 0, 43, textureX, textureY);
		bodyModel[6] = new NMTModelRenderer(this, 60, 0, textureX, textureY);
        bodyModel[7] = new NMTModelRenderer(this, 60, 0, textureX, textureY);
		bodyModel[8] = new NMTModelRenderer(this, 32, 49, textureX, textureY);
        bodyModel[9] = new NMTModelRenderer(this, 90, 12, textureX, textureY);

		bodyModel[0].addBox(8F, -6F , -14F, 16, 12, 28, 0.0F);		//Bonnet
        bodyModel[1].addBox(-24F, 4F, -16F, 32, 2, 32, 0.0F);		//Floor
		bodyModel[2].addBox(8F, 0F, -16F, 16, 1, 2, 0.0F);		//Wheel cover
		bodyModel[3].addBox(8F, 0F, 14F, 16, 1, 2, 0.0F);			//Wheel cover
		bodyModel[4].addBox(-6F, -4F, -14F, 1, 8, 28, 0.0F);		//Mid seat
		bodyModel[5].addBox(-24F, -6F, -14F, 4, 10, 28, 0.0F);		//Back seat
		bodyModel[6].addBox(-24F, -6F, -16F, 32, 10, 2, 0.0F);		//Door
		
		//Other door
		NMTModelBase temp = new NMTModelBox(bodyModel[7], -24F, -6F, -16F, 32, 10, 2, 0F, 0F, false);
		temp.doMirror(false, false, true);
		bodyModel[7].addModel(temp);

		bodyModel[8].addBox(8F, -14F, -16F, 1, 8, 32, 0.0F);		//Windshield
		bodyModel[9].addBox(12F, -8F, -4F, 8, 2, 8, 0.0F);			//Spare Wheel
			
		leftBackWheelModel = new NMTModelRenderer[1];	
        leftBackWheelModel[0] = new NMTModelRenderer(this, 90, 22, textureX, textureY);
		leftBackWheelModel[0].addBox(-20F, 6F, 13F, 8, 4, 2, 0.0F);
		
		rightBackWheelModel = new NMTModelRenderer[1];	
		rightBackWheelModel[0] = new NMTModelRenderer(this, 90, 22, textureX, textureY);
		rightBackWheelModel[0].addBox(-20F, 6F, -15F, 8, 4, 2, 0.0F);
		
		leftFrontWheelModel = new NMTModelRenderer[1];			
	    leftFrontWheelModel[0] = new NMTModelRenderer(this, 90, 28, textureX, textureY);		
		leftFrontWheelModel[0].addBox(-4F, -4F, -1F, 8, 8, 2, 0.0F);
		leftFrontWheelModel[0].setRotationPoint(16F, 6F, 15F);
		
		rightFrontWheelModel = new NMTModelRenderer[1];	
        rightFrontWheelModel[0] = new NMTModelRenderer(this, 90, 28, textureX, textureY);		
        rightFrontWheelModel[0].addBox(-4F, -4F, -1F, 8, 8, 2, 0.0F);
		rightFrontWheelModel[0].setRotationPoint(16F, 6F, -15F);	
		
		//Gun model not used yet
 		gunModel = new NMTModelRenderer[0];
		/*
		gunModel[0] = new NMTModelRenderer(this, 66, 55, textureX, textureY);
		gunModel[1] = new NMTModelRenderer(this, 66, 70, textureX, textureY);
		gunModel[2] = new NMTModelRenderer(this, 70, 55, textureX, textureY);

		gunModel[0].addBox(-1.5F, -1.5F, -4F, 3, 3, 12);
		gunModel[0].setRotationPoint(-6F, 18F, 0F);
		gunModel[1].addBox(-0.5F, -0.5F, -12F, 1, 1, 10);
		gunModel[1].setRotationPoint(-6F, 18F, 0F);
		gunModel[2].addBox(-1F, -3F, 8F, 2, 3, 2);
		gunModel[2].setRotationPoint(-6F, 18F, 0F);			
		
		ammoModel = new NMTModelRenderer[1];
		ammoModel[0] = new NMTModelRenderer(this, 84, 55, textureX, textureY);
		ammoModel[0].addBox(-6F, -3F, -2F, 5, 4, 2);
		ammoModel[0].setRotationPoint(-6F, 18F, 0F);			
		*/
		
		//This should be defined even when not used.
		turretModel = new NMTModelRenderer[0];	
		barrelModel = new NMTModelRenderer[0];	
		ammoModel = new NMTModelRenderer[0];
		
		flipAll(); //For old models
    }
}
