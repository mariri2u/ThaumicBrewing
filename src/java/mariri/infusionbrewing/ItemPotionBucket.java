package mariri.infusionbrewing;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBucket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBucket extends ItemBucket {

	private String iconName;
	public ItemPotionBucket(Block block){
		super(block);
	}
	
//	@Override
//	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z){
//		boolean result = super.tryPlaceContainedLiquid(world, x, y, z);
////		world.setBlockMetadataWithNotify(x, y, z, 3, 4);
//		return result;
//	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("mariri:bucket_water");
	}
}
