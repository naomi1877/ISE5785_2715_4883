package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.awt.Color.*;

class CameraTestMP2 {


    @Test
    void testWindmillPyramid15_MS_MP2() {
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

        // Render the scene with different configurations
        // renderWindmill(scene, false, false, "A_NoMT_NoFeature");
        // renderWindmill(scene, true, false, "B_MT_NoFeature");
        // renderWindmill(scene, false, true, "C_NoMT_WithFeature");
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
        scene.addLight(new SpotLight(new Color(600, 219, 255), new Point(200, 200, 200), new Vector(-2, -2, -3)).setKL(0.0001).setKQ(0.00001));
        scene.addLight(new SpotLight(new Color(800, 300, 100), new Point(-300, 300, 400), new Vector(2, -2, -3)).setKL(0.00005).setKQ(0.00001));

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
    void final_test_scene() {
        Scene scene = new Scene("Windmill With othe geometries");
        Point pivot = new Point(0, 150, 49);
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

        //build the Windmill:
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

                //Add the ground and the sky
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50)),

                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 50))
                        .setMaterial(new Material().setKD(1))
        );

        //Add other geometries
        scene.geometries.add(
                new Sphere(35, new Point(230, 15, 150))
                        .setEmission(new Color(8, 8, 12))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.2)
                                .setShininess(500)
                                .setKR(0.95)),

                new Sphere(25, new Point(95, 15, 90))
                        .setEmission(new Color(10, 10, 20))
                        .setMaterial(new Material()
                                .setKD(0.1).setKS(0.3).setShininess(400)
                                .setKT(0.8).setKR(0.20)),

                new Sphere(45, new Point(170, 26, 90))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.3).setShininess(120).setKT(0.4)),

                // new Cone(50, new Ray(new Point(-200, 150, 160), new Vector(0, -1, 0)), 170)
                new Cone(50, new Ray(new Point(300, 150, 0), new Vector(0, -1, 0)), 170)
                        .setEmission(new Color(blue))
                        .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(180).setKT(0.3)),

                new Pyramid(new Point(300, 100, 90),  // הקודקוד – מוגדר בדיוק מעל הבסיס
                        new Polygon(
                                new Point(360, -20, 110),
                                new Point(300, -20, 110),
                                new Point(290, -20, 70),
                                new Point(360, -20, 70)))
                        .setEmission(new Color(255, 100, 100)) // צבע אדום בוהק יותר
                        .setMaterial(new Material()
                                .setKD(0.25)
                                .setKS(0.5)
                                .setShininess(180)
                                .setKT(0.4))


        );
        //3 Asryeli Tours
        scene.geometries.add(
                new Cylinder(38,
                        new Ray(new Point(-290, -150, 0), new Vector(0, 1, 0)),
                        360)
                        .setEmission(new Color(black))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100)),
                new Box(new Double3(-200, -150, -10),
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

        scene.geometries.add(
                //Add the lampadars:
                //lampadar 1
                new Cylinder(2, new Ray(new Point(-350, -20, 80), new Vector(0, 1, 0)), 88)
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(100))

                , new Sphere(10, new Point(-350, 80, 80))
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(150).setKT(0.5))

                ,          //lampadar 2
                new Cylinder(2, new Ray(new Point(-300, -20, 100), new Vector(0, 1, 0)), 88)
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(100))

                , new Sphere(10, new Point(-300, 80, 100))
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(150).setKT(0.5))
        );
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        scene.lights.addAll(List.of(
                //lights from the left side of the scene
                new SpotLight(
                        new Color(800, 300, 100),
                        new Point(-300, 300, 400),
                        new Vector(2, -2, -3))
                        .setKL(0.00005).setKQ(0.00001)
                ,
                //lights from the right side of the scene
                new SpotLight(new Color(600, 219, 255),
                        new Point(200, 200, 200),
                        new Vector(-2, -2, -3))
                        .setKL(0.0001).setKQ(0.00001)
                ,
                // light in the blue sphere
                new PointLight(
                        new Color(500, 200, 700),
                        new Point(170, 26, 90))
                        .setKL(0.0004)
                        .setKQ(0.00005)
                ,
                //lights from the lampadars:
                new SpotLight(new Color(400, 350, 200), new Point(-350, 100, 80), new Vector(0, -1, 0))
                        .setKL(0.0004).setKQ(0.00005)
                ,
                new SpotLight(new Color(400, 350, 200), new Point(-300, 100, 100), new Vector(0, -1, 0))
                        .setKL(0.0004).setKQ(0.00005)
        ));

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(-10, 460, 480))
                .setDirection(new Point(0, 150, 0), new Vector(0, 1, -1))
                .setVpDistance(302)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .setAAFlag(true)
                .setASSFlag(true)
                .setDebugPrint(1)
                .build();

        long start = System.currentTimeMillis();
        camera.renderImage();
        long end = System.currentTimeMillis();

        camera.writeToImage("Windmill_lights+ASS");
        System.out.println("Windmill_ASS_Only done in " + (end - start) + " ms");

    }

}