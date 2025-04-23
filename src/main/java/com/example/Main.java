package com.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.mqtt.Config;
import com.example.mqtt.ConnectService;
import com.example.mqtt.InfluxDBService;
import com.example.mqtt.MqttClientService;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static ConnectService connectService;
    private static InfluxDBService influxDBService;

    public static void main(String[] args) {
        try {

            influxDBService = new InfluxDBService();

            // ConnectService를 사용하여 연결하고 구독
            connectService = new ConnectService();
            MqttClient client = connectService.connectToBrokerAndSubscribe(Config.BROKER, Config.CLIENT_ID, Config.TOPIC);

            // MqttClientService로 메시지 처리용 콜백 설정
            new MqttClientService(client, influxDBService); // 콜백을 설정하는 부분만 남기기

            logger.info("Connected to broker and subscribed to topic: {}", Config.TOPIC);

            // Main 애플리케이션 종료 전에 MQTT 연결을 계속 유지
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("Exiting and disconnecting...");
                    connectService.disconnect(); // 연결 종료만 처리
                } catch (MqttException e) {
                    logger.error("Error during disconnect: {}", e.getMessage());
                }
            }));

        } catch (MqttException e) {
            logger.error("Error starting MQTT client: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
