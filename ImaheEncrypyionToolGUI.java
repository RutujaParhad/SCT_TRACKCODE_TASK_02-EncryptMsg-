import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageEncryptionToolGUI extends JFrame {

    private static final int KEY = 123;
    private File selectedFile;
    private JLabel statusLabel;

    public ImageEncryptionToolGUI() {
        setTitle("Image Encryption Tool");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // File selection panel
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        JButton browseButton = new JButton("Select Image");
        JLabel fileLabel = new JLabel("No file selected");

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileLabel.setText("Selected: " + selectedFile.getName());
            }
        });

        filePanel.add(browseButton, BorderLayout.WEST);
        filePanel.add(fileLabel, BorderLayout.CENTER);
        add(filePanel, BorderLayout.NORTH);

        // Action buttons
        JPanel buttonPanel = new JPanel();
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("Choose an image to begin.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Encrypt and Decrypt actions
        encryptButton.addActionListener(e -> processImage("encrypt"));
        decryptButton.addActionListener(e -> processImage("decrypt"));
    }

    private void processImage(String action) {
        if (selectedFile == null) {
            showError("No image selected.");
            return;
        }

        try {
            BufferedImage image = ImageIO.read(selectedFile);
            if (image == null) {
                showError("Invalid image file.");
                return;
            }

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = image.getRGB(x, y);
                    int newPixel = pixel ^ KEY;
                    image.setRGB(x, y, newPixel);
                }
            }

            String outputPath = "output_" + action + ".png";
            ImageIO.write(image, "png", new File(outputPath));
            showSuccess("Image " + action + "ed successfully!\nSaved as: " + outputPath);

        } catch (IOException ex) {
            showError("Error processing image: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText("❌ " + message);
    }

    private void showSuccess(String message) {
        statusLabel.setText("✅ " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageEncryptionToolGUI().setVisible(true));
    }
}