echo "Enrolling the org0 root CA admin"

# rca-org0 컨테이너 실행
docker-compose up -d rca-org0
sleep 5

# fabric-ca-client 명령어를 사용하기 위한 환경변수 설정
# org-0의 transaction 서명을 위한 CA 인증서 추출
export FABRIC_CA_CLIENT_TLS_CERTFILES=../../../../../tmp/hyperledger/org0/ca/crypto/ca-cert.pem
# Fabric-CA-Client가 로컬 호스트에 RCA-ORG0 등록 기관을 저장하는 곳.
export FABRIC_CA_CLIENT_HOME=../tmp/hyperledger/org0/ca/admin

# 부트스트랩 ID 이름과 비밀번호와 Fabric-CA-Client를 사용하여 관리자 등록
fabric-ca-client enroll -d -u https://rca-org0-admin:rca-org0-adminpw@0.0.0.0:7053
sleep 5

fabric-ca-client register -d --id.name orderer1-org0 --id.secret ordererpw --id.type orderer -u https://0.0.0.0:7053
fabric-ca-client register -d --id.name admin-org0 --id.secret org0adminpw --id.type user -u https://0.0.0.0:7053
# admin/fabric-ca-client-config.yaml
# admin/msp/cacert/사용자의 자격 증명을 확인하는 데 사용되는 CA 인증서
# admin/msp/keystore/개인키 파일
# admin/msp/signcert/신원 확인 정보와 공개 키 파일

fabric-ca-client identity list

echo "Completed enrolling the org0 root CA admin"

sleep 5