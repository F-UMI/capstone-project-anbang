import java.util.ArrayList;
import java.util.List;


import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(
        name = "AnbangRealEstateChaincode",
        info = @Info(
                title = "AnbangRealEstateTransfer contract",
                description = "AnbangRealEstateTransfer contract",
                version = "0.0.1",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "anbang.com",
                        name = "AnbangRealEstateTransfer",
                        url = "https://hyperledger.example.com")))

@Default
public class AnbangRealEstateTransfer implements ContractInterface{

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean anbangRealEstateExists(final Context ctx, final String homeID) {
        ChaincodeStub stub = ctx.getStub();
        String RealtyJson = stub.getStringState(homeID);

        return (RealtyJson != null && !RealtyJson.isEmpty());
    }
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public AnbangRealEstate createAnbangRealEstate(final Context ctx, final String homeID, final String owner, final String address, final long price) {
        ChaincodeStub stub = ctx.getStub();

        if(anbangRealEstateExists(ctx, homeID)) {
            String errorMessage = String.format("%s은 이미 존재하는 매물입니다.", homeID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        AnbangRealEstate anbangRealEstate = new AnbangRealEstate(homeID, owner, address, price);
        String storeJson = genson.serialize(anbangRealEstate);
        stub.putStringState(homeID, storeJson);

        return anbangRealEstate;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public AnbangRealEstate readAnbangRealEstate(final Context ctx, final String homeID) {
        ChaincodeStub stub = ctx.getStub();
        String RealtyJson = stub.getStringState(homeID);

        if (RealtyJson == null || RealtyJson.isEmpty()) {
            String errorMessage = String.format("%s은 존재하지 않는 매물입니다.", homeID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        AnbangRealEstate anbangRealEstate = genson.deserialize(RealtyJson, AnbangRealEstate.class);

        return anbangRealEstate;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public AnbangRealEstate updateAnbangRealEstate(final Context ctx, final String homeID, final String owner, final String address, final long price) {
        ChaincodeStub stub = ctx.getStub();

        if(!anbangRealEstateExists(ctx, homeID)) {
            String errorMessage = String.format("%s은 존재하지 않는 매물입니다.", homeID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        AnbangRealEstate newAnbangRealEstate = new AnbangRealEstate(homeID, owner, address, price);
        String storeJson = genson.serialize(newAnbangRealEstate);
        stub.putStringState(homeID, storeJson);

        return newAnbangRealEstate;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String transferAnbangRealEstate(final Context ctx, final String homeID, final String newOwner) {
        ChaincodeStub stub = ctx.getStub();
        String RealtyJson = stub.getStringState(homeID);

        if(RealtyJson == null || RealtyJson.isEmpty()){
            String errorMessage = String.format("%s은 존재하지 않는 매물입니다.", homeID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        AnbangRealEstate anbangRealEstate = genson.deserialize(RealtyJson, AnbangRealEstate.class);

        AnbangRealEstate newAnbangRealEstate = new AnbangRealEstate(anbangRealEstate.getHomeID(), newOwner, anbangRealEstate.getAddress(), anbangRealEstate.getPrice());
        String storeJson = genson.serialize(newAnbangRealEstate);
        stub.putStringState(homeID, storeJson);

        return newOwner;
        //return anbangRealEstate.getOwner();
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryAllAnbangRealEstate(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<AnbangRealEstate> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            AnbangRealEstate anbangRealEstate = genson.deserialize(result.getStringValue(), AnbangRealEstate.class);
            System.out.println(anbangRealEstate);
            queryResults.add(anbangRealEstate);
        }
        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void deleteAnbangRealEstate(final Context ctx, final String homeID) {
        ChaincodeStub stub = ctx.getStub();

        if(!anbangRealEstateExists(ctx, homeID)) {
            String errorMessage = String.format("%s은 존재하지 않는 매물입니다.", homeID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }
        stub.delState(homeID);
    }
}
