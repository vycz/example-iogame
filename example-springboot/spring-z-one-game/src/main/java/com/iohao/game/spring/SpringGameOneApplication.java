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
package com.iohao.game.spring;

import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.simple.SimpleRunOne;
import com.iohao.game.spring.broker.GameBrokerBoot;
import com.iohao.game.spring.external.GameExternal;
import com.iohao.game.spring.logic.classes.GameLogicClassesClient;
import com.iohao.game.spring.logic.school.GameLogicSchoolClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 综合示例一键启动类
 * <p>
 * 示例涉及如下知识点
 * <pre>
 * JSR380
 * 断言 + 异常机制 = 清晰简洁的代码
 *
 * 请求、无响应
 * 请求、响应
 *
 * 广播指定玩家
 * 广播全服玩家
 *
 * 单个逻辑服与单个逻辑服通信请求 - 有返回值（可跨进程）
 * 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）
 * 单个逻辑服与同类型多个逻辑服通信请求（可跨进程）
 *
 * 游戏文档生成
 * 业务.proto文件的生成
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-09
 */
@SpringBootApplication
public class SpringGameOneApplication {

    public static void main(String[] args) {

        // 启动 spring boot
        SpringApplication.run(SpringGameOneApplication.class, args);

        // 注意，这个是临时测试用的，设置为 false 表示不用登录就可以访问逻辑服的方法
        ExternalGlobalConfig.verifyIdentity = false;

        // 游戏逻辑服列表
        List<AbstractBrokerClientStartup> logicList = List.of(
                // 学校逻辑服
                new GameLogicSchoolClient(),
                // 班级逻辑服
                new GameLogicClassesClient()
        );

        // 对外开放的端口
        int externalPort = 10100;
        // 游戏对外服
        ExternalServer externalServer = new GameExternal().createExternalServer(externalPort);

        // broker （游戏网关）
        BrokerServer brokerServer = new GameBrokerBoot().createBrokerServer();

        // 多服单进程的方式部署（类似单体应用）
        new SimpleRunOne()
                // broker （游戏网关）
                .setBrokerServer(brokerServer)
                // 游戏对外服
                .setExternalServer(externalServer)
                // 游戏逻辑服列表
                .setLogicServerList(logicList)
                // 启动 游戏对外服、游戏网关、游戏逻辑服
                .startup();

        // spring 集成 https://www.yuque.com/iohao/game/evkgnz
    }

    @Bean
    public ActionFactoryBeanForSpring actionFactoryBean() {
        // 将业务框架交给 spring 管理
        return ActionFactoryBeanForSpring.me();
    }
}
