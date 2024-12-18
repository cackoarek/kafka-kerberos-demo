package pl.cackoa.kafka.avro;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.util.Properties;

public class KafkaKerberosAvroProducer {
    public static void main(String[] args) {
        String topic = System.getenv("KAFKA_TOPIC");
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVERS"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, System.getenv("SCHEMA_REGISTRY_URL"));

        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("sasl.kerberos.service.name", "kafka");

        // Avro definition
        String userSchema = "{"
                + "\"type\":\"record\","
                + "\"name\":\"User\","
                + "\"fields\":["
                + "{\"name\":\"name\",\"type\":\"string\"},"
                + "{\"name\":\"age\",\"type\":\"int\"}"
                + "]"
                + "}";
        Schema schema = new Schema.Parser().parse(userSchema);

        Producer<String, GenericRecord> producer = new KafkaProducer<>(props);

        GenericRecord user = new GenericData.Record(schema);
        user.put("name", "John");

        try {
            int i = 0;
            while (true) {
                user.put("age", i);
                producer.send(new ProducerRecord<>(topic, "key" + i, user));
                System.out.println("Message sent. " + user);
                Thread.sleep(50);
                i++;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            producer.close();
        }
    }
}
