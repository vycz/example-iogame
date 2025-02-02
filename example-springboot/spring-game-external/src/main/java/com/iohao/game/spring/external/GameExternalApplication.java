/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.spring.external;

import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;

/**
 * 单独启动类：游戏对外服
 *
 * @author 渔民小镇
 * @date 2022-07-09
 */
public class GameExternalApplication {
    public static void main(String[] args) {
        // 注意，这个是临时测试用的，设置为 false 表示不用登录就可以访问逻辑服的方法
        ExternalGlobalConfig.verifyIdentity = false;

        // 对外开放的端口
        int externalPort = 10100;

        // 构建游戏对外服
        ExternalServer externalServer = new GameExternal().createExternalServer(externalPort);

        // 启动游戏对外服
        externalServer.startup();
    }
}
