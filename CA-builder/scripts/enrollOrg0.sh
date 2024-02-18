export PATH=$PATH:../bin/

echo "Enroll Orderer"

# 루트 인증서를 복사.
mkdir -p ../tmp/hyperledger/org0/orderer/assets/ca
cp ../tmp/hyperledger/org0/ca/admin/msp/cacerts/0-0-0-0-7053.pem ../tmp/hyperledger/org0/orderer/assets/ca/org0-ca-cert.pem

# 오더러-조직0 등록.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org0/orderer
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org0/orderer/assets/ca/org0-ca-cert.pem

fabric-ca-client enroll -d -u https://orderer1-org0:ordererpw@0.0.0.0:7053
sleep 5

# TLS 인증서를 복사.
mkdir -p ../tmp/hyperledger/org0/orderer/assets/tls-ca
cp ../tmp/hyperledger/tls-ca/admin/msp/cacerts/0-0-0-0-7052.pem ../tmp/hyperledger/org0/orderer/assets/tls-ca/tls-ca-cert.pem

# 오더러-조직0 TLS 등록.
export FABRIC_CA_CLIENT_MSPDIR=tls-msp
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org0/orderer/assets/tls-ca/tls-ca-cert.pem

fabric-ca-client enroll -d -u https://orderer1-org0:ordererPW@0.0.0.0:7052 --enrollment.profile tls --csr.hosts orderer1-org0
sleep 5

# TLS 인증서 이름 변경
mv ../tmp/hyperledger/org0/orderer/tls-msp/keystore/*_sk ../tmp/hyperledger/org0/orderer/tls-msp/keystore/key.pem

echo "Enroll Admin"

# 조직0 관리자 등록
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org0/admin
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org0/orderer/assets/ca/org0-ca-cert.pem
export FABRIC_CA_CLIENT_MSPDIR=msp

fabric-ca-client enroll -d -u https://admin-org0:org0adminpw@0.0.0.0:7053
sleep 5

# 조직 관리자 msp 폴더 --> 오더러 관리자 msp 폴더에 공개키 복사
mkdir -p ../tmp/hyperledger/org0/orderer/msp/admincerts
cp ../tmp/hyperledger/org0/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org0/orderer/msp/admincerts/orderer-admin-cert.pem

mkdir -p ../tmp/hyperledger/org0/msp/{admincerts,cacerts,tlscacerts,users}
cp ../tmp/hyperledger/org0/orderer/assets/ca/org0-ca-cert.pem ../tmp/hyperledger/org0/msp/cacerts/
cp ../tmp/hyperledger/org0/orderer/assets/tls-ca/tls-ca-cert.pem ../tmp/hyperledger/org0/msp/tlscacerts/
cp ../tmp/hyperledger/org0/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org0/msp/admincerts/admin-org0-cert.pem

# NodeOUs 설정
cp ../config/org0-config.yaml ../tmp/hyperledger/org0/msp/config.yaml

echo "Org0 done"

