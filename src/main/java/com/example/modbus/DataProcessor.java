package com.example.modbus;

import org.eclipse.paho.client.mqttv3.MqttException;

public class DataProcessor {
    private MqttPublisher mqttPublisher;
    private static final String TOPIC = "data/#";

    // MQTT Publisher 객체 초기화
    public DataProcessor() throws MqttException {
        mqttPublisher = new MqttPublisher("tcp://192.168.70.203:1883", "JavaClientExample");
    }

    // 레지스터 데이터를 처리하고 MQTT로 전송
    public void processAndSendData(int address, String name, int value) {
        // 예: 온도 값 처리
        String message = name + " (" + address + "): " + value;
        // 처리된 데이터를 MQTT로 전송
        mqttPublisher.publishData(TOPIC, message);
    }

    // 종료 시 MQTT 연결 해제
    public void close() {
        mqttPublisher.disconnect();
    }
}
