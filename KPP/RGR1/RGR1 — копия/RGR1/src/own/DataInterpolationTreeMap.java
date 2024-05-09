package own;

import consoleTasks.Interpolator;
import consoleTasks.Point2D;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class DataInterpolationTreeMap extends Interpolator {
    private TreeMap<Double, Double> data = new TreeMap<>();

    public DataInterpolationTreeMap(TreeMap<Double, Double> data) {
        this.data = data;
    }

    public DataInterpolationTreeMap() {
    }

    public void clear() {
        data.clear();
    }

    public int numPoints() {
        return data.size();
    }

    public void addPoint(Point2D pt) {
        data.put(pt.getX(), pt.getY());
    }

    public Point2D getPoint(int i) {
        if (i < 0 || data.size() <= i) {
            throw new IndexOutOfBoundsException("IndexOutOfBoundsException");
        }
        Map.Entry<Double, Double> e = null;
        Iterator<Map.Entry<Double, Double>> it = data.entrySet().iterator();
        while (0 <= i--) {
            e = it.next();
        }
        double x = e.getKey();
        double y = e.getValue();
        return new Point2D(x, y);
    }

    public void setPoint(int i, Point2D pt) {
        data.put(pt.getX(), pt.getY());
    }

    public void removeLastPoint() {
        data.remove(data.size() - 1);
    }

    public void sort() {
    }
}