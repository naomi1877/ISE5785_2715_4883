package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;


public class MyPicturesTests {

    private final Scene scene = new Scene("My tests");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);
    @Test
    void oceanSunsetCrystalWithLightPath() {

        scene.geometries.add(
                // הים – מישור תכלת כהה עם החזר שמש גבוה
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(30, 70, 90))  // כחול עמוק לים
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.5).setShininess(100)
                                .setKR(0.5)),

                // השמיים – מישור אחורי כתום שקיעה
                new Plane(new Point(0, 100, -300), new Vector(0, -1, 0))
                        .setEmission(new Color(255, 140, 50))  // כתום שקיעה
                        .setMaterial(new Material().setKD(1)),

                // השמש – כדור כתום חם, חצי מעל ומעל פני הים (Y= -50 הוא קו האופק)
                new Sphere(20, new Point(0, -30, -100))  // Y=-30 כדי שיחצה קו Y=-50 (הים)
                        .setEmission(new Color(255, 150, 50))  // כתום חם
                        .setMaterial(new Material()
                                .setKD(0.1).setKS(0.8).setShininess(300)),

                // שביל האור במים – כדור קטן שקוף, מבריק וכתום, ממש מעל פני הים
                new Sphere(8, new Point(0, -49.9, -100))  // קטן וקרוב לפני הים
                        .setEmission(new Color(255, 180, 120))
                        .setMaterial(new Material()
                                .setKT(0.7)   // שקיפות גבוהה
                                .setKS(0.6).setShininess(200)  // מבריק
                                .setKD(0.1))
        );

        // אור שקיעה כתום עדין – משקף את השמש
        scene.lights.add(
                new PointLight(new Color(255, 140, 50), new Point(0, 50, -120))
                        .setKL(0.0005).setKQ(0.00005)
        );

        // תאורה סביבתית חמה רכה
        scene.setAmbientLight(new AmbientLight(new Color(80, 40, 20)));

        // מצלמה
        cameraBuilder
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Point(0, 0, -100), Vector.AXIS_Y)
                .setVpDistance(100)
                .setVpSize(200, 200)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("oceanSunsetCrystalWithLightPath");
    }


    @Test
    void windmillDemoTest1() {
        Scene scene = new Scene("Windmill Scene");
        Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
        double bladeLength = 120;
        double bladeWidth = 80;
        scene.geometries.add(
                // עמוד התחנה
                new Cylinder(
                        49, // רדיוס וגובה
                        new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
                        , 300).setEmission(new Color(100,80,50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
                //גג
                ,new Sphere(49, new Point(0, 150, 0))
                        .setEmission(new Color(89,72,38))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),
                //  .setEmission(new Color(60, 60, 120))כנפיים אטומים
                //  .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
                // מרכז הטורבינה
                new Sphere(10, pivot)
                        .setEmission(new Color(80, 40, 100))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),
                // כנף עליונה
                new Triangle(
                        pivot,
                        new Point(0, 150 + bladeLength, 85),
                        new Point(bladeWidth, 150 + bladeLength-30, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף תחתונה
                new Triangle(
                        pivot,
                        new Point(0, 150 - bladeLength, 85),
                        new Point(-bladeWidth , 150 - bladeLength+30, 50)
                ).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף  ימנית
                new Triangle(
                        pivot,
                        new Point(bladeLength, 150, 85),
                        new Point(bladeLength-30, 150 - bladeWidth, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף שמאלית
                new Triangle(
                        pivot,
                        new Point( - bladeLength , 150, 85),
                        new Point( -bladeLength+30 , 150+bladeWidth, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                //new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                // מישור קרקע
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),
                // רקע אחורי
                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1))
        );
        // תאורה
        //scene.setAmbientLight(new AmbientLight(new Color(50, 30, 20) ) );
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(
//              new SpotLight(new Color(700, 400, 400), new Point(200, 200, 200), new Vector(-2, -2, -3))
//                      .setKL(0.0001).setKQ(0.00001)
                new SpotLight(new Color(800, 400, 200), new Point(200, 200, 200), new Vector(-2, -2, -3))
                        .setKL(0.0001).setKQ(0.00001)
        );
        // מצלמה (כפי שכבר בחרת)
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 100, 600))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800);

        cameraBuilder
                .build()
                .renderImage()
                .writeToImage("windmillDemo10");
    }

    @Test
    void windmillDemoTest2() {
        Scene scene = new Scene("Windmill Scene");
        // Point pivot = new Point(20, 147, 60);
        // Point pivot = new Point(30, 140, 40);
        Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
        double bladeLength = 120;
        double bladeWidth = 80;
        scene.geometries.add(
                // עמוד התחנה
                new Cylinder(
                        49, // רדיוס וגובה
                        new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
                        , 300).setEmission(new Color(100,80,50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
                //גג
                ,new Sphere(49, new Point(0, 147, 0))
                        .setEmission(new Color(89,72,38))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),
                //  .setEmission(new Color(60, 60, 120))כנפיים אטומים
                //  .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
                // כנף עליונה
                new Triangle(
                        pivot,
                        new Point(0, 150 + bladeLength, 85),
                        new Point(bladeWidth, 150 + bladeLength-bladeWidth, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף תחתונה
                new Triangle(
                        pivot,
                        new Point(0, 150 - bladeLength, 85),
                        new Point(-bladeWidth , 150 - bladeLength+bladeWidth, 50)
                ).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף  ימנית
                new Triangle(
                        pivot,
                        new Point(bladeLength, 150, 85),
                        new Point(bladeLength-bladeWidth, 150 - bladeWidth, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // כנף שמאלית
                new Triangle(
                        pivot,
                        new Point( - bladeLength , 150, 85),
                        new Point( -bladeLength+bladeWidth , 150+bladeWidth, 50))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                // מרכז הטורבינה
                new Sphere(10, pivot)
                        .setEmission(new Color(80, 40, 100))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),
                //new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                // מישור קרקע
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),
                // רקע אחורי
                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1))
        );
        // תאורה
        //scene.setAmbientLight(new AmbientLight(new Color(50, 30, 20) ) );
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(
//              new SpotLight(new Color(700, 400, 400), new Point(200, 200, 200), new Vector(-2, -2, -3))
//                      .setKL(0.0001).setKQ(0.00001)
                new SpotLight(new Color(800, 400, 200), new Point(200, 200, 200), new Vector(-2, -2, -3))
                        .setKL(0.0001).setKQ(0.00001)
        );
        // מצלמה (כפי שכבר בחרת)
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 100, 600))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800);

        cameraBuilder
                .build()
                .renderImage()
                .writeToImage("windmillDemo9");
    }


    @Test
    void cylinderAloneTest() {
        Scene scene = new Scene("Cylinder Test");

//        // גליל לבד – בצבע חזק ומרכז המסך
//        scene.geometries.add(
//                new Cylinder(
//                        40, // רדיוס וגובה
//                        new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
//                        , 300).setEmission(new Color(150, 20, 20)) // אדום בוהק
//                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
//        );
        scene.geometries.add(new Cone( 100, // רדיוס וגובה
                new Ray(new Point(0, 150, 0), new Vector(0, -1, 0)) // ממרכז כלפי מעלה
                , 300).setEmission(new Color(150, 20, 20)) // אדום בוהק
                .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)));

        // מישור רקע אחורי
        scene.geometries.add(
                new Plane(new Point(0, 0, -400), new Vector(0, 0, 1))
                        .setEmission(new Color(100, 120, 160))
                        .setMaterial(new Material().setKD(1))
        );

        // מישור רצפה
        scene.geometries.add(
                new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(40, 30, 20))
                        .setMaterial(new Material().setKD(1))
        );

        // תאורה
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));

        scene.lights.add(new SpotLight(
                new Color(600, 400, 300),
                new Point(200, 200, 200),
                new Vector(-2, -2, -3)
        ).setKL(0.0001).setKQ(0.00001));

        // מצלמה פשוטה שמסתכלת בדיוק על הגליל
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 100, 500)) // קרובה וישירה
                .setDirection(new Point(0, 0, 0), new Vector(0, 1, -1))
                .setVpDistance(500)
                .setVpSize(500, 500)
                .setResolution(800, 800);

        cameraBuilder
                .build()
                .renderImage()
                .writeToImage("coneAloneTest");
    }


    @Test
    void tinyCylinderTopViewTest() {
        Scene scene = new Scene("Tiny Cylinder Caps");

//        // גליל קצר ורחב – קל לראות בסיסים
//        scene.geometries.add(
//                new Cylinder(
//                        40, // רדיוס
//                        new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)),
//                        10  // גובה קטן
//                ).setEmission(new Color(200, 30, 30))
//                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))
//        );

        scene.geometries.add(
                new Pyramid(new Point(0,0,0),new Polygon(
                        new Point(-50,200,-50),
                        new Point(50,200,-50),
                        new Point(100,200,50),
                        new Point(-50,200,50)
                )).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
        // רקע
        scene.geometries.add(
                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(150, 180, 255))
                        .setMaterial(new Material().setKD(1))
        );

        // תאורה
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400),
                        new Point(100, 200, 200),
                        new Vector(-1, -2, -2)
                ).setKL(0.0001).setKQ(0.00001)
        );

        // מצלמה – מבט מלמעלה
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 100, 300))
                .setDirection(new Point(0, 5, 0), new Vector(0, -1, -1))
                .setVpDistance(300)
                .setVpSize(500, 500)
                .setResolution(800, 800);

        cameraBuilder
                .build()
                .renderImage()
                .writeToImage("Pyramid1");
    }

    @Test
    void testTransparentPyramidRender() {
        Scene scene = new Scene("Transparent Pyramid Test");

        // בסיס הפירמידה – מלבן בגובה 200
        Polygon base = new Polygon(
                new Point(-50, 200, -50),
                new Point(50, 200, -50),
                new Point(50, 200, 50),
                new Point(-50, 200, 50)
        );

        // קודקוד הפירמידה בגובה 0
        Point apex = new Point(0, 0, 0);

        // יצירת פירמידה
        Pyramid pyramid =new Pyramid(apex, base);
        pyramid .setEmission(new Color(red)) // צבע כחול בהיר
                .setMaterial(new Material()
                        .setKD(0.2)
                        .setKS(0.6)
                        .setShininess(200)
                        .setKT(0.6)); // שקיפות

        // הוספה לסצנה
        scene.geometries.add(pyramid);

        // מישור רקע
        scene.geometries.add(
                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(150, 180, 255))
                        .setMaterial(new Material().setKD(1))
        );

        // תאורה
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.lights.add(
                new SpotLight(new Color(800, 500, 300),
                        new Point(100, 300, 300),
                        new Vector(-1, -2, -2))
                        .setKL(0.0001)
                        .setKQ(0.00001)
        );

        // מצלמה - מבט מקדימה בזווית אלכסונית
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 300, 400))
                .setDirection(new Point(0, 100, 0), new Vector(0, -1, -1))
                .setVpDistance(400)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .build();
        // רינדור
        camera.renderImage().writeToImage("Pyramid2");
    }


    @Test
    void windmillRotatedTest() {
        Scene scene = new Scene("Rotated Windmill Scene");

        Point pivot = new Point(0, 150, 49); // מרכז התחנה
        double bladeLength = 120;
        double bladeWidth = 80;
        double angle = 30; // מעלות סיבוב סביב ציר Y

        // עמוד התחנה
        scene.geometries.add(new Cylinder(
                49,
                new Ray(
                        rotateAroundY(pivot, new Point(0, -150, 0), angle),
                        rotateAroundY(new Vector(0, 1, 0), angle)
                ),
                300).setEmission(new Color(100, 80, 50))
                .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)));

        // גג התחנה
        scene.geometries.add(new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                .setEmission(new Color(89, 72, 38))
                .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)));

        // כדור מרכז הכנפיים
        scene.geometries.add(new Sphere(10, rotateAroundY(pivot, pivot, angle))
                .setEmission(new Color(80, 40, 100))
                .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)));

        // כנפיים כפירמידות (ניתן להחליף ב-Triangle אם תרצי)
        scene.geometries.add(
                // עליונה
                new Pyramid(
                        rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 70), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 70), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength - 30, 30), angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength - 30, 30), angle)
                        )
                ).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),

                // תחתונה
                new Pyramid(
                        rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 70), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 70), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength + 30, 30), angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength + 30, 30), angle)
                        )
                ).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4))
        );

        // תאורה וסביבה
        scene.geometries.add(new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)));

        scene.geometries.add(new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                .setEmission(new Color(20, 20, 50))
                .setMaterial(new Material().setKD(1)));

        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(new SpotLight(new Color(800, 400, 200), new Point(200, 200, 200), new Vector(-2, -2, -3))
                .setKL(0.0001).setKQ(0.00001));

        // מצלמה
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 100, 600))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .build();

        camera.renderImage().writeToImage("windmillRotated30");
    }

    /**
     * Rotates a point around the Y-axis by a given angle, relative to a center point.
     *
     * The rotation is performed in the XZ-plane, while the Y-coordinate remains unchanged.
     * Positive angles represent counter-clockwise rotation when looking from above.</p>
     *
     * @param center the center of rotation (pivot point)
     * @param p the point to rotate
     * @param angleDegrees the rotation angle in degrees (positive = counter-clockwise)
     * @return the new point after rotation
     */
    public static Point rotateAroundY(Point center, Point p, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        double dx = p.getX() - center.getX();
        double dz = p.getZ() - center.getZ();

        double newX = dx * cos + dz * sin;
        double newZ = -dx * sin + dz * cos;

        return new Point(center.getX() + newX, p.getY(), center.getZ() + newZ);
    }

    public static Vector rotateAroundY(Vector v, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        double x = v.getX();
        double z = v.getZ();

        double newX = x * cos + z * sin;
        double newZ = -x * sin + z * cos;

        return new Vector(newX, v.getY(), newZ);
    }

    public static Point rotateAroundX(Point center, Point p, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        double dy = p.getY() - center.getY();
        double dz = p.getZ() - center.getZ();

        double newY = dy * cos - dz * sin;
        double newZ = dy * sin + dz * cos;

        return new Point(p.getX(), center.getY() + newY, center.getZ() + newZ);
    }


    @Test
    void testWithoutRotation() {
        Scene scene = new Scene("Windmill With Pyramid Blades");
        Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
        double bladeLength = 120;
        double bladeWidth = 80;
        double bladeDepth = 40;
        // חומר וצבע לכנפיים
        //   Material bladeMaterial = new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4);
        Material bladeMaterial = new Material()
                .setKD(0.3)
                .setKS(0.3)
                .setShininess(120)
                .setKT(0.4);
        Color bladeColor = new Color(BLUE);

        // בניית כנפיים כ-Pyramid
        scene.geometries.add(
                // עמוד
                new Cylinder(49, new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)), 300)
                        .setEmission(new Color(100, 80, 50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                // גג
                new Sphere(49, new Point(0, 150, 0))
                        .setEmission(new Color(89, 72, 38))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                // מרכז טורבינה
                new Sphere(10, pivot)
                        .setEmission(new Color(80, 40, 100))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                // כנף עליונה
                new Pyramid(pivot, new Polygon(
                        new Point(-bladeWidth / 2, 150 + bladeLength, 50),
                        new Point(bladeWidth / 2, 150 + bladeLength, 50),
                        new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth),
                        new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth)))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                // כנף תחתונה
                new Pyramid(pivot, new Polygon(
                        new Point(-bladeWidth / 2, 150 - bladeLength, 50),
                        new Point(bladeWidth / 2, 150 - bladeLength, 50),
                        new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth),
                        new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth)))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                // כנף ימנית
                new Pyramid(pivot, new Polygon(
                        new Point(bladeLength, 150 - bladeWidth / 2, 50),
                        new Point(bladeLength, 150 + bladeWidth / 2, 50),
                        new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth),
                        new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth)))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                // כנף שמאלית
                new Pyramid(pivot, new Polygon(
                        new Point(-bladeLength, 150 - bladeWidth / 2, 50),
                        new Point(-bladeLength, 150 + bladeWidth / 2, 50),
                        new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth),
                        new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth)))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                // קרקע
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                // רקע
                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1))
        );

        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(
                new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                        .setKL(0.0001).setKQ(0.00001)
        );
        scene.lights.add(new PointLight(new Color(250, 150, 50), new Point(-200, 250, 300))
                .setKL(0.0001)
                .setKQ(0.00001));

        Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)// מיקום גבוה יותר, קצת אחורה וימינה
                // מיקום המצלמה - קצת יותר גבוה
                .setLocation(new Point(0, 300, 600))
                // כיוון המצלמה - מעט למטה
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("windmillPyramid12");

    }
        @Test
        void finalTestWithOtherGeometries () {
            Scene scene = new Scene("Windmill With Pyramid Blades");
            Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
            double bladeLength = 105;
            double bladeWidth = 60;
            double bladeDepth = 40;
            double angle = 28;
            // חומר וצבע לכנפיים
            //   Material bladeMaterial = new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4);
            Material bladeMaterial = new Material()
                    .setKD(0.3)
                    .setKS(0.3)
                    .setShininess(120)
                    .setKT(0.4);
            Color bladeColor = new Color(BLUE);


//תחנת קמח
            scene.geometries.add(
                    // עמוד
                    new Cylinder(49,
                            new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                                    rotateAroundY(new Vector(0, 1, 0), angle)),
                            300)
                            .setEmission(new Color(100, 80, 50))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                    // גג
                    new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                            .setEmission(new Color(89, 72, 38))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    // מרכז טורבינה
                    new Sphere(10, rotateAroundY(pivot, pivot, angle))
                            .setEmission(new Color(80, 40, 100))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    // כנף עליונה
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף תחתונה
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף ימנית
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף שמאלית
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // קרקע
                    new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                            .setEmission(new Color(20, 20, 20))
                            .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                    // רקע
                    new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                            .setEmission(new Color(20, 20, 50))
                            .setMaterial(new Material().setKD(1))
            );

            scene.geometries.add(
                    new Sphere(65, new Point(220, 10, 200))
                            .setEmission(new Color(8, 8, 12))
                            .setMaterial(new Material()
                                    .setKD(0.1)    // מעט פיזור אור (Diffuse)
                                    .setKS(0.2)    // השתקפות מבריקה נמוכה
                                    .setShininess(500) // מאוד מבריק (עוד יותר מהקודם)
                                    .setKR(0.95)), // השתקפות מאוד גבוהה - מראה כמעט מושלם
//                new Cylinder(30,    new Ray(new Point(130, 0, 140), new Vector(0, 1, 0)), 120)
//                        .setEmission(new Color(8, 8, 12))
//                        .setMaterial(new Material()
//                                .setKD(0.1)    // מעט פיזור אור (Diffuse)
//                                .setKS(0.2)    // השתקפות מבריקה נמוכה
//                                .setShininess(500) // מאוד מבריק (עוד יותר מהקודם)
//                                .setKR(0.95)), // השתקפות מאוד גבוהה - מראה כמעט מושלם

                    new Sphere(50, new Point(120, 20, 80))
                            .setEmission(new Color(blue))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(150)),

                    new Cone(60, new Ray(new Point(-200, 150, 60), new Vector(0, -1, 0)), 170)
                            .setEmission(new Color(blue))
                            .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(180).setKT(0.3))
                    ,
                    new Sphere(30, new Point(-300, 50, 100))
                            .setEmission(new Color(blue))
                            .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(180).setKT(0.3))
                    // מישור קרקע

            );

            scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
            scene.lights.add(
                    new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                            .setKL(0.0001).setKQ(0.00001)
            );

            scene.lights.add(
                    new SpotLight(
                            new Color(800, 300, 100),               // גוון חמים של שקיעה
                            new Point(-300, 300, 400),              // מיקום רחוק באופק
                            new Vector(2, -2, -3))                  // כיוון אל הסצנה
                            .setKL(0.00005).setKQ(0.00001)
            );

            Camera camera = Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)// מיקום גבוה יותר, קצת אחורה וימינה
                    // מיקום המצלמה - קצת יותר גבוה
                    .setLocation(new Point(0, 300, 800))
                    // כיוון המצלמה - מעט למטה
                    .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                    .setVpDistance(600)
                    .setVpSize(500, 500)
                    .setResolution(800, 800)
                    .setAAFlag(false)
                    .build();
            camera.renderImage()
                    .writeToImage("windmillPyramid14");

        }


        @Test
        void finalTest () {
            Scene scene = new Scene("Windmill With Pyramid Blades");
            Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
            double bladeLength = 105;
            double bladeWidth = 60;
            double bladeDepth = 40;
            double angle = 28;
            // חומר וצבע לכנפיים
            //   Material bladeMaterial = new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4);
            Material bladeMaterial = new Material()
                    .setKD(0.3)
                    .setKS(0.3)
                    .setShininess(120)
                    .setKT(0.4);
            Color bladeColor = new Color(BLUE);

//תחנת קמח
            scene.geometries.add(
                    // עמוד
                    new Cylinder(49,
                            new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                                    rotateAroundY(new Vector(0, 1, 0), angle)),
                            300)
                            .setEmission(new Color(100, 80, 50))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                    // גג
                    new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                            .setEmission(new Color(89, 72, 38))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    // מרכז טורבינה
                    new Sphere(10, rotateAroundY(pivot, pivot, angle))
                            .setEmission(new Color(80, 40, 100))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    // כנף עליונה
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף תחתונה
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף ימנית
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // כנף שמאלית
                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    // קרקע
                    new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                            .setEmission(new Color(20, 20, 20))
                            .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                    // רקע
                    new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                            .setEmission(new Color(20, 20, 50))
                            .setMaterial(new Material().setKD(1))
            );


            scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
            scene.lights.add(
                    new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                            .setKL(0.0001).setKQ(0.00001)
            );

            scene.lights.add(
                    new SpotLight(
                            new Color(800, 300, 100),               // גוון חמים של שקיעה
                            new Point(-300, 300, 400),              // מיקום רחוק באופק
                            new Vector(2, -2, -3))                  // כיוון אל הסצנה
                            .setKL(0.00005).setKQ(0.00001)
            );

            Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)// מיקום גבוה יותר, קצת אחורה וימינה
                    // מיקום המצלמה - קצת יותר גבוה
                    .setLocation(new Point(0, 300, 800))
                    // כיוון המצלמה - מעט למטה
                    .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                    .setVpDistance(600)
                    .setVpSize(500, 500)
                    .setResolution(800, 800)
                    .build()
                    .renderImage()
                    .writeToImage("windmillPyramid13");
        }

        @Test
        void windmillPyramid15 () {
            Scene scene = new Scene("Windmill With Pyramid Blades");
            Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
            double bladeLength = 105;
            double bladeWidth = 60;
            double bladeDepth = 40;
            double angle = 28;
            Material bladeMaterial = new Material()
                    .setKD(0.3)
                    .setKS(0.3)
                    .setShininess(120)
                    .setKT(0.4);
            Color bladeColor = new Color(BLUE);

            // תחנת קמח - כמו שהיה במקור (ללא שינוי)
            scene.geometries.add(
                    new Cylinder(49,
                            new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                                    rotateAroundY(new Vector(0, 1, 0), angle)),
                            300)
                            .setEmission(new Color(100, 80, 50))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                    new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                            .setEmission(new Color(89, 72, 38))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    new Sphere(10, rotateAroundY(pivot, pivot, angle))
                            .setEmission(new Color(80, 40, 100))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                            .setEmission(new Color(20, 20, 20))
                            .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                    new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                            .setEmission(new Color(20, 20, 50))
                            .setMaterial(new Material().setKD(1))
            );

            // --- הכדורים בצד ימין של התחנה במשולש עם הכדור הגדול בקדמת התמונה משמאל ---
            // הכדור הגדול - קדמת משולש, משמאל (קדמת התמונה)

            // הכדורים הקטנים - מאחוריו, בצד ימין
            scene.geometries.add(
                    new Sphere(60, new Point(220, 10, 180))
                            .setEmission(new Color(8, 8, 12))
                            .setMaterial(new Material()
                                    .setKD(0.1)    // מעט פיזור אור (Diffuse)
                                    .setKS(0.2)    // השתקפות מבריקה נמוכה
                                    .setShininess(500) // מאוד מבריק (עוד יותר מהקודם)
                                    .setKR(0.95)), // השתקפות מאוד גבוהה - מראה כמעט מושלם

                    new Sphere(25, new Point(120, 10, 150)) // שמאלי - שמור כפי שהיה
                            .setEmission(new Color(10, 10, 20))
                            .setMaterial(new Material()
                                    .setKD(0.1).setKS(0.3).setShininess(400)
                                    .setKT(0.85).setKR(0.15)),
                    new Sphere(45, new Point(170, 15, 90))
                            .setEmission(new Color(BLUE)) // צבע כנפי הפרמידה
                            .setMaterial(new Material()
                                    .setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),
                    // new Cone(50, new Ray(new Point(-200, 150, 160), new Vector(0, -1, 0)), 170)
                    new Cone(50, new Ray(new Point(300, 150, 0), new Vector(0, -1, 0)), 170)
                            .setEmission(new Color(blue))
                            .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(180).setKT(0.3)),
                    // יצירת פירמידה
                    new Pyramid(new Point(-100, 100, 220),
                            new Polygon(
                                    new Point(-120, -20, 190),
                                    new Point(-50, -20, 190),
                                    new Point(-60, -20, 260),
                                    new Point(-140, -20, 260))).setEmission(new Color(red)) // צבע כחול בהיר
                            .setMaterial(new Material()
                                    .setKD(0.2)
                                    .setKS(0.6)
                                    .setShininess(200)
                                    .setKT(0.6)) // שקיפות
