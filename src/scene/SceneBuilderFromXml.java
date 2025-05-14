package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.*;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SceneBuilderFromXml {
    /**
     *
     * @param filePath
     * @return
     */
    public static Scene buildSceneFromXml(String filePath) {
        try {
            // Parse and normalize the XML document
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new File(filePath));
            doc.getDocumentElement().normalize();

            Element sceneElement = doc.getDocumentElement();
            Scene scene = new Scene("XML Scene");

            // ===== Background color =====
            if (sceneElement.hasAttribute("background-color")) {
                scene.setBackground(parseColor(sceneElement.getAttribute("background-color")));
            }

            // ===== Ambient light =====
            Node ambientNode = doc.getElementsByTagName("ambient-light").item(0);
            if (ambientNode instanceof Element ambientElement && ambientElement.hasAttribute("color")) {
                scene.setAmbientLight(new AmbientLight(parseColor(ambientElement.getAttribute("color"))));
            }

            // ===== Geometries =====
            NodeList geometriesNodeList = doc.getElementsByTagName("geometries");
            if (geometriesNodeList.getLength() > 0) {
                Element geometriesElement = (Element) geometriesNodeList.item(0);
                NodeList geometryList = geometriesElement.getChildNodes();
                Geometries geometries = new Geometries();

                for (int i = 0; i < geometryList.getLength(); i++) {
                    Node node = geometryList.item(i);
                    if (!(node instanceof Element geoElement)) continue;

                    Geometry geometry = switch (geoElement.getTagName()) {
                        case "sphere" -> parseSphere(geoElement);
                        case "triangle" -> parseTriangle(geoElement);
                        case "plane" -> parsePlane(geoElement);
                        case "cylinder" -> parseCylinder(geoElement);
                        // Add more geometry types as needed
                        default -> null;
                    };

                    if (geometry != null) {
                        geometries.add(geometry);
                    }
                }

                scene.setGeometries(geometries);
            }

            return scene;

        } catch (Exception e) {
            throw new RuntimeException("Failed to build scene from XML", e);
        }
    }

    // HELPERS

    /**
     *
     * @param str
     * @return Color
     */
    private static Color parseColor(String str) {
        String[] parts = str.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Color must have 3 components");
        return new Color(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    /**
     *
     * @param str
     * @return Point
     */
    private static Point parsePoint(String str) {
        String[] parts = str.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Point must have 3 coordinates");
        return new Point(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    /**
     * Parses a vector from a string.
     * @param str The string representation of the vector, e.g., "1 2 3".
     * @return A Vector object.
     */
    private static Vector parseVector(String str) {
        String[] parts = str.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Vector must have 3 components");
        return new Vector(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    /**
     *
     * @param element
     * @return
     */
    private static Sphere parseSphere(Element element) {
        Point center = parsePoint(element.getAttribute("center"));
        double radius = Double.parseDouble(element.getAttribute("radius"));
        Sphere sphere = new Sphere(radius, center);

        if (element.hasAttribute("emission")) {
            // sphere.setEmission(parseColor(element.getAttribute("emission")));
        }

        return sphere;
    }

    /**
     *
     * @param element
     * @return Triangle
     */
    private static Triangle parseTriangle(Element element) {
        Point p0 = parsePoint(element.getAttribute("p0"));
        Point p1 = parsePoint(element.getAttribute("p1"));
        Point p2 = parsePoint(element.getAttribute("p2"));
        Triangle triangle = new Triangle(p0, p1, p2);

        if (element.hasAttribute("emission")) {
            //   triangle.setEmission(parseColor(element.getAttribute("emission")));
        }

        return triangle;
    }

    /**
     *
     * @param element
     * @return Plane
     */
    private static Plane parsePlane(Element element) {
        Point q = parsePoint(element.getAttribute("q"));
        Vector normal = parseVector(element.getAttribute("normal"));
        Plane plane = new Plane(q, normal);

        if (element.hasAttribute("emission")) {
            //  plane.setEmission(parseColor(element.getAttribute("emission")));
        }

        return plane;
    }

    /**
     *
     * @param element
     * @return Cylinder
     */
    private static Cylinder parseCylinder(Element element) {
        double radius = Double.parseDouble(element.getAttribute("radius"));
        double height = Double.parseDouble(element.getAttribute("height"));
        Point head = parsePoint(element.getAttribute("head"));
        Vector direction = parseVector(element.getAttribute("direction"));
        Cylinder cylinder = new Cylinder(radius, new Ray(head, direction), height);

        if (element.hasAttribute("emission")) {
            //  cylinder.setEmission(parseColor(element.getAttribute("emission")));
        }

        return cylinder;
    }

}
