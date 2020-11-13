package org.syringemc.syringefabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.syringe.api.SyringeAPI;

public class SyringeFabric implements ModInitializer {
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        SyringeAPI.init(new APIImpl());
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> SERVER = server);
    }
}
