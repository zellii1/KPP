package gui;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextField textFieldFrom = null;
    private JTextField textFieldTo = null;
    private JTextField textFieldStep = null;
    private JTextField textFieldFunc = null;
    private XYSeries series = null;
    private XYSeries der = null;
    private double start;
    private double stop;
    private double step;

    public MainFrame() {

        setTitle("GetDerivative");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 20, 5, 20));
        mainPanel.setLayout(new BorderLayout(100, 5));
        setContentPane(mainPanel);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        mainPanel.add(panelButtons, BorderLayout.NORTH);

        JButton btnNewButtonPlot = new JButton("Calculate");
        btnNewButtonPlot.setPreferredSize(new Dimension(250, 30));
        btnNewButtonPlot.addActionListener(e -> {
            start = Double.parseDouble(textFieldFrom.getText());
            stop = Double.parseDouble(textFieldTo.getText());
            step = Double.parseDouble(textFieldStep.getText());
            series.clear();
            der.clear();
            Parser parser = new Parser(Parser.STANDARD_FUNCTIONS);
            Variable var = new Variable("x");
            Variable par = new Variable("a");
            String funStr = String.valueOf(textFieldFunc.getText());
            parser.add(var);
            parser.add(par);
            ExpressionProgram funs = parser.parse(funStr);
            Expression ders = funs.derivative(var);
            double a = 0.5;
            par.setVal(a);
            for (double x = start; x < stop; x += step) {
                var.setVal(x);
                series.add(x, funs.getVal());
                der.add(x, ders.getVal());
            }
        });
        panelButtons.add(btnNewButtonPlot);

        JPanel panelData = new JPanel();
        panelData.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelData.setPreferredSize(new Dimension(500, 100));
        mainPanel.add(panelData, BorderLayout.SOUTH);

        JLabel lblNewLabelfunc = new JLabel("f(x)= ");
        panelData.add(lblNewLabelfunc);

        textFieldFunc = new JTextField();
        textFieldFunc.setText("sin(x)/x");
        textFieldFunc.setColumns(15);
        panelData.add(textFieldFunc);

        JLabel lblNewLabelStart = new JLabel("From:");
        lblNewLabelStart.setPreferredSize(new Dimension(50, 50));
        panelData.add(lblNewLabelStart);

        textFieldFrom = new JTextField();
        textFieldFrom.setText("-6");
        textFieldFrom.setColumns(5);
        panelData.add(textFieldFrom);

        JLabel lblNewLabelStop = new JLabel("To:");
        lblNewLabelStop.setPreferredSize(new Dimension(50, 50));
        panelData.add(lblNewLabelStop);

        textFieldTo = new JTextField();
        textFieldTo.setText("6");
        textFieldTo.setColumns(5);
        panelData.add(textFieldTo);

        JLabel lblNewLabelStep = new JLabel("Step:");
        lblNewLabelStep.setPreferredSize(new Dimension(50, 50));

        panelData.add(lblNewLabelStep);

        textFieldStep = new JTextField();
        textFieldStep.setText("0.1");
        textFieldStep.setColumns(5);
        panelData.add(textFieldStep);

        JPanel panelChart = new JPanel();
        panelChart.setLayout(new BorderLayout());
        mainPanel.add(panelChart, BorderLayout.CENTER);

        series = new XYSeries("f(x)");
        der = new XYSeries("f'(x)");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(der);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Function Graph",
                "x",
                "y",
                dataset
        );

        chart.getPlot().setBackgroundPaint(ChartColor.WHITE);
        chart.getPlot().setForegroundAlpha(0.8f);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}