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
package ch.jamiete.hilda.motd.listeners;

import com.google.gson.JsonElement;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class FlowListener extends ListenerAdapter {
    private MotdPlugin plugin;

    public FlowListener(MotdPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {
        Configuration cfg = this.plugin.getHilda().getConfigurationManager().getConfiguration(this.plugin, event.getGuild().getId());

        JsonElement motd = cfg.get().get("message");
        JsonElement chan = cfg.get().get("channel");

        if (motd == null || chan == null) {
            return;
        }

        TextChannel channel = event.getGuild().getTextChannelById(chan.getAsString());

        if (channel == null) {
            return;
        }

        channel.sendMessage(motd.getAsString().replace("@user", event.getMember().getAsMention())).queue();
    }

}
