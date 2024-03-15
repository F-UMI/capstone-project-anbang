/*
 * SPDX-License-Identifier: Apache-2.0
 */
package org.example;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import static java.nio.charset.StandardCharsets.UTF_8;
@Contract(name = "MyAssetContract",
        info = @Info(title = "MyAsset contract",
                description = "Very basic Java Contract example",
                version = "0.0.1",
                license =
                @License(name = "SPDX-License-Identifier: Apache-2.0",
                        url = ""),
                contact =  @Contact(email = "MyAssetContract@example.com",
                        name = "MyAssetContract",
                        url = "http://MyAssetContract.me")))
@Default
public class MyAssetContract implements ContractInterface {
    public  MyAssetContract() {

    }
    @Transaction()
    public boolean myAssetExists(Context ctx, String proertyId) {
        byte[] buffer = ctx.getStub().getState(proertyId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createMyAsset(Context ctx, String propertyId, String propertyName, String bargainName, String propertyType, String propertyAddress, String detailAddress, String propertySize, String numberOfRooms, String typeOfPropertyTransaction, String propertyPrice, String maintenanceCost, String availableMoveDate) {
        boolean exists = myAssetExists(ctx,propertyId);
        if (exists) {
            throw new RuntimeException("The asset "+propertyId+" already exists");
        }
        AnbangAsset asset = new AnbangAsset();
        asset.setPropertyId(propertyId);
        asset.setPropertyName(propertyName);
        asset.setBargainName(bargainName);
        asset.setPropertyType(propertyType);
        asset.setPropertyAddress(propertyAddress);
        asset.setDetailAddress(detailAddress);
        asset.setPropertySize(propertySize);
        asset.setNumberOfRooms(numberOfRooms);
        asset.setTypeOfPropertyTransaction(typeOfPropertyTransaction);
        asset.setPropertyPrice(propertyPrice);
        asset.setMaintenanceCost(maintenanceCost);
        asset.setAvailableMoveDate(availableMoveDate);
        ctx.getStub().putState(propertyId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public MyAsset readMyAsset(Context ctx, String propertyId) {
        boolean exists = myAssetExists(ctx,propertyId);
        if (!exists) {
            throw new RuntimeException("The asset "+propertyId+" does not exist");
        }

        MyAsset newAsset = MyAsset.fromJSONString(new String(ctx.getStub().getState(propertyId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public void updateMyAsset(Context ctx, String propertyId, String newpropertyName, String newbargainName, String newpropertyType, String newpropertyAddress, String newdetailAddress, String newpropertySize, String newnumberOfRooms, String newtypeOfPropertyTransaction, String newpropertyPrice, String newmaintenanceCost, String newavailableMoveDate) {
        boolean exists = myAssetExists(ctx,propertyId);
        if (!exists) {
            throw new RuntimeException("The asset "+propertyId+" does not exist");
        }
        MyAsset asset = new MyAsset();
        asset.setValue(newpropertyName);
        asset.setBargainName(newbargainName);
        asset.setPropertyType(newpropertyType);
        asset.setPropertyAddress(newpropertyAddress);
        asset.setDetailAddress(newdetailAddress);
        asset.setPropertySize(newpropertySize);
        asset.setNumberOfRooms(newnumberOfRooms);
        asset.setTypeOfPropertyTransaction(newtypeOfPropertyTransaction);
        asset.setPropertyPrice(newpropertyPrice);
        asset.setMaintenanceCost(newmaintenanceCost);
        asset.setAvailableMoveDate(newavailableMoveDate);
        ctx.getStub().putState(propertyId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public void deleteMyAsset(Context ctx, String propertyId) {
        boolean exists = myAssetExists(ctx,propertyId
        if (!exists) {
            throw new RuntimeException("The asset "+propertyId+" does not exist");
        }
        ctx.getStub().delState(propertyId);
    }

}