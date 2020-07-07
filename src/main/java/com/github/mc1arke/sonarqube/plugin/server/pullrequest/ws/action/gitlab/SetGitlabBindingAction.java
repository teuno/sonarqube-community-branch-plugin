/*
 * Copyright (C) 2020 Michael Clarke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.github.mc1arke.sonarqube.plugin.server.pullrequest.ws.action.gitlab;

import com.github.mc1arke.sonarqube.plugin.server.pullrequest.ws.action.SetBindingAction;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.db.DbClient;
import org.sonar.db.alm.setting.ProjectAlmSettingDto;
import org.sonar.server.component.ComponentFinder;
import org.sonar.server.user.UserSession;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SetGitlabBindingAction extends SetBindingAction {
    private static final Logger LOGGER = Loggers.get(SetGitlabBindingAction.class);

    public SetGitlabBindingAction(DbClient dbClient, ComponentFinder componentFinder, UserSession userSession) {
        super(dbClient, componentFinder, userSession, "set_gitlab_binding");
    }

    @Override
    protected void configureAction(WebService.NewAction action) {
        super.configureAction(action);

        action.createParam("repository").setRequired(true);
    }

    @Override
    protected ProjectAlmSettingDto createProjectAlmSettingDto(String projectUuid, String settingsUuid,
                                                              Request request) {
        LOGGER.error(String.join(",", request.getParams().keySet()));

        return new ProjectAlmSettingDto()
                .setProjectUuid(projectUuid)
                .setAlmSettingUuid(settingsUuid)
                .setAlmRepo(request.mandatoryParam("repository"));
    }
}
