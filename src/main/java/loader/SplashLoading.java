package loader;

import ui.gui.GUI_Login;
import ultilities.WindowTitle;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lenovo
 */
public class SplashLoading extends javax.swing.JFrame {
    private String currentLoadDataString = "";
    private static final int TOTAL_PROGRESS = 100;
    private static final int DELAY_MS = 30; // Thời gian delay giữa các bước (ms)
    private float opacity = 0f; // Độ mờ cho hiệu ứng fade-in/out

    public SplashLoading() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Trong suốt để hỗ trợ bo tròn và hiệu ứng
        initComponents();
        setLocationRelativeTo(null); // Căn giữa màn hình
        jLabel2.setText("Phiên bản: " + WindowTitle.VERSION);

        currentLoadDataString = "Loading...";

        // Hiệu ứng fade-in
        Timer fadeInTimer = new Timer(50, e -> {
            opacity += 0.05f;
            if (opacity >= 1f) {
                opacity = 1f;
                ((Timer) e.getSource()).stop();
                startLoadingProcess(); // Bắt đầu tiến trình tải sau khi fade-in hoàn tất
            }
            setOpacity(opacity);
        });
        fadeInTimer.start();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        // Panel chính với hình nền gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(139, 145, 239));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel tiêu đề (phía trên)
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Logo khách sạn (giả định)
        JLabel logoLabel = new JLabel();

        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(logoLabel);

        // Label tiêu đề
        jLabel1 = new JLabel("MELODY HOTEL");
        jLabel1.setFont(new Font("Arial", Font.BOLD, 48));
        jLabel1.setForeground(new Color(139, 145, 239));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(jLabel1);

        // Panel phía dưới (phiên bản, bản quyền, progress bar)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label phiên bản
        jLabel2 = new JLabel("Phiên bản: 1.0.0");
        jLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(jLabel2, gbc);

        // Label bản quyền
        jLabel3 = new JLabel("(C) 2025 by ztnosleep-penguinguci");
        jLabel3.setFont(new Font("Arial", Font.PLAIN, 12));
        jLabel3.setForeground(Color.WHITE);
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        bottomPanel.add(jLabel3, gbc);

        // Progress bar
        jProgressBar1 = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Nền progress bar
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Thanh tiến trình
                int progressWidth = (int) ((getWidth() * getValue()) / 100.0);
                g2d.setColor(new Color(139, 145, 239));
                g2d.fillRect(0, 0, progressWidth, getHeight());

                g2d.dispose();
            }
        };
        jProgressBar1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jProgressBar1.setOpaque(false);
        jProgressBar1.setPreferredSize(new Dimension(600, 5));
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(jProgressBar1, gbc);

        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        // Kích thước cửa sổ
        setSize(600, 400);
        pack();
    }

    private void startLoadingProcess() {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String[] loadingSteps = {""};
                int steps = 5;
                int percentPerStep = TOTAL_PROGRESS / steps;
                int currentPercent = 0;

                for (int i = 0; i < steps; i++) {
                    currentPercent = Math.min((i + 1) * percentPerStep, TOTAL_PROGRESS);
                    publish(currentPercent);
                    processBarUpdate(currentPercent, loadingSteps[0]);
                    try {
                        Thread.sleep(DELAY_MS * 20); // Mô phỏng thời gian xử lý
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (Integer percent : chunks) {
                    jProgressBar1.setValue(percent);
                }
            }

            @Override
            protected void done() {
                // Hiệu ứng fade-out trước khi đóng
                Timer fadeOutTimer = new Timer(50, e -> {
                    opacity -= 0.05f;
                    if (opacity <= 0f) {
                        opacity = 0f;
                        ((Timer) e.getSource()).stop();
                        dispose();
                        // Ví dụ: Mở form chính
                        new GUI_Login().setVisible(true);
                    }
                    setOpacity(opacity);
                });
                fadeOutTimer.start();
            }
        };
        worker.execute();
    }

    public void processBarUpdate(int percent, String data) {
        currentLoadDataString = data;
        jProgressBar1.setValue(percent);
    }

    public void processBarUpdate(int percent) {
        jProgressBar1.setValue(percent);
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
                SplashLoading splash = new SplashLoading();
                splash.setVisible(true);

        });
    }

    // Variables declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
}