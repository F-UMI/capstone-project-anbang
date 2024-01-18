package org.example.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FabricConfig {
    // Hyperledger Fabric 네트워크와 상호 작용을 위한 설정 값들

    // Hyperledger Fabric의 멤버 서비스 제공자(MSP) ID. 환경 변수에서 읽어오며, 기본값은 "Org1MSP"입니다.
    private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");

    // 채널의 이름. 환경 변수에서 읽어오며, 기본값은 "mychannel"입니다.
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");

    // 사용할 체인코드의 이름. 환경 변수에서 읽어오며, 기본값은 "basic"입니다.
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

    // 암호화 자료의 경로
    private static final Path CRYPTO_PATH = Paths.get("/Users/mj/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");

    // 사용자 인증서의 경로
    private static final Path CERT_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts/cert.pem"));

    // 사용자 개인 키가 저장된 디렉토리의 경로
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));

    // 피어의 TLS 인증서의 경로
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));

    // Gateway의 피어 엔드포인트. "localhost:7051"로 정의되어 있습니다.
    private static final String PEER_ENDPOINT = "localhost:7051";

    // 인증 정보를 임의로 지정하는 부분. "peer0.org1.example.com"로 정의되어 있습니다.
    private static final String OVERRIDE_AUTH = "peer0.org1.example.com";

    public static String getMspId() {
        return MSP_ID;
    }

    public static String getChannelName() {
        return CHANNEL_NAME;
    }

    public static String getChaincodeName() {
        return CHAINCODE_NAME;
    }

    public static Path getCryptoPath() {
        return CRYPTO_PATH;
    }

    public static Path getCertPath() {
        return CERT_PATH;
    }

    public static Path getKeyDirPath() {
        return KEY_DIR_PATH;
    }

    public static Path getTlsCertPath() {
        return TLS_CERT_PATH;
    }

    public static String getPeerEndpoint() {
        return PEER_ENDPOINT;
    }

    public static String getOverrideAuth() {
        return OVERRIDE_AUTH;
    }

}
