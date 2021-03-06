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

import java.util.Collections;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.motd.MotdPlugin;

public class MotdMessageCommand extends ChannelSubCommand {
    private final MotdPlugin plugin;

    protected MotdMessageCommand(final Hilda hilda, final ChannelSeniorCommand senior, final MotdPlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("message");
        this.setAliases(Collections.singletonList("msg"));
        this.setDescription("Sets the message to send when someone joins.");
    }

    @Override
    public void execute(final Message message, final String[] arguments, final String label) {
        final Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getGuild().getId());

        if (arguments.length == 0) {
            cfg.get().remove("motd");
            cfg.save();
            this.reply(message, "Removed the MOTD message!");
        } else {
            String msg = message.getContentRaw();

            msg = StringUtils.removeAll(msg, "\\!\\w+ \\w+ ");

            if (msg.length() > 2000) {
                this.reply(message, "Oops, your message is too long. It must be less than 2000 characters when expanded. Please make it shorter and try again.");
                return;
            }

            cfg.get().addProperty("motd", msg);
            cfg.save();

            this.reply(message, "OK, I'll now send this when someone joins:\n\n");
            this.reply(message, msg);

            if (cfg.get().get("channel") == null) {
                this.reply(message, "You haven't yet set the channel for me to send the MOTD message to.");
            }
        }
    }

}
