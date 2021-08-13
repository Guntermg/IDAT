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
public class BOHEcinza {
    public BufferedImage BOHErun(BufferedImage img, int K) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        ColorSupport corConvert = new ColorSupport();
        int grayScale;
        
        //COMEÇO
        double min = 10000;
        double max = 0;
        int k = (int)K/2;
        int c, cumulativeH;
        float N = K*K;
        float CDF;
        int[] grayLevels = new int[256];
        int[][] pixelsMatrix = new int[img.getWidth()][img.getHeight()];
        int[][] pixelsMatrixAux = new int[img.getWidth()][img.getHeight()];

        Color cor, novo;


        //Copia imagem
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                cor = new Color(img.getRGB(i, j));
                grayScale = corConvert.convertRGBGray(cor);
                pixelsMatrix[i][j] = grayScale;
                pixelsMatrixAux[i][j] = pixelsMatrix[i][j];
            }
        }
        //Fim da cópia

        //Aplica o filtro
        for (int i = k; i < img.getWidth() - k; i++) {
            //System.out.println("for do pixel i " + i);
            for (int j = k; j < img.getHeight() - k; j++) {
                //System.out.println("for do pixel j " + j);
                //System.out.println("Tamanho da imagem " + img.getWidth() + "x" + img.getHeight());

                //Reinicializa as variáveis para cada pixel
                CDF = 0.0f;
                for (int l = 0; l < 256; l++) {
                    grayLevels[l] = 0;
                }

                //System.out.println("Iniciando o calculo do valor de CDF (JANELA)");
                //Para cada i, j uma janela (RC) é calculada
                for (int w1 = i-k; w1 < i + k; w1++) {
                    //System.out.println("w1 " + w1);
                     for (int w2 = j-k; w2 < j + k; w2++) {
                         //System.out.println("w2 " + w2);
                           //Calculando o CDF
                           //Conta a quantidade de vezes que uma determinada intensidade de cinza aparece na janela
                           grayLevels[pixelsMatrix[w1][w2]]++;
                     }
                }

                //Soma e divide pelo número de pixels na RC para terminar o cálculo do CDF cumulativo
                cumulativeH = 0;
                //Cálculo do CDF cumulativo, soma os valores até o nível de cinza do pixel atual
                for (int l = 0; l < pixelsMatrix[i][j]; l++) {
                    cumulativeH += grayLevels[l];
                }
                CDF = (float)cumulativeH/N;

                //Encontra os valores de cinza máximo e mínimo da RC
                //Min
                for (int l = 0; l < 256; l++) {
                    if (grayLevels[l] != 0) {
                        min = l;
                        break;
                    }
                }
                //Max
                for (int l = 255; l >= 0; l--) {
                    if (grayLevels[l] != 0) {
                        max = l;
                        break;
                    }
                }

                //Calcula o novo valor do pixel central de acordo com a fórmula do BOHE
                //System.out.println("Valor CDF " + CDF +" "+ min +" "+ max);
                float aux = (float)(max-min)*CDF;
                c = (int) (min + (int)aux);
                pixelsMatrixAux[i][j] = c;

                //Color cor2 = new Color(img.getRGB(i, j));
                //System.out.println("Valor antigo " + cor2.getGreen());
                //System.out.println("Valor novo " + novo.getGreen());
                //System.out.println("Terminou a conta do novo valor do pixel e salvou na imagem de saida");       
            }
        }

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                c = pixelsMatrixAux[i][j];
                novo = new Color(c, c, c);
                int rgb = novo.getRGB();
                res.setRGB(i, j, rgb);
            }
        }

        return res;
    } 
}
