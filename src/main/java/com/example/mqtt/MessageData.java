package com.example.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageData {
    private static final Logger logger = LoggerFactory.getLogger(MessageData.class);
    private final InfluxDBService influxDBService;

    public MessageData(InfluxDBService influxDBService) {
        this.influxDBService = influxDBService;
    }

    // 메시지 수신 처리 메서드
    public void handleMessageArrived(String topic, MqttMessage message) throws MessageProcessingException {
        String payload = new String(message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            // JSON 파싱
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new MessageProcessingException("Error processing the message payload", e);
        }

        // 로그 메시지 작성
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Topic: ").append(topic).append("\n");

        if (jsonNode.isObject()) {
            logMessage.append("Message Data:\n");
            appendJsonNode(jsonNode, logMessage);
        } else {
            logMessage.append("Message is not an object, raw payload: ").append(payload).append("\n");
            logger.warn("Received non-object message: {}", payload);
            return; // 메시지가 객체가 아니면 처리 중단
        }

        logger.info(logMessage.toString());
        
        // value 값을 추출하여 InfluxDB에 기록
        if (jsonNode.has("value")) {
            double value = jsonNode.get("value").asDouble();
            try {
                influxDBService.writeToInfluxDB(topic, value);
                // InfluxDBService 클래스에 데이터를 정확히 전달했는지 확인하는 로그
                logger.info("Data sent to InfluxDBService: {} -> {}", topic, value);
            } catch (Exception e) {
                logger.error("Failed to write data to InfluxDB: {}", e.getMessage());
            }
        } else {
            logger.warn("No 'value' field found in message, skipping InfluxDB write.");
        }
    }

    
    // JSON 노드를 한 줄씩 출력하는 메서드
    private void appendJsonNode(JsonNode node, StringBuilder logMessage) {
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(fieldName -> {
                JsonNode fieldValue = node.get(fieldName);
                logMessage.append(fieldName).append(": ").append(fieldValue.asText()).append("\n");
            });
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                appendJsonNode(item, logMessage);
            }
        } else {
            logMessage.append("Value: ").append(node.asText()).append("\n");
        }
    }
}
