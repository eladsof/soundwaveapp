package com.harman.hackathon.soundwaveapp.recording;

import android.net.Uri;

import com.musicg.wave.Wave;

import java.io.InputStream;

/**
 * Created by eladsof on 7/14/16.
 */
public class QualityAnalyzer {

    public int analyzeFile(InputStream fis, String fname) {

        String sampleFileName = Uri.parse("android.resource://com.harman.hackathon.soundwaveapp/raw/sample").getPath(); //"sample.wav";
        double score = corr(fis,fname);
        int factoredScore = (int)Math.min(score * 400,100);

        System.out.print("Score is " + score + "\n");
        return factoredScore;

    }

    private double corr(InputStream fis, String filename2) {

        Wave wave = new Wave(fis);
        Wave wave2 = new Wave (filename2);

        //System.out.println("wave.getFingerprintSimilarity" + wave.getFingerprintSimilarity(wave2).getScore());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double[] W = wave.getNormalizedAmplitudes();
        double[] W2 = wave2.getNormalizedAmplitudes();


        System.out.println(W.length + " " + W2.length);

        int L = 44100;
        int S = W2.length/3;

        double max = 0;
        int maxi = -1;
        double corr;

        for(int i=0;i< W.length-L;i=i+4) {

            corr = 0;

            for(int j=0; j < L; j=j+4) {
                corr += W[i+j] * W2[S+j];
            }

            corr = Math.abs(corr);

            if(corr > max) {
                max = corr;
                maxi = i;
            }

        }

        double e1 = 0 ;
        for(int i=maxi;i<maxi+L;i+=4){
            e1 += W[i] * W[i];
        }

        e1 = Math.sqrt(e1);

        double e2 = 0 ;
        for(int i=S;i<S+L;i+=4){
            e2 += W2[i] * W2[i];
        }

        e2 = Math.sqrt(e2);


        System.out.print(max + " , " + maxi + ", " + e1 + ", " + e2 + "\n");
        return max / (e1*e2);
    }

}
