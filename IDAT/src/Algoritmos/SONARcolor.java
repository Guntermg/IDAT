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
public class SONARcolor {
    public BufferedImage SONARrun(BufferedImage img, double K) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    
        int nivelY;
        int n=240;
        int hist[] = new int[240];
        int novoY[] = new int[240];
        float alfa=(float) K;
        float numPixels= (float) 0.0;
        float prob[]= new float[240];  

        float somaProb[]= new float[240];

        for (int i = 0; i<240; i++){
            hist[i]=0;
            prob[i]=0;
            somaProb[i]=0;
        }

        //Histograma (Fórmula (20))
        //Pega os valores de Y na imagem e determinar seu histograma 
        for(int linha = 0; linha<img.getWidth(); linha++){
            for(int coluna = 0; coluna < img.getHeight(); coluna++){

                Color c = new Color( img.getRGB( linha, coluna ) );

                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();

                    //Conversão pegando apenas o canal de Y
                    nivelY=(int)((0.2568*r + 0.5041*g + 0.0979*b) +16);       

                    hist[nivelY]++;

            }
        }

        numPixels = img.getWidth()*img.getHeight();


        //primeiro valor 16 até 240

        for(int i=16;i<240;i++){
            prob[i]= (float) (hist[i]/numPixels);
        }

        for(int i=17; i<240;i++){
            somaProb[i]+= somaProb[i-1]+prob[i-1];    
        }

        int u;
        //Formula (21) e (22)
        for(u=17; u<=(n-2); u++){
            novoY[u]= (int)(u+alfa*(n*somaProb[u]-u));        
        }

        for(int linha = 0; linha<img.getWidth(); linha++){
            for(int coluna = 0; coluna < img.getHeight(); coluna++){
                Color p = new Color(img.getRGB(linha, coluna));

                int r = p.getRed();
                int g = p.getGreen();
                int b = p.getBlue();


                //Obtem os valores de YCrCb para posteriormente fazer conversão com os novos valores de Y 
                int Y=(int)((0.2568*r + 0.5041*g + 0.0979*b) +16);
                float Cr =(float)((0.4392*r + -0.3678*g + -0.0714*b)+128); 
                float Cb =(float) ((-0.1482*r + -0.2910*g + 0.4392*b)+128);

                //Novos valores de Y são passados aos novos canais de RGB
                int red= (int)((1.164*(novoY[Y] - 16)) + (1.596*(Cr - 128)));
                int green = (int)( (1.164*(novoY[Y] - 16)) - (0.813*(Cr - 128)) - (0.391*(Cb - 128)));
                int blue = (int)((1.164*(novoY[Y] - 16)) + (2.018*(Cb - 128))); 


                //Valores que extrapolam o intervalo do canal são reajustados 
                if(red>255){
                    red=255;
                }
                if(red<0){
                    red=0;
                }
                if(green>255){
                    green=255;
                }
                if(green<0){
                    green=0;
                }

                if(blue>255){
                    blue=255;
                }
                if(blue<0){
                    blue=0;
                }          

                Color novo = new Color(red,green,blue);
                res.setRGB(linha, coluna, novo.getRGB());
            } 
        }

        return res;

    }
    
}
