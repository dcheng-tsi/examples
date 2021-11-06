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
@file:JvmName("TransactionConsumer")

package io.confluent.examples.clients.cloud

import io.confluent.examples.clients.cloud.config.KafkaTopicConfig
import io.confluent.examples.clients.cloud.model.Transaction
import io.confluent.examples.clients.cloud.util.loadConfig
import io.confluent.kafka.serializers.KafkaJsonDeserializer
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig.JSON_VALUE_TYPE
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration.ofMillis
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    if (args.size != 2) {
        println("Please provide command line arguments: configPath topic")
        exitProcess(1)
    }

    val topic = KafkaTopicConfig.transactionTopic

    // Load properties from disk.
    val props = loadConfig(args[0])

    // Add additional properties.
    props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    props[VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaJsonDeserializer::class.java.name
    props[JSON_VALUE_TYPE] = Transaction::class.java
    props[GROUP_ID_CONFIG] = "kotlin_example_group_1"
    props[AUTO_OFFSET_RESET_CONFIG] = "earliest"

    val consumer = KafkaConsumer<String, Transaction>(props).apply {
        subscribe(listOf(topic))
    }

    var totalPayout = 0.0F

    consumer.use {
        while (true) {
            totalPayout = consumer
                .poll(ofMillis(100))
                .fold(totalPayout) { accumulator, record ->
                    val aggregatePayout = accumulator + record.value().payout
                    println("Consumed record with key ${record.key()} and value ${record.value()}, and updated total payout to $aggregatePayout")
                    aggregatePayout
                }
        }
    }
}

/*
 ./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.TransactionConsumer" \
     -PconfigPath="$HOME/.confluent/java.config"\
     -Ptopic="test_transaction"
 */