package com.example.modbus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

public class ModbusMasterTCP {
    static final Logger logger = LoggerFactory.getLogger(ModbusMasterTCP.class);

    private List<AddressMap.RegisterInfo> registerInfos;
    private List<AddressMap.ChannelInfo> channelInfos;
    private List<AddressMap.ChannelInfo> channelLocations;

    // 생성자 수정: RegisterInfo, ChannelInfo 두 개의 리스트를 받음
    public ModbusMasterTCP(List<AddressMap.RegisterInfo> registerInfos, 
                           List<AddressMap.ChannelInfo> channelInfos, 
                           List<AddressMap.ChannelInfo> channelLocations) {
        this.registerInfos = registerInfos;
        this.channelInfos = channelInfos;
        this.channelLocations = channelLocations;
    }

    public void execute() {
        logger.info("Modbus 통신을 시작합니다...");

        TcpParameters tcpParameters = new TcpParameters();
        try {
            InetAddress hostAddress = InetAddress.getByName("192.168.70.203");
            tcpParameters.setHost(hostAddress);
            tcpParameters.setPort(Modbus.TCP_PORT); 
        } catch (UnknownHostException e) {
            logger.error("IP 주소를 찾을 수 없습니다: {}", e.getMessage());
            return;
        }

        ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);

        try {
            if (!master.isConnected()) {
                master.connect();
            }

            for (AddressMap.RegisterInfo register : registerInfos) {
                try {
                    int address = register.address;
                    int size = register.size;
                    logger.info("Reading from Address: {}, Size: {}", address, size);
                    int[] registerValues = master.readInputRegisters(1, address, size);
                    if (registerValues != null && registerValues.length > 0) {
                        logger.info("Address: {}, Name: {}, Type: {}, Value: {}", 
                            address, register.name, register.type, registerValues[0]);
                    } else {
                        logger.warn("No values returned for Address: {}", address);
                    }
                } catch (ModbusIOException e) {
                    logger.error("Modbus 통신 오류: {}", e.getMessage());
                }
            }

            // ChannelInfo 데이터도 처리할 수 있도록 추가
            for (AddressMap.ChannelInfo channel : channelInfos) {
                logger.info("Channel: {}, Address: {}, Location: {}", 
                    channel.channel, channel.address, channel.location);
            }

            // channelLocations 처리 예시 (필요에 따라 수정 가능)
            for (AddressMap.ChannelInfo channelLocation : channelLocations) {
                logger.info("Channel Location - Channel: {}, Location: {}", 
                    channelLocation.channel, channelLocation.location);
            }

        } catch (Exception e) {
            logger.error("Modbus 연결 오류: {}", e.getMessage());
        }
    }
}
