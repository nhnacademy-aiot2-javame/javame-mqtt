package com.example.modbus;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublisher {
    static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    private MqttClient client;

    // MQTT 클라이언트 초기화
    public MqttPublisher(String brokerUrl, String clientId) throws MqttException {
        client = new MqttClient(brokerUrl, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    // 데이터 전송 메서드
    public void publishData(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);  // QoS 설정 (1은 최소 한번 전송 보장)
            client.publish(topic, mqttMessage);
            logger.info("Message published: {}", message);
        } catch (MqttException e) {
            logger.error("Failed to publish message: {}", e.getMessage());
        }
    }

    // MQTT 클라이언트 연결 해제
    public void disconnect() {
        try {
            client.disconnect();
            logger.info("Disconnected from the MQTT broker.");
        } catch (MqttException e) {
            logger.error("Failed to disconnect: {}", e.getMessage());
        }
    }
}
