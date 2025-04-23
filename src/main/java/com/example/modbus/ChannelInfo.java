package com.example.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChannelInfo {
    public int channel;
    public int address;


    // Getter and Setter

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
                channelInfos.add(channelInfo);
            }
        }

        return channelInfos;
    }
}
