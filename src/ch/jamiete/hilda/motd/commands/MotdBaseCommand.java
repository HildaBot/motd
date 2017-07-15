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
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.core.Permission;

public class MotdBaseCommand extends ChannelSeniorCommand {
    private MotdPlugin plugin;

    public MotdBaseCommand(Hilda hilda, MotdPlugin plugin) {
        super(hilda);

        this.plugin = plugin;

        this.setName("motd");
        this.setDescription("Manages the motd system.");
        this.setMinimumPermission(Permission.MANAGE_SERVER);

        this.registerSubcommand(new MotdChannelCommand(hilda, this, plugin));
        this.registerSubcommand(new MotdCurrentCommand(hilda, this, plugin));
        this.registerSubcommand(new MotdMessageCommand(hilda, this, plugin));
        this.registerSubcommand(new MotdResetCommand(hilda, this, plugin));
    }

    public MotdPlugin getPlugin() {
        return this.plugin;
    }

}
