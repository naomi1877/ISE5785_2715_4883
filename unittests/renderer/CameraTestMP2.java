package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

class CameraTestMP2 {


    @Test
    void testWindmillPyramid15_MS_MP2() {
        // יצירת הסצנה הכבדה בדיוק כמו בפונקציה שלך
        Scene scene = new Scene("Windmill With Pyramid Blades");
        Point pivot = new Point(0, 150, 49);
        double bladeLength = 105;
        double bladeWidth = 60;
        double bladeDepth = 40;
        double angle = 28;
        Material bladeMaterial = new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4);
        Color bladeColor = new Color(BLUE);

        // הוספת כל הגופים והתאורות בדיוק כמו בתיאור שלך
        scene.geometries.add(
                // גוף התחנה
                new Cylinder(49, new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                        rotateAroundY(new Vector(0, 1, 0), angle)), 300)
                        .setEmission(new Color(100, 80, 50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                new Sphere(49, rotateAroundY(pivot, new Point(0, 150, 0), angle))
                        .setEmission(new Color(89, 72, 38))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                new Sphere(10, rotateAroundY(pivot, pivot, angle))
                        .setEmission(new Color(80, 40, 100))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                // כנפי פירמידות
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),

                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1)),

                // הכדורים בצד
                new Sphere(45, new Point(170, 15, 90))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),

                new Sphere(30, new Point(220, 10, 180))
                        .setEmission(new Color(8, 8, 12))
                        .setMaterial(new Material().setKD(0.1).setKS(0.2).setShininess(500).setKR(0.95)),

                new Sphere(25, new Point(120, 10, 150))
                        .setEmission(new Color(10, 10, 20))
                        .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(400).setKT(0.85).setKR(0.15))
        );

        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3)).setKL(0.0001).setKQ(0.00001));
        scene.lights.add(new SpotLight(new Color(800, 300, 100), new Point(-300, 300, 400), new Vector(2, -2, -3)).setKL(0.00005).setKQ(0.00001));

        // ביצוע כל 4 המצבים
