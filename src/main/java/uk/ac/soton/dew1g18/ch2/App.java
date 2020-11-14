package uk.ac.soton.dew1g18.ch2;


import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.openimaj.image.DisplayUtilities.createNamedWindow;

public class App {

    public static void main(String[] args){
        try {
            MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
            System.out.println(image.colourSpace);



//            DisplayUtilities.display(image);
            //Showing original image

//            DisplayUtilities.display(image.getBand(0), "Red Channel");
//            DisplayUtilities.display(image.getBand(1),"Blue Channel");
            /**
             * Displayed a couple bands together to see that the intensities changed based on the colours, lit
             */

            MBFImage clone = image.clone();


//            for (int y=0; y<image.getHeight(); y++) {
//                for(int x=0; x<image.getWidth(); x++) {
//                    clone.getBand(1).pixels[y][x] = 0;
//                    clone.getBand(2).pixels[y][x] = 0;
//                }
//            }
            //Direct pixel control, cool, bet this will be how we make point operators then

            clone.getBand(0).fill(0f);
            clone.getBand(2).fill(0f);
         /**
         *Tested out a bunch of combos of bands to turn off, interestingly (probably due to a low amount of either of these
         * colours in the picture) if only blue or green are off most of the image save for the obviously changing background
         * almost looks like a normal image (according to people who didnt know what I was changing when I showed the image)
         */
//            DisplayUtilities.display(clone,wind);



            MBFImage clone2 = image.clone();

            clone2.processInplace(new CannyEdgeDetector());

            clone2.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
            clone2.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
            clone2.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
            clone2.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
            clone2.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
            clone2.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);


            /**
             * drawing another not filled ellipse to make the borders
             * 2.1.2
             */
            clone2.drawShape(new Ellipse(500f,300,100f,70f,0f), 20, RGBColour.GREEN);
            clone2.drawShape(new Ellipse(650f,425f,25f,10f,0f), 10, RGBColour.GREEN);
            clone2.drawShape(new Ellipse(600f,380f,30f,10f,0f), 10, RGBColour.GREEN);
            clone2.drawShape(new Ellipse(700f,450f,20f,10f,0f), 5, RGBColour.GREEN);

            /**
             * Took a minute to dial in the sizes there, was counter intuitive and I have actually swapped the first and
             * last, chose green to make it stand out.
             */
//            clone2.drawShape(new Ellipse(425,300,20f,10f,0f), 5, RGBColour.BLACK);
//            clone2.drawShape(new Ellipse(425,330,20f,10f,0f), 5, RGBColour.BLACK);




            /**
             * 2.1.1 Excercise
             */
            JFrame mainWindow = DisplayUtilities.createNamedWindow("mainWindow","mainWindow", true);

//            DisplayUtilities.display(clone2, wind);
            DisplayUtilities.updateNamed("mainWindow", clone, "Only Green");
            mainWindow.setVisible(true);
            Helper ie = new Helper();
            ie.takeInput();
            /**
             * Taking an input from the console so that I can see that the named display will work for updating the page.
             */
            DisplayUtilities.updateNamed("mainWindow", clone2, "lines");
            mainWindow.setVisible(true);



        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
