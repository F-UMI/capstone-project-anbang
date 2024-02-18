# Hyperledger Fabric Network 구축

## 1. 전제 조건
      - fabric-samples 폴더 다운로드
      - fabric-ca 도커 이미지 다운로드(v1.5)
      - fabric-peer 도커 이미지 다운로드(v2.5)
      - fabric-orderer 도커 이미지 다운로드(v2.5)
      - fabric-tools 도커 이미지 다운로드(v2.5)
## 2. 파일 구성
      - CA-builder
        - config
          - ca-config.yaml
          - org0-config.yaml
          - org1-config.yaml
          - org2-config.yaml
        - bin
          - configtxgen <-- fabric-sample/bin 폴더에서 복사.
          - fabric-ca-client <-- fabric-sample/bin 폴더에서 복사.
        - scripts
          -...

## 3. 파일 실행 순서
     1. cd ../scripts
     2. ./builder.sh allCAup
     3. ./builder.sh enroll

* 현재 발견되는 오류 :
     - configtx.yaml 을 통해 genesis.block 파일을 생성할 때, 아래 오류 발생

     Failed validating bootstrap block: initializing channelconfig failed:
     could not create channel Orderer sub-group config:
     setting up the MSP manager failed: admin 0 is invalid
     [The identity does not contain OU [CLIENT], MSP: [org0MSP],The identity does not contain OU [ADMIN], MSP: [org0MSP]]

위 오류 해결시 체인코드 배포 테스트 진행 예정...