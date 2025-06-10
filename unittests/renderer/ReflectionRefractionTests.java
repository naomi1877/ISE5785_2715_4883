package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /**
    * Default constructor to satisfy JavaDoc generator
    */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /**
    * Scene for the tests
    */
   private final Scene scene = new Scene("Test scene");
   /**
    * Camera builder for the tests with triangles
    */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
              new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE)) //
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
              new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED)) //
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.lights.add( //
              new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                      .setKL(0.0004).setKQ(0.0000006));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(150, 150) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("refractionTwoSpheres");
   }

   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
              new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                              .setKT(new Double3(0.5, 0, 0))), //
              new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(670, 670, 3000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(1)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(-1500, -1500, -2000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
              .setKL(0.00001).setKQ(0.000005));

      cameraBuilder
              .setLocation(new Point(0, 0, 10000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(10000).setVpSize(2500, 2500) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                      .setKL(4E-5).setKQ(2E-7));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(200, 200) //
              .setResolution(600, 600) //
              .build() //
              .renderImage() //
              .writeToImage("refractionShadow");
   }

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
   void windmillDemoTest() {
      Scene scene = new Scene("Windmill Scene");
      // הוספת גופים לסצנה
      scene.geometries.add(
              // עמוד התחנה - גליל
              new Cylinder(20, new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)), 300)
                      .setEmission(new Color(100, 100, 100))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(100)),

              // כנף עליונה
              new Triangle(
                      new Point(0, 150, 0),
                      new Point(0, 250, 40),
                      new Point(0, 250, -40)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

              // כנף ימנית
              new Triangle(
                      new Point(0, 150, 0),
                      new Point(80, 200, 20),
                      new Point(80, 200, -20)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

              // כנף שמאלית
              new Triangle(
                      new Point(0, 150, 0),
                      new Point(-80, 200, 20),
                      new Point(-80, 200, -20)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

              // מרכז הטורבינה - כדור
              new Sphere(15, new Point(0, 150, 0))
                      .setEmission(new Color(80, 40, 100))
                      .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300))
      );

      // תאורה
      scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

      scene.lights.add(new SpotLight(
              new Color(700, 400, 400),
              new Point(200, 200, 200),
              new Vector(-1, -1, -2))
              .setKL(0.0001).setKQ(0.00001)
      );

      // מצלמה
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
              .writeToImage("windmillDemo");
   }

   @Test
   void windmillDemoTest2() {
      Scene scene = new Scene("Windmill Scene");
      Point pivot = new Point(20, 145, 55);
      Point pivot2 = new Point(0, 127, 40);


      scene.geometries.add(
              // עמוד התחנה
              new Cylinder(
                      49, // רדיוס וגובה
                      new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
                      , 300).setEmission(new Color(89,72,38)) // אדום בוהק
                      .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
              //גג
      ,new Sphere(49, new Point(0, 147, 0))
                      .setEmission(new Color(89,72,38))
                      .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

              new Triangle(
                      pivot,
                      new Point(20.0, 277, 60.0),
                      new Point(-19.39, 277, 53.05)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
              ,

// כנף עליונה שמאלית
              new Triangle(
                      pivot,
                      new Point(148.03, 147, 82.57),
                      new Point(148.03, 107, 82.57)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
              ,

// כנף תחתונה
              new Triangle(
                      pivot,
                      new Point(20.0, 17, 60.0),
                      new Point(59.39, 17, 66.95)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
              ,

              new Triangle(
                      pivot,
                      new Point(-108.03, 147, 37.43),
                      new Point(-108.03, 187, 37.43)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150))
              ,

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
      scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(200, 200, 200), new Vector(-2, -2, -3))
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
              .writeToImage("windmillDemo8 + Turbine rotation");
   }

   @Test
   void windmillDemoTest3() {
      Scene scene = new Scene("Windmill Scene");
      Point pivot = new Point(20, 147, 60);
      Point pivot2 = new Point(0, 127, 40);


      scene.geometries.add(
              // עמוד התחנה
              new Cylinder(
                      40, // רדיוס וגובה
                      new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
                      , 300).setEmission(new Color(89,72,38)) // אדום בוהק
                      .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
              //גג
              ,new Sphere(40, new Point(0, 147, 0))
                      .setEmission(new Color(89,72,38))
                      .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),

              // כנף עליונה ימנית
              new Triangle(
                      pivot,
                      new Point(120, 240, -60),
                      new Point(60, 240, 0)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

              // כנף עליונה שמאלית
              new Triangle(
                      pivot,
                      new Point(-100, 240, -60),
                      new Point(-60, 240, 0)
              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

//כנף תחתונה
              new Triangle(
                      pivot,                               // נקודת החיבור המשותפת
                      new Point(30, 50, 100),              // קצה נמוך אחורי ימני
                      new Point(0, 20, 80)                // קצה נמוך אחורי שמאלי


              ).setEmission(new Color(60, 60, 120))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(150)),

              // מרכז הטורבינה
              new Sphere(10, pivot)
                      .setEmission(new Color(80, 40, 100))
                      .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(300)),
              //new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
              // מישור קרקע
              new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(20, 20, 20))
                      .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(50))

//              // רקע אחורי
//              new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
//                      .setEmission(new Color(20, 20, 50))
//                      .setMaterial(new Material().setKD(1))
//
//
           );

