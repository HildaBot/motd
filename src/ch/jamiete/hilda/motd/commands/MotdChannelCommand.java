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

import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class MotdChannelCommand extends ChannelSubCommand {
    private MotdPlugin plugin;

    protected MotdChannelCommand(Hilda hilda, ChannelSeniorCommand senior, MotdPlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("channel");
        this.setDescription("Sets the channel to send to when someone joins.");
    }

    @Override
    public void execute(Message message, String[] arguments, String label) {
        Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getGuild().getId());

        if (arguments.length == 0) {
            cfg.get().remove("channel");
            cfg.save();
            this.reply(message, "Removed the MOTD channel!");
        } else {
            if (message.getMentionedChannels().isEmpty()) {
                this.reply(message, "You must mention the channel!");
                return;
            }

            TextChannel chan = message.getMentionedChannels().get(0);

            cfg.get().addProperty("channel", chan.getId());
            cfg.save();

            this.reply(message, "Okay, I'll now send messages to " + chan.getAsMention());

            if (cfg.get().get("message") == null) {
                this.reply(message, "You haven't yet set the message for me to send.");
            }
        }
    }

}
