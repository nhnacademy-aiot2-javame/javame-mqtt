package com.example.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConnectService {
    static final Logger logger = LoggerFactory.getLogger(ConnectService.class);
    private MqttClient client;
    private MqttConnectOptions options;

    public ConnectService() {
        this.options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(60);
        options.setConnectionTimeout(30);
    }

    // MQTT 브로커에 연결하고 TOPIC을 구독하는 메소드
    public MqttClient connectToBrokerAndSubscribe(String broker, String clientId, String topic) throws MqttException {
        this.client = new MqttClient(broker, clientId);
        
        while (true) {
            try {
                if (!client.isConnected()) {
                    logger.info("Connecting to broker...");
                    client.connect(options);
                    logger.info("Connected to broker!");

                    // 연결 후, 주어진 토픽을 구독
                    client.subscribe(topic);
                    logger.info("Subscribed to topic: {}", topic);
                    break;
                }
            } catch (MqttException e) {
                logger.error("Error connecting to broker: {}", e.getMessage());
                try {
                    Thread.sleep(5000); // 5초 대기 후 재시도
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return client; // 연결된 client 반환
    }

    // 연결 후, 주어진 토픽을 구독하는 메소드
    public void subscribeToTopic(MqttClient client, String topic) throws MqttException {
        client.subscribe(topic);
        logger.info("Subscribed to topic: {}", topic);
    }

    // 연결 종료 메서드
    public void disconnect() throws MqttException {
        if (client.isConnected()) {
            client.disconnect(); // 연결 안전 종료
            client.close();     // 메모리 수거
            logger.info("Disconnected from broker.");
        }
    }
}
