import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import javax.imageio.ImageIO;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

public class TextToHandwritingGUI extends JFrame {
    private JTextArea textArea;
    private JComboBox<String> fontCombo;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> paperCombo;
    private JButton generateButton, savePDFButton;
    private Color penColor = Color.BLACK;
    private BufferedImage handwritingImage;

    public TextToHandwritingGUI() {
        setTitle("Text to Handwriting");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Area
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Control Panel
        JPanel panel = new JPanel();

        // Font selector
        fontCombo = new JComboBox<>(new String[]{
                "handwriting1.ttf", "handwriting2.ttf", "handwriting3.ttf", "handwriting4.TTF",
                "handwriting5.ttf", "handwriting6.ttf", "handwriting7.ttf", "handwriting8.ttf",
                "handwriting9.ttf", "handwriting10.ttf", "handwriting11.ttf", "handwriting12.otf",
                "handwriting13.otf", "handwriting14.ttf", "handwriting15.ttf"
        });
        panel.add(new JLabel("Font:"));
        panel.add(fontCombo);

        // Font size
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(48, 12, 200, 2));
        panel.add(new JLabel("Font Size:"));
        panel.add(fontSizeSpinner);

        // Paper selection
        paperCombo = new JComboBox<>(new String[]{
                "paper1.png", "paper2.png", "paper3.png", "paper4.png", "paper5.png"
        });
        panel.add(new JLabel("Paper:"));
        panel.add(paperCombo);

        // Pen color button
        JButton colorButton = new JButton("Pen Color");
        colorButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Choose Pen Color", penColor);
            if (selected != null) penColor = selected;
        });
        panel.add(colorButton);

        // Generate button
        generateButton = new JButton("Generate Handwriting Image");
        generateButton.addActionListener(e -> generateImage());
        panel.add(generateButton);

        // Save PDF button
        savePDFButton = new JButton("Save as PDF");
        savePDFButton.addActionListener(e -> savePDF());
        panel.add(savePDFButton);

        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void generateImage() {
        try {
            String text = textArea.getText();
            String fontFile = "fonts/" + fontCombo.getSelectedItem();
            int fontSize = (Integer) fontSizeSpinner.getValue();
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFile)).deriveFont((float) fontSize);

            int width = 1000;
            int height = 600;

            // Load paper texture
            String textureFile = "textures/" + paperCombo.getSelectedItem();
            BufferedImage paper = ImageIO.read(new File(textureFile));

            // Create handwriting image
            handwritingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = handwritingImage.createGraphics();
            g2d.drawImage(paper, 0, 0, width, height, null);

            g2d.setFont(font);
            g2d.setColor(penColor);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            FontMetrics metrics = g2d.getFontMetrics(font);
            int lineHeight = metrics.getHeight();
            int xStart = 50;
            int y = 100;
            int maxWidth = width - 100;

            String[] words = text.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                int lineWidth = metrics.stringWidth(testLine);
                if (lineWidth > maxWidth) {
                    drawHumanizedLine(g2d, line.toString(), xStart, y, metrics);
                    line = new StringBuilder(word);
                    y += lineHeight + 10;
                } else {
                    if (line.length() > 0) line.append(" ");
                    line.append(word);
                }
            }
            drawHumanizedLine(g2d, line.toString(), xStart, y, metrics); // last line

            g2d.dispose();

            // Preview image
            ImageIcon icon = new ImageIcon(handwritingImage);
            JOptionPane.showMessageDialog(this, new JLabel(icon), "Handwriting Preview", JOptionPane.PLAIN_MESSAGE);

            // Save image automatically
            ImageIO.write(handwritingImage, "png", new File("handwriting.png"));
            JOptionPane.showMessageDialog(this, "Handwriting image saved as handwriting.png");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating image: " + e.getMessage());
        }
    }

    private void drawHumanizedLine(Graphics2D g2d, String line, int xStart, int yBase, FontMetrics metrics) {
        int x = xStart;
        for (char c : line.toCharArray()) {
            String ch = String.valueOf(c);

            // Random vertical jitter (-2 to +2 pixels)
            int yJitter = yBase + (int)(Math.random() * 5) - 2;

            // Small rotation (-3 to +3 degrees)
            double angle = Math.toRadians(Math.random() * 6 - 3);

            g2d.rotate(angle, x, yJitter);
            g2d.drawString(ch, x, yJitter);
            g2d.rotate(-angle, x, yJitter);

            // Random horizontal spacing variation (1 to 3 pixels)
            x += metrics.charWidth(c) + (int)(Math.random() * 3 + 1);
        }
    }

    private void savePDF() {
        if (handwritingImage == null) {
            JOptionPane.showMessageDialog(this, "Generate image first!");
            return;
        }
        try {
            String pdfPath = "handwriting.pdf";
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // Load handwriting.png into PDF
            ImageData imageData = ImageDataFactory.create("handwriting.png");
            Image image = new Image(imageData);

            // Scale image to fit inside page
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            // Add image
            document.add(image);

            document.close();
            JOptionPane.showMessageDialog(this, "PDF saved as " + pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving PDF: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextToHandwritingGUI::new);
    }
}
