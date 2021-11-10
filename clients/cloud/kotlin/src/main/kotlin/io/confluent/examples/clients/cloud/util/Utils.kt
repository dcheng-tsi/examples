package io.confluent.examples.clients.cloud.util

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.errors.TopicExistsException
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ExecutionException

fun loadConfig(configFile: String) = FileInputStream(configFile).use {
  Properties().apply {
    load(it)
  }
}

// Create topic in Confluent Cloud
fun createTopic(
  topic: String,
  partitions: Int,
  replication: Short,
  cloudConfig: Properties
) {
  val newTopic = NewTopic(topic, partitions, replication)

  try {
    with(AdminClient.create(cloudConfig)) {
      createTopics(listOf(newTopic)).all().get()
    }
  } catch (e: ExecutionException) {
    if (e.cause !is TopicExistsException) throw e
  }
}