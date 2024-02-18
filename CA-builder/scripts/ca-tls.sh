echo "Enrolling TLS CA Admin"

# ca-tls 컨테이너 실행
docker-compose up -d ca-tls
sleep 5

# TLS-CA에서 TLS 서버 인증서를 확인하기 위한 CA 인증서 추출
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../tmp/hyperledger/tls-ca/crypto/tls-cert.pem
# Fabric-CA-Client가 로컬 호스트에 TLS-CA용 등록 기관을 저장하는 곳.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/tls-ca/admin

# 부트스트랩 ID 이름과 비밀번호와 Fabric-CA-Client를 사용하여 관리자 등록
fabric-ca-client enroll -d -u https://tls-ca-admin:tls-ca-adminpw@0.0.0.0:7052
sleep 5

# TLS-CA의 관리자는 fabric-ca-client를 사용하여
# 각 피어에 대해 하나씩, 주문자에 대해 하나씩 4개의 새 ID를 CA에 등록한다.
# 이러한 ID는 피어 및 주문자에 대한 TLS 인증서를 받는 데 사용된다.
fabric-ca-client register -d --id.name peer1-org1 --id.secret peer1PW --id.type peer -u https://0.0.0.0:7052
fabric-ca-client register -d --id.name peer2-org1 --id.secret peer2PW --id.type peer -u https://0.0.0.0:7052
fabric-ca-client register -d --id.name peer1-org2 --id.secret peer1PW --id.type peer -u https://0.0.0.0:7052
fabric-ca-client register -d --id.name peer2-org2 --id.secret peer2PW --id.type peer -u https://0.0.0.0:7052
fabric-ca-client register -d --id.name orderer1-org0 --id.secret ordererPW --id.type orderer -u https://0.0.0.0:7052

echo
fabric-ca-client identity list

echo "completed TLS CA Admin Enroll"

sleep 5