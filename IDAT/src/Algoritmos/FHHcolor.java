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
public class FHHcolor {
    
    /*
    * Função FHHColorRun(Buffered Image, float) { }
    *  
    * 
    */
    public BufferedImage FHHColorRun(BufferedImage img, float beta) {
        
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int[] colorPixel;  // colorPixel[0] = Y; colorPixel[1] = I; colorPixel[2] = Q;
        int grayScale;

        double[] histogram = new double[256];
        double[] histogramaOriginal = new double[256];
        double[][][] pixelBackup = new double[img.getWidth()][img.getHeight()][3];
        int [] novoNivel = new int[256];
        
        double gmin = 10000;
        double gmax = 0;
        int i, j;

        // Obtém o histograma da imagem;
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j<img.getHeight();j++){
                Color val = new Color(img.getRGB(i,j)); //obtenção da intensidade dos pixels
                colorPixel = corConvert.convertRGBtoYIQ(val); // conversão do pixel para escala de cinza
                //histogramaOriginal[colorPixel[0] + 1]++; //contabilização do histograma
                histogramaOriginal[colorPixel[0]]++; //contabilização do histograma
                
                // Preenche pixelBackup
                pixelBackup[i][j][0] = colorPixel[0];  // Armazena Y
                pixelBackup[i][j][1] = colorPixel[1];  // Armazena I
                pixelBackup[i][j][2] = colorPixel[2];  // Armazena Q
                
                // Define gmin e gmax
                double cor = colorPixel[0];
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
                int corOriginal = (int)pixelBackup[i][j][0];
                int atual = novoNivel[corOriginal];

                Color novo = corConvert.convertYIQtoRGB(atual, (int)pixelBackup[i][j][1], (int)pixelBackup[i][j][2]);

               res.setRGB(i,j, novo.getRGB());
            }
        }

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
