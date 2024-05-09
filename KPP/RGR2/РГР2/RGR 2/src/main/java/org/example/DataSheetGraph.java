package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DataSheetGraph extends JPanel {

    private static final long serialVersionUID = 1L;

    private DataSheet dataSheet;
    private boolean isConnected;
    private double deltaX;
    private double deltaY;

    transient private Color color;

    public DataSheetGraph() {
        dataSheet = null;
        isConnected = false;
        deltaX = 5;
        deltaY = 5;
        color = Color.red;
    }

    public void paint(Graphics g) {
        paintComponent(g);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        showGraph((Graphics2D) g);
    }

    private void showGraph(Graphics2D g) {
        double xMin, xMax, yMin, yMax;
        double width = getWidth(), height = getHeight();

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        if ((maxX() != minX())) deltaX = (10.0 * (maxX() - minX())) / 100.0;
        else deltaX = 5;

        if ((maxY() != minY())) deltaY = (10.0 * (maxY() - minY())) / 100.0;
        else deltaY = 5;

        xMin = minX() - deltaX;
        xMax = maxX() + deltaX;
        yMin = minY() - deltaY;
        yMax = maxY() + deltaY;


        double xScale = width / (xMax - xMin);
        double yScale = height / (yMax - yMin);
        double x0 = -xMin * xScale;
        double y0 = yMax * yScale;

        Paint oldColor = g.getPaint();
        g.setPaint(Color.WHITE);
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        Stroke oldStroke = g.getStroke();
        Font oldFont = g.getFont();

        float[] dashPattern = {10, 10};
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        g.setFont(new Font("Serif", Font.BOLD, 14));

        double xStep = (xMax - xMin) / 10.0;
        if ((maxX() != minX())) xStep = (xMax - xMin - 2 * deltaX) / 10.0;

        for (double dx = xStep; dx < xMax; dx += xStep) {
            double x = x0 + dx * xScale;
            g.setPaint(Color.LIGHT_GRAY);
            g.draw(new Line2D.Double(x, 0, x, height));
            g.setPaint(Color.BLACK);
            g.drawString(df.format((dx / xStep) * xStep), (int) x + 2, 10);
        }

        for (double dx = -xStep; dx > xMin; dx -= xStep) {
            double x = x0 + dx * xScale;
            g.setPaint(Color.LIGHT_GRAY);
            g.draw(new Line2D.Double(x, 0, x, height));
            g.setPaint(Color.BLACK);
            g.drawString(df.format((dx / xStep) * xStep), (int) x + 2, 10);
        }

        double yStep = (yMax - yMin) / 10.0;
        if ((maxY() != minY())) yStep = (yMax - yMin - 2 * deltaY) / 10.0;

        for (double dy = yStep; dy < yMax; dy += yStep) {
            double y = y0 - dy * yScale;
            g.setPaint(Color.LIGHT_GRAY);
            g.draw(new Line2D.Double(0, y, width, y));
            g.setPaint(Color.BLACK);
            g.drawString(df.format(dy), 2, (int) y - 2);
        }
        for (double dy = -yStep; dy > yMin; dy -= yStep) {
            double y = y0 - dy * yScale;
            g.setPaint(Color.LIGHT_GRAY);
            g.draw(new Line2D.Double(0, y, width, y));
            g.setPaint(Color.BLACK);
            g.drawString(df.format(dy), 2, (int) y - 2);
        }

        g.setPaint(Color.BLACK);
        g.setStroke(new BasicStroke(3.0f));
        g.draw(new Line2D.Double(x0, 0, x0, height));
        g.draw(new Line2D.Double(0, y0, width, y0));
        g.drawString("X", (int) width - 10, (int) y0 - 2);
        g.drawString("Y", (int) x0 + 2, 10);

        if (dataSheet != null && dataSheet.getCountOfData() != 0) {
            if (!isConnected) {
                for (int i = 0; i < dataSheet.getCountOfData(); i++) {
                    double x = x0 + Double.parseDouble(dataSheet.getX(i)) * xScale;
                    double y = y0 - Double.parseDouble(dataSheet.getY(i)) * yScale;
                    g.setColor(Color.WHITE);
                    g.fillOval((int) (x - 2.5), (int) (y - 2.5), 5, 5);
                    g.setColor(color);
                    g.drawOval((int) (x - 2.5), (int) (y - 2.5), 5, 5);
                }
            } else {
                g.setColor(color);
                g.setStroke(new BasicStroke(2.0f));
                double xOld = x0 + Double.parseDouble(dataSheet.getX(0)) * xScale;
                double yOld = y0 - Double.parseDouble(dataSheet.getY(0)) * yScale;
                for (int i = 1; i < dataSheet.getCountOfData(); i++) {
                    double x = x0 + Double.parseDouble(dataSheet.getX(i)) * xScale;
                    double y = y0 - Double.parseDouble(dataSheet.getY(i)) * yScale;
                    g.draw(new Line2D.Double(xOld, yOld, x, y));
                    xOld = x;
                    yOld = y;
                }
            }
        }
        g.setPaint(oldColor);
        g.setStroke(oldStroke);
        g.setFont(oldFont);
    }

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public Color getColor() {
        return color;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public double minX() {
        double res = 0;
        if (dataSheet != null && dataSheet.getCountOfData() != 0) {
            res = Double.parseDouble(dataSheet.getX(0));
            for (int i = 0; i < dataSheet.getCountOfData(); i++)
                if (Double.parseDouble(dataSheet.getX(i)) < res)
                    res = Double.parseDouble(dataSheet.getX(i));
        }
        return res;
    }

    public double minY() {
        double res = 0;
        if (dataSheet != null && dataSheet.getCountOfData() != 0) {
            res = Double.parseDouble(dataSheet.getY(0));
            for (int i = 0; i < dataSheet.getCountOfData(); i++)
                if (Double.parseDouble(dataSheet.getY(i)) < res)
                    res = Double.parseDouble(dataSheet.getY(i));
        }
        return res;
    }

    public double maxX() {
        double res = 0;
        if (dataSheet != null && dataSheet.getCountOfData() != 0) {
            res = Double.parseDouble(dataSheet.getX(0));
            for (int i = 0; i < dataSheet.getCountOfData(); i++)
                if (Double.parseDouble(dataSheet.getX(i)) > res)
                    res = Double.parseDouble(dataSheet.getX(i));
        }
        return res;
    }

    public double maxY() {
        double res = 0;
        if (dataSheet != null && dataSheet.getCountOfData() != 0) {
            res = Double.parseDouble(dataSheet.getY(0));
            for (int i = 0; i < dataSheet.getCountOfData(); i++)
                if (Double.parseDouble(dataSheet.getY(i)) > res)
                    res = Double.parseDouble(dataSheet.getY(i));
        }
        return res;
    }
}