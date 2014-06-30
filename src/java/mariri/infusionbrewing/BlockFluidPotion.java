package mariri.infusionbrewing;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidPotion extends BlockFluidClassic {
	
	@SideOnly(Side.CLIENT)
	protected IIcon[] theIcon;
	protected int potionEffect;
	
	public BlockFluidPotion(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
	public BlockFluidPotion setPotionEffect(int potionEffect){
		this.potionEffect = potionEffect;
		return this;
	}
	
	public int getPotionEffect(){
		return potionEffect;
	}
	
//    protected void flowIntoBlock(World world, int x, int y, int z, int meta)
//    {
//    	Block block = world.getBlock(x, y, z);
//    	if(block == Blocks.water){
//    		
//    	}else if(block == Blocks.lava){
//    		
//    	}
//    	world.setBlockToAir(x, y, z);
//    	super.flowIntoBlock(world, x, y, z, meta);
//    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
    	checkExplosion(world, x, y, z);
    	super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
    	checkExplosion(world, x, y, z);
    	super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    private boolean checkExplosion(World world, int x, int y, int z){
        boolean flag = false;
        if(world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 0){
            flag |= world.getBlock(x, y, z - 1).getMaterial() == Material.lava;
            flag |= world.getBlock(x, y, z + 1).getMaterial() == Material.lava;
            flag |= world.getBlock(x - 1, y, z).getMaterial() == Material.lava;
            flag |= world.getBlock(x + 1, y, z).getMaterial() == Material.lava;
            flag |= world.getBlock(x, y + 1, z).getMaterial() == Material.lava;
           	flag |= world.canBlockSeeTheSky(x, y, z) && world.getBlockLightValue(x, y, z) == 15;
           	flag |= world.provider.isHellWorld;
            if(flag){
            	world.setBlockToAir(x, y, z);
            	world.createExplosion(null, x, y, z, 4, true);
            }
        }
        return flag;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand){
		super.updateTick(world, x, y, z, rand);
		if(checkExplosion(world, x, y, z)) { return; }
//		Block aboveBlock = world.getBlock(x, y + 1, z);
		int count = 0;
//		int waterCount = 0;
//		int lavaCount = 0;
		for(int i = 0; i < 4; i++){
			int offsetX = 0;
			int offsetZ = 0;
			switch(i){
			case 0:
				offsetX = -1;
				break;
			case 1:
				offsetX = 1;
				break;
			case 2:
				offsetZ = -1;
				break;
			case 3:
				offsetZ = 1;
				break;
			}
			Block block = world.getBlock(x + offsetX, y, z + offsetZ);
			if(block == this && world.getBlockMetadata(x + offsetX, y, z + offsetZ) == 0){
				count++;
			}
//			if(block == Blocks.water){
//				waterCount++;
//			}
//			if(block == Blocks.lava){
//				lavaCount++;
//			}
		}
//		block = world.getBlock(x - 1, y, z);
//		if(block == this && world.getBlockMetadata(x - 1, y, z) == 0){
//			count++;
//		}
//		block = world.getBlock(x, y, z + 1);
//		if(block == this && world.getBlockMetadata(x, y, z + 1) == 0){
//			count++;
//		}
//		block = world.getBlock(x, y, z - 1);
//		if(block == this && world.getBlockMetadata(x, y, z - 1) == 0){
//			count++;
//		}
//		if(count >= 2){
		
//		if(aboveBlock == Blocks.water || waterCount > 0){
//			world.setBlock(x, y, z, Blocks.water, 1, 2);
//            world.scheduleBlockUpdate(x, y, z, Blocks.water, 0);
//            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.water);
//		}else if(aboveBlock == Blocks.lava || lavaCount > 0){
//			if(world.getBlockMetadata(x, y, z) == 0){
//				world.createExplosion(null, x, y, z, 4, true);
//				world.setBlockToAir(x, y, z);
//	            world.scheduleBlockUpdate(x, y, z, Blocks.air, 0);
//	            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.air);
//			}else{
//				world.setBlock(x, y, z, Blocks.lava, 1, 2);
//	            world.scheduleBlockUpdate(x, y, z, Blocks.lava, 0);
//	            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.lava);
//			}
//		}else if(potCount >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
		if(count >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            world.scheduleBlockUpdate(x, y, z, this, 0);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
		}
		
//		if(potCount >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
//			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
//            world.scheduleBlockUpdate(x, y, z, this, 0);
//            world.notifyBlocksOfNeighborChange(x, y, z, this);
//		}
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.canDisplace(world, x, y, z);
	}
	
	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, x, y, z);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.theIcon[1] : this.theIcon[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.theIcon = new IIcon[] {
				iconRegister.registerIcon("mariri:potion_still"),
				iconRegister.registerIcon("mariri:potion_flow")};
	}
}
