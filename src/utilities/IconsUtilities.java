package utilities;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class IconsUtilities {

    private static final String svgCross = "m386.667 45.564-45.564-45.564-147.77 147.769-147.769-147.769-45.564 45.564 147.769 147.769-147.769 147.77 45.564 45.564 147.769-147.769 147.769 147.769 45.564-45.564-147.768-147.77z";

    private static final String svgCircle = "m257.778 515.556c-142.137 0-257.778-115.642-257.778-257.778s115.641-257.778 257.778-257.778 257.778 115.641 257.778 257.778-115.642 257.778-257.778 257.778zm0-451.112c-106.61 0-193.333 86.723-193.333 193.333s86.723 193.333 193.333 193.333 193.333-86.723 193.333-193.333-86.723-193.333-193.333-193.333z";

    public static Group makeGroupCross(double scaleX, double scaleY) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgCross);
        icon.setFill(Color.web(ColorsUtilities.RED));
        icon.setScaleX(scaleX);
        icon.setScaleY(scaleY);
        Group group = new Group(icon);
        group.setTranslateX(2);
        group.setTranslateY(2);
        return group;
    }

    public static Group makeGroupCircle(double scaleX, double scaleY) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgCircle);
        icon.setFill(Color.web(ColorsUtilities.YELLOW));
        icon.setScaleX(scaleX);
        icon.setScaleY(scaleY);
        Group group = new Group(icon);
        group.setTranslateX(2);
        group.setTranslateY(2);
        return group;
    }

    public static Group makeGroupCross(double scaleX, double scaleY, String color) {
        return getGroup(scaleX, scaleY, color, svgCross);
    }

    public static Group makeGroupCircle(double scaleX, double scaleY, String color) {
        return getGroup(scaleX, scaleY, color, svgCircle);
    }

    private static Group getGroup(double scaleX, double scaleY, String color, String svgCross) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgCross);
        icon.setFill(Color.web(color));
        icon.setScaleX(scaleX);
        icon.setScaleY(scaleY);
        Group group = new Group(icon);
        group.setTranslateX(2);
        group.setTranslateY(2);
        return group;
    }

    public static SVGPath makeCross(double scaleX, double scaleY) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgCross);
        icon.setFill(Color.web(ColorsUtilities.RED));
        icon.setScaleX(scaleX);
        icon.setScaleY(scaleY);
        return icon;
    }

    public static SVGPath makeCircle(double scaleX, double scaleY) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgCircle);
        icon.setFill(Color.web(ColorsUtilities.YELLOW));
        icon.setScaleX(scaleX);
        icon.setScaleY(scaleY);
        return icon;
    }
}
