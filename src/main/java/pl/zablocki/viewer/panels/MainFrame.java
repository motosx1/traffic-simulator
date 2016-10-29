package pl.zablocki.viewer.panels;

import lombok.Getter;
import pl.zablocki.core.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainFrame extends JFrame{

    public static final int FRAME_WIDTH = 1200;
    public static final int FRAME_HEIGHT = 800;
    public static final int BUTTONS_PANEL_WIDTH = 150;
    @Getter
    CanvasPanel canvas = null;

    public MainFrame(Map<Integer, Vehicle> vehicles) {
        super("Camera Simulator v1");
        this.canvas = new CanvasPanel(vehicles);

        setUpFrame();
        addCanvas();
        addButtonsPanel();

        pack();
        setVisible(true);
    }

    private void addButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(BUTTONS_PANEL_WIDTH, (int)buttonsPanel.getPreferredSize().getHeight()));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));

        add(buttonsPanel, BorderLayout.EAST);
    }

    private void addCanvas() {
        add(canvas,BorderLayout.CENTER);
    }

    private void setUpFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocation(((int)screenSize.getWidth() - FRAME_WIDTH)/2,100);
        setLayout(new BorderLayout());
    }


    public static Dimension getCanvasPanelSize(){
        return new Dimension(FRAME_WIDTH-BUTTONS_PANEL_WIDTH, FRAME_HEIGHT);
    }

}
