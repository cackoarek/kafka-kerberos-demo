```shell
export SCHEMA_REGISTRY_URL="http://host1:7788"
export BOOTSTRAP_SERVERS="broker1:9092,broker2:9092,broker3:9092"
export KAFKA_TOPIC="avro-topic"
```

Run commands:

Producer:
```shell
java -Djava.security.auth.login.config=/etc/jaas.conf -Djava.security.krb5.conf=/etc/krb5.conf -cp kerberos-kafka-1.0-SNAPSHOT-jar-with-dependencies.jar pl.cackoa.kafka.avro.KafkaKerberosAvroProducer
```

Consumer:
```shell
java -Djava.security.auth.login.config=/etc/jaas.conf -Djava.security.krb5.conf=/etc/krb5.conf -cp kerberos-kafka-1.0-SNAPSHOT-jar-with-dependencies.jar pl.cackoa.kafka.avro.KafkaKerberosAvroConsumer
```