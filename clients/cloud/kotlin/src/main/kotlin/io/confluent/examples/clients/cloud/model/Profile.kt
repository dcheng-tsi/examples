package io.confluent.examples.clients.cloud.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.confluent.kafka.serializers.KafkaJsonDeserializer
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Profile(
    @JsonProperty("profileId")
    val profileId: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("ssnDue")
    val ssnDue: Boolean = false
)

fun profileSerde(): Serde<Profile> {

    val properties = hashMapOf("json.value.type" to Profile::class.java)

    val dRSerializer = KafkaJsonSerializer<Profile>()
    dRSerializer.configure(properties, false)

    val dRDeserializer = KafkaJsonDeserializer<Profile>()
    dRDeserializer.configure(properties, false)

    return Serdes.serdeFrom(dRSerializer, dRDeserializer)
}