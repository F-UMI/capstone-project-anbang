export PATH=$PATH:../bin/

echo "Enroll Peer1"

# 루트 인증서를 복사.
mkdir -p ../tmp/hyperledger/org1/peer1/assets/ca
cp ../tmp/hyperledger/org1/ca/admin/msp/cacerts/0-0-0-0-7054.pem ../tmp/hyperledger/org1/peer1/assets/ca/org1-ca-cert.pem

# 피어1-조직1 등록.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org1/peer1
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org1/peer1/assets/ca/org1-ca-cert.pem
export FABRIC_CA_CLIENT_MSPDIR=msp

fabric-ca-client enroll -d -u https://peer1-org1:peer1PW@0.0.0.0:7054
sleep 5

# TLS 인증서를 복사.
mkdir -p ../tmp/hyperledger/org1/peer1/assets/tls-ca
cp ../tmp/hyperledger/tls-ca/admin/msp/cacerts/0-0-0-0-7052.pem ../tmp/hyperledger/org1/peer1/assets/tls-ca/tls-ca-cert.pem

# 피어1-조직1 TLS 등록.
export FABRIC_CA_CLIENT_MSPDIR=tls-msp
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org1/peer1/assets/tls-ca/tls-ca-cert.pem

fabric-ca-client enroll -d -u https://peer1-org1:peer1PW@0.0.0.0:7052 --enrollment.profile tls --csr.hosts peer1-org1
sleep 5

# TLS 인증서 이름 변경
mv ../tmp/hyperledger/org1/peer1/tls-msp/keystore/*_sk ../tmp/hyperledger/org1/peer1/tls-msp/keystore/key.pem

echo "Enroll Peer2"

# 루트 인증서를 복사.
mkdir -p ../tmp/hyperledger/org1/peer2/assets/ca
cp ../tmp/hyperledger/org1/ca/admin/msp/cacerts/0-0-0-0-7054.pem ../tmp/hyperledger/org1/peer2/assets/ca/org1-ca-cert.pem

# 피어2-조직1 등록.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org1/peer2
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org1/peer2/assets/ca/org1-ca-cert.pem
export FABRIC_CA_CLIENT_MSPDIR=msp

fabric-ca-client enroll -d -u https://peer2-org1:peer2PW@0.0.0.0:7054
sleep 5

# TLS 인증서를 복사.
mkdir -p ../tmp/hyperledger/org1/peer2/assets/tls-ca
cp ../tmp/hyperledger/tls-ca/admin/msp/cacerts/0-0-0-0-7052.pem ../tmp/hyperledger/org1/peer2/assets/tls-ca/tls-ca-cert.pem

# 피어2-조직1 TLS 등록.
export FABRIC_CA_CLIENT_MSPDIR=tls-msp
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org1/peer2/assets/tls-ca/tls-ca-cert.pem

fabric-ca-client enroll -d -u https://peer2-org1:peer2PW@0.0.0.0:7052 --enrollment.profile tls --csr.hosts peer2-org1
sleep 5
# TLS 인증서 이름 변경
mv ../tmp/hyperledger/org1/peer2/tls-msp/keystore/*_sk ../tmp/hyperledger/org1/peer2/tls-msp/keystore/key.pem

echo "Enroll Admin"
# 조직1 관리자 등록
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org1/admin
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/org1/peer1/assets/ca/org1-ca-cert.pem
export FABRIC_CA_CLIENT_MSPDIR=msp

fabric-ca-client enroll -d -u https://admin-org1:org1AdminPW@0.0.0.0:7054
sleep 5

# 조직 관리자 msp 폴더 --> 피어1 관리자 msp 폴더에 공개키 복사
mkdir -p ../tmp/hyperledger/org1/peer1/msp/admincerts
cp ../tmp/hyperledger/org1/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org1/peer1/msp/admincerts/org1-admin-cert.pem
# 조직 관리자 msp 폴더 --> 피어2 관리자 msp 폴더에 공개키 복사
mkdir -p ../tmp/hyperledger/org1/peer2/msp/admincerts
cp ../tmp/hyperledger/org1/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org1/peer2/msp/admincerts/org1-admin-cert.pem
# 조직 관리자 msp 폴더 --> 관리자 msp 폴더에 공개키 복사
mkdir -p ../tmp/hyperledger/org1/admin/msp/admincerts
cp ../tmp/hyperledger/org1/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org1/admin/msp/admincerts/org1-admin-cert.pem

# 조직 msp 폴더 생성
mkdir -p ../tmp/hyperledger/org1/msp/{admincerts,cacerts,tlscacerts,users}
cp ../tmp/hyperledger/org1/peer1/assets/ca/org1-ca-cert.pem ../tmp/hyperledger/org1/msp/cacerts/
cp ../tmp/hyperledger/org1/peer1/assets/tls-ca/tls-ca-cert.pem ../tmp/hyperledger/org1/msp/tlscacerts/
cp ../tmp/hyperledger/org1/admin/msp/signcerts/cert.pem ../tmp/hyperledger/org1/msp/admincerts/admin-org1-cert.pem

# NodeOUs 설정
cp ../config/org1-config.yaml ../tmp/hyperledger/org1/msp/config.yaml

echo "Org1 done"

docker-compose up -d peer1-org1
sleep 5
docker-compose up -d peer2-org1
sleep 5
