package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.example.config.FabricConfig;
import org.example.config.GatewayConfig;
import org.hyperledger.fabric.client.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class App {

    private final Gson gson = new Gson();
    private final Contract contract; // 스마트 계약과 상호작용하기 위한 Contract 객체
    private final String assetId = "0006(건물_고유_번호)" + System.nanoTime(); //새 자산을 식별하기 위한 고유 ID

    public App(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(FabricConfig.getChannelName());

        // Get the smart contract from the network.
        contract = network.getContract(FabricConfig.getChaincodeName());
    }

    public static void main(final String[] args) throws Exception {
        // gRPC 채널을 생성하여 Hyperledger Fabric 네트워크에 연결
        var channel = GatewayConfig.getManagedChannel();

        // Gateway 빌더를 생성하고, 필요한 구성을 추가
        var builder = Gateway.newInstance()
                .identity(GatewayConfig.getIdentity())    // 사용자 ID 및 인증서를 설정하는 메서드 호출
                .signer(GatewayConfig.getSigner())        // 사용자의 개인 키를 설정하는 메서드 호출
                .connection(channel); // 이전에 생성한 gRPC 채널을 설정하는 메서드 호출

        // 다양한 gRPC 호출에 대한 기본 타임아웃 설정
        var gateway = builder.connect();
        try {
            new App(gateway).run();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public void run() throws GatewayException, CommitException {

        // Return all the current assets on the ledger.

        Agent agent = new Agent();
        agent.contract = contract;
        agent.setAssetId(assetId);
        agent.setOwner("홍길동(소유자)");
        agent.setSize("5평(건물_크기)");
        agent.setValue("5000만원");
        agent.run();

        System.out.println(prettyJson(Buyer.getAllAssets(contract)));

    }

    /**
     * This type of transaction would typically only be run once by an application
     * the first time it was started after its initial deployment. A new version of
     * the chaincode deployed later would likely not need to run an "init" function.
     */
    private void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of assets on the ledger");

        contract.submitTransaction("InitLedger");

        System.out.println("*** Transaction committed successfully");
    }

    /**
     * Evaluate a transaction to query ledger state.
     */
    public String getAllAssets() throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: GetAllAssets, function returns all the current assets on the ledger");

        var result = contract.evaluateTransaction("GetAllAssets");

        return prettyJson(result);
    }


    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    /**
     * Submit a transaction synchronously, blocking until it has been committed to
     * the ledger.
     */
    private void createAsset(String assetId, String owner, String size, String value) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: CreateAsset, creates new asset with ID, Color, Size, Owner and AppraisedValue arguments");

        contract.submitTransaction("CreateAsset", assetId, owner, size, value);

        System.out.println("*** Transaction committed successfully");
    }

    /**
     * Submit transaction asynchronously, allowing the application to process the
     * smart contract response (e.g. update a UI) while waiting for the commit
     * notification.
     */
    private void transferAssetAsync() throws EndorseException, SubmitException, CommitStatusException {
        System.out.println("\n--> Async Submit Transaction: TransferAsset, updates existing asset owner");

        var commit = contract.newProposal("TransferAsset")
                .addArguments(assetId, "Saptha")
                .build()
                .endorse()
                .submitAsync();

        var result = commit.getResult();
        var oldOwner = new String(result, StandardCharsets.UTF_8);

        System.out.println("*** Successfully submitted transaction to transfer ownership from " + oldOwner + " to Saptha");
        System.out.println("*** Waiting for transaction commit");

        var status = commit.getStatus();
        if (!status.isSuccessful()) {
            throw new RuntimeException("Transaction " + status.getTransactionId() +
                    " failed to commit with status code " + status.getCode());
        }

        System.out.println("*** Transaction committed successfully");
    }

    private String readAssetById(String assetId) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: ReadAsset, function returns asset attributes");

        var evaluateResult = contract.evaluateTransaction("ReadAsset", assetId);

        return prettyJson(evaluateResult);
    }

    /**
     * submitTransaction() will throw an error containing details of any error
     * responses from the smart contract.
     */
    private void updateNonExistentAsset() {
        try {
            System.out.println("\n--> Submit Transaction: UpdateAsset asset70, asset70 does not exist and should return an error");

            contract.submitTransaction("UpdateAsset", "asset70", "blue", "5", "Tomoko", "300");

            System.out.println("******** FAILED to return an error");
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            System.out.println("*** Successfully caught the error: ");
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());

            var details = e.getDetails();
            if (!details.isEmpty()) {
                System.out.println("Error Details:");
                for (var detail : details) {
                    System.out.println("- address: " + detail.getAddress() + ", mspId: " + detail.getMspId()
                            + ", message: " + detail.getMessage());
                }
            }
        } catch (CommitException e) {
            System.out.println("*** Successfully caught the error: " + e);
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());
            System.out.println("Status code: " + e.getCode());
        }
    }
}

