package Chapter_5;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

import java.net.URL;

public class App {

    public static void main(String[] args){

        try {
            MBFImage query = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/query.jpg"));
            MBFImage target = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/target.jpg"));
            //Obligatory import images lol

            /**
             * Learning to use SIFT, a local feature descriptor, the goal being to try to find repeated objects between
             * images.
             */

            DoGSIFTEngine engine = new DoGSIFTEngine();
            LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
            LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
            /**
             * Uses a difference-of-gaussian feature detector, destcribed with the standard DoSIFTEngine SIFT descriptor
             *
             * The engine is used to create keypoints from the images we feed it, these keypoints can be used for
             * different forms of comparisons between the points from each image.
             *
             * Using a BasicMatcher we look for keypoints that are appearing in the target but not many times, as if it
             * was everywhere in the target it wouldnt be very descriptive.
             */

            LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);

            MBFImage basicMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
//            DisplayUtilities.display(basicMatches);

            /**
             * We then use the MatchingUtilities to draw lines between the query and target keypoints.
             * Very cool, but it is picking up a bunch of points on his tshirt and the background, how would we fix this?
             *
             * next to try using a geometric filter, the consistentLocalFeatureMatcher.
             *
             */
            RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(50.0, 1500,
                    new RANSAC.PercentageInliersStoppingCondition(0.5));
            matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                    new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);

            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);

            MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),
                    RGBColour.RED);

            DisplayUtilities.display(consistentMatches);

            /**
             * getting an error when I run this:
             *
             Oct 25, 2020 10:58:40 AM com.github.fommil.netlib.LAPACK <clinit>
             WARNING: Failed to load implementation from: com.github.fommil.netlib.NativeSystemLAPACK
             Oct 25, 2020 10:58:40 AM com.github.fommil.jni.JniLoader liberalLoad
             INFO: successfully loaded C:\Users\davew\AppData\Local\Temp\jniloader17714906544354334389netlib-native_ref-win-x86_64.dll
             *
             *
             * Nonetheless it still runs fine and the expected output is given
             */

            target.drawShape(
                    query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
            DisplayUtilities.display(target);

            /**
             * then using these keypoints we draw a box around the detected object
             */

            /**
             * Exercises: I Am creating Exercise.java and maybe another for part 2 instead of commenting everything out
             * etc. as there are some good examples here and I'd like to preserve them for reference while I do the
             * coursework.
             **/


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
