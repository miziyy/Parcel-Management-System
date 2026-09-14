package parcelmanagementsystem;

public class NormalParcel extends Parcel {

    public NormalParcel(String trackingNo, String senderName, String receiverName, double weight, String status) {
        super(trackingNo, senderName, receiverName, weight, status);
    }

    @Override
    public double calculateDeliveryFee() {
        return 5.00 + (getWeight() * 1.50);
    }

    @Override
    public String getParcelType() {
        return "Standard";
    }
}