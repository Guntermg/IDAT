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
public class ISIHEcinza {
    public BufferedImage ISIHErun(BufferedImage img) {
	 // Par�metro de Limiar de Ilumina��o It{
        double it;

        //calculo de Par�metro de Limiar de Ilumina��o It{
        //criacao da imagem  media e calculo do numerador de it{
        BufferedImage media = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());//imagem da media
        ColorSupport corConvert = new ColorSupport();
        int grayScale;

            for(int j=1  ; j<img.getWidth()-1; j++) { 
                    for(int soma=0 , i=1 ; i<img.getHeight()-1 ; i++) {

                            for(int col=j-1; col<=j+1; col++) {
                                    for(int lin=i-1; lin<=i+1; lin++) {
                                            Color x = new Color(img.getRGB(col , lin));
                                    //soma += (int)x.getRed();//soma todos os valores em uma mascara 3x3
                                    soma += (int)corConvert.convertRGBGray(x);//soma todos os valores em uma mascara 3x3
                            }
                            }

                            soma = (int)soma/9;//valor da media dos pixel vizinhos mascara 3x3
                            Color x = new Color(soma,soma,soma);
                            media.setRGB(j,i,x.getRGB());
                            soma=0;
                    }
            }
            //preenche as fronteiras da imagem media{
            for(int j=1  ; j<img.getWidth()-1; j++) { 
                    Color x = new Color(media.getRGB(j , 1));
                    media.setRGB( j,0, x.getRGB());
            }
            for(int i=0  ; i<img.getHeight()-1; i++) { 
                    Color x = new Color(media.getRGB( img.getWidth()-2 , i));
                    media.setRGB(img.getWidth()-1 ,i, x.getRGB());
            }
            for(int j=1  ; j<img.getWidth(); j++) { 
                    Color x = new Color(media.getRGB(j , img.getHeight()-2 ));
                    media.setRGB(j, img.getHeight()-1, x.getRGB());
            }
            for(int i=0  ; i<img.getHeight(); i++) { 
                    Color x = new Color(media.getRGB(1, i));
                    media.setRGB(0 ,i, x.getRGB());
            }
            //}preenche as fronteiras da imagem media

            //calculo do numerador{
            double numerador=0;

            for(int j=0;j<img.getWidth();j++) {
                    for(int i=0;i<img.getHeight();i++) {
                            Color x = new Color(media.getRGB(j,i));
                            Color central = new Color(img.getRGB(j , i));
                            //numerador+= Math.pow(central.getRed()-x.getRed(),2);
                            grayScale = corConvert.convertRGBGray(x);
                            numerador+= Math.pow(central.getRed()-grayScale,2);
                    }
            }
            //}calculo do numerador


            //}criacao da imagem  media e calculo do numerador de it

            //calcula denominador{

            double soma=0;
            double diferenca=0;
            double denominador=0;

            for(int j=1  ; j<img.getWidth()-1 ; j++) { 
                    for(int i=1 ; i<img.getHeight()-1; i++) {

                            for(int col=j-1; col<=j+1; col++) {
                                    for(int lin=i-1; lin<=i+1; lin++) {
                                            Color central = new Color(img.getRGB(j,i));
                                            Color x = new Color(img.getRGB(col , lin));
                                            grayScale = corConvert.convertRGBGray(x);
                                            diferenca += Math.pow( (central.getRed() - grayScale ),2);
                            }
                            }	
                            if((i==1 && j==1) || (i==1 && j==img.getWidth()-2) || (i==img.getHeight()-2 && j==img.getWidth()-2) || (i==img.getHeight()-2 && j==1))
                                    soma += 4* (diferenca/9);//incrementa a (diferenca do pixel central (i,j) pelo seu pixel vizinho (lin,col))^2
                            else if(i==1 || i==img.getHeight()-2 || j==1 || j==img.getWidth()-2)
                                    soma += 2* (diferenca/9);//incrementa a (diferenca do pixel central (i,j) pelo seu pixel vizinho (lin,col))^2
                            else
                                    soma += (diferenca/9);//incrementa a (diferenca do pixel central (i,j) pelo seu pixel vizinho (lin,col))^2
                            denominador+=soma;
                            diferenca=0;
                            soma=0;
                    }
            }


            //}calcula denominador
             it=(numerador/(denominador)); 
            //System.out.printf("%d   ",(int)(it*256));
            //}calculo de Par�metro de Limiar de Ilumina��o It



        //Histograma original
        double original[] = new double[256];//histograma original da imagem
        for (int i = 0 ;i<256; i++)
            original[i]=0;


