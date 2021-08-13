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
public class BLWHEcolor {
    public BufferedImage BLWHEColorRun(BufferedImage img, float ratio, float v) throws IOException {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //Imagem de saída
    BufferedImage canalY = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //Imagem com o canal Y

    final int LEVEL = 256; //Niveis 
    final double beta = 0.0001; //Beta ->  proposto no artigo
   
    double [] pdf = new double[LEVEL], pdfr = new double[LEVEL], 
            pdfrC = new double[LEVEL], cw= new double[LEVEL], histogramYIQ = new double[LEVEL];
    int i, j, totalPixel= img.getHeight()*img.getWidth();
    int [] novoNivel = new int[LEVEL];
    double alpha, max=0, d, soma=0;
    double [][] YIQ = {{0.299, 0.587, 0.114}, {0.596,-0.274, -0.322}, {0.211, -0.523, 0.312}}, 
            RGB = {{1, 0.956, 0.621}, {1,-0.272, -0.647}, {1, -1.106, 1.703}},
            canalI = new double[img.getWidth()][img.getHeight()], 
            canalQ = new double[img.getWidth()][img.getHeight()];
            
    
    //Passo 1: Histograma
    for(i=0; i < img.getWidth(); i++){
        for(j=0; j<img.getHeight();j++){
            Color val = new Color(img.getRGB(i,j)); //obtencao da intensidade dos pixels
            
            int Y = (int) (YIQ[0][0] * val.getRed()+  YIQ[0][1] * val.getGreen() //Conversao para o canal Y
                    + YIQ[0][2] * val.getBlue());
            
            double I =  (YIQ[1][0] * val.getRed() +  YIQ[1][1] * val.getGreen() //Conversao para o canal I
                    + YIQ[1][2] * val.getBlue());
            
            double Q =  (YIQ[2][0] * val.getRed() + YIQ[2][1] * val.getGreen() //Conversao para o canal Q
                + YIQ[2][2] * val.getBlue());
            
            
            //As condicoes sso para garantir que nao haja a extrapolacao a faixa de valores do espaco
            if(Y > 255){
                Y = 255;
            }
            if(Y < 0){
                Y = 0;
            }
            
            //Guarda os valores presentes nos canais I e Q sem altera-los 
            canalI[i][j] = I; 
            canalQ[i][j] = Q;
            
            histogramYIQ[Y]++; //Contabiliza o histograma para o canal Y
            
            //Gera as cores para compor as imagens de cada canal Y, I, Q
            Color nY = new Color(Y, Y, Y); 
            
            canalY.setRGB(i, j, nY.getRGB()); //Imagem com o canal Y
        }
    }
   
    //Passo 1.1: Calculo do PDF (probabilidades)
     for(i=0; i < LEVEL; i++){
        pdf[i]= histogramYIQ[i]/totalPixel; //Canal Y
    }
    //Passo 2: Media o PDF -> Como o PDF e um distribuição discreta, a media e dada
    //por i*p(i);
        for(i=0; i < LEVEL; i++){ 
            //Encontra o nivel de maior prob de cada canal
            if(pdf[i] > max){ 
                max= pdf[i];
            }  
        }
    
    //Valor do alpha e do divisor   
    alpha = max * v;
    d = alpha - beta;
    
    //Passo 3: Restricoes
        for(i=0; i < LEVEL; i++){
            if(pdf[i] > alpha){
                pdfr[i] = alpha;
            }

            if(beta <= pdf[i] && pdf[i] <= alpha){
                pdfr[i] = Math.pow((pdf[i]-beta)/d,ratio);
            }

            if(pdf[i] < beta){
                pdfr[i] = 0; 
            }
            soma += pdfr[i];
        }
    
   
    //Passo 4: Normalizacao 
    for(i=0; i < LEVEL; i++){
        cw[i]= pdfr[i]/soma;
    }
    
    //Passo 5: Acumulativo
    for(i=0; i < LEVEL; i++){
        if(i == 0){
            pdfrC[i] = cw[i];
        }
        else{
            pdfrC[i] = pdfrC[i-1]+cw[i];
        }
    }   
    
    //Passo 6: Gera novos niveis
    for(i=0; i < LEVEL; i++){
      pdfrC[i]*= 255;
      novoNivel[i] = (int) Math.round(pdfrC[i]);
    }

    //Passo 7: Converter para o espaço RGB e gerar a nova imagem
    for(i=0; i < img.getWidth(); i++){
        for(j=0; j < img.getHeight(); j++){
            Color pixelCanalY = new Color(canalY.getRGB(i, j)); //Pixel da imagem do canal Y
            
            int originalCanalY = pixelCanalY.getRed(); 
            int atual = novoNivel[originalCanalY];
            
            //Converte do espaço YIQ para o espaco RGB
            int RED = (int) (RGB[0][0]*atual + RGB[0][1] * canalI[i][j]
                + RGB[0][2] * canalQ[i][j]);
            
            int GREEN = (int) (RGB[1][0]*atual + RGB[1][1] * canalI[i][j]
                + RGB[1][2] * canalQ[i][j]);
            
            int BLUE = (int) (RGB[2][0] *atual + RGB[2][1]* canalI[i][j]
                + RGB[2][2] * canalQ[i][j]);
            
            //Garante que os novos valores estarao dentro do intervalo 
            if(RED > 255){
                RED = 255;
            }
            if (RED < 0){
                RED = 0;
            }
            
            if(GREEN > 255){
                GREEN = 255;
            }
            if (GREEN < 0){
                GREEN = 0;
            }
            if(BLUE > 255){
                BLUE = 255;
            }
            if (BLUE < 0){
                BLUE = 0;
            }
            
          
            Color novaCor = new Color(RED, GREEN, BLUE);
            
            res.setRGB(i, j, novaCor.getRGB());  
        }
    }

        return res;
    }
}
