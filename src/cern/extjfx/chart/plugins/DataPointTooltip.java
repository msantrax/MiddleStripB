/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.extjfx.chart.plugins;

import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.SignalListener;
import com.opus.syssupport.VirnaPayload;
import isothermview.IsothermPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

/**
 * A tool tip label appearing next to the mouse cursor when placed over a data point's symbol. If symbols are not
 * created/shown for given plot, the tool tip is shown for the closest data point that is within the
 * {@link #pickingDistanceProperty()} from the mouse cursor.
 * <p>
 * CSS style class name: {@value #STYLE_CLASS_LABEL}
 *
 * @param <X> type of X values
 * @param <Y> type of Y values
 */
public class DataPointTooltip<X, Y> extends AbstractDataFormattingPlugin<X, Y> {

    private static final Logger LOG = Logger.getLogger(DataPointTooltip.class.getName());
    
    
    /**
     * Name of the CSS class of the tool tip label.
     */
    public static final String STYLE_CLASS_LABEL = "chart-datapoint-tooltip-label";

    /**
     * The default distance between the data point coordinates and mouse cursor that triggers showing the tool tip
     * label.
     */
    public static final int DEFAULT_PICKING_DISTANCE = 5;

    private static final int LABEL_X_OFFSET = 15;
    private static final int LABEL_Y_OFFSET = 5;

    private final Label label = new Label();
    
    private DataPoint dataPoint;
    
    
    // Signal SMTraffic events to listeners
    private transient ArrayList<SignalListener> listeners = new ArrayList<>();
    
