export PATH=$PATH:../bin:${PWD}:$PATH

export CORE_PEER_TLS_ENABLED=true
export ORDERER_CA=${PWD}/crypto-config/ordererOrganizations/anbang.com/orderers/orderer.anbang.com/msp/tlscacerts/tlsca.anbang.com-cert.pem
export PEER0_ORG1_CA=${PWD}/crypto-config/peerOrganizations/org1.anbang.com/peers/peer0.org1.anbang.com/tls/ca.crt
export PEER0_ORG2_CA=${PWD}/crypto-config/peerOrganizations/org2.anbang.com/peers/peer0.org2.anbang.com/tls/ca.crt
export FABRIC_CFG_PATH=../config/

export CHANNEL_NAME="anbang-channel"
# Orderer 설정
setGlobalsForOrderer(){
    export CORE_PEER_LOCALMSPID="OrdererMSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$ORDERER_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/crypto-config/ordererOrganizations/anbang.com/users/Admin@anbang.com/msp
}
# Peer 설정
setGlobalsForPeer0Org1(){
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/crypto-config/peerOrganizations/org1.anbang.com/users/Admin@org1.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:7051
}

setGlobalsForPeer1Org1(){
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/crypto-config/peerOrganizations/org1.anbang.com/users/Admin@org1.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:8051
}

setGlobalsForPeer0Org2(){
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/crypto-config/peerOrganizations/org2.anbang.com/users/Admin@org2.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:9051
}

setGlobalsForPeer1Org2(){
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/crypto-config/peerOrganizations/org2.anbang.com/users/Admin@org2.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:10051
}
# 채널 생성
function createChannel(){
    # 이전 채널 블록 삭제
    rm -rf ./channel-artifacts/*
    setGlobalsForPeer0Org1

    peer channel create -o localhost:7050 -c $CHANNEL_NAME \
    --ordererTLSHostnameOverride orderer.anbang.com \
    -f ./blocks/${CHANNEL_NAME}.tx --outputBlock ./channel-artifacts/${CHANNEL_NAME}.block \
    --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA
}
# 채널 가입
function joinChannel(){
    setGlobalsForPeer0Org1
    peer channel join -b ./channel-artifacts/$CHANNEL_NAME.block

    setGlobalsForPeer1Org1
    peer channel join -b ./channel-artifacts/$CHANNEL_NAME.block

    setGlobalsForPeer0Org2
    peer channel join -b ./channel-artifacts/$CHANNEL_NAME.block

    setGlobalsForPeer1Org2
    peer channel join -b ./channel-artifacts/$CHANNEL_NAME.block

}
# 앵커 피어 업데이트
function updateAnchorPeers(){
    setGlobalsForPeer0Org1
    peer channel update -o localhost:7050 \
    --ordererTLSHostnameOverride orderer.anbang.com \
    -c $CHANNEL_NAME -f ./blocks/${CORE_PEER_LOCALMSPID}anchors.tx \
    --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA

    setGlobalsForPeer0Org2
    peer channel update -o localhost:7050 \
    --ordererTLSHostnameOverride orderer.anbang.com \
    -c $CHANNEL_NAME -f ./blocks/${CORE_PEER_LOCALMSPID}anchors.tx \
    --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA

}

if [ -n "$1" ]; then
    $1
else
    echo "Usage: $0 <function_name>"
fi