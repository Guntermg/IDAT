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
public class SONARcinza {
    public BufferedImage SONARrun(BufferedImage img, double K) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;

        int nivel;
        int n=256;
        int hist[] = new int[256];
        int novoCinza[] = new int[256];
        float alfa=(float) K;
        float numPixels= (float) 0;
        float prob[]= new float[256];  

        float somaProb[]= new float[256];


        for (int i = 0; i<256; i++){
            hist[i]=0;
            prob[i]=0;
            somaProb[i]=0;
        }

        //Histograma (Fórmula (20))
        //Pega os valores de red na imagem e determina seu histograma 
        for(int linha = 0; linha<img.getWidth(); linha++){
            for(int coluna = 0; coluna < img.getHeight(); coluna++){
                Color x = new Color(img.getRGB(linha,coluna));

                nivel = corConvert.convertRGBGray(x);
                //nivel=(int)x.getRed();
                hist[nivel]++;
            }
        }

        numPixels = img.getWidth()*img.getHeight();

        for(int i=0;i<256;i++){
            prob[i]= (float) (hist[i]/numPixels);     
        }

        for(int i=1; i<256;i++){
            somaProb[i]+= somaProb[i-1]+prob[i-1];
        }

        int u=1;
        //Formula (21) e (22)
        for(u=1; u<=(n-2); u++){
            novoCinza[u]= (int)(u+alfa*(n*somaProb[u]-u));
        }

        for(int linha = 0; linha<img.getWidth(); linha++){
            for(int coluna = 0; coluna < img.getHeight(); coluna++){
                Color p = new Color(img.getRGB(linha, coluna));

                //Encontra a posição relativa do vetor e passa o novo valor de cinza
                float novoNivel = novoCinza[corConvert.convertRGBGray(p)];

                int nivelFinal = (int)novoNivel;

                Color novo = new Color(nivelFinal,nivelFinal,nivelFinal);
                res.setRGB(linha, coluna, novo.getRGB());

            } 
        }

        return res;

    }
    
}
