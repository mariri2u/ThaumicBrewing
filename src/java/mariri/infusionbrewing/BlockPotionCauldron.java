package mariri.infusionbrewing;

import java.util.Random;

import net.minecraft.block.BlockCauldron;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPotionCauldron extends BlockCauldron {
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icon;
	
	/**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote) { return true; }
        ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack == null) { return true; }
        
        int meta = world.getBlockMetadata(x, y, z);
        int pondage = PotionBucketHelper.decodeHigherBit(meta);
//        int pondage = meta;
        System.out.println(meta);
        System.out.println(pondage);
        if (itemstack.getItem() == InfusionBrewing.itemPotionBucket && pondage < 8){
            if (!player.capabilities.isCreativeMode)
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
            }
            int itemmeta = itemstack.getItemDamage() | PotionBucketHelper.HIGHER_MASK;
            world.setBlockMetadataWithNotify(x, y, z, itemmeta, 2);
            System.out.println(itemmeta);
        }else{
            if (itemstack.getItem() == Items.glass_bottle && pondage > 0){
                ItemStack potion = new ItemStack(Items.potionitem, 1, 0);
                NBTTagCompound effect = new NBTTagCompound();
                NBTTagList customPotionEffect = new NBTTagList();
                NBTTagCompound tag = potion.getTagCompound();
                effect.setShort("id", (short)PotionBucketHelper.decodeEffect(meta));
                effect.setShort("Amplifier", (short)PotionBucketHelper.decodeAmplifier(meta));
                effect.setInteger("Duration", PotionBucketHelper.decodeDuration(meta));
                effect.setBoolean("Ambient", PotionBucketHelper.decodeSplash(meta));
                customPotionEffect.appendTag(effect);
                tag.setTag("CustomPotionEffect", customPotionEffect);
                potion.setTagCompound(tag);
                
                if (!player.inventory.addItemStackToInventory(potion)){
                    world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, potion));
                }else if (player instanceof EntityPlayerMP){
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                }

                --itemstack.stackSize;

                if (itemstack.stackSize <= 0){
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                }
                world.setBlockMetadataWithNotify(x, y, z, PotionBucketHelper.writeHigherBit(meta, pondage - 1), 2);
            }
            return false;
        }
        return true;
    }
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        int l = func_150027_b(p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_));
        float f = (float)p_149670_3_ + (6.0F + (float)(8 * l)) / 16.0F;

        if (!p_149670_1_.isRemote && p_149670_5_.isBurning() && l > 0 && p_149670_5_.boundingBox.minY <= (double)f)
        {
            p_149670_5_.extinguish();
            this.func_150024_a(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, l - 1);
        }
    }
	
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(this);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemFromBlock(this);
    }
	
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.icon[1] : (side == 0 ? this.icon[2] : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
    	this.icon = new IIcon[] {
    		iconRegister.registerIcon("mariri:cauldron_inner"),
    		iconRegister.registerIcon("mariri:cauldron_top"),
    		iconRegister.registerIcon("mariri:cauldron_bottom")
    	};
//        this.field_150029_a = iconRegister.registerIcon(this.getTextureName() + "_" + "inner");
//        this.field_150028_b = iconRegister.registerIcon(this.getTextureName() + "_top");
//        this.field_150030_M = iconRegister.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = iconRegister.registerIcon("mariri:cauldron_side");
    }
    
    @SideOnly(Side.CLIENT)
    public static IIcon getCauldronIcon(String p_150026_0_)
    {
        return p_150026_0_.equals("inner") ? InfusionBrewing.blockPotionCauldron.icon[0] : (p_150026_0_.equals("bottom") ? InfusionBrewing.blockPotionCauldron.icon[2] : null);
    }
    
    @SideOnly(Side.CLIENT)
    public static float getRenderLiquidLevel(int p_150025_0_)
    {
        int j = MathHelper.clamp_int(p_150025_0_, 0, 8);
        return (float)(6 + 8 * j) / 16.0F;
    }
    
    
    
    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_)
    {
        int i1 = p_149736_1_.getBlockMetadata(p_149736_2_, p_149736_3_, p_149736_4_);
        return func_150027_b(i1);
    }
}
