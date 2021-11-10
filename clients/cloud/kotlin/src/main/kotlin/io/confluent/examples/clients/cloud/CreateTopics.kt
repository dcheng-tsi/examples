/**
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("CreateTopics")

package io.confluent.examples.clients.cloud

import io.confluent.examples.clients.cloud.config.KafkaTopicConfig
import io.confluent.examples.clients.cloud.model.Transaction
import io.confluent.examples.clients.cloud.util.createTopic
import io.confluent.examples.clients.cloud.util.loadConfig
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Please provide command line arguments: configPath")
        exitProcess(1)
    }

    // Load properties from file
    val props = loadConfig(args[0])

    createTopic(KafkaTopicConfig.transactionTopic, 1, 1, props)
    createTopic(KafkaTopicConfig.profileTopic, 1, 1, props)
    createTopic(KafkaTopicConfig.profileUpdateTopic, 1, 1, props)
    println("All three topics created")
}