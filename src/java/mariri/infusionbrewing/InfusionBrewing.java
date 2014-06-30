package mariri.infusionbrewing;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = InfusionBrewing.MODID, version = InfusionBrewing.VERSION, dependencies = InfusionBrewing.DEPENDENCIES )
public class InfusionBrewing {
    public static final String MODID = "InfusionBrewing";
    public static final String VERSION = "1.7.2-0.1";
//    public static final String DEPENDENCIES = "required-after:Thaumcraft";
    public static final String DEPENDENCIES = "";
    
    public static BlockFluidPotion blockFluidPotion;
    public static Fluid fluidPotion;
    public static ItemPotionBucket itemPotionBucket;
    public static BlockPotionCauldron blockPotionCauldron;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {


        Material potionMaterial = new PotionMaterial();

    	fluidPotion = new Fluid("potion").setDensity(200).setViscosity(1000);
    	FluidRegistry.registerFluid(fluidPotion);
    	
    	blockFluidPotion =
//    			(BlockFluidPotion)new BlockFluidPotion(fluidPotion, Material.water)
    			(BlockFluidPotion)new BlockFluidPotion(fluidPotion, potionMaterial)
    			.setCreativeTab(CreativeTabs.tabFood).setBlockName("blockLiquidPotion");
    	blockFluidPotion.setPotionEffect(3);
    	GameRegistry.registerBlock(blockFluidPotion, "blockLiquidPotion");
    	
    	itemPotionBucket =
    			(ItemPotionBucket)new ItemPotionBucket(blockFluidPotion)
    			.setCreativeTab(CreativeTabs.tabFood)
    			.setUnlocalizedName("itemPotionBucket")
    			.setContainerItem(Items.bucket);
    	GameRegistry.registerItem(itemPotionBucket, "itemPotionBucket");
    	
    	FillBucketHandler.INSTANCE.buckets.put(blockFluidPotion, itemPotionBucket);
    	FluidContainerRegistry.registerFluidContainer(fluidPotion, new ItemStack(Items.bucket));
    	
//    	blockPotionCauldron = 
//    			(BlockPotionCauldron)new BlockPotionCauldron()
//    			.setCreativeTab(CreativeTabs.tabFood).setBlockName("blockPotionCauldron");
//    	GameRegistry.registerBlock(blockPotionCauldron, "blockPotionCauldron");
    	
		ResourceLocation background = new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png");

		//ResearchCategories.registerCategory(LibResearch.CATEGORY_ENCHANTING, new ResourceLocation(LibResources.MISC_R_ENCHANTING), background);
		ResearchCategories.registerCategory("Brewing", new ResourceLocation("thaumcraft/textures/misc/r_artifice.png"), background);
    	ResearchItem research = new ResearchItem("Brewing", "Brewing").setStub().setAutoUnlock().setRound().registerResearchItem();
    	
    	InfusionRecipe recipe = new InfusionRecipe( 
    			"Brewing",
    			new ItemStack(itemPotionBucket, 1, 3),
    			10,
    			new AspectList().add(Aspect.FIRE, 16).add(Aspect.ENTROPY, 16),
    			new ItemStack(Items.water_bucket),
    			new ItemStack[]{
    				new ItemStack(Items.nether_wart),
    				new ItemStack(Items.magma_cream)});
    	ThaumcraftApi.getCraftingRecipes().add(recipe);
    	
    	
    	MinecraftForge.EVENT_BUS.register(FillBucketHandler.INSTANCE);
    }
}
