/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.book.ch4;

import java.io.File;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBStore;
/**
 * 代码的方式启动Broker  
 * 采用KahaDB的方式存储消息
 */
public class KahaDBEmbeddedBroker {
	
	public static void main(String[] args) throws Exception {
		new KahaDBEmbeddedBroker().createEmbeddedBroker();
	}

	public void createEmbeddedBroker() throws Exception {
		//创建BrokerService对象	
		BrokerService broker = new BrokerService();
		//指定消息保存的路径
		File dataFileDir = new File("target/amq-in-action/kahadb");
		//创建KahaDB存储对象
		KahaDBStore kaha = new KahaDBStore();
		//设置存储路劲
		kaha.setDirectory(dataFileDir);
		// 设置日志文件（即存储消息的文件）的最大长度
		kaha.setJournalMaxFileLength(1024 * 1204 * 100);
		/*
		 * 当Metadata Cache中更新的索引到达了100时，才同步到磁盘上的Metadata Store中。
		 * Metadata Cache:内存当中B-Tree结构的索引
		 * Metadata Store:磁盘中B-Tree结构的索引，对应db.data文件
		 */
		kaha.setIndexWriteBatchSize(100);
		// 索引是否异步写入磁盘文件当中
		kaha.setEnableIndexWriteAsync(true);
		//给broker对象设置消息存储的方式
		broker.setPersistenceAdapter(kaha);
		// 添加协议连接器
		broker.addConnector("tcp://localhost:61616");
		// 开启Broker
		broker.start();
	}
}
