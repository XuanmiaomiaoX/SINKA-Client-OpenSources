/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SideOnly(Side.CLIENT)
public class Fonts extends MinecraftInstance {
    @FontDetails(fontName = "Jello", fontSize = 100)
    public static GameFontRenderer Jello40;
    @FontDetails(fontName = "Tenacity", fontSize = 25)
    public static GameFontRenderer Tenacity25;

    @FontDetails(fontName = "Tenacity", fontSize = 30)
    public static GameFontRenderer Tenacity30;

    @FontDetails(fontName = "Tenacity", fontSize = 35)
    public static GameFontRenderer Tenacity35;

    @FontDetails(fontName = "Tenacity", fontSize = 100)
    public static GameFontRenderer Tenacity40;

    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static GameFontRenderer font35;

    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static GameFontRenderer font40;

    @FontDetails(fontName = "regular40", fontSize = 40)
    public static GameFontRenderer regular40;

    @FontDetails(fontName = "regular30", fontSize = 30)
    public static GameFontRenderer regular30;

    @FontDetails(fontName = "regular90", fontSize = 90)
    public static GameFontRenderer regular90;

    @FontDetails(fontName = "regular60", fontSize = 60)
    public static GameFontRenderer regular60;

    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static GameFontRenderer fontBold180;

    @FontDetails(fontName = "wqy18", fontSize = 18)
    public static GameFontRenderer wqy18;

    @FontDetails(fontName = "wqy35", fontSize = 35)
    public static GameFontRenderer wqy35;

    @FontDetails(fontName = "wqy36", fontSize = 36)
    public static GameFontRenderer wqy36;

    @FontDetails(fontName = "wqy100", fontSize = 100)
    public static GameFontRenderer wqy100;

    @FontDetails(fontName = "wqy30", fontSize = 30)
    public static GameFontRenderer wqy30;

    @FontDetails(fontName = "Logo100", fontSize = 100)
    public static GameFontRenderer Logo100;

    @FontDetails(fontName = "wqy40", fontSize = 40)
    public static GameFontRenderer wqy40;

    @FontDetails(fontName = "wqy40_reg", fontSize = 40)
    public static GameFontRenderer wqy40_reg;

    @FontDetails(fontName = "BebasNeue", fontSize = 35)
    public static GameFontRenderer BebasNeue35;

    @FontDetails(fontName = "comfortaa35", fontSize = 35)
    public static GameFontRenderer comfortaa35;

    @FontDetails(fontName = "comfortaa35", fontSize = 100)
    public static GameFontRenderer comfortaa100;

    @FontDetails(fontName = "comfortaa16", fontSize = 16)
    public static GameFontRenderer comfortaa16;

    @FontDetails(fontName = "roboto20", fontSize = 20)
    public static GameFontRenderer roboto20;

    @FontDetails(fontName = "roboto25", fontSize = 25)
    public static GameFontRenderer roboto25;
    @FontDetails(fontName = "roboto35", fontSize = 35)
    public static GameFontRenderer roboto35;

    @FontDetails(fontName = "roboto100", fontSize = 100)
    public static GameFontRenderer roboto100;

    @FontDetails(fontName = "roboto40", fontSize = 40)
    public static GameFontRenderer roboto40;

    @FontDetails(fontName = "roboto30", fontSize = 30)
    public static GameFontRenderer roboto30;

    @FontDetails(fontName = "Sfui35", fontSize = 35)
    public static GameFontRenderer Sfui35;

    @FontDetails(fontName = "Sfui60", fontSize = 35)
    public static GameFontRenderer Sfui60;

    @FontDetails(fontName = "Sfui24", fontSize = 24)
    public static GameFontRenderer Sfui24;

    @FontDetails(fontName = "Wqy_Microhei", fontSize = 35)
    public static GameFontRenderer wqy_microhei35;

    @FontDetails(fontName = "arial28", fontSize = 28)
    public static GameFontRenderer arial28;

    @FontDetails(fontName = "arial35", fontSize = 35)
    public static GameFontRenderer arial35;

    @FontDetails(fontName = "baloo18", fontSize = 18)
    public static GameFontRenderer Baloo18;

    @FontDetails(fontName = "baloo35", fontSize = 35)
    public static GameFontRenderer Baloo35;

    @FontDetails(fontName = "icon28", fontSize = 28)
    public static GameFontRenderer icon28;

    @FontDetails(fontName = "icon35", fontSize = 35)
    public static GameFontRenderer icon35;

    @FontDetails(fontName = "icon16", fontSize = 16)
    public static GameFontRenderer icon16;

    @FontDetails(fontName = "icon40", fontSize = 40)
    public static GameFontRenderer icon40;

    @FontDetails(fontName = "icon18", fontSize = 18)
    public static GameFontRenderer icon18;

    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont = Minecraft.getMinecraft().fontRendererObj;

    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS = new ArrayList<>();

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

