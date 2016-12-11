package pl.zablocki.viewer.panels;

import lombok.Getter;
import lombok.Setter;
import pl.zablocki.core.model.ParamsSingleton;
import pl.zablocki.core.simulation.SimulationRunnable;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainFrame extends JFrame {

    static final int FRAME_WIDTH = 1900;
    static final int FRAME_HEIGHT = 800;
    private static final int BUTTONS_PANEL_WIDTH = 150;
    @Getter
    private CanvasPanel canvas = null;
    private NumberFormat decimalFormatter = new DecimalFormat("#0");
    private static double savedThreadSleep;
    @Setter
    private SimulationRunnable simulationRunnable;
    @Setter
    private Thread simulationThread;

    public MainFrame() {
        super("Road Traffic Simulation v1");
        this.canvas = new CanvasPanel();

        setUpFrame();
//        setLayout(new GridLayout(2, 1));
        addButtonsPanel();
        addCanvas();

        pack();
        setVisible(true);
    }

    private void addButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel startStopComponent = getStartStopComponent();
        JPanel paceComponent = getPaceComponent();
        JPanel resetComponent = getResetComponent();

        buttonsPanel.add(startStopComponent);
//        buttonsPanel.add(getGapContainer(10));
        buttonsPanel.add(resetComponent);
        buttonsPanel.add(getGapContainer(30));
        buttonsPanel.add(paceComponent);

        add(buttonsPanel, BorderLayout.NORTH);
    }

    private JPanel getResetComponent() {
        JButton resetBtn = new JButton("Reset");


        resetBtn.addActionListener(e -> {
            //TODO implement reset functionality
        });


        JPanel jPanel = new JPanel();
        jPanel.setMaximumSize(new Dimension(110, 100));
        jPanel.add(resetBtn);

        return jPanel;
    }

    private JPanel getStartStopComponent() {
        JButton startStopBtn = new JButton("START/STOP");


        startStopBtn.addActionListener(e -> {
            if (ParamsSingleton.getInstance().getDt() != 0) {
                ParamsSingleton.getInstance().setDt(0);
            } else {
                ParamsSingleton.getInstance().setDt(0.2);
            }
        });


        JPanel jPanel = new JPanel();
        jPanel.setMaximumSize(new Dimension(110, 100));
        jPanel.add(startStopBtn);

        return jPanel;
    }

    private JPanel getPaceComponent() {
        Label paceLabel = new Label("pace:");
        JButton plusButton = new JButton("+");
        Label actualPaceLabel = new Label("" + decimalFormatter.format(ParamsSingleton.getInstance().getThreadSleep()));
        JButton minusButton = new JButton("-");

        double step = 2;

        plusButton.addActionListener(e -> {
            ParamsSingleton params = ParamsSingleton.getInstance();
            double dt = params.getThreadSleep();
            if (dt - step >= 0) {
                params.setThreadSleep(dt - step);
                actualPaceLabel.setText(decimalFormatter.format(dt - step));
            }
        });

        minusButton.addActionListener(e -> {
            ParamsSingleton params = ParamsSingleton.getInstance();
            double dt = params.getThreadSleep();
//            if( dt + step >= 0) {
            params.setThreadSleep(dt + step);
            actualPaceLabel.setText(decimalFormatter.format(dt + step));
//            }
        });


        JPanel jPanel = new JPanel();
        jPanel.setMaximumSize(new Dimension(180, 100));
        jPanel.add(paceLabel);
        jPanel.add(minusButton);
        jPanel.add(actualPaceLabel);
        jPanel.add(plusButton);

        return jPanel;
    }

    private JPanel getGapContainer(int gapSize) {
        JPanel gap = new JPanel();
        gap.setMinimumSize(new Dimension(gapSize, 100));
        gap.setMaximumSize(new Dimension(gapSize, 100));
        return gap;
    }

    private void addCanvas() {
        add(canvas, BorderLayout.CENTER);
    }

    private void setUpFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocation(((int) screenSize.getWidth() - FRAME_WIDTH) / 2, 100);
        setLayout(new BorderLayout());
    }


    static Dimension getCanvasPanelSize() {
        return new Dimension(FRAME_WIDTH - BUTTONS_PANEL_WIDTH, FRAME_HEIGHT);
    }
}