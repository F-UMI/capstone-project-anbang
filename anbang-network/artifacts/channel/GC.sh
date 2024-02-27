export PATH=$PATH:../bin:${PWD}:$PATH

chmod -R 0755 ./crypto-config

function cryptogens() {
    # 이전 인증서 폴더 삭제
    rm -rf ./crypto-config
    # 인증서 발급
    cryptogen generate --config=../config/crypto-config.yaml --output=./crypto-config
}

function configtxgens(){

    # 이전 블록 삭제
    rm -rf ./blocks/*

    SYS_CHANNEL="sys-channel"
    CHANNEL_NAME="anbang-channel"

    echo $CHANNEL_NAME
    # 시스템 제네시스 블록 생성
    echo "#######    Generating Orderer Genesis block  ##########"
    configtxgen -profile AnbangOrdererGenesis -configPath ../config -channelID $SYS_CHANNEL -outputBlock ./blocks/genesis.block
    # 채널 설정 블록 생성
    echo "#######    Generating channel configuration transaction  ##########"
    configtxgen -profile AnbangChannel -configPath ../config -outputCreateChannelTx ./blocks/${CHANNEL_NAME}.tx -channelID $CHANNEL_NAME

    echo "#######    Generating anchor peer update for Org1MSP  ##########"
    configtxgen -profile AnbangChannel -configPath ../config -outputAnchorPeersUpdate ./blocks/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP

    echo "#######    Generating anchor peer update for Org2MSP  ##########"
    configtxgen -profile AnbangChannel -configPath ../config -outputAnchorPeersUpdate ./blocks/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP
}



if [ -n "$1" ]; then
    $1
else
    echo "Usage: $0 <function_name>"
fi
