package Chapter_4;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class App {


    public static void main(String[] args){

        try {
            /**
             * How to extract numerical representations from images & how they can relate different images together using similarities.
             */

//            //Obligatory importing an image:
//            MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
//
//
////        MultidimensionalHistogram histogram = new MultidimensionalHistogram( 4, 4, 4 );
//            //NOt needed as the following will create one using the model in a moment
//
//            HistogramModel model = new HistogramModel(4, 4, 4);
//            model.estimateModel(image);
//            MultidimensionalHistogram histogram = model.histogram;
            /**
             * 64 bins in a model of the given image, used to create a histogram of the colours in the image.
             *
             * All commented out as its just examples of how to get the histogram. Now we will get a few images and
             * use the histograms to compare features
             */

            URL[] imageURLs = new URL[] {
                    new URL( "http://openimaj.org/tutorial/figs/hist1.jpg" ),
                    new URL( "http://openimaj.org/tutorial/figs/hist2.jpg" ),
                    new URL( "http://openimaj.org/tutorial/figs/hist3.jpg" )
            };

            List<MultidimensionalHistogram> histograms = new ArrayList<>();
            HistogramModel model = new HistogramModel(4, 4, 4);


            Integer x =0;
            for( URL u : imageURLs ) {
                model.estimateModel(ImageUtilities.readMBF(u));
                histograms.add( model.histogram.clone() );
//                DisplayUtilities.displayName(ImageUtilities.readMBF(u), x.toString());
                x++;
                //Added this so I can see each of the images I'm comparing, very tired, x incrememting was the easiest label
                //Commented out the display for the final exercise.

            }

            double distanceScore = histograms.get(0).compare( histograms.get(1), DoubleFVComparison.EUCLIDEAN );
            //This double is a score representing the similarity or dissimilarity of the images behing the URLs


//            System.out.println(distanceScore);
//            DisplayUtilities.display(ImageUtilities.readMBF(imageURLs[0]));
//            DisplayUtilities.display(ImageUtilities.readMBF(imageURLs[1]));

            List<Triple> pairs = new ArrayList<>();
            for( int i = 0; i < histograms.size(); i++ ) {
                for( int j = i; j < histograms.size(); j++ ) {
                    double distance = histograms.get(i).compare( histograms.get(j), DoubleFVComparison.EUCLIDEAN );
                    pairs.add(new Triple(i,j,distance));
                    System.out.println("Images: "+i+" & "+j+" Scored: "+distance);
                }
            }
//            looking at all the urls in the list.
//

            /**
             * Excrcise 4.1.1
             * While reading throught hte chapter I inadvertently did all the displaying that I need to answer this
             * qudestion, the first 2 as I would have expected on a colour comparison were much more similar to one
             * another than they were to the 3rd. This is because they both have a purplish hue and the final one is
             * much darker.
             */

            /**
             * Exercise 4.1.2
             * This should be really simple, I will add a statement to the nested loop which builds an array of triples,
             * storing i,j and the distance, then I will just check for the highest distance and display the 2 (non-zero)
             * images.
             *
             * For this I am going to comment out the original displays so I only display the 2 that are close but not
             * the same.
             */
            double max = 10;
            Triple select = null;
            for(Triple pair: pairs){
                if (pair.getDistance()<max&&pair.getDistance()!=0){
                    max = pair.getDistance();
                    select = pair;
                }
            }
            DisplayUtilities.displayName(ImageUtilities.readMBF(imageURLs[select.getI()]),String.valueOf(select.getI()));
            DisplayUtilities.displayName(ImageUtilities.readMBF(imageURLs[select.getJ()]),String.valueOf(select.getJ()));
            System.out.println("The 2 displayed images are the closest which arent actually the same, they have a distance of: "+select.getDistance());



        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
