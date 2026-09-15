package parcelmanagementsystem;

public class ExpressParcel extends Parcel {

    public ExpressParcel(String trackingNo, String senderName,
                         String receiverName, double weight,
                         String status) {

        super(trackingNo, senderName, receiverName, weight, status);
    }

    @Override
    public double calculateDeliveryFee() {
        return 10.00 + (getWeight() * 1.50);
    }

    @Override
    public String getParcelType() {
        return "Express";
    }
}