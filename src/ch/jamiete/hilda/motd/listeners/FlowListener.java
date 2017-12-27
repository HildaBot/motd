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

import ch.jamiete.hilda.Util;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.events.EventHandler;
import ch.jamiete.hilda.motd.MotdPlugin;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

public class FlowListener {
    public static String compute(String message, final Member member) {
        message = message.replaceAll("\\$mention", member.getAsMention());
        message = message.replaceAll("\\$username", Util.sanitise(member.getUser().getName()));
        message = message.replaceAll("\\$effective", Util.sanitise(member.getEffectiveName()));
        message = message.replaceAll("\\$discriminator", member.getUser().getDiscriminator());
        message = message.replaceAll("\\$id", member.getUser().getId());

        return message;
    }

    private final MotdPlugin plugin;

    public FlowListener(final MotdPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {
        final Configuration cfg = this.plugin.getHilda().getConfigurationManager().getConfiguration(this.plugin, event.getGuild().getId());

        final String motd = cfg.getString("motd", null);
        final String chan = cfg.getString("channel", null);

        if (motd == null || chan == null) {
            return;
        }

        final TextChannel channel = event.getGuild().getTextChannelById(chan);

        if (channel == null) {
            return;
        }

        new MessageBuilder().append(FlowListener.compute(motd, event.getMember())).buildAll(SplitPolicy.SPACE).forEach(m -> channel.sendMessage(m).queue());
    }

}
