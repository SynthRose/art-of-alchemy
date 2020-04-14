package io.github.synthrose.artofalchemy;

import io.github.synthrose.artofalchemy.util.MateriaRank;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;


@Config(name = ArtOfAlchemy.MOD_ID)
public class AoAConfig implements ConfigData {

    public static AoAConfig get() {
        return AutoConfig.getConfigHolder(AoAConfig.class).getConfig();
    }

    public int networkProcessingLimit = 1024;
    public boolean formulaLoot = true;

    public int vesselCapacity = 4000;
    public int tankCapacity = 8000;
    public int centrifugeCapacity = 4000;

    @ConfigEntry.Gui.CollapsibleObject
    public CalcinatorSettings calcinatorSettings = new CalcinatorSettings();
    public static class CalcinatorSettings {
        public float yieldBasic = 0.5f;
        public float yieldPlus = 1.0f;
        public int opTimeBasic = 60;
        public int opTimePlus = 120;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public DissolverSettings dissolverSettings = new DissolverSettings();
    public static class DissolverSettings {
        public float yieldBasic = 0.5f;
        public float yieldPlus = 1.0f;
        public float speedBasic = 0.05f;
        public float speedPlus = 0.10f;
        public int tankBasic = 4000;
        public int tankPlus = 8000;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public SynthesizerSettings synthesizerSettings = new SynthesizerSettings();
    public static class SynthesizerSettings {
        public MateriaRank maxTierBasic = MateriaRank.C;
        public MateriaRank maxTierPlus = MateriaRank.OMEGA;
        public float speedBasic = 0.05f;
        public float speedPlus = 0.10f;
        public int tankBasic = 4000;
        public int tankPlus = 8000;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public ProjectorSettings projectorSettings = new ProjectorSettings();
    public static class ProjectorSettings {
        public int opTime = 180;
        public int tankSize = 8000;
    }

}
