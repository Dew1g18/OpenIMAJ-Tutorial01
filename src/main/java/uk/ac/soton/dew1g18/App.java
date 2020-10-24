package uk.ac.soton.dew1g18;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
    	//Create an image
        MBFImage image = new MBFImage(1080,700, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.BLACK);
        		        
        //Render some test into the image
        image.drawText("Whatup Comp Vision!!", 250, 300, HersheyFont.FUTURA_MEDIUM, 50, RGBColour.GREEN);

        //Apply a Gaussian blur
        image.processInplace(new FGaussianConvolve(1f));
        
        //Display the image
        DisplayUtilities.display(image);

        /**
         * Just changed parameters in the drawText method for the text, font and colour changes. (Also changed size and fill colour for neatness
         */

    }
}
