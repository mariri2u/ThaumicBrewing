package mariri.infusionbrewing;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FillBucketHandler {

    public static FillBucketHandler INSTANCE = new FillBucketHandler();
    public Map<Block, Item> buckets = new HashMap<Block, Item>();

    private FillBucketHandler() {}
	
	@SubscribeEvent
	public void onFillBucket(FillBucketEvent e){
        Block block = e.world.getBlock(e.target.blockX, e.target.blockY, e.target.blockZ);
        Item bucket = buckets.get(block);
        System.out.println("!!!");
        if(bucket != null && e.world.getBlockMetadata(e.target.blockX, e.target.blockY, e.target.blockZ) == 0){
            e.world.setBlockToAir(e.target.blockX, e.target.blockY, e.target.blockZ);
            e.result = new ItemStack(InfusionBrewing.itemPotionBucket, 1, ((BlockFluidPotion)block).getPotionEffect());
            e.setResult(Result.ALLOW);
        }
	}
}