        downloadFonts();
        wqy35 = new GameFontRenderer(getFont("wqy_microhei.ttf",35));
        Jello40 = new GameFontRenderer(getFont("Jello.ttf",100));
        Tenacity25 = new GameFontRenderer(getFont("tenacity.ttf",25));
        Tenacity30 = new GameFontRenderer(getFont("tenacity.ttf",30));
        Tenacity35 = new GameFontRenderer(getFont("tenacity.ttf",35));
        Tenacity40 = new GameFontRenderer(getFont("tenacity.ttf",100));
        roboto20 = new GameFontRenderer(getFont("Roboto.ttf",20));
        roboto25 = new GameFontRenderer(getFont("Roboto.ttf",25));
        roboto30 = new GameFontRenderer(getFont("Roboto.ttf",30));
        roboto35 = new GameFontRenderer(getFont("Roboto.ttf",35));
        roboto40 = new GameFontRenderer(getFont("Roboto.ttf",40));
        roboto100 = new GameFontRenderer(getFont("Roboto.ttf",100));
        BebasNeue35 = new GameFontRenderer(getFont("BebasNeue.ttf",35));
        Baloo18 = new GameFontRenderer(getFont("Baloo.ttf",18));
        Baloo35 = new GameFontRenderer(getFont("Baloo.ttf",35));
        Sfui35 = new GameFontRenderer(getFont("sfui.ttf",35));
        Sfui60 = new GameFontRenderer(getFont("sfui.ttf",60));
        Sfui24 = new GameFontRenderer(getFont("sfui.ttf",24));
        arial28 = new GameFontRenderer(getFont("ArialBold.ttf",28));
        arial35 = new GameFontRenderer(getFont("ArialBold.ttf",35));
        icon18 = new GameFontRenderer(getFont("Icon.ttf",18));
        icon40 = new GameFontRenderer(getFont("Icon.ttf",40));
        icon35 = new GameFontRenderer(getFont("Icon.ttf",35));
        icon16 = new GameFontRenderer(getFont("Icon.ttf",16));
        icon28 = new GameFontRenderer(getFont("Icon.ttf",28));
        wqy40 = new GameFontRenderer(getFont("wqy_microhei.ttf",40));
        wqy40_reg = new GameFontRenderer(getFont("regular.ttf",40));
        font35 = new GameFontRenderer(getFont("Roboto-Medium.ttf", 35));
        font40 = new GameFontRenderer(getFont("Roboto-Medium.ttf", 40));
        regular40 = new GameFontRenderer(getFont("regular.ttf", 40));
        regular30 = new GameFontRenderer(getFont("regular.ttf", 30));
        regular60 = new GameFontRenderer(getFont("regular.ttf", 60));
        regular90 = new GameFontRenderer(getFont("regular.ttf", 90));
        Logo100 = new GameFontRenderer(getFont("logo100.ttf",100));
        wqy18 = new GameFontRenderer(getFont("wqy_microhei.ttf",18));
        wqy30 = new GameFontRenderer(getFont("regular.ttf",30));
        wqy36 = new GameFontRenderer(getFont("regular.ttf",36));
        wqy100 = new GameFontRenderer(getFont("regular.ttf",100));
        wqy_microhei35 = new GameFontRenderer(getFont("wqy_microhei.ttf", 35));
        comfortaa16 = new GameFontRenderer(getFont("comfortaa.ttf",16));
        comfortaa35 = new GameFontRenderer(getFont("comfortaa.ttf",35));
        comfortaa100 = new GameFontRenderer(getFont("comfortaa.ttf",100));
        fontBold180 = new GameFontRenderer(getFont("Roboto-Bold.ttf", 180));

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if(fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if(jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for(final JsonElement element : jsonArray) {
                    if(element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            }else{
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        }catch(final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");

            if(!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download("https://cloud.liquidbounce.net/LiquidBounce/fonts/Roboto.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static FontRenderer getFontRenderer(final String name, final int size) {
        for(final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if(o instanceof FontRenderer) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if(fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (FontRenderer) o;
                }
            }catch(final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (final GameFontRenderer liquidFontRenderer : CUSTOM_FONT_RENDERERS) {
            final Font font = liquidFontRenderer.getDefaultFont().getFont();

            if(font.getName().equals(name) && font.getSize() == size)
                return liquidFontRenderer;
        }

        return minecraftFont;
    }

    public static Object[] getFontDetails(final FontRenderer fontRenderer) {
        for(final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if(o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new Object[] {fontDetails.fontName(), fontDetails.fontSize()};
                }
            }catch(final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (fontRenderer instanceof GameFontRenderer) {
            final Font font = ((GameFontRenderer) fontRenderer).getDefaultFont().getFont();

            return new Object[] {font.getName(), font.getSize()};
        }

        return null;
    }

    public static List<FontRenderer> getFonts() {
        final List<FontRenderer> fonts = new ArrayList<>();

        for(final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if(fontObj instanceof FontRenderer) fonts.add((FontRenderer) fontObj);
            }catch(final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        }catch(final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if(!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while(zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        }catch(final IOException e) {
            e.printStackTrace();
        }
    }
}