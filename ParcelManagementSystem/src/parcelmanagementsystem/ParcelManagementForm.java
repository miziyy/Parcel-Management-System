
package parcelmanagementsystem;

public class ParcelManagementForm {



 // Polymorphsm
        Parcel parcel;

        if (type.equals("Express")) {

            parcel = new ExpressParcel(
                    tracking,
                    sender,
                    receiver,
                    weight,
                    status
            );

        } else {

            parcel = new StandardParcel(
                    tracking,
                    sender,
                    receiver,
                    weight,
                    status
            );
        }

        return parcel;
    }    
}



