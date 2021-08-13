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
public class BOHEcolor {
    public BufferedImage BOHEColorRun(BufferedImage img, int K) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        //COMEÇO
        double min = 10000;
        double max = 0;
        int k = (int)K/2;
        int c, cumulativeH;
        float N = K*K;
        float CDF;
        int[] grayLevels = new int[256];
        int Y = 0, Cr = 1, Cb = 2; 
        double[][][] pixelsMatrix = new double[img.getWidth()][img.getHeight()][3];
        double[][][] pixelsMatrixAux = new double[img.getWidth()][img.getHeight()][3];

        Color cor, novo;
        //Y = (0.257 * R) + (0.504 * G) + (0.098 * B) + 16;
        //Cr = (0.439 * R) - (0.368 * G) - (0.071 * B) + 128;
        //Cb = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128;

        //Copia imagem e converte para YCrCb
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                cor = new Color(img.getRGB(i, j));
                //Contas para a conversão
                pixelsMatrix[i][j][Y] = (0.257 * cor.getRed()) + (0.504 * cor.getGreen()) + (0.098 * cor.getBlue()) + 16;
                pixelsMatrix[i][j][Cr] = (0.439 * cor.getRed()) - (0.368 * cor.getGreen()) - (0.071 * cor.getBlue()) + 128;
                pixelsMatrix[i][j][Cb] = -(0.148 * cor.getRed()) - (0.291 * cor.getGreen()) + (0.439 * cor.getBlue()) + 128;

                //Deixei apenas para mostrar que não ocorre o estouro
                verificaEstouro(pixelsMatrix[i][j][Y]);
                verificaEstouro(pixelsMatrix[i][j][Cr]);
                verificaEstouro(pixelsMatrix[i][j][Cb]);

                //Cópia na matriz auxiliar
                pixelsMatrixAux[i][j][Y] = pixelsMatrix[i][j][Y];
                pixelsMatrixAux[i][j][Cr] = pixelsMatrix[i][j][Cr];
                pixelsMatrixAux[i][j][Cb] = pixelsMatrix[i][j][Cb];
            }
        }
        //Fim da cópia

        //Aplica o filtro
        for (int i = k; i < img.getWidth() - k; i++) {
            for (int j = k; j < img.getHeight() - k; j++) {
                //Reinicializa as variáveis para cada pixel
                CDF = 0.0f;
                for (int l = 0; l < 256; l++) {
                    grayLevels[l] = 0;
                }

                //Para cada i, j uma janela (RC) é calculada
                for (int w1 = i-k; w1 < i + k; w1++) {
                     for (int w2 = j-k; w2 < j + k; w2++) {
                           //Calculando o CDF
                           //Conta a quantidade de vezes que uma determinada intensidade de cinza aparece na janela
                           grayLevels[(int)pixelsMatrix[w1][w2][Y]]++;
                     }
                }

                //Soma e divide pelo número de pixels na RC para terminar o cálculo do CDF cumulativo
                cumulativeH = 0;
                //Cálculo do CDF cumulativo, soma os valores até o nível de cinza do pixel atual
                for (int l = 0; l < (int)pixelsMatrix[i][j][Y]; l++) {
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
                float aux = (float)(max-min)*CDF;
                c = (int) (min + (int)aux);

                pixelsMatrixAux[i][j][Y] = c;    
            }
        }

        //Copia os valores da matriz para a imagem de saída convertendo de volta para RGB
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                novo = convYCrCbtoRGB(pixelsMatrixAux[i][j][Y], pixelsMatrixAux[i][j][Cr], pixelsMatrixAux[i][j][Cb]);
                int rgb = novo.getRGB();
                res.setRGB(i, j, rgb);
            }
        }
        return res;
    } 

    public boolean verificaEstouro (double value) {
        if (value > 240.0 || value < 16.0){
            System.out.println("Valor errado encontrado!!!");
            return false;
        }
        return true;
    }

    public Color convYCrCbtoRGB(double Y, double Cr, double Cb){
        int r,g,b;
        //Aplica as fórmulas para conversão
        r = (int) (1.164*(Y - 16) + 1.596*(Cr - 128));
        g = (int) (1.164*(Y - 16) - 0.813*(Cr - 128) - 0.391*(Cb - 128));
        b = (int) (1.164*(Y - 16) + 2.018*(Cb - 128));

        //Verifica e corrige possíveis estouros de valor
        if (b > 255) {b=255;}

        if (b < 0) {b=0;}

        if (g > 255) {g=255;}

        if (g < 0) {g=0;}

        if (r > 255) {r=255;}

        if (r < 0) {r=0;}

        Color rgb = new Color(r, g, b);
        return rgb;
    }

}