    /** Método de registro do listener do dispositivo serial
     * @param l */
    public void addSignalListener (SignalListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial
     * @param l */
    public void removeSignalListener (SignalListener l){
        listeners.remove(l);
    }

    /** Esse método é chamado quando algo acontece no dispositivo
     * @param uid_addr
     * @param signal */
    protected void notifySignalListeners(SMTraffic smt) {

        if (!listeners.isEmpty()){      
            //log.fine("Notifying "+ uid_addr);
            for (SignalListener sl : listeners){
                sl.processSignal(smt);
            }
        }
    }
    
    
    

    /**
     * Creates a new instance of DataPointTooltip class with {{@link #pickingDistanceProperty() picking distance}
     * initialized to {@value #DEFAULT_PICKING_DISTANCE}.
     */
    public DataPointTooltip() {
        label.getStyleClass().add(STYLE_CLASS_LABEL);
        registerMouseEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
        registerMouseEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
    }

    /**
     * Creates a new instance of DataPointTooltip class.
     *
     * @param pickingDistance the initial value for the {@link #pickingDistanceProperty() pickingDistance} property
     */
    public DataPointTooltip(double pickingDistance) {
        this();
        setPickingDistance(pickingDistance);
    }

    private final DoubleProperty pickingDistance = new SimpleDoubleProperty(this, "pickingDistance",
            DEFAULT_PICKING_DISTANCE) {
        @Override
        protected void invalidated() {
            if (get() <= 0) {
                throw new IllegalArgumentException("The " + getName() + " must be a positive value");
            }
        }
    };

    /**
     * Distance of the mouse cursor from the data point (expressed in display units) that should trigger showing the
     * tool tip. By default initialized to {@value #DEFAULT_PICKING_DISTANCE}.
     *
     * @return the picking distance property
     */
    public final DoubleProperty pickingDistanceProperty() {
        return pickingDistance;
    }

    /**
     * Returns the value of the {@link #pickingDistanceProperty()}.
     *
     * @return the current picking distance
     */
    public final double getPickingDistance() {
        return pickingDistanceProperty().get();
    }

    /**
     * Sets the value of {@link #pickingDistanceProperty()}.
     *
     * @param distance the new picking distance
     */
    public final void setPickingDistance(double distance) {
        pickingDistanceProperty().set(distance);
    }

    private final EventHandler<MouseEvent> mouseMoveHandler = (MouseEvent event) -> {
        updateToolTip(event);
    };

    private final EventHandler<MouseEvent> mouseClickHandler = (MouseEvent event) -> {
        
        
        //LOG.info(String.format("Mouse event : %s ", event.getEventType().getName()));
        if (event.getButton() == MouseButton.PRIMARY){
            if (dataPoint == null){
                SMTraffic psel = new SMTraffic(0l, "CLEARPOINT", 0, "", null,new VirnaPayload());
                notifySignalListeners(psel);
                //LOG.info(String.format("Mouse primary clicked - no selection"));
            }
            else{
                //IsothermPoint ip = (IsothermPoint)dataPoint.data.getExtraValue();
                SMTraffic psel = new SMTraffic(0l, "POINTSELECTED", 0, "", dataPoint.getClass(),
                        new VirnaPayload().setObject(dataPoint)
                );
                notifySignalListeners(psel);
                //LOG.info(String.format("Mouse primary clicked - selection on "));
            }
        }
        else{
            LOG.info(String.format("Mouse secondary clicked")); 
        }
        
    };
    
    private void updateToolTip(MouseEvent event) {
       
        Bounds plotAreaBounds = getChartPane().getPlotAreaBounds();
        dataPoint = findDataPoint(event, plotAreaBounds);

        if (dataPoint == null) {
            getChartChildren().remove(label);
            return;
        }
        updateLabel(event, plotAreaBounds, dataPoint);
        if (!getChartChildren().contains(label)) {
            getChartChildren().add(label);
            label.requestLayout();
        }
    }

    private DataPoint findDataPoint(MouseEvent event, Bounds plotAreaBounds) {
        if (!plotAreaBounds.contains(event.getX(), event.getY())) {
            return null;
        }

        Point2D mouseLocation = getLocationInPlotArea(event);
        DataPoint nearestDataPoint = null;

        List<XYChart<X, Y>> charts = new ArrayList<>(getCharts());
        // Search points from top charts to bottom so that symbols drawn on top have precedence
        Collections.reverse(charts);
        for (XYChart<X, Y> chart : charts) {
            DataPoint point = findNearestDataPointWithinPickingDistance(chart, mouseLocation);
            if (nearestDataPoint == null
                    || (point != null && point.distanceFromMouse < nearestDataPoint.distanceFromMouse)) {
                nearestDataPoint = point;
            }
        }
        return nearestDataPoint;
    }

    
    private DataPoint findNearestDataPointWithinPickingDistance(XYChart<X, Y> chart, Point2D mouseLocation) {
        DataPoint nearestDataPoint = null;

        X xValue = toDataPoint(chart.getYAxis(), mouseLocation).getXValue();
        for (DataPoint dataPoint : findPointsToCheck(chart, xValue)) {
            Node node = dataPoint.data.getNode();
            if (node != null && node.isVisible()) {
                if (node.getBoundsInParent().contains(mouseLocation)) {
                    dataPoint.distanceFromMouse = 0;
                    return dataPoint;
                }
            } else {
                Point2D displayPoint = toDisplayPoint(chart.getYAxis(), dataPoint.data);
                dataPoint.distanceFromMouse = displayPoint.distance(mouseLocation);
                if (displayPoint.distance(mouseLocation) <= getPickingDistance() && (nearestDataPoint == null
                        || dataPoint.distanceFromMouse < nearestDataPoint.distanceFromMouse)) {
                    nearestDataPoint = dataPoint;
                }
            }
        }
        return nearestDataPoint;
    }

    private List<DataPoint> findPointsToCheck(XYChart<X, Y> chart, X xValue) {
        if (xValue instanceof Number) {
            return findNeighborPoints(chart, ((Number) xValue).doubleValue());
        }
        List<DataPoint> points = new LinkedList<>();
        for (Series<X, Y> series : chart.getData()) {
            for (Data<X, Y> data : series.getData()) {
                points.add(new DataPoint(series, data));
            }
        }
        return points;
    }

    private List<DataPoint> findNeighborPoints(XYChart<X, Y> chart, double searchedX) {
        List<DataPoint> points = new LinkedList<>();
        for (Series<X, Y> series : chart.getData()) {
            Pair<DataPoint, DataPoint> neighborPoints = findNeighborPoints(series, searchedX);
            if (neighborPoints.getKey() != null) {
                points.add(neighborPoints.getKey());
            }
            if (neighborPoints.getValue() != null) {
                points.add(neighborPoints.getValue());
            }
        }
        return points;
    }

    /**
     * Handles series that have data sorted or not sorted with respect to X coordinate.
     */
    private Pair<DataPoint, DataPoint> findNeighborPoints(Series<X, Y> series, double searchedX) {
        List<Data<? extends Number, Y>> seriesData = castXToNumber(series);

        int prevIndex = -1;
        int nextIndex = -1;
        double prevX = Double.MIN_VALUE;
        double nextX = Double.MAX_VALUE;

        for (int i = 0, size = seriesData.size(); i < size; i++) {
            double currentX = seriesData.get(i).getXValue().doubleValue();

            if (currentX < searchedX) {
                if (prevX < currentX) {
                    prevIndex = i;
                    prevX = currentX;
                }
            } else if (nextX > currentX) {
                nextIndex = i;
                nextX = currentX;
            }
        }
        DataPoint prevPoint = (prevIndex == -1) ? null : new DataPoint(series, series.getData().get(prevIndex));
        DataPoint nextPoint = (nextIndex == -1 || nextIndex == prevIndex) ? null
                : new DataPoint(series, series.getData().get(nextIndex));

        return new Pair<>(prevPoint, nextPoint);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Data<? extends Number, Y>> castXToNumber(Series series) {
        return series.getData();
    }

    private void updateLabel(MouseEvent event, Bounds plotAreaBounds, DataPoint dataPoint) {
        
        label.setText(dataPoint.series.getName() + "\n" + formatDataPoint(dataPoint));

        double mouseX = event.getX();
        double mouseY = event.getY();
        double width = label.prefWidth(-1);
        double height = label.prefHeight(width);

        double xLocation = mouseX + LABEL_X_OFFSET;
        double yLocation = mouseY - LABEL_Y_OFFSET - height;

        if (xLocation + width > plotAreaBounds.getMaxX()) {
            xLocation = mouseX - LABEL_X_OFFSET - width;
        }
        if (yLocation < plotAreaBounds.getMinY()) {
            yLocation = mouseY + LABEL_Y_OFFSET;
        }
        label.resizeRelocate(xLocation, yLocation, width, height);
    }

    private String formatDataPoint(DataPoint dataPoint) {
        return formatData(dataPoint.series.getChart().getYAxis(), dataPoint.data);
    }

    public class DataPoint {
        final Series<X, Y> series;
        final Data<X, Y> data;

        public Series<X, Y> getSeries() {
            return series;
        }

        public Data<X, Y> getData() {
            return data;
        }
        double distanceFromMouse;

        DataPoint(Series<X, Y> series, Data<X, Y> data) {
            this.series = series;
            this.data = data;
        }
        
        
        
    }
}
