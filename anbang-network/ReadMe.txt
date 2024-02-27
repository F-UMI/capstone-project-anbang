# 하이퍼레저 패브릭 네트워크 구축 단계 지침서
## 1. 전제 조건
- 도커 설치
- 도커 컴포즈 설치
- bin 디렉토리에 configtxgen, cryptogen, peer 바이너리 파일이 존재해야 함 (hyperledger fabric sample 다운로드시 fabric-samples/bin 디렉토리에 존재함)

## 2. 파일 구조

├── ReadMe.txt
└── artifacts
    ├── bin
    │    ├── configtxgen
    │    ├── cryptogen
    │    └── peer
    ├── channel
    │    ├── CC.sh
    │    └── GC.sh
    ├── config
    │    ├── base.yaml
    │    ├── configtx.yaml
    │    ├── core.yaml
    │    └── crypto-config.yaml
    └── docker-compose.yaml

## 3. 네트워크 구축 단계
1. artifacts/channel 디렉토리로 이동
2. ./GC.sh cryptogens 실행
3. ./GC.sh configtxgens 실행
4. docker-compose file이 있는 artifacts 폴더로 이동 cd ../
5. docker-compose up -d 실행
6. ./CC.sh createChannel 실행
7. ./CC.sh joinChannel 실행
8. ./CC.sh updateAnchorPeers 실행



