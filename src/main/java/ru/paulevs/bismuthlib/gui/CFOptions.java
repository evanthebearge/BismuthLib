package ru.paulevs.bismuthlib.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class CFOptions {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toString(), "bismuthlib.json");
	private static JsonObject config;
	
	static {
		if (FILE.exists()) {
			try {
				FileReader reader = new FileReader(FILE);
				config = GSON.fromJson(reader, JsonObject.class);
				reader.close();
			}
			catch (IOException e) {
				config = new JsonObject();
				e.printStackTrace();
			}
		}
		else {
			config = new JsonObject();
		}
	}
	
	private static int mapRadiusXZ = getInt("mapRadiusXZ", 2);
	private static int mapRadiusY = getInt("mapRadiusY", 2);
	private static boolean fastLight = getBool("fastLight", false);
	private static int threads = getInt("threads", 4);
	private static boolean modifyColor = getBool("modifyColor", false);
	private static int brightness = getInt("brightness", 64);
	private static float floatBrightness = brightness / 64F;
	private static boolean brightSources = getBool("brightSources", false);
	
	public static final SimpleOption<Integer> MAP_RADIUS_XZ = new SimpleOption<>(
		"bismuthlib.options.mapRadiusXZ",
		SimpleOption.emptyTooltip(),
		(component, integer) -> {
			int i = integer * 2 + 1;
			return GameOptions.getGenericValueText(component, Text.translatable("options.biomeBlendRadius." + i));
		},
		new SimpleOption.ValidatingIntSliderCallbacks(1, 7),
		mapRadiusXZ,
		val -> {
			setInt("mapRadiusXZ", val);
			mapRadiusXZ = val;
		}
	);
	public static final SimpleOption<Integer> MAP_RADIUS_Y = new SimpleOption<>(
		"bismuthlib.options.mapRadiusY",
		SimpleOption.emptyTooltip(),
		(component, integer) -> {
			int i = integer * 2 + 1;
			return GameOptions.getGenericValueText(component, Text.translatable("bismuthlib.options.mapRadiusY." + i));
		},
		new SimpleOption.ValidatingIntSliderCallbacks(1, 7),
		mapRadiusY,
		val -> {
			setInt("mapRadiusY", val);
			mapRadiusY = val;
		}
	);
	public static final SimpleOption<Integer> BRIGHTNESS = new SimpleOption<>(
		"bismuthlib.options.brightness",
		SimpleOption.emptyTooltip(),
		(component, i) -> GameOptions.getGenericValueText(component, Text.translatable(String.format(Locale.ROOT, "%.2f", i / 64F))), //Options.genericValueLabel(component, i),
		new SimpleOption.ValidatingIntSliderCallbacks(0, 128),
		brightness,
		val -> {
			setInt("brightness", val);
			brightness = val;
			floatBrightness = val / 64F;
		}
	);
	
	private static final SimpleOption<Boolean> FAST_LIGHT = new SimpleOption<>(
		"bismuthlib.options.lightType",
		SimpleOption.emptyTooltip(),
		(component, bool) -> Text.translatable("bismuthlib.options.fastLight." + bool),
		SimpleOption.BOOLEAN,
		fastLight,
		val -> {
			setBool("fastLight", val);
			fastLight = val;
		}
	);
	private static final SimpleOption<Integer> THREADS = new SimpleOption<>(
		"bismuthlib.options.threads",
		SimpleOption.emptyTooltip(),
		(component, i) -> GameOptions.getGenericValueText(component, i),
		new SimpleOption.ValidatingIntSliderCallbacks(1, 16),
		threads,
		val -> {
			setInt("threads", val);
			threads = val;
		}
	);
	private static final SimpleOption<Boolean> MODIFY_COLOR = new SimpleOption<>(
		"bismuthlib.options.modifyColor",
		SimpleOption.emptyTooltip(),
		(component, bool) -> Text.translatable("bismuthlib.options.modifyColor." + bool),
		SimpleOption.BOOLEAN,
		modifyColor,
		val -> {
			setBool("modifyColor", val);
			modifyColor = val;
		}
	);
	private static final SimpleOption<Boolean> BRIGHT_SOURCES = new SimpleOption<>(
		"bismuthlib.options.brightSources",
		SimpleOption.emptyTooltip(),
		(component, bool) -> Text.translatable("bismuthlib.options.brightSources." + bool),
		SimpleOption.BOOLEAN,
		brightSources,
		val -> {
			setBool("brightSources", val);
			brightSources = val;
		}
	);
	
	public static final SimpleOption[] OPTIONS = new SimpleOption[] {
		FAST_LIGHT, THREADS, MODIFY_COLOR, BRIGHT_SOURCES
	};
	
	public static int getMapRadiusXZ() {
		return mapRadiusXZ * 2 + 1;
	}
	
	public static int getMapRadiusY() {
		return mapRadiusY * 2 + 1;
	}
	
	public static boolean isFastLight() {
		return fastLight;
	}
	
	public static int getThreadCount() {
		return threads;
	}
	
	public static boolean modifyColor() {
		return modifyColor;
	}
	
	public static float getBrightness() {
		return floatBrightness;
	}
	
	public static boolean isBrightSources() {
		return brightSources;
	}
	
	private static int getInt(String name, int def) {
		return config.has(name) ? config.get(name).getAsInt() : def;
	}
	
	private static void setInt(String name, int val) {
		config.add(name, new JsonPrimitive(val));
	}
	
	private static boolean getBool(String name, boolean def) {
		return config.has(name) ? config.get(name).getAsBoolean() : def;
	}
	
	private static void setBool(String name, boolean val) {
		config.add(name, new JsonPrimitive(val));
	}
	
	public static void save() {
		String line = GSON.toJson(config);
		try {
			FileWriter writer = new FileWriter(FILE);
			writer.write(line);
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
