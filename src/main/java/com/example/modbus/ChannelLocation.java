package com.example.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChannelLocation {
    public String channel;
    public String location;

    // Getter and Setter

    public static List<ChannelLocation> loadChannelLocationsFromJson(String filePath) throws IOException {
        InputStream inputStream = AddressMap.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);
        List<ChannelLocation> channelLocations = new ArrayList<>();

        JsonNode channelLocationNode = rootNode.path("channelLocations");
        if (channelLocationNode.isArray()) {
            for (JsonNode channelLocation : channelLocationNode) {
                ChannelLocation location = new ChannelLocation();
                location.channel = channelLocation.get("channel").asText();
                location.location = channelLocation.get("location").asText();
                channelLocations.add(location);
            }
        }

        return channelLocations;
    }
}
