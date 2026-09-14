package parcelmanagementsystem;
        
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ParcelManagementForm form =
                    new ParcelManagementForm();

            form.setVisible(true);
        });
    }
}