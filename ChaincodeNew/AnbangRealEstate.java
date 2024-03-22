import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;
@DataType
@Getter
public final class AnbangRealEstate {
    @Property
    private final String homeID;
    @Property
    private final String owner;
    @Property
    private final String address;
    @Property
    private final long price;


    public AnbangRealEstate(@JsonProperty("homeID") final String homeID, @JsonProperty("owner") final String owner, @JsonProperty("address") final String address, @JsonProperty("price") final long price) {
        this.homeID = homeID;
        this.owner = owner;
        this.address = address;
        this.price = price;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        AnbangRealEstate anbangRealEState = (AnbangRealEstate) obj;

        return Objects.deepEquals(
                new String[] {getHomeID(), getOwner(), getAddress()},
                new String[] {anbangRealEState.getHomeID(), anbangRealEState.getOwner(), anbangRealEState.getAddress()})
                &&
                Objects.deepEquals(
                        new long[] {getPrice()},
                        new long[] {anbangRealEState.getPrice()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeID(), getOwner(), getAddress(), getPrice());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [homeID=" + homeID + ", owner=" + owner + ", address=" + address + ", price=" + price + "]";
    }

}
