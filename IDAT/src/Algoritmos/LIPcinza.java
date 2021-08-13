package Algoritmos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

// Java program to demonstrate working 
// of java.lang.Math.log() method 
import java.lang.Math; 

/**
 * Classe para os métodos(funcoes) para o calculo do Greenness, onde serão mantidos
 * , que serao usadas pelo resto do programa.
 * 
 * @author Flavia Mattos & Arthur Costa
 */
public class LIPcinza {

/**
 * //Setando os valores iniciais:
	m_pdimadlgrealcelip.m_alfa="0.1";
	m_pdimadlgrealcelip.m_beta="4";
	m_pdimadlgrealcelip.m_tamjanela=3;
        * 
 * @param img A imagem onde o filtro será aplicado
     * @param ratio //Parâmetro que destaca áreas brilhantes (alfa>1) e áreas escuras (alfa<1).
     * @param v //Parâmetro que encrespa os cantos (aumenta a nitidez).
 * @return retorna a imagem depois de aplicado o filtro
     * @throws java.io.IOException
 */ 
    

public BufferedImage LIPrun(BufferedImage img, float v, float ratio) throws IOException {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //imagem de saída
    
    						//Esta é uma função membro da classe CPdiBase
	int col,lin;			//Contadores de linhas e colunas.
	int M=256;				//Já que se está trabalhando com níveis de cinza, existem 256 níveis.
	float alfa= v ;		//Parâmetro que destaca áreas brilhantes (alfa>1)
							//e áreas escuras (alfa<1). alfa="0.1";
	float beta=  ratio;		//Parâmetro que encrespa os cantos (aumenta a nitidez) beta="4"
//	double media;			//Média em relação a função em tom de cinza normalizada.
//	double media_intensidade;//Média da intensidade (imagem original).
//	int n = P_tamjanela;		//A janela possui tamanho igual a (n*n).
//	BYTE * lpTemp = lpBits; //Variável que varre a imagem original.		
//	double * pBuffer;		//Corresponde à imagem após a aplicação dos logaritmos;
//	pBuffer=(double *)malloc(GetHeight()*bmWidthBytes*sizeof(double));
//	double * t_Buffer=pBuffer;//Aponta para o início da imagem após a aplicação dos logs.
//	double t_ajuste;			//Temporária que ajusta valores maiores que 255 e menores que 0.

	//Armazenando o tamanho da imagem
	int Largura = img.getWidth();// Largura da imagem
	int Altura = img.getHeight();// Altura da imagem
	
	double media;			//Média em relação a função em tom de cinza normalizada.
	double media_intensidade;       //Média da intensidade (imagem original).
	int n = 3; //P_tamjanela;	//A janela possui tamanho igual a (n*n).
	int n_2 = n/2;		        //calculo de n/2.
	int n_quad = n*n;		//calculo de n*n.
	int cinza ;
	double valor1,valor2;    //Temporária variavel double.
	double newGray;                 //variavel double.
	double t_ajuste;		//Temporária que ajusta valores maiores que 255 e menores que 0.

	double[][] pBufferlog = new double[Altura][Largura]; //Cria um PONTEIRO para armazenar a aplicação dos logs.
	double[][] pBufferProcLinear = new double[Altura][Largura]; //Cria um PONTEIRO para armazenar Processamento Linear
	
// System.out.println("alfa: " + alfa + ", " + "beta: " + beta);
        
	for( lin = 0; lin < Altura; lin++) 
		{
		for( col = 0; col < Largura; col++) 
		{
                        Color x = new Color(img.getRGB(col,lin));
			cinza = (int)((x.getGreen() + x.getRed() + x.getBlue())/3);
			valor1 = 1.0 -((double)cinza/(double)M);    //Bloco 1 da FIGURA 1.
                        //System.out.println("cinza: " + cinza + "valor1: " + valor1);
                        valor2 = Math.log(valor1);	//Bloco 2 da FIGURA 1.
                        //System.out.println("valor1: " + valor1 + "valor2: " + valor2);
			pBufferlog[lin][col]=valor2;	//Bloco 2 da FIGURA 1.
		}
	}
				
						
	//Bloco 3 da FIGURA 1 - Processamento Linear.
	//A imagem é percorrida e é calculada a média em relação à função em tom de cinza
	//normalizada no ponto (lin,col).
	//As bordas são desprezadas.
	for(lin=n_2; lin<  Altura-n_2;lin++)
	{
		for(col=n_2; col < Largura -n_2;col++)
		{
			//System.out.println("Pixellog: " + pBufferlog[lin][col]);
                        //Fórmula 12:
			media=0;
			//media_intensidade=0;
			for (int t_lin=lin-(n_2);t_lin<=lin+(n_2);t_lin++)
				for(int t_col=col-(n_2);t_col<=col+(n_2);t_col++)
				{
					media += (double)pBufferlog[t_lin][t_col];
				}
			media /= (double)n_quad;
			//Fim da Fórmula 12.
			
                        valor1 = alfa*media + beta*(pBufferlog[lin][col]-media); //Fórmula 11
//System.out.println("media: " + media + ", " + "valor1: " + valor1);
			pBufferProcLinear[lin][col]= valor1; //Fórmula 11
			
		}
	}


	//Blocos 4 e 5- Retornando à escala normal.
	//pBuffer=t_Buffer;		//Apontando para o início do buffer.
	//lpTemp=lpBits;			//Apontando para o início do buffer.
	for(lin=n_2; lin<  Altura-n_2;lin++)
	{
		for(col=n_2; col < Largura -n_2;col++)
		{
			valor1 = pBufferProcLinear[lin][col];
                        valor2 = Math.exp((double)valor1); //Bloco 4 da FIGURA 1
                       // System.out.println("PixelProcLinear: " + valor1 + " EXp(valor2) " + valor2);
			t_ajuste = M*(1-valor2);
//System.out.println("PixelProcLinear: " + valor1 + " EXp(valor2) " + valor2 + " t_ajuste " + t_ajuste);
			//Bloco 5 da FIGURA 1:
			if(t_ajuste < 0)                newGray = 0;  
			else if(t_ajuste > 255)         newGray = 255;                  
                             else                       newGray = t_ajuste;
			
                    Color setGray = new Color((int)newGray, (int)newGray, (int)newGray);
                    res.setRGB(col,lin, setGray.getRGB());
//System.out.println("Novo nivel de cinza: " + setGray.getBlue());

//System.out.println("Pixel " + i + ", " + j + " = " + newGray);
        }

    }
    return res;
}
//Realce pelo modelo LIP
   
}
