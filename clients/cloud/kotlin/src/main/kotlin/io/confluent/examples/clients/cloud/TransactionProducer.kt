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
@file:JvmName("TransactionProducer")

package io.confluent.examples.clients.cloud

import io.confluent.examples.clients.cloud.config.KafkaTopicConfig
import io.confluent.examples.clients.cloud.model.Transaction
import io.confluent.examples.clients.cloud.util.createTopic
import io.confluent.examples.clients.cloud.util.loadConfig
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.util.UUID
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val profileId = "820a8627-5da3-40c5-b5a7-ec39e5678719"
    val payoutAmount = 20000
    val year = 2021

    if (args.size != 1) {
        println("Please provide command line arguments: configPath")
        exitProcess(1)
    }

    // Load properties from file
    val props = loadConfig(args[0])

    val topic = KafkaTopicConfig.transactionTopic
    // Add additional properties.
    props[ACKS_CONFIG] = "all"
    props[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    props[VALUE_SERIALIZER_CLASS_CONFIG] = KafkaJsonSerializer::class.qualifiedName

    KafkaProducer<String, Transaction>(props).use { producer ->
        val key = UUID.randomUUID().toString()
        val record = Transaction(profileId = profileId, payout = payoutAmount, year = year)
        println("Producing record: $key\t$record")

        producer.send(ProducerRecord(topic, key, record)) { m: RecordMetadata, e: Exception? ->
            when (e) {
                // no exception, good to go!
                null -> println("Produced record to topic ${m.topic()} partition [${m.partition()}] @ offset ${m.offset()}")
                // print stacktrace in case of exception
                else -> e.printStackTrace()
            }
        }


        producer.flush()
        println("1 messages were produced to topic $topic")
    }

}