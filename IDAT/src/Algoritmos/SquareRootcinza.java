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
public class SquareRootcinza {
    // Parâmetro C = [12, 15]
    public BufferedImage SquareRootRun(BufferedImage img, int C) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;
        double[] histogram = new double[256];
        int [] novoNivel = new int[256];
        int i, j;
        
        // Obtém o histograma da imagem;
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j<img.getHeight();j++){
                Color val = new Color(img.getRGB(i,j)); //obtenção da intensidade dos pixels
                grayScale = corConvert.convertRGBGray(val);
                histogram[grayScale]++; //contabilização do histograma
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
                Color pixel = new Color(img.getRGB(i, j));
                int corOriginal = corConvert.convertRGBGray(pixel);
                
                int atual = novoNivel[corOriginal];

                Color novo = new Color(atual, atual, atual);

               res.setRGB(i,j, novo.getRGB());
            }
        }
        
        return res;
    }
}
