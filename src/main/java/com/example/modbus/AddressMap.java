package com.example.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddressMap {

    public static class RegisterInfo {
        public int address;
        public String name;
        public String type;
        public String unit;
        public int size;
        public int scale;
        
        // Getter and Setter
    }

    public static class ChannelInfo {
        public int channel;
        public int address;
        public String location;
        
        // Getter and Setter
    }

    // RegisterInfo 로딩 메서드
    public static List<RegisterInfo> loadAddressMapFromJson(String filePath) throws IOException {
        InputStream inputStream = AddressMap.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);  
        List<RegisterInfo> registerInfos = new ArrayList<>();

        JsonNode addressMapNode = rootNode.path("addressMap");
        if (addressMapNode.isArray()) {
            for (JsonNode registerNode : addressMapNode) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.address = registerNode.get("address").asInt();
                registerInfo.name = registerNode.get("name").asText();
                registerInfo.type = registerNode.get("type").asText();
                registerInfo.unit = registerNode.get("unit").asText();
                registerInfo.size = registerNode.get("size").asInt();
                registerInfo.scale = registerNode.get("scale").asInt();
                registerInfos.add(registerInfo);
            }
        }

        return registerInfos;
    }

    // ChannelInfo 로딩 메서드
    public static List<ChannelInfo> loadChannelInfoFromJson(String filePath) throws IOException {
        InputStream inputStream = AddressMap.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);  
        List<ChannelInfo> channelInfos = new ArrayList<>();

        JsonNode channelInfoNode = rootNode.path("channelInfo");
        if (channelInfoNode.isArray()) {
            for (JsonNode channelNode : channelInfoNode) {
                ChannelInfo channelInfo = new ChannelInfo();
                channelInfo.channel = channelNode.get("channel").asInt();
                channelInfo.address = channelNode.get("address").asInt();
                channelInfo.location = channelNode.get("location").asText();
                channelInfos.add(channelInfo);
            }
        }

        return channelInfos;
    }

    // 채널 위치 정보를 로딩하는 메서드
    public static List<ChannelInfo> loadChannelLocationsFromJson(String filePath) throws IOException {
        InputStream inputStream = AddressMap.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);  
        List<ChannelInfo> channelLocations = new ArrayList<>();

        JsonNode channelLocationsNode = rootNode.path("channelLocations");
        if (channelLocationsNode.isArray()) {
            for (JsonNode locationNode : channelLocationsNode) {
                ChannelInfo channelLocation = new ChannelInfo();
                channelLocation.channel = locationNode.get("channel").asInt();
                channelLocation.location = locationNode.get("location").asText();
                channelLocations.add(channelLocation);
            }
        }

        return channelLocations;
    }
}
