/*
 * This file is part of HuskHomes, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.huskhomes.command;

import net.william278.huskhomes.HuskHomes;
import net.william278.huskhomes.manager.RequestsManager;
import net.william278.huskhomes.user.CommandUser;
import net.william278.huskhomes.user.OnlineUser;
import net.william278.huskhomes.user.SavedUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class TpToggleCommand extends InGameCommand {

    /**
     * Permission node that allows a user to bypass another player's teleport toggle.
     */
    public static final String BYPASS_PERMISSION = "huskhomes.command.tptoggle.bypass";

    protected TpToggleCommand(@NotNull HuskHomes plugin) {
        super(
                List.of("tptoggle"),
                "",
                plugin
        );
        addAdditionalPermissions(Map.of("bypass", true));
    }

    /**
     * Returns whether the given user may bypass another player's teleport toggle.
     *
     * @param user the user to check
     * @return {@code true} if the user may bypass the toggle
     */
    public static boolean canBypass(@NotNull CommandUser user) {
        return user.hasPermission(BYPASS_PERMISSION);
    }

    @Override
    public void execute(@NotNull OnlineUser onlineUser, @NotNull String[] args) {
        final RequestsManager manager = plugin.getManager().requests();
        final boolean acceptingTeleports = !manager.isAcceptingTeleports(onlineUser);

        plugin.editSavedUser(onlineUser, (SavedUser user) -> user.setAcceptingTeleports(acceptingTeleports));

        plugin.getLocales().getRawLocale("tptoggle_toggle_" + (acceptingTeleports ? "off" : "on"),
                        plugin.getLocales().getRawLocale("tptoggle_toggle_button").orElse(""))
                .map(plugin.getLocales()::format)
                .ifPresent(onlineUser::sendMessage);
    }
}
