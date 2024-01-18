package org.example.config;

import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;


public class GatewayConfig {
    private static ManagedChannel newGrpcConnection() throws IOException {
        // TLS 채널의 인증서를 설정합니다.
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(FabricConfig.getTlsCertPath().toFile())
                .build();

        // gRPC 연결을 설정하고 반환합니다.
        return Grpc.newChannelBuilder(FabricConfig.getPeerEndpoint(), credentials)
                .overrideAuthority(FabricConfig.getOverrideAuth())  // 피어 엔드포인트의 권한을 재정의합니다.
                .build();
    }

    private static Identity newIdentity() throws IOException, CertificateException {
        // 사용자의 X.509 인증서를 읽어와서 새로운 Identity 객체를 생성합니다.
        var certReader = Files.newBufferedReader(FabricConfig.getCertPath());
        var certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(FabricConfig.getMspId(), certificate);  // 사용자 ID와 인증서로 X.509 Identity 생성
    }

    private static Signer newSigner() throws IOException, InvalidKeyException {
        // 사용자의 프라이빗 키를 읽어와서 Signer로 변환하여 반환합니다.
        var keyReader = Files.newBufferedReader(getPrivateKeyPath());
        var privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);  // 프라이빗 키로 Signer 생성
    }

    private static Path getPrivateKeyPath() throws IOException {
        // 사용자의 프라이빗 키 파일 경로를 얻어옵니다.
        try (var keyFiles = Files.list(FabricConfig.getKeyDirPath())) {
            return keyFiles.findFirst().orElseThrow();  // 첫 번째 프라이빗 키 파일의 경로를 반환
        }
    }

    public static ManagedChannel getManagedChannel() throws IOException {
        return newGrpcConnection();
    }

    public static Identity getIdentity() throws IOException, CertificateException{
        return newIdentity();
    }

    public static Signer getSigner() throws IOException, InvalidKeyException {
        // 사용자의 프라이빗 키를 읽어와서 Signer로 변환하여 반환합니다.
        return newSigner();
    }

}
