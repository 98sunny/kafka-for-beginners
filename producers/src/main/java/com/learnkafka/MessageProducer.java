package com.learnkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageProducer {
    private static Logger logger= LoggerFactory.getLogger(MessageProducer.class);
    String topicName="test-topic";
    KafkaProducer<String, String> kafkaProducer;

    public MessageProducer(Map<String, Object> propsMap) {
        kafkaProducer = new KafkaProducer<String, String>(propsMap);
    }
    public static Map<String, Object> propsMaps(){
        Map<String, Object> propsMap=new HashMap<>();
        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return propsMap;
    }
    public void publishMessageSync(String key, String value) {
        ProducerRecord<String,String> producerRecord=new ProducerRecord<>(topicName, key,value);
        try{
            RecordMetadata recordMetadata=kafkaProducer.send(producerRecord).get();
//            System.out.print(recordMetadata);
//            System.out.print("Partition--> "+recordMetadata.partition()+"     and Offset-->"+recordMetadata.offset());
            logger.info("Message sent {} successfully for the key {}",value,key);
            logger.info("Published Message offset is {} and the Partition is {}",recordMetadata.offset(),recordMetadata.partition() );
        }catch(Exception e){
            logger.error("Exception is {}",e.getMessage());
        }


    }

    public static void main(String[] args) {
        MessageProducer messageProducer=new MessageProducer(propsMaps());
        messageProducer.publishMessageSync(null, "CDE");
    }
}
