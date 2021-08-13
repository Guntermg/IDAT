/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gunter M. Gaede
 */
public class PWBHEPLcolor {
    public BufferedImage PWBHEPLrun(BufferedImage img, double alpha, double beta) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        //COMEÇO NORMALIZAÇAO
        int min = 0;
        int max = 0;
        double A = alpha, B = beta;
        int cont = 0, cont3 = 0, nAlpha, difMaxMin = 0;
        double media = 0, cont2 = 0, pixelNewHisto = 0;
        int[] histograma = new int[256], nivelEqua = new int[256];
        double[] newHistograma = new double[256], hcl = new double[256], pdAlpha = new double[256], cdAlpha = new double[256];

        for (int i = 0; i<256; i++) {
            histograma[i] = 0;
            hcl[i] = 0;
            pdAlpha[i] = 0;
            cdAlpha[i] = 0;
            nivelEqua[i] = 0;
        }
        /**
         * for para conversão adquirir o valor de Y
         */
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color x = new Color(img.getRGB(i, j));
                int corR = x.getRed();
                int corB = x.getBlue();
                int corG = x.getGreen();

                int nivelY = (int)((0.2568*corR + 0.5041*corG + 0.0979*corB) +16);

                histograma[nivelY]++;

            }
        }


        for (int i = 0; i<256; i++) cont = histograma[i] + cont;

        cont = 0;

        /**
         * for para criar o novo histograma aplicando Power-log
         */
        for (int i = 0; i<256; i++) {
            newHistograma[i] = Math.pow(Math.log(histograma[i] + A), B);
            pixelNewHisto += newHistograma[i];
        }


        //FINAL NORMALIZAÇÃO
        /**
         * for para fazer somatorio de numero de pixels e quantidade de posições
         * não nulas no histograma.
         */
        for (int i = 0; i<256; i++) {
            if (newHistograma[i] != 0) {
                    cont2 += newHistograma[i];
                    cont3++;
            }
        }

        media = cont2/cont3;



        for (int i = 0; i<256; i++) hcl[i] = newHistograma[i];

        cont = 0;
        nAlpha = img.getWidth() * img.getHeight();
        for (int i = 0; i<256; i++) pdAlpha[i] = hcl[i]/pixelNewHisto;    	

        for (int i = 0; i<256; i++) {    	
            if (i==0) cdAlpha[i] = pdAlpha[i];
            else cdAlpha[i] = cdAlpha[i-1] + pdAlpha[i];    	
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

        for(int i = min; i <= max; i++) nivelEqua[i] = (int)(min + (difMaxMin * cdAlpha[i]));    

        /**
         * for para retornar a imagem equalizada para RGB
         */
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {

                Color p = new Color(img.getRGB(i, j));

                     double corR = p.getRed();
                     double corG = p.getGreen();
                     double corB = p.getBlue();


                     int nivelY = (int)((0.2568*corR + 0.5041*corG + 0.0979*corB) +16);
                     float Cr =(float)((0.4392*corR + -0.3678*corG + -0.0714*corB)+128); 
                 float Cb =(float) ((-0.1482*corR + -0.2910*corG + 0.4392*corB)+128);

                 int red= (int)((1.164*(nivelEqua[nivelY] - 16)) + (1.596*(Cr - 128)));
                 int green = (int)( (1.164*(nivelEqua[nivelY] - 16)) - (0.813*(Cr - 128)) - (0.391*(Cb - 128)));
                 int blue = (int)((1.164*(nivelEqua[nivelY] - 16)) + (2.018*(Cb - 128)));

                 red = red>255 ? 255 : red;
                 red = red<0 ? 0 : red;

                 green = green>255 ? 255 : green;
                 green = green<0 ? 0 : green;

                 blue = blue>255 ? 255 : blue;
                 blue = blue<0 ? 0 : blue;

                 Color novo = new Color(red, green, blue);
                 res.setRGB(i, j, novo.getRGB());
            }
        }
        
        return res;
    }
}
