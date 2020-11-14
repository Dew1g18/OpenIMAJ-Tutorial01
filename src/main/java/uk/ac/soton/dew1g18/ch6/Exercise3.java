package uk.ac.soton.dew1g18.ch6;



import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.dataset.BingImageDataset;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.BingAPIToken;
import org.openimaj.util.api.auth.common.FlickrAPIToken;

public class Exercise3 {
    public static void main(String[] args){

        try{


            FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
            FlickrImageDataset<FImage> cats =
                    FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken, "cat", 10);
            DisplayUtilities.display("Cats -flickr", cats);


            BingAPIToken bingAPIToken = DefaultTokenFactory.get(BingAPIToken.class);
            BingImageDataset<FImage> catsB=
                    BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingAPIToken, "cat",10);

            DisplayUtilities.display("Cats -bing", catsB);

            /**
             * Direct comparison
             *
             *
             * Actually, gonna wait and see if anyone gives me a way to do the azure thing without inputting my card
             * details cause I've had too many random charges lately..
             * Moving on
             */


        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
