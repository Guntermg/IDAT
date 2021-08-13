/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import Support.ColorSupport;
import Support.Converte;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gunter M. Gaede
 */
public class BHE2PLcolor {
    public BufferedImage BHE2PLColorRun(BufferedImage img) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		// COME�O NORMALIZA��O
		int histograma[] = new int[256];
		int histogramaAux[] = new int[256];
		int histograma1[] = new int[256];
		int histograma2[] = new int[256];
		int histogramaEqualizado1[] = new int[256];
		int histogramaEqualizado2[] = new int[256];
		int nivelEqualizado1[] = new int[256];
		int nivelEqualizado2[] = new int[256];
		float novoNivel = 0;
		float soma1 = 0;
		float soma2 = 0;
		float cont = 0;
		float sp = 0;
		float spl = 0;
		float spu = 0;
		float grl1 = 0;
		float grl2 = 0;
		float gru1 = 0;
		float gru2 = 0;
		float dl = 0;
		float du = 0;
		float pll1 = 0;
		float pll2 = 0;
		float plu1 = 0;
		float plu2 = 0;
		float pkl = 0;
		float pku = 0;
		float p1 = 0;
		float p2 = 0;

		// Inicializa os vetores dos histogramas
		for (int i=0; i<256; i++) {
			histograma[i] = 0;
			histograma1[i] = 0;
			histograma2[i] = 0;
			histogramaAux[i] = 0;
			nivelEqualizado1[i] = 0;
			nivelEqualizado2[i] = 0;
			histogramaEqualizado1[i]=0;
			histogramaEqualizado2[i]=0;
		}

		Converte conversao[][] = new Converte[res.getWidth()][res.getHeight()];

		Converte a = new Converte();
		for (int linha = 0; linha < res.getWidth(); linha++) {
			for (int coluna = 0; coluna < res.getHeight(); coluna++) {

				Color rgb = new Color(img.getRGB(linha, coluna));
				conversao[linha][coluna] = a.rgbParaYiq(rgb);	
			}
		}


		// Gera o Histograma
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				//Color x = new Color(img.getRGB(i, j));
				//int cor = x.getY();

				int cor = (int) (conversao[i][j].getY() + 0.5);

				histograma[cor]++;
			}
		}

		// Calcula a quantidade de pixels
		cont = img.getWidth()*img.getHeight();


		// Calcula a m�dia SP do histograma da imagem original
		for (int i = 0; i < 256; i++) {
			sp += (histograma[i]/cont) * i;
		}

		for (int i=0; i<256; i++) {
			if(i<=(int)sp) {
				histograma1[i] = histograma[i];
			}
			else {
				histograma2[i] = histograma[i];
			}

			soma1 += histograma1[i];
			soma2 += histograma2[i];

		}

		for (int i = 0; i < 256; i++) {
			spl += (histograma1[i]/soma1) * i;
		}

		for (int i = 0; i < 256; i++) {
			spu += (histograma2[i]/soma2) * i;
		}

		grl1 = (sp - spl)/(sp);


		if (grl1 > 0.5) {
			dl = (1 - grl1)/2;
		}
		else if (grl1 <= 0.5) {
			dl = grl1/2;
		}

		grl2 = grl1 + dl;

		gru1 = (255 - spu)/(255 - sp);


		if (gru1 > 0.5) {
			du = (1 - gru1)/2;
		}
		else if (gru1 <= 0.5) {
			du = gru1/2;
		}

		gru2 = gru1 + du;


		for(int i=0; i<=(int)sp; i++) {
			if (pkl < histograma1[i]) {
				pkl = histograma1[i];
			}
		}

		for(int i=(int)sp+1; i<256; i++) {
			if (pku < histograma2[i]) {
				pku = histograma2[i];
			}
		}

		pll1 = grl1 * pkl;
		pll2 = grl2 * pkl;
		plu1 = gru1 * pku;
		plu2 = gru2 * pku;

		for (int i=0; i<=(int)sp; i++) {
			if (histograma1[i] <= pll2) {
				histograma1[i] = (int) pll1;
			}
			else if (histograma1[i] > pll2) {
				histograma1[i] = (int) pll2;
			}
		}

		for (int i=(int)sp+1; i<256; i++) {
			if (histograma2[i] <= plu2) {
				histograma2[i] = (int) plu1;
			}
			else if (histograma2[i] > plu2) {
				histograma2[i] = (int) plu2;
			}
		}

		soma1 = 0;
		for(int i=0; i<=(int)sp; i++) {
			soma1 += histograma1[i];
		}

		for(int i=0; i<=(int)sp; i++) {
			p1 += (histograma1[i]/(float)soma1);
			novoNivel = (int)(sp * p1);
			nivelEqualizado1[i] = (int) novoNivel;
			histogramaEqualizado1[(int) novoNivel] = histograma1[i];

		}

		novoNivel = 0;

		soma2 = 0;
		for(int i=(int)sp+1; i<256; i++) {
			soma2 += histograma2[i];
		}

		for(int i=(int)sp+1; i<256; i++) {
			p2 += (histograma2[i]/(float)soma2);
			novoNivel = (int) ((sp+1) + (255 - (sp+1)) * p2);
			nivelEqualizado2[i] = (int) novoNivel;
			histogramaEqualizado2[(int)novoNivel] = histograma2[i];
		}

		for (int linha = 0; linha < res.getWidth(); linha++) {
			for (int coluna = 0; coluna < res.getHeight(); coluna++) {
				
			}
		}

		// FINAL NORMALIZA��O
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				
				Color rgb = new Color(0);
				rgb = conversao[i][j].yiqParaRgb(conversao[i][j]);	
				
				
				int atual = (int) (a.getY() + 0.5);
				int cor = 0;
				int cor1 = 0;

				if (atual <= sp) {
					cor = nivelEqualizado1[atual];
				}
				else if(atual > sp) {
					cor = nivelEqualizado2[atual];
				}
				res.setRGB(i, j, rgb.getRGB());
			}
		}

		return res;
	}
}
