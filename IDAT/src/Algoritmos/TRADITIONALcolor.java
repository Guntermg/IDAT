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
public class TRADITIONALcolor {
        public BufferedImage TraditionalColorRun(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();    // For color conversions...
        int[] colorScale;      // YIQ exclusive variable -> YIQ
        double[][][] pixelBackup = new double[img.getWidth()][img.getHeight()][3];    // YIQ exclusive;
        double[] histogram = new double[256];
        int [] novoNivel = new int[256];
        double [] pNivel = new double[256];
        int i, j;

        int totalPixels = img.getHeight() * img.getWidth();
        double Sk = 0;
        double SkMult;
        
        
        // Obtém o histograma da imagem;
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j<img.getHeight();j++){
                Color val = new Color(img.getRGB(i,j)); //obtenção da intensidade dos pixels
                colorScale = corConvert.convertRGBtoYIQ(val);
                histogram[colorScale[0]]++; //contabilização do histograma
                
                // Preenche pixelBackup
                pixelBackup[i][j][0] = colorScale[0];  // Armazena Y
                pixelBackup[i][j][1] = colorScale[1];  // Armazena I
                pixelBackup[i][j][2] = colorScale[2];  // Armazena Q
            }
        }
        
        // Calcula probabilidades de cada nível de cinza;
        for (i = 0; i < 256; i++) {
            pNivel[i] = histogram[i] / totalPixels;
        }
        
        // Calcula novos níveis;
        for (i = 0; i < 256; i++) {
            Sk += pNivel[i];
            SkMult = Sk * 255;
            novoNivel[i] = (int)SkMult;
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
    
}
