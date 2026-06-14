import Gui.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class VetCareApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
