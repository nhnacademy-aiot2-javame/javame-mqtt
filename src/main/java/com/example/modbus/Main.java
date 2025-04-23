package com.example.modbus;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // AddressMap에서 JSON 파일 읽기
        List<AddressMap.RegisterInfo> registerInfos = null;
        List<AddressMap.ChannelInfo> channelInfos = null;
        List<AddressMap.ChannelInfo> channelLocations = null;
        String jsonFilePath = "addressMap.json";  // JSON 파일 경로
        
        try {
            // AddressMap, ChannelInfo, ChannelLocations 데이터 읽기
            registerInfos = AddressMap.loadAddressMapFromJson(jsonFilePath);
            channelInfos = AddressMap.loadChannelInfoFromJson(jsonFilePath);
            channelLocations = AddressMap.loadChannelLocationsFromJson(jsonFilePath);
            
            logger.info("Address map, channel info, and channel locations loaded successfully.");
        } catch (IOException e) {
            logger.error("Error loading JSON files: {}", e.getMessage());
            e.printStackTrace();
            return; // JSON 파일을 읽지 못한 경우 종료
        }

        // 로드된 데이터를 로깅하여 확인
        logger.info("Register Infos: {}", registerInfos);
        logger.info("Channel Infos: {}", channelInfos);
        logger.info("Channel Locations: {}", channelLocations);

        // ModbusMasterTCP 실행 (Modbus 작업)
        ModbusMasterTCP modbusMaster = new ModbusMasterTCP(registerInfos, channelInfos, channelLocations);
        try {
            modbusMaster.execute();
        } catch (Exception e) {
            logger.error("Error during Modbus communication: {}", e.getMessage());
        }
    }
}