//        renderWindmill(scene, false, false, "A_NoMT_NoFeature");
//        renderWindmill(scene, true, false, "B_MT_NoFeature");
//        renderWindmill(scene, false, true, "C_NoMT_WithFeature");
        renderWindmill(scene, true, true, "D_MT_WithFeature");
    }

    private void renderWindmill(Scene scene, boolean mt, boolean aa, String name) {
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 300, 800))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .setMultithreading(mt ? -2 : 0)
                .setAAFlag(aa)
                .setDebugPrint(1)
                .build();

        camera.writeToImage(name);

        long start = System.currentTimeMillis();
        camera.renderImage();
        long end = System.currentTimeMillis();

        camera.writeToImage(name);
        System.out.println(name + " done in " + (end - start) + " ms");
    }

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
    @Test
    void testWindmillWithASS() {
        Scene scene = new Scene("Windmill With Pyramid Blades");
        Point pivot = new Point(0, 150, 49);
        double bladeLength = 105;
        double bladeWidth = 60;
        double bladeDepth = 40;
        double angle = 28;
        Material bladeMaterial = new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4);
        Color bladeColor = new Color(BLUE);


        scene.geometries.add(

                new Cylinder(49, new Ray(rotateAroundY(pivot, new Point(0, -150, 0), angle),
                        rotateAroundY(new Vector(0, 1, 0), angle)), 300)
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
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50), angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),
                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth), angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth), angle)
                        )).setEmission(bladeColor).setMaterial(bladeMaterial),

                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1)),


                new Sphere(45, new Point(170, 15, 90))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),

                new Sphere(30, new Point(220, 10, 180))
                        .setEmission(new Color(8, 8, 12))
                        .setMaterial(new Material().setKD(0.1).setKS(0.2).setShininess(500).setKR(0.95)),

                new Sphere(25, new Point(120, 10, 150))
                        .setEmission(new Color(10, 10, 20))
                        .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(400).setKT(0.85).setKR(0.15))
        );

        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3)).setKL(0.0001).setKQ(0.00001));
        scene.lights.add(new SpotLight(new Color(800, 300, 100), new Point(-300, 300, 400), new Vector(2, -2, -3)).setKL(0.00005).setKQ(0.00001));

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 300, 800))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .setAAFlag(false)
                .setASSFlag(true)
                .setDebugPrint(1)
                .build();

        long start = System.currentTimeMillis();
        camera.renderImage();
        long end = System.currentTimeMillis();

        camera.writeToImage("Windmill_ASS_Only");
        System.out.println("Windmill_ASS_Only done in " + (end - start) + " ms");
    }
    @Test
    void testWindmillWithASS_newPhoto() {
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


        scene.geometries.add(
                new Cylinder(49,
                        new Ray(rotateAroundY(pivot,new Point(0, -150, 0),angle),
                                rotateAroundY(new Vector(0, 1, 0),angle)),
                        300)
                        .setEmission(new Color(100, 80, 50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),

                new Sphere(49,rotateAroundY(pivot, new Point(0, 150, 0),angle))
                        .setEmission(new Color(89, 72, 38))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                new Sphere(10, rotateAroundY(pivot,pivot, angle))
                        .setEmission(new Color(80, 40, 100))
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50),angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50),angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth),angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 + bladeLength, 50 + bladeDepth),angle)
                        ))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot,new Point(-bladeWidth / 2, 150 - bladeLength, 50),angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50),angle),
                                rotateAroundY(pivot, new Point(bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth),angle),
                                rotateAroundY(pivot, new Point(-bladeWidth / 2, 150 - bladeLength, 50 + bladeDepth),angle)
                        ))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50),angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50),angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth),angle),
                                rotateAroundY(pivot, new Point(bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth),angle)
                        ))
                        .setEmission(bladeColor)
                        .setMaterial(bladeMaterial),

                new Pyramid(rotateAroundY(pivot, pivot, angle),
                        new Polygon(
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50),angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50),angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 + bladeWidth / 2, 50 + bladeDepth),angle),
                                rotateAroundY(pivot, new Point(-bladeLength, 150 - bladeWidth / 2, 50 + bladeDepth),angle)
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


        scene.geometries.add(
                new Sphere(30, new Point(220, 10, 180))
                        .setEmission(new Color(8, 8, 12))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.2)
                                .setShininess(500)
                                .setKR(0.95)),

                new Sphere(25, new Point(120, 10, 150))
                        .setEmission(new Color(10, 10, 20))
                        .setMaterial(new Material()
                                .setKD(0.1).setKS(0.3).setShininess(400)
                                .setKT(0.85).setKR(0.15)),
                new Sphere(45, new Point(170, 15, 90))
                        .setEmission(new Color(BLUE))
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

        );
//3 Asryeli Tours
        scene.geometries.add(
                new Cylinder(38,
                        new Ray(new Point(-290, -150, 0),new Vector(0, 1, 0)),
                        360)
                        .setEmission(new Color(black))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),
                new Box(new Double3(-200,-150,-10),//
                        new Double3(-124,210,-86))
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

        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(
                new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3))
                        .setKL(0.0001).setKQ(0.00001)
        );

        scene.lights.add(
                new SpotLight(
                        new Color(800, 300, 100),               
                        new Point(-300, 300, 400),
                        new Vector(2, -2, -3))
                        .setKL(0.00005).setKQ(0.00001)
        );

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 500, 300))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(500)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .setMultithreading(10) // ריבוי תהליכונים
                .setAAFlag(false)     //with  AA + .setMultithreading(-2)== 3 min 54 sec
                .setASSFlag(true)     //with  AAS + .setMultithreading(4)== 42 sec
                .setDebugPrint(1)
                .build();



        long start = System.currentTimeMillis();
        camera.renderImage();
        long end = System.currentTimeMillis();

        //camera.writeToImage("Windmill_ASS_New6");
        camera.writeToImage("Windmill_ASS_level4");
        System.out.println("Windmill_ASS_Only done in " + (end - start) + " ms");
        //Windmill_ASS_Only3 setVpDistance(1000)
        //Windmill_ASS_Only4 setVpDistance(189)
        //Windmill_ASS_Only5 setVpDistance(302)
        //Windmill_ASS_Only5 setVpDistance(302),setLocation(new Point(0, 400, 500))
        //Windmill_ASS_Only6 improve ASS
    }

}