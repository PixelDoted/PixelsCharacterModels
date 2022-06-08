package me.pixeldots.pixelscharactermodels.skin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class StringArgument implements ArgumentType<String> {

	public static <S> String getstring(String name, CommandContext<S> context) {
		return context.getArgument(name, String.class);
	}
	
	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.getString();
	}

}
