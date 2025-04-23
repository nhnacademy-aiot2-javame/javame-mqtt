package com.example.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientService implements MqttCallback {
    static final Logger logger = LoggerFactory.getLogger(MqttClientService.class);

    private MqttClient client;
    private MessageData messageData;
    private InfluxDBService influxDBService;

    // MqttClient 객체를 생성자에서 받아옵니다.
    public MqttClientService(MqttClient client, InfluxDBService influxDBService) {
        this.client = client;
        this.influxDBService = influxDBService;
        this.messageData = new MessageData(influxDBService); // MessageData 인스턴스를 생성
        client.setCallback(this); // 콜백 설정
    }

    // MQTT 메시지가 도착했을 때 호출되는 메서드
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 메시지를 MessageData로 전달하여 로그만 기록
        messageData.handleMessageArrived(topic, message);
    }

    // 메시지 전달이 완료되었을 때 호출되는 메서드
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("Message delivery complete: {}", token.getMessageId());
    }

    // 연결이 끊어졌을 때 호출되는 메서드
    @Override
    public void connectionLost(Throwable cause) {
        logger.error("Connection lost: {}", cause.getMessage(), cause);
    }
}
