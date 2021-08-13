/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import Support.ColorSupport;
import Support.ConverteISIHE;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gunter M. Gaede
 */
public class ISIHEcolor {
    public BufferedImage ISIHEColorRun(BufferedImage img) {
	 // Par�metro de Limiar de Ilumina��o It{
        double it;

        //criacao da matriz yiq{
        ConverteISIHE [][]yiq= new ConverteISIHE[img.getWidth()][img.getHeight()];

        for(int j=0;j<img.getWidth();j++) {
                    for(int i=0;i<img.getHeight();i++) {

                            Color x = new Color(img.getRGB(j , i));
                            ConverteISIHE c = new ConverteISIHE();
                            c.ConverteRGBparaYIQ(x.getRed(),x.getGreen(),x.getBlue());
                            yiq[j][i]=c;

                    }
        }
        //}criacao da matriz yiq
        //calculo de Par�metro de Limiar de Ilumina��o It{



        //criacao da imagem  media e calculo do numerador de it{
        //matriz da media do brilho
        double [][]yiqMedia= new double[img.getWidth()][img.getHeight()];
            for(int j=1  ; j<img.getWidth()-1; j++) { 
                    for(int soma=0 , i=1 ; i<img.getHeight()-1 ; i++) {

                            for(int col=j-1; col<=j+1; col++) {
                                    for(int lin=i-1; lin<=i+1; lin++) {		
                                    soma += yiq[j][i].getY();//soma todos os valores em uma mascara 3x3
                            }
                            }

                            soma = soma/9;//valor da media dos pixel vizinhos mascara 3x3
                            yiqMedia[j][i]=soma;
                            soma=0;
                    }
            }
            //preenche as fronteiras da imagem media{
            for(int j=1  ; j<img.getWidth()-1; j++) { 
                    yiqMedia[j][0]=yiqMedia[j][1];
            }
            for(int i=0  ; i<img.getHeight()-1; i++) { 
                    yiqMedia[img.getWidth()-1][i]=yiqMedia[img.getWidth()-2][i];
            }
            for(int j=1  ; j<img.getWidth(); j++) { 
                    yiqMedia[j][img.getHeight()-1]=yiqMedia[j][img.getHeight()-2];
            }
            for(int i=0  ; i<img.getHeight(); i++) { 
                    yiqMedia[0][i]=yiqMedia[1][i];
            }
            //}preenche as fronteiras da imagem media

            //calculo do numerador{
            double numerador=0;

            for(int j=0;j<img.getWidth();j++) {
                    for(int i=0;i<img.getHeight();i++) {
                            double x = yiqMedia[j][i];
                            //System.out.printf("%f\n",yiq[j][i].getY());
                            double central = yiq[j][i].getY();
                            numerador+= Math.pow(central-x,2);
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
                                            double x = yiq[col][lin].getY();
                                            double central = yiq[j][i].getY();
                                            diferenca += Math.pow( (central - x ),2);
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
                    original[(int)yiq[j][i].getY()] += 1;
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
        double [][]yiqRes= new double[img.getWidth()][img.getHeight()];
        //copiar imagem original na imagem de resultado
        for(int j=0; j<img.getWidth(); j++) {
            for(int i=0 ; i<img.getHeight() ; i++) {			
                double novo=NovoNivel[(int)yiq[j][i].getY()];
                yiqRes[j][i]=novo;
            }
        }
        //cria imagem resultado final, realizando a conversao de yiq para rgb{
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        //matrz de objetos Converte,para transformar uma matriz yiq em rgb
        ConverteISIHE [][]rgbRes= new ConverteISIHE[img.getWidth()][img.getHeight()];
        double yRes, iRes, qRes;
        int rRes, gRes, bRes;
            for(int j=0;j<img.getWidth();j++) {
                    for(int i=0;i<img.getHeight();i++) {
                            yRes=yiqRes[j][i];
                            iRes=yiq[j][i].getI();
                            qRes=yiq[j][i].getQ();
                            ConverteISIHE c = new ConverteISIHE();
                            c.ConverteYIQparaRGB((int)yRes,(int)iRes,(int)qRes);
                            rgbRes[j][i]=c;
                            rRes=(int)rgbRes[j][i].getY();
                            gRes=(int)rgbRes[j][i].getI();
                            bRes=(int)rgbRes[j][i].getQ();
                            Color novo = new Color(rRes, gRes, bRes);
                            res.setRGB(j, i, novo.getRGB());
                    }
        }
        //System.out.println("a");
        return res;


    }
}
