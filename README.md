## 회원가입 CreateAccountActivity.java
getCurrentDate - 입주가능일을 가져옴

SaveUserTask - 사용자 정보를 저장

doInBackground - 회원가입시 입력한 정보를 CouchDB에 연결하여 저장

onPostExucute - 회원가입 성공/실패에 따른 처리

---
## 로그인 LoginActivity.java
CheckUserTask - 로그인 시 작성한 정보와 CouchDB에 있는 정보를 비교

doInBackground - CouchDB에 연결하여 회원가입시 작성한 정보를 불러오기

onPostExecute - 불러온 정보와 로그인 시 작성한 정보를 비교하여 로그인 성공/실패에 따른 처리

---
## 사용자 정보 띄우기 UserProfileActivity
GetUserTask - CouchDB에 저장되어 있는 사용자 정보를 가져와서 정보를 띄워줌

doInBackground - CouchDB에 연결하여 사용자 정보를 조회

onPostExecute - CouchDB에서 알맞은 정보를 조회하여 정보를 띄워줌

---

## 매물 정보 저장 PropertyResgistrationActivity.java
saveSelectDate - DatePicker에서 설정한 입주가능일을 가져옴

onClickPropertyRegistration - 버튼 클릭 시 매물 정보 전달

getCategory - 작성 시 매물 유형을 가져옴

getMonthYear - 작성 시 매물 전/월세를 가져옴

getAddress - 작성 시 매물 주소를 가져옴

getSize - 작성 시 매물 크기를 가져옴

getRoomCount - 작성 시 매물 방 개수를 가져옴

getPrice - 작성 시 매물 가격을 가져옴

getManagePay - 작성 시 매물 관리비를 가져옴

getAboutProperty - 작성 시 매물 세부사항을 가져옴

doInBackground - CouchDB에 연결하여 매물 정보를 저장

onPostExecute - 매물 정보 저장 성공/실패에 따른 처

---
## 매물 정보 띄우기 PropertyInfoActivity.java
GetPropertyTask - CouchDB에 저장되어 있는 매물 정보를 가져와서 정보를 띄워줌

doInBackground - CouchDB에 연결하여 매물 정보를 조회

onPostExecute - CouchDB에서 알맞은 정보를 조회하여 정보를 띄워줌
