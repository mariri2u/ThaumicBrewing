package mariri.infusionbrewing;

import net.minecraft.potion.PotionEffect;


public class PotionBucketHelper {

	public static final int EFFECT_SHIFT = 0;
	public static final int EFFECT_MASK = 0x0000001F;
	public static final int AMPLIFIER_SHIFT = 5;
	public static final int AMPLIFIER_MASK = 0x000000E0;
	public static final int DURATION_SHIFT = 8;
	public static final int DURATION_MASK = 0x00000700;
	public static final int SPLASH_SHIFT = 11;
	public static final int SPLASH_MASK = 0x00000800;
	public static final int HIGHER_SHIFT = 12;
	public static final int HIGHER_MASK = 0x00007000;
	
	public static int decodeEffect(int metadata){
		return metadata & EFFECT_MASK;
	}
	
	public static int decodeAmplifier(int metadata){
		return (metadata & AMPLIFIER_MASK) >> AMPLIFIER_SHIFT;
	}
	
	public static int decodeDuration(int metadata){
		return (metadata & DURATION_MASK) >> DURATION_SHIFT;
	}
	
	public static boolean decodeSplash(int metadata){
		return ((metadata & SPLASH_MASK) >> SPLASH_SHIFT) > 0; 
	}
	
	public static int decodeHigherBit(int metadata){
		return (metadata & HIGHER_MASK) >> HIGHER_SHIFT;
	}
	
	public static PotionEffect decodePotionEffect(int metadata){
		PotionEffect potion = new PotionEffect(
				decodeEffect(metadata),
				decodeDuration(metadata),
				decodeAmplifier(metadata),
				decodeSplash(metadata));
		return potion;
	}
	
	public static int encodeMetadata(int effect, int amplifier, int duration, boolean splash){
		return encodeMetadata(effect, amplifier, duration, splash, 0);
	}
	
	public static int encodeMetadata(int effect, int amplifier, int duration, boolean splash, int higher){
		int meta = 0x00000000;
		meta |= effect;
		meta |= (amplifier << AMPLIFIER_SHIFT);
		meta |= (duration << DURATION_SHIFT);
		meta |= splash ? (1 << SPLASH_SHIFT) : 0;
		meta |= (higher << HIGHER_SHIFT);
		return meta;
	}
	
	public static int writeHigherBit(int metadata, int value){
		return encodeMetadata(
				decodeEffect(metadata),
				decodeAmplifier(metadata) ,
				decodeDuration(metadata),
				decodeSplash(metadata),
				value);
	}
}
