package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.*;
import primitives.Color;
import primitives.Point;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SceneBuilderFromXml {

    public static Scene buildSceneFromXml(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element sceneElement = doc.getDocumentElement();
            Scene scene = new Scene("XML Scene");

            //background color
            String background = sceneElement.getAttribute("background-color");
            if (!background.isEmpty()) {
                scene.setBackground(parseColor(background));
            }

            // Parse the ambient light
            Node ambientNode = doc.getElementsByTagName("ambient-light").item(0);
            if (ambientNode instanceof Element ambientElement) {
                scene.setAmbientLight(new AmbientLight(parseColor(ambientElement.getAttribute("color"))));
            }

            // Parse the geometries
            Geometries geometries = new Geometries();
            NodeList geometryList = doc.getElementsByTagName("geometries").item(0).getChildNodes();

            for (int i = 0; i < geometryList.getLength(); i++) {
                Node node = geometryList.item(i);
                if (!(node instanceof Element geo)) continue;
                switch (geo.getTagName()) {
                    case "sphere" -> {
                        Point center = parsePoint(geo.getAttribute("center"));
                        double radius = Double.parseDouble(geo.getAttribute("radius"));
                        geometries.add(new Sphere(radius, center));
                    }
                    case "triangle" -> {
                        Point p0 = parsePoint(geo.getAttribute("p0"));
                        Point p1 = parsePoint(geo.getAttribute("p1"));
                        Point p2 = parsePoint(geo.getAttribute("p2"));
                        geometries.add(new Triangle(p0, p1, p2));
                    }
                }
            }

            scene.setGeometries(geometries);
            return scene;

        } catch (Exception e) {
            throw new RuntimeException("Failed to build scene from XML", e);
        }
    }

    private static Color parseColor(String str) {
        String[] parts = str.trim().split("\\s+");
        return new Color(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    private static Point parsePoint(String str) {
        String[] parts = str.trim().split("\\s+");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}

