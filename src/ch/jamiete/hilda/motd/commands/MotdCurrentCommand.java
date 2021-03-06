/*******************************************************************************
 * Copyright 2017 jamietech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ch.jamiete.hilda.motd.commands;

import java.util.Arrays;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.commands.CommandManager;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class MotdCurrentCommand extends ChannelSubCommand {
    private final MotdPlugin plugin;

    protected MotdCurrentCommand(final Hilda hilda, final ChannelSeniorCommand senior, final MotdPlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("current");
        this.setAliases(Arrays.asList("view", "now", "show"));
        this.setDescription("Shows the current configuration of the motd.");
    }

    @Override
    public void execute(final Message message, final String[] arguments, final String label) {
        final Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getGuild().getId());

        final String motd = cfg.getString("motd", null);
        final TextChannel channel = message.getGuild().getTextChannelById(cfg.getString("channel", "1"));

        final MessageBuilder mb = new MessageBuilder();

        mb.append("Motd message", MessageBuilder.Formatting.UNDERLINE).append("\n");
        mb.append("Current motd message configuration", MessageBuilder.Formatting.ITALICS).append("\n\n");

        mb.append("Channel:", MessageBuilder.Formatting.BOLD).append(" ").append(channel == null ? "*Unset.*" : "#" + channel.getName()).append("\n");
        mb.append("Motd:", MessageBuilder.Formatting.BOLD).append(motd == null ? " *Unset.*" : "\n\n" + motd).append("\n\n");

        mb.append("To change the message say ").append(CommandManager.PREFIX + this.getSenior().getName() + " message <message>", MessageBuilder.Formatting.BOLD);
        mb.append(". You can use the following substitutions: $mention $username $effective $discriminator $id. ");
        mb.append("Emoji and emotes can be used.");

        mb.buildAll(MessageBuilder.SplitPolicy.SPACE).forEach(m -> this.reply(message, m));
    }

}
