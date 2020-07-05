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
package com.github.liyue2008.rpc.transport;

import com.github.liyue2008.rpc.transport.Transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 *在java.io.包下 InputStream，outputStream, Reader, Writer 等基类都实现了Closeable接口，因为每次的IO操作结束之后都要去释放资源。
 *
 * 1： 如果在调用此方法之前 stream已经关闭 ，则方法失效；
 * 2：建议先关闭内部的资源，并标记为已关闭；
 * 3：优先抛出IO异常；
 *
 *
 * 比如AutoCloseableObjecct 实现Closeable
 * 那么代码中就可以在try使用该对象，close会自动调用
 * 		try(AutoCloseableObjecct app = new AutoCloseableObjecct()){
 *
 *                }catch (Exception e) {
 *        }
 * @author LiYue
 * Date: 2019/9/25
 */
public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException;
    @Override
    void close();
}
