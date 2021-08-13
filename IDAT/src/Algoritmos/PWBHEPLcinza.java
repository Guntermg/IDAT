/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import Support.ColorSupport;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gunter M. Gaede
 */
public class PWBHEPLcinza {
    public BufferedImage PWBHEPLrun(BufferedImage img, double alpha, double beta) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;

        //COMEÇO NORMALIZAÇAO
        int min = 0;
        int max = 0;
        double A = alpha, B = beta;
        int cont = 0, cont3 = 0, nAlpha, difMaxMin = 0;
        double media = 0, cont2 = 0, pixelNewHisto = 0;
        int[] histograma = new int[256], nivelEqua = new int[256];
        double[] newHistograma = new double[256], hcl = new double[256], pdAlpha = new double[256], cdAlpha = new double[256];

    //    if(alpha == 0) {
    //    	A = 2;
    //    }else A = alpha;
    //    if(beta == 0) {
    //    	B = 2;
    //    }
        for (int i = 0; i<256; i++) {
            histograma[i] = 0;
            hcl[i] = 0;
            pdAlpha[i] = 0;
            cdAlpha[i] = 0;
            nivelEqua[i] = 0;
        }

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color x = new Color(img.getRGB(i, j));
                int cor = corConvert.convertRGBGray(x);

                histograma[cor]++;

            }
        }


        for (int i = 0; i<256; i++) {

            cont = histograma[i] + cont; 	
        }

        cont = 0;


        for (int i = 0; i<256; i++) {
            newHistograma[i] = Math.pow(Math.log(histograma[i] + A), B);
            pixelNewHisto += newHistograma[i];
        }


        //FINAL NORMALIZAÇÃO

        for (int i = 0; i<256; i++) {
            if (newHistograma[i] != 0) {
                    cont2 += newHistograma[i];
                    cont3++;
            }

        }

        media = cont2/cont3;



        for (int i = 0; i<256; i++) {

    //    	if (newHistograma[i] >= media) hcl[i] = media;
    //    	else 
                    hcl[i] = newHistograma[i];



        }

        cont = 0;
        nAlpha = img.getWidth() * img.getHeight();
        for (int i = 0; i<256; i++) {
    //    	if(newHistograma[i]!=0)
                    pdAlpha[i] = hcl[i]/pixelNewHisto;
    //    		/nAlpha;
    //    	if (newHistograma[i] >= media) newHistograma[i] = media;


    //    	cont += histograma[i];

        }

        for (int i = 0; i<256; i++) {

            if (i==0) {
                    cdAlpha[i] = pdAlpha[i];
            }else {
                    cdAlpha[i] = cdAlpha[i-1] + pdAlpha[i];
            }

        }

        for(int i = 0; i<256; i++) {
            if (histograma[i] != 0) { 
                    min = i;
                    break;
            }
        }

        for(int i = 255; i>=0; i--) {
            if (histograma[i] != 0) { 
                    max = i;
                    break;
            }
        }
        cont2 = 0;
        difMaxMin = max - min;

        for(int i = min; i <= max; i++) {
            nivelEqua[i] = (int)(min + (difMaxMin * cdAlpha[i]));
        }


        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {

                Color p = new Color(img.getRGB(i, j));

                     double cor1 = nivelEqua[corConvert.convertRGBGray(p)];

                 int corB31 = (int) cor1;

                 Color novo = new Color(corB31, corB31, corB31);
                 res.setRGB(i, j, novo.getRGB());
            }
        }
        return res;
    }
}
