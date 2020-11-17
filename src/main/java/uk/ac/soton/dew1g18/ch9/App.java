package uk.ac.soton.dew1g18.ch9;

import org.openimaj.image.processing.algorithm.FourierTransform;
import org.openimaj.audio.AudioPlayer;
import org.openimaj.audio.SampleChunk;
import org.openimaj.video.xuggle.XuggleAudio;
import org.openimaj.vis.audio.AudioWaveform;
import org.openimaj.vis.general.BarVisualisation;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class App {

    public static void main(String[] args){
//        XuggleAudio xa = new XuggleAudio( new File( "C:\\Users\\davew\\OpenIMAJ-Tutorial01\\src\\main\\java\\uk\\ac\\soton\\dew1g18\\ch9\\bttf.mp3" ) );
//
//        /**
//         * originally tested this out with the back to the future soundtrack, it was super jittery and I would have to
//         * wait 4 seconds for quarter of a second of music, so I set a line buffer first here.
//         */
//
//        AudioPlayer ap = AudioPlayer.createAudioPlayer( xa );
//        ap.setSoundLineBufferSize(2000);
//        ap.run();

        try {

//            final AudioWaveform vis = new AudioWaveform(400, 400);
//            vis.showWindow("Waveform");
////
//            final XuggleAudio xa = new XuggleAudio(
//                    new URL("https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3"));





//            XuggleAudio xa = new XuggleAudio( new File( "C:\\Users\\davew\\OpenIMAJ-Tutorial01\\src\\main\\java\\uk\\ac\\soton\\dew1g18\\ch9\\bttf.mp3" ) );

        /**
         * Was having 'ERROR_UNKNOWN' from the url that was given
         * so I went and found another cause I couldnt actually find what was wrong with that link, though it didnt seem
         * to exist, may be outdated..
         */

//            AudioPlayer ap = AudioPlayer.createAudioPlayer(xa);
//            ap.setSoundLineBufferSize(2000);
//            ap.run();




//            SampleChunk sc = null;
//            while ((sc = xa.nextSampleChunk()) != null) {
//                vis.setData(sc.getSampleBuffer());
//            }

            BarVisualisation vis = new BarVisualisation(400,400);
            XuggleAudio xa = new XuggleAudio( new URL("https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3"));
//
//
//
//
//            FourierTransform fft = new FourierTransform( xa );
//
//
//            SampleChunk sc = null;
//            while ((sc = xa.nextSampleChunk()) != null) {
//                vis.setData(sc.getSampleBuffer());
//            }
//
//            while( (sc = fft.nextSampleChunk()) != null )
//            {
//                float[][] fftData = fft.getMagnitudes();
//                vis.setData( fftData[0] );
//            }


        }catch(IOException e){
            e.printStackTrace();
        }



    }


}
