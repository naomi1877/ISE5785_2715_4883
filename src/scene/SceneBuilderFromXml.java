package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


/**
 * This class is responsible for building a 3D scene from an XML file.
 * It parses the XML document and extracts information about the scene's
 * background color, ambient light, and geometries (spheres, triangles, planes, etc.).
 */
public class SceneBuilderFromXml {
    /**
     * The path to the folder containing the XML files.
     */
    private static final String FOLDER_PATH = System.getProperty("user.dir") + "/xml";

    /**
     * Default constructor for SceneBuilderFromXml.
     */
    public SceneBuilderFromXml() {
    }

    /**
     * Builds a Scene object from an XML file.
     *
     * @param fileName the path to the XML file
     * @return a Scene object populated with data from the XML file
     */
    public static Scene buildSceneFromXml(String fileName) {
        File file = new File(FOLDER_PATH + '/' + fileName + ".xml");
        try {
            // Parse and normalize the XML document
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(file);
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
     * Parses a color from a string.
     *
     * @param str the string representation of the color
     * @return a Color object
     */
    private static Color parseColor(String str) {
        //split the string by whitespace and parse each component
        String[] parts = str.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Color must have 3 components");
        return new Color(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    /**
     * Parses a point from a string.
     *
     * @param str the string representation of the point
     * @return a Point object
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

    // Parsing vector from a string

    /**
     * Parses a vector from a string.
     *
     * @param str the string representation of the vector
     * @return a Vector object
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

    // Parsing geometries from XML elements
    // Each geometry type has its own parsing method


    /**
     * Parses a sphere from an XML element.
     *
     * @param element the XML element representing the sphere
     * @return a Sphere object
     */
    private static Sphere parseSphere(Element element) {
        Point center = parsePoint(element.getAttribute("center"));
        double radius = Double.parseDouble(element.getAttribute("radius"));
        Sphere sphere = new Sphere(radius, center);

        //if (element.hasAttribute("emission")) {
        // sphere.setEmission(parseColor(element.getAttribute("emission")));
        //  }

        return sphere;
    }

    // Parsing a triangle

    /**
     * Parses a triangle from an XML element.
     *
     * @param element the XML element representing the triangle
     * @return a Triangle object
     */
    private static Triangle parseTriangle(Element element) {
        Point p0 = parsePoint(element.getAttribute("p0"));
        Point p1 = parsePoint(element.getAttribute("p1"));
        Point p2 = parsePoint(element.getAttribute("p2"));
        Triangle triangle = new Triangle(p0, p1, p2);

//        if (element.hasAttribute("emission")) {
//         //   triangle.setEmission(parseColor(element.getAttribute("emission")));
//        }

        return triangle;
    }

    /**
     * Parses a plane from an XML element.
     *
     * @param element the XML element representing the plane
     * @return a Plane object
     */
    private static Plane parsePlane(Element element) {
        // Assuming the plane is defined by a point and a normal vector
        Point q = parsePoint(element.getAttribute("q"));
        Vector normal = parseVector(element.getAttribute("normal"));
        Plane plane = new Plane(q, normal);

//        if (element.hasAttribute("emission")) {
//          //  plane.setEmission(parseColor(element.getAttribute("emission")));
//        }

        return plane;
    }

    // Parsing a cylinder

    /**
     * Parses a cylinder from an XML element.
     *
     * @param element the XML element representing the cylinder
     * @return a Cylinder object
     */
    private static Cylinder parseCylinder(Element element) {
        double radius = Double.parseDouble(element.getAttribute("radius"));
        double height = Double.parseDouble(element.getAttribute("height"));
        Point head = parsePoint(element.getAttribute("head"));
        Vector direction = parseVector(element.getAttribute("direction"));
        Cylinder cylinder = new Cylinder(radius, new Ray(head, direction), height);

//        if (element.hasAttribute("emission")) {
//          //  cylinder.setEmission(parseColor(element.getAttribute("emission")));
//        }

        return cylinder;
    }

}
