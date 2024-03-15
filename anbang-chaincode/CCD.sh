export PATH=$PATH:./anbang-network/artifacts/bin:${PWD}:$PATH

export CORE_PEER_TLS_ENABLED=true
export ORDERER_CA=${PWD}/anbang-network/artifacts/channel/crypto-config/ordererOrganizations/anbang.com/orderers/orderer.anbang.com/msp/tlscacerts/tlsca.anbang.com-cert.pem
export PEER0_ORG1_CA=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org1.anbang.com/peers/peer0.org1.anbang.com/tls/ca.crt
export PEER0_ORG2_CA=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org2.anbang.com/peers/peer0.org2.anbang.com/tls/ca.crt
export FABRIC_CFG_PATH=${PWD}/anbang-network/artifacts/config/

export CHANNEL_NAME="anbang-channel"
# Orderer 설정
setGlobalsForOrderer(){
    export CORE_PEER_LOCALMSPID="OrdererMSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$ORDERER_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/anbang-network/artifacts/channel/crypto-config/ordererOrganizations/anbang.com/users/Admin@anbang.com/msp
}
# Peer 설정
setGlobalsForPeer0Org1(){
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org1.anbang.com/users/Admin@org1.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:7051
}

setGlobalsForPeer1Org1(){
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org1.anbang.com/users/Admin@org1.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:8051
}

setGlobalsForPeer0Org2(){
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org2.anbang.com/users/Admin@org2.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:9051
}

setGlobalsForPeer1Org2(){
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/anbang-network/artifacts/channel/crypto-config/peerOrganizations/org2.anbang.com/users/Admin@org2.anbang.com/msp
    export CORE_PEER_ADDRESS=localhost:10051
}

CHANNEL_NAME="anbnag-channel"
CC_RUNTIME_LANGUAGE="java"
VERSION="1"
SEQUENCE=1
CC_SRC_PATH="./anbang-chaincode/chaincode/java/"
CC_NAME="anbang-chaincode"

#echo "Compiling Java code..."
#./gradlew installDist
#echo "Finished compiling Java code"

packageChaincode() {
    rm -rf ${CC_NAME}.tar.gz
    setGlobalsForPeer0Org1
    peer lifecycle chaincode package ${CC_NAME}.tar.gz \
        --path ${CC_SRC_PATH} --lang ${CC_RUNTIME_LANGUAGE} \
        --label ${CC_NAME}_${VERSION}
    echo "===================== Chaincode is packaged ===================== "
}

#packageChaincode

installChaincode() {
    setGlobalsForPeer0Org2
    peer lifecycle chaincode install ${CC_NAME}.tar.gz
    echo "===================== Chaincode is installed on peer0.org1 ===================== "

}

installChaincode