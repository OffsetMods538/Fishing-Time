package top.offsetmonkey538.fishingtime;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishingTime implements ModInitializer {
	public static final String MOD_ID = "fishing-time";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Do stuff
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
