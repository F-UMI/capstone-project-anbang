package ChaincodeConfig;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public final class Asset {
    @Property()
    private String assetId;
    @Property
    private String owner;
    @Property
    private int size;
    @Property
    private int value;

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static Asset fromJsonString(String json){

        String assetId = new JSONObject(json).getString("assetId");
        String owner = new JSONObject(json).getString("owner");
        int size = new JSONObject(json).getInt("size");
        int value = new JSONObject(json).getInt("value");

        Asset asset = new Asset();

        asset.setAssetId(assetId);
        asset.setOwner(owner);
        asset.setSize(size);
        asset.setValue(value);

        return asset;
    }

}
