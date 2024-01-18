package org.example;

import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.GatewayException;

public class Buyer {

    public static byte[] getAllAssets(Contract contract) throws GatewayException {

        var result = contract.evaluateTransaction("GetAllAssets");

        return result;
    }
}