//                , new Box(
//                        new Double3(250, 0, 0),   // פינה תחתונה קדמית
//                        new Double3(320, 70,60)   // פינה עליונה אחורית
//                )
//                        .setEmission(new Color(60, 100, 180))
//                        .setMaterial(new Material()
//                                .setKD(0.1)
//                                .setKS(0.3)
//                                .setKT(0.5)
//                                .setShininess(300))
            );
            /// //////////////////////////////
//3 Asryeli Tours
            scene.geometries.add(
                    new Cylinder(38,
                            new Ray(new Point(-290, -150, 0), new Vector(0, 1, 0)),
                            360)
                            .setEmission(new Color(black))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),
                    new Box(new Double3(-200, -150, -10),//
                            new Double3(-124, 210, -86))
                            .setEmission(new Color(black))
                            .setMaterial(new Material().setKD(0.5).setKS(0.6).setShininess(100).setKT(0.4)),
                    new Pyramid(new Point(-250, -50, -70),
                            new Polygon(
                                    new Point(-298, 210, -98),
                                    new Point(-220, 210, -130),
                                    new Point(-222, 210, -22)))
                            .setEmission(new Color(black))
                            .setMaterial(new Material().setKD(0.5).setKS(0.6).setShininess(100).setKT(0.4)),
                    new Pyramid(new Point(-250, 200, -70),
                            new Polygon(
                                    new Point(-298, -50, -98),
                                    new Point(-220, -50, -130),
                                    new Point(-222, -50, -22)))
                            .setEmission(new Color(black))
                            .setMaterial(new Material().setKD(0.5).setKS(0.6).setShininess(100).setKT(0.4))


            );


            // תאורה
            scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
            scene.lights.add(
                    new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                            .setKL(0.0001).setKQ(0.00001)
            );

            scene.lights.add(
                    new SpotLight(
                            new Color(800, 300, 100),               // גוון חמים של שקיעה
                            new Point(-300, 300, 400),              // מיקום רחוק באופק
                            new Vector(2, -2, -3))                  // כיוון אל הסצנה
                            .setKL(0.00005).setKQ(0.00001)
            );

            // מיקום המצלמה - כמו שהיה
            Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .setLocation(new Point(0, 500, 800))
                    .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                    .setVpDistance(600)
                    .setVpSize(500, 500)
                    .setResolution(800, 800)
                    .build()
                    .renderImage()
                    .writeToImage("windmillPyramid15");
        }

        //Test for MP1
        @Test
        void windmillPyramid15_withAA() {
            Scene scene = new Scene("Windmill With Pyramid Blades");
            Point pivot = new Point(0, 150, 49); // נקודת חיבור הכנפיים
            double bladeLength = 105;
            double bladeWidth = 60;
            double bladeDepth = 40;
            double angle = 28;
            Material bladeMaterial = new Material()
                    .setKD(0.3)
                    .setKS(0.3)
                    .setShininess(120)
                    .setKT(0.4);
            Color bladeColor = new Color(BLUE);

            // תחנת קמח - כמו שהיה במקור (ללא שינוי)
            scene.geometries.add(
                    new Cylinder(49,
                            new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                                    rotateAroundY(new Vector(0, 1, 0), angle)),
                            300)
                            .setEmission(new Color(100, 80, 50))
                            .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                    new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                            .setEmission(new Color(89, 72, 38))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    new Sphere(10, rotateAroundY(pivot, pivot, angle))
                            .setEmission(new Color(80, 40, 100))
                            .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Pyramid(rotateAroundY(pivot, pivot, angle),
                            new Polygon(
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                    rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                            ))
                            .setEmission(bladeColor)
                            .setMaterial(bladeMaterial),

                    new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                            .setEmission(new Color(20, 20, 20))
                            .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                    new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                            .setEmission(new Color(20, 20, 50))
                            .setMaterial(new Material().setKD(1))
            );

            // --- הכדורים בצד ימין של התחנה במשולש עם הכדור הגדול בקדמת התמונה משמאל ---
            // הכדור הגדול - קדמת משולש, משמאל (קדמת התמונה)
            scene.geometries.add(
                    new Sphere(45, new Point(170, 15, 90))
                            .setEmission(new Color(BLUE)) // צבע כנפי הפרמידה
                            .setMaterial(new Material()
                                    .setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4))
            );

            // הכדורים הקטנים - מאחוריו, בצד ימין
            scene.geometries.add(
                    new Sphere(30, new Point(220, 10, 180))
                            .setEmission(new Color(8, 8, 12))
                            .setMaterial(new Material()
                                    .setKD(0.1)    // מעט פיזור אור (Diffuse)
                                    .setKS(0.2)    // השתקפות מבריקה נמוכה
                                    .setShininess(500) // מאוד מבריק (עוד יותר מהקודם)
                                    .setKR(0.95)), // השתקפות מאוד גבוהה - מראה כמעט מושלם

                    new Sphere(25, new Point(120, 10, 150)) // שמאלי - שמור כפי שהיה
                            .setEmission(new Color(10, 10, 20))
                            .setMaterial(new Material()
                                    .setKD(0.1).setKS(0.3).setShininess(400)
                                    .setKT(0.85).setKR(0.15))
            );

            // תאורה
            scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
            scene.lights.add(
                    new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                            .setKL(0.0001).setKQ(0.00001)
            );

            scene.lights.add(
                    new SpotLight(
                            new Color(800, 300, 100),               // גוון חמים של שקיעה
                            new Point(-300, 300, 400),              // מיקום רחוק באופק
                            new Vector(2, -2, -3))                  // כיוון אל הסצנה
                            .setKL(0.00005).setKQ(0.00001)
            );

            // יצירת המצלמה והגדרותיה
            Camera camera = Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .setLocation(new Point(0, 300, 800))
                    .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                    .setVpDistance(600)
                    .setVpSize(500, 500)
                    .setResolution(800, 800)
                    .setAAFlag(true)
                    .build();


            // ביצוע הרינדור והכתיבה לקובץ
            camera.renderImage();
            camera.writeToImage("windmillPyramid15_withAA");
        }


}
