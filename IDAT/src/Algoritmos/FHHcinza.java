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
public class FHHcinza {

    public BufferedImage FHH(BufferedImage img, float beta) {
        
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;

        double[] histogram = new double[257];
        double[] histogramaOriginal = new double[257];
        int [] novoNivel = new int[257];
        
        double gmin = 10000;
        double gmax = 0;
        int i, j;

        // Obtém o histograma da imagem;
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j<img.getHeight();j++){
                Color val = new Color(img.getRGB(i,j)); //obtenção da intensidade dos pixels
                grayScale = corConvert.convertRGBGray(val); // conversão do pixel para escala de cinza
                histogramaOriginal[grayScale+1]++; //contabilização do histograma
                
                // Define gmin e gmax
                double cor = grayScale;
                if (cor < gmin) gmin = cor;
                if (cor > gmax) gmax = cor;
            }
        }

        for (i = (int)gmin; i < ((int)gmax + 1); i++) {
            // cor atual é o "i";
            
            double mi = (i - gmin) / (gmax - gmin);
            double newGray = FuzzyMethod(mi, beta);
            //System.out.println((int)newGray + " at pixel " + i);
            novoNivel[i] = (int)newGray;
        }
        
        //Gera a nova imagem
        //System.out.println("Gerando imagem");
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j < img.getHeight();j++){

                Color pixel = new Color(img.getRGB(i, j));
                int corOriginal = corConvert.convertRGBGray(pixel);
                int atual = novoNivel[corOriginal];

                Color novo = new Color(atual, atual, atual);

               res.setRGB(i,j, novo.getRGB());
            }
        }
        /*
        for (i = 0; i < 257; i++) {
            for (j = 0; j < 257; j++) {
                Color gmn = new Color(img.getRGB(i, j));
                
                double mi = (gmn.getGreen() - gmin) / (gmax - gmin);
                double newGray = FuzzyMethod(mi, beta);

                //System.out.println("Valor gmn: " + gmn.getBlue() + ", valor gmin e gmax: " + gmin + ", " + gmax);
                Color setGray = new Color((int) newGray, (int) newGray, (int) newGray);
                res.setRGB(i, j, setGray.getRGB());
                //System.out.println("Novo nivel de cinza: " + setGray.getBlue());

                //System.out.println("Pixel " + i + ", " + j + " = " + newGray);
            }
        }*/

        return res;
    }

    private double FuzzyMethod(double mi, float beta) {
        double newPixel;
        // L = valor máximo da escala de cinza;
        int L = 256;
        //beta = 1;
        // F1 e F2 = parâmetros adaptativos. F1 = 1 e F2 = 0 configuram a versão padrão do FHH;
        // F1 > 1: Imagem mais clara - F1 = [1.0, 1.4]
        // F1 < 1: Imagem mais escura - F1 = [0.6, 1.0]
        // F2 > 0: Imagem mais clara - F2 = [0, 40]
        // F2 < 0: Imagem mais escura - F2 = [-40, 0]
        // F2: Pode facilmente estourar os limites do histograma
        
        // Standard values:
        //int F1 = 1;
        //int F2 = 0;
        double F1 = 1.25;
        double F2 = -20;

        // Algoritmo FHH
        double mid1 = (L - 1) / (Math.exp(-1) - 1);
        double mid2 = Math.exp((-1) * Math.pow(mi, beta)) - 1;

        newPixel = (F1 * mid1 * mid2) + F2;

        if (newPixel > 255) newPixel = 255;
        else if (newPixel < 0) newPixel = 0;
        
        return newPixel;
    }

}
