package uk.ac.soton.dew1g18.ch5;


import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.*;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;

import java.net.URL;

public class Exercise1 {

    public static void main(String[] args){
        try{

            MBFImage query = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/query.jpg"));
            MBFImage target = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/target.jpg"));

            DoGSIFTEngine engine = new DoGSIFTEngine();
            LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
            LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());

            LocalFeatureMatcher<Keypoint> matcher = new BasicTwoWayMatcher<>();
            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);

            MBFImage basicMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
//
//            DisplayUtilities.displayName(basicMatches, "Basic 2 way matcher");

//            RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(50.0, 1500,
//                    new RANSAC.PercentageInliersStoppingCondition(0.5));
//            matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
//                    new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);
//
//            matcher.setModelFeatures(queryKeypoints);
//            matcher.findMatches(targetKeypoints);
//
//            MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),
//                    RGBColour.RED);
//            DisplayUtilities.displayName(consistentMatches, "Basic 2 way matcher");


            /**
             * From here I can see that before the transform to remove noise the basic matcher is much more densly
             * populated with matched features than the two way matcher.
             *
             * however after the same Ransac is applied from the tutorial section the both give very similar results
             */

            /**
             * For exercise 5.1.2
             */

            RobustHomographyEstimator modelFitter = new RobustHomographyEstimator(50, 1500,
                    new RANSAC.PercentageInliersStoppingCondition(0.5), HomographyRefinement.SINGLE_IMAGE_TRANSFER_INVERSE);
            /**
             * Here I learned that with no homography refinement model it matches a lot of noise and you end up with
             * a completely non-sensical object drawn around. So I tried a few of the differnent refinement models
             * shown by the intelliJ snippets.
             * None -   the 'object' highlighted included parts of the books title and no more, it also went off the top
             *          of the image.
             *
             * Single-Image-Transfer-   Succesfully surrounded the book but with a huge area around it too, including
             *                          a large section of the photograper's tshirt etc in the image
             *
             * Symmetric-Transfer-  Definitely the best so far, still has a larger gap between the boundary of the
             *                      detected features of the magazine and the actual edges of it than the Robust
             *                      affline transform.
             *
             * Single Image inverse-    By far the best, the magasine wasnt all inside the box, however to me this means
             *                          there was much less noise affecting the final result and so the features matched
             *                          inside were much more consistently useful
             */
            matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                    new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);



            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);

            MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),
                    RGBColour.RED);

            DisplayUtilities.display(consistentMatches);

            target.drawShape(
                    query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
            DisplayUtilities.display(target);


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
