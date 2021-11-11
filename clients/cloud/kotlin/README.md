= Overview

Produce messages to and consume messages from a Kafka cluster using the Kotlin
version of Java Producer and Consumer, and Kafka Streams API.

= Documentation

You can find the documentation and instructions for running this Kotlin example
at
https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/kotlin.html?utm_source=github&utm_medium=demo&utm_campaign=ch.examples_type.community_content.clients-ccloud[https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/kotlin.html]

---
Forked version setup guide

This sample project is intended to demonstrate stream producer, consumer, stream app, kafkacat and ksql capability. If you have any setup related question that is not answered here, please use the link above for help.

Here is the background of the example. You are running a used item resale app, and IRS require user who made more than $600 per year have their tax record submitted

Once you clone this repo, here are the steps you need to go through the flow:

Stream app demo flow:
0. Set config in here `$HOME/.confluent/java.config` to
```
# Kafka
bootstrap.servers=localhost:9092
```
1. Go to the root folder and call `docker-compose up -d` to start up the suites of docker containers for kafka
2. set java home directory, use java 11:

2a. Run `/usr/libexec/java_home -V` to check if you have Java 11 installed

2b. set java 11 by running ```export JAVA_HOME=`/usr/libexec/java_home -v 11` ```

3. Create kafka topic needed for the application by running:
```
./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.CreateTopics"\
     -PconfigPath="$HOME/.confluent/java.config"
```
4. Run kafkacat to produce profile result `kafkacat -P -b localhost:9092 -t profile -K$ -T` with payload
```
820a8627-5da3-40c5-b5a7-ec39e5678719${"profileId": "820a8627-5da3-40c5-b5a7-ec39e5678719", "name": "profile1", "ssnDue": false}
```
5. You can run kafkacat in consumer mode to verify the message: `kafkacat -b localhost:9092 -t profile -K$ -T`
6. Run transaction producer to create a $400 (as 40000 integer) transaction and a $100 transaction.
You can change the transaction year, amount and profileId in the const val at front.
```
./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.TransactionProducer"\
     -PconfigPath="$HOME/.confluent/java.config"
```
7. Run transaction consumer to generate the sum:
```
 ./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.TransactionConsumer" \
     -PconfigPath="$HOME/.confluent/java.config"
```
8. Run stream example to check 60000 mark:
```
./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.StreamsExample" \
     -PconfigPath="$HOME/.confluent/java.config"
```
9. In another command line session, run transaction producer to create a $200 (as 20000 integer) transaction.
   You can change the transaction year, amount and profileId in the const val at front.
   You should see the stream sample processed the new message and send out the update
```
./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.TransactionProducer"\
     -PconfigPath="$HOME/.confluent/java.config"
```
10. Double check using kafkacat `kafkacat -b localhost:9092 -t profileUpdate -K$ -T`
----
Ksql demo flow:
1. Get into Ksql cli:
```
docker exec -it ksqldb-cli ksql http://ksqldb-server:8088
```
2. Run ksql setup script here:
```
 CREATE TABLE profileTable (
 Id String PRIMARY KEY,
 profileId STRING,
 name STRING,
 ssnDue BOOLEAN
) WITH (
 KAFKA_TOPIC = 'profile',
 VALUE_FORMAT = 'JSON'
);

CREATE TABLE transactionTable (
Id String PRIMARY KEY,
profileId STRING,
payout INT,
year INT
) WITH (
KAFKA_TOPIC = 'transaction',
VALUE_FORMAT = 'JSON'
);

CREATE TABLE profileNewUpdateWithNeeded WITH (KEY_FORMAT='JSON') AS
 SELECT profileId, year, SUM(ts.payout) as totalAnnualPayout, true as ssnDue
 FROM transactionTable ts
 GROUP BY year,profileId
 HAVING SUM(ts.payout) > 60000;

 Create TABLE profileUpdate WITH (KEY_FORMAT='JSON') AS
 SELECT pnuwn.profileId as profileId, pnuwn.year as year, pt.name as name, pnuwn.ssnDue as ssnDue
 FROM profileNewUpdateWithNeeded pnuwn
 JOIN profileTable pt on pt.Id = pnuwn.profileId;
```
3. Now you can check ksql in the cli for the result, which should match with the stream app:
```
SELECT * FROM profileUpdate emit changes;
```