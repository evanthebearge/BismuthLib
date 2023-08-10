package ru.paulevs.bismuthlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class PrintCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(ru.paulevs.bismuthlib.BismuthLibClient.MOD_ID);
	
	public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
		commandDispatcher.register(((CommandManager.literal("cfprint")).executes(PrintCommand::process)));
	}
	
	private static int process(CommandContext<ServerCommandSource> commandSourceStackCommandContext) {
		Set<Block> blocks = new HashSet<>();
		Registry.BLOCK.forEach(block ->
			block.getStateManager().getStates().stream().filter(
				state -> state.getLuminance() > 0).forEach(state -> blocks.add(block)
			)
		);
		blocks.forEach(block -> {
			Identifier id = Registry.BLOCK.getId(block);
			LOGGER.info(id.toString());
		});
		return 0;
	}
}
