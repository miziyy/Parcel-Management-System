package parcelmanagementsystem;

public class Parcel {

    private int trackingNo;
    private String senderName;
    private String receiverName;
    private double weight;
    private String status;

    public Parcel(int trackingNo, String senderName,
                  String receiverName, double weight,
                  String status) {

        this.trackingNo = trackingNo;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.weight = weight;
        this.status = status;
    }

    public int getTrackingNo() {
        return trackingNo;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public double getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public void setTrackingNo(int trackingNo) {
        this.trackingNo = trackingNo;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double calculateDeliveryFee() {
        return 0.0;
    }

    public String getParcelType() {
        return "Parcel";
    }
}