// שמיים מדורגים — רקע של שקיעה
      int numLines = 450;              // מספר שכבות/קווים
      double yStart = 1000;             // גובה התחלתי (למעלה)
      double lineHeight = 2;           // גובה כל שורת רקע
      double zPos = -500;              // עומק הרקע (כמו Plane קודם)
      double width = 1000;             // חצי רוחב כל שורת רקע (תלוי במידת התמונה)

      for (int i = 0; i < numLines; i++) {
         double yTop = yStart - i * lineHeight;
         double yBottom = yTop - lineHeight;

         double ratio =1.0 -  (double) i / numLines;

         int r = (int)(116 - ratio * 87);
         int g = (int)(146 - ratio * 97);
         int b = (int)(172 - ratio * 98);
         Color color = new Color(r, g, b);


         // יצירת פס אופקי בשמיים
         scene.geometries.add(
                 new Polygon(
                         new Point(-width, yTop, zPos),
                         new Point(width, yTop, zPos),
                         new Point(width, yBottom, zPos),
                         new Point(-width, yBottom, zPos)
                 ).setEmission(color)
                         .setMaterial(new Material().setKD(0.5))
         );
      }




      // תאורה
      scene.setAmbientLight(new AmbientLight(new Color(80, 60, 40)));

      scene.lights.add(
              new SpotLight(
                      new Color(300, 200, 100),               // גוון כתום חמים
                      new Point(300, 100, 300),               // מקום נמוך - שמש שוקעת
                      new Vector(-2, -0.7, -2)                // מכה באלכסון מהצד
              ).setKL(0.0001).setKQ(0.00001)
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
              .writeToImage("windmillDemo7");
   }

   @Test
   void cylinderAloneTest() {
      Scene scene = new Scene("Cylinder Test");

      // גליל לבד – בצבע חזק ומרכז המסך
      scene.geometries.add(
              new Cylinder(
                      40, // רדיוס וגובה
                      new Ray(new Point(0, -150, 0), new Vector(0, 1, 0)) // ממרכז כלפי מעלה
                      , 300).setEmission(new Color(150, 20, 20)) // אדום בוהק
                      .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(100))
      );

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
              .writeToImage("cylinderAloneTest");
   }


   @Test
   void tinyCylinderTopViewTest() {
      Scene scene = new Scene("Tiny Cylinder Caps");

      // גליל קצר ורחב – קל לראות בסיסים
      scene.geometries.add(
              new Cylinder(
                      40, // רדיוס
                      new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)),
                      10  // גובה קטן
              ).setEmission(new Color(200, 30, 30))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))
      );

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
              .writeToImage("tinyCylinderTopViewTest");
   }



}