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
import org.apache.commons.lang3.StringUtils;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.core.entities.Message;

public class MotdMessageCommand extends ChannelSubCommand {
    private MotdPlugin plugin;

    protected MotdMessageCommand(Hilda hilda, ChannelSeniorCommand senior, MotdPlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("message");
        this.setAliases(Arrays.asList(new String[] { "msg" }));
        this.setDescription("Sets the message to send when someone joins.");
    }

    @Override
    public void execute(Message message, String[] arguments, String label) {
        Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getGuild().getId());

        if (arguments.length == 0) {
            cfg.get().remove("message");
            cfg.save();
            this.reply(message, "Removed the MOTD message!");
        } else {
            String msg = message.getRawContent();

            msg = StringUtils.removeAll(msg, "\\!\\w+ \\w+ ");

            cfg.get().addProperty("message", msg);
            cfg.save();

            this.reply(message, "Okay, I'll now send this when someone joins:");
            this.reply(message, msg);

            if (cfg.get().get("channel") == null) {
                this.reply(message, "You haven't yet set the channel for me to send the MOTD message to.");
            }
        }
    }

}
