
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class AnbangAsset {

    public AnbangAsset() {
    }

    @Property()
    private String propertyId;
    @Property()
    private String propertyName;
    @Property()
    private String bargainName;
    @Property()
    private String propertyType;
    @Property()
    private String propertyAddress;
    @Property()
    private String detailAddress;
    @Property()
    private String propertySize;
    @Property()
    private String numberOfRooms;
    @Property()
    private String typeOfPropertyTransaction;
    @Property()
    private String propertyPrice;
    @Property()
    private String maintenanceCost;
    @Property()
    private String availableMoveDate;

    public Long getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getBargainName() {
        return bargainName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public String getTypeOfPropertyTransaction() {
        return typeOfPropertyTransaction;
    }

    public String getPropertyPrice() {
        return propertyPrice;
    }

    public String getMaintenanceCost() {
        return maintenanceCost;
    }

    public String getAvailableMoveDate() {
        return availableMoveDate;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setBargainName(String bargainName) {
        this.bargainName = bargainName;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setTypeOfPropertyTransaction(String typeOfPropertyTransaction) {
        this.typeOfPropertyTransaction = typeOfPropertyTransaction;
    }

    public void setPropertyPrice(String propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public void setMaintenanceCost(String maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public void setAvailableMoveDate(String availableMoveDate) {
        this.availableMoveDate = availableMoveDate;
    }


    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("propertyId", propertyId);
        jsonObject.put("propertyName", propertyName);
        jsonObject.put("bargainName", bargainName);
        jsonObject.put("propertyType", propertyType);
        jsonObject.put("propertyAddress", propertyAddress);
        jsonObject.put("detailAddress", detailAddress);
        jsonObject.put("propertySize", propertySize);
        jsonObject.put("numberOfRooms", numberOfRooms);
        jsonObject.put("typeOfPropertyTransaction", typeOfPropertyTransaction);
        jsonObject.put("propertyPrice", propertyPrice);
        jsonObject.put("maintenanceCost", maintenanceCost);
        jsonObject.put("availableMoveDate", availableMoveDate);
        return jsonObject.toString();
    }

    public static AnbangAsset fromJSONString(String json) {
        JSONObject jsonObject = new JSONObject(json);
        AnbangAsset asset = new AnbangAsset();
        asset.setPropertyId(jsonObject.getLong("propertyId"));
        asset.setPropertyName(jsonObject.getString("propertyName"));
        asset.setBargainName(jsonObject.getString("bargainName"));
        asset.setPropertyType(jsonObject.getString("propertyType"));
        asset.setPropertyAddress(jsonObject.getString("propertyAddress"));
        asset.setDetailAddress(jsonObject.getString("detailAddress"));
        asset.setPropertySize(jsonObject.getString("propertySize"));
        asset.setNumberOfRooms(jsonObject.getString("numberOfRooms"));
        asset.setTypeOfPropertyTransaction(jsonObject.getString("typeOfPropertyTransaction"));
        asset.setPropertyPrice(jsonObject.getString("propertyPrice"));
        asset.setMaintenanceCost(jsonObject.getString("maintenanceCost"));
        asset.setAvailableMoveDate(jsonObject.getString("availableMoveDate"));
        return asset;

}