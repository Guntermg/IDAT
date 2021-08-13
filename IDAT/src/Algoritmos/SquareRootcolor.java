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
public class SquareRootcolor {
    // Parâmetro C = [12, 15]
    public BufferedImage SquareRootRun(BufferedImage img, int C) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int[] colorScale;      // YIQ exclusive variable -> YIQ
        double[][][] pixelBackup = new double[img.getWidth()][img.getHeight()][3];    // YIQ exclusive;
        double[] histogram = new double[256];
        int [] novoNivel = new int[256];
        int i, j;
        
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
        
        // Calcula o novo nível pela raiz quadrada;
        // Formula: Novo = C * raiz(nivel);
        for (i = 0; i < 256; i++) {
            novoNivel[i] = (int) ( C * Math.sqrt(i));
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
