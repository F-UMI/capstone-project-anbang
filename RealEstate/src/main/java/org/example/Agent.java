package org.example;

import org.hyperledger.fabric.client.*;


public class Agent {
    public Contract contract;
    private String assetId;
    private String owner;
    private String size;
    private String value;

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private void createAsset() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        this.contract.submitTransaction("CreateAsset", this.assetId, this.owner, this.size, this.value);
    }

    public void run() throws EndorseException, CommitException, SubmitException, CommitStatusException {
        createAsset();
    }


}
