package own;

import consoleTasks.Interpolator;
import consoleTasks.Point2D;

import java.util.Iterator;
import java.util.TreeSet;

public class DataInterpolationTreeSet extends Interpolator {
    private TreeSet<Point2D> data = new TreeSet<>();

    public DataInterpolationTreeSet(TreeSet<Point2D> data) {
        this.data = data;
    }

    public DataInterpolationTreeSet() {
    }

    public void clear() {
        data.clear();
    }

    public int numPoints() {
        return data.size();
    }

    public void addPoint(Point2D pt) {
        data.add(pt);
    }

    public Point2D getPoint(int i) {
        if (i == 0) {
            return data.first();
        }
        int n = 0;
        Iterator<Point2D> it = data.iterator();
        Point2D curr = null;
        while (it.hasNext() && n <= i) {
            curr = it.next();
            n++;
        }
        return curr;
    }

    public void setPoint(int i, Point2D pt) {
        data.add(pt);
    }

    public void removeLastPoint() {
        data.remove(data.size() - 1);
    }

    public void sort() {
    }
}