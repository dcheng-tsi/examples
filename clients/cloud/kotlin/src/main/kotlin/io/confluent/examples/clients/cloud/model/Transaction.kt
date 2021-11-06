package io.confluent.examples.clients.cloud.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import com.google.gson.Gson
import io.confluent.kafka.serializers.KafkaJsonDeserializer
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

val gson = Gson()

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Transaction(
    @JsonProperty("payout")
    val payout: Int = 0,
    @JsonProperty("profileId")
    val profileId: String,
    @JsonProperty("date")
    val year: Int = 2021
) {
    override fun toString(): String {
        return gson.toJson(this)
    }
}

fun transactionSerde(): Serde<Transaction> {

    val properties = hashMapOf("json.value.type" to Transaction::class.java)

    val dRSerializer = KafkaJsonSerializer<Transaction>()
    dRSerializer.configure(properties, false)

    val dRDeserializer = KafkaJsonDeserializer<Transaction>()
    dRDeserializer.configure(properties, false)

    return Serdes.serdeFrom(dRSerializer, dRDeserializer)
}