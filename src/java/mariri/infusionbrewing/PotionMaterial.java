package mariri.infusionbrewing;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

public class PotionMaterial extends MaterialLiquid {
	
    public PotionMaterial()
    {
        super(MapColor.waterColor);
        this.setNoPushMobility();
    }


    public PotionMaterial(MapColor par1MapColor)
    {
        super(par1MapColor);
        this.setNoPushMobility();
    }
}
