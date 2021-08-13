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
public class BBOBMOcinza {
    public BufferedImage bbobmoRun(BufferedImage img) {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    
    int globalAverage = this.getGlobalAverage(img.getWidth(), img.getHeight(), img);
    Color colorGlobal = new Color(globalAverage, globalAverage, globalAverage);
    
    double a = 1;
    double b = 1;
    double c = 1;
    double k = 0.5;
    
    for(int i = 0; i<img.getWidth(); i++) {
    	for(int j = 0; j<img.getHeight(); j++) {
    		
    		Color aux = new Color(img.getRGB(i, j));
    		Color aux2 = new Color(aux.getRed(), aux.getRed(), aux.getRed());
    		
    		int localMean = this.getLocalAverage(i, j, img);
    		Color colorAverage = new Color(localMean, localMean, localMean);
    		
    		int deviation = this.getDeviation(i, j, img);
    		Color colorDeviation = new Color(deviation, deviation, deviation);
    		
    		int opt = this.optimize(k, colorGlobal.getRed(), colorDeviation.getRed(), b);
    		Color colorOpt = new Color(opt, opt, opt);

    		
    		int enhance = (int) (colorOpt.getRed()*(aux2.getRed() - c*colorAverage.getRed())+Math.pow(colorAverage.getRed(), a));
    		
    		if(enhance < 0) {
    			Color colorEnhance = new Color(0, 0, 0);
    			res.setRGB(i, j, colorEnhance.getRGB());	}
    		if(enhance > 255) {
    			Color colorEnhance = new Color(255, 255, 255);
    			res.setRGB(i, j, colorEnhance.getRGB());	}
    		if(enhance >= 0 && enhance <= 255) {
    			Color colorEnhance = new Color(enhance, enhance, enhance);
    			res.setRGB(i, j, colorEnhance.getRGB());	}
    			
    		}
    }    
    return res;
}


	public int getLocalAverage(int x, int y, BufferedImage img){
		
		int mean = 0;
		
		for(int i = x-1; i <= x+1 ; i++){
			for( int j = y-1; j <= y+1; j++){
				if((x-1)<1 || 
				   (y-1)<1 || 
				   (x+1)>=img.getWidth() || 
				   (y+1)>=img.getHeight() ) {
						Color aux = new Color(img.getRGB(x, y));
						mean += aux.getRed(); 
		//				mean += aux.getBlue();
		//				mean += aux.getGreen();
				}
				else {
					Color aux = new Color(img.getRGB(i, j));
					mean += aux.getRed();
		//			mean += aux.getBlue();
		//			mean += aux.getGreen();	This created a really neat style effect
				}
			}
		}
		mean /= 9 ;
		return mean;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getDeviation( int x, int y, BufferedImage img){

		int dev = 0;
		
		for(int i=x-1; i<=x+1; i++){
			for(int j=y-1; j<=y+1; j++){
				int localMean = this.getLocalAverage(x, y, img);
				Color colorMean = new Color(localMean, localMean, localMean);
				
				if((x-1)<1 || 
				   (y-1)<1 || 
				   (x+1)>=img.getWidth() || 
				   (y+1)>=img.getHeight() ) {
						Color aux = new Color(img.getRGB(x, y));
						dev += Math.pow( aux.getRed() - colorMean.getRed(), 2 );	}
				
				else {
					Color aux = new Color(img.getRGB(i, j));
					dev += Math.pow( aux.getRed() - colorMean.getRed(), 2 );	}
				
			}
		}
		dev = (int) Math.sqrt(dev/9);
		return dev;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public int getGlobalAverage( int x, int y, BufferedImage img){

		int D = 0;
		Color aux;
		
		for( int i=0; i < img.getWidth(); i++){
			for( int j=0; j < img.getHeight(); j++){
				aux = new Color(img.getRGB(i, j));
				D += aux.getRed();	}
		}
		D /= ( img.getWidth()*img.getHeight() );
		return D;
    }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////

    public int optimize(double k, int globalMean, int localDeviation, double b){
		return (int) ((k*globalMean)/(localDeviation+b));	
    }
	
    
}
