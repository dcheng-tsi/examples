/*
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
@file:JvmName("StreamsExample")

package io.confluent.examples.clients.cloud

import io.confluent.examples.clients.cloud.config.KafkaTopicConfig
import io.confluent.examples.clients.cloud.model.Profile
import io.confluent.examples.clients.cloud.model.ProfileUpdate
import io.confluent.examples.clients.cloud.model.Transaction
import io.confluent.examples.clients.cloud.model.profileSerde
import io.confluent.examples.clients.cloud.model.profileUpdateSerde
import io.confluent.examples.clients.cloud.model.transactionSerde
import io.confluent.examples.clients.cloud.util.loadConfig
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG
import org.apache.kafka.streams.StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG
import org.apache.kafka.streams.StreamsConfig.REPLICATION_FACTOR_CONFIG
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Joined
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.kstream.Produced

val threshold: Int = 60000

fun main(args: Array<String>) {

    if (args.size != 1) {
        println("Please provide command line arguments: <configPath>")
        System.exit(1)
    }

    // Load properties from disk.
    val props = loadConfig(args[0])
    props[APPLICATION_ID_CONFIG] = "kotlin_streams_example_group_1"
    // Disable caching to print the aggregation value after each record
    props[CACHE_MAX_BYTES_BUFFERING_CONFIG] = 0
    props[REPLICATION_FACTOR_CONFIG] = 1
    props[AUTO_OFFSET_RESET_CONFIG] = "earliest"

    val builder = StreamsBuilder()
    val transactionStream =
        builder.stream(KafkaTopicConfig.transactionTopic, Consumed.with(Serdes.String(), transactionSerde()))

    val transactionRekeyed = transactionStream.map { _, v -> KeyValue(constructKey(v), v) }
    transactionRekeyed.print(Printed.toSysOut<String, Transaction>().withLabel("Consumed record"))

    // Aggregate values by key
    val transactionAggPerYearPerPerson = transactionRekeyed
        .groupByKey(Grouped.with(Serdes.String(), transactionSerde()))
        .aggregate(
            { 0 },
            { _: String, newValue: Transaction, aggValue: Int -> aggValue + newValue.payout },
            Materialized.with(Serdes.String(), Serdes.Integer())
        )
        .toStream()
        .peek{k, v -> println("For transaction aggregation, key (profileId::year) is $k and total payout is $v")}
    val transactionAggNeededForSSN = transactionAggPerYearPerPerson.filter { _, v ->
        v >= threshold
    }

    val transactionAggNeededForSSNRekey = transactionAggNeededForSSN.map { k, v ->
        val profileId = getProfileIdFromKey(k)
        KeyValue(
            profileId, v
        )
    }

    val profileTable = builder.table(
        KafkaTopicConfig.profileTopic,
        Consumed.with(Serdes.String(), profileSerde()),
        Materialized.with(Serdes.String(), profileSerde())
    )

    val profileUpdateStream = transactionAggNeededForSSNRekey.join(
        profileTable,
        { _: Int, profile: Profile ->
            ProfileUpdate(profile.copy(ssnDue = true), ProfileUpdate.Action.UPDATE_SSN)
        },
        Joined.with(Serdes.String(), Serdes.Integer(), profileSerde())
            .withName("join-and-filter-profile-needed-ssn")
    ).peek{k, v -> println("For profileUpdateStream, key (profileId) is $k and updated profile is $v")}

    profileUpdateStream.to(KafkaTopicConfig.profileUpdateTopic, Produced.`with`(Serdes.String(), profileUpdateSerde()))

    val streams = KafkaStreams(builder.build(), props)
    streams.start()

    // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { streams.close() }))

}

fun constructKey(transaction: Transaction): String =
    "${transaction.profileId}::${transaction.year}"


fun getProfileIdFromKey(key: String): String =
    key.split("::")[0]
