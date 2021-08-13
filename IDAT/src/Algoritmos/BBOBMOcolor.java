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
public class BBOBMOcolor {
    public BufferedImage bbobmoColorRun(BufferedImage img) {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
   
	int globalAverage = this.getGlobalAverage(img.getWidth(), img.getHeight(), img);
    
    double a = 1.02;
    double b = globalAverage/2;
    double c = 1;
    double k = 1.5;
    
    for(int i = 0; i<img.getWidth(); i++) {
    	for(int j = 0; j<img.getHeight(); j++) {
    		
    		int Y[] = this.convertRGBtoYCbCr( new Color( img.getRGB(i, j) ) );
    		
    		
    		Color aux = new Color( Y[0], Y[0], Y[0] );
    		
    		int localMean = this.getLocalAverage(i, j, img);
    		
    		int deviation = this.getDeviation(i, j, img);
    		
    		int opt = this.optimize(k, globalAverage, deviation, b);
    		Color optBW = new Color(opt, opt, opt);

    		
    		Y[0] = (int) (optBW.getRed()*(aux.getRed() - c*localMean)+Math.pow(localMean, a));
    		
    		if(Y[0] < 16) {
    			int[] lmao={16, Y[1], Y[2]};
    			Color colorEnhance = this.convertYCbCrToRGB( lmao );
    			res.setRGB(i, j, colorEnhance.getRGB());	}
    		if(Y[0] > 240) {
    			int[] lmao={240, Y[1], Y[2]};
    			Color colorEnhance = this.convertYCbCrToRGB( lmao );
    			res.setRGB(i, j, colorEnhance.getRGB());	}
    		if(Y[0] >= 16 && Y[0] <= 240) {
    			Color enhance = this.convertYCbCrToRGB(Y);
    			res.setRGB(i, j, enhance.getRGB());	}
    			
    		}
    }    
    return res;
}
    
    public int[] convertRGBtoYCbCr( Color rgb ) {
		
		int R = rgb.getRed();
		int G = rgb.getGreen();
		int B = rgb.getBlue();
		
		int Y = (int) ( 16 + (0.257*R) + (0.504*G) + (0.098*B) );
		int Cb = (int) ( 128 + (0.439*R) - (0.368*G) - (0.071*B) );
		int Cr = (int) ( 128 - (0.148*R) - (0.291*G) + (0.439*B) );
		
		if(Y>240) {
			Y = 240;	}
		if(Y<0) {
			Y = 0;	}
		if(Cb<16) {
			Cb = 16;	}
		if(Cb>236) {
			Cb = 236;	}
		if(Cr<16) {
			Cr = 16;	}
		if(Cr>240) {
			Cr = 240;	}
		
		int[] fox = {Y, Cb, Cr};

		return fox;	
    }
    
    public Color convertYCbCrToRGB( int yCbCr[] ) {
		
		int Y = yCbCr[0];
		int Cb = yCbCr[1];
		int Cr = yCbCr[2];
		
		int R = (int) ( 1.164*(Y-16) + 1.596*(Cr - 128) );
		int G = (int) ( 1.164*(Y-16) - 0.813*(Cr - 128) - 0.391*(Cb - 128) );
		int B = (int) ( 1.164*(Y-16) + 2.018*(Cb - 128) );
		
		if(R>255) {
			R = 255;	}
		if(R<0) {
			R = 0;	}
		if(B>255) {
			B = 255;	}
		if(B<0)	{
			B = 0;	}
		if(G>255) {
			G = 255;	}
		if(G<0) {
			G = 0;	}
		
		return new Color(B,G,R);
	}

	public int getLocalAverage(int x, int y, BufferedImage img){
		
		int yaw = 0;
		
		for(int i = x-1; i <= x+1 ; i++){
			for( int j = y-1; j <= y+1; j++){
				if((x-1)<1 || 
				   (y-1)<1 || 
				   (x+1)>=img.getWidth() || 
				   (y+1)>=img.getHeight() ) {
						yaw += this.convertRGBtoYCbCr( new Color(img.getRGB(x, y)) )[0];
				}
				else {
					yaw += this.convertRGBtoYCbCr( new Color(img.getRGB(i, j)) )[0];
				}
			}
		}
		yaw /= 9;
		return yaw;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getDeviation( int x, int y, BufferedImage img){

		int yaw = 0;
		int dev = 0;
		
		for(int i=x-1; i<=x+1; i++){
			for(int j=y-1; j<=y+1; j++){
				int localMean = this.getLocalAverage(x, y, img);
				
				if((x-1)<1 || 
				   (y-1)<1 || 
				   (x+1)>=img.getWidth() || 
				   (y+1)>=img.getHeight() ) {
						yaw = this.convertRGBtoYCbCr( new Color(img.getRGB(x, y)) )[0];
						dev += Math.pow(yaw - localMean, 2);
				}
				
				else {
					yaw = this.convertRGBtoYCbCr( new Color(img.getRGB(i, j)) )[0];
					dev += Math.pow( yaw - localMean, 2);
				}
				
			}
		}
		dev = (int) Math.sqrt(dev/9);
		
		return dev;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public int getGlobalAverage( int x, int y, BufferedImage img){

		int yaw = 0;
		
		for( int i=0; i < img.getWidth(); i++){
			for( int j=0; j < img.getHeight(); j++){ 
				yaw += this.convertRGBtoYCbCr( new Color(img.getRGB(i, j)) )[0];
			}
		}
		yaw /= ( img.getWidth()*img.getHeight() );
		
		return yaw;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////

    public int optimize(double k, int globalMean, int localDeviation, double b){
		int opt = (int) ((k*globalMean)/(localDeviation+b));
		
		if(opt>255) {
			opt = 255;	}
		if(opt<0) {
			opt = 0;	}
		
		return opt;
		
	}
	
    
}
