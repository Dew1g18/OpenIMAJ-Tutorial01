package Chapter_3;

import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args){
        try {
            MBFImage input = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));

            input = ColourSpace.convert(input, ColourSpace.CIE_Lab);
            //Colour-space transform for the imput for the kmeans algorithm


            MBFImage clone = input.clone();
            //Cloning image for the exercises

            FloatKMeans cluster = FloatKMeans.createExact(3);
            /**
             * Kmeans algorithm component, (2 is the number of clusters we're gonna make.)
             * 2 defines the number of colour spaces we map to, when I change this number it adds to the number of
             * colours I am drawing in, here we have already learned how to make the classic hope Obama poster with
             * computer vision! Very cool
             */

            float[][] imageData = input.getPixelVectorNative(new float[input.getWidth() * input.getHeight()][3]);
            /**
             * Taking the image as a float array so that the
             */

            FloatCentroidsResult result = cluster.cluster(imageData);


            final float[][] centroids = result.centroids;
            for (float[] fs : centroids) {
                System.out.println(Arrays.toString(fs));
            }
            //  At this point we have each centroid and are able to start assigning pixels based on which is closest to which

            final HardAssigner<float[],?,?> assigner = result.defaultHardAssigner();
            for (int y=0; y<input.getHeight(); y++) {
                for (int x=0; x<input.getWidth(); x++) {
                    float[] pixel = input.getPixelNative(x, y);
                    int centroid = assigner.assign(pixel);
                    input.setPixelNative(x, y, centroids[centroid]);
                }
            }

            input = ColourSpace.convert(input, ColourSpace.RGB);
//            DisplayUtilities.display(input);

            GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
            List<ConnectedComponent> components = labeler.findComponents(input.flatten());

            int i = 0;
            for (ConnectedComponent comp : components) {
                if (comp.calculateArea() < 50)
                    continue;
                input.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
            }
            /**
             * Here we're drawing on the different points we use as means during the different iterations
             */

//            DisplayUtilities.display(input);


            /**
             * 3.1.1
             *
             *
             * Going to use the same assigner as before on the clone
             */
            DisplayUtilities.display(clone);
            Helper h = new Helper();
            h.takeInput("Hit enter to proceed");
//            Float[] pixel = h.arrayPrimToObj(clone.getPixelNative(x));
            clone.processInplace(new PixelProcessor<Float[]>() {
                @Override
                public Float[] processPixel(Float[] pixel) {
                    Helper h = new Helper();
                    int centroid = assigner.assign(h.arrayObjToPrim(pixel));
                    return h.arrayPrimToObj(centroids[centroid]);
                }
            });
            DisplayUtilities.display(clone);
            /**this looks strange
             *
             * Found out this is becuase I had done something else to the data before I got a hold of the clone
             *
             * rather than working out where this is though becuase it's cool I'm going to leave it there and
             * create another class to test out the excersises so I can come back and see what made this weird
             * image (clone from before using process in place)
             */

            /**
             * Switched to Exercise3.java to continue the excersizes as I was getting some bugs with the Felzen object
             * and wanted to try it in a clean main.
             */


            //            clone.processInplace(new PixelProcessor<Float[]>() {
//                Float[] processPixel(Float[] pixel) {
//
//                }
//            });









        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