        for (int j = 0; j < img.getWidth(); j++) {
            // for acima e abaixo servem para preencher os valores do histograma original
            for (int i = 0; i < img.getHeight(); i++) {
                    Color x = new Color(img.getRGB(j,i));
                    grayScale = corConvert.convertRGBGray(x);
                    original[grayScale] += 1;
            }
        }

        //Histograma Plower
        int sizeLow=(int)((256*it)+1);//tamanho do vetor Plower, quantidade de niveis de cinza de Plower
        double Plower[] = new double[sizeLow];//parte do histograma mais escuro (0 ate it)
        for (int i = 0 ;i<sizeLow; i++)
            Plower[i]=original[i];

        //Histograma Pupper
        int sizeUpp=(int)(256-(it*256));//tamanho do vetor Pupper, quantidade de niveis de cinza de Pupper
        double Pupper[] = new double[sizeUpp];//parte do histograma mais claro (it+1 ate 255)
        for (int j=0,i = sizeLow ;i<256; j++,i++)
            Pupper[j]=original[i];


        //calculos de Probabilidade simples{

        //probabilidade de Plower{
        int NPixLow=0;//Numero de pixels totais de Plower
        for (int i = 0 ;i<sizeLow; i++)
            NPixLow += Plower[i];

        double probabilidadeLow[] = new double[sizeLow];//vetor que guarda a probabilidade de Plower
        for (int i=0 ;i<sizeLow; i++)
            probabilidadeLow[i] = (Plower[i]/NPixLow);///Npixels de determinado nivel de cinza / Npixels totais de Plower
        //}probabilidade de Plower

        //probabilidade de Pupper{
        int NPixUpp=0;//Numero de pixels totais de Plower
        for (int i=0 ;i<sizeUpp; i++)
            NPixUpp += Pupper[i];

        double probabilidadeUpp[] = new double[sizeUpp];//vetor que guarda a probabilidade de Pupper
        for (int i = 0 ;i<sizeUpp; i++)
            probabilidadeUpp[i] = (Pupper[i]/NPixUpp);///Npixels de determinado nivel de cinza / Npixels de cinza de Pupper
        //}probabilidade de Pupper

        //}calculos de Probabilidade simples


        //calculos de Probabilidade acumulada{

        //Probabilidade acumulada de Plower{
        double CDFprobabilidadeLow[] = new double[sizeLow];
        CDFprobabilidadeLow[0]=probabilidadeLow[0];//caso inicial nivel de cinza 0
        for(int i=1;i<sizeLow;i++) 
            CDFprobabilidadeLow[i]=probabilidadeLow[i]+CDFprobabilidadeLow[i-1];

        //}Probabilidade acumulada de Plower

        //Probabilidade acumulada de Pupper{
        double CDFprobabilidadeUpp[] = new double[sizeUpp];
        CDFprobabilidadeUpp[0]=probabilidadeUpp[0];//caso inicial nivel de cinza 0
        for(int i=1;i<sizeUpp;i++) 
            CDFprobabilidadeUpp[i]=probabilidadeUpp[i]+CDFprobabilidadeUpp[i-1];

        //}Probabilidade acumulada de Pupper

        //}calculos de Probabilidade acumulada


        //calculos de NovoNivel{

        //novo nivel Plower
        double NovoNivelLow[] = new double[sizeLow];
        for(int i=0 ;i<sizeLow; i++)
            NovoNivelLow[i]=CDFprobabilidadeLow[i] * (sizeLow);

        //novo nivel Pupper
        double NovoNivelUpp[] = new double[sizeUpp];
        for(int i=0 ;i<sizeUpp; i++)
            NovoNivelUpp[i]=CDFprobabilidadeUpp[i] * (sizeUpp);

        //novo nivel juntando (novo nivel Plower) com (novo nivel Pupper)
        double NovoNivel[] = new double[256];
        for(int i=0 ;i<sizeLow; i++) {
            NovoNivel[i]=NovoNivelLow[i];
            //System.out.printf("%d) %f\n",i,NovoNivel[i]);
    }
        for(int j=0 ,i=sizeLow ;i<256; i++,j++) {
            NovoNivel[i]=NovoNivelUpp[j];
            //System.out.printf("%d) %f\n",i,NovoNivel[i]);
        }
      //}calculos de NovoNivel

      //resultado final apos juncao de histogramas{
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        //copiar imagem original na imagem de resultado
        for(int j=0; j<img.getWidth(); j++) {
            for(int i=0 ; i<img.getHeight() ; i++) {			
                Color x = new Color(img.getRGB(j,i));
                grayScale = corConvert.convertRGBGray(x);
                int corNova = (int) NovoNivel[grayScale]; 
                Color novo = new Color(corNova,corNova, corNova);
                res.setRGB( j, i, novo.getRGB());
            }
        }
        return res;


    }
}
