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
public class DCMHEcinza {
    public BufferedImage DCMHErun(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;
        
        //COMEÇO
        double min = 10000; 
        double max = 0;
        int k = 5/2; //Colunas da janela 1x5
        int[] HistograyLevels = new int[256];//histograma
        float [] HistoProb = new float[256];
        int numeroPixel=img.getHeight()*img.getWidth();
        int[] h = new int[256];
        int[] b = new int[256];


        //Copia imagem
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                res.setRGB(i, j, img.getRGB(i, j));
            }
        }
        //Fim da cópia

        //Calcula o Histograma
        for (int i = 0; i < img.getWidth(); i++) {
            //System.out.println("for do pixel i " + i);
            for (int j = 0; j < img.getHeight(); j++) {
               Color cor = new Color(img.getRGB(i, j));
                //HistograyLevels[cor.getGreen()]++; 
               HistograyLevels[corConvert.convertRGBGray(cor)]++; 
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
                Color velho = new Color(img.getRGB(i, j));
                cor = histoEqualizado[corConvert.convertRGBGray(velho)];
                Color novo = new Color(cor, cor, cor);
                res.setRGB(i, j, novo.getRGB());
            }
        }

        return res;
    }
    
}
