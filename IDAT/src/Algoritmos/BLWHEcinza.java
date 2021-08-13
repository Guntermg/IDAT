/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import Support.ColorSupport;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author Gunter M. Gaede
 */
public class BLWHEcinza {
    public BufferedImage BLWHErun(BufferedImage img, float ratio, float v) throws IOException {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //imagem de saída
        ColorSupport corConvert = new ColorSupport();
        int grayScale;
        
        
        final int LEVEL = 257;

        //FileWriter arq = new FileWriter("C:\\Users\\igorp\\Desktop\\resultado.txt");
        //PrintWriter gravarArq = new PrintWriter(arq);

        DecimalFormat df = new DecimalFormat("0.00000000000");

        double [] histogram= new double[LEVEL];
        double [] pdf = new double[LEVEL], pdfr = new double[LEVEL], pdfrC = new double[LEVEL];
        double [] cw= new double[LEVEL];
        double sum =0, max =0;
        int i, j, totalPixel= img.getHeight()*img.getWidth();
        int [] novoNivel = new int[LEVEL];
        double alpha, beta = 0.0001; //ratio=0.2, v = 0.9;

        //Passo 1: Histograma
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j<img.getHeight();j++){
                Color val = new Color(img.getRGB(i,j)); //obtenção da intensidade dos pixels
                grayScale = corConvert.convertRGBGray(val);
                //histogram[val.getBlue()+1]++; //contabilização do histograma
                histogram[grayScale + 1]++; //contabilização do histograma
            }
        }

        //Passo 1.1: Calculo do PDF (probabilidades)
        for(i=1; i < LEVEL; i++){
            pdf[i]= histogram[i]/totalPixel;
        }


        //Passo 2: Média o PDF -> Como o PDF é um distribuição discreta, a média é dada
        //por i*p(i);
        for(i=1; i < LEVEL; i++){
            if(pdf[i] > max){ //Encontra o nível de maior prob
                max = pdf[i];
            }
        }

        //Valor do alpha
        alpha = max * v;
        double d= alpha-beta;

        //Passo 3: Restrições
        for(i=1; i < LEVEL; i++){
            if(pdf[i] > alpha){
                pdfr[i] = alpha;
            }

            if(beta <= pdf[i] && pdf[i] <= alpha){
                pdfr[i] = Math.pow((pdf[i]-beta)/d,ratio);
            }

            if(pdf[i] < beta){
                pdfr[i] = 0; 
            }
            sum += pdfr[i];
        }

        //Normalização 
        for(i=1; i < LEVEL; i++){
            cw[i]= pdfr[i]/sum;
        }

        //Acumulativo
        for(i=1; i < LEVEL; i++){
           if(i == 1){
               pdfrC[i] = cw[i];
           }
           else{
               pdfrC[i] = pdfrC[i-1]+cw[i];
           }
        }

        //Gera novos níveis
        for(i=1; i < LEVEL; i++){
            pdfrC[i]*= 255;
            novoNivel[i] = (int) Math.round(pdfrC[i]);
        }

        //Gera a nova imagem
        for(i=0; i < img.getWidth(); i++){
            for(j=0; j < img.getHeight();j++){

                Color pixel = new Color(img.getRGB(i, j));
                grayScale = corConvert.convertRGBGray(pixel);
                int corOriginal = (int) grayScale;
                int atual = novoNivel[corOriginal];

                Color novo = new Color(atual, atual, atual);

                if(atual > 256){
                    System.out.println(novo);
                }


               res.setRGB(i,j, novo.getRGB());
            }
        }

        return res;
    }
}
