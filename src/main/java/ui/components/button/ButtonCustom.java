/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components.button;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import ui.components.effect.RippleEffect;

public class ButtonCustom extends JButton{
    
    private Color startColor = new Color(149, 145, 239);
    private Color endColor = new Color(127, 122, 239);
    private boolean isHovered = false;
    private int round = 10;
    private final RippleEffect rippleEffect;

    public ButtonCustom() {
        setContentAreaFilled(false); // Tắt nền mặc định
        setFocusPainted(false); // Bỏ khung focus
        setBorderPainted(false); // Bỏ viền
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Khởi tạo hiệu ứng Ripple
        rippleEffect = new RippleEffect(this);

        // add event hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Bật khử răng cưa
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // tạo hiệu ứng gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, isHovered ? endColor : startColor, // Màu bắt đầu khi hover
                0, getHeight(), isHovered ? startColor : endColor // Màu kết thúc khi hover
        );

        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Vẽ nút với góc bo tròn

        Area area = new Area(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        g2d.fill(area);

        // Vẽ gợn sóng
        rippleEffect.reder(g2d, area);

        // vẽ chữ
        super.paintComponent(g);
    }
}
