package Chapter_3;

import net.didion.jwnl.data.Exc;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Exercise3 {

    public static void main(String[] args){
        try {
//            MBFImage input = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
//
//            input = ColourSpace.convert(input, ColourSpace.RGB);
//
//            FloatKMeans cluster = FloatKMeans.createExact(3);
//
//            FelzenszwalbHuttenlocherSegmenter seg = new FelzenszwalbHuttenlocherSegmenter();
////            seg.segment(input);
//
//            float[][] imageData = input.getPixelVectorNative(new float[input.getWidth() * input.getHeight()][3]);
//
//            FloatCentroidsResult result = cluster.cluster(imageData);
//
//            final float[][] centroids = result.centroids;
//            for (float[] fs : centroids) {
//                System.out.println(Arrays.toString(fs));
//            }
//
//            final HardAssigner<float[],?,?> assigner = result.defaultHardAssigner();
//
////            DisplayUtilities.display(input);
////            Helper h = new Helper();
////            h.takeInput("Hit enter to proceed");
////            Float[] pixel = h.arrayPrimToObj(clone.getPixelNative(x));
//
//            /**
//             * 3.1.1
//             */
//            input.processInplace(new PixelProcessor<Float[]>() {
//                @Override
//                public Float[] processPixel(Float[] pixel) {
//                    Helper h = new Helper();
//                    int centroid = assigner.assign(h.arrayObjToPrim(pixel));
//                    return h.arrayPrimToObj(centroids[centroid]);
//                }
//            });
//
////            input = ColourSpace.convert(input, ColourSpace.CIE_Lab);
//            DisplayUtilities.display(input);
////            h.takeInput("Hit enter in console to proceed");

            /**
             * 3.1.2
             */
            System.out.println("this bit!");
            MBFImage input2 = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
//            input2 = ColourSpace.convert(input2,ColourSpace.CIE_Lab);
            DisplayUtilities.display(input2);

            FelzenszwalbHuttenlocherSegmenter f = new FelzenszwalbHuttenlocherSegmenter();
            List<ConnectedComponent> ls = f.segment(input2);

//            input2 = ColourSpace.convert(input2, ColourSpace.RGB);
            MBFImage out = SegmentationUtilities.renderSegments(input2, ls);
//            out= ColourSpace.convert(out, ColourSpace.RGB);
            DisplayUtilities.display(out);

            /**
             * Setting colour spaces doesnt seem to be able to give me a grayscaled image, however the API says
             * it chooses random colours, I'll come back to this one to play with choosing colourspaces and
             * number of segments etc, very cool
             */
//            SegmentationUtilities.re

        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
