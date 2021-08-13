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
public class DCMHEcolor {
    public BufferedImage DCMHEColorRun(BufferedImage img) {
            BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            //COMEÇO
            double min = 10000; 
            double max = 0;
            int k = 5/2; //Colunas da janela 1x5
            int[] HistograyLevels = new int[256];//histograma
            float [] HistoProb = new float[256];
            int numeroPixel=img.getHeight()*img.getWidth();
            int[] h = new int[256];
            int[] b = new int[256];
            double[][][] mat = new double[img.getWidth()][img.getHeight()][3];


             //Copia imagem
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    res.setRGB(i, j, img.getRGB(i, j));
                }
            }
            //Fim da cópia

            //Calcula o Histograma
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    Color cor = new Color(img.getRGB(i, j));
                    mat[i][j][0] = (0.257 * cor.getRed()) + (0.504 * cor.getGreen()) + (0.098 * cor.getBlue()) + 16;
                    mat[i][j][1] = (0.439 * cor.getRed()) - (0.368 * cor.getGreen()) - (0.071 * cor.getBlue()) + 128;
                    mat[i][j][2] = -(0.148 * cor.getRed()) - (0.291 * cor.getGreen()) + (0.439 * cor.getBlue()) + 128;
                    HistograyLevels[(int)mat[i][j][0]]++;
                }
            }

             for (int nivel = 0; nivel < 256; nivel++) {
                h[nivel] = HistograyLevels[nivel];
                b[nivel] = 0;
            }

           // filtrar o histograma
            int m=2;
            for (int nivel = m; nivel< 255-m; nivel++) {
                h[nivel]=(HistograyLevels[nivel-m] + HistograyLevels[nivel-m+1] + HistograyLevels[nivel] + HistograyLevels[nivel+m-1] + HistograyLevels[nivel+m])/(2*m+1);
            }

            //Detecção de Picos no histograma de níveis de cinza.
            //Maximo local
            for (int nivel = 1; nivel < 256; nivel++) {
                if (h[nivel] > h[nivel-1]) {
                    b[nivel] = 1;
                } else {
                    if(h[nivel] != h[nivel-1])
                        b[nivel] = -1;
                }
            }

           //Remoção dos sinais perdidos
             for (int nivel = 2; nivel < 256; nivel++) {
                if (b[nivel-1] != b[nivel-2] && b[nivel-1] != b[nivel]) {
                    b[nivel-1] = b[nivel];
                } 
            }

           int[] listaLimite = new int[30]; //30 picos
            int contadorUp=0;
            int contadorDown=0;
            int flag = 0;

            //Encontrando os limites ótimos (maximos locais) 
            listaLimite[0]=0; //limite inicial
            int contadorLimite=0;
            for (int nivel = 9; nivel < 246; nivel++) {  //9 antes e 9 depois
                contadorUp=0;
                for (int x = 0; x <=8; x++) {
                    if (b[nivel-x] == 1) {
                        contadorUp++;
                    }
                }      
                if(contadorUp==9){
                    contadorDown=0;
                    for (int x = 0; x <=8; x++) {
                        if (b[nivel+x] == -1) {
                            contadorDown++;
                        }
                    }
                }        
                if(contadorUp==9 && contadorDown==9){
                    contadorLimite++;
                    listaLimite[contadorLimite]=nivel;
                }          
            }
            contadorLimite++;
            listaLimite[contadorLimite]=255;//armazena o último limite que é igual a 255

             float cumulative;
            int sum;
            int[] histoEqualizado = new int[256];

            for (int nivel = 0; nivel < 256; nivel++){//calculo das Probabilidades
                HistoProb[nivel]= (float) HistograyLevels[nivel]/numeroPixel;
            }

            float [] HistoProbSoma = new float[256];
            for (int nivel = 0; nivel < 256; nivel++){//inicialiazação
                HistoProbSoma[nivel]= 0;
            }

            int valor;

            for(int p=0; p<contadorLimite; p++){ //calculo da soma das Probabilidades 
                valor=listaLimite[p];
                HistoProbSoma[valor]= HistoProb[valor] ;
                for (int nivel = listaLimite[p]+1; nivel <= listaLimite[p+1]; nivel++){    
                    HistoProbSoma[nivel] = HistoProbSoma[nivel-1]+ HistoProb[nivel];
                }
            }
            // Gerar histograma de saída
            for (int nivel = 0; nivel < 256; nivel++){
                histoEqualizado[nivel] = nivel ;
            }


            for(int p=0; p<contadorLimite; p++){ //limite do histograma para cada trecho

                for (int nivel = listaLimite[p]; nivel <= listaLimite[p+1]; nivel++){
                    histoEqualizado[nivel] = (int) (listaLimite[p]+(listaLimite[p+1]-listaLimite[p])*HistoProbSoma[nivel]);
                }
            } 

            int cor;
            //Transforma f nas imagens
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    cor = histoEqualizado[(int)mat[i][j][0]];
                    Color novo = convFromRGB(cor, mat[i][j][1], mat[i][j][2]);
                    res.setRGB(i, j, novo.getRGB());
                }
            }  
        return res;
    }
    
    public Color convFromRGB (double Y, double Cr, double Cb){
        int r,g,b;
        //Aplica as fórmulas para conversão
        r = (int) (1.164*(Y - 16) + 1.596*(Cr - 128));
        g = (int) (1.164*(Y - 16) - 0.813*(Cr - 128) - 0.391*(Cb - 128));
        b = (int) (1.164*(Y - 16) + 2.018*(Cb - 128));

        //Verifica e corrige possíveis estouros de valor
        if (b > 255) {
            b=255;
        }
        if (b < 0) {
            b=0;
        }
        if (g > 255) {
            g=255;
        }
        if (g < 0) {
            g=0;
        }
        if (r > 255) {
            r=255;
        }
        if (r < 0) {
            r=0;
        }

        Color rgb = new Color(r, g, b);
        return rgb;
    }
}
