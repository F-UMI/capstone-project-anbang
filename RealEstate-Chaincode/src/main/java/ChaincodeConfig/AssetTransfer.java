package ChaincodeConfig;

import org.hyperledger.fabric.contract.Context;

import static java.nio.charset.StandardCharsets.UTF_8;
public class AssetTransfer {

    public boolean assetExists(Context context, String assetId){
        byte[] buffer = context.getStub().getState("assetId");
        return (buffer != null && buffer.length > 0);
    }
    public void createAsset(Context context, String assetId, String owner, int size, int value){
        boolean exists = assetExists(context,assetId);

        if (exists) {
            throw new RuntimeException(assetId);
        }
        Asset asset = new Asset();

        asset.setAssetId(assetId);
        asset.setOwner(owner);
        asset.setSize(size);
        asset.setValue(value);

        context.getStub().putState(assetId, asset.toJSONString().getBytes(UTF_8));
    }
}
