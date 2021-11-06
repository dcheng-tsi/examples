package io.confluent.examples.clients.cloud.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.confluent.kafka.serializers.KafkaJsonDeserializer
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProfileUpdate(
    @JsonProperty("profile")
    val profile: Profile,
    @JsonProperty("action")
    val action: Action
) {
    enum class Action{
        CREATE,
        UPDATE_SSN
    }
}

fun profileUpdateSerde(): Serde<ProfileUpdate> {

    val properties = hashMapOf("json.value.type" to ProfileUpdate::class.java)

    val dRSerializer = KafkaJsonSerializer<ProfileUpdate>()
    dRSerializer.configure(properties, false)

    val dRDeserializer = KafkaJsonDeserializer<ProfileUpdate>()
    dRDeserializer.configure(properties, false)

    return Serdes.serdeFrom(dRSerializer, dRDeserializer)
}