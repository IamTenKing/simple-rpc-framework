/**
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
package com.github.liyue2008.rpc.server;

import com.github.liyue2008.rpc.NameService;
import com.github.liyue2008.rpc.RpcAccessPoint;
import com.github.liyue2008.rpc.hello.HelloService;
import com.github.liyue2008.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String [] args) throws Exception {

        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动RpcAccessPoint...");
        try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
            //开启netty server,监听端口
            Closeable ignored = rpcAccessPoint.startServer()) {
            //注册中心启动
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            //实际上就是将hello服务实例put到server的一个map里面
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            logger.info("服务名: {}, 向NameService注册...", serviceName);
            //nameSevice可以有多个实现类，用spi动态加载实现类，目前只有本地文件实现的方式
            nameService.registerService(serviceName, uri);
            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }

}
