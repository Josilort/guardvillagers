package tallestegg.guardvillagers;

import net.minecraft.block.Blocks;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tallestegg.guardvillagers.configuration.GuardConfig;
import tallestegg.guardvillagers.entities.GuardEntity;
import tallestegg.guardvillagers.renderer.GuardRenderer;
import tallestegg.guardvillagers.renderer.GuardRenderer2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("guardvillagers")
public class GuardVillagers
{  
	public static final String MODID = "guardvillagers";
	
    private static final Logger LOGGER = LogManager.getLogger();

    public GuardVillagers() 
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, GuardConfig.CLIENT_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HandlerEvents());
        MinecraftForge.EVENT_BUS.register(new GuardSpawner());
        MinecraftForge.EVENT_BUS.register(new GuardEntityType());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) 
    {
    	if (GuardConfig.GuardModel == false)
    	RenderingRegistry.registerEntityRenderingHandler(GuardEntityType.GUARD, GuardRenderer::new);
    	else
        RenderingRegistry.registerEntityRenderingHandler(GuardEntityType.GUARD, GuardRenderer2::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents 
    {
 
    }
}


