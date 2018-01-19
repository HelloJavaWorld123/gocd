/*
 * Copyright 2018 ThoughtWorks, Inc.
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
 */

package com.thoughtworks.go.apiv1.admin.security.representers

import com.thoughtworks.go.api.mocks.TestRequestContext
import com.thoughtworks.go.api.util.GsonTransformer
import com.thoughtworks.go.config.PluginRoleConfig
import com.thoughtworks.go.domain.config.ConfigurationKey
import com.thoughtworks.go.domain.config.ConfigurationProperty
import com.thoughtworks.go.domain.config.ConfigurationValue
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

class PluginRoleConfigRepresenterTest {
  private final LinkedHashMap<String, Object> map = [
    _links    : [
      doc : [href: 'https://api.gocd.org/#roles'],
      self: [href: 'http://test.host/go/api/admin/security/roles/blackbird'],
      find: [href: 'http://test.host/go/api/admin/security/roles/:role_name']
    ],
    name      : 'blackbird',
    type      : 'plugin',
    attributes: [
      auth_config_id: "ldap",
      properties: [
        [
          key: "UserGroupMembershipAttribute",
          value: "memberOf"
        ],
        [
          key: "GroupIdentifiers",
          value: "ou=admins,ou=groups,ou=system,dc=example,dc=com"
        ]
      ]
    ]
  ]

  private final PluginRoleConfig roleConfig = new PluginRoleConfig("blackbird", "ldap",
    new ConfigurationProperty(new ConfigurationKey("UserGroupMembershipAttribute"), new ConfigurationValue("memberOf")),
    new ConfigurationProperty(new ConfigurationKey("GroupIdentifiers"), new ConfigurationValue("ou=admins,ou=groups,ou=system,dc=example,dc=com")))

  @Test
  void shouldGenerateJSON() {
    Map map = RoleRepresenter.toJSON(roleConfig, new TestRequestContext());

    assertThat(map).isEqualTo(this.map)
  }

  @Test
  void shouldBuildObjectFromJson() {
    def jsonReader = GsonTransformer.instance.jsonReaderFrom(map)
    def roleConfig = RoleRepresenter.fromJSON(jsonReader)
    assertThat(roleConfig).isEqualTo(this.roleConfig)
  }
}
