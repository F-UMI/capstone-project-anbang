echo "Enrolling the org1 root CA admin"

# rca-org2 컨테이너 실행
docker-compose up -d rca-org2
sleep 5

# fabric-ca-client 명령어를 사용하기 위한 환경변수 설정
export PATH=$PATH:../bin/

# org-2의 transaction 서명을 위한 CA 인증서 추출
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../../tmp/hyperledger/org2/ca/crypto/ca-cert.pem
# Fabric-CA-Client가 로컬 호스트에 RCA-ORG2 등록 기관을 저장하는 곳.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org2/ca/admin

# 부트스트랩 ID 이름과 비밀번호와 Fabric-CA-Client를 사용하여 관리자 등록
fabric-ca-client enroll -d -u https://rca-org2-admin:rca-org2-adminpw@0.0.0.0:7055
sleep 5

fabric-ca-client register -d --id.name peer1-org2 --id.secret peer1PW --id.type peer -u https://0.0.0.0:7055
fabric-ca-client register -d --id.name peer2-org2 --id.secret peer2PW --id.type peer -u https://0.0.0.0:7055
fabric-ca-client register -d --id.name admin-org2 --id.secret org2AdminPW --id.type user -u https://0.0.0.0:7055
fabric-ca-client register -d --id.name user-org2 --id.secret org2UserPW --id.type user -u https://0.0.0.0:7055
# admin/fabric-ca-client-config.yaml
# admin/msp/cacert/사용자의 자격 증명을 확인하는 데 사용되는 CA 인증서
# admin/msp/keystore/개인키 파일
# admin/msp/signcert/신원 확인 정보와 공개 키 파일

fabric-ca-client identity list

echo "Completed enrolling the org2 root CA admin"

sleep 5